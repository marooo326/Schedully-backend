package schedully.schedully.auth.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import schedully.schedully.auth.domain.RefreshToken;
import schedully.schedully.auth.dto.JwtToken;
import schedully.schedully.auth.domain.CustomUserDetails;
import schedully.schedully.auth.repository.RefreshTokenRepository;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key secret;
    private final Date accessExpirationDate;
    private final Date refreshExpirationDate;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        this.accessExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 30); // 30분
        this.refreshExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3); // 3일
        this.refreshTokenRepository = refreshTokenRepository;   // 생성자 주입
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.secret = Keys.hmacShaKeyFor(secretByteKey);
    }

    public JwtToken generateToken(Authentication authentication, String memberId, Long scheduleId) {
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(generateAccessToken(authentication, memberId, scheduleId))
                .refreshToken(generateRefreshToken(memberId))
                .build();
    }

    public String generateAccessToken(Authentication authentication, String memberId, Long scheduleId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.info("memberId: " + memberId + ", Auth: " + authorities);

        return Jwts.builder()
                .setSubject(memberId)
                .claim("auth", authorities)
                .claim("scheduleId", scheduleId)
                .setExpiration(accessExpirationDate)
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

    @Transactional
    public String generateRefreshToken(String memberId) {
        String refreshToken = null;
        // 없으면 새로 발급
        RefreshToken dbRefreshToken = refreshTokenRepository.findByMemberId(Long.parseLong(memberId));
        if (dbRefreshToken == null) {
            refreshToken = Jwts.builder()
                    .setSubject(memberId)
                    .setExpiration(refreshExpirationDate)
                    .signWith(secret, SignatureAlgorithm.HS256)
                    .compact();

            RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .memberId(Long.parseLong(memberId))
                    .refreshToken(refreshToken)
                    .build();

            refreshTokenRepository.save(refreshTokenEntity);
        } else {
            refreshToken = dbRefreshToken.getRefreshToken();
        }
        return refreshToken;
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch (Exception e) {
            log.info("오류가 발생했습니다.", e);
        }
        return false;
    }

    public Long validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(refreshToken);
            Long memberId = Long.parseLong(claims.getBody().getSubject());
            RefreshToken refreshTokenEntity = refreshTokenRepository.findByMemberId(memberId);
            if (!refreshTokenEntity.getRefreshToken().equals(refreshToken)) throw new Exception("토큰정보가 DB와 일치하지 않습니다.");
            return memberId;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch (Exception e) {
            log.info("올바르지 않은 JWT 토큰입니다.", e);
        }
        return null;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        } else if (claims.get("scheduleId") == null) {
            throw new RuntimeException("스케쥴 ID 정보가 없는 토큰입니다.");
        } else if (claims.getSubject() == null) {
            throw new RuntimeException("멤버 정보가 없는 토큰입니다.");
        }

        Long scheduleId = Long.valueOf(claims.get("scheduleId").toString());

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = CustomUserDetails.builder()
                .username(claims.getSubject())
                .password("")
                .authorities(authorities)
                .scheduleId(scheduleId)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
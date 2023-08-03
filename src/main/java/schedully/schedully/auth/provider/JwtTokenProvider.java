package schedully.schedully.auth.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import schedully.schedully.auth.JwtToken;
import schedully.schedully.auth.common.CustomUserDetails;

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

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.accessExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        this.refreshExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 36);
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.secret = Keys.hmacShaKeyFor(secretByteKey);
    }

    public JwtToken generateToken(Authentication authentication, Long scheduleId, Long memberId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.info(authorities);

        //Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(memberId.toString())
                .claim("auth",authorities)
                .claim("scheduleId",scheduleId)
                .claim("memberId", memberId)
                .setExpiration(accessExpirationDate)
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();

        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshExpirationDate)
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }else if (claims.get("scheduleId") == null){
            throw new RuntimeException("스케쥴 ID 정보가 없는 토큰입니다.");
        }else if (claims.get("memberId") == null){
            throw new RuntimeException("멤버 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Long scheduleId = Long.valueOf(claims.get("scheduleId").toString());
        Long memberId = Long.valueOf(claims.get("memberId").toString());

        UserDetails principal = CustomUserDetails.builder()
                .username(claims.getSubject())
                .password("")
                .authorities(authorities)
                .scheduleId(scheduleId)
                .memberId(memberId)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
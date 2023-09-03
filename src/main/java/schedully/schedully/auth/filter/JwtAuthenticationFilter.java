package schedully.schedully.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import schedully.schedully.auth.domain.CustomUserDetails;
import schedully.schedully.auth.provider.JwtTokenProvider;
import schedully.schedully.exception.ErrorCode;
import schedully.schedully.exception.auth.JwtAuthException;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException, ServletException {
        String uri = request.getRequestURI();
        // uriParams : "/schedule/id/members" -> {"","schedule","id","members"} 와 같이 변환됨
        String[] uriParams = uri.split("/");

        String token = resolveToken(request);
        log.info("수신한 JWT 토큰 : " + token);

        if (uri.startsWith("/refresh")) {
            // "/refresh" 요청인 경우 인증 정보를 가져오지 않고 다음 필터로 요청을 전달
            filterChain.doFilter(request, response);
            return;
        }

        // null 이거나 유효하지 않은 토큰인지 검사
        if (token != null && jwtTokenProvider.validateAccessToken(token)) {

            // 파라미터가 3개 이상이고 schedule 관련된 요청인 경우 검증 진행
            if (uriParams.length >= 3 && uri.startsWith("/schedule")) {

                String paramScheduleId = uriParams[2];

                // 토큰에서 Authentication 객체 및 scheduleId 가져오기
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Long tokenScheduleId = ((CustomUserDetails) authentication.getPrincipal()).getScheduleId();

                // 토큰의 schedule Id와 요청 URI 의 schedule Id 파라미터가 같은지 확인
                if (!paramScheduleId.equals(tokenScheduleId.toString())) {
                    throw new JwtAuthException("토큰 정보와 요청 파라미터가 일치하지 않습니다.", ErrorCode.JWT_BAD_REQUEST);
                }
            } else {
                log.info("토큰 검증이 필요하지 않은 요청입니다. : " + request.getRequestURI());
            }
        }
        filterChain.doFilter(request, response);
    }

    // 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

package schedully.schedully.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import schedully.schedully.auth.domain.CustomUserDetails;
import schedully.schedully.auth.provider.JwtTokenProvider;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);
        log.info("수신한 JWT 토큰 : " + token);

        // null 이거나 유효하지 않은 토큰인지 검사
        if (token!=null && jwtTokenProvider.validateAccessToken(token)) {
            // uriParams : "/schedule/id/members" -> {"","schedule","id","members"} 와 같이 변환됨
            String[] uriParams = ((HttpServletRequest) request).getRequestURI().split("/");

            // 파라미터가 3개 이상이고 schedule 관련된 요청인 경우
            if (uriParams.length >= 3 && uriParams[1].equals("schedule")) {
                String paramScheduleId = uriParams[2];

                // 토큰에서 Authentication 객체 및 scheduleId 가져오기
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Long tokenScheduleId = ((CustomUserDetails) authentication.getPrincipal()).getScheduleId();

                // 토큰의 schedule Id와 요청 URI 의 schedule Id 파라미터가 같은지 확인
                if (!paramScheduleId.equals(tokenScheduleId.toString())) {
                    log.error("잘못된 접근입니다. : " + ((HttpServletRequest) request).getRequestURI());
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                log.info("토큰 검증이 필요하지 않은 요청입니다. : " + ((HttpServletRequest) request).getRequestURI());
            }
        }
        chain.doFilter(request, response);
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

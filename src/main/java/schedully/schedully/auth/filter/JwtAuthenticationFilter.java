package schedully.schedully.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import schedully.schedully.auth.common.CustomUserDetails;
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

        if (token!=null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 Authentication 객체 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Schedule Id 가져오기
            Long scheduleId = ((CustomUserDetails) authentication.getPrincipal()).getScheduleId();

            // uriParams : "/schedule/id/members" -> {"","schedule","id","members"} 와 같이 변환됨
            String[] uriParams = ((HttpServletRequest) request).getRequestURI().split("/");

            // 파라미터가 3개 이상인 경우 (맨 앞 빈 파라미터 포함)
            if(uriParams.length>=3){
                boolean isScheduleRequest = uriParams[1].equals("schedule");
                String paramScheduleId = uriParams[2];
                if(!isScheduleRequest){
                    log.error("Schedule 관련 요청이 아닙니다 : " + ((HttpServletRequest) request).getRequestURI());
                }
                else if(!paramScheduleId.equals(scheduleId.toString())){
                    log.error(((HttpServletRequest) request).getRequestURI());
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
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

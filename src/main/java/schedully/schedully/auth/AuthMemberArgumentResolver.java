package schedully.schedully.auth;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import schedully.schedully.auth.common.CustomUserDetails;
import schedully.schedully.converter.MemberConverter;
import schedully.schedully.domain.Member;

@Component
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        AuthMember authMember = parameter.getParameterAnnotation(AuthMember.class);
        if (authMember == null) return false;
        if (!parameter.getParameterType().equals(Member.class)) return false;
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object principal = null;
        // JwtAuthenticationFilter 에서 저장한 authentication 을 SecurityContextHolder 에서 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            principal = authentication.getPrincipal();
        }
        if (principal == null || principal.getClass() == String.class) {
            return null;
        }
        String memberId = ((CustomUserDetails) principal).getUsername();
        return Long.parseLong(memberId);
    }
}

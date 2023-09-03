package schedully.schedully.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import schedully.schedully.auth.domain.CustomUserDetails;
import schedully.schedully.domain.Member;
import schedully.schedully.repository.MemberRepository;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new UsernameNotFoundException("scheduleId를 찾을 수 없습니다.");
        }

        Long scheduleId = (Long) requestAttributes.getAttribute("scheduleId", RequestAttributes.SCOPE_REQUEST);

        Member memberEntity = memberRepository.findByUsernameAndScheduleIdJpql(username, scheduleId);
        if (memberEntity == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        // 사용자 정보를 UserDetails 객체로 변환하여 반환하면
        return CustomUserDetails.builder()
                .username(memberEntity.getId().toString())
                .password(memberEntity.getPassword())
                .scheduleId(scheduleId)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(memberEntity.getRole().toString())))
                .build();
    }
}

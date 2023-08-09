package schedully.schedully.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import schedully.schedully.auth.common.CustomUserDetails;
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

        // 데이터베이스에서 사용자 정보를 가져와서 UserDetails 객체를 생성하여 반환
        Long scheduleId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Member memberEntity = memberRepository.findByUsernameAndSchedule_Id(username, scheduleId);

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

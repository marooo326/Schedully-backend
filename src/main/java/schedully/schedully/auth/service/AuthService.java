package schedully.schedully.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import schedully.schedully.auth.domain.CustomUserDetails;
import schedully.schedully.auth.dto.JwtToken;
import schedully.schedully.auth.provider.JwtTokenProvider;
import schedully.schedully.controller.dto.AuthRequestDto;
import schedully.schedully.domain.Member;
import schedully.schedully.domain.Role;
import schedully.schedully.domain.Schedule;
import schedully.schedully.repository.MemberRepository;
import schedully.schedully.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    public Member signUp(Long scheduleId, AuthRequestDto.SignUpDto signUpDto) throws Exception {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isPresent()) {
            try {
                // 멤버 중복체크
                Schedule scheduleEntity = schedule.get();
                List<Member> memberList = scheduleEntity.getMembers();
                for (Member member : memberList) {
                    if (member.getUsername().equals(signUpDto.getUsername())) {
                        throw new Exception("이미 존재하는 이름입니다.");
                    }
                }

                // 비밀번호 / 비밀번호 확인
                if (!signUpDto.getPassword().equals(signUpDto.getCheckedPassword())) {
                    throw new Exception("비밀번호가 일치하지 않습니다.");
                }

                // 첫 멤버라면(스케쥴을 생성한 사용자) ADMIN ROLE 부여
                Role role = memberList.isEmpty() ? Role.ADMIN : Role.USER;

                // 비밀번호를 인코딩해서 멤버 엔티티 생성 후 저장
                Member member = Member.builder()
                        .username(signUpDto.getUsername())
                        .password(passwordEncoder.encode(signUpDto.getPassword()))
                        .role(role)
                        .availableDates(new ArrayList<>())
                        .build();
                member.updateSchedule(schedule.get());
                memberRepository.save(member);
                return member;
            } catch (DataIntegrityViolationException e) {
                log.info(e.toString());
                throw new Exception("DataIntegrityViolationException");
            }
        } else {
            log.info("해당 스케줄 없음. Id: {}", scheduleId);
            throw new Exception("존재하지 않는 스케쥴 ID 입니다.");
        }
    }

    public JwtToken login(Long scheduleId, String username, String password) {
        // 추후 적용
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute("scheduleId", scheduleId, RequestAttributes.SCOPE_REQUEST);
        }

//        // CustomUserDetailsService 로 scheduleId를 전달하기 위한 토큰
//        UsernamePasswordAuthenticationToken scheduleIdToken = new UsernamePasswordAuthenticationToken(username, password);
//        scheduleIdToken.setDetails(scheduleId);
//        SecurityContextHolder.getContext().setAuthentication(scheduleIdToken);

        // loginForm 의 정보로 authenticationToken 인스턴스 및 authentication 인스턴스 생성 (-> CustomUserDetailService)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // AuthenticationProvider 에 의해 CustomUserDetailService 호출됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return jwtTokenProvider.generateToken(authentication, principal.getUsername(), scheduleId);
    }

    public String regenerateAccessToken(String refreshToken) {
        Long memberId = jwtTokenProvider.validateRefreshToken(refreshToken);
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent()) {
            Member memberEntity = member.get();
            Long scheduleId = memberRepository.findScheduleIdByIdJpql(memberId);

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                requestAttributes.setAttribute("scheduleId", scheduleId, RequestAttributes.SCOPE_REQUEST);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    memberEntity.getUsername(),
                    "",
                    Collections.singletonList(new SimpleGrantedAuthority(memberEntity.getRole().toString()))
            );

            return jwtTokenProvider.generateAccessToken(authentication, memberEntity.getUsername(), scheduleId);
        } else {
            log.info("DB에 해당 토큰 멤버 정보가 존재하지 않습니다.");
        }
        return null;
    }
}

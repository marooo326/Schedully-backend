package schedully.schedully.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schedully.schedully.auth.JwtToken;
import schedully.schedully.auth.common.CustomUserDetails;
import schedully.schedully.controller.dto.AuthRequestDto;
import schedully.schedully.controller.dto.ScheduleRequestDto;
import schedully.schedully.converter.AuthConverter;
import schedully.schedully.converter.MemberConverter;
import schedully.schedully.domain.Date;
import schedully.schedully.domain.Member;
import schedully.schedully.auth.provider.JwtTokenProvider;
import schedully.schedully.domain.Role;
import schedully.schedully.domain.Schedule;
import schedully.schedully.repository.DateRepository;
import schedully.schedully.repository.MemberRepository;
import schedully.schedully.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final DateRepository dateRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findMemberBySchedule(Long scheduleId) throws Exception{
        List<Member> memberList = memberRepository.findByScheduleJpql(scheduleId);
        if (memberList.isEmpty()) {
            throw new Exception("오류가 발생했습니다. (멤버가 존재하지 않는 스케쥴)");
        }
        return memberList;
    }

    public Member signUp(Long scheduleId, AuthRequestDto.SignUpDto signUpDto) throws Exception{
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isPresent()){
            try{
                // 비밀번호 / 비밀번호 확인
                if (!signUpDto.getPassword().equals(signUpDto.getCheckedPassword())) {
                    throw new Exception("비밀번호가 일치하지 않습니다.");
                }

                // 멤버 중복체크
                Schedule scheduleEntity = schedule.get();
                List<Member> memberList = scheduleEntity.getMembers();
                for (Member member : memberList){
                    if (member.getUsername().equals(signUpDto.getUsername())) {
                        throw new Exception("이미 존재하는 이름입니다.");
                    }
                }

                // 첫 멤버라면(스케쥴을 생성한 사용자) ADMIN ROLE 부여
                Role role = memberList.isEmpty()?Role.ADMIN:Role.USER;

                // 비밀번호를 인코딩해서 함께 전달
                Member member = AuthConverter.toMember(signUpDto, passwordEncoder.encode(signUpDto.getPassword()),role);
                member.updateSchedule(schedule.get());
                memberRepository.save(member);
                return member;
            }catch(DataIntegrityViolationException e){
                log.info(e.toString());
                throw new Exception("DataIntegrityViolationException");
            }
        }else{
            log.info("해당 스케쥴 없음. Id: {}",scheduleId);
            throw new Exception("존재하지 않는 스케쥴 ID 입니다.");
        }
    }

    public JwtToken login(Long scheduleId, String username, String password) {
        // CustomUserDetailsService 로 scheduleId를 전달하기 위한 토큰
        UsernamePasswordAuthenticationToken tempToken = new UsernamePasswordAuthenticationToken(username, password);
        tempToken.setDetails(scheduleId);
        SecurityContextHolder.getContext().setAuthentication(tempToken);

        // loginForm 의 정보로 authenticationToken 인스턴스 및 authentication 인스턴스 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return jwtTokenProvider.generateToken(authentication, scheduleId, principal.getMemberId());
    }

//    public Member saveAvailableDate(DateRequestDto dateRequestDto){
//        Optional<Member> member = memberRepository.findById(dateRequestDto.getMemberId());
//        if (member.isPresent()){
//            Member memberEntity = member.get();
//            if (memberEntity.getSchedule().getId() == dateRequestDto.getScheduleId()) {
//                log.trace("{}", dateRequestDto.getDates());
//                for (LocalDate dateDTO : dateRequestDto.getDates()) {
//                    // Date 객체 생성
//                    Date date = Date.builder()
//                            .date(dateDTO)
//                            .build();
//                    date.updateMember(memberEntity);
//                    dateRepository.save(date);
//                }
//            } else {
//                log.info("존재하지 않는 멤버");
//            }
//        }
//        return null;
//    }

}

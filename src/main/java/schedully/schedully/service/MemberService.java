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
import schedully.schedully.controller.DTO.DateListDTO;
import schedully.schedully.controller.DTO.LoginRequestDTO;
import schedully.schedully.controller.DTO.SignUpRequestDTO;
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


    public List<Member> findAllMember(Long scheduleId){
        return memberRepository.findByScheduleJpql(scheduleId);
    }

    public Member signUp(Long scheduleId, SignUpRequestDTO memberDTO) throws Exception{
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isPresent()){
            try{
                Schedule scheduleEntity = schedule.get();
                List<Member> memberList = scheduleEntity.getMembers();

                // 중복체크
                for (Member member : memberList){
                    if (member.getUsername().equals(memberDTO.getUsername())) {
                        throw new Exception("이미 존재하는 이름입니다.");
                    }
                }

                // 비밀번호 확인
                if (!memberDTO.getPassword().equals(memberDTO.getCheckedPassword())) {
                    throw new Exception("비밀번호가 일치하지 않습니다.");
                }

                Role role;
                if (schedule.get().getMembers().isEmpty()) {
                    role = Role.ADMIN;
                } else {
                    role = Role.USER;
                }

                Member member = Member.builder()
                        .username(memberDTO.getUsername())
                        .password(passwordEncoder.encode(memberDTO.getPassword()))
                        .role(role)
                        .availableDates(new ArrayList<>())
                        .build();
                member.updateSchedule(schedule.get());
                memberRepository.save(member);
                return member;

            }catch(DataIntegrityViolationException e){
                log.info(e.toString());
            }
        }else{
            log.info("해당 스케쥴 없음. Id: {}",scheduleId);
            return null;
        }
        return null;
    }

    public JwtToken login(Long scheduleId, LoginRequestDTO loginForm) {


        UsernamePasswordAuthenticationToken temp = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        temp.setDetails(scheduleId);
        SecurityContextHolder.getContext().setAuthentication(temp);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public Member saveAvailableDate(DateListDTO dateListDTO){
        Optional<Member> member = memberRepository.findById(dateListDTO.getMemberId());
        if (member.isPresent()){
            Member memberEntity = member.get();
            if (memberEntity.getSchedule().getId() == dateListDTO.getScheduleId()) {
                log.trace("{}",dateListDTO.getDates());
                for (LocalDate dateDTO : dateListDTO.getDates()) {
                    // Date 객체 생성
                    Date date = Date.builder()
                            .date(dateDTO)
                            .build();
                    date.updateMember(memberEntity);
                    dateRepository.save(date);
                }
            } else {
                log.info("존재하지 않는 멤버");
            }
        }
        return null;
    }
}

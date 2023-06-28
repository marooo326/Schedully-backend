package schedully.schedully.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schedully.schedully.member.Member;
import schedully.schedully.member.MemberDTO;
import schedully.schedully.member.MemberRepository;
import schedully.schedully.member.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Schedule createSchedule(@NotNull ScheduleDTO scheduleDTO) {
        MemberDTO memberDTO = scheduleDTO.getAuthor();

        Schedule schedule = Schedule.builder()
                .title(scheduleDTO.getTitle())
                .password(passwordEncoder.encode(scheduleDTO.getPassword()))
                .content(scheduleDTO.getContent())
                .startDate(scheduleDTO.getStartDate())
                .endDate(scheduleDTO.getEndDate())
                .members(new ArrayList<>())
                .build();

        schedule = scheduleRepository.save(schedule);

        this.addMember(schedule.getId(),memberDTO);
        return schedule;
    }

    public Optional<Schedule> findSchedule(Long scheduleId){
        return scheduleRepository.findById(scheduleId);
    }

    public List<Member> findAllMember(Long scheduleId){
        return memberRepository.findByScheduleJpql(scheduleId);
    }

    public Member addMember(Long scheduleId, MemberDTO memberDTO){
        Optional<Schedule> schedule = this.findSchedule(scheduleId);
        if(schedule.isPresent()){
            try{
                Role role;
                if (schedule.get().getMembers().isEmpty()) {
                    role = Role.ADMIN;
                } else {
                    role = Role.BASIC;
                }
                Member member = Member.builder()
                        .name(memberDTO.getName())
                        .password(passwordEncoder.encode(memberDTO.getPassword()))
                        .role(role)
                        .availableDates(new ArrayList<>())
                        .build();
                member.updateSchedule(schedule.get());
                memberRepository.save(member);
                return member;
            }catch(DataIntegrityViolationException e){
                //log.info("{}",e);
            }
        }else{
            log.info("해당 스케쥴 없음. Id: {}",scheduleId);
            return null;
        }
        return null;
    }
}

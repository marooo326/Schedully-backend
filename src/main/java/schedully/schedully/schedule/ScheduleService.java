package schedully.schedully.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

    public Schedule createSchedule(@NotNull ScheduleDTO scheduleDTO) {
        MemberDTO memberDTO = scheduleDTO.getAuthor();

        Schedule schedule = Schedule.builder()
                .title(scheduleDTO.getTitle())
                .password(scheduleDTO.getPassword())
                .content(scheduleDTO.getContent())
                .startDate(scheduleDTO.getStartDate())
                .endDate(scheduleDTO.getEndDate())
                .members(new ArrayList<>())
                .build();

        Member author = Member.builder()
                .name(memberDTO.getName())
                .password(memberDTO.getPassword())
                .role(Role.ADMIN)
                .availableDates(new ArrayList<>())
                .build();

        author.updateSchedule(schedule);
        memberRepository.save(author);

        log.info("Save Success = {}", schedule.getMembers());
        return scheduleRepository.save(schedule);
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
            Member member = Member.builder()
                    .name(memberDTO.getName())
                    .password(memberDTO.getPassword())
                    .role(Role.BASIC)
                    .availableDates(new ArrayList<>())
                    .build();
            member.updateSchedule(schedule.get());
            memberRepository.save(member);
            return member;
        }else{
            return null;
        }
    }

}

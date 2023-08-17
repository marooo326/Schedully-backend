package schedully.schedully.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schedully.schedully.controller.dto.DateRequestDto;
import schedully.schedully.converter.DateConverter;
import schedully.schedully.domain.Date;
import schedully.schedully.domain.Member;
import schedully.schedully.auth.provider.JwtTokenProvider;
import schedully.schedully.repository.DateRepository;
import schedully.schedully.repository.MemberRepository;
import schedully.schedully.repository.ScheduleRepository;

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

    public List<Member> findMemberBySchedule(Long scheduleId) throws Exception {
        List<Member> memberList = memberRepository.findByScheduleJpql(scheduleId);
        if (memberList.isEmpty()) {
            throw new Exception("오류가 발생했습니다. (멤버가 존재하지 않는 스케쥴)");
        }
        return memberList;
    }

//    public List<> getAvailableDates(Long memberId, List<DateRequestDto.DateDto> dateList) throws Exception {
//        Optional<Member> member = memberRepository.findById(memberId);
//        if (member.isPresent()) {
//            Member memberEntity = member.get();
//            for (DateRequestDto.DateDto dateDto : dateList) {
//                Date dateEntity = DateConverter.toDate(dateDto);
//                dateEntity.updateMember(memberEntity);
//                dateRepository.save(dateEntity);
//            }
//        }
//        return null;
//    }

    public Member saveAvailableDates(Long memberId, List<DateRequestDto.DateDto> dateList) throws Exception {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent()) {
            Member memberEntity = member.get();
            for (DateRequestDto.DateDto dateDto : dateList) {
                Date dateEntity = DateConverter.toDate(dateDto);
                dateEntity.updateMember(memberEntity);
                dateRepository.save(dateEntity);
            }
        }
        return null;
    }

}

package schedully.schedully.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.member.Member;
import schedully.schedully.member.MemberDTO;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 새로운 Schedule 생성
     * @param scheduleDTO
     * @return 생성된 Schedule 정보 / HttpStatus Code
     */
    @PostMapping("/new")
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        log.info("{}",scheduleDTO);
        Schedule schedule = scheduleService.createSchedule(scheduleDTO);
        if(schedule!=null){
            return new ResponseEntity<>(schedule, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Schedule 세부 정보 가져오기
     * @param scheduleId
     * @return Schedule 세부 정보 / HttpStatus Code
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Schedule> getScheduleDetails(@PathVariable Long scheduleId){
        Optional<Schedule> schedule = scheduleService.findSchedule(scheduleId);
        if (schedule.isPresent()) {
            return new ResponseEntity<>(schedule.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * member list 가져오기
     * @param scheduleId
     * @return MemberList / HttpStatus Code
     */
    @GetMapping("/{scheduleId}/members")
    public ResponseEntity<List<Member>> getMemberList(@PathVariable Long scheduleId){
        List<Member> memberList = scheduleService.findAllMember(scheduleId);
        if (!memberList.isEmpty()){
            return new ResponseEntity<>(memberList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Schedule에 새로운 Member 추가
     * @param scheduleId
     * @param memberDTO
     * @return 새로운 Member 정보 / HttpStatus Code
     */
    @PostMapping("/{scheduleId}/members/new")
    public ResponseEntity<Member> addMember(@PathVariable Long scheduleId, @RequestBody MemberDTO memberDTO){
        Member member = scheduleService.addMember(scheduleId,memberDTO);
        if(member != null){
            return new ResponseEntity<>(member, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
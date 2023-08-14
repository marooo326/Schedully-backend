package schedully.schedully.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.auth.annotation.AuthMember;
import schedully.schedully.controller.dto.DateRequestDto;
import schedully.schedully.domain.Member;
import schedully.schedully.service.MemberService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{scheduleId}/members")
    public ResponseEntity<List<Member>> getMembers(@PathVariable Long scheduleId){
        try {
            List<Member> memberList = memberService.findMemberBySchedule(scheduleId);
            return ResponseEntity.ok()
                    .body(memberList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{scheduleId}/dates/all")
    public ResponseEntity<Member> getAllAvailableDates(@PathVariable Long scheduleId){
        try{
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }


    @GetMapping("/{scheduleId}/dates")
    public ResponseEntity<Member> getAvailableDates(@PathVariable Long scheduleId, @AuthMember Long memberId){
        try{
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @PutMapping("/{scheduleId}/dates")
    public ResponseEntity<Member> updateAvailableDates(@PathVariable Long scheduleId,
                                                       @RequestBody @Valid DateRequestDto.DateListDto dateListDto,
                                                       @AuthMember Long memberId) {
        // 임시 try catch
        try{
            log.info(memberId.toString());
            return ResponseEntity.ok(memberService.saveAvailableDates(memberId, dateListDto.getDateList()));
        } catch (Exception e) {
            log.info(e.toString());
          return null;
        }
    }
}

package schedully.schedully.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.auth.JwtToken;
import schedully.schedully.controller.DTO.DateListDTO;
import schedully.schedully.controller.DTO.LoginRequestDTO;
import schedully.schedully.controller.DTO.SignUpRequestDTO;
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
        List<Member> memberList = memberService.findAllMember(scheduleId);
        if (!memberList.isEmpty()){
            return new ResponseEntity<>(memberList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{scheduleId}/join")
    public ResponseEntity<Member> join(@PathVariable Long scheduleId, @RequestBody SignUpRequestDTO signUpForm){
        ResponseEntity<Member> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            Member member = memberService.signUp(scheduleId, signUpForm);
            if (member != null) {
                response = new ResponseEntity<>(member, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return response;
    }

    @PostMapping("/{scheduleId}/login")
    public ResponseEntity<JwtToken> login(@PathVariable Long scheduleId, @RequestBody LoginRequestDTO loginForm){
        try {
            JwtToken token = memberService.login(scheduleId,loginForm);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }

    @PostMapping("/{scheduleId}/{memberId}/availableDate")
    public ResponseEntity<Member> saveAvailableDate(@RequestBody DateListDTO dateListDTO){
        return ResponseEntity.ok(memberService.saveAvailableDate(dateListDTO));
    }
}

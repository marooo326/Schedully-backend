package schedully.schedully.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.auth.JwtToken;
import schedully.schedully.controller.dto.AuthRequestDto;
import schedully.schedully.converter.AuthConverter;
import schedully.schedully.domain.Member;
import schedully.schedully.service.MemberService;
import schedully.schedully.controller.dto.AuthResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{scheduleId}/join")
    public ResponseEntity<AuthResponseDto.SignUpDto> join(@PathVariable Long scheduleId, @RequestBody @Valid AuthRequestDto.SignUpDto signUpForm){
        try {
            Member member = memberService.signUp(scheduleId, signUpForm);
            JwtToken token = memberService.login(scheduleId, signUpForm.getUsername(), signUpForm.getPassword());
            return ResponseEntity.ok()
                        .body(AuthConverter.toSignUpDto(member, token));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

    @PostMapping("/{scheduleId}/login")
    public ResponseEntity<AuthResponseDto.SignUpDto> login(@PathVariable Long scheduleId, @RequestBody @Valid AuthRequestDto.LonginDto loginForm){
        try {
            JwtToken token = memberService.login(scheduleId, loginForm.getUsername(), loginForm.getPassword());
            return ResponseEntity.ok()
                    .body(AuthConverter.toLoginDto(token));
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

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

//
//    @GetMapping("/{scheduleId}/dates")
//    public ResponseEntity<Member> getAvailableDates(@PathVariable Long scheduleId, @RequestParam("memberId") Long memberId, HttpServletRequest request){
//        String token = request.getHeader("Authorization").substring(7);
//        log.info(token);
//        return ResponseEntity.ok(null);
//
//    }

//    @PutMapping("/{scheduleId}/{memberId}/dates")
//    public ResponseEntity<Member> updateAvailableDates(@RequestBody @Valid ){
//        return ResponseEntity.ok(memberService.saveAvailableDate(dateRequestDto));
//    }
}

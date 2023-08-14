package schedully.schedully.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import schedully.schedully.auth.dto.JwtToken;
import schedully.schedully.auth.service.AuthService;
import schedully.schedully.controller.dto.AuthRequestDto;
import schedully.schedully.controller.dto.AuthResponseDto;
import schedully.schedully.converter.AuthConverter;
import schedully.schedully.domain.Member;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/schedule/{scheduleId}/signup")
    public ResponseEntity<AuthResponseDto.SignUpDto> signUp(@PathVariable Long scheduleId, @RequestBody @Valid AuthRequestDto.SignUpDto signUpForm){
        try {
            Member member = authService.signUp(scheduleId, signUpForm);
            JwtToken token = authService.login(scheduleId, signUpForm.getUsername(), signUpForm.getPassword());
            return ResponseEntity.ok()
                    .body(AuthConverter.toSignUpDto(member, token));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

    @PostMapping("/schedule/{scheduleId}/login")
    public ResponseEntity<AuthResponseDto.LoginDto> login(@PathVariable Long scheduleId, @RequestBody @Valid AuthRequestDto.LonginDto loginForm){
        try {
            JwtToken token = authService.login(scheduleId, loginForm.getUsername(), loginForm.getPassword());
            return ResponseEntity.ok()
                    .body(AuthConverter.toLoginDto(token));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

    @PostMapping("/schedule/{scheduleId}/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(HttpServletRequest request, @PathVariable Long scheduleId) {
        String refreshToken = null;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            refreshToken = bearerToken.substring(7);
            log.info(refreshToken);
        }

        try {
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", authService.regenerateAccessToken(refreshToken));
            return ResponseEntity.ok()
                    .body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }
}

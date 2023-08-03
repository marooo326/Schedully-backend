package schedully.schedully.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import schedully.schedully.auth.JwtToken;
import schedully.schedully.controller.dto.AuthRequestDto;
import schedully.schedully.domain.Member;
import schedully.schedully.controller.dto.AuthResponseDto;
import schedully.schedully.domain.Role;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class AuthConverter {
    public static AuthResponseDto.SignUpDto toSignUpDto(Member member, JwtToken token) {
        return AuthResponseDto.SignUpDto.builder()
                .memberId(member.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public static AuthResponseDto.SignUpDto toLoginDto(JwtToken token) {
        return AuthResponseDto.SignUpDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public static Member toMember(AuthRequestDto.SignUpDto signUpDto, String encodedPassword, Role role){
        return Member.builder()
                .username(signUpDto.getUsername())
                .password(encodedPassword)
                .role(role)
                .availableDates(new ArrayList<>())
                .build();
    }
}

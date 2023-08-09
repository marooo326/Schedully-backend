package schedully.schedully.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import schedully.schedully.auth.dto.JwtToken;
import schedully.schedully.domain.Member;
import schedully.schedully.controller.dto.AuthResponseDto;

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

}

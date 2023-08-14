package schedully.schedully.converter;

import schedully.schedully.auth.dto.JwtToken;
import schedully.schedully.domain.Member;
import schedully.schedully.controller.dto.AuthResponseDto;

public class AuthConverter {
    public static AuthResponseDto.SignUpDto toSignUpDto(Member member, JwtToken token) {
        return AuthResponseDto.SignUpDto.builder()
                .memberId(member.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public static AuthResponseDto.LoginDto toLoginDto(JwtToken token) {
        return AuthResponseDto.LoginDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

}

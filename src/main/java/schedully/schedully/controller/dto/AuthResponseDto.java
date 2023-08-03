package schedully.schedully.controller.dto;

import lombok.*;

public class AuthResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDto {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpDto {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
    }

}

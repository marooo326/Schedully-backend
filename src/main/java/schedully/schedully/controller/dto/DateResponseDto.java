package schedully.schedully.controller.dto;

import lombok.*;

public class DateResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DateDetailDto {
        private String accessToken;
        private String refreshToken;
    }
}

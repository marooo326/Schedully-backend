package schedully.schedully.controller.dto;

import lombok.*;
import schedully.schedully.domain.Role;

public class MemberResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberDto {
        private Long id;
        private Long scheduleId;
        private String username;
        private Role role;
    }
}
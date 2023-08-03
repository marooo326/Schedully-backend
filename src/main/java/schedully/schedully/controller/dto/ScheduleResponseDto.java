package schedully.schedully.controller.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ScheduleResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScheduleDto {
        private String title;

        private String explanation;

        private LocalDate startDate;

        private LocalDate endDate;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberListDto {
        List<MemberResponseDto.MemberDto> members;
    }
}

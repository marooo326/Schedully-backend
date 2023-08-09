package schedully.schedully.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class DateRequestDto {
    @Getter
    public static class DateDto {
        @NotBlank
        private LocalDate date;

        @NotBlank
        private boolean morningAvailable;

        @NotBlank
        private boolean afternoonAvailable;
    }

    @Getter
    public static class DateListDto {
        @NotBlank
        List<DateDto> dateList;
    }
}

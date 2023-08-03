package schedully.schedully.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

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
}

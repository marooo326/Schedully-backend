package schedully.schedully.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

public class ScheduleRequestDto {
    @Getter
    public static class ScheduleDto {
        @NotBlank
        private String title;

        private String explanation;

        @NotBlank
        private String password;

        @NotBlank
        private LocalDate startDate;

        @NotBlank
        private LocalDate endDate;
    }

    @Getter
    public static class DeleteDto {
        @NotBlank
        private Long id;

        @NotBlank
        private String password;
    }
}

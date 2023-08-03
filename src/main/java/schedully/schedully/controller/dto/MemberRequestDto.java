package schedully.schedully.controller.dto;

import lombok.Getter;

import java.util.List;

public class MemberRequestDto {
    @Getter
    public static class UpdateAvailableDatesDto {
        List<DateRequestDto.DateDto> availableDates;
    }

}

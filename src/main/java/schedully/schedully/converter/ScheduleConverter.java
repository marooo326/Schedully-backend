package schedully.schedully.converter;

import schedully.schedully.controller.dto.ScheduleResponseDto;
import schedully.schedully.domain.Schedule;

public class ScheduleConverter {
    public static ScheduleResponseDto.ScheduleDto toScheduleResponseDto(Schedule schedule) {
        return ScheduleResponseDto.ScheduleDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .explanation(schedule.getExplanation())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}
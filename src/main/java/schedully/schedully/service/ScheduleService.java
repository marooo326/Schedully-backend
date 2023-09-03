package schedully.schedully.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schedully.schedully.controller.dto.ScheduleRequestDto;
import schedully.schedully.controller.dto.ScheduleResponseDto;
import schedully.schedully.converter.ScheduleConverter;
import schedully.schedully.domain.*;
import schedully.schedully.exception.ErrorCode;
import schedully.schedully.exception.schedule.ScheduleException;
import schedully.schedully.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ScheduleResponseDto.ScheduleDto> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream().map(ScheduleConverter::toScheduleResponseDto).toList();
    }

    public ScheduleResponseDto.ScheduleDto findSchedule(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        return ScheduleConverter.toScheduleResponseDto(schedule.orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND)));
    }

    public ScheduleResponseDto.ScheduleDto createSchedule(ScheduleRequestDto.ScheduleDto createDto) {
        Schedule schedule = Schedule.builder()
                .title(createDto.getTitle())
                .password(passwordEncoder.encode(createDto.getPassword()))
                .explanation(createDto.getExplanation())
                .startDate(createDto.getStartDate())
                .endDate(createDto.getEndDate())
                .members(new ArrayList<>())
                .build();
        return ScheduleConverter.toScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto.ScheduleDto updateSchedule(Long scheduleId, ScheduleRequestDto.ScheduleDto updateDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
        schedule.updateInfo(updateDto);
        scheduleRepository.save(schedule);
        return ScheduleConverter.toScheduleResponseDto(schedule);
    }

    public Long deleteSchedule(Long scheduleId, ScheduleRequestDto.DeleteDto deleteDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
        if (schedule.getPassword().equals(deleteDto.getPassword())) {
            scheduleRepository.delete(schedule);
            return scheduleId;
        }
        return null;
    }
}

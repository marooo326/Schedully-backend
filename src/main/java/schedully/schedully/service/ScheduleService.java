package schedully.schedully.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import schedully.schedully.controller.dto.ScheduleRequestDto;
import schedully.schedully.domain.*;
import schedully.schedully.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;

    public Schedule createSchedule(@NotNull ScheduleRequestDto.CreateDto createDto) {

        Schedule schedule = Schedule.builder()
                .title(createDto.getTitle())
                .password(passwordEncoder.encode(createDto.getPassword()))
                .content(createDto.getExplanation())
                .startDate(createDto.getStartDate())
                .endDate(createDto.getEndDate())
                .members(new ArrayList<>())
                .build();

        return scheduleRepository.save(schedule);
    }

    public Schedule findSchedule(Long scheduleId){
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isEmpty()) {
            throw new NotFoundException("존재하지 않는 스케쥴");
        }
        return schedule.get();
    }

}

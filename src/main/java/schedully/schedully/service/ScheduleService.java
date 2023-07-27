package schedully.schedully.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schedully.schedully.controller.DTO.ScheduleDTO;
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

    public Schedule createSchedule(@NotNull ScheduleDTO scheduleDTO) {

        Schedule schedule = Schedule.builder()
                .title(scheduleDTO.getTitle())
                .password(passwordEncoder.encode(scheduleDTO.getPassword()))
                .content(scheduleDTO.getContent())
                .startDate(scheduleDTO.getStartDate())
                .endDate(scheduleDTO.getEndDate())
                .members(new ArrayList<>())
                .build();

        return scheduleRepository.save(schedule);
    }

    public Optional<Schedule> findSchedule(Long scheduleId){
        return scheduleRepository.findById(scheduleId);
    }

}

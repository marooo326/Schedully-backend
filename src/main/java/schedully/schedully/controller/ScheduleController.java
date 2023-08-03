package schedully.schedully.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.controller.dto.ScheduleRequestDto;
import schedully.schedully.domain.*;
import schedully.schedully.service.ScheduleService;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {


    private final ScheduleService scheduleService;

    @GetMapping(value = {""})
    public ResponseEntity<Schedule> getSchedules() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = {""})
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleRequestDto.CreateDto createDto) {
        Schedule schedule = scheduleService.createSchedule(createDto);
        return ResponseEntity.ok()
                .body(schedule);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<Schedule> getScheduleDetails(@PathVariable Long scheduleId){
        Schedule schedule = scheduleService.findSchedule(scheduleId);
        return ResponseEntity.ok()
                .body(schedule);
    }
}
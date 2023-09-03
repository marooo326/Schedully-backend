package schedully.schedully.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.controller.dto.ScheduleRequestDto;
import schedully.schedully.controller.dto.ScheduleResponseDto;
import schedully.schedully.service.ScheduleService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {


    private final ScheduleService scheduleService;

    @GetMapping(value = {""})
    public ResponseEntity<List<ScheduleResponseDto.ScheduleDto>> getAllSchedules() {
        return ResponseEntity.ok()
                .body(scheduleService.findAll());
    }

    @PostMapping(value = {""})
    public ResponseEntity<Long> createSchedule(@RequestBody ScheduleRequestDto.ScheduleDto createDto) {
        ScheduleResponseDto.ScheduleDto scheduleDto = scheduleService.createSchedule(createDto);
        return ResponseEntity.ok()
                .body(scheduleDto.getId());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto.ScheduleDto> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok()
                .body(scheduleService.findSchedule(scheduleId));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<Long> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto.ScheduleDto updateDto) {
        ScheduleResponseDto.ScheduleDto scheduleDto = scheduleService.updateSchedule(scheduleId, updateDto);
        return ResponseEntity.ok()
                .body(scheduleDto.getId());
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Long> deleteSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto.DeleteDto deleteDto) {
        return ResponseEntity.ok()
                .body(scheduleService.deleteSchedule(scheduleId, deleteDto));
    }
}
package schedully.schedully.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schedully.schedully.controller.DTO.ScheduleDTO;
import schedully.schedully.domain.*;
import schedully.schedully.service.ScheduleService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping(value = {"","/"})
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.createSchedule(scheduleDTO);
        if(schedule!=null){
            return new ResponseEntity<>(schedule, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<Schedule> getScheduleDetails(@PathVariable Long scheduleId){
        Optional<Schedule> schedule = scheduleService.findSchedule(scheduleId);
        if (schedule.isPresent()) {
            return new ResponseEntity<>(schedule.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
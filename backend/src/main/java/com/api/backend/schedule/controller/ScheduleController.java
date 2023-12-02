package com.api.backend.schedule.controller;

import com.api.backend.schedule.data.dto.ScheduleDto;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedules")
public class ScheduleController {
  private final ScheduleService scheduleService;

  @PostMapping
  public ResponseEntity<ScheduleDto> addSchedule(@RequestBody ScheduleRequest request, @PathVariable Long teamId) {
    ScheduleDto scheduleDto = ScheduleDto.of(scheduleService.add(request));
    return ResponseEntity.ok(scheduleDto);
  }

}

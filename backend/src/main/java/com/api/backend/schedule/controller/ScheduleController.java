package com.api.backend.schedule.controller;

import com.api.backend.schedule.data.dto.ScheduleDto;
import com.api.backend.schedule.data.dto.ScheduleEditRequest;
import com.api.backend.schedule.data.dto.ScheduleEditResponse;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
import com.api.backend.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

  @PostMapping
  public ResponseEntity<Page<ScheduleResponse>> addSchedule(@RequestBody ScheduleRequest request,
      @PathVariable Long teamId) {
    Page<ScheduleDto> scheduleDto = ScheduleDto.of(scheduleService.addSchedules(request));
    Page<ScheduleResponse> scheduleResponse = ScheduleResponse.from(scheduleDto);
    return ResponseEntity.ok(scheduleResponse);
  }

  @GetMapping("/{scheduleId}")
  public ResponseEntity<Page<ScheduleResponse>> searchSchedule(@PathVariable Long teamId,
      @PathVariable Long scheduleId, Pageable pageable) {
    Page<ScheduleDto> scheduleDto = ScheduleDto.of(scheduleService.searchSchedule(pageable, teamId));
    Page<ScheduleResponse> scheduleDtoPage = ScheduleResponse.from(scheduleDto);
    return ResponseEntity.ok(scheduleDtoPage);
  }

  @PutMapping
  public ResponseEntity<ScheduleEditResponse> editSchedule(@PathVariable Long teamId, @RequestBody
  ScheduleEditRequest request) {
    ScheduleDto scheduleDto = ScheduleDto.of(scheduleService.editSchedule(request));
    ScheduleEditResponse response = ScheduleEditResponse.from(scheduleDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<String> deleteSchedule(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    scheduleService.deleteSchedule(scheduleId);
    return ResponseEntity.ok("해당 일정이 정상적으로 삭제되었습니다.");
  }
}

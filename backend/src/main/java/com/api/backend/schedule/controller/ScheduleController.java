package com.api.backend.schedule.controller;

import com.api.backend.schedule.data.dto.ScheduleEditResponse;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
import com.api.backend.schedule.service.ScheduleService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    Page<ScheduleResponse> scheduleResponse = ScheduleResponse.from(
        scheduleService.addSchedules(request)
    );
    return ResponseEntity.ok(scheduleResponse);
  }

  @GetMapping("/{scheduleId}")
  public ResponseEntity<ScheduleResponse> searchScheduleDetailInfo(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    ScheduleResponse response = ScheduleResponse.from(
        scheduleService.searchScheduleDetailInfo(scheduleId, teamId)
    );
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<ScheduleEditResponse> editSchedule(@PathVariable Long teamId, @RequestBody
  @Valid ScheduleRequest editRequest) {
    ScheduleEditResponse response = ScheduleEditResponse.from(
        scheduleService.editSchedule(editRequest)
    );
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<String> deleteSchedule(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    scheduleService.deleteSchedule(scheduleId);
    return ResponseEntity.ok("해당 일정이 정상적으로 삭제되었습니다.");
  }
}

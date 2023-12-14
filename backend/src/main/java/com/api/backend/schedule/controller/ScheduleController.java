package com.api.backend.schedule.controller;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.dto.ScheduleEditRequest;
import com.api.backend.schedule.data.dto.ScheduleEditResponse;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
import com.api.backend.schedule.service.ScheduleService;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

  @PostMapping
  public ResponseEntity<ScheduleResponse> addSchedule(@RequestBody @Valid ScheduleRequest request,
      @PathVariable Long teamId) {
    ScheduleResponse scheduleResponse;
    if (!request.isRepeat()) {
      scheduleResponse = ScheduleResponse.from(
          scheduleService.addSimpleSchedule(request)
      );
    } else {
      scheduleResponse = ScheduleResponse.from(
          scheduleService.addRepeatSchedule(request)
      );
    }
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


  @GetMapping("/calendar")
  public ResponseEntity<Page<ScheduleResponse>> getMonthlySchedules(
      @PathVariable Long teamId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDt,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDt,
      @RequestParam(required = false) CategoryType type,
      Pageable pageable) {

    Page<ScheduleResponse> schedules = scheduleService.getSchedulesForMonth(teamId, startDt, endDt,
        type, pageable);

    return ResponseEntity.ok(schedules);
  }

  @PutMapping
  public ResponseEntity<ScheduleEditResponse> editSchedule(@PathVariable Long teamId, @RequestBody
  @Valid ScheduleEditRequest editRequest) {
    ScheduleEditResponse response = ScheduleEditResponse.from(
        scheduleService.editSimpleScheduleAndSave(editRequest)
    );
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<String> deleteSchedule(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    scheduleService.deleteSimpleSchedule(scheduleId);
    return ResponseEntity.ok("해당 일정이 정상적으로 삭제되었습니다.");
  }
}
package com.api.backend.schedule.controller;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.dto.AllSchedulesMonthlyView;
import com.api.backend.schedule.data.dto.RepeatScheduleInfoEditRequest;
import com.api.backend.schedule.data.dto.RepeatScheduleResponse;
import com.api.backend.schedule.data.dto.ScheduleEditResponse;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
import com.api.backend.schedule.data.dto.SimpleScheduleInfoEditRequest;
import com.api.backend.schedule.data.dto.SimpleScheduleResponse;
import com.api.backend.schedule.service.ScheduleService;
import java.time.LocalDateTime;
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
    if (request.getRepeatCycle() != null) {
      RepeatScheduleResponse response = RepeatScheduleResponse.from(scheduleService.addRepeatScheduleAndSave(request));
      scheduleResponse = ScheduleResponse.from(response);
    } else {
      SimpleScheduleResponse response = SimpleScheduleResponse.from(scheduleService.addSimpleScheduleAndSave(request));
      scheduleResponse = ScheduleResponse.from(response);
    }
    return ResponseEntity.ok(scheduleResponse);
  }

  @GetMapping("/repeat/{scheduleId}")
  public ResponseEntity<RepeatScheduleResponse> getRepeatScheduleDetailInfo(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    RepeatScheduleResponse response = RepeatScheduleResponse.from(
        scheduleService.getRepeatScheduleDetailInfo(scheduleId, teamId)
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping("/simple/{scheduleId}")
  public ResponseEntity<SimpleScheduleResponse> getSimpleScheduleDetailInfo(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    SimpleScheduleResponse response = SimpleScheduleResponse.from(
        scheduleService.getSimpleScheduleDetailInfo(scheduleId, teamId)
    );
    return ResponseEntity.ok(response);
  }


  @GetMapping("/calendar")
  public ResponseEntity<Page<AllSchedulesMonthlyView>> getMonthlySchedules(
      @PathVariable Long teamId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDt,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDt,
      @RequestParam(required = false) String categoryType,
      Pageable pageable
  ) {
    if (startDt == null) {
      startDt = LocalDateTime.now().withDayOfMonth(1);
    }
    if (endDt == null) {
      endDt = startDt.plusMonths(1).withDayOfMonth(1).minusDays(1);
    }

    Page<AllSchedulesMonthlyView> allSchedules;
    if (categoryType == null) {
      allSchedules = scheduleService.getAllMonthlySchedules(teamId, pageable);
    } else {
      CategoryType enumCategoryType = CategoryType.valueOf(categoryType.toUpperCase());
      allSchedules = scheduleService.getCategoryTypeMonthlySchedules(teamId, enumCategoryType, pageable);
    }

    return ResponseEntity.ok(allSchedules);
  }


  @PutMapping("/simple")
  public ResponseEntity<ScheduleEditResponse> editSimpleSchedule(@PathVariable Long teamId,
      @RequestBody
      @Valid SimpleScheduleInfoEditRequest editRequest) {
    ScheduleEditResponse response = ScheduleEditResponse.from(
        scheduleService.editSimpleScheduleInfoAndSave(editRequest));
    return ResponseEntity.ok(response);
  }

  @PutMapping("/repeat")
  public ResponseEntity<ScheduleEditResponse> editRepeatSchedule(@PathVariable Long teamId,
      @RequestBody
      @Valid RepeatScheduleInfoEditRequest editRequest) {
    ScheduleEditResponse response = ScheduleEditResponse.from(
        scheduleService.editRepeatScheduleInfoAndSave(editRequest));
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/simple/{scheduleId}")
  public ResponseEntity<String> deleteSimpleSchedule(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    scheduleService.deleteSimpleSchedule(scheduleId);
    return ResponseEntity.ok("해당 단순 일정이 정상적으로 삭제되었습니다.");
  }

  @DeleteMapping("/repeat/{scheduleId}")
  public ResponseEntity<String> deleteSchedule(@PathVariable Long teamId,
      @PathVariable Long scheduleId) {
    scheduleService.deleteRepeatSchedule(scheduleId);
    return ResponseEntity.ok("해당 반복 일정이 정상적으로 삭제되었습니다.");
  }
}

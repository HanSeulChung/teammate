package com.api.backend.schedule.controller;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.dto.AllSchedulesMonthlyView;
import com.api.backend.schedule.data.dto.RepeatScheduleInfoEditRequest;
import com.api.backend.schedule.data.dto.RepeatScheduleInfoEditResponse;
import com.api.backend.schedule.data.dto.RepeatScheduleResponse;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
import com.api.backend.schedule.data.dto.SimpleScheduleInfoEditRequest;
import com.api.backend.schedule.data.dto.SimpleScheduleInfoEditResponse;
import com.api.backend.schedule.data.dto.SimpleScheduleResponse;
import com.api.backend.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
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
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "일정")
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

  @ApiOperation(value = "일정 생성")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정이 성공적으로 추가되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 추가에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @PostMapping
  public ResponseEntity<ScheduleResponse> addSchedule(@RequestBody @Valid ScheduleRequest request,
      @PathVariable Long teamId, @ApiIgnore Principal principal) {
    ScheduleResponse scheduleResponse;
    if (request.getRepeatCycle() != null) {
      RepeatScheduleResponse response = RepeatScheduleResponse.from(
          scheduleService.addRepeatScheduleAndSave(request, principal)
      );
      scheduleResponse = ScheduleResponse.from(response);
    } else {
      SimpleScheduleResponse response = SimpleScheduleResponse.from(
          scheduleService.addSimpleScheduleAndSave(request, principal)
      );
      scheduleResponse = ScheduleResponse.from(response);
    }
    return ResponseEntity.ok(scheduleResponse);
  }


  @ApiOperation(value = "반복일정 상세 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "반복일정 상세 정보 조회에 성공하였습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "반복일정 조회에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1"),
          @ApiImplicitParam(
              name = "scheduleId"
              , value = "반복일정 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @GetMapping("/repeat/{scheduleId}")
  public ResponseEntity<RepeatScheduleResponse> getRepeatScheduleDetailInfo(
      @PathVariable Long teamId,
      @PathVariable Long scheduleId, @ApiIgnore Principal principal) {
    RepeatScheduleResponse response = RepeatScheduleResponse.from(
        scheduleService.getRepeatScheduleDetailInfo(scheduleId, teamId, principal)
    );
    return ResponseEntity.ok(response);
  }


  @ApiOperation(value = "단순일정 상세 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "단순일정 상세 정보 조회에 성공하였습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "단순일정 조회에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1"),
          @ApiImplicitParam(
              name = "scheduleId"
              , value = "단순일정 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @GetMapping("/simple/{scheduleId}")
  public ResponseEntity<SimpleScheduleResponse> getSimpleScheduleDetailInfo(
      @PathVariable Long teamId,
      @PathVariable Long scheduleId, @ApiIgnore Principal principal) {
    SimpleScheduleResponse response = SimpleScheduleResponse.from(
        scheduleService.getSimpleScheduleDetailInfo(scheduleId, teamId, principal)
    );
    return ResponseEntity.ok(response);
  }


  @ApiOperation(value = "캘린더 월간보기")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "월간보기 조회에 성공하였습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "월간보기 조회에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @GetMapping("/calendar")
  public ResponseEntity<Page<AllSchedulesMonthlyView>> getMonthlySchedules(
      @PathVariable Long teamId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDt,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDt,
      @RequestParam(required = false) String categoryType,
      @ApiIgnore Principal principal,
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
      allSchedules = scheduleService.getAllMonthlySchedules(teamId, pageable, principal);
    } else {
      CategoryType enumCategoryType = CategoryType.valueOf(categoryType.toUpperCase());
      allSchedules = scheduleService.getCategoryTypeMonthlySchedules(teamId, enumCategoryType,
          pageable, principal);
    }

    return ResponseEntity.ok(allSchedules);
  }

  @ApiOperation(value = "단순일정 수정")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정이 성공적으로 수정되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 수정에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @PutMapping("/simple")
  public ResponseEntity<SimpleScheduleInfoEditResponse> editSimpleSchedule(
      @PathVariable Long teamId,
      @RequestBody @Valid SimpleScheduleInfoEditRequest editRequest,
      @ApiIgnore Principal principal
  ) {
    SimpleScheduleInfoEditResponse response = SimpleScheduleInfoEditResponse.from(
        scheduleService.editSimpleScheduleInfoAndSave(editRequest, principal)
    );
    return ResponseEntity.ok(response);
  }


  @ApiOperation(value = "반복일정 수정")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정이 성공적으로 수정되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 수정에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @PutMapping("/repeat")
  public ResponseEntity<RepeatScheduleInfoEditResponse> editRepeatSchedule(
      @PathVariable Long teamId,
      @RequestBody @Valid RepeatScheduleInfoEditRequest editRequest,
      @ApiIgnore Principal principal
  ) {
    RepeatScheduleInfoEditResponse response = RepeatScheduleInfoEditResponse.from(
        scheduleService.editRepeatScheduleInfoAndSave(editRequest, principal));
    return ResponseEntity.ok(response);
  }


  @ApiOperation(value = "단순일정 삭제")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정이 성공적으로 삭제되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 삭제에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1"),
          @ApiImplicitParam(
              name = "scheduleId"
              , value = "단순일정 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @DeleteMapping("/simple/{scheduleId}")
  public ResponseEntity<String> deleteSimpleSchedule(
      @PathVariable Long teamId,
      @PathVariable Long scheduleId,
      @ApiIgnore Principal principal
  ) {
    scheduleService.deleteSimpleSchedule(scheduleId, principal);
    return ResponseEntity.ok("해당 단순 일정이 정상적으로 삭제되었습니다.");
  }


  @ApiOperation(value = "반복일정 삭제")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정이 성공적으로 삭제되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 삭제에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1"),
          @ApiImplicitParam(
              name = "scheduleId"
              , value = "반복일정 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })
  @DeleteMapping("/repeat/{scheduleId}")
  public ResponseEntity<String> deleteSchedule(
      @PathVariable Long teamId,
      @PathVariable Long scheduleId,
      @ApiIgnore Principal principal
  ) {
    scheduleService.deleteRepeatSchedule(scheduleId, principal);
    return ResponseEntity.ok("해당 반복 일정이 정상적으로 삭제되었습니다.");
  }
}
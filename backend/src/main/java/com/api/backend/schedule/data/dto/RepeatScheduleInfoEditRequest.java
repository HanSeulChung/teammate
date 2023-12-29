package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.customValidAnnotation.StartAndEndDtCheck;
import com.api.backend.schedule.data.type.EditOption;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

//반복 일정 정보만 수정
@Getter
@AllArgsConstructor
@Builder
@ToString
@StartAndEndDtCheck(scheduleStart = "startDt", scheduleEnd = "endDt")
public class RepeatScheduleInfoEditRequest {

  @NotNull(message = "repeatScheduleId는 필수 값입니다.")
  @Schema(description = "repeat schedule id", example = "1L")
  private Long repeatScheduleId;

  @NotNull(message = "teamId는 필수 값입니다.")
  @Schema(description = "team id", example = "1L")
  private Long teamId;

  @NotNull(message = "categoryId는 필수 값입니다.")
  @Schema(description = "category id", example = "1L")
  private Long categoryId;

  @NotBlank(message = "일정 제목을 입력해주세요.")
  @Size(min = 1, max = 10, message = "일정 제목은 1자 이상, 10자 이하여야 합니다.")
  @Schema(description = "title", example = "기획 회의")
  private String title;

  @Schema(description = "content", example = "컨셉 선정")
  private String content;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "start date", example = "2023-12-15T12:00:00")
  private LocalDateTime startDt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "end date", example = "2024-01-15T12:00:00")
  private LocalDateTime endDt;

  @Schema(description = "place", example = "회의실")
  private String place;

  @Schema(description = "color", example = "#ff0000")
  private String color;

  @Schema(description = "repeat cycle", example = "WEEKLY")
  private RepeatCycle repeatCycle;

  @Schema(description = "team participants ids", example = "[1L, 2L]")
  private List<Long> teamParticipantsIds;

  @Schema(description = "edit option", example = "이 일정")
  private EditOption editOption;
  
  @NotNull(message = "updateParticipantId는 필수 값입니다.")
  @Schema(description = "update team participant id", example = "1L")
  private Long updateParticipantId;
}

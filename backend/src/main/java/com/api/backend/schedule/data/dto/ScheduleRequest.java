package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.customValidAnnotation.StartAndEndDtCheck;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
@StartAndEndDtCheck(scheduleStart = "startDt", scheduleEnd = "endDt")
public class ScheduleRequest {

  private Long teamId;
  private Long categoryId;
  @NotBlank(message = "일정 제목을 입력해주세요.")
  @Size(min = 1, max = 10, message = "일정 제목은 1자 이상, 10자 이하여야 합니다.")
  private String title;
  private String content;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String place;
  private RepeatCycle repeatCycle;
  private String color;
  private List<Long> teamParticipantsIds;

  // 알람
  private Long createParticipantId;
}

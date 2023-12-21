package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.customValidAnnotation.StartAndEndDtCheck;
import com.api.backend.schedule.data.type.EditOption;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//반복 일정 정보만 수정
@Getter
@AllArgsConstructor
@Builder
@StartAndEndDtCheck(scheduleStart = "startDt", scheduleEnd = "endDt")
public class RepeatScheduleInfoEditRequest {

  private Long repeatScheduleId;
  private Long teamId;
  private Long categoryId;
  private String title;
  private String content;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String place;
  private String color;
  private RepeatCycle repeatCycle;
  private List<Long> teamParticipantsIds;
  private EditOption editOption;
}

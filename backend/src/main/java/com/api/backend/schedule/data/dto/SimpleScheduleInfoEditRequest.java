package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.customValidAnnotation.StartAndEndDtCheck;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

//단순 일정 정보만 수정
@Getter
@AllArgsConstructor
@Builder
@ToString
@StartAndEndDtCheck(scheduleStart = "startDt", scheduleEnd = "endDt")
public class SimpleScheduleInfoEditRequest {

  private Long simpleScheduleId;
  private Long teamId;
  private Long categoryId;
  private String title;
  private String content;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String place;
  private List<Long> teamParticipantsIds;
  private String color;
  private Long updateParticipantId;
}

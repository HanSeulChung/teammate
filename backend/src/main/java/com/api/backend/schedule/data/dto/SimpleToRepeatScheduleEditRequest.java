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

@Getter
@AllArgsConstructor
@Builder
@StartAndEndDtCheck(scheduleStart = "startDt", scheduleEnd = "endDt")
public class SimpleToRepeatScheduleEditRequest {
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
  private RepeatCycle repeatCycle;
  private List<Long> teamParticipantsIds;
  private String color;
  private EditOption editOption;
  private boolean isConverted;
}

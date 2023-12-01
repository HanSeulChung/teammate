package com.api.backend.schedule.data.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleRequest {
  private Long scheduleId;
  private Long teamId;
  private Long categoryId;
  private String title;
  private String content;
  private String place;
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private boolean repeatYn;
  private LocalDate repeatCycle;
  private String color;
  private HashMap<Long, String> scheduleParticipantMap;

}

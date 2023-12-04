package com.api.backend.schedule.data.dto;

import com.api.backend.team.data.entity.TeamParticipants;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private LocalTime time;
  private String place;

  //TODO: 추후 일정 반복 기능 구현 예정
  private boolean isRepeat;
  private LocalDateTime repeatCycle;

  private String color;
  private List<TeamParticipants> teamParticipants;
}

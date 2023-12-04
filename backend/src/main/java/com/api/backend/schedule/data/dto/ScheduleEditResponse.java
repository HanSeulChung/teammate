package com.api.backend.schedule.data.dto;

import com.api.backend.team.data.entity.TeamParticipants;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleEditResponse {
  private Long scheduleId;
  private Long categoryId;
  private String title;
  private String content;
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private String place;
  private boolean isRepeat;
  private LocalDateTime repeatCycle;
  private String color;
  private List<TeamParticipants> teamParticipants;

  public static ScheduleEditResponse from(ScheduleDto scheduleDto) {
    return ScheduleEditResponse.builder()
        .scheduleId(scheduleDto.getScheduleId())
        .categoryId(scheduleDto.getCategoryId())
        .title(scheduleDto.getTitle())
        .content(scheduleDto.getContent())
        .startDt(scheduleDto.getStartDt())
        .endDt(scheduleDto.getEndDt())
        .place(scheduleDto.getPlace())
        .isRepeat(scheduleDto.isRepeat())
        .repeatCycle(scheduleDto.getRepeatCycle())
        .color(scheduleDto.getColor())
        .teamParticipants(scheduleDto.getTeamParticipants())
        .build();
  }
}

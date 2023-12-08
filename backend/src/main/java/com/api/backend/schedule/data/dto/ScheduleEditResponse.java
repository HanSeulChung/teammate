package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.type.RepeatCycle;
import com.api.backend.team.data.type.TeamRole;
import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String place;
  private boolean isRepeat;
  private RepeatCycle repeatCycle;
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsNames;
  private List<TeamRole> teamRoles;

  public static ScheduleEditResponse from(ScheduleDto scheduleDto) {
    return ScheduleEditResponse.builder()
        .scheduleId(scheduleDto.getScheduleId())
        .categoryId(scheduleDto.getCategoryId())
        .startDt(scheduleDto.getStartDt())
        .endDt(scheduleDto.getEndDt())
        .title(scheduleDto.getTitle())
        .content(scheduleDto.getContent())
        .place(scheduleDto.getPlace())
        .isRepeat(scheduleDto.isRepeat())
        .repeatCycle(scheduleDto.getRepeatCycle())
        .teamParticipantsIds(scheduleDto.getTeamParticipantsIds())
        .teamParticipantsNames(scheduleDto.getTeamParticipantsName())
        .teamRoles(scheduleDto.getTeamRoles())
        .build();
  }
}

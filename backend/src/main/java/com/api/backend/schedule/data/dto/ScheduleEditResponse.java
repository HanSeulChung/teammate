package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
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
  private String categoryName;
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
  private boolean isConverted;

  public static ScheduleEditResponse from(SimpleSchedule simpleSchedule) {
    return ScheduleEditResponse.builder()
        .categoryName(simpleSchedule.getScheduleCategory().getCategoryName())
        .startDt(simpleSchedule.getStartDt())
        .endDt(simpleSchedule.getEndDt())
        .title(simpleSchedule.getTitle())
        .content(simpleSchedule.getContent())
        .place(simpleSchedule.getPlace())
        .teamParticipantsIds(ScheduleResponse.getTeamParticipantsIdsFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(ScheduleResponse.getTeamParticipantsNameFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamRoles(ScheduleResponse.getTeamParticipantsRoleFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .build();
  }

  public static ScheduleEditResponse from(RepeatSchedule repeatSchedule) {
    return ScheduleEditResponse.builder()
        .categoryName(repeatSchedule.getScheduleCategory().getCategoryName())
        .startDt(repeatSchedule.getStartDt())
        .endDt(repeatSchedule.getEndDt())
        .title(repeatSchedule.getTitle())
        .content(repeatSchedule.getContent())
        .place(repeatSchedule.getPlace())
        .teamParticipantsIds(ScheduleResponse.getTeamParticipantsIdsFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(ScheduleResponse.getTeamParticipantsNameFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamRoles(ScheduleResponse.getTeamParticipantsRoleFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .build();
  }

}

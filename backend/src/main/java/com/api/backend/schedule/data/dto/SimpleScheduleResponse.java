package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.team.data.type.TeamRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimpleScheduleResponse {

  private Long scheduleId;
  private String scheduleType;
  private String categoryName;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String title;
  private String content;
  private String place;
  private String color;
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsNames;
  private List<TeamRole> teamRoles;

  public static SimpleScheduleResponse from(SimpleSchedule simpleSchedule) {
    return SimpleScheduleResponse.builder()
        .scheduleType("단순 일정")
        .categoryName(simpleSchedule.getScheduleCategory().getCategoryName())
        .startDt(simpleSchedule.getStartDt())
        .endDt(simpleSchedule.getEndDt())
        .title(simpleSchedule.getTitle())
        .content(simpleSchedule.getContent())
        .place(simpleSchedule.getPlace())
        .teamParticipantsIds(
            getTeamParticipantsIdsFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(
            getTeamParticipantsNameFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamRoles(
            getTeamParticipantsRoleFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .build();
  }

  public static List<Long> getTeamParticipantsIdsFromSchedules(
      List<TeamParticipantsSchedule> teamParticipantsSchedules) {
    List<Long> teamParticipantsIds = new ArrayList<>();
    if (teamParticipantsSchedules != null) {
      for (TeamParticipantsSchedule teamParticipantsSchedule : teamParticipantsSchedules) {
        teamParticipantsIds.add(
            teamParticipantsSchedule.getTeamParticipants().getTeamParticipantsId());
      }
    }
    return teamParticipantsIds;
  }

  public static List<String> getTeamParticipantsNameFromSchedules(
      List<TeamParticipantsSchedule> teamParticipantsSchedules) {
    List<String> teamParticipantsNames = new ArrayList<>();
    if (teamParticipantsSchedules != null) {
      for (TeamParticipantsSchedule teamParticipantsSchedule : teamParticipantsSchedules) {
        teamParticipantsNames.add(teamParticipantsSchedule.getTeamParticipants().getTeamNickName());
      }
    }
    return teamParticipantsNames;
  }

  public static List<TeamRole> getTeamParticipantsRoleFromSchedules(
      List<TeamParticipantsSchedule> teamParticipantsSchedules) {
    List<TeamRole> teamParticipantsRoles = new ArrayList<>();
    if (teamParticipantsSchedules != null) {
      for (TeamParticipantsSchedule teamParticipantsSchedule : teamParticipantsSchedules) {
        teamParticipantsRoles.add(teamParticipantsSchedule.getTeamParticipants().getTeamRole());
      }
    }
    return teamParticipantsRoles;
  }
}
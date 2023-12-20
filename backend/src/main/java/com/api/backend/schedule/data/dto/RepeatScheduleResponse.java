package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.schedule.data.type.RepeatCycle;
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
public class RepeatScheduleResponse {
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
  private RepeatCycle repeatCycle;
  private String month;
  private int day;
  private String dayOfWeek;
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsNames;
  private List<TeamRole> teamRoles;

  public static RepeatScheduleResponse from(RepeatSchedule repeatSchedule) {
    return RepeatScheduleResponse.builder()
        .scheduleType("반복 일정")
        .categoryName(repeatSchedule.getScheduleCategory().getCategoryName())
        .startDt(repeatSchedule.getStartDt())
        .endDt(repeatSchedule.getEndDt())
        .title(repeatSchedule.getTitle())
        .content(repeatSchedule.getContent())
        .place(repeatSchedule.getPlace())
        .repeatCycle(repeatSchedule.getRepeatCycle())
        .month(repeatSchedule.getMonth())
        .day(repeatSchedule.getDay())
        .dayOfWeek(repeatSchedule.getDayOfWeek())
        .teamParticipantsIds(
            getTeamParticipantsIdsFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(
            getTeamParticipantsNameFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamRoles(getTeamParticipantsRoleFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
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
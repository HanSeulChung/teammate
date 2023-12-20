package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.api.backend.team.data.type.TeamRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleResponse {

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

  public static ScheduleResponse from(RepeatScheduleResponse repeatScheduleResponse) {
    return ScheduleResponse.builder()
        .scheduleId(repeatScheduleResponse.getScheduleId())
        .scheduleType("반복 일정")
        .categoryName(repeatScheduleResponse.getCategoryName())
        .startDt(repeatScheduleResponse.getStartDt())
        .endDt(repeatScheduleResponse.getEndDt())
        .title(repeatScheduleResponse.getTitle())
        .content(repeatScheduleResponse.getContent())
        .place(repeatScheduleResponse.getPlace())
        .repeatCycle(repeatScheduleResponse.getRepeatCycle())
        .month(repeatScheduleResponse.getMonth())
        .day(repeatScheduleResponse.getDay())
        .dayOfWeek(repeatScheduleResponse.getDayOfWeek())
        .teamParticipantsIds(repeatScheduleResponse.getTeamParticipantsIds())
        .teamParticipantsNames(repeatScheduleResponse.getTeamParticipantsNames())
        .teamRoles(repeatScheduleResponse.getTeamRoles())
        .build();
  }

  public static ScheduleResponse from(SimpleScheduleResponse simpleScheduleResponse) {
    return ScheduleResponse.builder()
        .scheduleId(simpleScheduleResponse.getScheduleId())
        .scheduleType("단순 일정")
        .categoryName(simpleScheduleResponse.getCategoryName())
        .scheduleId(simpleScheduleResponse.getScheduleId())
        .startDt(simpleScheduleResponse.getStartDt())
        .endDt(simpleScheduleResponse.getEndDt())
        .title(simpleScheduleResponse.getTitle())
        .content(simpleScheduleResponse.getContent())
        .place(simpleScheduleResponse.getPlace())
        .teamParticipantsIds(simpleScheduleResponse.getTeamParticipantsIds())
        .teamParticipantsNames(simpleScheduleResponse.getTeamParticipantsNames())
        .teamRoles(simpleScheduleResponse.getTeamRoles())
        .build();
  }


  public static Page<RepeatScheduleResponse> fromRepeatSchedule(Page<RepeatSchedule> schedules) {
    return schedules.map(RepeatScheduleResponse::from);
  }

  public static Page<SimpleScheduleResponse> fromSimpleSchedule(Page<SimpleSchedule> schedules) {
    return schedules.map(SimpleScheduleResponse::from);
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

package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.api.backend.team.data.type.TeamRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleResponse {
  private String scheduleType;
  private Long scheduleId;
  private Long categoryId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String title;
  private String content;
  private String place;
  private boolean isRepeat;
  private RepeatCycle repeatCycle;
  private String month; //월: 연간 반복시 사용
  private int day; //일: 연간, 월간 반복시 사용
  private String dayOfWeek; //요일: 주간 반복시 사용
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsNames;
  private List<TeamRole> teamRoles;
  private boolean isConverted;

  public static ScheduleResponse from(SimpleSchedule simpleSchedule) {
    return ScheduleResponse.builder()
        .scheduleType("단순 일정")
        .scheduleId(simpleSchedule.getSimpleScheduleId())
        .categoryId(simpleSchedule.getScheduleCategory().getScheduleCategoryId())
        .startDt(simpleSchedule.getStartDt())
        .endDt(simpleSchedule.getEndDt())
        .title(simpleSchedule.getTitle())
        .content(simpleSchedule.getContent())
        .place(simpleSchedule.getPlace())
        .teamParticipantsIds(
            getTeamParticipantsIdsFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(
            getTeamParticipantsNameFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamRoles(getTeamParticipantsRoleFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .build();
  }
  public static ScheduleResponse from(RepeatSchedule repeatSchedule) {
    return ScheduleResponse.builder()
        .scheduleType("반복 일정")
        .scheduleId(repeatSchedule.getRepeatScheduleId())
        .categoryId(repeatSchedule.getScheduleCategory().getScheduleCategoryId())
        .startDt(repeatSchedule.getStartDt())
        .endDt(repeatSchedule.getEndDt())
        .title(repeatSchedule.getTitle())
        .content(repeatSchedule.getContent())
        .place(repeatSchedule.getPlace())
        .day(repeatSchedule.getDay())
        .month(repeatSchedule.getMonth())
        .dayOfWeek(repeatSchedule.getDayOfWeek())
        .teamParticipantsIds(
            getTeamParticipantsIdsFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(
            getTeamParticipantsNameFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamRoles(getTeamParticipantsRoleFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .build();
  }
  public static Page<ScheduleResponse> from(Page<SimpleSchedule> schedules) {
    return schedules.map(ScheduleResponse::from);
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

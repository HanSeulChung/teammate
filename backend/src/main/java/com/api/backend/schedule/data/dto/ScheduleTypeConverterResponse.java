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
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ScheduleTypeConverterResponse {
  private Long scheduleId;
  private String categoryName;
  private String title;
  private String content;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String place;
  private String color;
  private RepeatCycle repeatCycle;
  private String month;
  private int day;
  private String dayOfWeek;
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsNames;
  private List<TeamRole> teamRoles;
  private boolean isConverted;

  public static ScheduleTypeConverterResponse from(SimpleSchedule simpleSchedule) {
    return ScheduleTypeConverterResponse.builder()
        .scheduleId(simpleSchedule.getSimpleScheduleId())
        .categoryName(simpleSchedule.getScheduleCategory().getCategoryName())
        .startDt(simpleSchedule.getStartDt())
        .endDt(simpleSchedule.getEndDt())
        .title(simpleSchedule.getTitle())
        .content(simpleSchedule.getContent())
        .place(simpleSchedule.getPlace())
        .color(simpleSchedule.getColor())
        .teamParticipantsIds(ScheduleResponse.getTeamParticipantsIdsFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(ScheduleResponse.getTeamParticipantsNameFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .teamRoles(ScheduleResponse.getTeamParticipantsRoleFromSchedules(simpleSchedule.getTeamParticipantsSchedules()))
        .isConverted(true)
        .build();
  }

  public static ScheduleTypeConverterResponse from(RepeatSchedule repeatSchedule) {
    return ScheduleTypeConverterResponse.builder()
        .scheduleId(repeatSchedule.getRepeatScheduleId())
        .categoryName(repeatSchedule.getScheduleCategory().getCategoryName())
        .startDt(repeatSchedule.getStartDt())
        .endDt(repeatSchedule.getEndDt())
        .title(repeatSchedule.getTitle())
        .content(repeatSchedule.getContent())
        .place(repeatSchedule.getPlace())
        .color(repeatSchedule.getColor())
        .repeatCycle(repeatSchedule.getRepeatCycle())
        .month(repeatSchedule.getMonth())
        .day(repeatSchedule.getDay())
        .dayOfWeek(repeatSchedule.getDayOfWeek())
        .teamParticipantsIds(ScheduleResponse.getTeamParticipantsIdsFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(ScheduleResponse.getTeamParticipantsNameFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamRoles(ScheduleResponse.getTeamParticipantsRoleFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .isConverted(true)
        .build();
  }

}

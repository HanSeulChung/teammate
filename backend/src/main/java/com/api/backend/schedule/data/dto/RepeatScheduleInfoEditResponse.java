package com.api.backend.schedule.data.dto;

import static com.api.backend.schedule.data.dto.ScheduleResponse.getTeamParticipantsIdsFromSchedules;
import static com.api.backend.schedule.data.dto.ScheduleResponse.getTeamParticipantsNameFromSchedules;
import static com.api.backend.schedule.data.dto.ScheduleResponse.getTeamParticipantsRoleFromSchedules;

import com.api.backend.schedule.data.entity.RepeatSchedule;
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
public class RepeatScheduleInfoEditResponse {

  private Long scheduleId;
  private Long originRepeatScheduleId;
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

  public static RepeatScheduleInfoEditResponse from(RepeatSchedule repeatSchedule) {
    return RepeatScheduleInfoEditResponse.builder()
        .scheduleId(repeatSchedule.getRepeatScheduleId())
        .originRepeatScheduleId(repeatSchedule.getOriginRepeatScheduleId())
        .scheduleType("반복 일정")
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
        .teamParticipantsIds(
            getTeamParticipantsIdsFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamParticipantsNames(
            getTeamParticipantsNameFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .teamRoles(
            getTeamParticipantsRoleFromSchedules(repeatSchedule.getTeamParticipantsSchedules()))
        .build();
  }

}

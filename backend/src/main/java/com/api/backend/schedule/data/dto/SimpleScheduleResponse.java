package com.api.backend.schedule.data.dto;

import static com.api.backend.schedule.data.dto.ScheduleResponse.getTeamParticipantsIdsFromSchedules;
import static com.api.backend.schedule.data.dto.ScheduleResponse.getTeamParticipantsNameFromSchedules;
import static com.api.backend.schedule.data.dto.ScheduleResponse.getTeamParticipantsRoleFromSchedules;

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

}
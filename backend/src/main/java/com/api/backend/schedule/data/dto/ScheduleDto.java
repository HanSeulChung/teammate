package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.entity.Schedule;
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
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {

  private Long scheduleId;
  private Long teamId;
  private Long categoryId;
  private String title;
  private String content;
  private String place;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime endDt;
  private boolean isRepeat;
  private RepeatCycle repeatCycle;
  private String color;
  private List<TeamParticipantsSchedule> teamParticipantsSchedules;
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsName;
  private List<TeamRole> teamRoles;
  public static ScheduleDto of(Schedule schedule) {

    return ScheduleDto.builder()
        .scheduleId(schedule.getScheduleId())
        .teamId(schedule.getTeam().getTeamId())
        .categoryId(schedule.getScheduleCategory().getScheduleCategoryId())
        .title(schedule.getTitle())
        .content(schedule.getContent())
        .place(schedule.getPlace())
        .startDt(schedule.getStartDt())
        .endDt(schedule.getEndDt())
        .isRepeat(schedule.isRepeat())
        .repeatCycle(schedule.getRepeatCycle())
        .color(schedule.getColor())
        .teamParticipantsSchedules(schedule.getTeamParticipantsSchedules())
        .teamParticipantsIds(getTeamParticipantsIdsFromSchedules(schedule.getTeamParticipantsSchedules()))
        .teamParticipantsName(getTeamParticipantsNameFromSchedules(schedule.getTeamParticipantsSchedules()))
        .teamRoles(getTeamParticipantsRoleFromSchedules(schedule.getTeamParticipantsSchedules()))
        .build();
  }

  public static Page<ScheduleDto> of(Page<Schedule> schedules) {
    return schedules.map(ScheduleDto::of);
  }

  private static List<Long> getTeamParticipantsIdsFromSchedules(List<TeamParticipantsSchedule> teamParticipantsSchedules) {
    List<Long> teamParticipantsIds = new ArrayList<>();
    if (teamParticipantsSchedules != null) {
      for (TeamParticipantsSchedule teamParticipantsSchedule : teamParticipantsSchedules) {
        teamParticipantsIds.add(teamParticipantsSchedule.getTeamParticipants().getTeamParticipantsId());
      }
    }
    return teamParticipantsIds;
  }

  private static List<String> getTeamParticipantsNameFromSchedules(List<TeamParticipantsSchedule> teamParticipantsSchedules) {
    List<String> teamParticipantsNames = new ArrayList<>();
    if (teamParticipantsSchedules != null) {
      for (TeamParticipantsSchedule teamParticipantsSchedule : teamParticipantsSchedules) {
        teamParticipantsNames.add(teamParticipantsSchedule.getTeamParticipants().getMember().getName());
      }
    }
    return teamParticipantsNames;
  }

  private static List<TeamRole> getTeamParticipantsRoleFromSchedules(List<TeamParticipantsSchedule> teamParticipantsSchedules) {
    List<TeamRole> teamParticipantsRoles = new ArrayList<>();
    if (teamParticipantsSchedules != null) {
      for (TeamParticipantsSchedule teamParticipantsSchedule : teamParticipantsSchedules) {
        teamParticipantsRoles.add(teamParticipantsSchedule.getTeamParticipants().getTeamRole());
      }
    }
    return teamParticipantsRoles;
  }

}

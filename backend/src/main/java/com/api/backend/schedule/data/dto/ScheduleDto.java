package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.enetity.Schedule;
import com.api.backend.team.data.entity.TeamParticipants;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private LocalTime time;
  private boolean isRepeat;
  private LocalDateTime repeatCycle;
  private String color;
  private List<TeamParticipants> teamParticipants;

  public static ScheduleDto of(Schedule schedule) {
    LocalDateTime startDt = schedule.getStartDt();
    LocalTime time = startDt.toLocalTime();

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
        .teamParticipants(schedule.getTeamParticipants())
        .time(time)
        .build();
  }

  public static List<ScheduleDto> of(Page<Schedule> schedules) {
    if (schedules != null) {
      List<ScheduleDto> scheduleDtoList = new ArrayList<>();
      for (Schedule schedule : schedules) {
        scheduleDtoList.add(ScheduleDto.of(schedule));
      }
      return scheduleDtoList;
    }
    return new ArrayList<>();
  }
}

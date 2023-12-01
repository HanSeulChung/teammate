package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.enetity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
  private boolean repeatYn;
  private LocalDate repeatCycle;
  private String color;
  private HashMap<Long, String> scheduleParticipantMap;

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
        .repeatYn(schedule.isRepeatYn())
        .repeatCycle(schedule.getRepeatCycle())
        .color(schedule.getColor())
        .scheduleParticipantMap(schedule.getScheduleParticipantMap())
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

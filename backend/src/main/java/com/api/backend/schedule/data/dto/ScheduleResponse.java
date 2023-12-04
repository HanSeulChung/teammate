package com.api.backend.schedule.data.dto;

import com.api.backend.team.data.entity.TeamParticipants;
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
public class ScheduleResponse {

  private Long scheduleId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime datetime;
  private String place;
  private List<TeamParticipants> teamParticipants;

  public static ScheduleResponse from(ScheduleDto scheduleDto) {
    LocalDateTime combinedDateTime = LocalDateTime.of(
        scheduleDto.getStartDt().toLocalDate(),
        scheduleDto.getTime()
    );
    return ScheduleResponse.builder()
        .scheduleId(scheduleDto.getScheduleId())
        .datetime(combinedDateTime)
        .place(scheduleDto.getPlace())
        .teamParticipants(scheduleDto.getTeamParticipants())
        .build();
  }

  public static List<ScheduleResponse> from(List<ScheduleDto> scheduleDtoList) {
    if (scheduleDtoList != null) {
      List<ScheduleResponse> scheduleResponses = new ArrayList<>();
      for (ScheduleDto dto : scheduleDtoList) {
        scheduleResponses.add(from(dto));
      }
      return scheduleResponses;
    }
    return new ArrayList<>();
  }
}

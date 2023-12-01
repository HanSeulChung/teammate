package com.api.backend.schedule.data.dto;

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
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private String place;

  public static ScheduleResponse from(ScheduleDto scheduleDto) {
    return ScheduleResponse.builder()
        .scheduleId(scheduleDto.getScheduleId())
        .startDt(scheduleDto.getStartDt())
        .endDt(scheduleDto.getEndDt())
        .place(scheduleDto.getPlace())
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

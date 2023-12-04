package com.api.backend.schedule.data.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleEditResponse {
  private Long scheduleId;
  private String title;
  private String contents;
  private LocalDateTime currentDt;
  private String place;

  public static ScheduleEditResponse from(ScheduleDto scheduleDto) {
    return ScheduleEditResponse.builder()
        .scheduleId(scheduleDto.getScheduleId())
        .title(scheduleDto.getTitle())
        .contents(scheduleDto.getContent())
        .currentDt(scheduleDto.getStartDt())
        .place(scheduleDto.getPlace())
        .build();
  }
}

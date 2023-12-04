package com.api.backend.schedule.data.dto;

import com.api.backend.team.data.entity.TeamParticipants;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleRequest {

  private Long scheduleId;
  private Long teamId;
  private Long categoryId;
  @NotBlank(message = "일정 제목을 입력해주세요.")
  private String title;
  private String content;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private LocalTime time;
  private String place;

  //TODO: 추후 일정 반복 기능 구현 예정
  private boolean isRepeat;
  private LocalDateTime repeatCycle;

  private String color;
  private List<TeamParticipants> teamParticipants;
}

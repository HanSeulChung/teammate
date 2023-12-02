package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.enetity.Schedule;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
  private String title;
  private String content;
  private String place;
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private boolean isRepeat;
  private LocalDateTime repeatCycle;
  private String color;
  private List<TeamParticipants> teamParticipants;
}

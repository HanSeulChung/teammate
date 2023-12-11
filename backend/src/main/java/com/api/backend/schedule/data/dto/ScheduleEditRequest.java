package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.type.RepeatCycle;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleEditRequest {
  private Long scheduleId;
  private Long teamId;
  private Long categoryId;
  private String title;
  private String content;
  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private String place;
  private boolean isRepeat;
  private RepeatCycle repeatCycle;
  private List<Long> teamParticipantsIds;
}

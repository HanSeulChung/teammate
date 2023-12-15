package com.api.backend.schedule.data.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EditOption {
  THIS_SCHEDULE("이 일정"),
  PAST_AND_FUTURE_SCHEDULES("이 일정 및 향후 일정"),
  ALL_SCHEDULES("모든 일정");
  private final String description;
}

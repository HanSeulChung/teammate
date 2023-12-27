package com.api.backend.schedule.data.dto;

import com.api.backend.schedule.data.type.EditOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ScheduleDeleteRequest {
  private Long scheduleId;
  private Long teamId;
  private Long teamParticipantsId;
  private EditOption editOption;
}

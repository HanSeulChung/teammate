package com.api.backend.schedule.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ScheduleDeleteRequest {
  @NotNull(message = "scheduleId는 필수 값입니다.")
  @Schema(description = "schedule id", example = "1L")
  private Long scheduleId;

  @NotNull(message = "teamId는 필수 값입니다.")
  @Schema(description = "team id", example = "1L")
  private Long teamId;

  @NotNull(message = "teamParticipantId는 필수 값입니다.")
  @Schema(description = "teamParticipant id", example = "1L")
  private Long teamParticipantId;
}

package com.api.backend.category.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class ScheduleCategoryDeleteRequest {
  @NotNull(message = "categoryId는 필수 값입니다.")
  @Schema(description = "category id", example = "1L")
  private Long categoryId;

  @NotNull(message = "teamId는 필수 값입니다.")
  @Schema(description = "team id", example = "1L")
  private Long teamId;

  @NotNull(message = "participantId는 필수 값입니다.")
  @Schema(description = "participant id", example = "1L")
  private Long participantId;

  @Schema(description = "is moved", example = "true")
  private boolean isMoved;

  @Schema(description = "new category id", example = "2L")
  private Long newCategoryId;
}

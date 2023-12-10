package com.api.backend.comment.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteCommentsResponse {


  @NotNull
  @Schema(description = "deleted comment id", example = "6575d6fe99101a62f9710877")
  private String id;


  @NotNull
  @Schema(description = "deleted writer id", example = "12L")
  private Long writerId;

  @NotNull
  @Schema(description = "deleted comment title", example = "해당 사안 확인했습니다.")
  private String content;

  @NotNull
  @Schema(description = "deleted comment message", example = "삭제 되었습니다.")
  private String message;
}

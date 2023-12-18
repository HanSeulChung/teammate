package com.api.backend.documents.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteDocsResponse {

  @NotBlank
  @Schema(description = "deleted document id", example = "1L")
  private String id;

  @NotBlank
  @Schema(description = "deleted document title", example = "12월 10일 회의사항")
  private String title;

  @NotBlank
  @Schema(description = "deleted document message", example = "삭제 되었습니다.")
  private String message;

  @NotBlank
  @Schema(description = "deleted document deletedParticipantNickName", example = "김땡땡")
  private String deleteParticipantNickName;

  @NotBlank
  @Schema(description = "deleted document deletedParticipantId", example = "1L")
  private Long deleteParticipantId;

  @NotBlank
  @Schema(description = "deleted document teamId", example = "1L")
  private Long teamId;
}


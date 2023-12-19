package com.api.backend.documents.data.dto;

import static com.api.backend.notification.data.NotificationMessage.DELETE_DOCUMENT;

import com.api.backend.notification.data.dto.DtoValueExtractor;
import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.data.type.SenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteDocsResponse implements DtoValueExtractor {

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

  @Override
  public Long getExcludeMemberId() {
    return null;
  }

  @Override
  public Long getMemberId() {
    return null;
  }

  @Override
  public Long getExcludeTeamParticipantId() {
    return deleteParticipantId;
  }

  @Override
  public SenderType getSenderType() {
    return SenderType.TEAM_PARTICIPANTS;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.DOCUMENTS;
  }

  @Override
  public String getTeamNameOrTeamParticipantNickName() {
    return deleteParticipantNickName;
  }

  @Override
  public String getUrl() {
    return null;
  }

  @Override
  public String getSendMessage() {
    return DELETE_DOCUMENT;
  }
}


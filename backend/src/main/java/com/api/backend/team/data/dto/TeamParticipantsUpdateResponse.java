package com.api.backend.team.data.dto;

import static com.api.backend.notification.data.NotificationMessage.UPDATE_TEAM_PARTICIPANT_TEAM;

import com.api.backend.notification.data.dto.DtoValueExtractor;
import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.data.type.SenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamParticipantsUpdateResponse implements DtoValueExtractor {

  private Long teamId;
  private Long updateTeamParticipantId;
  private String updateTeamParticipantNickName;
  private String teamName;
  private String message;

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
    return updateTeamParticipantId;
  }

  @Override
  public SenderType getSenderType() {
    return SenderType.TEAM_PARTICIPANTS;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.INVITE;
  }

  @Override
  public String getTeamNameOrTeamParticipantNickName() {
    return updateTeamParticipantNickName;
  }

  @Override
  public String getUrl() {
    return null;
  }

  @Override
  public String getSendMessage() {
    return UPDATE_TEAM_PARTICIPANT_TEAM;
  }
}

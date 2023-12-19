package com.api.backend.team.data.dto;

import static com.api.backend.notification.data.NotificationMessage.KICK_OUT_TEAM;

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
@ToString
@Builder
public class TeamKickOutResponse implements DtoValueExtractor {
  private Long teamId;
  private Long userId;
  private Long kickOutMemberId;
  private String teamName;
  private String message;

  @Override
  public Long getExcludeMemberId() {
    return null;
  }

  @Override
  public Long getMemberId() {
    return kickOutMemberId;
  }

  @Override
  public Long getExcludeTeamParticipantId() {
    return null;
  }


  @Override
  public SenderType getSenderType() {
    return SenderType.MEMBER;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.KICKOUT;
  }

  @Override
  public String getTeamNameOrTeamParticipantNickName() {
    return teamName;
  }

  @Override
  public String getUrl() {
    return null;
  }

  @Override
  public String getSendMessage() {
    return KICK_OUT_TEAM;
  }
}

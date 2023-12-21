package com.api.backend.team.data.dto;

import static com.api.backend.notification.data.NotificationMessage.EXIT_TEAM_PARTICIPANT;

import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.transfers.TeamParticipantsNotifyByDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamParticipantsDeleteResponse implements TeamParticipantsNotifyByDto {
  private Long teamId;
  private Long teamParticipantsId;
  private String nickName;
  private String message;

  @Override
  public Long getExcludeTeamParticipantId() {
    return teamParticipantsId;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.EXIT_TEAM_PARTICIPANT;
  }

  @Override
  public String getTeamParticipantsNickName() {
    return nickName;
  }

  @Override
  public String getSendMessage() {
    return EXIT_TEAM_PARTICIPANT;
  }
}
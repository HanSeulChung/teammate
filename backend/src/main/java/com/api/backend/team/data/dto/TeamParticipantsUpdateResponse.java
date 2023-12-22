package com.api.backend.team.data.dto;

import static com.api.backend.notification.data.NotificationMessage.UPDATE_TEAM_PARTICIPANT_TEAM;

import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.transfers.TeamParticipantsNotifyByDto;
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
public class TeamParticipantsUpdateResponse implements TeamParticipantsNotifyByDto {

  private Long teamId;
  private Long updateTeamParticipantId;
  private String updateTeamParticipantNickName;
  private String message;


  @Override
  public Long getExcludeTeamParticipantId() {
    return updateTeamParticipantId;
  }


  @Override
  public AlarmType getAlarmType() {
    return AlarmType.INVITE;
  }

  @Override
  public String getTeamParticipantsNickName() {
    return updateTeamParticipantNickName;
  }

  @Override
  public String getSendMessage() {
    return UPDATE_TEAM_PARTICIPANT_TEAM;
  }

}

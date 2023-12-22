package com.api.backend.schedule.data.dto;

import com.api.backend.notification.data.NotificationMessage;
import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.transfers.MentionTeamParticipantsNotifyByDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmScheduleDeleteResponse implements MentionTeamParticipantsNotifyByDto {

  private String message;

  // 알람
  private Long teamId;
  private Long teamParticipantsId;
  private String teamParticipantsNickName;
  private String title;
  private List<Long> teamParticipantIds;
  @Override
  public List<Long> getMentionTeamParticipantIds() {
    return teamParticipantIds;
  }

  @Override
  public Long getExcludeTeamParticipantId() {
    return teamParticipantsId;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.SCHEDULE_DELETE;
  }

  @Override
  public String getSendMessage() {
    return NotificationMessage.getDeleteScheduleMessage(title);
  }

  @Override
  public String getTeamParticipantsNickName() {
    return teamParticipantsNickName;
  }
}

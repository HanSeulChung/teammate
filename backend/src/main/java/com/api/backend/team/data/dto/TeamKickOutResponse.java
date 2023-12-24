package com.api.backend.team.data.dto;

import com.api.backend.notification.data.NotificationMessage;
import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.transfers.MemberNotifyByDto;
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
public class TeamKickOutResponse implements MemberNotifyByDto {
  private Long teamId;
  private String teamName;
  private String message;

  // 알람
  private Long kickOutMemberId;
  private String alarmMessage;

  @Override
  public Long getMemberId() {
    return kickOutMemberId;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.KICKOUT;
  }

  @Override
  public String getSendMessage() {
    return NotificationMessage.getKickOutMessage(alarmMessage);
  }

}

package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;

public interface TeamParticipantsNotifyByDto {

  Long getExcludeTeamParticipantId();

  Long getTeamId();

  AlarmType getAlarmType();

  String getTeamParticipantsNickName();

  String getSendMessage();
}

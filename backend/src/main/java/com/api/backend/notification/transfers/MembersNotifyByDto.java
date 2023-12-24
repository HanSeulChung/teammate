package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;

public interface MembersNotifyByDto {

  String getSendMessage();

  AlarmType getAlarmType();

  String getTeamName();

  Long getExcludeMemberId();

  Long getTeamId();
}

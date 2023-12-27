package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;

public interface MemberNotifyByDto {
  String getSendMessage();

  AlarmType getAlarmType();

  Long getMemberId();
}

package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {
    "sendMessage",
    "alarmType",
    "memberId"
})
public interface MemberNotifyByDto {
  String getSendMessage();

  AlarmType getAlarmType();

  Long getMemberId();
}

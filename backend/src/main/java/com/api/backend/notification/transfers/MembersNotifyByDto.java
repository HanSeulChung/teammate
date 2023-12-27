package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {
    "sendMessage",
    "alarmType",
    "teamId",
    "excludeMemberId"
})
public interface MembersNotifyByDto {

  String getSendMessage();

  AlarmType getAlarmType();

  Long getExcludeMemberId();

  Long getTeamId();
}

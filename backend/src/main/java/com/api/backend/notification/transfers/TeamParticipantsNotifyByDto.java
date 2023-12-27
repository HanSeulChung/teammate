package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {
    "excludeTeamParticipantId",
    "teamId",
    "alarmType",
    "teamParticipantsNickName",
    "sendMessage"
})
public interface TeamParticipantsNotifyByDto {

  Long getExcludeTeamParticipantId();

  Long getTeamId();

  AlarmType getAlarmType();

  String getTeamParticipantsNickName();

  String getSendMessage();
}

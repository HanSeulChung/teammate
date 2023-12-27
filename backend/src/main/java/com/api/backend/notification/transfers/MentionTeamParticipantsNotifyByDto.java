package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(value = {
    "mentionTeamParticipantIds",
    "excludeTeamParticipantId",
    "alarmType",
    "sendMessage",
    "teamParticipantsNickName",
    "teamId"
})
public interface MentionTeamParticipantsNotifyByDto {

  List<Long> getMentionTeamParticipantIds();

  Long getExcludeTeamParticipantId();

  AlarmType getAlarmType();

  String getSendMessage();

  String getTeamParticipantsNickName();

  Long getTeamId();

}

package com.api.backend.notification.transfers;

import com.api.backend.notification.data.type.AlarmType;
import java.util.List;

public interface MentionTeamParticipantsNotifyByDto {

  List<Long> getMentionTeamParticipantIds();

  Long getExcludeTeamParticipantId();

  AlarmType getAlarmType();

  String getSendMessage();
}

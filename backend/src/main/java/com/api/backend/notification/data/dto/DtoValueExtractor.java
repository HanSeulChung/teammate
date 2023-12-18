package com.api.backend.notification.data.dto;

import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.data.type.SenderType;

public interface DtoValueExtractor {

  Long getExcludeMemberId();

  Long getMemberId();

  Long getExcludeTeamParticipantId();

  Long getTeamId();

  SenderType getSenderType();

  AlarmType getAlarmType();

  String getTeamNameOrTeamParticipantNickName();

  String getUrl();

  String getSendMessage();
}

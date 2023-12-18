package com.api.backend.notification.data.dto;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.type.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto{
  private AlarmType alarmType;
  private String teamName;
  private String nickName;
  private String message;
  private String targetUrl;

  public static NotificationDto from(Notification notification) {
    return NotificationDto.builder()
        .message(notification.getMessage())
        .teamName(notification.getTeamName() == null ? null : notification.getTeamName())
        .targetUrl(notification.getTargetUrl() == null ? null : notification.getTargetUrl())
        .alarmType(notification.getAlarmType())
        .build();
  }
}

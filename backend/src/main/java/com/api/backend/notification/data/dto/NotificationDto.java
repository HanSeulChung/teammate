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
  private String nickName;
  private String message;

  public static NotificationDto from(Notification notification) {
    return NotificationDto.builder()
        .message(notification.getMessage())
        .nickName(notification.getNickName() == null ? null : notification.getNickName())
        .alarmType(notification.getAlarmType())
        .build();
  }
}

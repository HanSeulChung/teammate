package com.api.backend.notification.data.dto;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.type.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
  private Type type;
  private String teamName;
  private String nickName;
  private String message;
  private String targetUrl;

  public static NotificationDto from(Notification notification) {
    return NotificationDto.builder()
        .message(notification.getMessage())
        .teamName(notification.getTeamName() == null ? null : notification.getTeamName())
        .teamName(notification.getTargetUrl() == null ? null : notification.getTargetUrl())
        .type(notification.getType())
        .build();
  }
}

package com.api.backend.notification.data.dto;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.type.Type;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsResponse {

  private Long notificationId;
  private Type type;
  private String teamName;
  private String nickName;
  private String message;
  private String targetUrl;
  private LocalDateTime createDt;

  public static NotificationsResponse from(Notification notification) {
    return NotificationsResponse.builder()
        .notificationId(notification.getNotificationId())
        .message(notification.getMessage())
        .type(notification.getType())
        .createDt(notification.getCreateDt())
        .teamName(notification.getTeamName())
        .nickName(notification.getNickName())
        .build();
  }

  public static Page<NotificationsResponse> fromDtos(Page<Notification> notificationsPage){
    return notificationsPage.map(NotificationsResponse::from);
  }
}

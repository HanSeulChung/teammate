package com.api.backend.notification.service;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

  private final NotificationRepository notificationRepository;

  public void saveAllNotification(List<Notification> notifications) {
    notificationRepository.saveAll(notifications);
  }


  public void saveNotification(Notification notification) {
    notificationRepository.save(notification);
  }
}

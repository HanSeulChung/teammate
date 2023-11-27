package com.api.backend.notification.data.repository;

import com.api.backend.notification.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}

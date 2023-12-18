package com.api.backend.notification.data.repository;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.team.data.entity.TeamParticipants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

  Page<Notification> findAllByTeamParticipants(TeamParticipants teamParticipants, Pageable pageable);

  Page<Notification> findAllByMember_MemberId(Long memberId, Pageable pageable);
}

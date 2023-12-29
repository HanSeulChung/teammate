package com.api.backend.notification.data.repository;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.team.data.entity.TeamParticipants;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

  Page<Notification> findAllByTeamParticipantsAndIsReadFalse(TeamParticipants teamParticipants, Pageable pageable);

  Page<Notification> findAllByMember_MemberIdAndIsReadFalse(Long memberId, Pageable pageable);

  @Transactional
  @Modifying
  @Query(
      value = "delete from notification n where n.notification_id in :ids",
      nativeQuery = true
  )
  void deleteAllByIdInQuery(@Param("ids") List<Long> notificationIds);
}

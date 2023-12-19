package com.api.backend.notification.service;

import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.repository.NotificationRepository;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import com.api.backend.team.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final TeamParticipantsService teamParticipantsService;
  private final TeamService teamService;

  public void saveAllNotification(List<Notification> notifications) {
    notificationRepository.saveAll(notifications);
  }


  public void saveNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  public Page<Notification> getTeamNotificationList(Long teamId, Long memberId, Pageable pageable) {
    TeamParticipants teamParticipants = teamParticipantsService.getTeamParticipant(teamId, memberId);

    teamService.isDeletedCheck(teamParticipants.getTeam());

    return notificationRepository.findAllByTeamParticipantsAndIsReadFalse(teamParticipants, pageable);
  }

  public Page<Notification> getMemberNotificationList(Long memberId, Pageable pageable) {
    return notificationRepository.findAllByMember_MemberIdAndIsReadFalse(memberId, pageable);
  }
}

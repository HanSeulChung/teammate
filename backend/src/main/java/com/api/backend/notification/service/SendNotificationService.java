package com.api.backend.notification.service;

import com.api.backend.member.data.entity.Member;
import com.api.backend.notification.data.dto.NotificationDto;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotificationService {

  private final EmitterService emitterService;
  private final TeamParticipantsService teamParticipantsService;

  public void sendNotifications(List<SseEmitter> sseEmitters, NotificationDto notificationDto) {

    if (sseEmitters.isEmpty()) {
      log.info("알람 항목이 없음");
      return;
    }

    for (SseEmitter emitter : sseEmitters) {
      emitterService.sendNotification(emitter, notificationDto);
    }
  }

  public List<SseEmitter> getTeamEmitters(
      Long teamId,
      List<TeamParticipants> teamParticipants
  ) {

    List<SseEmitter> sseEmitters = new ArrayList<>();
    for (TeamParticipants teamParticipant : teamParticipants) {
      String emitterId = EmitterService.createEmitterIdByTeamIdAndTeamParticipantId(
          teamId,
          teamParticipant.getTeamParticipantsId()
      );

      SseEmitter sseEmitter = emitterService.getTeamParticipantEmitters(teamId, emitterId);
      if (sseEmitter == null) {
        continue;
      }

      sseEmitters.add(
          sseEmitter
      );
    }
    return sseEmitters;
  }

  public List<SseEmitter> getMemberEmitters(List<Member> members) {
    List<SseEmitter> sseEmitters = new ArrayList<>();

    for (Member member : members) {
      SseEmitter sseEmitter = emitterService.getMemberEmitter(member.getMemberId());
      if (sseEmitter != null) {
        sseEmitters.add(sseEmitter);
      }
    }
    return sseEmitters;
  }
  @Transactional
  public List<Member> getSendMembers(Long teamId, Long excludeMemberId) {
    return teamParticipantsService.getTeamParticipantsByExcludeMemberId(
            teamId, excludeMemberId).stream()
        .map(TeamParticipants::getMember)
        .collect(Collectors.toList());
  }
}
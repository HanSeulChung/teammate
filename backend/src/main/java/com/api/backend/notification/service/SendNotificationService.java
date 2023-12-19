package com.api.backend.notification.service;

import static com.api.backend.notification.data.type.SenderType.MEMBER;
import static com.api.backend.notification.data.type.SenderType.MEMBERS;
import static com.api.backend.notification.data.type.SenderType.TEAM_PARTICIPANTS;
import static com.api.backend.notification.data.type.SenderType.TEAM_PARTICIPANTS_URL;

import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.notification.data.dto.DtoValueExtractor;
import com.api.backend.notification.data.dto.NotificationDto;
import com.api.backend.notification.data.entity.Notification;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotificationService {

  private final EmitterService emitterService;
  private final NotificationService notificationService;
  private final TeamParticipantsService teamParticipantsService;

  @Async("EMITTER_SEND_EXECUTOR")
  public void convertToDtoAndSendOrThrowsNotFoundClass(DtoValueExtractor sendInfo) {
    List<Notification> notifications = new ArrayList<>();

    if (MEMBER.equals(sendInfo.getSenderType())) {
      notificationService.saveNotification(
          memberNotifySend(sendInfo)
      );

      return;
    } else if (MEMBERS.equals(sendInfo.getSenderType())) {
      notifications.addAll(
          membersNotifySend(sendInfo)
      );
    } else if (TEAM_PARTICIPANTS.equals(sendInfo.getSenderType())) {
      notifications.addAll(
          teamParticipantsSendNotify(sendInfo)
      );
    } else if (TEAM_PARTICIPANTS_URL.equals(sendInfo.getSenderType())) {
      notifications.addAll(
        teamParticipantsIncludeURLNotifySend(sendInfo)
      );
    } else {
      throw new CustomException();
    }

    if (notifications.isEmpty()) {
      return;
    }

    notificationService.saveAllNotification(notifications);
  }

  public Notification memberNotifySend(DtoValueExtractor info) {

    Notification result = Notification
        .convertToMemberNotify(
            info.getMemberId()
            , info.getTeamNameOrTeamParticipantNickName()
            , info.getSendMessage()
            , info.getAlarmType()
        );
    SseEmitter emitter = emitterService.getMemberEmitter(info.getMemberId());

    if (emitter == null) {
      return result;
    }

    emitterService.sendNotification(emitter, result);

    return result;
  }

  public List<Notification> teamParticipantsSendNotify(DtoValueExtractor info) {
    String nickName = info.getTeamNameOrTeamParticipantNickName();

    List<TeamParticipants> teamParticipants = teamParticipantsService
        .getTeamParticipantsExcludeId(
            info.getExcludeTeamParticipantId(), info.getTeamId()
        );

    if (teamParticipants.isEmpty()) {
      return null;
    }
    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertNickNameToTeamParticipantsNotify(
                i,
                nickName,
                info.getSendMessage(),
                info.getAlarmType()
            )
        )
        .collect(Collectors.toList());

    sendNotifications(
        getTeamEmitters(info.getTeamId(), teamParticipants),
        NotificationDto.from(notifications.get(0))
    );

    return notifications;
  }

  public List<Notification> membersNotifySend(DtoValueExtractor info) {
    List<Member> members = teamParticipantsService.getTeamParticipantsByExcludeMemberId(
            info.getTeamId(), info.getExcludeMemberId()).stream()
        .map(TeamParticipants::getMember)
        .collect(Collectors.toList());

    if (members.isEmpty()) {
      return null;
    }

    List<Notification> notifications = members
        .stream().map(i ->
            Notification.convertToMemberNotify(
                i,
                info.getTeamNameOrTeamParticipantNickName(),
                info.getSendMessage(),
                info.getAlarmType()
            )
        )
        .collect(Collectors.toList());

    sendNotifications(
        getMemberEmitters(members),
        NotificationDto.from(notifications.get(0))
    );

    return notifications;
  }

  public List<Notification> teamParticipantsIncludeURLNotifySend(DtoValueExtractor info) {

    List<TeamParticipants> teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
        info.getExcludeTeamParticipantId(), info.getTeamId()
    );

    if (teamParticipants.isEmpty()) {
      return null;
    }

    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertUrlToTeamParticipantsNotify(
                i, info.getUrl(), info.getSendMessage(), info.getAlarmType()
            )
        )
        .collect(Collectors.toList());

    sendNotifications(
        getTeamEmitters(info.getTeamId(), teamParticipants),
        NotificationDto.from(notifications.get(0))
    );

    return notifications;
  }

  public void sendNotifications(List<SseEmitter> sseEmitters, NotificationDto notificationDto) {

    if (sseEmitters.isEmpty()) {
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
}
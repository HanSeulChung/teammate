package com.api.backend.notification.service;

import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_ID_AND_TEAM_ID_NOT_FOUND_EXCEPTION;
import static com.api.backend.notification.data.NotificationMessage.CREATE_DOCUMENT;
import static com.api.backend.notification.data.NotificationMessage.DELETE_DOCUMENT;
import static com.api.backend.notification.data.NotificationMessage.EXIT_TEAM_PARTICIPANT;
import static com.api.backend.notification.data.NotificationMessage.KICK_OUT_TEAM;
import static com.api.backend.notification.data.NotificationMessage.UPDATE_TEAM_PARTICIPANT_TEAM;
import static com.api.backend.notification.data.type.Type.DOCUMENTS;

import com.api.backend.documents.data.dto.DeleteDocsResponse;
import com.api.backend.documents.data.dto.DocumentResponse;
import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.notification.data.dto.NotificationDto;
import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.type.Type;
import com.api.backend.team.data.dto.TeamDisbandResponse;
import com.api.backend.team.data.dto.TeamKickOutResponse;
import com.api.backend.team.data.dto.TeamParticipantsDeleteResponse;
import com.api.backend.team.data.dto.TeamParticipantsUpdateResponse;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Delete;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Async
@Slf4j
@RequiredArgsConstructor
public class SendNotificationService {

  private final EmitterService emitterService;
  private final NotificationService notificationService;
  private final TeamParticipantsService teamParticipantsService;
  @Async("EMITTER_SEND_EXECUTOR")
  public void convertToDtoAndSendOrThrowsNotFoundClass(Object source) {

    List<Notification> notifications = new ArrayList<>();

    if (source instanceof TeamKickOutResponse) { // 팀원 강퇴
      handleTeamKickOutAndNotifySend((TeamKickOutResponse) source);
    }else if (source instanceof TeamParticipantsUpdateResponse) { // 팀 참가
      notifications = handleTeamParticipantsUpdateAndNotifySend((TeamParticipantsUpdateResponse) source);
    }else if (source instanceof TeamParticipantsDeleteResponse) { // 팀(나 자신) 탈퇴
      notifications = handleTeamParticipantsDeleteAndNotifySend((TeamParticipantsDeleteResponse) source);
    } else if (source instanceof TeamDisbandResponse) { // 팀 해체
      notifications = handleTeamDisbandAndNotifySend((TeamDisbandResponse) source);
    } else if (source instanceof DocumentResponse) { // 문서 알람
      notifications = handleDocumentAndNotifySend((DocumentResponse) source);
    } else if (source instanceof Delete) {
      notifications = handleDeleteDocumentAndNotifySend((DeleteDocsResponse) source);
    }

    if (notifications.isEmpty()) {
      return;
    }
    notificationService.saveAllNotification(notifications);
  }

  private List<Notification> handleDeleteDocumentAndNotifySend(DeleteDocsResponse response) {
    List<TeamParticipants> teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
        response.getDeleteParticipantId(), response.getTeamId()
    );

    if (teamParticipants.isEmpty()) {
      return null;
    }

    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertNickNameToTeamParticipantsNotify(i, response.getDeleteParticipantNickName() ,DELETE_DOCUMENT , DOCUMENTS)
        )
        .collect(Collectors.toList());

    sendNotifications(
        getTeamEmitters(response.getTeamId(), teamParticipants),
        NotificationDto.from(notifications.get(0))
    );

    return notifications;
  }

  public void handleTeamKickOutAndNotifySend(TeamKickOutResponse response) {
    Notification result = Notification
        .convertToMemberNotify(
            response.getKickOutMemberId()
            , response.getTeamName()
            , KICK_OUT_TEAM
            , Type.KICKOUT
        );
    notificationService.saveNotification(result);

    SseEmitter emitter = emitterService.getMemberEmitter(response.getKickOutMemberId());

    if (emitter == null) {
      return;
    }

    emitterService.sendNotification(emitter,result);
  }

  public List<Notification> handleTeamParticipantsUpdateAndNotifySend(TeamParticipantsUpdateResponse response) {
    String nickName = response.getUpdateTeamParticipantNickName();

    List<TeamParticipants> teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
        response.getUpdateTeamParticipantId(), response.getTeamId()
    );

    if (teamParticipants.isEmpty()) {
      return null;
    }
    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertNickNameToTeamParticipantsNotify(
                i,
                nickName ,
                UPDATE_TEAM_PARTICIPANT_TEAM ,
                Type.INVITE
            )
        )
        .collect(Collectors.toList());

    sendNotifications(
        getTeamEmitters(response.getTeamId(), teamParticipants),
        NotificationDto.from(notifications.get(0))
        );

    return notifications;
  }

  public List<Notification> handleTeamParticipantsDeleteAndNotifySend(TeamParticipantsDeleteResponse response) {
    String nickName = response.getNickName();

    List<TeamParticipants> teamParticipants = teamParticipantsService.getTeamParticipantsByExcludeMemberId(
        response.getTeamParticipantsId(),response.getTeamId());

    if (teamParticipants.isEmpty()) {
      return null;
    }

    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertNickNameToTeamParticipantsNotify(i, nickName ,EXIT_TEAM_PARTICIPANT , Type.EXIT_TEAM_PARTICIPANT)
        )
        .collect(Collectors.toList());

    List<SseEmitter> sseEmitters = getTeamEmitters(response.getTeamId(),teamParticipants);

    if (notifications.isEmpty()) {
      return null;
    }

    sendNotifications(
        sseEmitters,
        NotificationDto.from(notifications.get(0))
    );

    return notifications;
  }

  public List<Notification> handleTeamDisbandAndNotifySend(TeamDisbandResponse response) {
    List<Member> members = teamParticipantsService.getTeamParticipantsByExcludeMemberId(
            response.getTeamId(), response.getMemberId()).stream()
        .map(TeamParticipants::getMember)
        .collect(Collectors.toList());


    if (members.isEmpty()) {
      return null;
    }

    List<Notification> notifications = members
        .stream().map(i ->
            Notification.convertToMemberNotify(
                i,
                response.getTeamName(),
                EXIT_TEAM_PARTICIPANT ,
                Type.EXIT_TEAM_PARTICIPANT
            )
        )
        .collect(Collectors.toList());

    sendNotifications(
        getMemberEmitters(members),
        NotificationDto.from(notifications.get(0))
    );

    return notifications;
  }
  public List<Notification> handleDocumentAndNotifySend(DocumentResponse response) {
    if (response.getTeamId() == null && response.getId() == null) {
      throw new CustomException(DOCUMENT_ID_AND_TEAM_ID_NOT_FOUND_EXCEPTION);
    }
    String targetUrl = "/team/" + response.getTeamId() +"/documents/" + response.getId();

    List<TeamParticipants> teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
        response.getWriterId(), response.getTeamId()
    );

    if (teamParticipants.isEmpty()) {
      return null;
    }

    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertUrlToTeamParticipantsNotify(i, targetUrl ,CREATE_DOCUMENT , DOCUMENTS)
        )
        .collect(Collectors.toList());

    sendNotifications(
        getTeamEmitters(response.getTeamId(), teamParticipants),
        NotificationDto.from(notifications.get(0))
    );
    return notifications;
  }

  public void sendNotifications(List<SseEmitter> sseEmitters, NotificationDto notificationDto) {

    if (sseEmitters.isEmpty()) {
      return;
    }

    for (SseEmitter emitter : sseEmitters) {
      emitterService.sendNotification(emitter,notificationDto);
    }
  }

  public List<SseEmitter> getTeamEmitters(Long teamId,
      List<TeamParticipants> teamParticipants) {

    List<SseEmitter> sseEmitters = new ArrayList<>();
    for (TeamParticipants teamParticipant : teamParticipants) {
      String emitterId = EmitterService.createEmitterIdByTeamIdAndTeamParticipantId(
          teamId,
          teamParticipant.getTeamParticipantsId()
      );

      sseEmitters.add(
          emitterService.getTeamParticipantEmitters(teamId, emitterId)
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
package com.api.backend.notification.data.converter;

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
import com.api.backend.notification.service.EmitterService;
import com.api.backend.notification.service.NotificationService;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class TypeConverter {

  private final EmitterService emitterService;
  private final NotificationService notificationService;
  private final TeamParticipantsService teamParticipantsService;

  @Async
  public void convertToDtoAndSendOrThrowsNotFoundClass(Object source) {

    List<TeamParticipants> teamParticipants;
    List<Notification> notifications = new ArrayList<>();
    // 보내야 할 알람 data
    NotificationDto notificationDto = new NotificationDto();
    // 구독한 유저들
    List<SseEmitter> sseEmitters = new ArrayList<>();

    if (source instanceof TeamKickOutResponse) { // 팀원 강퇴
      TeamKickOutResponse response = (TeamKickOutResponse) source;

      Notification result = Notification
          .convertToMemberNotify(
              Member.builder().memberId(response.getKickOutMemberId()).build()
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

      return;
    }else if (source instanceof TeamParticipantsUpdateResponse) { // 팀 참가
      TeamParticipantsUpdateResponse response = (TeamParticipantsUpdateResponse) source;
      String nickName = response.getUpdateTeamParticipantNickName();

      teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
          response.getUpdateTeamParticipantId(), response.getTeamId()
      );


      notifications = teamParticipants
          .stream().map(i ->
              Notification.convertNickNameToTeamParticipantsNotify(i, nickName ,UPDATE_TEAM_PARTICIPANT_TEAM , Type.INVITE)
          )
          .collect(Collectors.toList());

      List<String> emitterIds = teamParticipants.stream()
          .map(i -> EmitterService
              .createEmitterIdByTeamIdAndTeamParticipantId(response.getTeamId(),i.getTeamParticipantsId())
          )
          .collect(Collectors.toList());

      sseEmitters = emitterIds.stream()
          .map(i -> emitterService.getTeamParticipantEmitters(response.getTeamId(), i))
          .collect(Collectors.toList());

      // 보내야 할 알람
      notificationDto = NotificationDto.from(notifications.get(0));

    }else if (source instanceof TeamParticipantsDeleteResponse) { // 팀 탈퇴
      TeamParticipantsDeleteResponse response = (TeamParticipantsDeleteResponse) source;
      String nickName = response.getNickName();
      teamParticipants = teamParticipantsService.getTeamParticipantsByExcludeMemberId(
          response.getTeamParticipantsId(),response.getTeamId());

      if (teamParticipants.isEmpty()) {
        return;
      }

      notifications = teamParticipants
          .stream().map(i ->
              Notification.convertNickNameToTeamParticipantsNotify(i, nickName ,EXIT_TEAM_PARTICIPANT , Type.EXIT_TEAM_PARTICIPANT)
          )
          .collect(Collectors.toList());

      List<String> emitterIds = teamParticipants.stream()
          .map(i -> EmitterService
              .createEmitterIdByTeamIdAndTeamParticipantId(response.getTeamId(),i.getTeamParticipantsId())
          )
          .collect(Collectors.toList());

      sseEmitters = emitterIds.stream()
          .map(i -> emitterService.getTeamParticipantEmitters(response.getTeamId(), i))
          .collect(Collectors.toList());

      notificationDto = NotificationDto.from(notifications.get(0));

    } else if (source instanceof TeamDisbandResponse) { // 팀 해체
      TeamDisbandResponse response = (TeamDisbandResponse) source;

      // 보내야 할 맴버
      List<Member> members = teamParticipantsService.getTeamParticipantsByExcludeMemberId(
          response.getTeamId(), response.getMemberId()).stream()
          .map(TeamParticipants::getMember)
          .collect(Collectors.toList());


      if (members.isEmpty()) {
        return;
      }

      // 각 맴버에게 보내는 알람
      notifications = members
          .stream().map(i ->
              Notification.convertToMemberNotify(
                  i,
                  response.getTeamName(),
                  EXIT_TEAM_PARTICIPANT ,
                  Type.EXIT_TEAM_PARTICIPANT
              )
          )
          .collect(Collectors.toList());

      // 맴버들에게 보내는 알람은 고정적이기에 하나의 DTO만 반환
      notificationDto = NotificationDto.from(notifications.get(0));

      // 맴버별 emitter 객체 불러오기
      for (Member member : members) {
        SseEmitter sseEmitter = emitterService.getMemberEmitter(member.getMemberId());
        if (sseEmitter != null) {
          sseEmitters.add(sseEmitter);
        }
      }


    } else if (source instanceof DocumentResponse) {
      DocumentResponse response = (DocumentResponse) source;

      if (response.getTeamId() == null && response.getId() == null) {
        throw new CustomException(DOCUMENT_ID_AND_TEAM_ID_NOT_FOUND_EXCEPTION);
      }
      String targetUrl = "/team/" + response.getTeamId() +"/documents/" + response.getId();

      teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
          response.getWriterId(), response.getTeamId()
      );

      if (teamParticipants.isEmpty()) {
        return;
      }

      notifications = teamParticipants
          .stream().map(i ->
              Notification.convertUrlToTeamParticipantsNotify(i, targetUrl ,CREATE_DOCUMENT , DOCUMENTS)
          )
          .collect(Collectors.toList());

      List<String> emitterIds = teamParticipants.stream()
          .map(i -> EmitterService
              .createEmitterIdByTeamIdAndTeamParticipantId(response.getTeamId(),i.getTeamParticipantsId())
          )
          .collect(Collectors.toList());


      for (String emitterId : emitterIds) {
        SseEmitter sseEmitter = emitterService.getTeamParticipantEmitters(response.getTeamId(), emitterId);
        if (sseEmitter != null) {
          sseEmitters.add(sseEmitter);
        }
      }

      // 보내야 할 알람
      notificationDto = NotificationDto.from(notifications.get(0));
    } else if (source instanceof DeleteDocsResponse) {
      DeleteDocsResponse response = (DeleteDocsResponse) source;


      teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
          response.getDeleteParticipantId(), response.getTeamId()
      );

      if (teamParticipants.isEmpty()) {
        return;
      }

      notifications = teamParticipants
          .stream().map(i ->
              Notification.convertNickNameToTeamParticipantsNotify(i, response.getDeleteParticipantNickName() ,DELETE_DOCUMENT , DOCUMENTS)
          )
          .collect(Collectors.toList());

      List<String> emitterIds = teamParticipants.stream()
          .map(i -> EmitterService
              .createEmitterIdByTeamIdAndTeamParticipantId(response.getTeamId(),i.getTeamParticipantsId())
          )
          .collect(Collectors.toList());


      for (String emitterId : emitterIds) {
        SseEmitter sseEmitter = emitterService.getTeamParticipantEmitters(response.getTeamId(), emitterId);
        if (sseEmitter != null) {
          sseEmitters.add(sseEmitter);
        }
      }

      // 보내야 할 알람
      notificationDto = NotificationDto.from(notifications.get(0));
    }

    // 맴버에게 알람 저장
    notificationService.saveAllNotification(notifications);

    if (sseEmitters.isEmpty()) {
      return;
    }

    // emitter 별로 보내는 고정적인 dto 보내기
    for (SseEmitter emitter : sseEmitters) {
      emitterService.sendNotification(emitter,notificationDto);
    }
  }

}
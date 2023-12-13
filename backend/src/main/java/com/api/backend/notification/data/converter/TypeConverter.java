package com.api.backend.notification.data.converter;

import static com.api.backend.notification.data.NotificationMessage.EXIT_TEAM_PARTICIPANT;
import static com.api.backend.notification.data.NotificationMessage.KICK_OUT_TEAM;
import static com.api.backend.notification.data.NotificationMessage.UPDATE_TEAM_PARTICIPANT_TEAM;

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
import java.util.List;
import java.util.Map;
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

      SseEmitter emitter = emitterService.getMemberEmitter(result.getMember().getMemberId());

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

      List<Notification> notifications = teamParticipants
          .stream().map(i ->
              Notification.convertToTeamParticipantsNotify(i, nickName ,UPDATE_TEAM_PARTICIPANT_TEAM , Type.INVITE)
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
      sseEmitters = members.stream()
          .map(i -> emitterService.getMemberEmitter(i.getMemberId()))
          .collect(Collectors.toList());

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

  private void sendNotificationByEmitterMap(
      List<String> emitterIds, Map<String, SseEmitter> sseEmitterMap, NotificationDto notificationDto
  ) {
    for (int i = 0; i < emitterIds.size(); i++) {
      if (sseEmitterMap.containsKey(emitterIds.get(i))) {
        emitterService.sendNotification(sseEmitterMap.get(emitterIds.get(i)) , notificationDto);
      }
    }
  }

}
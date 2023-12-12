package com.api.backend.notification.data.converter;

import static com.api.backend.notification.data.NotificationMessage.EXIT_TEAM_PARTICIPANT;
import static com.api.backend.notification.data.NotificationMessage.KICK_OUT_TEAM;
import static com.api.backend.notification.data.NotificationMessage.UPDATE_TEAM_PARTICIPANT_TEAM;

import com.api.backend.member.data.entity.Member;
import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.type.Type;
import com.api.backend.notification.service.EmitterService;
import com.api.backend.notification.service.NotificationService;
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

    if (source instanceof TeamKickOutResponse) {
      Notification result = Notification.convertToNotifyByKickOutDto((TeamKickOutResponse) source);
      notificationService.saveNotification(result);
    } else if (source instanceof TeamParticipantsUpdateResponse) {
      TeamParticipantsUpdateResponse response = (TeamParticipantsUpdateResponse) source;
      String nickName = response.getUpdateTeamParticipantNickName();

      List<TeamParticipants> teamParticipants = teamParticipantsService.getTeamParticipantsExcludeId(
          response.getUpdateTeamParticipantId(), response.getTeamId()
      );

      List<Notification> notifications = teamParticipants
          .stream().map(i ->
              Notification.convertToNotification(i, nickName ,UPDATE_TEAM_PARTICIPANT_TEAM , Type.INVITE)
          )
          .collect(Collectors.toList());

      notificationService.saveAllNotification(notifications);

      Map<String,SseEmitter> sseEmitterMap = emitterService.getTeamParticipantEmitters(
          response.getTeamId(),
          response.getUpdateTeamParticipantId()
      );

      if (sseEmitterMap.isEmpty()) {
        return;
      }

      List<String> emitterIds = teamParticipants.stream()
          .map(i -> EmitterService
              .createEmitterIdByTeamIdAndTeamParticipantId(response.getTeamId(), i.getTeamParticipantsId())
          )
          .collect(Collectors.toList());

      sendNotificationByEmitterMap(emitterIds, sseEmitterMap, notifications);
    }else if (source instanceof TeamParticipantsDeleteResponse) {
      TeamParticipantsDeleteResponse response = (TeamParticipantsDeleteResponse) source;
      String nickName = response.getMessage();
      List<TeamParticipants> teamParticipants = getSendTeamParticipantList(
          response.getTeamParticipantsId(),response.getTeamId());

      List<Notification> notifications = teamParticipants
          .stream().map(i ->
              Notification.convertToNotification(i, nickName ,EXIT_TEAM_PARTICIPANT , Type.EXIT_TEAM_PARTICIPANT)
          )
          .collect(Collectors.toList());

      notificationService.saveAllNotification(notifications);

      Map<String ,SseEmitter> sseEmitterMap = emitterService.getTeamParticipantEmitters(
          response.getTeamId(),
          response.getTeamParticipantsId()
      );

      if (sseEmitterMap.isEmpty()) {
        return;
      }

      List<String> emitterIds = teamParticipants.stream()
          .map(i -> EmitterService
              .createEmitterIdByTeamIdAndTeamParticipantId(response.getTeamId(),i.getTeamParticipantsId())
          )
          .collect(Collectors.toList());

      sendNotificationByEmitterMap(emitterIds, sseEmitterMap, notifications);
    }
  }

  private List<TeamParticipants> getSendTeamParticipantList(Long excludeTeamParticipantId, Long teamId) {
    return teamParticipantsService.getTeamParticipantsExcludeId(
        excludeTeamParticipantId, teamId
    );
  }


  private void sendNotificationByEmitterMap(
      List<String> emitterIds, Map<String, SseEmitter> sseEmitterMap, List<Notification> notifications
  ) {
    for (int i = 0; i < emitterIds.size(); i++) {
      if (sseEmitterMap.containsKey(emitterIds.get(i))) {
        emitterService.sendNotification(sseEmitterMap.get(emitterIds.get(i)) , notifications.get(i));
      }
    }
  }
}
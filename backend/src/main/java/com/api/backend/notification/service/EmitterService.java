package com.api.backend.notification.service;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.notification.data.repository.EmitterRepository;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import com.api.backend.team.service.TeamService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterService {

  private final EmitterRepository emitterRepository;
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
  private final TeamParticipantsService teamParticipantsService;
  private final TeamService teamService;
  private static final String DUMMY_DATA = "dummy data";


  /**
   * 팀 참가자는 구독을 수행한다.
   **/
  public SseEmitter setTeamParticipantEmitter(
      Long teamId, Long memberId
  ) {
    if (!teamService.existById(teamId)) {
      throw new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION);
    }

    TeamParticipants teamParticipant = teamParticipantsService.getTeamParticipant(teamId, memberId);

    String emitterId = createEmitterIdByTeamIdAndTeamParticipantId(teamId , teamParticipant.getTeamParticipantsId());

    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.saveTeamParticipantsEmitter(teamId, emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    emitter.onCompletion(() -> emitterRepository.deleteTeamParticipantEmitter(teamId, emitterId));
    emitter.onTimeout(() -> emitterRepository.deleteTeamParticipantEmitter(teamId, emitterId));


    sendNotification(emitter, DUMMY_DATA);

    return emitter;
  }

  public static String createEmitterIdByTeamIdAndTeamParticipantId(Long teamId, Long teamParticipantId) {
    return teamId + "_" + teamParticipantId;
  }


  /**
   * 메시지 전송
   */
  public void sendNotification(SseEmitter emitter,Object data) {
    try {
      emitter.send(
          data, MediaType.APPLICATION_JSON
      );
    } catch (IOException exception) {
      log.info("발송 에러 : " + exception.getMessage());
    }
  }

  public SseEmitter getMemberEmitter(Long memberId) {
    return emitterRepository.getEmitter(memberId);
  }

  public Map<String,SseEmitter> getTeamParticipantEmitters(Long teamId, Long participantsId) {

    String excludeEmitterId = createEmitterIdByTeamIdAndTeamParticipantId(teamId,participantsId);

    return emitterRepository.getAllByTeamIdAndExcludeEmitterId(teamId, excludeEmitterId);
  }
}

package com.api.backend.notification.service;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.notification.data.repository.EmitterRepository;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import com.api.backend.team.service.TeamService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final EmitterRepository emitterRepository;
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
  private final TeamParticipantsService teamParticipantsService;
  private final TeamService teamService;
  private static final String DUMMY_DATA = "dummy data";

  /**
   * 팀 참가자는 구독을 수행한다.
   **/
  public SseEmitter setTeamParticipantEmitter(
      Long teamId, Long userId
  ) {
    if (teamService.existById(teamId)) {
      throw new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION);
    }

    TeamParticipants teamParticipant = teamParticipantsService.getTeamParticipant(teamId, userId);

    String emitterId = createTimeIncludeId(teamId , teamParticipant.getTeamParticipantsId());

    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.save(teamId, emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    emitter.onCompletion(() -> emitterRepository.deleteById(teamId, emitterId));
    emitter.onTimeout(() -> emitterRepository.deleteById(teamId, emitterId));

    // 503 에러를 방지하기 위한 더미 이벤트 전송
    String eventId = createTimeIncludeId(
        teamId,
        teamParticipant.getTeamParticipantsId()
    );
    sendNotification(emitter, eventId, emitterId, DUMMY_DATA);

    return emitter;
  }

  public static String createTimeIncludeId(Long teamId, Long teamParticipantId) {
    return teamId + "_" + teamParticipantId;
  }

  /**
   * 메시지 전송
   */
  private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .data(data)
      );
    } catch (IOException exception) {
      log.info("발송 에러 : " + exception.getMessage());
    }
  }
}

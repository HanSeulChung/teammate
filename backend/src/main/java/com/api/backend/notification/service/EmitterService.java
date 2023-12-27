package com.api.backend.notification.service;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.notification.data.repository.EmitterRepository;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import com.api.backend.team.service.TeamService;
import java.io.IOException;
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
  private final MemberRepository memberRepository;
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

    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    Long teamParticipantsId = teamParticipant.getTeamParticipantsId();
    emitterRepository.saveTeamParticipantsEmitter(
        teamId, teamParticipantsId, emitter
    );

    emitter.onCompletion(() -> emitterRepository.deleteTeamParticipantEmitter(teamId, teamParticipantsId));
    emitter.onTimeout(() ->
        emitterRepository.deleteTeamParticipantEmitter(teamId, teamParticipantsId)
    );


    sendNotification(emitter, DUMMY_DATA);

    return emitter;
  }

  /**
   * 회원은 구독을 수행한다.
   **/
  public SseEmitter setMemberEmitter(
      Long memberId
  ) {
    if (!memberRepository.existsById(memberId)) {
      throw new CustomException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION);
    }

    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.saveMemberEmitter(memberId,emitter);

    emitter.onCompletion(() -> emitterRepository.deleteMemberEmitter(memberId));
    emitter.onTimeout(() ->
        emitterRepository.deleteMemberEmitter(memberId)
    );


    sendNotification(emitter, DUMMY_DATA);

    return emitter;
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

  public SseEmitter getTeamParticipantEmitters(Long teamId, Long customId) {
    SseEmitter sseEmitter = emitterRepository.getTeamParticipantEmitter(teamId, customId);

    if (sseEmitter == null) {
      return null;
    }

    return sseEmitter;
  }
}

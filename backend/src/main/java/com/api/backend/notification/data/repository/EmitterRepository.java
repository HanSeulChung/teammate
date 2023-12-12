package com.api.backend.notification.data.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

  private final Map<Long, Map<String, SseEmitter>> teamEmitterMap = new ConcurrentHashMap<>();
  private final Map<Long, SseEmitter> memberEmitterMap = new ConcurrentHashMap<>();

  public void saveTeamParticipantsEmitter(Long teamId, String emitterId, SseEmitter sseEmitter) {
    if (!teamEmitterMap.containsKey(teamId)) {
      Map<String, SseEmitter> teamParticipantMap = new ConcurrentHashMap<>();
      teamParticipantMap.put(emitterId, sseEmitter);

      teamEmitterMap.put(teamId, teamParticipantMap);
    } else {
      Map<String, SseEmitter> teamParticipantMap = teamEmitterMap.get(teamId);
      teamParticipantMap.put(emitterId, sseEmitter);

      teamEmitterMap.put(teamId, teamParticipantMap);
    }
  }

  public void saveMemberEmitter(Long memberId, SseEmitter sseEmitter) {
    memberEmitterMap.put(memberId, sseEmitter);
  }

  public void deleteTeamParticipantEmitter(Long teamId, String emitterId) {
    if (!teamEmitterMap.containsKey(teamId)) {
      return;
    }

    Map<String, SseEmitter> teamParticipantMap = teamEmitterMap.get(teamId);
    teamParticipantMap.remove(emitterId);

    if (teamParticipantMap.isEmpty()) {
      teamEmitterMap.remove(teamId);
      return;
    }

    teamEmitterMap.put(teamId, teamParticipantMap);
  }
  public void deleteMemberEmitter(Long memberId) {
    memberEmitterMap.remove(memberId);
  }
  public Map<String,SseEmitter> getAllByTeamIdAndExcludeEmitterId(Long teamId, String excludeEmitterId) {
    Map<String, SseEmitter> teamParticipantsEmitters = teamEmitterMap.get(teamId);
    Map<String,SseEmitter> map = new ConcurrentHashMap<>();

    for (Map.Entry<String, SseEmitter> info : teamParticipantsEmitters.entrySet()) {
      if (!info.getKey().equals(excludeEmitterId)) {
        map.put(info.getKey(), info.getValue());
      }
    }

    return map;
  }

  public SseEmitter getEmitter(Long memberId) {
    if (memberEmitterMap.containsKey(memberId)) {
      return memberEmitterMap.get(memberId);
    }
    return null;
  }
}

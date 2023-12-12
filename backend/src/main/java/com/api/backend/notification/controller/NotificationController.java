package com.api.backend.notification.controller;

import com.api.backend.notification.service.EmitterService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final EmitterService emitterService;

  @GetMapping(value = "/subscribe/team/{teamId}", produces = "text/event-stream")
  public SseEmitter subscribeTeamRequest(
      Principal principal,
      @PathVariable(value = "teamId") Long teamId
  ) {
    return emitterService.setTeamParticipantEmitter(
        teamId, Long.valueOf(principal.getName())
    );
  }
}

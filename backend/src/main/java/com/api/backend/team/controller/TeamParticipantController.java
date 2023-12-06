package com.api.backend.team.controller;

import com.api.backend.team.service.TeamParticipantsService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamParticipantController {

  private final TeamParticipantsService teamParticipantsService;

  @DeleteMapping
  public ResponseEntity<String> deleteTeamParticipantRequest(
      Principal principal,
      @PathVariable(value = "teamId") Long teamId
  ) {
    return ResponseEntity.ok(
        teamParticipantsService.deleteTeamParticipant(principal.getName(), teamId)
    );
  }
}

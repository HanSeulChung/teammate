package com.api.backend.team.controller;

import com.api.backend.team.service.TeamParticipantsService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/participant")
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

  @PatchMapping("/{participantId}")
  public ResponseEntity<String> updateRoleTeamParticipantRequest(
      Principal principal,
      @RequestParam(value = "participantId") Long participantId,
      @PathVariable(value = "teamId") Long teamId
  ) {
    return ResponseEntity.ok(
        teamParticipantsService.updateRoleTeamParticipant(principal.getName(), participantId, teamId)
    );
  }
}

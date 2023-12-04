package com.api.backend.team.controller;

import com.api.backend.team.data.dto.CreateTeamRequest;
import com.api.backend.team.data.dto.TeamCreateResponse;
import com.api.backend.team.service.TeamService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

  private final TeamService teamService;

  @PostMapping
  public ResponseEntity<TeamCreateResponse> createTeamRequest(
      @RequestBody @Valid
      CreateTeamRequest teamRequest
  ) {
    return ResponseEntity.ok(
        TeamCreateResponse.from(
            teamService.createTeam(teamRequest)
        )
    );
  }

  @GetMapping("/{teamId}/code")
  public ResponseEntity<String> getTeamUrlRequest(
      @PathVariable("teamId") Long teamId
  ) {
    return ResponseEntity.ok(
        teamService.getTeamUrl(teamId)
    );
  }


}

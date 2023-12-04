package com.api.backend.team.controller;

import static com.api.backend.team.data.ResponseMessage.UPDATE_TEAM_PARTICIPANTS;

import com.api.backend.team.data.dto.TeamCreateRequest;
import com.api.backend.team.data.dto.TeamCreateResponse;
import com.api.backend.team.data.dto.TeamKickOutRequest;
import com.api.backend.team.data.dto.TeamKickOutResponse;
import com.api.backend.team.data.dto.UpdateTeamParticipantsResponse;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.service.TeamService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

  private final TeamService teamService;

  @PostMapping
  public ResponseEntity<TeamCreateResponse> createTeamRequest(
      @RequestBody @Valid
      TeamCreateRequest teamRequest,
      @RequestParam(value = "userId") String userId
  ) {
    return ResponseEntity.ok(
            teamService.createTeam(teamRequest,userId)
    );
  }

  @GetMapping("/{teamId}/code")
  public ResponseEntity<String> getTeamUrlRequest(
      @PathVariable("teamId") Long teamId
      // todo Princial를 통한 유저 객체 가져오기
  ) {
    return ResponseEntity.ok(
        teamService.getTeamUrl(teamId,null)
    );
  }

  @PostMapping("/{teamId}/{code}")
  public ResponseEntity<UpdateTeamParticipantsResponse> updateTeamParticipantRequest(
      @PathVariable("teamId") Long teamId,
      @PathVariable("code") String code
      // todo Princial를 통한 유저 객체 가져오기
  ) {
    Team team = teamService.updateTeamParticipants(teamId, code, null);
    return ResponseEntity.ok(
        UpdateTeamParticipantsResponse
            .builder().teamName(team.getName())
            .teamId(teamId)
            .message(team.getName() + UPDATE_TEAM_PARTICIPANTS)
            .build()
    );
  }

  @PostMapping("/kick-out")
  public ResponseEntity<TeamKickOutResponse> kickOutTeamParticipantsRequest(
      @RequestBody @Valid
      TeamKickOutRequest teamKickOutRequest
  ) {
    return ResponseEntity.ok(
        teamService.KickOutTeamParticipants(teamKickOutRequest)
    );
  }

}

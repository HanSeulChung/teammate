package com.api.backend.team.service;

import com.api.backend.team.data.dto.TeamRequest.Create;
import com.api.backend.team.data.dto.TeamCreateResponse;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  public TeamCreateResponse createTeam(Create teamRequest) {
    Team team = teamRepository.save(Team.createTeam(teamRequest));
    team.setInviteLink();
    return TeamCreateResponse.from(team);
  }
}

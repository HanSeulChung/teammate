package com.api.backend.team.service;

import com.api.backend.team.data.dto.TeamRequest.Create;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  @Transactional
  public Team createTeam(Create teamRequest) {
    Team team = teamRepository.save(Team.createTeam(teamRequest));
    team.setInviteLink();
    return team;
  }
}

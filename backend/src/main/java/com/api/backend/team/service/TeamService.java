package com.api.backend.team.service;

import com.api.backend.team.data.dto.CreateTeamRequest;
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
  public Team createTeam(CreateTeamRequest teamRequest) {
    Team team = teamRepository.save(
        Team.builder()
            .memberLimit(teamRequest.getMemberLimit())
            .name(teamRequest.getTeamName())
            // todo imge는 나중에 어떻게 처리를 해야할지 결정하면 다시 구현하겠다.
            .profileUrl(teamRequest.getTeamImg())
            .build()
    );
    team.setInviteLink();
    return team;
  }
}

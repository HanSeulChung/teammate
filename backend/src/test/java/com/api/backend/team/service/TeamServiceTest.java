package com.api.backend.team.service;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;
  @Mock
  private TeamParticipantsRepository teamParticipantsRepository;
  @InjectMocks
  private TeamService teamService;

  @Test
  void createTeam() {
    // todo img 저장 부분이 구현이 된다면 하겠습니다.
  }

  @Test
  @DisplayName("팀 초대 코드 url 로직")
  void getTeamUrl() {
    //given
    Long id = 1L;
    String userId = "1";
    Team team = Team.builder()
        .teamId(1L)
        .profileUrl("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    //when
    String result = teamService.getTeamUrl(id,userId);

    //then
    assertEquals(result,team.getInviteLink());
  }
}
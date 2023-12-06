package com.api.backend.team.service;

import static com.api.backend.team.data.ResponseMessage.DELETE_TEAM_PARTICIPANT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.type.TeamRole;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamParticipantsServiceTest {

  @Mock
  private TeamParticipantsRepository teamParticipantsRepository;

  @InjectMocks
  private TeamParticipantsService teamParticipantsService;

  @Test
  @DisplayName("나 자신을 해당 팀으로 부터 탈퇴 로직")
  void deleteTeamParticipant() {
    //given
    String userId = "1";
    Long teamId = 1L;
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.MATE).build();
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        anyLong(), anyLong()
    )).thenReturn(Optional.of(teamParticipants));
    doNothing().when(teamParticipantsRepository).delete(any());
    //when
    String result = teamParticipantsService.deleteTeamParticipant(userId, teamId);

    //then
    assertEquals(result,DELETE_TEAM_PARTICIPANT);
  }
}
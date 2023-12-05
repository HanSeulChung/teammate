package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.PASSWORD_NOT_MATCH_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_CODE_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_IS_DELETEING_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_IS_DELETE_TRUE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_EXIST_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.team.data.dto.TeamDisbandRequest;
import com.api.backend.team.data.dto.TeamKickOutRequest;
import com.api.backend.team.data.dto.TeamKickOutResponse;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import com.api.backend.team.data.type.TeamRole;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
  @DisplayName("팀 초대 코드 url 로직 - [성공]")
  void getTeamUrl_success() {
    //given
    Long id = 1L;
    String userId = "1";
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(id, Long.valueOf(userId)))
        .thenReturn(true);
    //when
    String result = teamService.getTeamUrl(id,userId);

    //then
    assertEquals(result,team.getInviteLink());
  }

  @Test
  @DisplayName("팀 초대 코드 url 로직 - [실패] 팀원이 아닐경우")
  void getTeamUrl_fail() {
    //given
    Long id = 1L;
    String userId = "1";
    Team team = Team.builder()
        .teamId(1L)
        .profileUrl("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(id, Long.valueOf(userId)))
        .thenReturn(false);
    //when
    CustomException result = assertThrows(CustomException.class,
        () -> teamService.getTeamUrl(id,userId));

    //then
    assertEquals(result.getErrorMessage(),TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(),500);
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 성공")
  void updateTeamParticipants_success(){
    //given
    Long id = 1L;
    String userId = "1";
    String code = "dsfefsefnklsd";
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(
        anyLong(), anyLong()
    )).thenReturn(false);
    //when
    Team result = teamService.updateTeamParticipants(id, code, userId);

    //then
    assertEquals(result.getTeamId(), team.getTeamId());
    assertEquals(result.getInviteLink(), team.getInviteLink());
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 실패[code]")
  void updateTeamParticipants_fail_code(){
    //given
    Long id = 1L;
    String userId = "1";
    String code = "sadsadasd";
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.updateTeamParticipants(id, code, userId)
    );

    //then
    assertEquals(result.getErrorMessage(),TEAM_CODE_NOT_VALID_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(),TEAM_CODE_NOT_VALID_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 실패[existUser]")
  void updateTeamParticipants_fail_exist_user(){
    //given
    Long id = 1L;
    String userId = "1";
    String code = "dsfefsefnklsd";
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(anyLong(),anyLong()))
        .thenReturn(true);

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.updateTeamParticipants(id, code, userId)
    );

    //then
    assertEquals(result.getErrorMessage(),TEAM_PARTICIPANTS_EXIST_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(),TEAM_PARTICIPANTS_EXIST_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀원 강퇴 로직 - 성공")
  void KickOutTeamParticipants_success(){
    //given
    TeamKickOutRequest request =
        new TeamKickOutRequest(1L, 1L, "testMessage");
    TeamParticipants teamParticipants = TeamParticipants
        .builder()
        .teamParticipantsId(1L)
        .teamRole(TeamRole.READER)
        .member(
            Member.builder().memberId(1L).build()
        ).build();
    TeamParticipants teamParticipants2 = TeamParticipants
        .builder()
        .teamParticipantsId(2L)
        .teamRole(TeamRole.READER)
        .member(
            Member.builder().memberId(1L).name("testUser").build()
        ).build();
    List<TeamParticipants> teamParticipantsList = List.of(teamParticipants);

    Team team = Team.builder()
        .teamParticipants(teamParticipantsList)
        .teamId(1L)
        .build();
    when(teamRepository.existsByTeamIdAndIsDelete(anyLong(), anyBoolean()))
        .thenReturn(false);
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(anyLong(), anyLong()))
        .thenReturn(Optional.of(teamParticipants2))
        .thenReturn(Optional.of(teamParticipants));
    doNothing().when(teamParticipantsRepository).delete(any());
    //when
    TeamKickOutResponse result = teamService.kickOutTeamParticipants(request, "2");

    //then
    assertEquals(result.getTeamId(),request.getTeamId());
    assertEquals(result.getUserId(),request.getUserId());
  }
  @Test
  @DisplayName("팀원 강퇴 로직 - 실패")
  void KickOutTeamParticipants_fail(){
    //given
    TeamKickOutRequest request =
        new TeamKickOutRequest(1L, 1L, "testMessage");

    Team team = Team.builder()
        .teamParticipants(new ArrayList<>())
        .teamId(1L)
        .build();

    when(teamRepository.existsByTeamIdAndIsDelete(anyLong(), anyBoolean()))
        .thenReturn(false);
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.kickOutTeamParticipants(request, "2")
    );

    //then
    assertEquals(result.getErrorCode().getCode(),
        TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getCode());
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getErrorMessage());
  }
}
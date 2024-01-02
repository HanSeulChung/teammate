package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.MEMBER_NOT_EQUALS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_EQUALS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_NOT_VALID_MATE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION;
import static com.api.backend.team.data.ResponseMessage.UPDATE_ROLE_TEAM_PARTICIPANT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.api.backend.file.service.FileProcessService;
import com.api.backend.file.type.FileFolder;
import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.dto.TeamParticipantUpdateRequest;
import com.api.backend.member.data.entity.Member;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.type.TeamRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class TeamParticipantsServiceTest {

  @Mock
  private TeamParticipantsRepository teamParticipantsRepository;
  @Mock
  private TeamService teamService;
  @Mock
  private FileProcessService fileProcessService;

  @InjectMocks
  private TeamParticipantsService teamParticipantsService;

  @Test
  @DisplayName("나 자신을 해당 팀으로 부터 탈퇴 로직 - 성공")
  void deleteTeamParticipant() {
    //given
    Long memberId = 1L;
    Long teamId = 1L;
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.MATE)
        .participantsProfileUrl("testUrl")
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        memberId, teamId
    )).thenReturn(Optional.of(teamParticipants));
    doNothing().when(fileProcessService).deleteImage(
        teamParticipants.getParticipantsProfileUrl()
    );
    doNothing().when(teamParticipantsRepository).delete(any());

    //when
    TeamParticipants result = teamParticipantsService.deleteTeamParticipantById(memberId, teamId);

    //then
    assertEquals(result.getTeamRole() , TeamRole.MATE);
  }

  @Test
  @DisplayName("나 자신을 해당 팀으로 부터 탈퇴 로직 - 실패[존재하지 않는 팀 참가자]")
  void deleteTeamParticipant_fail_empty_participant() {
    //given
    Long memberId = 1L;
    Long teamId = 1L;

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        memberId, teamId
    )).thenReturn(Optional.empty());

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamParticipantsService.deleteTeamParticipantById(memberId, teamId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("나 자신을 해당 팀으로 부터 탈퇴 로직 - 실패[리더]")
  void deleteTeamParticipant_fail_leader() {
    //given
    Long memberId = 1L;
    Long teamId = 1L;

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.LEADER)
        .participantsProfileUrl("testUrl")
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        memberId, teamId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamParticipantsService.deleteTeamParticipantById(memberId, teamId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION.getCode());
  }
  @Test
  @DisplayName("팀장이 팀원에게 권한부여 로직")
  void updateRoleTeamParticipant(){
    //given
    Long memberId = 1L;
    Long participantId = 1L;
    Long teamId = 1L;
    Team team = Team.builder().teamId(1L).build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.LEADER)
        .team(team)
        .build();
    TeamParticipants teamParticipants2 = TeamParticipants.builder()
        .teamRole(TeamRole.MATE)
        .team(team)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId,
        memberId
    )).thenReturn(Optional.of(teamParticipants));
    when(teamParticipantsRepository.findById(participantId))
        .thenReturn(Optional.of(teamParticipants2));

    //when
    String result = teamParticipantsService.updateRoleTeamParticipant(memberId, participantId,
        teamId);

    //then
    assertEquals(result,UPDATE_ROLE_TEAM_PARTICIPANT);
  }

  @Test
  @DisplayName("팀장이 팀원에게 권한부여 로직 실패[요청자가 리더가 아닌 경우]")
  void updateRoleTeamParticipant_fail_leader_valid(){
    //given
    Long memberId = 1L;
    Long participantId = 1L;
    Long teamId = 1L;
    Team team = Team.builder().teamId(1L).build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.MATE)
        .team(team)
        .build();


    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId,
        memberId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamParticipantsService.updateRoleTeamParticipant(
            memberId,
            participantId,
            teamId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀장이 팀원에게 권한부여 로직 - 실패[같은 팀 소속이 아닌 경우]")
  void updateRoleTeamParticipant_fail_equal_teamId(){
    //given
    Long memberId = 1L;
    Long participantId = 1L;
    Long teamId = 1L;
    Team team = Team.builder().teamId(2L).build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.LEADER)
        .build();
    TeamParticipants teamParticipants2 = TeamParticipants.builder()
        .teamRole(TeamRole.MATE)
        .team(team)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId,
        memberId
    )).thenReturn(Optional.of(teamParticipants));
    when(teamParticipantsRepository.findById(participantId))
        .thenReturn(Optional.of(teamParticipants2));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamParticipantsService.updateRoleTeamParticipant(
            memberId,
            participantId,
            teamId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_NOT_EQUALS_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_NOT_EQUALS_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀장이 팀원에게 권한부여 로직 - 실패[변경할려는 대상이 mate가 아닌 경우]")
  void updateRoleTeamParticipant_fail_not_valid_mate(){
    //given
    Long memberId = 1L;
    Long participantId = 1L;
    Long teamId = 1L;
    Team team = Team.builder().teamId(1L).build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.LEADER)
        .build();
    TeamParticipants teamParticipants2 = TeamParticipants.builder()
        .teamRole(TeamRole.LEADER)
        .team(team)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId,
        memberId
    )).thenReturn(Optional.of(teamParticipants));
    when(teamParticipantsRepository.findById(participantId))
        .thenReturn(Optional.of(teamParticipants2));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamParticipantsService.updateRoleTeamParticipant(
            memberId,
            participantId,
            teamId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_PARTICIPANT_NOT_VALID_MATE_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANT_NOT_VALID_MATE_EXCEPTION.getCode());
  }
  @Test
  @DisplayName("내가 속한 팀의 모든 팀원 조회 로직")
  void getTeamParticipants(){
    //given
    Long teamId = 1L;
    Long memberId = 1L;
    List<TeamParticipants> teamParticipantsList = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      teamParticipantsList.add(
          TeamParticipants.builder()
              .teamNickName("test" + i)
              .build()
      );
    }
    Team team = Team.builder()
        .isDelete(false)
        .teamParticipants(teamParticipantsList)
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamNickName("test")
        .team(team).build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId, memberId
    )).thenReturn(Optional.of(teamParticipants));
    doNothing().when(teamService).isDeletedCheck(team.getRestorationDt(), team.isDelete());

    //when
    List<TeamParticipants> result = teamParticipantsService.getTeamParticipants(teamId, memberId);

    //then
    for (int i = 0; i < 3; i++) {
      assertEquals(result.get(i).getTeamNickName(),
          teamParticipantsList.get(i).getTeamNickName());
    }
  }

  @Test
  @DisplayName("내가 속한 단건 팀 참가자 조회")
  void getTeamParticipant(){
    //given
    Long teamId = 1L;
    Long memberId = 1L;

    Team team = Team.builder()
        .isDelete(false)
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamNickName("test")
        .team(team).build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId,memberId
    )).thenReturn(Optional.of(teamParticipants));
    doNothing().when(teamService).isDeletedCheck(team.getRestorationDt(), team.isDelete());

    //when
    TeamParticipants result = teamParticipantsService.getTeamParticipant(teamId, memberId);

    //then
    assertEquals(result.getTeamNickName(),teamParticipants.getTeamNickName());
  }

  @Test
  @DisplayName("팀 참가자 수정 로직 - 성공")
  void updateParticipantContent() {
    //given
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "testImg", "test".getBytes()
    );
    String successImg = "testImg";
    TeamParticipantUpdateRequest request = TeamParticipantUpdateRequest.builder()
        .teamParticipantsId(1L)
        .participantImg(mockMultipartFile)
        .teamNickName("수정된 내용입니다.")
        .build();
    Long memberId = 1L;

    Team team = Team.builder()
        .isDelete(false)
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .member(
            Member.builder().memberId(1L).build()
        )
        .teamNickName("test")
        .team(team).build();

    when(teamParticipantsRepository.findById(anyLong()))
        .thenReturn(Optional.of(teamParticipants));
    when(fileProcessService.uploadImage(mockMultipartFile, FileFolder.PARTICIPANT))
        .thenReturn(successImg);

    //when
    TeamParticipants result = teamParticipantsService.updateParticipantContent(request, memberId);

    //then
    assertEquals(result.getTeamNickName(), request.getTeamNickName());
    assertEquals(result.getParticipantsProfileUrl(), successImg);
  }

  @Test
  @DisplayName("팀 참가자 프로필 수정 로직 - 실패")
  void updateParticipantContent_fail_not_valid_memberId(){
    //given
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "testImg", "test".getBytes()
    );
    TeamParticipantUpdateRequest request = TeamParticipantUpdateRequest.builder()
        .teamParticipantsId(1L)
        .participantImg(mockMultipartFile)
        .teamNickName("수정된 내용입니다.")
        .build();
    Long memberId = 2L;

    Team team = Team.builder()
        .isDelete(false)
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .member(
            Member.builder().memberId(1L).build()
        )
        .teamNickName("test")
        .team(team).build();

    when(teamParticipantsRepository.findById(anyLong()))
        .thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamParticipantsService.updateParticipantContent(request, memberId)
    );

    //then
    assertEquals(result.getErrorMessage(), MEMBER_NOT_EQUALS_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), MEMBER_NOT_EQUALS_EXCEPTION.getCode());
  }
}
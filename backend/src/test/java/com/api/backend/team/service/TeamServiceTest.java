package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.PASSWORD_NOT_MATCH_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_CODE_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_IS_DELETEING_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_IS_DELETE_TRUE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_LIMIT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_DELETEING_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_EXIST_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;
import static com.api.backend.team.data.ResponseMessage.KICK_OUT_TEAM_PARTICIPANTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.backend.file.service.FileProcessService;
import com.api.backend.file.type.FileFolder;
import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.team.data.dto.TeamCreateRequest;
import com.api.backend.team.data.dto.TeamCreateResponse;
import com.api.backend.team.data.dto.TeamDisbandRequest;
import com.api.backend.team.data.dto.TeamKickOutRequest;
import com.api.backend.team.data.dto.TeamKickOutResponse;
import com.api.backend.team.data.dto.TeamParticipantsUpdateResponse;
import com.api.backend.team.data.dto.TeamUpdateRequest;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import com.api.backend.team.data.type.TeamRole;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;
  @Mock
  private TeamParticipantsRepository teamParticipantsRepository;
  @Mock
  private FileProcessService fileProcessService;
  @Mock
  private MemberRepository memberRepository;


  @InjectMocks
  private TeamService teamService;

  @Test
  @DisplayName("팀 생성 로직 - 성공")
  void createTeam() {
    //given
    Long userId = 1L;
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "testImg", "test".getBytes()
    );

    TeamCreateRequest teamCreateRequest = new TeamCreateRequest(
        "testTeam",
        mockMultipartFile,
        3
    );
    String testUrl = "smdfiksf";

    Member member = Member.builder().memberId(1L).build();

    Team team = Team.builder()
        .teamId(1L)
        .memberLimit(teamCreateRequest.getMemberLimit())
        .name(teamCreateRequest.getTeamName())
        .profileUrl(testUrl).build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .member(member)
        .team(team)
        .teamRole(TeamRole.READER)
        .teamNickName("testNickName")
        .build();

    when(memberRepository.findById(userId))
        .thenReturn(Optional.of(member));
    when(fileProcessService.uploadImage(mockMultipartFile, FileFolder.TEAM))
        .thenReturn(testUrl);
    when(teamRepository.save(any()))
        .thenReturn(team);
    when(teamParticipantsRepository.save(any()))
        .thenReturn(teamParticipants);

    //when
    TeamCreateResponse result = teamService.createTeam(teamCreateRequest, userId);

    //then
    assertEquals(result.getTeamName(), team.getName());
    assertEquals(result.getInviteCode(), team.getInviteLink());
    assertEquals(result.getTeamUrl(), team.getProfileUrl());

    verify(memberRepository, timeout(1)).findById(userId);
    verify(fileProcessService, timeout(1)).uploadImage(mockMultipartFile, FileFolder.TEAM);
    verify(teamRepository, timeout(1)).save(any());
    verify(teamParticipantsRepository, timeout(1)).save(any());

  }

  @Test
  @DisplayName("팀 초대 코드 url 로직 - [성공]")
  void getTeamUrl_success() {
    //given
    Long teamId = 1L;
    Long userId = 1L;
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(teamId))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(teamId, userId))
        .thenReturn(true);
    //when
    String result = teamService.getTeamUrl(teamId, userId);

    //then
    assertEquals(result, team.getInviteLink());
  }

  @Test
  @DisplayName("팀 초대 코드 url 로직 - [실패] 팀원이 아닐경우")
  void getTeamUrl_fail() {
    //given
    Long teamId = 1L;
    Long userId = 1L;
    Team team = Team.builder()
        .teamId(1L)
        .profileUrl("2/dsfefsefnklsd")
        .build();
    when(teamRepository.findById(teamId))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(teamId, userId))
        .thenReturn(false);
    //when
    CustomException result = assertThrows(CustomException.class,
        () -> teamService.getTeamUrl(teamId, userId));

    //then
    assertEquals(result.getErrorMessage(), TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), 500);
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 성공")
  void updateTeamParticipants_success() {
    //given
    Long id = 1L;
    Long userId = 1L;
    String code = "dsfefsefnklsd";
    Team team = Team.builder()
        .teamId(1L)
        .name("test")
        .inviteLink("2/dsfefsefnklsd")
        .memberLimit(3)
        .teamParticipants(new ArrayList<>())
        .build();
    Member member = Member.builder().build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamParticipantsId(1L)
        .member(member)
        .team(team)
        .teamRole(TeamRole.MATE)
        .teamNickName("testNickName")
        .build();

    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(
        anyLong(), anyLong()
    )).thenReturn(false);
    when(memberRepository.findById(anyLong()))
        .thenReturn(Optional.of(Member.builder().build()));
    when(teamParticipantsRepository.save(any()))
        .thenReturn(teamParticipants);
    //when
    TeamParticipantsUpdateResponse result = teamService.updateTeamParticipants(id, code, userId);

    //then
    assertEquals(result.getTeamId(), team.getTeamId());
    assertEquals(result.getUpdateTeamParticipantId(), teamParticipants.getTeamParticipantsId());
    assertEquals(result.getUpdateTeamParticipantNickName(), teamParticipants.getTeamNickName());
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 실패[code]")
  void updateTeamParticipants_fail_code() {
    //given
    Long id = 1L;
    Long userId = 1L;
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
    assertEquals(result.getErrorMessage(), TEAM_CODE_NOT_VALID_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_CODE_NOT_VALID_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 실패[existUser]")
  void updateTeamParticipants_fail_exist_user() {
    //given
    Long id = 1L;
    Long userId = 1L;
    String code = "dsfefsefnklsd";
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .memberLimit(3)
        .teamParticipants(new ArrayList<>())
        .build();
    when(teamRepository.findById(anyLong()))
        .thenReturn(Optional.of(team));
    when(teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(anyLong(), anyLong()))
        .thenReturn(true);

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.updateTeamParticipants(id, code, userId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_PARTICIPANTS_EXIST_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANTS_EXIST_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀에 팀원 추가 로직 - 실패[인원 제한]")
  void updateTeamParticipants_fail_member_limit() {
    //given
    Long teamId = 1L;
    Long userId = 1L;
    String code = "dsfefsefnklsd";
    Team team = Team.builder()
        .teamId(1L)
        .inviteLink("2/dsfefsefnklsd")
        .memberLimit(1)
        .teamParticipants(Lists.list(new TeamParticipants()))
        .build();
    when(teamRepository.findById(teamId))
        .thenReturn(Optional.of(team));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.updateTeamParticipants(teamId, code, userId)
    );

    //then
    assertEquals(result.getErrorMessage(), TEAM_LIMIT_VALID_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_LIMIT_VALID_EXCEPTION.getCode());
  }


  @Test
  @DisplayName("팀원 강퇴 로직 - 성공")
  void KickOutTeamParticipants_success() {
    //given
    Long userId = 1L;
    TeamKickOutRequest request =
        new TeamKickOutRequest(1L, 2L, "testMessage");
    TeamParticipants teamParticipants = TeamParticipants
        .builder()
        .teamParticipantsId(1L)
        .teamRole(TeamRole.READER)
        .member(
            Member.builder().memberId(1L).build()
        ).build();
    TeamParticipants kickOutTeamParticipants = TeamParticipants
        .builder()
        .teamParticipantsId(2L)
        .teamRole(TeamRole.READER)
        .teamNickName("강퇴 사용자 닉네임")
        .member(
            Member.builder().memberId(1L).name("testUser").build()
        ).build();

    when(teamRepository.existsByTeamIdAndIsDelete(request.getTeamId(), false))
        .thenReturn(true);
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        request.getTeamId(),
        userId)
    ).thenReturn(Optional.of(teamParticipants));
    when(teamParticipantsRepository.findById(request.getParticipantId()))
        .thenReturn(Optional.of(kickOutTeamParticipants));

    doNothing().when(teamParticipantsRepository).delete(kickOutTeamParticipants);
    //when
    TeamKickOutResponse result = teamService.kickOutTeamParticipants(request, userId);

    //then
    assertEquals(result.getKickOutMemberId(), 1L);
    assertEquals(result.getTeamId(), 1L);
    assertEquals(result.getTeamName(), kickOutTeamParticipants.getTeamNickName());
    assertEquals(result.getMessage(), KICK_OUT_TEAM_PARTICIPANTS);
    assertEquals(result.getAlarmMessage(),request.getKickOutReason());

    verify(teamRepository, timeout(1)).existsByTeamIdAndIsDelete(request.getTeamId(), false);
    verify(teamParticipantsRepository, timeout(1))
        .findByTeam_TeamIdAndMember_MemberId(request.getTeamId(), userId);
    verify(teamParticipantsRepository, timeout(1)).findById(request.getParticipantId());
    verify(teamParticipantsRepository, timeout(1)).delete(any());
  }

  @Test
  @DisplayName("팀원 강퇴 로직 - 실패[이미 해체된 팀]")
  void KickOutTeamParticipants_fail() {
    //given
    TeamKickOutRequest request =
        new TeamKickOutRequest(1L, 1L, "testMessage");
    Long userId = 1L;

    when(teamRepository.existsByTeamIdAndIsDelete(anyLong(), anyBoolean()))
        .thenReturn(true);
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.kickOutTeamParticipants(request, userId)
    );

    //then
    assertEquals(result.getErrorCode().getCode(),
        TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getCode());
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getErrorMessage());
  }


  @Test
  @DisplayName("팀 해체 로직 - 성공")
  void disbandTeam_success() {
    //given
    Long userId = 1L;
    TeamDisbandRequest teamDisbandRequest = new TeamDisbandRequest(1L, "test");
    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(false)
        .name("test")
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamDisbandRequest.getTeamId(), userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    Team result = teamService.disbandTeam(userId, teamDisbandRequest);

    //then
    assertEquals(result.getRestorationDt(), LocalDate.now().plusDays(30));
  }

  @Test
  @DisplayName("팀 해체 로직 - 실패[팀원이 존재하지 않음]")
  void disbandTeam_fail_participants() {
    //given
    Long userId = 1L;
    TeamDisbandRequest teamDisbandRequest = new TeamDisbandRequest(1L, "test");

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamDisbandRequest.getTeamId(), userId
    )).thenReturn(Optional.empty());

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.disbandTeam(userId, teamDisbandRequest)
    );

    //then
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀 해체 로직 - 실패[권한 부족]")
  void disbandTeam_fail_mate() {
    //given
    Long userId = 1L;
    TeamDisbandRequest teamDisbandRequest = new TeamDisbandRequest(1L, "test");
    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(false)
        .name("test")
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.MATE)
        .member(member)
        .team(team)
        .build();
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamDisbandRequest.getTeamId(), userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.disbandTeam(userId, teamDisbandRequest)
    );

    //then
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀 해체 로직 - 실패[팀 이름 불일치]")
  void disbandTeam_fail_password() {
    //given
    Long userId = 1L;
    TeamDisbandRequest teamDisbandRequest = new TeamDisbandRequest(1L, "22test");
    Member member = Member.builder().memberId(1L).password("asdasdswd").build();
    Team team = Team.builder()
        .isDelete(false)
        .name("test")
        .build();
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamDisbandRequest.getTeamId(), userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.disbandTeam(userId, teamDisbandRequest)
    );

    //then
    assertEquals(result.getErrorCode().getErrorMessage(),
        PASSWORD_NOT_MATCH_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), PASSWORD_NOT_MATCH_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀 해체 로직 - 실패[복구 시간 not null]")
  void disbandTeam_fail_restoreDt() {
    //given
    Long userId = 1L;
    TeamDisbandRequest teamDisbandRequest = new TeamDisbandRequest(1L, "test");
    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(false)
        .name("test")
        .restorationDt(LocalDate.now())
        .build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamDisbandRequest.getTeamId(), userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.disbandTeam(userId, teamDisbandRequest)
    );

    //then
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_IS_DELETEING_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_IS_DELETEING_EXCEPTION.getCode());
  }

  @Test
  @DisplayName("팀 해체 로직 - 실패[팀이 이미 해체됨]")
  void disbandTeam_fail_delete() {
    //given
    Long userId = 1L;
    TeamDisbandRequest teamDisbandRequest = new TeamDisbandRequest(1L, "test");
    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(true)
        .name("test")
        .build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamDisbandRequest.getTeamId(), userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.disbandTeam(userId, teamDisbandRequest)
    );

    //then
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_IS_DELETE_TRUE_EXCEPTION.getErrorMessage());
    assertEquals(result.getErrorCode().getCode(), TEAM_IS_DELETE_TRUE_EXCEPTION.getCode());
  }


  @Test
  @DisplayName("팀 복구 로직 - 성공[복구 성공]")
  void restoreTeam_success_restore_success() {
    //given
    Long userId = 1L;
    LocalDate restoreDt = LocalDate.now().minusDays(19L);
    Long teamId = 1L;

    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(false)
        .restorationDt(LocalDate.now())
        .build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();
    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId, userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    Team result = teamService.restoreTeam(userId, restoreDt, teamId);

    //then
    assertEquals(result.getTeamId(), team.getTeamId());
    assertFalse(result.isDelete());
  }

  @Test
  @DisplayName("팀 복구 로직 - 실패[존재하지 않는 팀원]")
  void restoreTeam_fail_mate() {
    //given
    Long userId = 1L;
    LocalDate restoreDt = LocalDate.now().minusDays(19L);
    Long teamId = 1L;

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId, userId
    )).thenReturn(Optional.empty());

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.restoreTeam(userId, restoreDt, teamId)
    );

    //then
    assertEquals(result.getErrorCode().getCode(), TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getCode());
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION.getErrorMessage());
  }

  @Test
  @DisplayName("팀 복구 로직 - 실패[이미 해체된 팀]")
  void restoreTeam_fail_isDelete() {
    //given
    Long userId = 1L;
    LocalDate restoreDt = LocalDate.now().minusDays(19L);
    Long teamId = 1L;
    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(true)
        .restorationDt(LocalDate.now())
        .build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId, userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.restoreTeam(userId, restoreDt, teamId)
    );

    //then
    assertEquals(result.getErrorCode().getCode(), TEAM_IS_DELETE_TRUE_EXCEPTION.getCode());
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_IS_DELETE_TRUE_EXCEPTION.getErrorMessage());
  }

  @Test
  @DisplayName("팀 복구 로직 - 실패[restoreDt null]")
  void restoreTeam_fail_restoreDt() {
    //given
    Long userId = 1L;
    LocalDate restoreDt = LocalDate.now().minusDays(19L);
    Long teamId = 1L;
    Member member = Member.builder().memberId(1L).password("test").build();
    Team team = Team.builder()
        .isDelete(false)
        .build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .teamRole(TeamRole.READER)
        .member(member)
        .team(team)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId, userId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException result = assertThrows(
        CustomException.class,
        () -> teamService.restoreTeam(userId, restoreDt, teamId)
    );

    //then
    assertEquals(result.getErrorCode().getCode(), TEAM_NOT_DELETEING_EXCEPTION.getCode());
    assertEquals(result.getErrorCode().getErrorMessage(),
        TEAM_NOT_DELETEING_EXCEPTION.getErrorMessage());
  }

  @Test
  @DisplayName("자신이 속한 팀 조회시 팀 해체여부가 false 인 것만 받아온다.")
  void getTimes() {
    //given
    Long memberId = 1L;
    List<TeamParticipants> teamParticipants = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      teamParticipants.add(
          TeamParticipants.builder()
              .team(
                  Team.builder()
                      .teamId((long) i)
                      .build()
              )
              .teamNickName("test" + i)
              .build()
      );
    }
    when(teamParticipantsRepository.findAllByMember_MemberIdAndTeam_IsDelete(
        memberId, false
    )).thenReturn(teamParticipants);

    //when
    List<TeamParticipants> result = teamService.getTeams(memberId);

    //then
    for (int i = 0; i < 3; i++) {
      assertEquals(result.get(i).getTeam().getTeamId(), i);
      assertEquals(result.get(i).getTeamNickName(),"test" + i);
    }
  }

  @Test
  @DisplayName("팀 내용수정 service로직")
  void updateTeam() {
    //given
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "testImg", "test".getBytes()
    );
    TeamUpdateRequest teamUpdateRequest =
        new TeamUpdateRequest(1L, "test", mockMultipartFile);
    Long userId = 1L;
    Team team = Team.builder()
        .name("test1")
        .profileUrl("test").build();
    String testUrl = "teamUrl";
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .team(team)
        .teamRole(TeamRole.READER)
        .build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(anyLong(), anyLong()))
        .thenReturn(Optional.of(teamParticipants));
    when(fileProcessService.uploadImage(mockMultipartFile, FileFolder.TEAM))
        .thenReturn(testUrl);
    //when
    Team result = teamService.updateTeam(teamUpdateRequest, userId);

    //then
    assertEquals(result.getName(), teamUpdateRequest.getTeamName());
    assertEquals(result.getProfileUrl(), testUrl);
  }

  @Test
  @DisplayName("자신이 속한 및 teamId에 따른 팀 반환")
  void getTeamByTeamIdAndMemberId(){
    //given
    Long teamId = 1L;
    Long memberId = 1L;
    TeamParticipants teamParticipants = TeamParticipants.builder()
        .team(
            Team.builder()
                .name("testTeam")
                .isDelete(false).build()
        ).build();

    when(teamParticipantsRepository.findByTeam_TeamIdAndMember_MemberId(
        teamId, memberId
    )).thenReturn(Optional.of(teamParticipants));

    //when
    Team team = teamService.getTeamByTeamIdAndMemberId(teamId, memberId);

    //then
    assertEquals(team.getName(),"testTeam");
  }
}
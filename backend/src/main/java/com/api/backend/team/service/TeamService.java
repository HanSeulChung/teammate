package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.MEMBER_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.PASSWORD_NOT_MATCH_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_CODE_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_EXPIRED_DATE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_IS_DELETEING_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_IS_DELETE_TRUE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_LIMIT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_DELETEING_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_EQUALS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_EXIST_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION;
import static com.api.backend.team.data.ResponseMessage.KICK_OUT_TEAM_PARTICIPANTS;
import static com.api.backend.team.data.ResponseMessage.UPDATE_TEAM_PARTICIPANTS;

import com.api.backend.file.service.FileProcessService;
import com.api.backend.file.type.FileFolder;
import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.team.crypt.SimpleEncryption;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private final SimpleEncryption simpleEncryption;
  private static final boolean DELETE_FALSE_FLAG = false;
  private static final int EXPIRE_DATE = 10;

  private final FileProcessService fileProcessService;

  @Transactional
  public TeamCreateResponse createTeam(TeamCreateRequest teamRequest, Long userId) {
    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_EXCEPTION));

    String imgUrl = fileProcessService.uploadImage(teamRequest.getTeamImg(), FileFolder.TEAM);

    Team team = teamRepository.save(
        Team.builder()
            .memberLimit(teamRequest.getMemberLimit())
            .name(teamRequest.getTeamName())
            .profileUrl(imgUrl)
            .inviteCode(UUID.randomUUID().toString())
            .build()
    );

    teamParticipantsRepository.save(
        TeamParticipants.builder()
            .team(team)
            .member(member)
            .teamNickName(getRandomNickName(member.getName()))
            .teamRole(TeamRole.LEADER)
            .build()
    );

    return TeamCreateResponse.from(team,userId);
  }

  public String getTeamUrl(Long teamId,Long userId) {
    Team team = getTeam(teamId);

    isDeletedCheck(team.getRestorationDt(), team.isDelete());

    existTeamParticipantsFalseThrows(teamId, userId);

    String encryptDate = simpleEncryption.encrypt(
        LocalDate.now().plusDays(EXPIRE_DATE).toString()
    );

    return team.getTeamId() + "/" +team.getInviteCode() + "/" + encryptDate;
  }

  @Transactional
  public TeamParticipantsUpdateResponse updateTeamParticipants(Long teamId, String code, Long userId,
      String dateString) {
    Team team = getTeam(teamId);
    String codeByEntity = team.getInviteCode();

    if (!codeByEntity.equals(code)) {
      throw new CustomException(TEAM_CODE_NOT_VALID_EXCEPTION);
    }

    LocalDate date = simpleEncryption.decrypt(dateString);

    if (LocalDate.now().isAfter(date)) {
      throw new CustomException(TEAM_EXPIRED_DATE_EXCEPTION);
    }

    isDeletedCheck(team.getRestorationDt(), team.isDelete());

    if (team.getTeamParticipants().size() + 1 > team.getMemberLimit()) {
      throw new CustomException(TEAM_LIMIT_VALID_EXCEPTION);
    }

    existTeamParticipantsTrueThrows(teamId, userId);

    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_EXCEPTION));

    TeamParticipants teamParticipants = teamParticipantsRepository.save(
        TeamParticipants.builder()
            .member(member)
            .teamNickName(getRandomNickName(member.getName()))
            .team(team)
            .teamRole(TeamRole.MATE)
            .build()
    );

    return TeamParticipantsUpdateResponse
        .builder()
        .teamId(teamId)
        .updateTeamParticipantId(teamParticipants.getTeamParticipantsId())
        .updateTeamParticipantNickName(teamParticipants.getTeamNickName())
        .message(team.getName() + UPDATE_TEAM_PARTICIPANTS)
        .build();
  }

  @Transactional
  public TeamKickOutResponse kickOutTeamParticipants(TeamKickOutRequest request, Long userId) {
    if (!teamRepository.existsByTeamIdAndIsDelete(request.getTeamId(), DELETE_FALSE_FLAG)) {
      throw new CustomException(TEAM_IS_DELETE_TRUE_EXCEPTION);
    }

    TeamParticipants leaderParticipants = getTeamParticipantByTeamIdAndMemberId
            (
                request.getTeamId(), userId
            );

    if (!leaderParticipants.getTeamRole().equals(TeamRole.LEADER)) {
      throw new CustomException(TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION);
    }

    TeamParticipants teamParticipants = getTeamParticipantById
        (
        request.getParticipantId()
    );

    if (teamParticipants.getTeamParticipantsId()
        .equals(leaderParticipants.getTeamParticipantsId())) {
      throw new CustomException(TEAM_PARTICIPANTS_EQUALS_EXCEPTION);
    }

    deleteTeamParticipantById(teamParticipants);

    return TeamKickOutResponse.builder()
        .teamId(request.getTeamId())
        .kickOutMemberId(teamParticipants.getMember().getMemberId())
        .teamName(teamParticipants.getTeamNickName())
        .alarmMessage(request.getKickOutReason())
        .message(KICK_OUT_TEAM_PARTICIPANTS)
        .build();
  }

  @Transactional
  public Team disbandTeam(Long userId, TeamDisbandRequest request) {

    TeamParticipants teamParticipants = getTeamParticipantByTeamIdAndMemberId
        (
            request.getTeamId(), userId
        );

    disbandCheckPermission(request.getTeamName(), teamParticipants);

    Team team = teamParticipants.getTeam();

    isDeletedCheck(team.getRestorationDt(), team.isDelete());

    team.setRestorationDt(LocalDate.now().plusDays(30));
    return team;
  }

  @Transactional
  public Team restoreTeam(Long userId, LocalDate restoreDt, Long teamId) {
    TeamParticipants teamParticipants = getTeamParticipantByTeamIdAndMemberId
            (
                teamId,
                userId
            );

    if (!teamParticipants.getTeamRole().equals(TeamRole.LEADER)) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION);
    }

    Team team = teamParticipants.getTeam();

    if (team.isDelete()) {
      throw new CustomException(TEAM_IS_DELETE_TRUE_EXCEPTION);
    }

    if (team.getRestorationDt() == null) {
      throw new CustomException(TEAM_NOT_DELETEING_EXCEPTION);
    } else if (!team.getRestorationDt().isAfter(restoreDt)) {
      team.setDelete(true);
      return team;
    }

    team.setRestorationDt(null);
    return team;
  }

  public boolean existById(Long teamId) {
    return teamRepository.existsById(teamId);
  }

  public List<TeamParticipants> getTeams(Long memberId) {
    return getTeamParticipantsById(memberId);
  }

  @Transactional
  public Team updateTeam(TeamUpdateRequest teamUpdateRequest, Long userId) {
    TeamParticipants teamParticipants = getTeamParticipantByTeamIdAndMemberId
        (
            teamUpdateRequest.getTeamId(), userId
        );

    if (!teamParticipants.getTeamRole().equals(TeamRole.LEADER)) {
      throw new CustomException(TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION);
    }

    Team team = teamParticipants.getTeam();

    isDeletedCheck(team.getRestorationDt(), team.isDelete());

    String imgUrl = fileProcessService.uploadImage(teamUpdateRequest.getProfileImg(), FileFolder.TEAM);
    team.updateNameAndProfileUrl(teamUpdateRequest.getTeamName(), imgUrl);
    return team;
  }

  public void isDeletedCheck(LocalDate restoreDt, boolean isDelete) {
    if (!Objects.isNull(restoreDt)) {
      throw new CustomException(TEAM_IS_DELETEING_EXCEPTION);
    }

    if (isDelete) {
      throw new CustomException(TEAM_IS_DELETE_TRUE_EXCEPTION);
    }
  }

  public void disbandCheckPermission(String teamName, TeamParticipants teamParticipants) {
    if (!teamParticipants.getTeamRole().equals(TeamRole.LEADER)) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION);
    }

    if (!teamParticipants.getTeam().getName()
        .equals(teamName)) {
      throw new CustomException(PASSWORD_NOT_MATCH_EXCEPTION);
    }
  }

  @Transactional
  public void deleteTeamParticipantById(TeamParticipants teamParticipants) {
    teamParticipantsRepository.delete(teamParticipants);
  }

  public Team getTeamByTeamIdAndMemberId(Long teamId, Long memberId) {
    TeamParticipants teamParticipants = teamParticipantsRepository
        .findByTeam_TeamIdAndMember_MemberId(
            teamId,
            memberId
        ).orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    Team team = teamParticipants.getTeam();

    isDeletedCheck(team.getRestorationDt(), team.isDelete());
    return team;
  }

  private void existTeamParticipantsFalseThrows(Long teamId, Long userId) {
    if (!teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(teamId, userId)) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION);
    }
  }

  private void existTeamParticipantsTrueThrows(Long teamId, Long userId) {
    if (teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(teamId, userId)) {
      throw new CustomException(TEAM_PARTICIPANTS_EXIST_EXCEPTION);
    }
  }

  private TeamParticipants getTeamParticipantById(Long teamParticipantId) {
    return teamParticipantsRepository
        .findById(teamParticipantId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));
  }

  private List<TeamParticipants> getTeamParticipantsById(Long memberId) {
    return teamParticipantsRepository.findAllByMember_MemberIdAndTeam_IsDelete(
        memberId, DELETE_FALSE_FLAG
    );
  }
  private TeamParticipants getTeamParticipantByTeamIdAndMemberId(Long teamId, Long userId) {
    return teamParticipantsRepository
        .findByTeam_TeamIdAndMember_MemberId(teamId, userId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));
  }

  private String getRandomNickName(String name){
    return RandomStringUtils.randomAlphanumeric(4) + "_" + name;
  }

  private Team getTeam(Long id) {
    return teamRepository.findById(id)
        .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND_EXCEPTION));
  }
}

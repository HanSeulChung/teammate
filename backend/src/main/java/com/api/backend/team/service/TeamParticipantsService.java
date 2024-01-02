package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.MEMBER_NOT_EQUALS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_EQUALS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_NOT_VALID_MATE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION;
import static com.api.backend.team.data.ResponseMessage.UPDATE_ROLE_TEAM_PARTICIPANT;

import com.api.backend.file.service.FileProcessService;
import com.api.backend.file.type.FileFolder;
import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.dto.TeamParticipantUpdateRequest;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.type.TeamRole;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamParticipantsService {

  private final TeamService teamService;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private static final boolean DELETE_FALSE_FLAG = false;

  private final FileProcessService fileProcessService;

  @Transactional
  public TeamParticipants deleteTeamParticipantById(Long memberId, Long teamId) {
    TeamParticipants teamParticipants = getTeamParticipantByTeamIdAndMemberId(teamId, memberId);

    if (teamParticipants.getTeamRole().equals(TeamRole.LEADER)) {
      throw new CustomException(TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION);
    }
    fileProcessService.deleteImage(teamParticipants.getParticipantsProfileUrl());
    teamParticipantsRepository.delete(teamParticipants);

    return teamParticipants;
  }

  @Transactional
  public String updateRoleTeamParticipant(Long memberId, Long participantId, Long teamId) {
    TeamParticipants readerParticipant = getTeamParticipantByTeamIdAndMemberId(teamId, memberId);

    if (!readerParticipant.getTeamRole().equals(TeamRole.LEADER)) {
      throw new CustomException(TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION);
    }

    TeamParticipants mateParticipant = teamParticipantsRepository.findById(participantId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    if (!readerParticipant.getTeam().getTeamId()
        .equals(mateParticipant.getTeam().getTeamId())) {
      throw new CustomException(TEAM_NOT_EQUALS_EXCEPTION);
    }
    if (!mateParticipant.getTeamRole().equals(TeamRole.MATE)) {
      throw new CustomException(TEAM_PARTICIPANT_NOT_VALID_MATE_EXCEPTION);
    }

    readerParticipant.setTeamRole(TeamRole.MATE);
    mateParticipant.setTeamRole(TeamRole.LEADER);
    return UPDATE_ROLE_TEAM_PARTICIPANT;
  }

  public List<TeamParticipants> getTeamParticipants(Long teamId, Long memberId) {
    TeamParticipants teamParticipants = getTeamParticipantByTeamIdAndMemberId(teamId, memberId);

    Team team = teamParticipants.getTeam();

    teamService.isDeletedCheck(team.getRestorationDt(), team.isDelete());

    return team.getTeamParticipants();
  }

  public TeamParticipants getTeamParticipant(Long teamId, Long memberId) {
    TeamParticipants teamParticipants = getTeamParticipantByTeamIdAndMemberId(teamId, memberId);

    Team team = teamParticipants.getTeam();

    teamService.isDeletedCheck(team.getRestorationDt(), team.isDelete());

    return teamParticipants;
  }

  public List<TeamParticipants> getTeamParticipantsByUserId(Long memberId) {
    return teamParticipantsRepository
        .findAllByMember_MemberIdAndTeam_IsDeleteAndTeam_RestorationDtIsNull(
            memberId, DELETE_FALSE_FLAG
        );
  }

  @Transactional
  public TeamParticipants updateParticipantContent(
      TeamParticipantUpdateRequest teamParticipantUpdateRequest, Long memberId
  ) {
    TeamParticipants teamParticipant = teamParticipantsRepository.findById(
        teamParticipantUpdateRequest.getTeamParticipantsId()
    ).orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    Team team = teamParticipant.getTeam();

    teamService.isDeletedCheck(team.getRestorationDt(), team.isDelete());

    if (!teamParticipant.getMember().getMemberId()
        .equals(memberId)) {
      throw new CustomException(MEMBER_NOT_EQUALS_EXCEPTION);
    }

    if (!Objects.equals(
        teamParticipantUpdateRequest.getTeamNickName(),
        teamParticipant.getTeamNickName())
    ) {
      teamParticipant.setTeamNickName(teamParticipantUpdateRequest.getTeamNickName());
    }

    if (teamParticipantUpdateRequest.getParticipantImg() != null) {
      teamParticipant.setParticipantsProfileUrl(
          fileProcessService.uploadImage(
              teamParticipantUpdateRequest.getParticipantImg(), FileFolder.PARTICIPANT)
      );
    }
    return teamParticipant;
  }

  public List<TeamParticipants> getTeamParticipantsExcludeId(Long teamParticipantId, Long teamId) {
    return teamParticipantsRepository.findByTeam_TeamIdAndTeamParticipantsIdNot(teamId,
        teamParticipantId);
  }

  public List<TeamParticipants> getTeamParticipantsByExcludeMemberId(Long teamId, Long memberId) {
    return teamParticipantsRepository.findAllByTeam_TeamIdAndMember_MemberIdNot(teamId, memberId);
  }

  public List<TeamParticipants> getTeamParticipantsByIds(List<Long> teamParticipantIds) {
    return teamParticipantsRepository.findAllById(teamParticipantIds);
  }

  private TeamParticipants getTeamParticipantByTeamIdAndMemberId(Long teamId, Long memberId) {
    return teamParticipantsRepository
        .findByTeam_TeamIdAndMember_MemberId(teamId, memberId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));
  }
}

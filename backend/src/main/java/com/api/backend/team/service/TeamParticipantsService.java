package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION;
import static com.api.backend.team.data.ResponseMessage.DELETE_TEAM_PARTICIPANT;

import com.api.backend.global.exception.CustomException;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.type.TeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamParticipantsService {

  private final TeamParticipantsRepository teamParticipantsRepository;

  public String deleteTeamParticipant(String userId, Long teamId) {
    TeamParticipants teamParticipants = teamParticipantsRepository
        .findByTeam_TeamIdAndMember_MemberId(teamId, Long.valueOf(userId))
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    if (teamParticipants.getTeamRole().equals(TeamRole.READER)) {
      throw new CustomException(TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION);
    }
    teamParticipantsRepository.delete(teamParticipants);

    return DELETE_TEAM_PARTICIPANT;
  }

}

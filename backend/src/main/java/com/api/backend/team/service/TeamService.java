package com.api.backend.team.service;

import static com.api.backend.global.exception.type.ErrorCode.MEMBER_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_CODE_NOT_VALID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_EXIST_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;

import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.team.data.dto.TeamCreateRequest;
import com.api.backend.team.data.dto.TeamCreateResponse;
import com.api.backend.team.data.dto.TeamKickOutRequest;
import com.api.backend.team.data.dto.TeamKickOutResponse;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import com.api.backend.team.data.type.TeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  @Transactional
  public TeamCreateResponse createTeam(TeamCreateRequest teamRequest, String userId) {
    Long changeTypeUserId = Long.valueOf(userId);
    Member member = memberRepository.findById(changeTypeUserId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_EXCEPTION));

    Team team = teamRepository.save(
        Team.builder()
            .memberLimit(teamRequest.getMemberLimit())
            .name(teamRequest.getTeamName())
            // todo imge는 나중에 어떻게 처리를 해야할지 결정하면 다시 구현하겠다.
            .profileUrl(teamRequest.getTeamImg())
            .build()
    );
    team.setInviteLink();

    teamParticipantsRepository.save(
        TeamParticipants.builder()
            .team(team)
            .member(member)
            .teamRole(TeamRole.READER)
            .build()
    );

    return TeamCreateResponse.from(team,changeTypeUserId);
  }


  private Team getTeam(Long id) {
    return teamRepository.findById(id)
        .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND_EXCEPTION));
  }

  public String getTeamUrl(Long teamId,String userId) {
    Team team = getTeam(teamId);

    if (!teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(teamId, Long.valueOf(userId))) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION);
    }
    return team.getInviteLink();
  }

  @Transactional
  public Team updateTeamParticipants(Long teamId, String code, String userId) {
    Team team = getTeam(teamId);
    Long changedTypeUserId = Long.valueOf(userId);
    String entityCode = team.getInviteLink().split("/")[1];

    if (!entityCode.equals(code)) {
      throw new CustomException(TEAM_CODE_NOT_VALID_EXCEPTION);
    }

    if (teamParticipantsRepository.existsByTeam_TeamIdAndMember_MemberId(teamId,changedTypeUserId)) {
      throw new CustomException(TEAM_PARTICIPANTS_EXIST_EXCEPTION);
    }

    Member member = Member.builder().memberId(changedTypeUserId).build();
    teamParticipantsRepository.save(
        TeamParticipants.builder()
            .member(member)
            .team(team)
            .teamRole(TeamRole.MATE)
            .build()
    );

    return team;
  }

  @Transactional
  public TeamKickOutResponse kickOutTeamParticipants(TeamKickOutRequest request, String userId) {
    Team team = getTeam(request.getTeamId());

    TeamParticipants teamParticipants = team.getTeamParticipants().stream()
        .filter(i -> i.getMember().getMemberId().equals(request.getUserId()))
        .findFirst()
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    teamParticipantsRepository.delete(teamParticipants);

    return TeamKickOutResponse.builder()
        .userId(request.getUserId())
        .teamId(request.getTeamId())
        .message("해당 사용자가 팀에서 강퇴됐습니다.")
        .build();
  }

}

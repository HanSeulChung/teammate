package com.api.backend.team.data.dto;

import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.type.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TeamParticipantsDto {
  private Long teamParticipantsId;
  private Long teamId;
  private TeamRole teamRole;
  private String teamName;
  private String participantsProfileUrl;
  private String teamNickName;

  public static TeamParticipantsDto from(TeamParticipants teamParticipants) {
    return TeamParticipantsDto.builder()
        .teamId(teamParticipants.getTeam().getTeamId())
        .teamParticipantsId(teamParticipants.getTeamParticipantsId())
        .teamNickName(teamParticipants.getTeamNickName())
        .teamName(teamParticipants.getTeam().getName())
        .teamRole(teamParticipants.getTeamRole())
        .participantsProfileUrl(teamParticipants.getParticipantsProfileUrl())
        .build();
  }
}

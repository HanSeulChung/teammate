package com.api.backend.team.data.dto;


import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.type.TeamRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@ToString
public class TeamsDtoResponse {
  private Long teamId;
  private String name;
  private String profileUrl;
  private LocalDate restorationDt;
  private TeamRole teamRole;

  public static TeamsDtoResponse from(Team team) {
    return TeamsDtoResponse.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .profileUrl(team.getProfileUrl())
        .build();
  }

  public static TeamsDtoResponse fromByTeamParticipant(TeamParticipants teamParticipants) {
    Team team = teamParticipants.getTeam();
    return TeamsDtoResponse.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .restorationDt(team.getRestorationDt())
        .teamRole(teamParticipants.getTeamRole())
        .profileUrl(team.getProfileUrl())
        .build();
  }
}

package com.api.backend.team.data.dto;


import com.api.backend.team.data.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@ToString
public class TeamsDtoResponse {
  private Long teamId;
  private String name;
  private String profileUrl;

  public static TeamsDtoResponse from(Team team) {
    return TeamsDtoResponse.builder()
        .teamId(team.getTeamId())
        .name(team.getName())
        .profileUrl(team.getProfileUrl())
        .build();
  }

  public static Page<TeamsDtoResponse> fromDtos(Page<Team> teams){
    return teams.map(TeamsDtoResponse::from);
  }
}

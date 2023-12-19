package com.api.backend.team.data.dto;

import com.api.backend.team.data.entity.Team;
import java.time.LocalDateTime;
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
public class TeamDtoResponse {

  private Long teamId;
  private String teamName;
  private int memberLimit;
  private LocalDateTime createDt;
  private String profileUrl;

  public static TeamDtoResponse from(Team team) {
    return TeamDtoResponse.builder()
        .teamId(team.getTeamId())
        .teamName(team.getName())
        .createDt(team.getCreateDt())
        .memberLimit(team.getMemberLimit())
        .profileUrl(team.getProfileUrl())
        .build();
  }

}

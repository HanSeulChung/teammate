package com.api.backend.team.data.dto;

import com.api.backend.team.data.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamCreateResponse {

  private Long teamId;
  private String teamName;
  private Long leaderId;
  private String inviteCode;
  // todo learderId는 추후 Security가 구현 된다면 넣도록하겠습니다.
  public static TeamCreateResponse from(Team team,Long userId) {
    return TeamCreateResponse.builder()
        .teamId(team.getTeamId())
        .leaderId(userId)
        .inviteCode(team.getInviteLink())
        .teamName(team.getName())
        .build();
  }
}

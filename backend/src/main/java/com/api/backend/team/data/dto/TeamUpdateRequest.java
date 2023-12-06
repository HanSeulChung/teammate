package com.api.backend.team.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateRequest {
  private Long teamId;
  private String teamName;
  private String profileUrl;
}

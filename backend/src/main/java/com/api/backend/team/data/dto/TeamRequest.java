package com.api.backend.team.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class TeamRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class Create{
    private String teamName;
    private String teamImg;
    private int memberLimit;
  }
}

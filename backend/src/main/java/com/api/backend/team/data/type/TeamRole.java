package com.api.backend.team.data.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamRole {
  LEADER("리더"),
  MATE("메이트");

  private final String description;
}

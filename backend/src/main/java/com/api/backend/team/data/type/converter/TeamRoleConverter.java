package com.api.backend.team.data.type.converter;

import com.api.backend.global.type.converter.AbstractLegacyEnumAttributeConverter;
import com.api.backend.member.data.type.Authority;
import com.api.backend.team.data.type.TeamRole;

public class TeamRoleConverter extends AbstractLegacyEnumAttributeConverter<TeamRole> {

  private static final String ENUM_NAME = "권한 종류";

  public TeamRoleConverter() {
    super(TeamRole.class, ENUM_NAME);
  }
}

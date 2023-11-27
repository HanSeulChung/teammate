package com.api.backend.team.data.type;

import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;

public enum TeamRole implements LegacyCodeCommonType {
  READER("0","READER"),
  MATE("1","MATE");

  private final String code;
  private final String description;

  TeamRole(String code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public String getLegacyCode() {
    return this.code;
  }

  @Override
  public String getDesc() {
    return this.description;
  }
}

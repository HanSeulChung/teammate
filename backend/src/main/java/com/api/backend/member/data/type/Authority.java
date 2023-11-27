package com.api.backend.member.data.type;

import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;

public enum Authority implements LegacyCodeCommonType {
  USER("0", "USER"),
  ADMIN("1", "ADMIN");

  private final String code;
  private final String description;

  Authority(String code, String description) {
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

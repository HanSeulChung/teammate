package com.api.backend.member.data.type;

import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;

public enum SexType implements LegacyCodeCommonType {
  MALE("0", "MALE"),
  FEMALE("1", "FEMALE");

  private final String code;
  private final String description;

  SexType(String code, String description) {
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

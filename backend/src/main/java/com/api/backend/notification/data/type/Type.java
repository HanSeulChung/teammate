package com.api.backend.notification.data.type;

import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;
import lombok.Getter;

@Getter
public enum Type implements LegacyCodeCommonType {
  DOCUMENTS("0","DOCUMENTS"),
  COMMENT("1","COMMENT"),
  MENTION("2", "MENTION"),
  INVITE("3","INVITE");

  private final String code;
  private final String description;

  Type(String code, String description) {
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

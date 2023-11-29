package com.api.backend.category.type;

import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;

public enum CategoryType implements LegacyCodeCommonType {
  DOCUMENTS("0", "DOCUMENTS"),
  SCHEDULE("1", "SCHEDULE");

  private final String code;
  private final String description;

  CategoryType(String code, String description) {
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

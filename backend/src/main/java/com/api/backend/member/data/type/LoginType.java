package com.api.backend.member.data.type;


import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;
import lombok.Getter;

@Getter
public enum LoginType implements LegacyCodeCommonType {
  KAKAO("0","KAKAO"),
  NAVER("1","NAVER"),
  GOOGLE("2", "GOOGLE"),
  TEAMMATE("3","TEAMMATE");

  private final String code;
  private final String description;

  LoginType(String code, String description) {
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

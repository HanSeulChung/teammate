package com.api.backend.member.data.type.converter;

import com.api.backend.member.data.type.LoginType;
import com.api.backend.global.type.converter.AbstractLegacyEnumAttributeConverter;

public class LoginTypeConverter  extends AbstractLegacyEnumAttributeConverter<LoginType> {

  private static final String ENUM_NAME = "SNS 종류";

  public LoginTypeConverter() {
    super(LoginType.class, ENUM_NAME);
  }
}

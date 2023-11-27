package com.api.backend.member.data.type.converter;

import com.api.backend.member.data.type.Authority;
import com.api.backend.global.type.converter.AbstractLegacyEnumAttributeConverter;

public class AuthorityConverter extends AbstractLegacyEnumAttributeConverter<Authority> {

  private static final String ENUM_NAME = "권한 종류";

  public AuthorityConverter() {
    super(Authority.class, ENUM_NAME);
  }
}

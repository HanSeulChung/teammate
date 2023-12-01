package com.api.backend.member.data.type.converter;

import com.api.backend.global.type.converter.AbstractLegacyEnumAttributeConverter;
import com.api.backend.member.data.type.SexType;

public class SexTypeConverter extends AbstractLegacyEnumAttributeConverter<SexType> {

  private static final String ENUM_NAME = "성별 종류";

  public SexTypeConverter() {
    super(SexType.class, ENUM_NAME);
  }
}

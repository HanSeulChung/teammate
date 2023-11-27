package com.api.backend.notification.data.type.converter;

import com.api.backend.global.type.converter.AbstractLegacyEnumAttributeConverter;
import com.api.backend.notification.data.type.Type;

public class TypeConverter extends AbstractLegacyEnumAttributeConverter<Type> {

  private static final String ENUM_NAME = "권한 종류";

  public TypeConverter() {
    super(Type.class, ENUM_NAME);
  }
}

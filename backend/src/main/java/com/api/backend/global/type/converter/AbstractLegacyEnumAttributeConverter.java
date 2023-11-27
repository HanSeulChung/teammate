package com.api.backend.global.type.converter;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.global.type.converter.legacy.LegacyCodeCommonType;
import com.api.backend.global.type.converter.legacy.LegacyCodeEnumValueConverterUtils;
import javax.persistence.AttributeConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@Getter
@Slf4j
public class AbstractLegacyEnumAttributeConverter<E extends Enum<E> & LegacyCodeCommonType> implements
    AttributeConverter<E, String> {

  public AbstractLegacyEnumAttributeConverter(Class<E> targetEnumClass, String enumName) {
    this.targetEnumClass = targetEnumClass;
    this.enumName = enumName;
  }

  private final Class<E> targetEnumClass;
  private final String enumName;

  @Override
  public String convertToDatabaseColumn(E attribute) {
    if (attribute == null) {
      throw new CustomException(ErrorCode.ENUM_NOT_FOUND_EXCEPTION);
    }
    return LegacyCodeEnumValueConverterUtils.toLegacyCode(attribute);
  }

  @Override
  public E convertToEntityAttribute(String dbData) {
    return LegacyCodeEnumValueConverterUtils.ofLegacyCode(targetEnumClass, dbData);
  }
}


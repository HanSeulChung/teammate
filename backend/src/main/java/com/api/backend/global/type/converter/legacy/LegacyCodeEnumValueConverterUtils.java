package com.api.backend.global.type.converter.legacy;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import java.util.EnumSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class LegacyCodeEnumValueConverterUtils {

  public static <T extends Enum<T> & LegacyCodeCommonType> T ofLegacyCode(Class<T> enumClass,
      String legacyCode){

    if (!StringUtils.hasText(legacyCode)) {
      return null;
    }

    return EnumSet.allOf(enumClass)
        .stream()
        .filter(e -> e.getLegacyCode().equals(legacyCode))
        .findAny()
        .orElseThrow(() -> new CustomException(ErrorCode.ENUM_NOT_VALID_EXCEPTION));
  }

  public static <T extends Enum<T> & LegacyCodeCommonType> String toLegacyCode(T enumValue) {
    return enumValue.getLegacyCode();
  }
}

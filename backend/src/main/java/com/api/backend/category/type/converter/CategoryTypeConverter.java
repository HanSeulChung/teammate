package com.api.backend.category.type.converter;

import com.api.backend.category.type.CategoryType;
import com.api.backend.global.type.converter.AbstractLegacyEnumAttributeConverter;

public class CategoryTypeConverter extends AbstractLegacyEnumAttributeConverter<CategoryType> {

  private static final String ENUM_NAME = "카테고리 종류";

  public CategoryTypeConverter(){
    super(CategoryType.class, ENUM_NAME);
  }
}

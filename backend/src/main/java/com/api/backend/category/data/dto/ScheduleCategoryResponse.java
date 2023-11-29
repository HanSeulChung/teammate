package com.api.backend.category.data.dto;

import com.api.backend.category.type.CategoryType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleCategoryResponse {
  private Long id;
  private String categoryName;
  private CategoryType categoryType;
  private LocalDateTime createDt;
  private String color;

  public static ScheduleCategoryResponse toResponse(ScheduleCategoryDto scheduleCategoryDto) {
    return ScheduleCategoryResponse.builder()
        .id(scheduleCategoryDto.getId())
        .categoryName(scheduleCategoryDto.getCategoryName())
        .categoryType(scheduleCategoryDto.getCategoryType())
        .createDt(scheduleCategoryDto.getCreateDt())
        .color(scheduleCategoryDto.getColor())
        .build();
  }
}

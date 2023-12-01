package com.api.backend.category.data.dto;

import com.api.backend.category.type.CategoryType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleCategoryResponse {

  private Long categoryId;
  private String categoryName;
  private CategoryType categoryType;
  private LocalDateTime createDt;
  private LocalDateTime updateDt;
  private String color;

  public static ScheduleCategoryResponse from(ScheduleCategoryDto scheduleCategoryDto) {
    return ScheduleCategoryResponse.builder()
        .categoryId(scheduleCategoryDto.getCategoryId())
        .categoryName(scheduleCategoryDto.getCategoryName())
        .categoryType(scheduleCategoryDto.getCategoryType())
        .createDt(scheduleCategoryDto.getCreateDt())
        .updateDt(scheduleCategoryDto.getUpdateDt())
        .color(scheduleCategoryDto.getColor())
        .build();
  }

  public static List<ScheduleCategoryResponse> from(
      List<ScheduleCategoryDto> scheduleCategoryDtoList) {
    if (scheduleCategoryDtoList != null) {
      List<ScheduleCategoryResponse> scheduleCategoryResponses = new ArrayList<>();
      for (ScheduleCategoryDto dto : scheduleCategoryDtoList) {
        scheduleCategoryResponses.add(from(dto));
      }
      return scheduleCategoryResponses;
    }
    return new ArrayList<>();
  }
}

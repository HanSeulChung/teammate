package com.api.backend.category.data.dto;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleCategoryDto {

  private Long id;
  private Long teamId;
  private LocalDateTime createDt;
  private LocalDateTime updateDt;
  private String categoryName;
  private CategoryType categoryType;
  private String color;

  public static ScheduleCategoryDto of(ScheduleCategory scheduleCategory) {
    return ScheduleCategoryDto.builder()
        .id(scheduleCategory.getScheduleCategoryId())
        .teamId(scheduleCategory.getTeam().getTeamId())
        .createDt(scheduleCategory.getCreateDt())
        .updateDt(scheduleCategory.getUpdateDt())
        .categoryName(scheduleCategory.getCategoryName())
        .categoryType(scheduleCategory.getCategoryType())
        .color(scheduleCategory.getColor())
        .build();
  }

  public static List<ScheduleCategoryDto> of(List<ScheduleCategory> scheduleCategories) {
    if (scheduleCategories != null) {
      List<ScheduleCategoryDto> scheduleCategoryDtoList = new ArrayList<>();
      for (ScheduleCategory scheduleCategory : scheduleCategories) {
        scheduleCategoryDtoList.add(ScheduleCategoryDto.of(scheduleCategory));
      }
      return scheduleCategoryDtoList;
    }
    return null;
  }

}

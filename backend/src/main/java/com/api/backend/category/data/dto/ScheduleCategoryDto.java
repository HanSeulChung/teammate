package com.api.backend.category.data.dto;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleCategoryDto {

  private Long categoryId;
  private Long teamId;
  private LocalDateTime createDt;
  private LocalDateTime updateDt;
  private String categoryName;
  private CategoryType categoryType;
  private String color;

  public static ScheduleCategoryDto of(ScheduleCategory scheduleCategory) {
    return ScheduleCategoryDto.builder()
        .categoryId(scheduleCategory.getScheduleCategoryId())
        .teamId(scheduleCategory.getTeam().getTeamId())
        .categoryName(scheduleCategory.getCategoryName())
        .categoryType(scheduleCategory.getCategoryType())
        .color(scheduleCategory.getColor())
        .createDt(scheduleCategory.getCreateDt())
        .updateDt(scheduleCategory.getUpdateDt())
        .build();
  }

  public static List<ScheduleCategoryDto> of(Page<ScheduleCategory> scheduleCategories) {
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

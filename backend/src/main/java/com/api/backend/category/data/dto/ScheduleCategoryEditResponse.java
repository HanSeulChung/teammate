package com.api.backend.category.data.dto;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ScheduleCategoryEditResponse {

  private Long categoryId;
  private Long updateTeamParticipantId;
  private String categoryName;
  private CategoryType categoryType;
  private LocalDateTime updateDt;
  private String color;

  public static ScheduleCategoryEditResponse from(ScheduleCategory scheduleCategory) {
    return ScheduleCategoryEditResponse.builder()
        .categoryId(scheduleCategory.getScheduleCategoryId())
        .updateTeamParticipantId(scheduleCategory.getCreateParticipantId())
        .categoryName(scheduleCategory.getCategoryName())
        .categoryType(scheduleCategory.getCategoryType())
        .updateDt(LocalDateTime.now())
        .color(scheduleCategory.getColor())
        .build();
  }
}

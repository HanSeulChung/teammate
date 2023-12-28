package com.api.backend.category.data.dto;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ScheduleCategoryResponse {

  private Long categoryId;
  private Long createParticipantId;
  private String categoryName;
  private CategoryType categoryType;
  private LocalDateTime createDt;
  private LocalDateTime updateDt;
  private String color;

  public static ScheduleCategoryResponse from(ScheduleCategory scheduleCategory) {
    return ScheduleCategoryResponse.builder()
        .categoryId(scheduleCategory.getScheduleCategoryId())
        .createParticipantId(scheduleCategory.getCreateParticipantId())
        .categoryName(scheduleCategory.getCategoryName())
        .categoryType(scheduleCategory.getCategoryType())
        .createDt(scheduleCategory.getCreateDt())
        .updateDt(scheduleCategory.getUpdateDt())
        .color(scheduleCategory.getColor())
        .build();
  }

  public static Page<ScheduleCategoryResponse> from(
      Page<ScheduleCategory> ScheduleCategory) {
    return ScheduleCategory.map(ScheduleCategoryResponse::from);
  }
}

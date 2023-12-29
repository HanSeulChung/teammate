package com.api.backend.schedule.data.dto;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class AllSchedulesMonthlyView {

  @Schema(description = "schedule id", example = "1L")
  private Long scheduleId;

  @Schema(description = "schedule type", example = "반복 일정/단순 일정")
  private String scheduleType;

  @Schema(description = "category type", example = "문서/일정")
  private CategoryType category;

  @Schema(description = "category name", example = "휴가")
  private String categoryName;

  @Schema(description = "title", example = "기획 회의")
  private String title;

  @Schema(description = "content", example = "컨셉 선정")
  private String content;

  @Schema(description = "place", example = "회의실")
  private String place;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "start date", example = "2023-12-15T12:00:00")
  private LocalDateTime startDt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "end date", example = "2023-12-15T12:00:00")
  private LocalDateTime endDt;

  @Schema(description = "color", example = "#ff0000")
  private String color;

  public static AllSchedulesMonthlyView from(RepeatSchedule repeatSchedule) {
    return AllSchedulesMonthlyView.builder()
        .scheduleId(repeatSchedule.getRepeatScheduleId())
        .scheduleType("반복일정")
        .category(repeatSchedule.getScheduleCategory().getCategoryType())
        .categoryName(repeatSchedule.getScheduleCategory().getCategoryName())
        .title(repeatSchedule.getTitle())
        .content(repeatSchedule.getContent())
        .place(repeatSchedule.getPlace())
        .startDt(repeatSchedule.getStartDt())
        .endDt(repeatSchedule.getEndDt())
        .color(repeatSchedule.getColor())
        .build();
  }

  public static AllSchedulesMonthlyView from(SimpleSchedule simpleSchedule) {
    return AllSchedulesMonthlyView.builder()
        .scheduleId(simpleSchedule.getSimpleScheduleId())
        .scheduleType("단순일정")
        .category(simpleSchedule.getScheduleCategory().getCategoryType())
        .categoryName(simpleSchedule.getScheduleCategory().getCategoryName())
        .title(simpleSchedule.getTitle())
        .content(simpleSchedule.getContent())
        .place(simpleSchedule.getPlace())
        .startDt(simpleSchedule.getStartDt())
        .endDt(simpleSchedule.getEndDt())
        .color(simpleSchedule.getColor())
        .build();
  }
}

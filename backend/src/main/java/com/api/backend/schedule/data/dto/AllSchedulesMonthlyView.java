package com.api.backend.schedule.data.dto;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@Builder
public class AllSchedulesMonthlyView {

  private Long scheduleId;
  private String scheduleType;
  private CategoryType category;
  private String categoryName;
  private String title;
  private String content;
  private String place;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
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

  public static Page<AllSchedulesMonthlyView> fromSimpleSchedulePage(
      Page<SimpleSchedule> simpleSchedules) {
    return simpleSchedules.map(AllSchedulesMonthlyView::from);
  }

  public static Page<AllSchedulesMonthlyView> fromRepeatSchedulePage(
      Page<RepeatSchedule> repeatSchedules) {
    return repeatSchedules.map(AllSchedulesMonthlyView::from);
  }
}

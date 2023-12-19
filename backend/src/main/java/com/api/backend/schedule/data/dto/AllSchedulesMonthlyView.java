package com.api.backend.schedule.data.dto;

import com.api.backend.category.data.entity.ScheduleCategory;
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
  private CategoryType category;
  private String title;
  private String content;
  private String place;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String color;
  private String scheduleType;

  public static AllSchedulesMonthlyView from(RepeatSchedule repeatSchedule) {
    return AllSchedulesMonthlyView.builder()
        .scheduleId(repeatSchedule.getRepeatScheduleId())
        .category(repeatSchedule.getScheduleCategory().getCategoryType())
        .title(repeatSchedule.getTitle())
        .content(repeatSchedule.getContent())
        .place(repeatSchedule.getPlace())
        .startDt(repeatSchedule.getStartDt())
        .endDt(repeatSchedule.getEndDt())
        .color(repeatSchedule.getColor())
        .scheduleType("반복일정")
        .build();
  }

  public static AllSchedulesMonthlyView from(SimpleSchedule simpleSchedule) {
    return AllSchedulesMonthlyView.builder()
        .scheduleId(simpleSchedule.getSimpleScheduleId())
        .category(simpleSchedule.getScheduleCategory().getCategoryType())
        .title(simpleSchedule.getTitle())
        .content(simpleSchedule.getContent())
        .place(simpleSchedule.getPlace())
        .startDt(simpleSchedule.getStartDt())
        .endDt(simpleSchedule.getEndDt())
        .color(simpleSchedule.getColor())
        .scheduleType("단순일정")
        .build();
  }

  public static Page<AllSchedulesMonthlyView> fromSimpleSchedulePage(Page<SimpleSchedule> simpleSchedules) {
    return simpleSchedules.map(AllSchedulesMonthlyView::from);
  }

  public static Page<AllSchedulesMonthlyView> fromRepeatSchedulePage(Page<RepeatSchedule> repeatSchedules) {
    return repeatSchedules.map(AllSchedulesMonthlyView::from);
  }
}

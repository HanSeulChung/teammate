package com.api.backend.schedule.data.entity;

import com.api.backend.global.domain.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "monthly_repeat_schedule")
public class MonthlyRepeatSchedule extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long monthlyRepeatScheduleId;
  @Setter
  private String day;
  @Setter
  private String title;
  @Setter
  private String content;
  @Setter
  private String place;
  @Setter
  private LocalDateTime startDt;
  @Setter
  private LocalDateTime endDt;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;
}

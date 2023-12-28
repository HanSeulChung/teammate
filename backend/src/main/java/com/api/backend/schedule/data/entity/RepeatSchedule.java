package com.api.backend.schedule.data.entity;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.global.domain.BaseEntity;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.api.backend.team.data.entity.Team;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "repeat_schedule")
public class RepeatSchedule extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long repeatScheduleId;
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
  @Enumerated(EnumType.STRING)
  @Setter
  private RepeatCycle repeatCycle;
  @Setter
  private String month;
  @Setter
  private int day;
  @Setter
  private String dayOfWeek;
  @Setter
  private String color;
  @Setter
  private Long originRepeatScheduleId;

  private Long createParticipantId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_category_id")
  @Setter
  private ScheduleCategory scheduleCategory;

  @Setter
  @OneToMany(mappedBy = "repeatSchedule", cascade = CascadeType.ALL)
  private List<TeamParticipantsSchedule> teamParticipantsSchedules;

  public void setRepeatScheduleInfo(ScheduleCategory scheduleCategory, String title, String content,
      LocalDateTime startDt, LocalDateTime endDt, String place, String color) {
    if (scheduleCategory != null) {
      this.scheduleCategory = scheduleCategory;
    }
    if (title != null){
      this.title = title;
    }
    if (content != null) {
      this.content = content;
    }
    if (startDt != null) {
      this.startDt = startDt;
    }
    if (endDt != null) {
      this.endDt = endDt;
    }
    if (place != null) {
      this.place = place;
    }
    if (color != null) {
      this.color = color;
    }
  }
}

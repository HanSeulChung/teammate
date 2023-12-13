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
  private Month month;
  @Setter
  private int day;
  @Setter
  private String dayOfWeek;
  @Setter
  private String color;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_category_id")
  private ScheduleCategory scheduleCategory;

  @Setter
  @OneToMany(mappedBy = "repeatSchedule", cascade = CascadeType.ALL)
  private List<TeamParticipantsSchedule> teamParticipantsSchedules;

}

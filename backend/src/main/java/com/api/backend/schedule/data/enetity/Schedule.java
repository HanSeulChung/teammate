package com.api.backend.schedule.data.enetity;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.global.domain.BaseEntity;
import com.api.backend.team.data.entity.Team;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import javax.persistence.Column;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "schedule")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Schedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long scheduleId;

  private String title;
  private String content;
  private String place;
  @Type(type = "json")
  @Column(columnDefinition = "longtext")
  private HashMap<Long, String> scheduleParticipantMap = new HashMap<>();

  private LocalDateTime startDt;
  private LocalDateTime endDt;
  private boolean repeatYn;
  private LocalDate repeatCycle;
  private String color;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teamId")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "scheduleCategoryId")
  private ScheduleCategory scheduleCategory;
}

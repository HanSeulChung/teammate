package com.api.backend.schedule.data.entity;

import com.api.backend.global.domain.BaseEntity;
import com.api.backend.team.data.entity.TeamParticipants;
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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "team_participants_schedule")
public class TeamParticipantsSchedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teamParticipantsScheduleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_participants_id")
  @Setter
  private TeamParticipants teamParticipants;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "simple_schedule_id")
  @Setter
  private SimpleSchedule simpleSchedule;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "repeat_schedule_id")
  @Setter
  private RepeatSchedule repeatSchedule;

}

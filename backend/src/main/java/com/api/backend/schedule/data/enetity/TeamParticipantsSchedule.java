package com.api.backend.schedule.data.enetity;

import com.api.backend.global.domain.BaseEntity;
import com.api.backend.team.data.entity.TeamParticipants;
import javax.persistence.Entity;
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

  @ManyToOne
  @JoinColumn(name = "team_participants_id")
  private TeamParticipants teamParticipants;

  @ManyToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;
}

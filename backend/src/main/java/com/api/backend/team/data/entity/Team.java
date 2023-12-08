package com.api.backend.team.data.entity;

import com.api.backend.global.domain.BaseEntity;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.schedule.data.entity.Schedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@Table(name = "team")
public class Team extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teamId;
  private String name;
  private LocalDateTime restorationTime;
  private boolean isDelete;
  private int memberLimit;
  private String inviteLink;
  private String profileUrl;

  @OneToMany(mappedBy = "team")
  private List<TeamParticipants> teamParticipants = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  private List<Schedule> schedules = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  private List<Documents> documents = new ArrayList<>();


  public void setInviteLink() {
    this.inviteLink = this.teamId +
        "/" + UUID.randomUUID();
  }
}

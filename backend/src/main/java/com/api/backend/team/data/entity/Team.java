package com.api.backend.team.data.entity;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.global.domain.BaseEntity;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import lombok.Setter;

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
  @Setter
  private LocalDate restorationDt;
  @Setter
  private boolean isDelete;
  private int memberLimit;
  @Setter
  private String inviteCode;
  private String profileUrl;

  @OneToMany(mappedBy = "team")
  @Builder.Default
  private List<TeamParticipants> teamParticipants = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  @Builder.Default
  private List<SimpleSchedule> simpleSchedules = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  @Builder.Default
  private List<RepeatSchedule> repeatSchedules = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  @Builder.Default
  private List<ScheduleCategory> scheduleCategories = new ArrayList<>();

  public void updateNameAndProfileUrl(String nickName, String url) {
    if (!name.equals(nickName)) {
      name = nickName;
    }
    if (!profileUrl.equals(url)) {
      profileUrl = url;
    }
  }

  public void changeRestoreInfo() {
    this.isDelete = true;
    this.restorationDt = null;
  }
}

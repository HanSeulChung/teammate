package com.api.backend.team.scheduler;

import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TeamRestoreScheduler {

  private final TeamRepository teamRepository;

  public TeamRestoreScheduler(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void teamRestoreCheckAndUpdate() {
    List<Team> teams = teamRepository.findAllByRestorationDtIsNotNull();

    for (Team team : teams) {
      if (!team.getRestorationDt().isAfter(LocalDate.now())) {
        team.setDelete(true);
      }
    }
  }
}

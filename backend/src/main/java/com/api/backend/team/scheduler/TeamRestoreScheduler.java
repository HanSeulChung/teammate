package com.api.backend.team.scheduler;

import com.api.backend.file.service.FileProcessService;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Profile("prod")
@Component
public class TeamRestoreScheduler {

  private final FileProcessService fileProcessService;
  private final TeamRepository teamRepository;

  public TeamRestoreScheduler(TeamRepository teamRepository, FileProcessService fileProcessService) {
    this.teamRepository = teamRepository;
    this.fileProcessService = fileProcessService;
  }

  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void teamRestoreCheckAndUpdate() {
    List<Team> teams = teamRepository.findAllByRestorationDtIsNotNull();

    for (Team team : teams) {
      if (!team.getRestorationDt().isAfter(LocalDate.now())) {
        team.setDelete(true);
        fileProcessService.deleteImage(team.getProfileUrl());
        log.info("현재 날짜 {}에 해당 팀을 삭제(soft delete)한 뒤 팀 프로필 이미지를 삭제하였습니다.", LocalDateTime.now());
      }
    }
  }
}

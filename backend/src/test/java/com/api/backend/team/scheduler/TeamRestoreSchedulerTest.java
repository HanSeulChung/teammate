package com.api.backend.team.scheduler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TeamRestoreSchedulerTest {

  @Mock
  private TeamRepository teamRepository;

  @InjectMocks
  private TeamRestoreScheduler teamRestoreScheduler;

  @Test
  @DisplayName("매일 자정에 팀 해체 기한이 지났을 경우")
  void teamRestoreCheckAndUpdate() {
    // arrange
    LocalDate today = LocalDate.now();
    LocalDate pastDate = today.minusDays(1);
    LocalDate futureDate = today.plusDays(1);

    Team teamWithPastDate = new Team();
    teamWithPastDate.setRestorationDt(pastDate);

    Team teamWithFutureDate = new Team();
    teamWithFutureDate.setRestorationDt(futureDate);

    List<Team> teams = Arrays.asList(teamWithPastDate, teamWithFutureDate);

    // given
    when(teamRepository.findByRestorationDtIsNotNull()).thenReturn(teams);

    // when
    teamRestoreScheduler.teamRestoreCheckAndUpdate();

    // then
    verify(teamRepository, times(1)).findByRestorationDtIsNotNull();
  }
}
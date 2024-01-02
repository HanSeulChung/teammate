package com.api.backend.team.scheduler;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.comment.service.CommentService;
import com.api.backend.documents.service.DocumentService;
import com.api.backend.file.service.FileProcessService;
import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.data.repository.NotificationRepository;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.schedule.data.repository.RepeatScheduleRepository;
import com.api.backend.schedule.data.repository.SimpleScheduleRepository;
import com.api.backend.schedule.data.repository.TeamParticipantsScheduleRepository;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamDisbandScheduler {

  private final DocumentService documentService;
  private final CommentService commentService;
  private final FileProcessService fileProcessService;
  private final TeamRepository teamRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private final ScheduleCategoryRepository scheduleCategoryRepository;
  private final NotificationRepository notificationRepository;
  private final SimpleScheduleRepository simpleScheduleRepository;
  private final RepeatScheduleRepository repeatScheduleRepository;
  private final TeamParticipantsScheduleRepository teamParticipantsScheduleRepository;

  @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
  @Transactional
  public void teamRestoreCheckAndUpdate() {
    List<Team> teams = teamRepository.findAllByRestorationDtIsNotNull();

    for (Team team : teams) {
      if (!team.getRestorationDt().isAfter(LocalDate.now())) {
        team.changeRestoreInfo();
      }
    }
  }

  @Scheduled(cron = "0 0 3 1 1 *") // 매년 1월 1일 03시
  public void teamDisbandCheckAndDelete() {
    List<Team> teams = teamRepository.findAllByIsDeleteIsTrue();

    if (teams.isEmpty()) {
      return;
    }

    List<Long> teamIds = teams.stream().map(Team::getTeamId).collect(Collectors.toList());
    documentService.deleteAllDocsInTeams(teamIds);
    commentService.deleteAllCommentsInTeams(teamIds);

    teams.forEach(team -> {
      deleteProcessing(team);
      fileProcessService.deleteImage(team.getProfileUrl());
      log.info("현재 날짜 {}에 삭제할 팀 {}의 프로필 이미지를 삭제하였습니다.", LocalDateTime.now(), team.getName());
    });

    teamRepository.deleteAllByIdsInQuery(teamIds);
  }

  @Transactional
  public void deleteProcessing(Team team) {
    List<Long> teamParticipantsIds = new ArrayList<>();

    List<Long> notificationIds = new ArrayList<>();
    List<Long> teamParticipantScheduleIds = new ArrayList<>();

    for (TeamParticipants teamParticipant : team.getTeamParticipants()) {
      teamParticipantsIds.add(teamParticipant.getTeamParticipantsId());

      notificationIds.addAll(
          teamParticipant.getNotifications()
              .stream().map(Notification::getNotificationId)
              .collect(Collectors.toList())
      );

      teamParticipantScheduleIds.addAll(
          teamParticipant.getTeamParticipantsSchedules()
              .stream().map(TeamParticipantsSchedule::getTeamParticipantsScheduleId).collect(Collectors.toList())
      );
    }

    if (!notificationIds.isEmpty()) {
      notificationRepository.deleteAllByIdInQuery(
          notificationIds
      );
    }
    if (!teamParticipantScheduleIds.isEmpty()) {
      teamParticipantsScheduleRepository.deleteAllByIdInQuery(
          teamParticipantScheduleIds
      );
    }
    if (!teamParticipantsIds.isEmpty()) {
      teamParticipantsRepository.deleteAllByIdInQuery(
          teamParticipantsIds
      );
    }

    List<Long> repeatScheduleIds = team.getRepeatSchedules().stream()
        .map(RepeatSchedule::getRepeatScheduleId)
        .collect(Collectors.toList());

    List<Long> simpleScheduleIds = team.getSimpleSchedules().stream()
        .map(SimpleSchedule::getSimpleScheduleId)
        .collect(Collectors.toList());

    List<Long> scheduleCategoryIds = team.getScheduleCategories().stream()
        .map(ScheduleCategory::getScheduleCategoryId)
        .collect(Collectors.toList());

    if (!repeatScheduleIds.isEmpty()) {
      repeatScheduleRepository.deleteAllByIdsInQuery(
          repeatScheduleIds);
    }
    if (!simpleScheduleIds.isEmpty()) {
      simpleScheduleRepository.deleteAllByIdInQuery(
          simpleScheduleIds
      );
    }
    if (!scheduleCategoryIds.isEmpty()) {
      scheduleCategoryRepository.deleteAllByIdInQuery(
          scheduleCategoryIds
      );
    }
  }
}


package com.api.backend.schedule.service.impl;

import static com.api.backend.global.exception.type.ErrorCode.INVALID_REPEAT_CYCLE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_NOT_FOUND_EXCEPTION;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.ScheduleEditRequest;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;
import com.api.backend.schedule.data.repository.ScheduleRepository;
import com.api.backend.schedule.service.ScheduleService;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final TeamRepository teamRepository;
  private final ScheduleCategoryRepository categoryRepository;

  @Override
  @Transactional
  public Page<Schedule> addSchedules(ScheduleRequest scheduleRequest) {
    Team team = validateTeam(scheduleRequest.getTeamId());
    ScheduleCategory category = validateCategory(scheduleRequest.getCategoryId());
    List<Schedule> schedules = new ArrayList<>();

    if (scheduleRequest.isRepeat()) {
      schedules.addAll(createRepeatingSchedules(scheduleRequest, team, category));
    } else {
      Schedule schedule = createSingleSchedule(scheduleRequest, team, category);
      schedules.add(schedule);
    }
    return new PageImpl<>(schedules);
  }

  private List<Schedule> createRepeatingSchedules(ScheduleRequest scheduleRequest, Team team,
      ScheduleCategory category) {
    List<Schedule> schedules = new ArrayList<>();
    LocalDateTime currentStart = scheduleRequest.getStartDt();

    while (currentStart.isBefore(scheduleRequest.getEndDt())) {
      Schedule schedule = createSchedules(scheduleRequest, team, category, currentStart);
      schedules.add(schedule);

      switch (scheduleRequest.getRepeatCycle()) {
        case WEEKLY:
          currentStart = currentStart.plus(1, ChronoUnit.WEEKS);
          break;
        case MONTHLY:
          currentStart = currentStart.plus(1, ChronoUnit.MONTHS);
          break;
        case YEARLY:
          currentStart = currentStart.plus(1, ChronoUnit.YEARS);
          break;
        default:
          throw new CustomException(INVALID_REPEAT_CYCLE_EXCEPTION);
      }

      currentStart = currentStart.plus(1, ChronoUnit.WEEKS);
    }
    return schedules;
  }

  private Schedule createSingleSchedule(ScheduleRequest scheduleRequest, Team team,
      ScheduleCategory category) {
    return createSchedules(scheduleRequest, team, category, scheduleRequest.getStartDt());
  }

  private Schedule createSchedules(ScheduleRequest scheduleRequest, Team team,
      ScheduleCategory category, LocalDateTime currentStart) {
    Schedule schedule = Schedule.builder()
        .team(team)
        .scheduleCategory(category)
        .title(scheduleRequest.getTitle())
        .content(scheduleRequest.getContent())
        .startDt(currentStart)
        .endDt(currentStart)
        .isRepeat(scheduleRequest.isRepeat())
        .repeatCycle(scheduleRequest.getRepeatCycle())
        .teamParticipants(scheduleRequest.getTeamParticipants())
        .place(scheduleRequest.getPlace())
        .color(scheduleRequest.getColor())
        .build();

    scheduleRepository.save(schedule);
    return schedule;
  }


  @Override
  public Page<Schedule> searchSchedule(Pageable pageable) {
    Page<Schedule> schedules = scheduleRepository.findAll(pageable);
    List<Schedule> scheduleList = schedules.toList();
    return new PageImpl<>(scheduleList);
  }

  @Override
  @Transactional
  public Schedule editSchedule(ScheduleEditRequest scheduleEditRequest) {
    if (scheduleEditRequest.getScheduleId() == null) {
      throw new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION);
    }
    Team team = validateTeam(scheduleEditRequest.getTeamId());
    ScheduleCategory category = validateCategory(scheduleEditRequest.getCategoryId());

    Schedule schedule = Schedule.builder()
        .scheduleId(scheduleEditRequest.getScheduleId())
        .team(team)
        .scheduleCategory(category)
        .title(scheduleEditRequest.getTitle())
        .content(scheduleEditRequest.getContent())
        .startDt(scheduleEditRequest.getStartDt())
        .endDt(scheduleEditRequest.getEndDt())
        .color(scheduleEditRequest.getColor())
        .place(scheduleEditRequest.getPlace())
        .isRepeat(scheduleEditRequest.isRepeat())
        .repeatCycle(scheduleEditRequest.getRepeatCycle())
        .teamParticipants(scheduleEditRequest.getTeamParticipants())
        .build();

    return scheduleRepository.save(schedule);
  }

  @Override
  @Transactional
  public void deleteSchedule(Long scheduleId) {
    Schedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
    scheduleRepository.delete(schedule);
  }


  public Team validateTeam(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
  }

  public ScheduleCategory validateCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION));
  }

}

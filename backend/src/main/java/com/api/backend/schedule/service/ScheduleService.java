package com.api.backend.schedule.service;

import static com.api.backend.global.exception.type.ErrorCode.INVALID_REPEAT_CYCLE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.entity.Schedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.schedule.data.repository.ScheduleRepository;
import com.api.backend.schedule.data.repository.TeamParticipantsScheduleRepository;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final TeamRepository teamRepository;
  private final ScheduleCategoryRepository categoryRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private final TeamParticipantsScheduleRepository teamParticipantsScheduleRepository;

  public Page<Schedule> addSchedules(ScheduleRequest scheduleRequest) {
    Team team = findTeamOrElseThrow(scheduleRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(scheduleRequest.getCategoryId());
    List<Schedule> schedules;

    if (scheduleRequest.isRepeat()) {
      schedules = createRepeatingSchedules(scheduleRequest, team, category);
    } else {
      Schedule schedule = createSingleSchedule(scheduleRequest, team, category);
      schedules = Collections.singletonList(schedule);
    }
    return new PageImpl<>(schedules);
  }

  public Schedule searchScheduleDetailInfo(Long scheduleId, Long teamId) {
    findTeamOrElseThrow(teamId);
    return scheduleRepository.findScheduleByScheduleIdAndTeam_TeamId(scheduleId, teamId);
  }

  public Schedule editSchedule(ScheduleRequest editRequest) {
    if (editRequest.getScheduleId() == null) {
      throw new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION);
    }

    Team team = findTeamOrElseThrow(editRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(editRequest.getCategoryId());

    Schedule existingSchedule = scheduleRepository.findById(editRequest.getScheduleId())
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));

    Schedule updatedSchedule = Schedule.builder()
        .scheduleId(existingSchedule.getScheduleId())
        .team(team)
        .scheduleCategory(category)
        .title(editRequest.getTitle())
        .content(editRequest.getContent())
        .startDt(editRequest.getStartDt())
        .endDt(editRequest.getEndDt())
        .place(editRequest.getPlace())
        .isRepeat(editRequest.isRepeat())
        .repeatCycle(editRequest.getRepeatCycle())
        .teamParticipantsSchedules(new ArrayList<>())
        .build();

    List<TeamParticipantsSchedule> existingTeamParticipantsSchedules =
        teamParticipantsScheduleRepository.findAllBySchedule_ScheduleId(
            existingSchedule.getScheduleId());

    for (Long teamParticipantsId : editRequest.getTeamParticipantsIds()) {
      TeamParticipants participants = findTeamParticipantsOrElseThrow(teamParticipantsId);

      TeamParticipantsSchedule teamParticipantsSchedule = existingTeamParticipantsSchedules.stream()
          .filter(schedule -> schedule.getTeamParticipants().getTeamParticipantsId()
              .equals(participants.getTeamParticipantsId()))
          .findFirst()
          .orElse(TeamParticipantsSchedule.builder().teamParticipants(participants)
              .schedule(updatedSchedule).build());

      updatedSchedule.getTeamParticipantsSchedules().add(teamParticipantsSchedule);
    }

    scheduleRepository.save(updatedSchedule);
    return updatedSchedule;
  }

  @Transactional
  public void deleteSchedule(Long scheduleId) {
    Schedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
    scheduleRepository.delete(schedule);
  }


  public Team findTeamOrElseThrow(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
  }

  public ScheduleCategory findScheduleCategoryOrElseThrow(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION));
  }

  public TeamParticipants findTeamParticipantsOrElseThrow(Long teamParticipantsId) {
    return teamParticipantsRepository.findById(teamParticipantsId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));
  }

  private Schedule createSingleSchedule(ScheduleRequest scheduleRequest, Team team,
      ScheduleCategory category) {
    return createSchedules(scheduleRequest, team, category, scheduleRequest.getStartDt());
  }

  private Schedule createSchedules(ScheduleRequest scheduleRequest, Team team,
      ScheduleCategory category, LocalDateTime currentStart) {

    Schedule schedule = Schedule.builder()
        .scheduleId(scheduleRequest.getScheduleId())
        .team(team)
        .scheduleCategory(category)
        .title(scheduleRequest.getTitle())
        .content(scheduleRequest.getContent())
        .startDt(currentStart)
        .endDt(currentStart)
        .isRepeat(scheduleRequest.isRepeat())
        .repeatCycle(scheduleRequest.getRepeatCycle())
        .teamParticipantsSchedules(new ArrayList<>())
        .place(scheduleRequest.getPlace())
        .build();

    List<TeamParticipantsSchedule> teamParticipantsSchedules = schedule.getTeamParticipantsSchedules();

    for (Long teamParticipantsId : scheduleRequest.getTeamParticipantsIds()) {
      TeamParticipants participants = findTeamParticipantsOrElseThrow(teamParticipantsId);

      TeamParticipantsSchedule teamParticipantsSchedule = TeamParticipantsSchedule.builder()
          .teamParticipants(participants)
          .schedule(schedule)
          .build();

      teamParticipantsSchedule.setSchedule(schedule);
      teamParticipantsSchedule.setTeamParticipants(participants);
      teamParticipantsSchedules.add(teamParticipantsSchedule);
    }

    schedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    scheduleRepository.save(schedule);
    return schedule;
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
    }
    return schedules;
  }

}

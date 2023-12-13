package com.api.backend.schedule.service;

import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.category.type.CategoryType;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.ScheduleEditRequest;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleService {

  private final SimpleScheduleRepository simpleScheduleRepository;
  private final RepeatScheduleRepository repeatScheduleRepository;
  private final TeamRepository teamRepository;
  private final ScheduleCategoryRepository categoryRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private final TeamParticipantsScheduleRepository teamParticipantsScheduleRepository;


  @Transactional
  public SimpleSchedule addSimpleSchedule(ScheduleRequest scheduleRequest) {
    Team team = findTeamOrElseThrow(scheduleRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(scheduleRequest.getCategoryId());

    List<Long> teamParticipantsIds = scheduleRequest.getTeamParticipantsIds();

    if (teamParticipantsIds == null) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION);
    }

    SimpleSchedule simpleSchedule = SimpleSchedule.builder()
        .team(team)
        .scheduleCategory(category)
        .title(scheduleRequest.getTitle())
        .content(scheduleRequest.getContent())
        .startDt(scheduleRequest.getStartDt())
        .endDt(scheduleRequest.getEndDt())
        .teamParticipantsSchedules(new ArrayList<>())
        .place(scheduleRequest.getPlace())
        .build();

    List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesBySimpleSchedule(
        simpleSchedule, scheduleRequest.getTeamParticipantsIds()
    );
    simpleSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    simpleScheduleRepository.save(simpleSchedule);
    return simpleSchedule;
  }


  @Transactional
  public RepeatSchedule addRepeatSchedule(ScheduleRequest scheduleRequest) {
    Team team = findTeamOrElseThrow(scheduleRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(scheduleRequest.getCategoryId());

    List<Long> teamParticipantsIds = scheduleRequest.getTeamParticipantsIds();

    if (teamParticipantsIds == null) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION);
    }

    validateSameTeamOrElsThrow(teamParticipantsIds, team.getTeamId());

    RepeatSchedule repeatSchedule = RepeatSchedule.builder()
        .scheduleCategory(category)
        .team(team)
        .title(scheduleRequest.getTitle())
        .content(scheduleRequest.getContent())
        .place(scheduleRequest.getPlace())
        .startDt(scheduleRequest.getStartDt())
        .endDt(scheduleRequest.getEndDt())
        .repeatCycle(scheduleRequest.getRepeatCycle())
        .color(scheduleRequest.getColor())
        .build();

    String month = scheduleRequest.getStartDt().getMonth().name();
    int day = scheduleRequest.getStartDt().getDayOfMonth();
    String dayOfWeek = String.valueOf(scheduleRequest.getStartDt().getDayOfWeek());

    switch (scheduleRequest.getRepeatCycle()) {
      case WEEKLY:
        repeatSchedule.setDayOfWeek(dayOfWeek);
        break;
      case MONTHLY:
        repeatSchedule.setDay(day);
        break;
      case YEARLY:
        repeatSchedule.setMonth(month);
        repeatSchedule.setDay(day);
        break;
      default:
        throw new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION);
    }

    List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesByRepeatSchedule(
        repeatSchedule, scheduleRequest.getTeamParticipantsIds()
    );
    repeatSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    repeatScheduleRepository.save(repeatSchedule);
    return repeatSchedule;
  }

  public SimpleSchedule searchScheduleDetailInfo(Long scheduleId, Long teamId) {
    findTeamOrElseThrow(teamId);
    return simpleScheduleRepository.findSimpleScheduleBySimpleScheduleIdAndTeam_TeamId(scheduleId,
        teamId);
  }

  public SimpleSchedule editSimpleScheduleAndSave(ScheduleEditRequest editRequest) {
    Team team = findTeamOrElseThrow(editRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(editRequest.getCategoryId());

    validateSameTeamOrElsThrow(editRequest.getTeamParticipantsIds(), team.getTeamId());

    SimpleSchedule existingSimpleSchedule = simpleScheduleRepository.findById(
            editRequest.getScheduleId())
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION)
        );

    SimpleSchedule updatedSimpleSchedule = SimpleSchedule.builder()
        .team(team)
        .scheduleCategory(category)
        .title(editRequest.getTitle())
        .content(editRequest.getContent())
        .startDt(editRequest.getStartDt())
        .endDt(editRequest.getEndDt())
        .place(editRequest.getPlace())
        .build();

    List<TeamParticipantsSchedule> existingTeamParticipantsSchedules =
        teamParticipantsScheduleRepository.findAllBySimpleSchedule_SimpleScheduleId(
            existingSimpleSchedule.getSimpleScheduleId()
        );

    List<TeamParticipantsSchedule> updatedTeamParticipantsSchedules =
        buildTeamParticipantsSchedulesBySimpleSchedule(updatedSimpleSchedule,
            editRequest.getTeamParticipantsIds()
        );

    for (TeamParticipantsSchedule existingTeamParticipantsSchedule : existingTeamParticipantsSchedules) {
      TeamParticipants participants = existingTeamParticipantsSchedule.getTeamParticipants();

      if (editRequest.getTeamParticipantsIds().contains(participants.getTeamParticipantsId())) {
        updatedSimpleSchedule.getTeamParticipantsSchedules().add(existingTeamParticipantsSchedule);
      }
    }

    updatedSimpleSchedule.setTeamParticipantsSchedules(updatedTeamParticipantsSchedules);
    simpleScheduleRepository.save(updatedSimpleSchedule);
    return updatedSimpleSchedule;
  }


  @Transactional
  public void deleteSimpleSchedule(Long scheduleId) {
    SimpleSchedule simpleSchedule = simpleScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
    simpleScheduleRepository.delete(simpleSchedule);
  }

  public Page<ScheduleResponse> getSchedulesForMonth(Long teamId, LocalDate monthStart,
      LocalDate monthEnd, CategoryType type, Pageable pageable) {
    LocalDateTime startDt = monthStart.atStartOfDay();
    LocalDateTime endDt = monthEnd.atTime(LocalTime.MAX);
    return getSchedulesByDateRange(teamId, startDt, endDt, type, pageable);
  }


  private List<TeamParticipantsSchedule> buildTeamParticipantsSchedulesBySimpleSchedule(
      SimpleSchedule simpleSchedule,
      List<Long> teamParticipantsIds) {
    List<TeamParticipantsSchedule> teamParticipantsSchedules = new ArrayList<>();

    for (Long teamParticipantsId : teamParticipantsIds) {
      TeamParticipants participants = findTeamParticipantsOrElseThrow(teamParticipantsId);

      TeamParticipantsSchedule teamParticipantsSchedule = TeamParticipantsSchedule.builder()
          .teamParticipants(participants)
          .simpleSchedule(simpleSchedule)
          .build();

      teamParticipantsSchedule.setSimpleSchedule(simpleSchedule);
      teamParticipantsSchedule.setTeamParticipants(participants);
      teamParticipantsSchedules.add(teamParticipantsSchedule);
    }

    return teamParticipantsSchedules;
  }

  private List<TeamParticipantsSchedule> buildTeamParticipantsSchedulesByRepeatSchedule(
      RepeatSchedule repeatSchedule,
      List<Long> teamParticipantsIds) {
    List<TeamParticipantsSchedule> teamParticipantsSchedules = new ArrayList<>();

    for (Long teamParticipantsId : teamParticipantsIds) {
      TeamParticipants participants = findTeamParticipantsOrElseThrow(teamParticipantsId);

      TeamParticipantsSchedule teamParticipantsSchedule = TeamParticipantsSchedule.builder()
          .teamParticipants(participants)
          .repeatSchedule(repeatSchedule)
          .build();

      teamParticipantsSchedule.setRepeatSchedule(repeatSchedule);
      teamParticipantsSchedule.setTeamParticipants(participants);
      teamParticipantsSchedules.add(teamParticipantsSchedule);
    }

    return teamParticipantsSchedules;
  }


  private Team findTeamOrElseThrow(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
  }

  private ScheduleCategory findScheduleCategoryOrElseThrow(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION));
  }

  private TeamParticipants findTeamParticipantsOrElseThrow(Long teamParticipantsId) {
    return teamParticipantsRepository.findById(teamParticipantsId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));
  }


  private void validateSameTeamOrElsThrow(List<Long> teamParticipantsIds, Long teamId) {
    for (Long teamParticipantsId : teamParticipantsIds) {
      TeamParticipants participants = findTeamParticipantsOrElseThrow(teamParticipantsId);
      if (teamId != participants.getTeam().getTeamId()) {
        throw new CustomException(ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION);
      }
    }
  }

  private Page<ScheduleResponse> getSchedulesByDateRange(Long teamId, LocalDateTime startDt,
      LocalDateTime endDt, CategoryType type, Pageable pageable) {
    Page<SimpleSchedule> schedules;

    if (type != null) {
      schedules = simpleScheduleRepository.findByTeam_TeamIdAndStartDtBetweenAndScheduleCategory_CategoryType(
          teamId, startDt, endDt, type, pageable);
    } else {
      schedules = simpleScheduleRepository.findByTeam_TeamIdAndStartDtBetween(teamId, startDt,
          endDt, pageable);
    }

    return ScheduleResponse.from(schedules);
  }
}

package com.api.backend.schedule.service;

import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_ID_DUPLICATE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.schedule.data.type.RepeatCycle.WEEKLY;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.category.type.CategoryType;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.RepeatScheduleInfoEditRequest;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.dto.ScheduleResponse;
import com.api.backend.schedule.data.dto.SimpleScheduleInfoEditRequest;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import com.api.backend.schedule.data.repository.RepeatScheduleRepository;
import com.api.backend.schedule.data.repository.SimpleScheduleRepository;
import com.api.backend.schedule.data.repository.TeamParticipantsScheduleRepository;
import com.api.backend.schedule.data.type.EditOption;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    setRepeatScheduleFieldsByCycle(repeatSchedule, month, day, dayOfWeek,
        scheduleRequest.getRepeatCycle());

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

  public Page<ScheduleResponse> getSchedulesForMonth(Long teamId, LocalDate monthStart,
      LocalDate monthEnd, CategoryType type, Pageable pageable) {
    LocalDateTime startDt = monthStart.atStartOfDay();
    LocalDateTime endDt = monthEnd.atTime(LocalTime.MAX);
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

  @Transactional
  //단순 일정 -> 단순 일정
  public SimpleSchedule editSimpleSchedule(SimpleScheduleInfoEditRequest editRequest) {
    Team team = findTeamOrElseThrow(editRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(editRequest.getCategoryId());
    List<Long> teamParticipantsIds = editRequest.getTeamParticipantsIds();
    validateSameTeamOrElsThrow(teamParticipantsIds, team.getTeamId());

    SimpleSchedule updatedSimpleSchedule = SimpleSchedule.builder()
        .simpleScheduleId(editRequest.getSimpleScheduleId())
        .team(team)
        .scheduleCategory(category)
        .title(editRequest.getTitle())
        .content(editRequest.getContent())
        .startDt(editRequest.getStartDt())
        .endDt(editRequest.getEndDt())
        .place(editRequest.getPlace())
        .color(editRequest.getColor())
        .build();

    List<TeamParticipantsSchedule> originTeamParticipantsSchedules =
        teamParticipantsScheduleRepository.findAllBySimpleSchedule_SimpleScheduleId(
            updatedSimpleSchedule.getSimpleScheduleId()
        );

    teamParticipantsScheduleRepository.deleteAll(originTeamParticipantsSchedules);

    List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesBySimpleSchedule(
        updatedSimpleSchedule, teamParticipantsIds);

    updatedSimpleSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    return simpleScheduleRepository.save(updatedSimpleSchedule);
  }

  @Transactional
  //반복 일정 -> 반복 일정
  public RepeatSchedule editRepeatSchedule(RepeatScheduleInfoEditRequest editRequest) {
    Team team = findTeamOrElseThrow(editRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(editRequest.getCategoryId());
    List<Long> teamParticipantsIds = editRequest.getTeamParticipantsIds();
    validateSameTeamOrElsThrow(teamParticipantsIds, team.getTeamId());

    String month = editRequest.getStartDt().getMonth().name();
    int day = editRequest.getStartDt().getDayOfMonth();
    String dayOfWeek = String.valueOf(editRequest.getStartDt().getDayOfWeek());

    RepeatSchedule originRepeatSchedule = repeatScheduleRepository.findById(
        editRequest.getRepeatScheduleId()).orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));

    //모든 일정을 변경할 시엔 update
    if (editRequest.getEditOption().equals(EditOption.ALL_SCHEDULES)){
      RepeatSchedule scheduleByOriginRepeatScheduleId = repeatScheduleRepository.findByOriginRepeatScheduleId(
          editRequest.getRepeatScheduleId());
      //이전에 이 일정, 이일정및 향후 일정을 수정한 기록이 있을 수 있기때문에 체크
      if (scheduleByOriginRepeatScheduleId != null) {
        repeatScheduleRepository.delete(scheduleByOriginRepeatScheduleId);
      }
      RepeatSchedule updateRepeatSchedule = RepeatSchedule.builder()
          .repeatScheduleId(editRequest.getRepeatScheduleId())
          .scheduleCategory(category)
          .team(team)
          .title(editRequest.getTitle())
          .content(editRequest.getContent())
          .place(editRequest.getPlace())
          .startDt(editRequest.getStartDt())
          .endDt(editRequest.getEndDt())
          .repeatCycle(editRequest.getRepeatCycle())
          .color(editRequest.getColor())
          .build();

      setRepeatScheduleFieldsByCycle(updateRepeatSchedule, month, day, dayOfWeek, editRequest.getRepeatCycle());


      List<TeamParticipantsSchedule> originTeamParticipantsSchedules =
          teamParticipantsScheduleRepository.findAllByRepeatSchedule_RepeatScheduleId(updateRepeatSchedule.getRepeatScheduleId()
          );

      teamParticipantsScheduleRepository.deleteAll(originTeamParticipantsSchedules);

      List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesByRepeatSchedule(
          updateRepeatSchedule, teamParticipantsIds);

      updateRepeatSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
      return repeatScheduleRepository.save(updateRepeatSchedule);

      //이 일정 혹은 이 일정 및 향후 일정 변경시 기존반복일정 id 값은 컬럼에 넣고, 새롭게 insert
    } else {
      RepeatSchedule updateRepeatSchedule = RepeatSchedule.builder()
          .scheduleCategory(category)
          .team(team)
          .title(editRequest.getTitle())
          .content(editRequest.getContent())
          .place(editRequest.getPlace())
          .startDt(editRequest.getStartDt())
          .endDt(editRequest.getEndDt())
          .repeatCycle(editRequest.getRepeatCycle())
          .color(editRequest.getColor())
          .originRepeatScheduleId(originRepeatSchedule.getRepeatScheduleId())
          .build();

      setRepeatScheduleFieldsByCycle(updateRepeatSchedule, month, day, dayOfWeek, editRequest.getRepeatCycle());

      List<TeamParticipantsSchedule> originTeamParticipantsSchedules =
          teamParticipantsScheduleRepository.findAllByRepeatSchedule_RepeatScheduleId(
              updateRepeatSchedule.getRepeatScheduleId()
          );

      teamParticipantsScheduleRepository.deleteAll(originTeamParticipantsSchedules);

      List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesByRepeatSchedule(
          updateRepeatSchedule, teamParticipantsIds);

      updateRepeatSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
      return repeatScheduleRepository.save(updateRepeatSchedule);
    }

  }

  @Transactional
  public void deleteSimpleSchedule(Long scheduleId) {
    SimpleSchedule simpleSchedule = simpleScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
    simpleScheduleRepository.delete(simpleSchedule);
  }

  private RepeatSchedule buildRepeatScheduleForAdd(ScheduleRequest request, Team team,
      ScheduleCategory category) {
    String month = request.getStartDt().getMonth().name();
    int day = request.getStartDt().getDayOfMonth();
    String dayOfWeek = String.valueOf(request.getStartDt().getDayOfWeek());

    RepeatSchedule newRepeatSchedule = RepeatSchedule.builder()
        .scheduleCategory(category)
        .team(team)
        .title(request.getTitle())
        .content(request.getContent())
        .place(request.getPlace())
        .startDt(request.getStartDt())
        .endDt(request.getEndDt())
        .repeatCycle(request.getRepeatCycle())
        .color(request.getColor())
        .build();

    setRepeatScheduleFieldsByCycle(newRepeatSchedule, month, day, dayOfWeek,
        request.getRepeatCycle()
    );
    return newRepeatSchedule;
  }

  private List<TeamParticipantsSchedule> buildTeamParticipantsSchedulesBySimpleSchedule(
      SimpleSchedule simpleSchedule,
      List<Long> teamParticipantsIds) {
    if (hasDuplicateTeamParticipantsIds(teamParticipantsIds)) {
      throw new CustomException(TEAM_PARTICIPANTS_ID_DUPLICATE_EXCEPTION);
    }
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
    if (hasDuplicateTeamParticipantsIds(teamParticipantsIds)) {
      throw new CustomException(TEAM_PARTICIPANTS_ID_DUPLICATE_EXCEPTION);
    }
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
        .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND_EXCEPTION));
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

  private void setRepeatScheduleFieldsByCycle(RepeatSchedule repeatSchedule, String month, int day, String dayOfWeek, RepeatCycle repeatCycle) {
    switch (repeatCycle) {
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
  }

  private boolean hasDuplicateTeamParticipantsIds(List<Long> teamParticipantsIds) {
    Set<Long> uniqueIds = new HashSet<>();
    for (Long teamParticipantsId : teamParticipantsIds) {
      if (!uniqueIds.add(teamParticipantsId)) {
        return true;
      }
    }
    return false;
  }
}

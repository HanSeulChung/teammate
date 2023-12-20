package com.api.backend.schedule.service;

import static com.api.backend.global.exception.type.ErrorCode.NON_REPEATING_SCHEDULE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_ID_DUPLICATE_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.category.type.CategoryType;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.AllSchedulesMonthlyView;
import com.api.backend.schedule.data.dto.RepeatScheduleInfoEditRequest;
import com.api.backend.schedule.data.dto.ScheduleRequest;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  public SimpleSchedule addSimpleScheduleAndSave(ScheduleRequest scheduleRequest, Principal principal) {
    Team team = findTeamOrElseThrow(scheduleRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(scheduleRequest.getCategoryId());

    SimpleSchedule simpleSchedule = buildSimpleScheduleForAdd(scheduleRequest, team, category);
    List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesBySimpleSchedule(
        simpleSchedule, scheduleRequest.getTeamParticipantsIds()
    );
    simpleSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    simpleScheduleRepository.save(simpleSchedule);
    return simpleSchedule;
  }

  @Transactional
  public RepeatSchedule addRepeatScheduleAndSave(ScheduleRequest scheduleRequest, Principal principal) {
    Team team = findTeamOrElseThrow(scheduleRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(scheduleRequest.getCategoryId());

    List<Long> teamParticipantsIds = scheduleRequest.getTeamParticipantsIds();
    validateSameTeamOrElsThrow(teamParticipantsIds, team.getTeamId());
    RepeatSchedule repeatSchedule = buildRepeatScheduleForAdd(scheduleRequest, team, category);

    List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesByRepeatSchedule(
        repeatSchedule, scheduleRequest.getTeamParticipantsIds()
    );

    repeatSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    repeatScheduleRepository.save(repeatSchedule);
    return repeatSchedule;
  }

  @Transactional
  public SimpleSchedule editSimpleScheduleInfoAndSave(SimpleScheduleInfoEditRequest editRequest, Principal principal) {
    Team team = findTeamOrElseThrow(editRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(editRequest.getCategoryId());
    List<Long> teamParticipantsIds = editRequest.getTeamParticipantsIds();
    validateSameTeamOrElsThrow(teamParticipantsIds, team.getTeamId());

    SimpleSchedule updatedSimpleSchedule = findSimpleScheduleOrElseThrow(
        editRequest.getSimpleScheduleId()
    );

    updatedSimpleSchedule.setSimpleScheduleInfo(
        category,
        editRequest.getTitle(), editRequest.getContent(),
        editRequest.getStartDt(), editRequest.getEndDt(),
        editRequest.getPlace(), editRequest.getColor()
    );

    List<TeamParticipantsSchedule> originTeamParticipantsSchedules =
        teamParticipantsScheduleRepository.findAllBySimpleSchedule_SimpleScheduleId(
            updatedSimpleSchedule.getSimpleScheduleId());

    teamParticipantsScheduleRepository.deleteAll(originTeamParticipantsSchedules);

    List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesBySimpleSchedule(
        updatedSimpleSchedule, teamParticipantsIds);

    updatedSimpleSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
    return simpleScheduleRepository.save(updatedSimpleSchedule);
  }

  @Transactional
  public RepeatSchedule editRepeatScheduleInfoAndSave(RepeatScheduleInfoEditRequest editRequest, Principal principal) {
    Team team = findTeamOrElseThrow(editRequest.getTeamId());
    ScheduleCategory category = findScheduleCategoryOrElseThrow(editRequest.getCategoryId());
    List<Long> teamParticipantsIds = editRequest.getTeamParticipantsIds();
    validateSameTeamOrElsThrow(teamParticipantsIds, team.getTeamId());

    String month = editRequest.getStartDt().getMonth().name();
    int day = editRequest.getStartDt().getDayOfMonth();
    String dayOfWeek = String.valueOf(editRequest.getStartDt().getDayOfWeek());
    RepeatSchedule scheduleByOriginRepeatScheduleId = repeatScheduleRepository.findByOriginRepeatScheduleId(
        editRequest.getRepeatScheduleId());

    //모든 일정 선택시 update
    if (editRequest.getEditOption().equals(EditOption.ALL_SCHEDULES)) {

      if (scheduleByOriginRepeatScheduleId != null) {
        repeatScheduleRepository.delete(scheduleByOriginRepeatScheduleId);
      }

      RepeatSchedule updateRepeatSchedule = findRepeatScheduleOrElseThrow(
          editRequest.getRepeatScheduleId()
      );

      updateRepeatSchedule.setScheduleCategory(category);
      updateRepeatSchedule.setTitle(editRequest.getTitle());
      updateRepeatSchedule.setContent(editRequest.getContent());
      updateRepeatSchedule.setStartDt(editRequest.getStartDt());
      updateRepeatSchedule.setEndDt(editRequest.getEndDt());
      updateRepeatSchedule.setPlace(editRequest.getPlace());
      updateRepeatSchedule.setColor(editRequest.getColor());

      setRepeatScheduleFieldsByCycle(updateRepeatSchedule, month, day, dayOfWeek,
          editRequest.getRepeatCycle());

      List<TeamParticipantsSchedule> originTeamParticipantsSchedules =
          teamParticipantsScheduleRepository.findAllByRepeatSchedule_RepeatScheduleId(
              updateRepeatSchedule.getRepeatScheduleId()
          );

      teamParticipantsScheduleRepository.deleteAll(originTeamParticipantsSchedules);

      List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesByRepeatSchedule(
          updateRepeatSchedule, teamParticipantsIds);

      updateRepeatSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
      return repeatScheduleRepository.save(updateRepeatSchedule);

      //이 일정/이 일정 및 향후 일정 선택시 originRepeatId를 갖고 newRepeatSchedule Insert
    } else {
      RepeatSchedule updateRepeatSchedule = buildRepeatScheduleForEdit(editRequest, team, category);

      List<TeamParticipantsSchedule> originTeamParticipantsSchedules =
          teamParticipantsScheduleRepository.findAllByRepeatSchedule_RepeatScheduleId(
              updateRepeatSchedule.getRepeatScheduleId()
          );

      teamParticipantsScheduleRepository.deleteAll(originTeamParticipantsSchedules);

      List<TeamParticipantsSchedule> teamParticipantsSchedules = buildTeamParticipantsSchedulesByRepeatSchedule(
          updateRepeatSchedule, teamParticipantsIds);

      updateRepeatSchedule.setTeamParticipantsSchedules(teamParticipantsSchedules);
      updateRepeatSchedule.setOriginRepeatScheduleId(
          scheduleByOriginRepeatScheduleId.getOriginRepeatScheduleId());
      return repeatScheduleRepository.save(updateRepeatSchedule);
    }
  }


  @Transactional
  public void deleteSimpleSchedule(Long scheduleId, Principal principal) {
    SimpleSchedule simpleSchedule = simpleScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
    simpleScheduleRepository.delete(simpleSchedule);
  }

  @Transactional
  public void deleteRepeatSchedule(Long scheduleId, Principal principal) {
    RepeatSchedule repeatSchedule = repeatScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
    repeatScheduleRepository.delete(repeatSchedule);
  }

  public SimpleSchedule getSimpleScheduleDetailInfo(Long scheduleId, Long teamId, Principal principal) {
    findTeamOrElseThrow(teamId);
    return simpleScheduleRepository.findSimpleScheduleBySimpleScheduleIdAndTeam_TeamId(scheduleId,
        teamId);
  }

  public RepeatSchedule getRepeatScheduleDetailInfo(Long scheduleId, Long teamId, Principal principal) {
    findTeamOrElseThrow(teamId);
    return repeatScheduleRepository.findRepeatScheduleByRepeatScheduleIdAndTeam_TeamId(scheduleId,
        teamId);
  }


  public Page<AllSchedulesMonthlyView> getCategoryTypeMonthlySchedules(Long teamId, CategoryType categoryType, Pageable pageable, Principal principal) {
    Page<RepeatSchedule> repeatSchedules = repeatScheduleRepository.findAllByScheduleCategory_CategoryTypeAndTeam_TeamId(
        categoryType, teamId, pageable);
    Page<SimpleSchedule> simpleSchedules = simpleScheduleRepository.findAllByScheduleCategory_CategoryTypeAndTeam_TeamId(
        categoryType, teamId, pageable);

    List<AllSchedulesMonthlyView> allSchedulesList = Stream.concat(
        repeatSchedules.getContent().stream().map(AllSchedulesMonthlyView::from),
        simpleSchedules.getContent().stream().map(AllSchedulesMonthlyView::from)
    ).collect(Collectors.toList());

    return new PageImpl<>(allSchedulesList, pageable,
        repeatSchedules.getTotalElements() + simpleSchedules.getTotalElements()
    );
  }

  public Page<AllSchedulesMonthlyView> getAllMonthlySchedules(Long teamId, Pageable pageable, Principal principal) {
    Page<RepeatSchedule> repeatSchedules = repeatScheduleRepository.findAllByTeam_TeamId(
         teamId, pageable);
    Page<SimpleSchedule> simpleSchedules = simpleScheduleRepository.findAllByTeam_TeamId(
         teamId, pageable);

    List<AllSchedulesMonthlyView> allSchedulesList = Stream.concat(
        repeatSchedules.getContent().stream().map(AllSchedulesMonthlyView::from),
        simpleSchedules.getContent().stream().map(AllSchedulesMonthlyView::from)
    ).collect(Collectors.toList());

    return new PageImpl<>(allSchedulesList, pageable,
        repeatSchedules.getTotalElements() + simpleSchedules.getTotalElements()
    );
  }

  private SimpleSchedule findSimpleScheduleOrElseThrow(Long simpleScheduleId) {
    return simpleScheduleRepository.findById(simpleScheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
  }

  private RepeatSchedule findRepeatScheduleOrElseThrow(Long repeatScheduleId) {
    return repeatScheduleRepository.findById(repeatScheduleId)
        .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND_EXCEPTION));
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

  private SimpleSchedule buildSimpleScheduleForAdd(ScheduleRequest request, Team team,
      ScheduleCategory category) {
    return SimpleSchedule.builder()
        .team(team)
        .scheduleCategory(category)
        .title(request.getTitle())
        .content(request.getContent())
        .startDt(request.getStartDt())
        .endDt(request.getEndDt())
        .teamParticipantsSchedules(new ArrayList<>())
        .place(request.getPlace())
        .build();
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

  private RepeatSchedule buildRepeatScheduleForEdit(RepeatScheduleInfoEditRequest editRequest,
      Team team,
      ScheduleCategory category) {
    String month = editRequest.getStartDt().getMonth().name();
    int day = editRequest.getStartDt().getDayOfMonth();
    String dayOfWeek = String.valueOf(editRequest.getStartDt().getDayOfWeek());

    RepeatSchedule newRepeatSchedule = RepeatSchedule.builder()
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

    setRepeatScheduleFieldsByCycle(newRepeatSchedule, month, day, dayOfWeek,
        editRequest.getRepeatCycle()
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

  private void setRepeatScheduleFieldsByCycle(RepeatSchedule repeatSchedule, String month, int day,
      String dayOfWeek, RepeatCycle repeatCycle) {
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
        throw new CustomException(NON_REPEATING_SCHEDULE_EXCEPTION);
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

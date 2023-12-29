package com.api.backend.category.service;

import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_CREATOR_EXISTS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_CREATOR_NOT_MATCH_TEAM_PARTICIPANTS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;

import com.api.backend.category.data.dto.ScheduleCategoryDeleteRequest;
import com.api.backend.category.data.dto.ScheduleCategoryEditRequest;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;
import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.category.type.CategoryType;
import com.api.backend.global.exception.CustomException;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import com.api.backend.schedule.data.repository.RepeatScheduleRepository;
import com.api.backend.schedule.data.repository.SimpleScheduleRepository;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import com.api.backend.team.data.type.TeamRole;
import com.api.backend.team.service.TeamParticipantsService;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class ScheduleCategoryService {

  private final ScheduleCategoryRepository scheduleCategoryRepository;
  private final TeamRepository teamRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private final TeamParticipantsService teamParticipantsService;
  private final SimpleScheduleRepository simpleScheduleRepository;
  private final RepeatScheduleRepository repeatScheduleRepository;

  @Transactional
  public ScheduleCategory add(ScheduleCategoryRequest scheduleCategoryRequest,
      Long memberId) {
    Team team = findTeamOrElseThrow(scheduleCategoryRequest.getTeamId());

    teamParticipantsService.getTeamParticipants(scheduleCategoryRequest.getTeamId(), memberId);
    ScheduleCategory scheduleCategory = ScheduleCategory.builder()
        .team(team)
        .categoryName(scheduleCategoryRequest.getCategoryName())
        .categoryType(scheduleCategoryRequest.getCategoryType())
        .color(scheduleCategoryRequest.getColor())
        .createParticipantId(scheduleCategoryRequest.getCreateTeamParticipantId())
        .build();
    scheduleCategoryRepository.save(scheduleCategory);
    log.info("일정 카테고리가 성공적으로 추가되었습니다.");
    return scheduleCategory;
  }


  public Page<ScheduleCategory> searchByCategoryType(CategoryType categoryType,
      Pageable pageable, Long teamId, Long memberId) {
    teamParticipantsService.getTeamParticipants(teamId, memberId);
    Page<ScheduleCategory> categoryPagesByCategoryType = scheduleCategoryRepository.findAllByCategoryTypeAndTeam_TeamId(
        categoryType, pageable,
        teamId);
    log.info("카테고리 유형별 조회에 성공하였습니다.");
    return categoryPagesByCategoryType;
  }

  @Transactional
  public ScheduleCategory edit(ScheduleCategoryEditRequest scheduleCategoryEditRequest,
      Long memberId) {
    findTeamOrElseThrow(scheduleCategoryEditRequest.getTeamId());
    teamParticipantsService.getTeamParticipant(scheduleCategoryEditRequest.getTeamId(), memberId);
    ScheduleCategory scheduleCategory = findCategoryOrElseThrow(
        scheduleCategoryEditRequest.getCategoryId());
    scheduleCategory.editScheduleCategory(scheduleCategoryEditRequest);
    ScheduleCategory editCategory = scheduleCategoryRepository.save(scheduleCategory);
    log.info("일정 카테고리 수정에 성공하였습니다.");
    return editCategory;
  }

  @Transactional
  public void delete(ScheduleCategoryDeleteRequest deleteRequest, Long memberId) {
    ScheduleCategory category = findCategoryOrElseThrow(deleteRequest.getCategoryId());
    TeamParticipants teamParticipants = teamParticipantsService.getTeamParticipant(
        deleteRequest.getTeamId(), memberId);

    if (teamParticipants.getTeamRole() == TeamRole.LEADER
        && category.getCreateParticipantId() != teamParticipants.getTeamParticipantsId()
    ) {
      if (teamParticipantsRepository.existsByTeamParticipantsId(
          category.getCreateParticipantId())) {
        throw new CustomException(SCHEDULE_CATEGORY_CREATOR_EXISTS_EXCEPTION);
      }
    } else {
      if (deleteRequest.getParticipantId() != teamParticipants.getTeamParticipantsId()) {
        throw new CustomException(SCHEDULE_CATEGORY_CREATOR_NOT_MATCH_TEAM_PARTICIPANTS_EXCEPTION);
      }
    }

    if (!deleteRequest.isMoved()) {
      scheduleCategoryRepository.delete(category);
      log.info("일정 카테고리가 성공적으로 삭제되었습니다.");
    } else {
      if (deleteRequest.getNewCategoryId() != null ) {

        ScheduleCategory newCategory = findCategoryOrElseThrow(deleteRequest.getNewCategoryId());
        List<RepeatSchedule> repeatSchedules = category.getRepeatSchedules();
        List<SimpleSchedule> simpleSchedules = category.getSimpleSchedule();

        for (RepeatSchedule repeatSchedule : repeatSchedules) {
          repeatSchedule.setScheduleCategory(newCategory);
          repeatScheduleRepository.save(repeatSchedule);
        }
        log.info("해당 카테고리에 속한 반복 일정들의 카테고리가 성공적으로 변경되었습니다.");
        for (SimpleSchedule simpleSchedule : simpleSchedules) {
          simpleSchedule.setScheduleCategory(newCategory);
          simpleScheduleRepository.save(simpleSchedule);
        }
        log.info("해당 카테고리에 속한 단순 일정들의 카테고리가 성공적으로 변경되었습니다.");
        scheduleCategoryRepository.delete(category);
        log.info("일정 카테고리가 성공적으로 삭제되었습니다.");
      }
    }

  }

  private Team findTeamOrElseThrow(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND_EXCEPTION));
  }

  private ScheduleCategory findCategoryOrElseThrow(Long scheduleCategoryId) {
    return scheduleCategoryRepository.findById(scheduleCategoryId)
        .orElseThrow(() -> new CustomException(SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION));
  }

}

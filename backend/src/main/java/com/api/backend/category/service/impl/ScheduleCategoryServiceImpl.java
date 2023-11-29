package com.api.backend.category.service.impl;

import static com.api.backend.global.exception.type.ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;

import com.api.backend.category.data.dto.ScheduleCategoryDto;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;
import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.category.service.ScheduleCategoryService;
import com.api.backend.global.exception.CustomException;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleCategoryServiceImpl implements ScheduleCategoryService {

  private final ScheduleCategoryRepository scheduleCategoryRepository;
  private final TeamRepository teamRepository;


  @Override
  public ScheduleCategoryDto add(ScheduleCategoryRequest scheduleCategoryRequest, Long teamId) {
    Team team = validateTeam(teamId);

    ScheduleCategory scheduleCategory = ScheduleCategory.builder()
        .scheduleCategoryId(scheduleCategoryRequest.getId())
        .team(team)
        .categoryName(scheduleCategoryRequest.getCategoryName())
        .categoryType(scheduleCategoryRequest.getCategoryType())
        .color(scheduleCategoryRequest.getColor())
        .build();
    scheduleCategoryRepository.save(scheduleCategory);
    return ScheduleCategoryDto.of(scheduleCategory);
  }

  public Team validateTeam(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND_EXCEPTION));
  }

  //TODO: 카테고리 조회, 수정, 삭제시 사용할 메소드
  public ScheduleCategory validateScheduleCategory(Long scheduleCategoryId) {
    return scheduleCategoryRepository.findById(scheduleCategoryId)
        .orElseThrow(() -> new CustomException(SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION));
  }

}

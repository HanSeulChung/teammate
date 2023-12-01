package com.api.backend.schedule.service.impl;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.ScheduleDto;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;
import com.api.backend.schedule.data.repository.ScheduleRepository;
import com.api.backend.schedule.service.ScheduleService;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final TeamRepository teamRepository;
  private final ScheduleCategoryRepository categoryRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;

  @Override
  public ScheduleDto add(ScheduleRequest scheduleRequest, Long teamId, Long categoryId) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
    ScheduleCategory category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION));

    Schedule schedule = Schedule.builder()
        .scheduleId(scheduleRequest.getScheduleId())
        .team(team)
        .scheduleCategory(category)
        .title(scheduleRequest.getTitle())
        .content(scheduleRequest.getContent())
        .startDt(scheduleRequest.getStartDt())
        .endDt(scheduleRequest.getEndDt())
        .repeatCycle(scheduleRequest.getRepeatCycle())
        .isRepeat(scheduleRequest.isRepeat())
        .build();
    return null;
  }

  @Override
  public void delete(Long scheduleId) {

  }
}

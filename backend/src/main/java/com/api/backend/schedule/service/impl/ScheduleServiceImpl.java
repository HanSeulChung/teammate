package com.api.backend.schedule.service.impl;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;
import com.api.backend.schedule.data.repository.ScheduleRepository;
import com.api.backend.schedule.service.ScheduleService;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
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
  private final TeamParticipantsRepository teamParticipantsRepository;

  @Override
  @Transactional
  public Schedule add(ScheduleRequest scheduleRequest) {
    Team team = teamRepository.findById(scheduleRequest.getTeamId())
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND_EXCEPTION));
    ScheduleCategory category = categoryRepository.findById(scheduleRequest.getCategoryId())
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
        .teamParticipants(scheduleRequest.getTeamParticipants())
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
  public void delete(Long scheduleId) {

  }
}

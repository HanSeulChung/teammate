package com.api.backend.schedule.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.data.repository.ScheduleCategoryRepository;
import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;
import com.api.backend.schedule.data.repository.ScheduleRepository;
import com.api.backend.schedule.service.ScheduleService;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.repository.TeamRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

  @Mock
  private TeamRepository teamRepository;

  @Mock
  private ScheduleCategoryRepository categoryRepository;

  @Mock
  private ScheduleRepository scheduleRepository;

  @InjectMocks
  private ScheduleServiceImpl scheduleService;

  @Test
  @DisplayName("스케줄 추가 성공")
  public void testAddSchedule() {
    // Mocking data
    Long teamId = 1L;
    Long categoryId = 1L;
    ScheduleRequest scheduleRequest = ScheduleRequest.builder()
        .scheduleId(1L)
        .categoryId(categoryId)
        .teamId(teamId)
        .title("김하나 휴가")
        .content("휴가")
        .repeatCycle(LocalDateTime.now())
        .isRepeat(false)
        .place("집")
        .startDt(LocalDateTime.now())
        .endDt(LocalDateTime.now())
        .color("PINK")
        .build();


    Team mockTeam = Team.builder()
        .teamId(teamId)
        .build();
    ScheduleCategory scheduleCategory = ScheduleCategory.builder()
        .scheduleCategoryId(categoryId)
        .build();

    when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(scheduleCategory));
    when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Schedule result = scheduleService.add(scheduleRequest);

    assertEquals(scheduleRequest.getTitle(), result.getTitle());
    assertEquals(scheduleRequest.getContent(), result.getContent());
    assertEquals(scheduleRequest.getStartDt(), result.getStartDt());
    assertEquals(scheduleRequest.getEndDt(), result.getEndDt());
    assertEquals(scheduleRequest.getRepeatCycle(), result.getRepeatCycle());
    assertEquals(scheduleRequest.isRepeat(), result.isRepeat());
    assertEquals(mockTeam, result.getTeam());
    assertEquals(scheduleCategory, result.getScheduleCategory());
  }

  @Test
  @DisplayName("일정 조회 - 성공")
  public void searchScheduleSuccess() {
    // given
    Pageable pageable = PageRequest.of(0, 10);

    List<Schedule> mockScheduleList = new ArrayList<>();

    ScheduleCategory scheduleCategory = ScheduleCategory.builder()
        .scheduleCategoryId(1L)
        .build();
    Team team = Team.builder()
        .teamId(1L)
        .build();
    LocalDateTime startDt = LocalDateTime.now();
    Schedule schedule1 = Schedule.builder()
        .scheduleId(1L)
        .scheduleCategory(scheduleCategory)
        .title("Test1")
        .content("Test1")
        .startDt(startDt)
        .endDt(startDt.plusWeeks(1))
        .team(team)
        .teamParticipants(team.getTeamParticipants())
        .color("SKYBLUE")
        .isRepeat(false)
        .repeatCycle(null)
        .build();
    Schedule schedule2 = Schedule.builder()
        .scheduleId(2L)
        .scheduleCategory(scheduleCategory)
        .title("Test2")
        .content("Test2")
        .startDt(startDt)
        .endDt(startDt.plusWeeks(1))
        .team(team)
        .teamParticipants(team.getTeamParticipants())
        .color("SKYBLUE")
        .isRepeat(false)
        .repeatCycle(null)
        .build();
    mockScheduleList.add(schedule1);
    mockScheduleList.add(schedule2);

    Page<Schedule> mockPage = new PageImpl<>(mockScheduleList, pageable, mockScheduleList.size());

    when(scheduleRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

    // when
    Page<Schedule> resultPage = scheduleService.searchSchedule(pageable);
    List<Schedule> resultScheduleList = resultPage.getContent();

    // then
    assertEquals(mockScheduleList.size(), resultScheduleList.size());
  }
}
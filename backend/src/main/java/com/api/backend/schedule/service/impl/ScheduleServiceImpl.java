package com.api.backend.schedule.service.impl;

import com.api.backend.schedule.data.repository.ScheduleRepository;
import com.api.backend.schedule.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
  private final ScheduleRepository scheduleRepository;

  @Override
  public void delete(Long scheduleId) {

  }
}

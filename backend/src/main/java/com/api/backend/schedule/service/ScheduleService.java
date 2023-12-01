package com.api.backend.schedule.service;

import com.api.backend.schedule.data.dto.ScheduleDto;
import com.api.backend.schedule.data.dto.ScheduleRequest;

public interface ScheduleService {
  ScheduleDto add(ScheduleRequest scheduleRequest, Long teamId, Long categoryId);
  void delete(Long scheduleId);
}

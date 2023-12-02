package com.api.backend.schedule.service;

import com.api.backend.schedule.data.dto.ScheduleDto;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;

public interface ScheduleService {
  Schedule add(ScheduleRequest scheduleRequest);
  void delete(Long scheduleId);
}

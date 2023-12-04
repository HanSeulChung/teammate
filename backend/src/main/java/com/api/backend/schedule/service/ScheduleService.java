package com.api.backend.schedule.service;

import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
  Schedule add(ScheduleRequest scheduleRequest);

  Page<Schedule> searchSchedule(Pageable pageable);
  void delete(Long scheduleId);
}

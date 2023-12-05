package com.api.backend.schedule.service;

import com.api.backend.schedule.data.dto.ScheduleEditRequest;
import com.api.backend.schedule.data.dto.ScheduleRequest;
import com.api.backend.schedule.data.enetity.Schedule;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
  Page<Schedule> addSchedules(ScheduleRequest scheduleRequest);

  Page<Schedule> searchSchedule(Pageable pageable);

  Schedule editSchedule(ScheduleEditRequest scheduleEditRequest);

  void deleteSchedule(Long scheduleId);
}

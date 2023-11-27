package com.api.backend.schedule.data.repository;

import com.api.backend.schedule.data.enetity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

}

package com.api.backend.schedule.data.repository;

import com.api.backend.schedule.data.enetity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

}

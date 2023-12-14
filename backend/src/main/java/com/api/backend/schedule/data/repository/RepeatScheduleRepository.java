package com.api.backend.schedule.data.repository;

import com.api.backend.schedule.data.entity.RepeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {

  RepeatSchedule findRepeatScheduleByRepeatScheduleIdAndTeam_TeamId(Long repeatScheduleId, Long teamId);

}

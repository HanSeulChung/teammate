package com.api.backend.schedule.data.repository;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleScheduleRepository extends JpaRepository<SimpleSchedule, Long> {

  SimpleSchedule findSimpleScheduleBySimpleScheduleIdAndTeam_TeamId(Long scheduleId, Long teamId);

  Page<SimpleSchedule> findByTeam_TeamIdAndStartDtBetweenAndScheduleCategory_CategoryType(Long teamId, LocalDateTime startDt,
      LocalDateTime endDt, CategoryType type, Pageable pageable);


  Page<SimpleSchedule> findByTeam_TeamIdAndStartDtBetween(Long teamId, LocalDateTime startDt,
      LocalDateTime endDt, Pageable pageable);
}

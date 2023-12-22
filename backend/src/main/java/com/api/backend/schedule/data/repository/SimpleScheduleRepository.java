package com.api.backend.schedule.data.repository;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleScheduleRepository extends JpaRepository<SimpleSchedule, Long> {

  SimpleSchedule findSimpleScheduleBySimpleScheduleIdAndTeam_TeamId(Long scheduleId, Long teamId);

  Page<SimpleSchedule> findAllByTeam_TeamId(Long teamId, Pageable pageable);

  Page<SimpleSchedule> findAllByScheduleCategory_CategoryTypeAndTeam_TeamId(CategoryType categoryType, Long teamId, Pageable pageable);
}

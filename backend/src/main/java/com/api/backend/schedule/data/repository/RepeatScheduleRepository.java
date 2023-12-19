package com.api.backend.schedule.data.repository;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.RepeatSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {

  RepeatSchedule findByOriginRepeatScheduleId(Long originRepeatScheduleId);

  Page<RepeatSchedule> findAllByTeam_TeamId(Long teamId, Pageable pageable);

  Page<RepeatSchedule> findAllByScheduleCategory_CategoryTypeAndTeam_TeamId(CategoryType categoryType, Long teamId, Pageable pageable);
}

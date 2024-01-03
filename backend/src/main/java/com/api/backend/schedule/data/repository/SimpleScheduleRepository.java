package com.api.backend.schedule.data.repository;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.SimpleSchedule;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SimpleScheduleRepository extends JpaRepository<SimpleSchedule, Long> {

  SimpleSchedule findSimpleScheduleBySimpleScheduleIdAndTeam_TeamId(Long scheduleId, Long teamId);

  List<SimpleSchedule> findAllByTeam_TeamId(Long teamId);

  List<SimpleSchedule> findAllByScheduleCategory_CategoryTypeAndTeam_TeamId(CategoryType categoryType, Long teamId);

  List<SimpleSchedule> findAllByScheduleCategory_ScheduleCategoryIdAndTeam_TeamId(Long categoryId, Long teamId);

  @Transactional
  @Modifying
  @Query(
      value = "delete from simple_schedule s where s.simple_schedule_id in :ids",
      nativeQuery = true
  )
  void deleteAllByIdInQuery(@Param("ids") List<Long> simpleIds);
}

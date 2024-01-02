package com.api.backend.schedule.data.repository;

import com.api.backend.category.type.CategoryType;
import com.api.backend.schedule.data.entity.RepeatSchedule;
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
public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {

  RepeatSchedule findByOriginRepeatScheduleId(Long originRepeatScheduleId);
  RepeatSchedule findRepeatScheduleByRepeatScheduleIdAndTeam_TeamId(Long scheduleId, Long teamId);

  List<RepeatSchedule> findAllByTeam_TeamId(Long teamId);

  List<RepeatSchedule> findAllByScheduleCategory_CategoryTypeAndTeam_TeamId(CategoryType categoryType, Long teamId);

  List<RepeatSchedule> findAllByScheduleCategory_ScheduleCategoryIdAndTeam_TeamId(Long categoryId, Long teamId);

  @Transactional
  @Modifying
  @Query(
      value = "delete from repeat_schedule r where r.repeat_schedule_id in :ids",
      nativeQuery = true
  )
  void deleteAllByIdsInQuery(@Param("ids") List<Long> repeatScheduleIds);
}

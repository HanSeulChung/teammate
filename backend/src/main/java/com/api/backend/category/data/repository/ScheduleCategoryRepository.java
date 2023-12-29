package com.api.backend.category.data.repository;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
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
public interface ScheduleCategoryRepository extends JpaRepository<ScheduleCategory, Long> {

  Page<ScheduleCategory> findAllByCategoryTypeAndTeam_TeamId(CategoryType categoryType, Pageable pageable, Long teamId);

  @Transactional
  @Modifying
  @Query(
      value = "delete from schedule_category c where c.schedule_category_id in :ids",
      nativeQuery = true
  )
  void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}

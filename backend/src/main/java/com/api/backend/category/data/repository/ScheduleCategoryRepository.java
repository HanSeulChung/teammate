package com.api.backend.category.data.repository;

import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleCategoryRepository extends JpaRepository<ScheduleCategory, Long> {
  List<ScheduleCategory> findAllByCategoryType(CategoryType categoryType);
}

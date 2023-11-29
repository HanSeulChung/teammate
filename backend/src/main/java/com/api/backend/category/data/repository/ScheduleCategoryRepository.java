package com.api.backend.category.data.repository;

import com.api.backend.category.data.entity.ScheduleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleCategoryRepository extends JpaRepository<ScheduleCategory, Long> {

}

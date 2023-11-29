package com.api.backend.category.service;

import com.api.backend.category.data.dto.ScheduleCategoryDto;
import com.api.backend.category.data.dto.ScheduleCategoryEditRequest;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;
import com.api.backend.category.data.entity.ScheduleCategory;
import com.api.backend.category.type.CategoryType;
import java.util.List;

public interface ScheduleCategoryService {

  ScheduleCategoryDto add(ScheduleCategoryRequest scheduleCategoryRequest, Long teamId);

  List<ScheduleCategoryDto> searchByCategoryType(CategoryType categoryType);

  ScheduleCategoryDto edit(ScheduleCategoryEditRequest scheduleCategoryEditRequest, Long teamId);
}

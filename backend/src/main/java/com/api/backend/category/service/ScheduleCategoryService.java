package com.api.backend.category.service;

import com.api.backend.category.data.dto.ScheduleCategoryDto;
import com.api.backend.category.data.dto.ScheduleCategoryEditRequest;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;
import com.api.backend.category.data.dto.ScheduleCategoryResponse;
import com.api.backend.category.type.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleCategoryService {

  ScheduleCategoryDto add(ScheduleCategoryRequest scheduleCategoryRequest, Long teamId);

  Page<ScheduleCategoryResponse> searchByCategoryType(CategoryType categoryType, Pageable pageable);

  ScheduleCategoryDto edit(ScheduleCategoryEditRequest scheduleCategoryEditRequest, Long teamId);

  void delete(Long categoryId);
}
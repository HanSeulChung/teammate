package com.api.backend.category.service;

import com.api.backend.category.data.dto.ScheduleCategoryDto;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;

public interface ScheduleCategoryService {

  ScheduleCategoryDto add(ScheduleCategoryRequest scheduleCategoryRequest, Long teamId);
}

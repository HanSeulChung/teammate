package com.api.backend.schedule.controller;

import com.api.backend.category.service.ScheduleCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedules")
public class ScheduleController {
  private final ScheduleCategoryService scheduleCategoryService;

}

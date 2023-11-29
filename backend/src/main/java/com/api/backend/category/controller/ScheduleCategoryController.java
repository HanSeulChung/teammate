package com.api.backend.category.controller;

import com.api.backend.category.data.dto.ScheduleCategoryDto;
import com.api.backend.category.data.dto.ScheduleCategoryEditRequest;
import com.api.backend.category.data.dto.ScheduleCategoryEditResponse;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;
import com.api.backend.category.data.dto.ScheduleCategoryResponse;
import com.api.backend.category.service.ScheduleCategoryService;
import com.api.backend.category.type.CategoryType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class ScheduleCategoryController {

  private final ScheduleCategoryService scheduleCategoryService;

  @PostMapping
  public ResponseEntity<ScheduleCategoryResponse> categoryAdd(
      @RequestBody ScheduleCategoryRequest request, @RequestParam Long teamId) {
    ScheduleCategoryDto scheduleCategoryDto = scheduleCategoryService.add(request, teamId);
    return ResponseEntity.ok(ScheduleCategoryResponse.toResponse(scheduleCategoryDto));
  }

  @GetMapping
  public ResponseEntity<List<ScheduleCategoryDto>> searchByCategoryType(
      @RequestParam String categoryType) {
    return ResponseEntity.ok(scheduleCategoryService.searchByCategoryType(
        CategoryType.valueOf(categoryType.toUpperCase())));
  }

  @PutMapping
  public ResponseEntity<ScheduleCategoryEditResponse> editCategory(
      @RequestBody ScheduleCategoryEditRequest request, @RequestParam Long teamId) {
    ScheduleCategoryDto scheduleCategoryDto = scheduleCategoryService.edit(request, teamId);
    return ResponseEntity.ok(ScheduleCategoryEditResponse.toResponse(scheduleCategoryDto));
  }

  @DeleteMapping
  public ResponseEntity<String> deleteCategory(@RequestParam Long categoryId) {
    scheduleCategoryService.delete(categoryId);
    return ResponseEntity.ok("해당 일정 카테고리가 정상적으로 삭제되었습니다.");
  }
}

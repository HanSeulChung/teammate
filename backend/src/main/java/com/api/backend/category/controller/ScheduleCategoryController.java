package com.api.backend.category.controller;

import com.api.backend.category.data.dto.ScheduleCategoryDeleteRequest;
import com.api.backend.category.data.dto.ScheduleCategoryEditRequest;
import com.api.backend.category.data.dto.ScheduleCategoryEditResponse;
import com.api.backend.category.data.dto.ScheduleCategoryRequest;
import com.api.backend.category.data.dto.ScheduleCategoryResponse;
import com.api.backend.category.service.ScheduleCategoryService;
import com.api.backend.category.type.CategoryType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "일정 카테고리")
@RequiredArgsConstructor
@RequestMapping("/category")
public class ScheduleCategoryController {

  private final ScheduleCategoryService scheduleCategoryService;

  @ApiOperation(value = "일정 카테고리 생성")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정 카테고리가 성공적으로 추가되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 카테고리 추가에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None")
      })

  @PostMapping
  public ResponseEntity<ScheduleCategoryResponse> categoryAdd(
      @RequestBody ScheduleCategoryRequest request, @ApiIgnore Principal principal
  ) {
    ScheduleCategoryResponse response = ScheduleCategoryResponse.from(
        scheduleCategoryService.add(request, Long.valueOf(principal.getName()))
    );
    return ResponseEntity.ok(response);
  }

  @ApiOperation(value = "카테고리 유형 검색")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "카테고리 유형 검색에 성공하였습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "카테고리 유형 검색 조회에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None"),
          @ApiImplicitParam(
              name = "teamId"
              , value = "팀 id"
              , required = true
              , dataType = "Long"
              , paramType = "path"
              , defaultValue = "None"
              , example = "1")
      })

  @GetMapping("/{categoryType}")
  public ResponseEntity<Page<ScheduleCategoryResponse>> searchByCategoryType(
      @PathVariable String categoryType, Pageable pageable, @RequestParam Long teamId, @ApiIgnore Principal principal
  ) {
    CategoryType enumCategoryType = CategoryType.valueOf(categoryType.toUpperCase());
    Page<ScheduleCategoryResponse> responses = ScheduleCategoryResponse.from(
        scheduleCategoryService.searchByCategoryType(enumCategoryType, pageable, teamId, Long.valueOf(principal.getName()))
    );
    return ResponseEntity.ok(responses);
  }

  @ApiOperation(value = "일정 카테고리 수정")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정 카테고리가 성공적으로 수정되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 카테고리 수정에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None")
      })

  @PutMapping
  public ResponseEntity<ScheduleCategoryEditResponse> editCategory(
      @RequestBody ScheduleCategoryEditRequest request, @ApiIgnore Principal principal
  ) {
    ScheduleCategoryEditResponse editResponse = ScheduleCategoryEditResponse.from(
        scheduleCategoryService.edit(request, Long.valueOf(principal.getName()))
    );
    return ResponseEntity.ok(editResponse);
  }

  @ApiOperation(value = "일정 카테고리 삭제")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "일정 카테고리가 성공적으로 삭제되었습니다."),
      @ApiResponse(code = 404, message = "페이지를 찾을 수 없습니다."),
      @ApiResponse(code = 400, message = "일정 카테고리 삭제에 실패했습니다.")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "access token"
              , value = "jwt access token"
              , required = true
              , dataType = "String"
              , paramType = "header"
              , defaultValue = "None")
      })

  @DeleteMapping
  public ResponseEntity<String> deleteCategory(
      @RequestBody ScheduleCategoryDeleteRequest deleteRequest, @ApiIgnore Principal principal) {
    scheduleCategoryService.delete(deleteRequest, Long.valueOf(principal.getName()));
    return ResponseEntity.ok("해당 일정 카테고리가 정상적으로 삭제되었습니다.");
  }
}

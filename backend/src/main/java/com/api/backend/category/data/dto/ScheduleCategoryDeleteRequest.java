package com.api.backend.category.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class ScheduleCategoryDeleteRequest {
  private Long categoryId;
  private Long teamId;
  private Long participantId;
  private boolean isMoved;
}

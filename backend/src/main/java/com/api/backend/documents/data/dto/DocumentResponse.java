package com.api.backend.documents.data.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

  private String id;

  private String documentIdx;

  private String title;

  private String content;

  private Long writerId;
  private Long modifierId;

  private Long teamId;

  private LocalDateTime createdDt;
  private LocalDateTime updatedDt;

}
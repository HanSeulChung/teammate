package com.api.backend.documents.data.dto;

import com.api.backend.documents.data.entity.Documents;
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

  public static DocumentResponse from(Documents documents) {
    return DocumentResponse.builder()
        .id(documents.getId())
        .documentIdx(documents.getDocumentIdx())
        .title(documents.getTitle())
        .content(documents.getContent())
        .writerId(documents.getWriterId())
        .modifierId(documents.getModifierId())
        .teamId(documents.getTeamId())
        .createdDt(documents.getCreatedDt())
        .updatedDt(documents.getUpdatedDt())
        .build();
  }
}
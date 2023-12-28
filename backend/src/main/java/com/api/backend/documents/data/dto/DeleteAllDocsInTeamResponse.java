package com.api.backend.documents.data.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAllDocsInTeamResponse {
  private Long teamId;
  private String teamName;
  private Long totalDocumentCount;
  private LocalDateTime deletedDt;
}

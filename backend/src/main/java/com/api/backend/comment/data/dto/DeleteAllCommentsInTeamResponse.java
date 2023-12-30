package com.api.backend.comment.data.dto;

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
public class DeleteAllCommentsInTeamResponse {
  private Long teamId;
  private String teamName;
  private Long totalCommentCount;
  private LocalDateTime deletedDt;
}

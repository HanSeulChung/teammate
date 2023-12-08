package com.api.backend.comment.data.dto;

import com.api.backend.comment.data.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

  private String id;
  private Long writerId;
  private String content;
  private Long teamId;
  private LocalDateTime createdDt;
  private LocalDateTime updatedDt;

  public static CommentResponse from(Comment comment) {
    return CommentResponse.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .writerId(comment.getWriterId())
        .teamId(comment.getTeamId())
        .createdDt(comment.getCreatedDt())
        .updatedDt(comment.getUpdatedDt())
        .build();
  }
}

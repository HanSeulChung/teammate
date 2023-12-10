package com.api.backend.documents.data.dto;

import com.api.backend.comment.data.entity.Comment;
import com.api.backend.documents.data.entity.Documents;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  private List<String> commentsId;

  private LocalDateTime createdDt;
  private LocalDateTime updatedDt;

  public static DocumentResponse from(Documents documents) {

    DocumentResponse documentResponse = DocumentResponse.builder()
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

    if (documents.getCommentIds() == null) {
        documentResponse.setCommentList(new ArrayList<>());
        return documentResponse;
    }

    List<String> commentIds = documents.getCommentIds().stream()
        .map(Comment::getId)
        .collect(Collectors.toList());
    documentResponse.setCommentList(commentIds);

    return documentResponse;
  }

  public void setCommentList(List<String> commentList) {
    if (commentsId == null) {
      commentsId = new ArrayList<>();
    }

    for (String commentId : commentList) {
      commentsId.add(commentId);
    }
  }
}
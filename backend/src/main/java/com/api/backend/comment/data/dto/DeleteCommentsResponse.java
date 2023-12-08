package com.api.backend.comment.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteCommentsResponse {

  private String id;

  private Long writerId;

  private String content;

  private String message;
}

package com.api.backend.comment.controller;

import com.api.backend.comment.data.dto.CommentRequest;
import com.api.backend.comment.data.dto.CommentResponse;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.service.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/team/{teamId}/documents/{documentId}/comments")
  public ResponseEntity<CommentResponse> createComments(
      @PathVariable Long teamId, @PathVariable String documentId,
      @RequestBody @Valid CommentRequest request
  ) {
    Comment comment = commentService.createComment(teamId, documentId, request);

    return ResponseEntity.ok(CommentResponse.from(comment));
  }
}

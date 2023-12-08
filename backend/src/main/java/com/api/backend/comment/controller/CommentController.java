package com.api.backend.comment.controller;

import com.api.backend.comment.data.dto.CommentEditRequest;
import com.api.backend.comment.data.dto.CommentInitRequest;
import com.api.backend.comment.data.dto.CommentResponse;
import com.api.backend.comment.data.dto.DeleteCommentsResponse;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.service.CommentService;
import java.security.Principal;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/team/{teamId}/documents/{documentId}/comments")
  public ResponseEntity<CommentResponse> createComments(
      @PathVariable Long teamId, @PathVariable String documentId,
      @RequestBody @Valid CommentInitRequest request
  ) {
    Comment comment = commentService.createComment(teamId, documentId, request);

    return ResponseEntity.ok(CommentResponse.from(comment));
  }

  @GetMapping("/team/{teamId}/documents/{documentId}/comments")
  public ResponseEntity<Page<CommentResponse>> getCommentsList(
      @PathVariable Long teamId, @PathVariable String documentId,
      Pageable pageable) {
    Page<Comment> commentPage = commentService.getCommentList(teamId, documentId, pageable);
    Page<CommentResponse> commentDtoPage = commentPage.map(
        comment -> CommentResponse.from(comment)
    );

    return ResponseEntity.ok(commentDtoPage);
  }

  @PutMapping("/team/{teamId}/documents/{documentId}/comments/{commentId}")
  public ResponseEntity<CommentResponse> editComment(
      @PathVariable Long teamId, @PathVariable String documentId,
      @PathVariable String commentId,
      @RequestBody @Valid CommentEditRequest request
  ) {
    Comment comment = commentService.editComment(teamId, documentId, commentId, request);

    return ResponseEntity.ok(CommentResponse.from(comment));
  }

  @DeleteMapping("/team/{teamId}/documents/{documentId}/comments/{commentId}")
  public ResponseEntity<DeleteCommentsResponse> deleteComment(
      @PathVariable Long teamId, @PathVariable String documentId,
      @PathVariable String commentId, Principal principal
  ) {

    return ResponseEntity.ok(commentService.deleteComment(teamId, documentId, commentId, principal));
  }
}

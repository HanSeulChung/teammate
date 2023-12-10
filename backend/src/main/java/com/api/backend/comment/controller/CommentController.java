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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/documents/{documentId}/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping()
  public ResponseEntity<CommentResponse> createComments(
      @PathVariable Long teamId, @PathVariable String documentId,
      @RequestBody @Valid CommentInitRequest request,
      Principal principal
  ) {
    Comment comment = commentService.createComment(teamId, documentId, request, principal);

    return ResponseEntity.ok(CommentResponse.from(comment));
  }

  @GetMapping()
  public ResponseEntity<Page<CommentResponse>> getCommentsList(
      @PathVariable Long teamId, @PathVariable String documentId,
      Principal principal,
      Pageable pageable) {
    Page<Comment> commentPage = commentService.getCommentList(teamId, documentId, principal, pageable);
    Page<CommentResponse> commentDtoPage = commentPage.map(
        comment -> CommentResponse.from(comment)
    );

    return ResponseEntity.ok(commentDtoPage);
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponse> editComment(
      @PathVariable Long teamId, @PathVariable String documentId,
      @PathVariable String commentId,
      @RequestBody @Valid CommentEditRequest request,
      Principal principal
  ) {
    Comment comment = commentService.editComment(teamId, documentId, commentId, request, principal);

    return ResponseEntity.ok(CommentResponse.from(comment));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<DeleteCommentsResponse> deleteComment(
      @PathVariable Long teamId, @PathVariable String documentId,
      @PathVariable String commentId,
      Principal principal
  ) {

    return ResponseEntity.ok(commentService.deleteComment(teamId, documentId, commentId, principal));
  }
}

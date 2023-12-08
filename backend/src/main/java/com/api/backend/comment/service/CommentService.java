package com.api.backend.comment.service;

import com.api.backend.comment.data.dto.CommentRequest;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final DocumentsRepository documentsRepository;


  public Comment createComment(Long teamId, String documentIdx, CommentRequest commentRequest) {

    Documents documents = documentsRepository.findByDocumentIdx(documentIdx)
        .orElseThrow(() -> new CustomException(ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION));

    if (documents.getTeamId() != teamId) {
      throw new CustomException(ErrorCode.DOCUMENT_NOT_IN_TEAM_EXCEPTION);
    }

    Comment comment = commentRepository.save(Comment.builder()
        .teamId(teamId)
        .content(commentRequest.getContent())
        .writerId(commentRequest.getWriterId())
        .build());

    documents.getCommentIds().add(comment);
    documentsRepository.save(documents);
    return comment;
  }

  public Page<Comment> getCommentList(Long teamId, String documentIdx, Pageable pageable) {

    Documents documents = documentsRepository.findByDocumentIdx(documentIdx)
        .orElseThrow(() -> new CustomException());

    if (documents.getTeamId() != teamId) {
      throw new CustomException();
    }
    List<Comment> commentIds = documents.getCommentIds();

    if (commentIds != null && !commentIds.isEmpty()) {
      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), commentIds.size());

      List<Comment> sublist = commentIds.subList(start, end);
      return new PageImpl<>(sublist, pageable, commentIds.size());
    }
    return Page.empty();
  }

}

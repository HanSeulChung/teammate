package com.api.backend.comment.service;

import com.api.backend.comment.data.dto.CommentRequest;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
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

}

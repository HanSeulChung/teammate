package com.api.backend.comment.service;

import static com.api.backend.global.exception.type.ErrorCode.COMMENT_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.COMMENT_UNMATCH_WRITER_ID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_IN_TEAM_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;

import com.api.backend.comment.data.dto.CommentEditRequest;
import com.api.backend.comment.data.dto.CommentInitRequest;
import com.api.backend.comment.data.dto.DeleteCommentsResponse;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final DocumentsRepository documentsRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;


  @Transactional
  public Comment createComment(Long teamId, String documentIdx, CommentInitRequest commentInitRequest) {

    Documents documents = documentsRepository.findByDocumentIdx(documentIdx)
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));

    if (documents.getTeamId() != teamId) {
      throw new CustomException(DOCUMENT_NOT_IN_TEAM_EXCEPTION);
    }

    Comment comment = commentRepository.save(Comment.builder()
        .teamId(teamId)
        .content(commentInitRequest.getContent())
        .writerId(commentInitRequest.getWriterId())
        .build());

    documents.getCommentIds().add(comment);
    documentsRepository.save(documents);
    return comment;
  }

  public Page<Comment> getCommentList(Long teamId, String documentIdx, Pageable pageable) {

    Documents documents = documentsRepository.findByDocumentIdx(documentIdx)
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));

    if (documents.getTeamId() != teamId) {
      throw new CustomException(DOCUMENT_NOT_IN_TEAM_EXCEPTION);
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

  @Transactional
  public Comment editComment(Long teamId, String documentIdx, String commentId, CommentEditRequest commentEditRequest) {

    Documents documents = documentsRepository.findByDocumentIdx(documentIdx)
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));

    if (documents.getTeamId() != teamId) {
      throw new CustomException(DOCUMENT_NOT_IN_TEAM_EXCEPTION);
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND_EXCEPTION));

    if (comment.getWriterId() != commentEditRequest.getEditorId()) {
      throw new CustomException(COMMENT_UNMATCH_WRITER_ID_EXCEPTION);
    }

    return commentRepository.save(Comment.builder()
        .id(commentId)
        .writerId(commentEditRequest.getEditorId())
        .content(commentEditRequest.getContent())
        .teamId(teamId)
        .createdDt(comment.getCreatedDt())
        .build());
  }

  @Transactional
  public DeleteCommentsResponse deleteComment(Long teamId, String documentIdx, String commentId, Principal principal) {

    Long memberId = Long.parseLong(principal.getName());
    TeamParticipants teamParticipants = teamParticipantsRepository.findByMember_MemberId(memberId)
        .orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    Documents documents = documentsRepository.findByDocumentIdx(documentIdx)
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));

    if (documents.getTeamId() != teamId) {
      throw new CustomException(DOCUMENT_NOT_IN_TEAM_EXCEPTION);
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND_EXCEPTION));

    if (comment.getWriterId() != teamParticipants.getTeamParticipantsId()) {
      throw new CustomException(COMMENT_UNMATCH_WRITER_ID_EXCEPTION);
    }

    commentRepository.deleteById(commentId);

    return DeleteCommentsResponse.builder()
        .id(commentId)
        .writerId(comment.getWriterId())
        .content(comment.getContent())
        .message("해당 댓글이 삭제 되었습니다.")
        .build();
  }
}

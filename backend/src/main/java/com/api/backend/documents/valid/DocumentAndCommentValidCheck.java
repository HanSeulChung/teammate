package com.api.backend.documents.valid;


import static com.api.backend.global.exception.type.ErrorCode.COMMENT_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.COMMENT_UNMATCH_WRITER_ID_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_IN_TEAM_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_WRITER_UNMATCH_TEAM_PARTICIPANTS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.PRINCIPAL_IS_NULL;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;

import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentAndCommentValidCheck {
  private final DocumentsRepository documentsRepository;
  private final CommentRepository commentRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;


  public Long getMemberId(Principal principal){
    if (principal == null) {
      log.info("[Principal is Null]: principal.getName -> %s ", principal.getName());
      throw new CustomException(PRINCIPAL_IS_NULL);
    }
    return Long.parseLong(principal.getName());
  }

  public void validTeamParticipant(Long memberId) {
    boolean existsTeamParticipants = teamParticipantsRepository.existsByMember_MemberId(memberId);
    if (!existsTeamParticipants) {
      throw new CustomException(ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION);
    }
  }
  public TeamParticipants findValidTeamParticipantByMemberIdAndTeamId(Long memberId, Long teamId) {
    return teamParticipantsRepository.findByMember_MemberIdAndTeam_TeamId(
        memberId, teamId).orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));
  }


  public Documents findValidDocument(String documentId) {
    return documentsRepository.findById(documentId)
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));
  }

  public void validDocumentByTeamId(Long teamId, Long documentsTeamId) {
    if (documentsTeamId != teamId) {
      throw new CustomException(DOCUMENT_NOT_IN_TEAM_EXCEPTION);
    }
  }
  public void validDocumentByWriterId(Long documentWriterId, Long teamParticipantId) {
    if (documentWriterId != teamParticipantId) {
      throw new CustomException(DOCUMENT_WRITER_UNMATCH_TEAM_PARTICIPANTS_EXCEPTION);
    }
  }

  public Comment findValidComment(String commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND_EXCEPTION));
  }

  public void validCommentByWriterId(Long commentWriterId, Long teamParticipantsId) {
    if (commentWriterId != teamParticipantsId) {
      throw new CustomException(COMMENT_UNMATCH_WRITER_ID_EXCEPTION);
    }
  }
}

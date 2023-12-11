package com.api.backend.documents.service;

import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_IN_TEAM_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_WRITER_UNMATCH_TEAM_PARTICIPANTS_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.PRINCIPAL_IS_NULL;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;

import com.api.backend.documents.data.dto.DeleteDocsResponse;
import com.api.backend.documents.data.dto.DocumentInitRequest;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.documents.valid.DocumentAndCommentValidCheck;
import com.api.backend.global.exception.CustomException;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
  private final DocumentsRepository documentsRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;
  private final DocumentAndCommentValidCheck validCheck;

  public Page<Documents> getDocsList(Long teamId, Principal principal, Pageable pageable) {

    Long memberId = validCheck.getMemberId(principal);
    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberId(memberId);

    validCheck.validTeamAndTeamParticipant(teamId, teamParticipant);

    Page<Documents> allDocs = documentsRepository.findAll(pageable);
    if (allDocs != null) {
      return allDocs;
    }
    return Page.empty();
  }

  public Documents createDocs(DocumentInitRequest request, Long teamId, Principal principal) throws CustomException {
    Long memberId = validCheck.getMemberId(principal);
    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberId(memberId);

    validCheck.validTeamAndTeamParticipant(teamId, teamParticipant);

    Documents saveDocuments = documentsRepository.save(Documents.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .writerId(teamParticipant.getTeamParticipantsId())
        .teamId(teamId)
        .build());

    return saveDocuments;
  }

  @Transactional
  public DeleteDocsResponse deleteDocs(Long teamId, String documentId, Principal principal) {

    Long memberId = validCheck.getMemberId(principal);
    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberId(memberId);
    validCheck.validTeamAndTeamParticipant(teamId, teamParticipant);

    Documents documents = validCheck.findValidDocument(documentId);
    validCheck.validDocumentByTeamId(teamId, documents);
    validCheck.validDocumentByWriterId(teamParticipant, documents);
    documentsRepository.deleteById(documentId);

    return DeleteDocsResponse.builder()
        .id(documents.getId())
        .title(documents.getTitle())
        .message("삭제 되었습니다.")
        .build();
  }

}

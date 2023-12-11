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
import java.time.LocalDate;
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

  public Page<Documents> getDocsList(Long teamId, Principal principal, Pageable pageable, LocalDate startDt, LocalDate endDt) {

    Long memberId = validCheck.getMemberId(principal);
    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberId(memberId);

    validCheck.validTeamAndTeamParticipant(teamId, teamParticipant);

    Page<Documents> allDocsInTeam = null ;
    if (startDt == null && endDt == null) {
      allDocsInTeam = documentsRepository.findAllByTeamId(teamId, pageable);
      if (allDocsInTeam != null) {
        return allDocsInTeam;
      } else {
        return Page.empty();
      }
    }

    if (startDt != null && endDt == null) {
      allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtGreaterThanEqual(teamId, startDt.atStartOfDay() ,pageable);
      if (allDocsInTeam != null) {
        return allDocsInTeam;
      } else {
        return Page.empty();
      }
    }

    if (startDt == null && endDt != null) {
      allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtLessThanEqual(teamId, endDt.plusDays(1).atStartOfDay(), pageable);
      if (allDocsInTeam != null) {
        return allDocsInTeam;
      } else {
        return Page.empty();
      }
    }

    if (startDt != null && endDt != null) {
      allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtBetween(teamId, startDt.atStartOfDay(), endDt.plusDays(1).atStartOfDay(), pageable);
      if (allDocsInTeam != null) {
        return allDocsInTeam;
      } else {
        return Page.empty();
      }
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

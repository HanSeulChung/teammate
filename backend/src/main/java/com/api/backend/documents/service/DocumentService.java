package com.api.backend.documents.service;

import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_DELETEING_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_NOT_FOUND_EXCEPTION;

import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.dto.DeleteAllDocsInTeamResponse;
import com.api.backend.documents.data.dto.DeleteDocsResponse;
import com.api.backend.documents.data.dto.DocumentInitRequest;
import com.api.backend.documents.data.dto.DocumentInitResponse;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.documents.valid.DocumentAndCommentValidCheck;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import com.api.backend.team.data.repository.TeamRepository;
import com.api.backend.team.data.type.TeamRole;
import com.mongodb.bulk.BulkWriteResult;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
  private final MongoTemplate mongoTemplate;
  private final DocumentsRepository documentsRepository;
  private final CommentRepository commentRepository;
  private final DocumentAndCommentValidCheck validCheck;

  public Page<Documents> getDocsList(Long teamId, Principal principal, Pageable pageable, LocalDate startDt, LocalDate endDt) {

    Long memberId = validCheck.getMemberId(principal);

    validCheck.validTeamParticipant(memberId);

    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberIdAndTeamId(memberId, teamId);
    validCheck.validTeam(teamId);
    Page<Documents> allDocsInTeam = null ;
    if (startDt == null && endDt == null) {
      return findAllDocumentInTeam(teamId, pageable, allDocsInTeam);
    }

    if (startDt != null && endDt == null) {
      return findAllDocumentInTeamWithStartDt(teamId, startDt.atStartOfDay(), pageable, allDocsInTeam);
    }

    if (startDt == null && endDt != null) {
      return findAllDocumentInTeamWithEndDt(teamId, endDt.plusDays(1).atStartOfDay(), pageable, allDocsInTeam);
    }

    if (startDt != null && endDt != null) {
      return findAllDocumentInTeamBetweenStartDtAndEndDt(teamId, startDt.atStartOfDay(),
          endDt.plusDays(1).atStartOfDay(), pageable, allDocsInTeam);
    }
    return Page.empty();
  }

  public DocumentInitResponse createDocs(DocumentInitRequest request, Long teamId, Principal principal) throws CustomException {
    Long memberId = validCheck.getMemberId(principal);

    validCheck.validTeamParticipant(memberId);

    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberIdAndTeamId(memberId, teamId);

    Documents saveDocuments = documentsRepository.save(Documents.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .writerId(teamParticipant.getTeamParticipantsId())
        .teamId(teamId)
        .build());

    return DocumentInitResponse.from(
        saveDocuments,
        teamParticipant.getTeamNickName()
    );
  }

  @Transactional
  public DeleteDocsResponse deleteDocs(Long teamId, String documentId, Principal principal) {

    Long memberId = validCheck.getMemberId(principal);

    validCheck.validTeamParticipant(memberId);

    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberIdAndTeamId(memberId, teamId);

    Documents validDocument = validCheck.findValidDocument(documentId);
    validCheck.validDocumentByTeamId(teamId, validDocument.getTeamId());

    if (teamParticipant.getTeamRole() == TeamRole.LEADER) {
      validCheck.validWriterStatus(validDocument.getWriterId());
    } else {
      validCheck.validDocumentByWriterId(validDocument.getWriterId(), teamParticipant.getTeamParticipantsId());
    }

    commentRepository.deleteAll(validDocument.getCommentIds());
    documentsRepository.delete(validDocument);

    return DeleteDocsResponse.builder()
        .id(validDocument.getId())
        .deleteParticipantId(teamParticipant.getTeamParticipantsId())
        .teamId(teamId)
        .deleteParticipantNickName(teamParticipant.getTeamNickName())
        .title(validDocument.getTitle())
        .message("삭제 되었습니다.")
        .build();
  }

  @Transactional
  public DeleteAllDocsInTeamResponse deleteAllDocsInTeam(Long teamId) {

    Team team = validCheck.validTeamToDelete(teamId);
    BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED, Documents.class);
    Query query = new Query(Criteria.where("teamId").is(teamId));

    bulkOperations.remove(query);
    BulkWriteResult bulkWriteResult = bulkOperations.execute();
    long deletedCount = bulkWriteResult.getDeletedCount();

    return DeleteAllDocsInTeamResponse.builder()
        .teamId(teamId)
        .teamName(team.getName())
        .totalDocumentCount(deletedCount)
        .deletedDt(LocalDateTime.now())
        .build();
  }

  private Page<Documents> findAllDocumentInTeam(Long teamId, Pageable pageable, Page<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamId(teamId, pageable);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return Page.empty();
    }
  }

  private Page<Documents> findAllDocumentInTeamWithStartDt(Long teamId, LocalDateTime startDt, Pageable pageable, Page<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtGreaterThanEqual(teamId, startDt ,pageable);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return Page.empty();
    }
  }
  private Page<Documents> findAllDocumentInTeamWithEndDt(Long teamId, LocalDateTime endDt, Pageable pageable, Page<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtLessThanEqual(teamId, endDt, pageable);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return Page.empty();
    }
  }

  private Page<Documents> findAllDocumentInTeamBetweenStartDtAndEndDt(Long teamId, LocalDateTime startDt, LocalDateTime endDt, Pageable pageable, Page<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtBetween(teamId, startDt, endDt, pageable);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return Page.empty();
    }
  }

}

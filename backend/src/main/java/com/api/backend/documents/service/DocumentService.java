package com.api.backend.documents.service;

import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.dto.DeleteDocsResponse;
import com.api.backend.documents.data.dto.DocumentInitRequest;
import com.api.backend.documents.data.dto.DocumentInitResponse;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.documents.valid.DocumentAndCommentValidCheck;
import com.api.backend.global.exception.CustomException;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.type.TeamRole;
import com.mongodb.bulk.BulkWriteResult;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public List<Documents> getDocsList(Long teamId, Principal principal, LocalDate startDt, LocalDate endDt) {

    Long memberId = validCheck.getMemberId(principal);

    validCheck.validTeamParticipant(memberId);

    TeamParticipants teamParticipant = validCheck.findValidTeamParticipantByMemberIdAndTeamId(memberId, teamId);
    validCheck.validTeam(teamId);
    List<Documents> allDocsInTeam = null;
    if (startDt == null && endDt == null) {
      return findAllDocumentInTeam(teamId, allDocsInTeam);
    }

    if (startDt != null && endDt == null) {
      return findAllDocumentInTeamWithStartDt(teamId, startDt.atStartOfDay(),  allDocsInTeam);
    }

    if (startDt == null && endDt != null) {
      return findAllDocumentInTeamWithEndDt(teamId, endDt.plusDays(1).atStartOfDay(), allDocsInTeam);
    }

    if (startDt != null && endDt != null) {
      return findAllDocumentInTeamBetweenStartDtAndEndDt(teamId, startDt.atStartOfDay(),
          endDt.plusDays(1).atStartOfDay(), allDocsInTeam);
    }
    return new ArrayList<>();
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

    if (teamParticipant.getTeamRole() == TeamRole.LEADER && teamParticipant.getTeamParticipantsId() != validDocument.getWriterId()) {
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
  public void deleteAllDocsInTeams(List<Long> teamIdList) {
    BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED, Documents.class);

    for (Long teamId : teamIdList) {
      Query query = new Query(Criteria.where("teamId").is(teamId));
      bulkOperations.remove(query);
    }

    BulkWriteResult bulkWriteResult = bulkOperations.execute();
    long deletedCount = bulkWriteResult.getDeletedCount();
    log.info("{}개의 팀들의 총 문서 {}개가 삭제되었습니다.", teamIdList.size(), deletedCount);
  }

  private List<Documents> findAllDocumentInTeam(Long teamId, List<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamId(teamId);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return new ArrayList<>();
    }
  }

  private List<Documents> findAllDocumentInTeamWithStartDt(Long teamId, LocalDateTime startDt, List<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtGreaterThanEqual(teamId, startDt);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return new ArrayList<>();
    }
  }
  private List<Documents> findAllDocumentInTeamWithEndDt(Long teamId, LocalDateTime endDt,  List<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtLessThanEqual(teamId, endDt);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return new ArrayList<>();
    }
  }

  private List<Documents> findAllDocumentInTeamBetweenStartDtAndEndDt(Long teamId, LocalDateTime startDt, LocalDateTime endDt, List<Documents> allDocsInTeam) {
    allDocsInTeam = documentsRepository.findAllByTeamIdAndCreatedDtBetween(teamId, startDt, endDt);
    if (allDocsInTeam != null) {
      return allDocsInTeam;
    } else {
      return new ArrayList<>();
    }
  }

}

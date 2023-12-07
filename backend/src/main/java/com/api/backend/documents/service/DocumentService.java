package com.api.backend.documents.service;

import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;

import com.api.backend.documents.data.dto.DocumentInitRequest;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
  private final DocumentsRepository documentsRepository;
  private final TeamParticipantsRepository teamParticipantsRepository;

  public Page<Documents> getDocsList(Pageable pageable) {
    // TODO 조회시에도 사용자에 관해서, teamId 유효성 검사 추후 추가
    Page<Documents> allDocs = documentsRepository.findAll(pageable);
    if (allDocs == null) {
      return Page.empty();
    }
    if (allDocs.getTotalElements() == 0) {
      return Page.empty();
    }
    return allDocs;
  }

  public Documents createDocs(DocumentInitRequest request, Long teamId) throws CustomException {

    TeamParticipants teamParticipants = teamParticipantsRepository.findByMember_Email(
        request.getWriterEmail()).orElseThrow(() -> new CustomException(TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION));

    if (teamId != teamParticipants.getTeam().getTeamId()) {
      throw new CustomException(TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION);
    }

    String uuid = UUID.randomUUID().toString();

    Documents saveDocuments = documentsRepository.save(Documents.builder()
        .documentIdx(uuid)
        .title(request.getTitle())
        .content(request.getContent())
        .writerId(teamParticipants.getTeamParticipantsId())
        .teamId(teamId)
        .build());

    return saveDocuments;
  }
}

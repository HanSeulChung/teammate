package com.api.backend.documents.service;

import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION;
import static com.api.backend.global.exception.type.ErrorCode.TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.api.backend.documents.data.dto.DocumentInitRequest;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import com.api.backend.member.data.entity.Member;
import com.api.backend.team.data.entity.Team;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.data.repository.TeamParticipantsRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

  @Mock
  private DocumentsRepository documentsRepository;
  @Mock
  private TeamParticipantsRepository teamParticipantsRepository;
  @InjectMocks
  private DocumentService documentService;

  @Test
  @DisplayName("문서 전체 조회(문서가 존재할 때")
  void getDocsListWhenDocsExists() {
      //given
    Documents document = Documents.builder()
        .id("testId")
        .documentIdx("testDocumentsIdx")
        .title("testTitle")
        .content("testContent")
        .writerId(1L)
        .createdDt(LocalDateTime.now())
        .updatedDt(LocalDateTime.now())
        .build();

    Pageable pageable = PageRequest.of(0, 4, Sort.unsorted());
    Page<Documents> documentsPage = new PageImpl<>(Collections.singletonList(document));
    when(documentsRepository.findAll(pageable)).thenReturn(documentsPage);

    //when
    Page<Documents> docsPage = documentService.getDocsList(pageable);

    //then
    assertNotNull(docsPage);
    assertFalse(docsPage.isEmpty());
    assertEquals(1, docsPage.getContent().size());
    assertEquals("testId", docsPage.getContent().get(0).getId());
    assertEquals("testDocumentsIdx", docsPage.getContent().get(0).getDocumentIdx());
  }

  @Test
  @DisplayName("문서 전체 조회(문서가 존재하지 않을 때")
  void getDocsListWhenDocsNotExists() {
    //given

    //when
    Pageable pageable = PageRequest.of(0, 4, Sort.unsorted());
    Page<Documents> docsPage = documentService.getDocsList(pageable);

    //then
    assertNotNull(docsPage);
    assertTrue(docsPage.isEmpty());
  }

  private Documents createtestDocuments() {
    return Documents.builder()
        .id("test_id")
        .documentIdx("testDocumentIdx")
        .title("testTitle")
        .content("testContent")
        .writerId(1L)
        .teamId(2L)
        .createdDt(LocalDateTime.now())
        .updatedDt(LocalDateTime.now())
        .build();
  }

  private TeamParticipants createTestSetting() {
    Team team = Team.builder()
        .teamId(2L)
        .build();

    Member member  = Member.builder()
        .email("test@Email.com")
        .build();

    TeamParticipants teamParticipants = TeamParticipants.builder()
        .team(team)
        .member(member)
        .build();

    return teamParticipants;

  }

  private DocumentInitRequest createTestDocumentsRequest() {
    return DocumentInitRequest.builder()
        .title("testTitle")
        .content("testContent")
        .writerEmail("test@Email.com")
        .build();
  }
  @Test
  @DisplayName("문서 초기 생성 성공")
  void createDocs_Success() {
    //given
    DocumentInitRequest request = createTestDocumentsRequest();
    TeamParticipants teamParticipants = createTestSetting();
    Documents documents = createtestDocuments();

    when(teamParticipantsRepository.findByMember_Email("test@Email.com")).thenReturn(Optional.of(teamParticipants));
    when(documentsRepository.save(any())).thenReturn(documents);
    //when
    Documents savedDocuments = documentService.createDocs(request, 2L);

    //then
    assertEquals(2L, savedDocuments.getTeamId());
    assertEquals("testTitle", savedDocuments.getTitle());
    assertEquals("testContent", savedDocuments.getContent());

  }


  @Test
  @DisplayName("문서 초기 생성 실패_teamParticipants가 아닐 경우")
  void createDocs_Fail_By_Not_Exist() {
    //given
    DocumentInitRequest request = createTestDocumentsRequest();
    createTestSetting();

    //when
    CustomException exception = assertThrows(CustomException.class, () -> documentService.createDocs(request, 2L));

    //then
    assertEquals(exception.getErrorCode(), TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION);

  }

  @Test
  @DisplayName("문서 초기 생성 실패_teamId가 다를 경우")
  void createDocs_Fail_By_UnMatch_TeamId() {
    //given
    DocumentInitRequest request = createTestDocumentsRequest();
    TeamParticipants teamParticipants = createTestSetting();

    when(teamParticipantsRepository.findByMember_Email("test@Email.com")).thenReturn(Optional.of(teamParticipants));

    //when
    CustomException exception = assertThrows(CustomException.class, () -> documentService.createDocs(request, 213L));;

    //then
    assertEquals(exception.getErrorCode(), TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION);

  }
}
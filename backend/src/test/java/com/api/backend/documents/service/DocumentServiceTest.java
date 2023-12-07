package com.api.backend.documents.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import java.time.LocalDateTime;
import java.util.Collections;
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
}
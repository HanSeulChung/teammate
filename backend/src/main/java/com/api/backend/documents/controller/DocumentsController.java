package com.api.backend.documents.controller;

import com.api.backend.documents.data.dto.DocumentResponse;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentsController {
  private final DocumentService documentService;

  @GetMapping("/team/{teamId}/documents")
  public ResponseEntity<Page<DocumentResponse>> getDocsList(
        @PathVariable Long teamId,
        Pageable pageable) {
    Page<Documents> docsPage = documentService.getDocsList(pageable);
    Page<DocumentResponse> documentDtoPage = docsPage.map(
        document -> DocumentResponse.builder()
                              .id(document.getId())
                              .documentIdx(document.getDocumentIdx())
                              .title(document.getTitle())
                              .content(document.getContent())
                              .writerId(document.getWriterId())
                              .modifierId(document.getModifierId())
                              .teamId(document.getTeamId())
                              .createdDt(document.getCreatedDt())
                              .updatedDt(document.getUpdatedDt())
                              .build());

    return ResponseEntity.ok().body(documentDtoPage);
  }
}

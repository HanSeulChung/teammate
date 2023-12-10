package com.api.backend.documents.controller;

import com.api.backend.documents.data.dto.DeleteDocsResponse;
import com.api.backend.documents.data.dto.DocumentInitRequest;
import com.api.backend.documents.data.dto.DocumentResponse;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.service.DocumentService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team/{teamId}/documents")
@RequiredArgsConstructor
public class DocumentsController {
  private final DocumentService documentService;

  @GetMapping()
  public ResponseEntity<Page<DocumentResponse>> getDocsList(
        @PathVariable Long teamId,
        Principal principal,
        Pageable pageable) {
    Page<Documents> docsPage = documentService.getDocsList(teamId, principal, pageable);
    Page<DocumentResponse> documentDtoPage = docsPage.map(
        document -> DocumentResponse.from(document));

    return ResponseEntity.ok(documentDtoPage);
  }

  @PostMapping()
  public ResponseEntity<DocumentResponse> createDocs(
          @PathVariable Long teamId,
          @RequestBody @Valid DocumentInitRequest request,
          Principal principal) {
    return ResponseEntity.ok(DocumentResponse.from(documentService.createDocs(request, teamId, principal)));
  }

  @DeleteMapping("/{documentsId}")
  public ResponseEntity<DeleteDocsResponse> deleteDocs(@PathVariable Long teamId, @PathVariable String documentsId, Principal principal) {

    return ResponseEntity.ok()
        .body(documentService.deleteDocs(teamId, documentsId, principal));
  }
}

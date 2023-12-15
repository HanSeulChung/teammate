package com.api.backend.documents.controller;

import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION;

import com.api.backend.documents.data.dto.RequestedDocument;
import com.api.backend.documents.data.dto.DocumentResponse;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentsWebSocketController {
  private final DocumentsRepository documentsRepository;

  @MessageMapping("/doc.showDocs")
  @SendTo("/topic/public")
  public DocumentResponse getDocs(
      @Payload RequestedDocument requestedDocument
  ) {
    Documents documents = documentsRepository.findById(requestedDocument.getDocumentId())
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));
    return DocumentResponse.from(documents);
  }

  @MessageMapping("/doc.saveDocs")
  @SendTo("/topic/public")
  public DocumentResponse saveDocs(
      @Payload RequestedDocument requestedDocument
  ) {
    Documents documents = documentsRepository.findById(requestedDocument.getDocumentId())
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));
    return DocumentResponse.from(documents);
  }


  // 브로드 캐스팅
  @MessageMapping("/doc.changeDocs")
  @SendTo("/topic/public")
  public DocumentResponse broadcastChanges(
      @Payload RequestedDocument requestedDocument
  ) {
    Documents documents = documentsRepository.findById(requestedDocument.getDocumentId())
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));
    return DocumentResponse.from(documents);
  }


}

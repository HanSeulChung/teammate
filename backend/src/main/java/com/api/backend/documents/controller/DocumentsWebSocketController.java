package com.api.backend.documents.controller;

import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION;

import com.api.backend.documents.data.dto.DocumentResponse;
import com.api.backend.documents.data.dto.RequestedDocument;
import com.api.backend.documents.data.dto.TotalMessage;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DocumentsWebSocketController {
  private final DocumentsRepository documentsRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/doc.showDocs")
  public void getDocs(
      @Payload RequestedDocument requestedDocument
  ) {
    Documents documents = documentsRepository.findById(requestedDocument.getDocumentId())
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));
    messagingTemplate.convertAndSend("/topic/display/" + requestedDocument.getDocumentId(), DocumentResponse.from(documents));
    log.info("subsribe로 전송 : /topic/display/{}", requestedDocument.getDocumentId());
  }

  @MessageMapping("/doc.saveDocs")
  @SendTo("/topic/save")
  public DocumentResponse saveDocs(
      @Payload TotalMessage totalMessage
  ) {
    Documents documents = documentsRepository.findById(totalMessage.getDocumentId())
        .orElseThrow(() -> new CustomException(DOCUMENT_NOT_FOUND_EXCEPTION));

    documents.setDifference(totalMessage);
    log.info("document가 수정되서 저장되었습니다. document is {}", documents);
    return DocumentResponse.from(documentsRepository.save(documents));
  }

  // 브로드 캐스팅
  @MessageMapping("/doc.updateDocsByTextChange")
  public void handleTotalBroadCastByTextChange(@Payload TotalMessage totalMessage) {

    log.info("TotalMessage From text-change: {} in documentId : {}", totalMessage, totalMessage.getDocumentId());
    messagingTemplate.convertAndSend("/topic/broadcastByTextChange/" + totalMessage.getDocumentId() , totalMessage);
  }

}

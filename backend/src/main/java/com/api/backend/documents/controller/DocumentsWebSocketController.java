package com.api.backend.documents.controller;

import static com.api.backend.global.exception.type.ErrorCode.DOCUMENT_NOT_FOUND_EXCEPTION;

import com.api.backend.documents.data.dto.DeltaMessage;
import com.api.backend.documents.data.dto.RequestedDocument;
import com.api.backend.documents.data.dto.DocumentResponse;
import com.api.backend.documents.data.dto.SelectionChangeMessage;
import com.api.backend.documents.data.dto.TotalMessage;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import com.api.backend.global.exception.CustomException;
import java.util.LinkedHashMap;
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
  @MessageMapping("/doc.updateDocsBySelectionChange")
  public void handleBroadCastBySelectionChange(@Payload DeltaMessage deltaMessage) {
    LinkedHashMap<String, Integer> deltaValue = (LinkedHashMap<String, Integer>) deltaMessage.getDeltaValue();

    log.info("deltaMessage From selection-change {}", deltaMessage);

    Integer index = deltaValue.get("index");
    Integer length = deltaValue.get("length");

    SelectionChangeMessage selectionChangeMessage = SelectionChangeMessage.builder()
        .eventName("selection-change")
        .index(index)
        .length(length)
        .build();
    messagingTemplate.convertAndSend("/topic/broadcastBySelectionChange", selectionChangeMessage);
  }

//  @MessageMapping("/doc.updateDocsByTextChange")
//  public void handleBroadCastByTextChange(@Payload DeltaMessage deltaMessage) {
//
//    System.out.println(deltaMessage);
//
//    messagingTemplate.convertAndSend("/topic/broadcastByTextChange", deltaMessage);
//  }

  @MessageMapping("/doc.updateDocsByTextChange")
  public void handleTotalBroadCastByTextChange(@Payload TotalMessage totalMessage) {

    log.info("TotalMessage From text-change: {} in documentId : {}", totalMessage, totalMessage.getDocumentId());
    messagingTemplate.convertAndSend("/topic/broadcastByTextChange/" + totalMessage.getDocumentId() , totalMessage);
  }

}

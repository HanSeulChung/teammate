package com.api.backend.global.websocket;

import com.api.backend.documents.data.dto.RequestedDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String documentId = (String) headerAccessor.getSessionAttributes().get("documentId");
    if (documentId != null) {
      log.info("documentId disconnected: {}", documentId);
      var requestedDocument = RequestedDocument.builder()
          .documentId(documentId)
          .build();
      messagingTemplate.convertAndSend("/topic/public", requestedDocument);
    }
  }
}

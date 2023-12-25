package com.api.backend.global.websocket;

import com.api.backend.global.exception.type.ErrorCode;
import java.nio.charset.StandardCharsets;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

  public WebSocketErrorHandler() {
    super();
  }

  @Override
  public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
    if(ex.getCause().getMessage().equals("jwt")) {
      return jwtException(clientMessage, ex);
    }

    if(ex.getCause().getMessage().equals("error")) {
      return messageException(clientMessage, ex);
    }

    return super.handleClientMessageProcessingError(clientMessage, ex);
  }

  //메시지 예외
  private Message<byte[]> messageException(Message<byte[]> clientMessage, Throwable ex) {
    return errorMessage(ErrorCode.INVALID_MESSAGE_EXCEPTION);
  }

  //jwt 예외
  private Message<byte[]> jwtException(Message<byte[]> clientMessage, Throwable ex) {
    return errorMessage(ErrorCode.TOKEN_INVALID_EXCEPTION);
  }

  //메시지 생성
  private Message<byte[]> errorMessage(ErrorCode errorCode) {
    String code = String.valueOf(errorCode.getErrorMessage());

    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

    accessor.setMessage(String.valueOf(errorCode.getCode()));
    accessor.setLeaveMutable(true);

    return MessageBuilder.createMessage(code.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
  }
}
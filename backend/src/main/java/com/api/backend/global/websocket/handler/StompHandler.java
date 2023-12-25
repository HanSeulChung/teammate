package com.api.backend.global.websocket.handler;


import com.api.backend.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  private static final String BEARER_PREFIX = "Bearer ";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if (accessor.getCommand() == StompCommand.CONNECT) {
      String authorizationHeader = String.valueOf(accessor.getNativeHeader("Authorization"));
      if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
        String token = authorizationHeader.substring(BEARER_PREFIX.length(), authorizationHeader.length() - 1);
        if (jwtTokenProvider.validateAccessToken(token)) {
          Authentication authentication = jwtTokenProvider.getAuthentication(token);
          if (authentication != null) {
            accessor.setUser(authentication);
          } else {
            throw new InsufficientAuthenticationException("Authentication failed");
          }
        } else {
          throw new InsufficientAuthenticationException("Invalid access token");
        }
      } else {
        throw new InsufficientAuthenticationException("No or invalid Authorization header");
      }
    }
    return message;

  }
}
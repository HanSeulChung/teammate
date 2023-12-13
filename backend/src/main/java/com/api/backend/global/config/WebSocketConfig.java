package com.api.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*") // 이 부분을 추가하여 CORS를 허용할 수 있습니다.
        .setAllowedOrigins("http://localhost:5173");

    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*") // 이 부분을 추가하여 CORS를 허용할 수 있습니다.
        .setAllowedOrigins("http://localhost:5173")
        .withSockJS(); // SockJS를 사용하고자 하는 경우에만 추가합니다.
  }
}

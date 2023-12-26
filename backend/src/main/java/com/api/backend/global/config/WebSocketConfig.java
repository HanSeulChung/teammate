package com.api.backend.global.config;

import com.api.backend.global.websocket.handler.StompHandler;
import lombok.extern.slf4j.Slf4j;
import com.api.backend.global.websocket.handler.WebSocketErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final WebSocketErrorHandler webSocketErrorHandler;
  private final StompHandler stompHandler;

  @Value("${frontend.host}")
  String host;
  @Value("${frontend.port}")
  String port;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
    log.info("configureMessageBroker -----");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .addInterceptors()
        .setAllowedOriginPatterns("*") // 이 부분을 추가하여 CORS를 허용할 수 있습니다.
        .setAllowedOrigins("http://"+ host +":"+ port);
    registry.setErrorHandler(webSocketErrorHandler);
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompHandler);
  }
}

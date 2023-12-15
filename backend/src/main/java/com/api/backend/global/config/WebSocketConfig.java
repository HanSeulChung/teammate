package com.api.backend.global.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

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
        .setAllowedOriginPatterns("*") // 이 부분을 추가하여 CORS를 허용할 수 있습니다.
        .setAllowedOrigins("http://"+ host +":"+ port);


    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*") // 이 부분을 추가하여 CORS를 허용할 수 있습니다.
        .setAllowedOrigins("http://"+ host +":"+ port)
        .withSockJS(); // SockJS를 사용하고자 하는 경우에만 추가합니다.
  }
}

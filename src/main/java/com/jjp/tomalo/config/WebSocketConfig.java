package com.jjp.tomalo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. 클라이언트가 웹소켓 연결을 맺을 엔드포인트 (Handshake)
        // ws://localhost:8080/ws-stomp 로 연결하게 됨
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*"); // 모든 도메인 허용 (개발용)
        // .withSockJS(); // 필요하다면 SockJS 지원 추가 (앱에서는 보통 기본 웹소켓 사용)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. 메시지 받을 때 (Client -> Server)
        // prefix가 "/pub"으로 시작하면 @MessageMapping이 달린 컨트롤러로 라우팅
        registry.setApplicationDestinationPrefixes("/pub");

        // 3. 메시지 보낼 때 (Server -> Client)
        // prefix가 "/sub"으로 시작하면 브로커가 해당 채널을 구독한 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/sub");
    }

}

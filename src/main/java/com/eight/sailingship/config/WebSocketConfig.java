package com.eight.sailingship.config;

import com.eight.sailingship.handler.WebSocketCustomHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 엔드포인트 지정 + 웹소켓 서버 요청 모두 수용
        registry.addHandler(customHandler(), "/wsHandler").setAllowedOriginPatterns("*");
    }

    @Bean
    // 웹소켓 핸들러 지정
    public WebSocketHandler customHandler() {
        return new WebSocketCustomHandler();
    }
}
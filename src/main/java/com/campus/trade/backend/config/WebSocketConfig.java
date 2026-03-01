package com.campus.trade.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;

    public WebSocketConfig(WebSocketChatHandler webSocketChatHandler) {
        this.webSocketChatHandler = webSocketChatHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 处理器
        // 前端连接地址：ws://localhost:8080/ws/chat?userId=1
        registry.addHandler(webSocketChatHandler, "/ws/chat")
                .setAllowedOrigins("http://localhost:5173", "http://127.0.0.1:5173"); // 限制为前端域名
    }
}
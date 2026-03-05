package com.campus.trade.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket 配置类
 * 
 * 启用并配置 WebSocket 支持，用于实时聊天功能
 * 前端连接地址：ws://localhost:8080/ws/chat?userId=xxx
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Configuration                              // Spring 配置类注解
@EnableWebSocket                           // 启用 WebSocket 支持
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;  // WebSocket 聊天处理器

    /**
     * 构造方法注入 WebSocketChatHandler
     * 
     * @param webSocketChatHandler WebSocket 聊天处理器
     */
    public WebSocketConfig(WebSocketChatHandler webSocketChatHandler) {
        this.webSocketChatHandler = webSocketChatHandler;
    }

    /**
     * 注册 WebSocket 处理器
     * 
     * @param registry WebSocketHandlerRegistry 用于注册 WebSocket 处理器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 处理器，处理 /ws/chat 路径的请求
        // 前端连接地址：ws://localhost:8080/ws/chat?userId=1
        registry.addHandler(webSocketChatHandler, "/ws/chat")
                .setAllowedOrigins("http://localhost:5173", "http://127.0.0.1:5173"); // 限制为前端域名，增强安全性
    }
}
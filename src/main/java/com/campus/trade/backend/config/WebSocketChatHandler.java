package com.campus.trade.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    // 存储在线用户：userId -> Session
    private static final Map<Long, WebSocketSession> onlineUsers = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从 URL 参数中获取 userId
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            String userIdStr = query.split("=")[1];
            Long userId = Long.parseLong(userIdStr);

            // 存储用户会话
            onlineUsers.put(userId, session);
            System.out.println("用户 " + userId + " 上线，当前在线人数：" + onlineUsers.size());

            // 发送欢迎消息
            Map<String, Object> welcomeMsg = Map.of(
                    "type", "welcome",
                    "message", "连接成功",
                    "userId", userId
            );
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMsg)));
        }
    }

    /**
     * 收到客户端消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("收到消息：" + payload);

        // 解析消息
        Map<String, Object> msg = objectMapper.readValue(payload, Map.class);
        String type = (String) msg.get("type");

        if ("chat".equals(type)) {
            // 聊天消息，转发给接收者
            Long receiverId = Long.valueOf(msg.get("receiverId").toString());
            WebSocketSession receiverSession = onlineUsers.get(receiverId);

            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(payload));
                System.out.println("消息已推送给用户 " + receiverId);
            } else {
                System.out.println("用户 " + receiverId + " 不在线，消息未推送");
            }
        }
    }

    /**
     * 连接关闭后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 移除离线用户
        onlineUsers.entrySet().removeIf(entry -> entry.getValue() == session);
        System.out.println("用户下线，当前在线人数：" + onlineUsers.size());
    }

    /**
     * 传输错误
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("传输错误：" + exception.getMessage());
        if (session.isOpen()) {
            session.close();
        }
    }

    /**
     * 🔑 关键方法：向指定用户发送消息（供 Controller 调用）
     */
    public void sendMessageToUser(Long userId, String message) throws IOException {
        WebSocketSession session = onlineUsers.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
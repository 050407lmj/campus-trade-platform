package com.campus.trade.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 聊天处理器
 * 
 * 处理 WebSocket 连接、消息收发、断开连接等事件
 * 实现实时聊天功能，支持用户状态广播
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Component                              // Spring 组件注解
public class WebSocketChatHandler extends TextWebSocketHandler {

    /**
     * 存储在线用户会话
     * Key: 用户 ID (Long)
     * Value: WebSocketSession
     * 使用 ConcurrentHashMap 保证线程安全
     */
    private static final Map<Long, WebSocketSession> onlineUsers = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();  // JSON 序列化工具

    /**
     * 连接建立成功后回调
     * 
     * 执行逻辑：
     * 1. 从 URL 参数中获取 userId
     * 2. 将用户会话添加到在线用户列表
     * 3. 发送欢迎消息给新用户
     * 4. 通知其他在线用户有人上线
     * 
     * @param session WebSocket 会话
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
                    "userId", userId,
                    "onlineCount", onlineUsers.size()
            );
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMsg)));
            
            // 通知其他在线用户有人上线
            broadcastUserStatus(userId, "online");
        }
    }

    /**
     * 收到客户端消息后回调
     * 
     * 处理逻辑：
     * 1. 解析接收到的消息
     * 2. 根据消息类型处理（目前只支持 chat 类型）
     * 3. 将消息转发给接收者（如果在线）
     * 
     * @param session WebSocket 会话
     * @param message 接收到的文本消息
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
     * 连接关闭后回调
     * 
     * 处理逻辑：
     * 1. 查找离线用户的 ID
     * 2. 从在线用户列表中移除该用户
     * 3. 通知其他在线用户有人下线
     * 
     * @param session WebSocket 会话
     * @param status 关闭状态
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 查找离线用户ID
        Long offlineUserId = null;
        for (Map.Entry<Long, WebSocketSession> entry : onlineUsers.entrySet()) {
            if (entry.getValue() == session) {
                offlineUserId = entry.getKey();
                break;
            }
        }
        
        // 移除离线用户
        onlineUsers.entrySet().removeIf(entry -> entry.getValue() == session);
        System.out.println("用户下线，当前在线人数：" + onlineUsers.size());
        
        // 通知其他在线用户有人下线
        if (offlineUserId != null) {
            broadcastUserStatus(offlineUserId, "offline");
        }
    }

    /**
     * 传输错误时回调
     * 
     * @param session WebSocket 会话
     * @param exception 异常信息
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("传输错误：" + exception.getMessage());
        if (session.isOpen()) {
            session.close();
        }
    }

    /**
     * 向指定用户发送消息
     * 供 Controller 调用，用于 HTTP 请求触发 WebSocket 消息推送
     * 
     * @param userId 接收者用户 ID
     * @param message 要发送的消息（JSON 字符串）
     * @throws IOException 发送失败时抛出
     */
    public void sendMessageToUser(Long userId, String message) throws IOException {
        WebSocketSession session = onlineUsers.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
    
    /**
     * 广播用户状态变化
     * 通知所有在线用户（除了目标用户）有人上线/下线
     * 
     * @param userId 状态变化的用户 ID
     * @param status 状态（"online" 或 "offline"）
     */
    private void broadcastUserStatus(Long userId, String status) {
        Map<String, Object> statusMsg = Map.of(
                "type", "userStatus",
                "userId", userId,
                "status", status
        );
        
        try {
            String message = objectMapper.writeValueAsString(statusMsg);
            for (Map.Entry<Long, WebSocketSession> entry : onlineUsers.entrySet()) {
                if (!entry.getKey().equals(userId) && entry.getValue().isOpen()) {
                    entry.getValue().sendMessage(new TextMessage(message));
                }
            }
        } catch (Exception e) {
            System.out.println("广播用户状态失败: " + e.getMessage());
        }
    }
}
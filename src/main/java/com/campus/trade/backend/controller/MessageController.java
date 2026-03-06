package com.campus.trade.backend.controller;

import com.campus.trade.backend.config.WebSocketChatHandler;
import com.campus.trade.backend.entity.Message;
import com.campus.trade.backend.entity.User;
import com.campus.trade.backend.repository.MessageRepository;
import com.campus.trade.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 消息控制器（Controller）
 * 
 * 处理与聊天消息相关的 HTTP 请求，包括：
 * - 发送消息（保存到数据库 + WebSocket 推送）
 * - 获取聊天记录
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@RestController                              // RESTful 控制器注解
@RequestMapping("/api/messages")          // 基础请求路径
public class MessageController {

    @Autowired                             // 自动依赖注入
    private MessageRepository messageRepository;  // 消息数据访问层

    @Autowired                             // 自动依赖注入
    private UserRepository userRepository;        // 用户数据访问层

    @Autowired                             // 自动依赖注入
    private WebSocketChatHandler webSocketChatHandler;  // WebSocket 聊天处理器

    private final ObjectMapper objectMapper = new ObjectMapper();  // JSON 序列化工具

    /**
     * 发送消息
     * 功能：
     * 1. 保存消息到数据库
     * 2. 通过 WebSocket 推送给接收者（如果在线）
     * 
     * @param messageData 消息数据（包含 senderId、receiverId、content）
     * @return Map<String, Object> 发送结果（success、message、messageId）
     */
    @PostMapping                                      // POST 请求映射到 /api/messages
    public Map<String, Object> sendMessage(@RequestBody Map<String, Object> messageData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Message message = new Message();
            Long senderId = Long.parseLong(messageData.get("senderId").toString());
            Long receiverId = Long.parseLong(messageData.get("receiverId").toString());

            User sender = userRepository.findById(senderId).orElse(null);
            User receiver = userRepository.findById(receiverId).orElse(null);

            if (sender == null || receiver == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }

            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent((String) messageData.get("content"));
            message.setIsRead(false);

            messageRepository.save(message);

            // 通过 WebSocket 推送给接收者（如果在线）
            try {
                Map<String, Object> wsMsg = new HashMap<>();
                wsMsg.put("type", "chat");
                wsMsg.put("id", message.getId());
                wsMsg.put("senderId", senderId);
                wsMsg.put("receiverId", receiverId);
                wsMsg.put("content", message.getContent());
                wsMsg.put("createTime", message.getCreateTime().toString());
                wsMsg.put("sender", Map.of("id", sender.getId(), "username", sender.getUsername()));

                webSocketChatHandler.sendMessageToUser(receiverId, objectMapper.writeValueAsString(wsMsg));
            } catch (Exception e) {
                System.out.println("WebSocket 推送失败：" + e.getMessage());
            }

            result.put("success", true);
            result.put("message", "发送成功");
            result.put("messageId", message.getId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取两个用户之间的聊天记录
     * 
     * @param userId1 用户 1 的 ID
     * @param userId2 用户 2 的 ID
     * @return List<Map<String, Object>> 聊天记录列表，按时间升序排列
     */
    @GetMapping("/between/{userId1}/{userId2}")      // GET 请求映射到 /api/messages/between/{userId1}/{userId2}
    public List<Map<String, Object>> getChatHistory(@PathVariable Long userId1, @PathVariable Long userId2) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByCreateTimeAsc(userId1, userId2);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", message.getId());
            map.put("content", message.getContent());
            map.put("isRead", message.getIsRead());
            map.put("createTime", message.getCreateTime());

            if (message.getSender() != null) {
                map.put("sender", Map.of(
                        "id", message.getSender().getId(),
                        "username", message.getSender().getUsername()
                ));
            }

            result.add(map);
        }

        return result;
    }

    /**
     * 获取用户未读消息数量
     * 
     * @param userId 用户ID
     * @return Map<String, Object> 未读消息数量
     */
    @GetMapping("/unread/count/{userId}")
    public Map<String, Object> getUnreadCount(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        int count = messageRepository.countByReceiverIdAndIsRead(userId, false);
        result.put("success", true);
        result.put("count", count);
        return result;
    }

    /**
     * 标记消息已读
     * 
     * @param userId 当前用户ID
     * @param senderId 发送者ID
     * @return Map<String, Object> 操作结果
     */
    @PostMapping("/mark-read/{userId}/{senderId}")
    public Map<String, Object> markAsRead(@PathVariable Long userId, @PathVariable Long senderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Message> unreadMessages = messageRepository.findBySenderIdAndReceiverIdAndIsRead(senderId, userId, false);
            for (Message msg : unreadMessages) {
                msg.setIsRead(true);
            }
            messageRepository.saveAll(unreadMessages);
            result.put("success", true);
            result.put("message", "已标记为已读");
            result.put("count", unreadMessages.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "标记失败：" + e.getMessage());
        }
        return result;
    }
}
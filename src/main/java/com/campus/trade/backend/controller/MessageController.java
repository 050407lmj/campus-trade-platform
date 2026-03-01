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

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketChatHandler webSocketChatHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 发送消息（同时保存到数据库 + WebSocket 推送）
    @PostMapping
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

    // 获取两个用户之间的聊天记录
    @GetMapping("/between/{userId1}/{userId2}")
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
}
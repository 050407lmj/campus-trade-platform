package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 获取两个用户之间的聊天记录
    List<Message> findBySenderIdAndReceiverIdOrderByCreateTimeAsc(Long senderId, Long receiverId);

    // 获取某个用户的所有消息
    List<Message> findBySenderIdOrReceiverIdOrderByCreateTimeDesc(Long senderId, Long receiverId);
}
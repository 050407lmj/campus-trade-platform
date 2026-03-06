package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 消息数据访问层（Repository）
 * 
 * 继承 JpaRepository，提供对 Message 实体的 CRUD 操作和自定义查询方法
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Repository                                    // Spring 仓库组件注解
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 获取两个用户之间的聊天记录
     * 按创建时间升序排列（从旧到新）
     * 
     * @param senderId 发送者 ID
     * @param receiverId 接收者 ID
     * @return List<Message> 聊天记录列表
     */
    List<Message> findBySenderIdAndReceiverIdOrderByCreateTimeAsc(Long senderId, Long receiverId);

    /**
     * 获取某个用户的所有消息（包括发送和接收的）
     * 按创建时间降序排列（从新到旧）
     * 
     * @param senderId 发送者 ID
     * @param receiverId 接收者 ID
     * @return List<Message> 消息列表
     */
    List<Message> findBySenderIdOrReceiverIdOrderByCreateTimeDesc(Long senderId, Long receiverId);

    /**
     * 获取用户未读消息数量
     * 
     * @param receiverId 接收者 ID
     * @param isRead 是否已读
     * @return 未读消息数量
     */
    int countByReceiverIdAndIsRead(Long receiverId, boolean isRead);

    /**
     * 获取两个用户之间的未读消息
     * 
     * @param senderId 发送者 ID
     * @param receiverId 接收者 ID
     * @param isRead 是否已读
     * @return 未读消息列表
     */
    List<Message> findBySenderIdAndReceiverIdAndIsRead(Long senderId, Long receiverId, boolean isRead);
}
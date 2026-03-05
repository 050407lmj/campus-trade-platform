package com.campus.trade.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息实体类
 * 对应数据库表：messages
 * 
 * 用于存储用户之间的聊天消息记录
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Data                                           // Lombok 注解，自动生成 getter/setter/toString 等方法
@Entity                                         // JPA 实体类注解
@Table(name = "messages")                      // 指定对应的数据库表名
public class Message {

    /**
     * 消息 ID（主键）
     * 自增策略：GenerationType.IDENTITY
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发送者
     * 多对一关系：多个消息可以来自同一个发送者
     * fetch = FetchType.LAZY：懒加载，延迟加载关联对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)  // 外键列名
    private User sender; // 发送者

    /**
     * 接收者
     * 多对一关系：多个消息可以发送给同一个接收者
     * fetch = FetchType.LAZY：懒加载，延迟加载关联对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)  // 外键列名
    private User receiver; // 接收者

    /**
     * 消息内容
     * 约束：非空、最大长度 1000 字符
     */
    @Column(nullable = false, length = 1000)
    private String content; // 消息内容

    /**
     * 是否已读
     * 约束：非空
     * true: 已读，false: 未读
     */
    @Column(nullable = false)
    private Boolean isRead; // 是否已读

    /**
     * 创建时间（消息发送时间）
     * updatable = false 表示该字段在创建后不可更新
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 持久化前的回调方法
     * 在实体保存到数据库之前自动执行
     * 设置创建时间为当前时间，并初始化 isRead 为 false
     */
    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.isRead = false;  // 默认未读
    }
}
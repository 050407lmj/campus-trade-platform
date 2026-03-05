package com.campus.trade.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 交易请求实体类
 * 对应数据库表：trade_requests
 * 
 * 用于存储买家对商品的购买请求记录
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Data                                           // Lombok 注解，自动生成 getter/setter/toString 等方法
@Entity                                         // JPA 实体类注解
@Table(name = "trade_requests")                // 指定对应的数据库表名
public class TradeRequest {

    /**
     * 交易请求ID（主键）
     * 自增策略：GenerationType.IDENTITY
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 买家（想要商品的人）
     * 多对一关系：多个交易请求可以来自同一个买家
     * fetch = FetchType.LAZY：懒加载，延迟加载关联对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)  // 外键列名
    private User buyer; // 买家

    /**
     * 商品
     * 多对一关系：多个交易请求可以针对同一个商品
     * fetch = FetchType.LAZY：懒加载，延迟加载关联对象
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)  // 外键列名
    private Product product; // 商品

    /**
     * 交易请求状态
     * 约束：非空
     * 可选值：
     * - pending: 待处理（等待卖家响应）
     * - accepted: 已同意（卖家同意交易）
     * - rejected: 已拒绝（卖家拒绝交易）
     * - completed: 已完成（交易完成）
     */
    @Column(nullable = false)
    private String status; // 状态：pending(待处理), accepted(已同意), rejected(已拒绝), completed(已完成)

    /**
     * 创建时间（请求发起时间）
     * updatable = false 表示该字段在创建后不可更新
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间（状态变更时间）
     * 可更新
     */
    private LocalDateTime updateTime;

    /**
     * 持久化前的回调方法
     * 在实体保存到数据库之前自动执行
     * 设置创建时间为当前时间，并初始化状态为 pending
     */
    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.status = "pending";  // 默认状态为待处理
    }

    /**
     * 更新前的回调方法
     * 在实体更新到数据库之前自动执行
     * 设置更新时间为当前时间
     */
    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
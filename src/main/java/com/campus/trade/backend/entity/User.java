package com.campus.trade.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表：users
 * 
 * 用于存储平台用户的个人信息，包括账号信息和个人资料
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Data                                           // Lombok 注解，自动生成 getter/setter/toString 等方法
@Entity                                         // JPA 实体类注解
@Table(name = "users")                         // 指定对应的数据库表名
public class User {

    /**
     * 用户 ID（主键）
     * 自增策略：GenerationType.IDENTITY
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     * 约束：非空、唯一、最大长度 50 字符
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码（加密存储）
     * 约束：非空、最大长度 100 字符
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * 微信号（选填）
     * 最大长度 50 字符
     */
    @Column(length = 50)
    private String wechatId;

    /**
     * 微信二维码图片 URL（选填）
     * 最大长度 500 字符
     */
    @Column(length = 500)
    private String wechatQrCodeUrl;

    /**
     * 专业（选填）
     * 最大长度 50 字符
     */
    @Column(length = 50)
    private String major;

    /**
     * 年级（选填）
     * 最大长度 10 字符，如：2022 级
     */
    @Column(length = 10)
    private String grade;

    /**
     * 性别（选填）
     * 最大长度 10 字符，如：男/女
     */
    @Column(length = 10)
    private String gender;

    /**
     * 创建时间
     * updatable = false 表示该字段在创建后不可更新
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 持久化前的回调方法
     * 在实体保存到数据库之前自动执行
     * 设置创建时间为当前时间
     */
    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}
package com.campus.trade.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // ✅ 添加 password 字段
    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String wechatId;

    @Column(length = 500)
    private String wechatQrCodeUrl;

    @Column(length = 50)
    private String major;

    @Column(length = 10)
    private String grade;

    @Column(length = 10)
    private String gender;

    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}
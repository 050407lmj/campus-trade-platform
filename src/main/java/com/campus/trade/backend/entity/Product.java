package com.campus.trade.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // 1. 导入这个注解
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // 商品名称

    @Column(nullable = false, length = 500)
    private String description; // 商品描述

    @Column(nullable = false)
    private Double price; // 商品价格

    @Column(length = 5000)
    private String imageUrl; // 商品图片路径/URL

    @Column(nullable = false)
    private String status; // 商品状态

    // 2. 在这里添加 @JsonIgnoreProperties 注解
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnoreProperties({"products", "tradeRequests", "messages", "createTime", "updateTime"})
    private User seller; // 卖家

    @Column(updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
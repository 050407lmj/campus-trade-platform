package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 商品数据访问层（Repository）
 * 
 * 继承 JpaRepository，提供对 Product 实体的 CRUD 操作和自定义查询方法
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Repository                                    // Spring 仓库组件注解
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 获取所有商品列表，按创建时间倒序排列
     * 
     * @return List<Product> 商品列表，最新的在前
     */
    List<Product> findAllByOrderByCreateTimeDesc();

    /**
     * 根据卖家 ID 获取该卖家的所有商品
     * 按创建时间倒序排列
     * 
     * @param sellerId 卖家 ID
     * @return List<Product> 该卖家的商品列表
     */
    List<Product> findBySellerIdOrderByCreateTimeDesc(Long sellerId);

    /**
     * 根据商品状态筛选商品
     * 按创建时间倒序排列
     * 
     * @param status 商品状态（available/sold/reserved）
     * @return List<Product> 符合条件的商品列表
     */
    List<Product> findByStatusOrderByCreateTimeDesc(String status);
}
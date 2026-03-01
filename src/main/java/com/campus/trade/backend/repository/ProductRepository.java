package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 获取所有商品
    List<Product> findAllByOrderByCreateTimeDesc();

    // 根据卖家ID获取商品
    List<Product> findBySellerIdOrderByCreateTimeDesc(Long sellerId);

    // 根据状态筛选商品
    List<Product> findByStatusOrderByCreateTimeDesc(String status);
}
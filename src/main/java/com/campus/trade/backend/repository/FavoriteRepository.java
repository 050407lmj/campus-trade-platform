package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.Favorite;
import com.campus.trade.backend.entity.User;
import com.campus.trade.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 收藏数据访问层
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * 查找用户对某商品的收藏记录
     */
    Optional<Favorite> findByUserAndProduct(User user, Product product);

    /**
     * 检查用户是否收藏了某商品
     */
    boolean existsByUserAndProduct(User user, Product product);

    /**
     * 获取用户的所有收藏
     */
    List<Favorite> findByUserOrderByCreateTimeDesc(User user);

    /**
     * 获取用户的所有收藏（通过用户ID）
     */
    List<Favorite> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 删除用户对某商品的收藏
     */
    void deleteByUserAndProduct(User user, Product product);

    /**
     * 统计商品的收藏数
     */
    long countByProduct(Product product);
}

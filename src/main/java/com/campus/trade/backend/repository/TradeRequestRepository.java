package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 交易请求数据访问层（Repository）
 * 
 * 继承 JpaRepository，提供对 TradeRequest 实体的 CRUD 操作和自定义查询方法
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Repository                                    // Spring 仓库组件注解
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {

    /**
     * 根据买家 ID 获取该买家的所有交易请求
     * 按创建时间降序排列
     * 
     * @param buyerId 买家 ID
     * @return List<TradeRequest> 交易请求列表
     */
    List<TradeRequest> findByBuyerIdOrderByCreateTimeDesc(Long buyerId);

    /**
     * 根据商品 ID 获取该商品的所有交易请求
     * 按创建时间降序排列
     * 
     * @param productId 商品 ID
     * @return List<TradeRequest> 交易请求列表
     */
    List<TradeRequest> findByProductIdOrderByCreateTimeDesc(Long productId);
}
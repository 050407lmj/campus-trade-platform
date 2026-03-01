package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {

    // 根据买家ID获取交易请求
    List<TradeRequest> findByBuyerIdOrderByCreateTimeDesc(Long buyerId);

    // 根据商品ID获取交易请求
    List<TradeRequest> findByProductIdOrderByCreateTimeDesc(Long productId);
}
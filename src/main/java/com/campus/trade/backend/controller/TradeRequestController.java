package com.campus.trade.backend.controller;

import com.campus.trade.backend.entity.Product;
import com.campus.trade.backend.entity.TradeRequest;
import com.campus.trade.backend.entity.User;
import com.campus.trade.backend.repository.ProductRepository;
import com.campus.trade.backend.repository.TradeRequestRepository;
import com.campus.trade.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易请求控制器（Controller）
 * 
 * 处理与交易请求相关的 HTTP 请求，包括：
 * - 创建交易请求
 * - 获取交易请求列表
 * - 更新交易请求状态
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@RestController                                  // RESTful 控制器注解
@RequestMapping("/api/trade-requests")          // 基础请求路径
public class TradeRequestController {

    @Autowired                                   // 自动依赖注入
    private TradeRequestRepository tradeRequestRepository;  // 交易请求数据访问层

    @Autowired                                   // 自动依赖注入
    private ProductRepository productRepository;         // 商品数据访问层

    @Autowired                                   // 自动依赖注入
    private UserRepository userRepository;             // 用户数据访问层

    /**
     * 创建交易请求
     * 
     * @param requestData 交易请求数据（包含 buyerId、productId）
     * @return Map<String, Object> 创建结果（success、message、requestId）
     */
    @PostMapping                                      // POST 请求映射到 /api/trade-requests
    public Map<String, Object> createTradeRequest(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        try {
            TradeRequest request = new TradeRequest();
            Long buyerId = Long.parseLong(requestData.get("buyerId").toString());
            Long productId = Long.parseLong(requestData.get("productId").toString());

            User buyer = userRepository.findById(buyerId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);

            if (buyer == null || product == null) {
                result.put("success", false);
                result.put("message", "用户或商品不存在");
                return result;
            }

            request.setBuyer(buyer);
            request.setProduct(product);
            request.setStatus("pending");

            tradeRequestRepository.save(request);
            result.put("success", true);
            result.put("message", "请求已发送");
            result.put("requestId", request.getId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取某商品的所有交易请求
     * 
     * @param productId 商品 ID
     * @return List<Map<String, Object>> 交易请求列表，每个请求包含买家和商品信息
     */
    @GetMapping("/product/{productId}")            // GET 请求映射到 /api/trade-requests/product/{productId}
    public List<Map<String, Object>> getRequestsByProduct(@PathVariable Long productId) {
        List<TradeRequest> requests = tradeRequestRepository.findByProductIdOrderByCreateTimeDesc(productId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (TradeRequest request : requests) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", request.getId());
            map.put("status", request.getStatus());
            map.put("createTime", request.getCreateTime());

            // 只返回买家ID和用户名
            if (request.getBuyer() != null) {
                Map<String, Object> buyerMap = new HashMap<>();
                buyerMap.put("id", request.getBuyer().getId());
                buyerMap.put("username", request.getBuyer().getUsername());
                map.put("buyer", buyerMap);
            }

            // 只返回商品ID和名称
            if (request.getProduct() != null) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", request.getProduct().getId());
                productMap.put("name", request.getProduct().getName());
                map.put("product", productMap);
            }

            result.add(map);
        }

        return result;
    }

    /**
     * 更新交易请求状态
     * 
     * @param id 交易请求ID
     * @param statusData 包含新状态的 Map（status 字段）
     * @return Map<String, Object> 更新结果（success、message）
     */
    @PutMapping("/{id}/status")                    // PUT 请求映射到 /api/trade-requests/{id}/status
    public Map<String, Object> updateRequestStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        Map<String, Object> result = new HashMap<>();
        TradeRequest request = tradeRequestRepository.findById(id).orElse(null);
        if (request == null) {
            result.put("success", false);
            result.put("message", "请求不存在");
            return result;
        }
        request.setStatus(statusData.get("status"));
        tradeRequestRepository.save(request);
        result.put("success", true);
        result.put("message", "更新成功");
        return result;
    }
}
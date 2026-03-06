package com.campus.trade.backend.controller;

import com.campus.trade.backend.entity.Product;
import com.campus.trade.backend.entity.User;
import com.campus.trade.backend.repository.ProductRepository;
import com.campus.trade.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器（Controller）
 * 
 * 处理与商品相关的 HTTP 请求，包括：
 * - 发布商品
 * - 获取商品列表
 * - 获取商品详情
 * - 更新商品状态
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@RestController                              // RESTful 控制器注解
@RequestMapping("/api/products")          // 基础请求路径
public class ProductController {

    @Autowired                             // 自动依赖注入
    private ProductRepository productRepository;  // 商品数据访问层

    @Autowired                             // 自动依赖注入
    private UserRepository userRepository;        // 用户数据访问层

    /**
     * 发布商品
     * 
     * @param productData 商品信息（包含 name、description、price、imageUrl、category、sellerId）
     * @return Map<String, Object> 发布结果（success、message、id）
     */
    @PostMapping                                         // POST 请求映射到 /api/products
    public Map<String, Object> createProduct(@RequestBody Map<String, Object> productData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Product product = new Product();
            product.setName((String) productData.get("name"));
            product.setDescription((String) productData.get("description"));
            
            // 安全地处理价格字段，支持数字和字符串类型
            Object priceObj = productData.get("price");
            Double price;
            if (priceObj instanceof Integer) {
                price = ((Integer) priceObj).doubleValue();
            } else if (priceObj instanceof Double) {
                price = (Double) priceObj;
            } else if (priceObj instanceof String) {
                price = Double.parseDouble((String) priceObj);
            } else {
                throw new IllegalArgumentException("价格格式不正确");
            }
            product.setPrice(price);
            
            product.setImageUrl((String) productData.get("imageUrl"));
            product.setCategory((String) productData.get("category"));
            product.setStatus("available");

            // 安全地处理卖家ID字段
            Object sellerIdObj = productData.get("sellerId");
            Long sellerId;
            if (sellerIdObj instanceof Integer) {
                sellerId = ((Integer) sellerIdObj).longValue();
            } else if (sellerIdObj instanceof Long) {
                sellerId = (Long) sellerIdObj;
            } else if (sellerIdObj instanceof String) {
                sellerId = Long.parseLong((String) sellerIdObj);
            } else {
                throw new IllegalArgumentException("卖家ID格式不正确");
            }
            
            User seller = userRepository.findById(sellerId).orElse(null);
            if (seller == null) {
                result.put("success", false);
                result.put("message", "卖家不存在");
                return result;
            }
            product.setSeller(seller);

            productRepository.save(product);
            result.put("success", true);
            result.put("message", "发布成功");
            result.put("id", product.getId()); // 兼容前端判断逻辑
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布失败：" + e.getMessage());
            e.printStackTrace(); // 添加日志用于调试
        }
        return result;
    }

    /**
     * 获取所有商品列表
     * 
     * @return List<Map<String, Object>> 商品列表，每个商品包含基本信息和卖家信息
     */
    @GetMapping                                      // GET 请求映射到 /api/products
    public List<Map<String, Object>> getAllProducts() {
        List<Product> products = productRepository.findAllByOrderByCreateTimeDesc();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Product product : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("name", product.getName());
            map.put("description", product.getDescription());
            map.put("price", product.getPrice());
            map.put("imageUrl", product.getImageUrl());
            map.put("status", product.getStatus());
            map.put("category", product.getCategory());
            map.put("createTime", product.getCreateTime());

            // 手动获取卖家信息，只放用户名和ID
            if (product.getSeller() != null) {
                Map<String, Object> sellerMap = new HashMap<>();
                sellerMap.put("id", product.getSeller().getId());
                sellerMap.put("username", product.getSeller().getUsername());
                map.put("seller", sellerMap);
            } else {
                map.put("seller", null);
            }

            result.add(map);
        }

        return result;
    }
    
    /**
     * 根据 ID 获取商品详情
     * 
     * @param id 商品 ID
     * @return Map<String, Object> 商品详情（success、data 或 message）
     */
    @GetMapping("/{id}")                              // GET 请求映射到 /api/products/{id}
    public Map<String, Object> getProductById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                result.put("success", false);
                result.put("message", "商品不存在");
                return result;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("name", product.getName());
            map.put("description", product.getDescription());
            map.put("price", product.getPrice());
            map.put("imageUrl", product.getImageUrl());
            map.put("status", product.getStatus());
            map.put("category", product.getCategory());
            map.put("createTime", product.getCreateTime());

            if (product.getSeller() != null) {
                Map<String, Object> sellerMap = new HashMap<>();
                sellerMap.put("id", product.getSeller().getId());
                sellerMap.put("username", product.getSeller().getUsername());
                map.put("seller", sellerMap);
            } else {
                map.put("seller", null);
            }

            result.put("success", true);
            result.put("data", map);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取商品详情失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新商品状态
     * 
     * @param id 商品 ID
     * @param statusData 包含新状态的 Map（status 字段）
     * @return Map<String, Object> 更新结果（success、message）
     */
    @PutMapping("/{id}/status")                       // PUT 请求映射到 /api/products/{id}/status
    public Map<String, Object> updateProductStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        Map<String, Object> result = new HashMap<>();
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            result.put("success", false);
            result.put("message", "商品不存在");
            return result;
        }
        product.setStatus(statusData.get("status"));
        productRepository.save(product);
        result.put("success", true);
        result.put("message", "更新成功");
        return result;
    }

    /**
     * 获取用户自己发布的商品列表
     * 
     * @param sellerId 卖家ID
     * @return List<Map<String, Object>> 商品列表
     */
    @GetMapping("/my/{sellerId}")
    public List<Map<String, Object>> getMyProducts(@PathVariable Long sellerId) {
        List<Product> products = productRepository.findBySellerIdOrderByCreateTimeDesc(sellerId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Product product : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("name", product.getName());
            map.put("description", product.getDescription());
            map.put("price", product.getPrice());
            map.put("imageUrl", product.getImageUrl());
            map.put("status", product.getStatus());
            map.put("category", product.getCategory());
            map.put("createTime", product.getCreateTime());
            result.add(map);
        }

        return result;
    }

    /**
     * 删除商品（下架）
     * 
     * @param id 商品 ID
     * @return Map<String, Object> 删除结果
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            result.put("success", false);
            result.put("message", "商品不存在");
            return result;
        }
        productRepository.delete(product);
        result.put("success", true);
        result.put("message", "商品已下架");
        return result;
    }
}
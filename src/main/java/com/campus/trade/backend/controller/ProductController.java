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

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // 发布商品
    @PostMapping
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
            result.put("productId", product.getId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布失败：" + e.getMessage());
            e.printStackTrace(); // 添加日志用于调试
        }
        return result;
    }

    // 获取所有商品（修改版：避免循环引用）
    @GetMapping
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

    // 根据ID获取商品（修改版：避免循环引用）
    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", product.getId());
        map.put("name", product.getName());
        map.put("description", product.getDescription());
        map.put("price", product.getPrice());
        map.put("imageUrl", product.getImageUrl());
        map.put("status", product.getStatus());

        if (product.getSeller() != null) {
            Map<String, Object> sellerMap = new HashMap<>();
            sellerMap.put("id", product.getSeller().getId());
            sellerMap.put("username", product.getSeller().getUsername());
            map.put("seller", sellerMap);
        } else {
            map.put("seller", null);
        }

        return map;
    }

    // 更新商品状态
    @PutMapping("/{id}/status")
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
}
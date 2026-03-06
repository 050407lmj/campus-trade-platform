package com.campus.trade.backend.controller;

import com.campus.trade.backend.entity.Favorite;
import com.campus.trade.backend.entity.Product;
import com.campus.trade.backend.entity.User;
import com.campus.trade.backend.repository.FavoriteRepository;
import com.campus.trade.backend.repository.ProductRepository;
import com.campus.trade.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 * 
 * 处理商品收藏相关的请求
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 添加收藏
     */
    @PostMapping("/{userId}/{productId}")
    public Map<String, Object> addFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }
            
            if (product == null) {
                result.put("success", false);
                result.put("message", "商品不存在");
                return result;
            }
            
            // 检查是否已收藏
            if (favoriteRepository.existsByUserAndProduct(user, product)) {
                result.put("success", false);
                result.put("message", "已收藏该商品");
                return result;
            }
            
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            favoriteRepository.save(favorite);
            
            result.put("success", true);
            result.put("message", "收藏成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "收藏失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{userId}/{productId}")
    public Map<String, Object> removeFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            
            if (user == null || product == null) {
                result.put("success", false);
                result.put("message", "用户或商品不存在");
                return result;
            }
            
            favoriteRepository.deleteByUserAndProduct(user, product);
            
            result.put("success", true);
            result.put("message", "已取消收藏");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消收藏失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{userId}/{productId}")
    public Map<String, Object> checkFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            
            boolean isFavorited = false;
            if (user != null && product != null) {
                isFavorited = favoriteRepository.existsByUserAndProduct(user, product);
            }
            
            result.put("success", true);
            result.put("isFavorited", isFavorited);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败");
        }
        
        return result;
    }

    /**
     * 获取用户的收藏列表
     */
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserFavorites(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserIdOrderByCreateTimeDesc(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Favorite favorite : favorites) {
            Product product = favorite.getProduct();
            if (product != null && !"offline".equals(product.getStatus())) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", product.getId());
                item.put("name", product.getName());
                item.put("description", product.getDescription());
                item.put("price", product.getPrice());
                item.put("imageUrl", product.getImageUrl());
                item.put("status", product.getStatus());
                item.put("category", product.getCategory());
                item.put("createTime", product.getCreateTime());
                item.put("favoriteId", favorite.getId());
                item.put("favoriteTime", favorite.getCreateTime());
                
                if (product.getSeller() != null) {
                    Map<String, Object> sellerMap = new HashMap<>();
                    sellerMap.put("id", product.getSeller().getId());
                    sellerMap.put("username", product.getSeller().getUsername());
                    item.put("seller", sellerMap);
                }
                
                result.add(item);
            }
        }
        
        return result;
    }

    /**
     * 获取商品收藏数
     */
    @GetMapping("/count/{productId}")
    public Map<String, Object> getFavoriteCount(@PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                result.put("success", false);
                result.put("count", 0);
                return result;
            }
            
            long count = favoriteRepository.countByProduct(product);
            result.put("success", true);
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("count", 0);
        }
        
        return result;
    }
}

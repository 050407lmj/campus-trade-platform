package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 用户数据访问层（Repository）
 * 
 * 继承 JpaRepository，提供对 User 实体的 CRUD 操作
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Repository                                    // Spring 仓库组件注解
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * 主要用于登录验证
     * 
     * @param username 用户名
     * @return Optional<User> 如果找到则返回包含用户的 Optional，否则返回空 Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     * 主要用于注册时验证用户名唯一性
     * 
     * @param username 用户名
     * @return boolean 如果用户名已存在返回 true，否则返回 false
     */
    boolean existsByUsername(String username);
}
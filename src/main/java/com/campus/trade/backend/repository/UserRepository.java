package com.campus.trade.backend.repository;

import com.campus.trade.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查找用户（用于登录）
    Optional<User> findByUsername(String username);

    // 检查用户名是否存在
    boolean existsByUsername(String username);
}
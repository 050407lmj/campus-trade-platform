package com.campus.trade.backend.controller;

import com.campus.trade.backend.entity.User;
import com.campus.trade.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 用户注册
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        // ✅ 验证密码不为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }

        userRepository.save(user);
        result.put("success", true);
        result.put("message", "注册成功");
        result.put("userId", user.getId());
        return result;
    }

    // 用户登录
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> result = new HashMap<>();
        String username = loginData.get("username");
        String password = loginData.get("password");  // ✅ 获取密码

        // ✅ 验证密码不为空
        if (password == null || password.isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // ✅ 验证密码是否正确
            if (!password.equals(user.getPassword())) {
                result.put("success", false);
                result.put("message", "密码错误");
                return result;
            }

            // ✅ 登录成功，不返回密码
            user.setPassword(null);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
        }
        return result;
    }

    // 获取所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 根据 ID 获取用户
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPassword(null);  // ✅ 不返回密码
        }
        return user;
    }
}
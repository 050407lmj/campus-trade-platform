package com.campus.trade.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域资源共享（CORS）配置
 * 
 * 解决前后端分离架构中的跨域访问问题
 * 允许前端应用（http://localhost:5173）访问后端 API
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@Configuration                              // Spring 配置类注解
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 添加跨域映射配置
     * 
     * @param registry CorsRegistry 用于注册跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                                      // 允许所有路径跨域
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")  // 允许的前端域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")         // 允许的 HTTP 方法
                .allowedHeaders("*")                                             // 允许所有请求头
                .allowCredentials(true)                                          // 允许携带 Cookie/凭证
                .maxAge(3600);                                                   // 预检请求缓存时间（秒）
    }
}

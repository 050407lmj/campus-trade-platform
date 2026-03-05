package com.campus.trade.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园交易平台 - 主启动类
 * 
 * 这是 Spring Boot 应用的入口点
 * 使用 @SpringBootApplication 注解自动配置和组件扫描
 * 
 * @author Campus Trade Platform
 * @version 1.0
 */
@SpringBootApplication                        // Spring Boot 核心注解，包含@Configuration、@EnableAutoConfiguration、@ComponentScan
public class CampusTradePlatformApplication {

    /**
     * 主方法 - 应用启动入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CampusTradePlatformApplication.class, args);
    }

}

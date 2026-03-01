# 🏪 校园交易平台

基于 Spring Boot 3.x + Vue 3 + WebSocket 的校园二手交易平台，支持商品发布、浏览、实时聊天、交易请求等功能。

## 🛠️ 技术栈

### 后端
- **Java 21** - 编程语言
- **Spring Boot 3.5.11** - 应用框架
- **Spring Data JPA** - 数据持久层
- **Spring WebSocket** - 实时通信
- **MySQL 8.0** - 数据库
- **Lombok** - 简化代码
- **Maven** - 项目管理

### 前端
- **Vue 3** (Composition API) - 前端框架
- **Vite 7.3.1** - 构建工具
- **Element Plus 2.13.2** - UI 组件库
- **Vue Router 4.6.4** - 路由管理
- **Pinia 3.0.4** - 状态管理
- **Axios 1.13.5** - HTTP 客户端
- **@element-plus/icons-vue** - 图标库

## 📁 项目结构

```
campus-trade-platform/
├── src/main/java/com/campus/trade/backend/
│   ├── CampusTradePlatformApplication.java  # 启动类
│   ├── config/                              # 配置类
│   │   ├── WebSocketConfig.java           # WebSocket 配置
│   │   └── WebSocketChatHandler.java      # WebSocket 消息处理
│   ├── controller/                         # 控制器层
│   │   ├── UserController.java           # 用户控制器
│   │   ├── ProductController.java        # 商品控制器
│   │   ├── MessageController.java         # 消息控制器
│   │   └── TradeRequestController.java    # 交易请求控制器
│   ├── entity/                             # 实体类
│   │   ├── User.java                      # 用户实体
│   │   ├── Product.java                   # 商品实体
│   │   ├── Message.java                   # 消息实体
│   │   └── TradeRequest.java              # 交易请求实体
│   └── repository/                         # 数据访问层
│       ├── UserRepository.java
│       ├── ProductRepository.java
│       ├── MessageRepository.java
│       └── TradeRequestRepository.java
├── src/main/resources/
│   └── application.properties              # 应用配置
├── frontend/                               # 前端项目
│   ├── src/
│   │   ├── api/                           # API 接口封装
│   │   │   ├── request.js                 # Axios 配置
│   │   │   ├── user.js                    # 用户 API
│   │   │   ├── product.js                 # 商品 API
│   │   │   ├── message.js                 # 消息 API
│   │   │   └── trade.js                   # 交易 API
│   │   ├── router/
│   │   │   └── index.js                   # 路由配置
│   │   ├── views/                         # 页面组件
│   │   │   ├── Login.vue                  # 登录页
│   │   │   ├── Register.vue               # 注册页
│   │   │   ├── Home.vue                   # 首页
│   │   │   ├── ProductDetail.vue          # 商品详情
│   │   │   ├── Publish.vue                # 发布商品
│   │   │   └── Chat.vue                   # 聊天页
│   │   ├── App.vue                        # 根组件
│   │   └── main.js                        # 入口文件
│   ├── package.json                       # 依赖配置
│   └── vite.config.js                     # Vite 配置
├── pom.xml                                # Maven 配置
└── README.md                              # 项目文档
```

## 🚀 快速启动

### 环境要求
- **JDK 21+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Maven 3.6+**

### 数据库配置
```sql
CREATE DATABASE campus_trade CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.properties` 中的数据库连接信息：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/campus_trade?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=你的密码
```

### 启动后端
```bash
# 在项目根目录执行
mvn spring-boot:run
```
或在 IDEA 中运行 `CampusTradePlatformApplication.java`

后端服务启动在：http://localhost:8080

### 启动前端
```bash
cd frontend
npm install
npm run dev
```

前端服务启动在：http://localhost:5173

## ✨ 核心功能

| 模块 | 功能 | 状态 |
|------|------|------|
| 用户模块 | 注册、登录、个人信息管理 | ✅ |
| 商品模块 | 发布、列表、详情、图片展示 | ✅ |
| 聊天模块 | WebSocket 实时通信 | ✅ |
| 交易模块 | 购买请求、状态管理 | ✅ |

## 🗄️ 数据库设计

### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    wechat_id VARCHAR(50),
    wechat_qr_code_url VARCHAR(500),
    major VARCHAR(50),
    grade VARCHAR(10),
    gender VARCHAR(10),
    create_time DATETIME NOT NULL
);
```

### 商品表 (products)
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price DOUBLE NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    seller_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);
```

### 消息表 (messages)
```sql
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);
```

### 交易请求表 (trade_requests)
```sql
CREATE TABLE trade_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    create_time DATETIME NOT NULL,
    update_time DATETIME,
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## 🔌 API 接口

### 用户相关
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/{id}` - 获取用户信息

### 商品相关
- `GET /api/products` - 获取商品列表
- `POST /api/products` - 发布商品
- `GET /api/products/{id}` - 获取商品详情

### 消息相关
- `GET /api/messages/{userId}` - 获取消息列表
- `POST /api/messages` - 发送消息

### 交易相关
- `POST /api/trade-requests` - 创建交易请求
- `PUT /api/trade-requests/{id}/status` - 更新请求状态

### WebSocket 连接
```
ws://localhost:8080/ws/chat?userId={用户ID}
```

## 🌟 项目亮点

1. **实时通信** - 基于 WebSocket 的即时聊天功能，支持消息实时推送
2. **前后端分离** - RESTful API 设计，清晰的架构分层
3. **响应式设计** - Element Plus UI 组件，良好的用户体验
4. **数据完整性** - JPA 实体关系映射，保证数据一致性
5. **跨域支持** - 完善的 CORS 配置，支持前后端分离部署

## 📸 功能截图

（此处可添加系统截图）

- 登录注册页面
- 商品列表页面
- 商品详情页面
- 发布商品页面
- 实时聊天页面

## 🔧 开发说明

### 后端技术要点
- 使用 Spring Data JPA 进行数据持久化
- WebSocket 配置支持跨域连接
- 统一的异常处理和响应格式
- Lombok 简化实体类代码

### 前端技术要点
- Vue 3 Composition API
- Element Plus 组件库
- Axios 请求拦截和响应处理
- Vue Router 路由懒加载
- Pinia 状态管理

## 🚧 部署说明

### 后端部署
```bash
# 打包
mvn clean package

# 运行
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
# 构建
cd frontend
npm run build

# 构建产物在 dist/ 目录
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👨‍💻 开发者信息

- **项目名称**：校园交易平台
- **开发时间**：2026年2月
- **技术栈**：Spring Boot + Vue 3 + MySQL + WebSocket
- **项目类型**：毕业设计/全栈开发项目

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件
- 微信交流

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！

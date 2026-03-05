# IntelliJ IDEA 启动指南

## 🚀 使用 IntelliJ IDEA 运行校园交易平台

### 前提条件
确保已安装以下工具：
- ✅ JDK 21（推荐 Eclipse Temurin 或 Oracle JDK）
- ✅ Node.js 18+
- ✅ MySQL 8.0
- ✅ IntelliJ IDEA 2023.3+（推荐 Ultimate 版，支持 Spring Boot）

---

## 📋 步骤一：配置 IDEA

### 1. 打开项目
```
File → Open → 选择 campus-trade-platform 目录
```

### 2. 等待 Maven 导入完成
IDEA 会自动识别 `pom.xml` 并下载依赖，等待右下角进度条完成。

### 3. 检查项目结构
确保项目结构正确：
- **后端**: `src/main/java/com/campus/trade/backend`
- **前端**: `frontend/`

---

## ⚙️ 步骤二：配置数据库

### 1. 创建数据库
在 MySQL 中执行：
```sql
CREATE DATABASE campus_trade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置文件位置
编辑 `src/main/resources/application.properties`：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/campus_trade?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=你的密码
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA 配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# 服务器端口
server.port=8080

# WebSocket 配置
spring.websocket.enabled=true
```

**⚠️ 重要：** 修改 `username` 和 `password` 为您的 MySQL 实际配置！

---

## ▶️ 步骤三：启动后端（IDEA）

### 方法一：直接运行主类（推荐）
1. 找到主类：`src/main/java/com/campus/trade/backend/CampusTradePlatformApplication.java`
2. 右键点击文件中的 `main` 方法
3. 选择 **"Run 'CampusTradePlatformApplication.main()'"**

或者点击右上角的绿色运行按钮 ▶️

### 方法二：使用 Maven 运行
在 IDEA 的 Terminal 中执行：
```bash
mvn spring-boot:run
```

### 成功标志
看到以下日志表示启动成功：
```
Started CampusTradePlatformApplication in X seconds
Tomcat started on port 8080 (http) with context path '/'
```

访问测试：http://localhost:8080/api/users

---

## 🌐 步骤四：启动前端（IDEA 内置终端）

### 1. 打开 IDEA Terminal
底部工具栏 → **Terminal** 或使用快捷键 `Alt + F12`

### 2. 进入前端目录并启动
```bash
cd frontend
npm install      # 首次运行需要执行
npm run dev
```

### 成功标志
看到以下输出表示启动成功：
```
VITE v7.x.x  ready in xxx ms
➜  Local:   http://localhost:5173/
```

**注意：** 如果 5173 端口被占用，Vite 会自动使用 5174、5175 等后续端口。

---

## 🔧 IDEA 优化配置

### 1. 启用热部署（DevTools）
IDEA 已自动检测并使用 DevTools，代码修改后会自动重启应用。

### 2. 调整编译设置
```
File → Settings → Build, Execution, Deployment → Compiler
→ 勾选 "Build project automatically"
```

### 3. 配置 Lombok 插件
```
File → Settings → Plugins
→ 搜索 "Lombok" 并安装
→ 重启 IDEA
```

### 4. 设置编码为 UTF-8
```
File → Settings → Editor → File Encodings
→ Project Encoding: UTF-8
→ Default encoding for properties files: UTF-8
```

---

## 🎯 快速启动流程

### 每次开发的启动顺序：

**1. 启动 MySQL**
```bash
# Windows 服务启动
net start mysql80
```

**2. 启动 IDEA**
打开项目，等待 Maven 导入完成

**3. 启动后端**
右键运行 `CampusTradePlatformApplication.java` 的 main 方法

**4. 启动前端**
在 IDEA Terminal 中：
```bash
cd frontend
npm run dev
```

---

## 🧪 功能测试

### 1. 访问应用
浏览器打开：http://localhost:5173 （或终端显示的端口）

### 2. 注册账号
- 点击"立即注册"
- 填写用户名、密码等信息
- 提交后自动跳转到登录页

### 3. 登录系统
- 输入用户名和密码
- 点击登录
- 成功后跳转到首页

### 4. 发布商品
- 点击右上角"发布商品"按钮
- 填写商品信息（名称、价格、分类、描述）
- 上传图片（可选）
- 点击"立即发布"

### 5. 查看商品
- 首页会显示刚发布的商品
- 可以点击查看详情

---

## 🛠️ 常见问题解决

### ❌ 端口 8080 被占用
**解决方案 1：** 停止占用的进程
```bash
# PowerShell 管理员权限
netstat -ano | findstr :8080
taskkill /F /PID <进程 ID>
```

**解决方案 2：** 修改端口
编辑 `application.properties`：
```properties
server.port=8081
```

### ❌ 前端无法连接后端
检查点：
1. 后端是否启动成功（查看 IDEA 控制台日志）
2. 端口是否正确（默认 8080）
3. 数据库是否连接成功

### ❌ npm 命令失败
```bash
# 清除缓存
npm cache clean --force

# 重新安装依赖
rm -rf node_modules package-lock.json
npm install
```

### ❌ IDEA 提示 "Java 版本不匹配"
确保 IDEA 使用的 JDK 是 21：
```
File → Project Structure → Project → SDK: 选择 JDK 21
File → Settings → Build, Execution, Deployment → Compiler → Java Compiler
→ Target bytecode version: 21
```

---

## 📝 开发技巧

### 1. 调试模式
运行后端时选择 **"Debug"** 而不是 **"Run"**，可以设置断点调试。

### 2. 查看日志
IDEA 控制台会实时显示 Spring Boot 日志，包括：
- HTTP 请求日志
- SQL 语句（已开启 show-sql）
- WebSocket 连接状态

### 3. 前端热重载
Vite 支持热重载，修改 Vue 组件后浏览器会自动刷新。

### 4. 数据库查看
使用 IDEA 的 Database 工具窗口：
```
View → Tool Windows → Database
→ 添加 MySQL 连接
→ 查看表结构和数据
```

---

## 📦 打包部署

### 生产环境打包
```bash
# 在 IDEA Terminal 执行
mvn clean package -DskipTests
```

生成的 JAR 文件位置：
```
target/backend-0.0.1-SNAPSHOT.jar
```

### 运行打包后的应用
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

---

## ✅ 检查清单

每次启动前确认：
- [ ] MySQL 服务已启动
- [ ] 数据库配置正确（application.properties）
- [ ] JDK 版本为 21
- [ ] Node.js 版本为 18+
- [ ] IDEA 已安装 Lombok 插件
- [ ] 端口 8080 和 5173 未被占用

---

## 🎉 完成！

现在您可以完全使用 IntelliJ IDEA 进行开发和运行了！

**下一步建议：**
1. 测试完整的用户流程（注册 → 登录 → 发布商品）
2. 尝试使用 WebSocket 聊天功能
3. 查看代码注释了解各模块功能
4. 开始您的功能开发或修改

如有问题，请查看 IDEA 控制台的错误日志！

#!/bin/bash

# 腾讯云校园交易平台部署脚本
# 使用方法：chmod +x deploy.sh && ./deploy.sh

echo "========================================="
echo "  校园交易平台 - 自动化部署脚本"
echo "========================================="

# 1. 停止现有服务
echo "[1/8] 停止现有服务..."
pkill -f 'campus-trade-platform' || true
systemctl stop nginx || true

# 2. 拉取最新代码
echo "[2/8] 拉取最新代码..."
cd /opt/campus-trade-platform
git pull origin main

# 3. 构建后端
echo "[3/8] 构建后端项目..."
cd /opt/campus-trade-platform
mvn clean package -DskipTests

# 4. 启动后端服务
echo "[4/8] 启动后端服务..."
nohup java -jar target/backend-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=prod \
    --server.port=8080 \
    > logs/application.log 2>&1 &

echo "后端服务启动中... (等待 5 秒)"
sleep 5

# 5. 构建前端
echo "[5/8] 构建前端项目..."
cd /opt/campus-trade-platform/frontend
npm install
npm run build

# 6. 配置 Nginx
echo "[6/8] 配置 Nginx..."
cat > /etc/nginx/conf.d/campus-trade.conf << 'EOF'
server {
    listen 80;
    server_name your-server-ip;  # 替换为你的服务器 IP
    
    # 前端静态文件
    location / {
        root /opt/campus-trade-platform/frontend/dist;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端 API 代理
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket 支持
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
    
    # WebSocket 路径
    location /ws/ {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
    }
}
EOF

# 7. 启动 Nginx
echo "[7/8] 启动 Nginx..."
systemctl start nginx
systemctl enable nginx

# 8. 检查服务状态
echo "[8/8] 检查服务状态..."
echo ""
echo "=== 后端服务状态 ==="
ps aux | grep campus-trade-platform | grep -v grep

echo ""
echo "=== Nginx 状态 ==="
systemctl status nginx --no-pager

echo ""
echo "========================================="
echo "  部署完成！"
echo "========================================="
echo ""
echo "访问地址：http://your-server-ip"
echo "后端 API: http://your-server-ip:8080/api"
echo ""
echo "日志查看："
echo "  后端日志：tail -f /opt/campus-trade-platform/logs/application.log"
echo "  Nginx 日志：tail -f /var/log/nginx/error.log"
echo ""

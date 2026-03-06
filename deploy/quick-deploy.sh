#!/bin/bash

# 校园二手交易平台 - 一键部署脚本
# 在服务器上运行此脚本

set -e

echo "=========================================="
echo "  校园二手交易平台 - 一键部署"
echo "=========================================="

# 安装 Docker
if ! command -v docker &> /dev/null; then
    echo ">>> 安装 Docker..."
    curl -fsSL https://get.docker.com | bash
    systemctl start docker
    systemctl enable docker
    echo "Docker 安装完成"
fi

# 安装 Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo ">>> 安装 Docker Compose..."
    curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    echo "Docker Compose 安装完成"
fi

# 创建项目目录
echo ">>> 创建项目目录..."
mkdir -p /opt/campus-trade/uploads
cd /opt/campus-trade

# 停止旧容器
echo ">>> 停止旧容器..."
docker-compose down 2>/dev/null || true

# 启动新容器
echo ">>> 启动容器..."
docker-compose up -d

# 等待服务启动
echo ">>> 等待服务启动..."
sleep 15

# 检查服务状态
echo ""
echo ">>> 服务状态:"
docker-compose ps

# 显示访问地址
PUBLIC_IP=$(curl -s ifconfig.me || echo "115.159.125.42")

echo ""
echo "=========================================="
echo "  部署完成!"
echo "=========================================="
echo ""
echo "  访问地址: http://${PUBLIC_IP}"
echo "  或者:     http://115.159.125.42"
echo ""
echo "  查看日志: docker-compose logs -f"
echo "  重启服务: docker-compose restart"
echo "  停止服务: docker-compose down"
echo ""
echo "=========================================="

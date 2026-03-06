#!/bin/bash

# 校园二手交易平台 - 服务器部署脚本
# 适用于腾讯云 CVM (Ubuntu 22.04 / CentOS 8)

set -e

echo "=========================================="
echo "  校园二手交易平台 - 自动部署脚本"
echo "=========================================="

# 检测操作系统
if [ -f /etc/os-release ]; then
    . /etc/os-release
    OS=$ID
else
    echo "无法检测操作系统"
    exit 1
fi

echo "检测到操作系统: $OS"

# 步骤 1: 安装 Docker
install_docker() {
    echo ""
    echo ">>> 步骤 1: 安装 Docker..."
    
    if command -v docker &> /dev/null; then
        echo "Docker 已安装，跳过..."
        return
    fi
    
    if [ "$OS" = "ubuntu" ]; then
        apt-get update
        apt-get install -y ca-certificates curl gnupg
        curl -fsSL https://get.docker.com | bash
        usermod -aG docker $USER
    elif [ "$OS" = "centos" ]; then
        yum install -y yum-utils
        yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
        yum install -y docker-ce docker-ce-cli containerd.io
        systemctl start docker
        systemctl enable docker
    fi
    
    echo "Docker 安装完成!"
}

# 步骤 2: 安装 Docker Compose
install_docker_compose() {
    echo ""
    echo ">>> 步骤 2: 安装 Docker Compose..."
    
    if command -v docker-compose &> /dev/null; then
        echo "Docker Compose 已安装，跳过..."
        return
    fi
    
    curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    
    echo "Docker Compose 安装完成!"
}

# 步骤 3: 创建项目目录
create_directories() {
    echo ""
    echo ">>> 步骤 3: 创建项目目录..."
    
    mkdir -p /opt/campus-trade
    mkdir -p /opt/campus-trade/uploads
    
    echo "项目目录创建完成!"
}

# 步骤 4: 配置防火墙
configure_firewall() {
    echo ""
    echo ">>> 步骤 4: 配置防火墙..."
    
    if command -v ufw &> /dev/null; then
        # Ubuntu 使用 ufw
        ufw allow 22/tcp
        ufw allow 80/tcp
        ufw allow 443/tcp
        ufw allow 8080/tcp
        echo "ufw 防火墙已配置"
    elif command -v firewall-cmd &> /dev/null; then
        # CentOS 使用 firewalld
        systemctl start firewalld
        firewall-cmd --permanent --add-port=22/tcp
        firewall-cmd --permanent --add-port=80/tcp
        firewall-cmd --permanent --add-port=443/tcp
        firewall-cmd --permanent --add-port=8080/tcp
        firewall-cmd --reload
        echo "firewalld 防火墙已配置"
    else
        echo "未检测到防火墙，请手动配置开放端口: 22, 80, 443, 8080"
    fi
}

# 步骤 5: 启动服务
start_services() {
    echo ""
    echo ">>> 步骤 5: 启动服务..."
    
    cd /opt/campus-trade
    
    # 停止旧容器
    docker-compose down 2>/dev/null || true
    
    # 启动新容器
    docker-compose up -d
    
    echo "服务启动中..."
    sleep 10
    
    # 检查服务状态
    docker-compose ps
}

# 显示部署信息
show_info() {
    echo ""
    echo "=========================================="
    echo "  部署完成!"
    echo "=========================================="
    echo ""
    echo "请将以下文件上传到 /opt/campus-trade/ 目录："
    echo "  1. dist/         (前端构建文件)"
    echo "  2. backend.jar   (后端 JAR 包)"
    echo "  3. nginx.conf    (Nginx 配置)"
    echo "  4. docker-compose.yml"
    echo ""
    echo "然后在服务器执行："
    echo "  cd /opt/campus-trade"
    echo "  docker-compose up -d"
    echo ""
    echo "访问地址: http://服务器IP"
    echo "=========================================="
}

# 主函数
main() {
    install_docker
    install_docker_compose
    create_directories
    configure_firewall
    show_info
}

main

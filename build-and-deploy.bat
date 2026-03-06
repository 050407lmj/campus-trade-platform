@echo off
chcp 65001 >nul
echo ==========================================
echo   校园二手交易平台 - 构建脚本
echo ==========================================
echo.

:: 设置服务器信息
set SERVER_IP=115.159.125.42
set SERVER_USER=root
set DEPLOY_PATH=/opt/campus-trade

echo 请先编辑此脚本，设置服务器信息：
echo   SERVER_IP: %SERVER_IP%
echo   SERVER_USER: %SERVER_USER%
echo.
pause

:: 步骤 1: 构建前端
echo.
echo ^>^>^> 步骤 1: 构建前端...
cd frontend
call npm run build
if %errorlevel% neq 0 (
    echo 前端构建失败!
    pause
    exit /b 1
)
cd ..
echo 前端构建完成!

:: 步骤 2: 构建后端
echo.
echo ^>^>^> 步骤 2: 构建后端...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo 后端构建失败!
    pause
    exit /b 1
)
echo 后端构建完成!

:: 步骤 3: 准备部署文件
echo.
echo ^>^>^> 步骤 3: 准备部署文件...
if not exist "deploy" mkdir deploy
xcopy /E /I /Y frontend\dist deploy\dist
copy /Y target\backend-*.jar deploy\backend.jar
copy /Y docker-compose.yml deploy\
copy /Y nginx.conf deploy\
copy /Y deploy-server.sh deploy\
echo 部署文件准备完成!

:: 步骤 4: 上传到服务器
echo.
echo ^>^>^> 步骤 4: 上传到服务器...
echo 请确保已配置 SSH 密钥或准备好密码
echo.
pause

:: 使用 scp 上传文件
scp -r deploy\* %SERVER_USER%@%SERVER_IP%:%DEPLOY_PATH%/

if %errorlevel% neq 0 (
    echo 上传失败! 请检查服务器连接
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   构建和上传完成!
echo ==========================================
echo.
echo 请在服务器上执行以下命令启动服务:
echo   cd %DEPLOY_PATH%
echo   chmod +x deploy-server.sh
echo   ./deploy-server.sh
echo   docker-compose up -d
echo.
echo 访问地址: http://%SERVER_IP%
echo ==========================================
pause

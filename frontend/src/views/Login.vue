<template>
  <div class="login-container">
    <!-- 左侧装饰区域 -->
    <div class="login-decoration">
      <div class="decoration-content">
        <!-- 动态背景图案 -->
        <div class="pattern-bg">
          <div class="pattern-circle"></div>
          <div class="pattern-circle"></div>
          <div class="pattern-circle"></div>
        </div>
        
        <!-- 主视觉 -->
        <div class="hero-content">
          <div class="hero-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M3 9L12 2L21 9V20C21 20.5304 20.7893 21.0391 20.4142 21.4142C20.0391 21.7893 19.5304 22 19 22H5C4.46957 22 3.96086 21.7893 3.58579 21.4142C3.21071 21.0391 3 20.5304 3 20V9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M9 22V12H15V22" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h1 class="hero-title">校园交易平台</h1>
          <p class="hero-subtitle">发现校园生活 · 连接同学需求</p>
          
          <!-- 特性列表 -->
          <div class="features-list">
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
              </div>
              <span>安全可靠的交易环境</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
              </div>
              <span>便捷的校内线下交易</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
              </div>
              <span>实时聊天沟通</span>
            </div>
          </div>
        </div>
        
        <!-- 底部装饰 -->
        <div class="decoration-shapes">
          <div class="shape shape-1"></div>
          <div class="shape shape-2"></div>
          <div class="shape shape-3"></div>
        </div>
      </div>
    </div>
    
    <!-- 右侧登录表单 -->
    <div class="login-form-section">
      <div class="form-wrapper">
        <!-- 移动端标题 -->
        <div class="mobile-header">
          <div class="mobile-logo">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M3 9L12 2L21 9V20C21 20.5304 20.7893 21.0391 20.4142 21.4142C20.0391 21.7893 19.5304 22 19 22H5C4.46957 22 3.96086 21.7893 3.58579 21.4142C3.21071 21.0391 3 20.5304 3 20V9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M9 22V12H15V22" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h1 class="mobile-title">校园交易平台</h1>
          <p class="mobile-subtitle">欢迎回来，请登录</p>
        </div>
        
        <!-- 登录卡片 -->
        <div class="login-card">
          <div class="card-header desktop-only">
            <h2 class="card-title">登录账号</h2>
            <p class="card-subtitle">请输入您的账号信息</p>
          </div>
          
          <el-form
              :model="loginForm"
              :rules="rules"
              ref="loginFormRef"
              class="login-form"
          >
            <el-form-item prop="username">
              <div class="form-field">
                <label class="field-label">用户名</label>
                <div class="input-wrapper" :class="{ 'has-value': loginForm.username }">
                  <el-icon class="input-icon"><User /></el-icon>
                  <el-input
                      v-model="loginForm.username"
                      placeholder="请输入用户名"
                      class="custom-input"
                      @keyup.enter="handleLogin"
                  />
                </div>
              </div>
            </el-form-item>

            <el-form-item prop="password">
              <div class="form-field">
                <label class="field-label">密码</label>
                <div class="input-wrapper" :class="{ 'has-value': loginForm.password }">
                  <el-icon class="input-icon"><Lock /></el-icon>
                  <el-input
                      v-model="loginForm.password"
                      type="password"
                      placeholder="请输入密码"
                      show-password
                      class="custom-input"
                      @keyup.enter="handleLogin"
                  />
                </div>
              </div>
            </el-form-item>

            <div class="form-options">
              <el-checkbox v-model="rememberMe" label="记住我"></el-checkbox>
              <a href="#" class="forgot-link">忘记密码？</a>
            </div>

            <el-form-item>
              <el-button
                  type="primary"
                  class="login-btn"
                  :loading="loading"
                  @click="handleLogin"
              >
                <span v-if="!loading">登录</span>
                <span v-else>登录中...</span>
                <el-icon v-if="!loading" class="btn-arrow"><ArrowRight /></el-icon>
              </el-button>
            </el-form-item>
          </el-form>
          
          <!-- 分隔线 -->
          <div class="divider">
            <span class="divider-text">或者</span>
          </div>
          
          <!-- 注册入口 -->
          <div class="register-section">
            <p class="register-text">还没有账号？</p>
            <el-button class="register-btn" @click="goToRegister">
              立即注册
              <el-icon class="btn-arrow"><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
        
        <!-- 底部信息 -->
        <div class="footer-info">
          <p>© 2024 校园交易平台 · 安全 · 便捷 · 信任</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, ArrowRight } from '@element-plus/icons-vue'
import { login } from '@/api/user'

/**
 * Login.vue - 登录页面组件
 * 
 * 功能：
 * 1. 提供用户登录表单
 * 2. 验证用户输入
 * 3. 调用后端 API 进行登录
 * 4. 保存用户信息到本地存储
 * 5. 跳转到首页
 */

const router = useRouter()  // 路由实例
const loginFormRef = ref(null)  // 表单引用，用于表单验证
const loading = ref(false)  // 登录按钮加载状态
const rememberMe = ref(false)  // 是否记住我

/**
 * 登录表单数据
 */
const loginForm = reactive({
  username: '',  // 用户名
  password: ''   // 密码
})

/**
 * 表单验证规则
 */
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],  // 必填项
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]     // 必填项
}

/**
 * 处理登录逻辑
 * 1. 验证表单
 * 2. 调用登录 API
 * 3. 保存用户信息
 * 4. 跳转到首页
 */
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    // 表单验证
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    // 设置加载状态
    loading.value = true
    
    // 调用登录 API
    const res = await login(loginForm)
    
    // 处理登录结果
    if (res.success) {
      ElMessage.success('登录成功')
      // 保存用户信息到本地存储
      localStorage.setItem('user', JSON.stringify(res.user))
      // 跳转到首页，使用 replace 避免返回到登录页
      await router.replace('/home')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (error) {
    // 错误处理
    if (error !== false) {
      ElMessage.error('登录失败：' + error.message)
    }
  } finally {
    // 重置加载状态
    loading.value = false
  }
}

/**
 * 跳转到注册页面
 */
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  background: #f8fafc;
  position: relative;
  overflow: hidden;
}

/* 左侧装饰区域 */
.login-decoration {
  flex: 1;
  background: linear-gradient(135deg, #059669 0%, #10b981 50%, #0d9488 100%);
  position: relative;
  overflow: hidden;
  display: none;
}

@media (min-width: 1024px) {
  .login-decoration {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.decoration-content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3rem;
}

/* 动态背景图案 */
.pattern-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.pattern-circle {
  position: absolute;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.pattern-circle:nth-child(1) {
  width: 600px;
  height: 600px;
  top: -200px;
  left: -200px;
  animation: rotate 20s linear infinite;
}

.pattern-circle:nth-child(2) {
  width: 400px;
  height: 400px;
  bottom: -100px;
  right: -100px;
  animation: rotate 25s linear infinite reverse;
}

.pattern-circle:nth-child(3) {
  width: 300px;
  height: 300px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: pulse-circle 4s ease-in-out infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes pulse-circle {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.3; }
  50% { transform: translate(-50%, -50%) scale(1.1); opacity: 0.5; }
}

/* 主视觉 */
.hero-content {
  position: relative;
  z-index: 10;
  text-align: center;
  color: white;
  animation: fadeInUp 0.8s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 1.5rem;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: float 3s ease-in-out infinite;
}

.hero-icon svg {
  width: 40px;
  height: 40px;
  color: white;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 800;
  margin: 0 0 0.75rem 0;
  letter-spacing: -0.02em;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.hero-subtitle {
  font-size: 1.125rem;
  opacity: 0.9;
  margin: 0 0 2.5rem 0;
  font-weight: 400;
}

/* 特性列表 */
.features-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 320px;
  margin: 0 auto;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(5px);
}

.feature-icon {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
}

.feature-icon svg {
  width: 100%;
  height: 100%;
}

.feature-item span {
  font-size: 0.938rem;
  font-weight: 500;
}

/* 底部装饰 */
.decoration-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.shape-1 {
  width: 100px;
  height: 100px;
  top: 10%;
  right: 10%;
  animation: float 6s ease-in-out infinite;
}

.shape-2 {
  width: 60px;
  height: 60px;
  bottom: 20%;
  left: 15%;
  animation: float 8s ease-in-out infinite 1s;
}

.shape-3 {
  width: 40px;
  height: 40px;
  top: 30%;
  left: 10%;
  animation: float 5s ease-in-out infinite 2s;
}

/* 右侧登录表单 */
.login-form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.5rem;
  background: white;
  position: relative;
}

.form-wrapper {
  width: 100%;
  max-width: 420px;
  animation: fadeInUp 0.6s ease-out;
}

/* 移动端标题 */
.mobile-header {
  text-align: center;
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

@media (min-width: 1024px) {
  .mobile-header {
    display: none;
  }
}

.mobile-logo {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #059669, #10b981);
  border-radius: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.mobile-logo svg {
  width: 28px;
  height: 28px;
  color: white;
}

.mobile-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #111827;
  margin: 0 0 0.375rem 0;
}

.mobile-subtitle {
  font-size: 0.875rem;
  color: #6b7280;
  margin: 0;
}

/* 桌面端显示控制 */
.desktop-only {
  display: block;
}

@media (max-width: 1023px) {
  .desktop-only {
    display: none;
  }
}

/* 登录卡片 */
.login-card {
  background: white;
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
  border: 1px solid #e5e7eb;
}

@media (min-width: 1024px) {
  .login-card {
    padding: 2.5rem;
  }
}

.card-header {
  margin-bottom: 2rem;
}

.card-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #111827;
  margin: 0 0 0.5rem 0;
}

.card-subtitle {
  font-size: 0.938rem;
  color: #6b7280;
  margin: 0;
}

/* 表单字段 */
.form-field {
  width: 100%;
}

.field-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 0.5rem;
  padding: 0 1rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.input-wrapper:hover {
  border-color: #d1d5db;
  background: white;
}

.input-wrapper:focus-within {
  border-color: #059669;
  background: white;
  box-shadow: 0 0 0 4px rgba(5, 150, 105, 0.1);
}

.input-icon {
  font-size: 18px;
  color: #9ca3af;
  margin-right: 0.75rem;
  transition: color 0.3s ease;
  flex-shrink: 0;
}

.input-wrapper:focus-within .input-icon {
  color: #059669;
}

.custom-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  background: transparent !important;
  padding: 0;
}

.custom-input :deep(.el-input__inner) {
  height: 48px;
  font-size: 1rem;
  color: #1f2937;
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: #9ca3af;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.form-options :deep(.el-checkbox__label) {
  color: #4b5563;
  font-size: 0.875rem;
}

.form-options :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #059669;
  border-color: #059669;
}

.forgot-link {
  font-size: 0.875rem;
  color: #059669;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.forgot-link:hover {
  color: #047857;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 52px;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 0.5rem;
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 14px 0 rgba(5, 150, 105, 0.25);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px 0 rgba(5, 150, 105, 0.35);
}

.login-btn:active {
  transform: translateY(0);
}

.btn-arrow {
  font-size: 16px;
  transition: transform 0.2s ease;
}

.login-btn:hover .btn-arrow {
  transform: translateX(3px);
}

/* 分隔线 */
.divider {
  display: flex;
  align-items: center;
  margin: 1.5rem 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #e5e7eb;
}

.divider-text {
  padding: 0 1rem;
  font-size: 0.875rem;
  color: #9ca3af;
}

/* 注册区域 */
.register-section {
  text-align: center;
}

.register-text {
  font-size: 0.938rem;
  color: #6b7280;
  margin: 0 0 0.75rem 0;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 0.938rem;
  font-weight: 600;
  border-radius: 0.5rem;
  background: #f3f4f6;
  border: 2px solid #e5e7eb;
  color: #374151;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.register-btn:hover {
  background: #f9fafb;
  border-color: #059669;
  color: #059669;
  transform: translateY(-1px);
}

.register-btn .btn-arrow {
  transition: transform 0.2s ease;
}

.register-btn:hover .btn-arrow {
  transform: translateX(3px);
}

/* 底部信息 */
.footer-info {
  text-align: center;
  margin-top: 2rem;
}

.footer-info p {
  font-size: 0.813rem;
  color: #9ca3af;
  margin: 0;
}

/* 响应式调整 */
@media (max-width: 640px) {
  .login-form-section {
    padding: 1rem;
  }
  
  .login-card {
    padding: 1.5rem;
  }
  
  .card-title {
    font-size: 1.25rem;
  }
}

/* ==================== 移动端深度优化 ==================== */

/* 平板设备 */
@media (max-width: 1024px) {
  .login-container {
    flex-direction: column;
  }
  
  .login-decoration {
    display: none;
  }
  
  .login-form-section {
    width: 100%;
    padding: 2rem 1.5rem;
  }
  
  .form-wrapper {
    max-width: 480px;
  }
}

/* 大手机/小平板 */
@media (max-width: 768px) {
  .login-form-section {
    padding: 1.5rem 1rem;
    align-items: flex-start;
    padding-top: 3rem;
  }
  
  .mobile-header {
    margin-bottom: 1.5rem;
  }
  
  .mobile-title {
    font-size: 1.5rem;
  }
  
  .login-card {
    padding: 1.25rem;
    border-radius: 0.75rem;
  }
  
  .card-header {
    margin-bottom: 1.5rem;
  }
  
  .card-title {
    font-size: 1.125rem;
  }
  
  .card-subtitle {
    font-size: 0.875rem;
  }
  
  /* 输入框移动端优化 */
  .input-wrapper {
    padding: 0 0.875rem;
    min-height: 50px;
  }
  
  .custom-input :deep(.el-input__inner) {
    height: 50px;
    font-size: 16px !important; /* 防止 iOS 缩放 */
  }
  
  .input-icon {
    font-size: 20px;
  }
  
  /* 按钮移动端优化 */
  .login-btn {
    height: 52px;
    font-size: 1rem;
  }
  
  .register-btn {
    height: 48px;
    font-size: 0.938rem;
  }
  
  .form-options {
    flex-wrap: wrap;
    gap: 0.5rem;
  }
  
  .footer-info {
    margin-top: 1.5rem;
  }
  
  .footer-info p {
    font-size: 0.75rem;
  }
}

/* 手机设备 */
@media (max-width: 640px) {
  .login-container {
    min-height: 100vh;
    min-height: -webkit-fill-available; /* iOS Safari 修复 */
  }
  
  .login-form-section {
    padding: 1rem;
    padding-top: 2rem;
    padding-bottom: max(1rem, env(safe-area-inset-bottom));
  }
  
  .mobile-header {
    margin-bottom: 1.25rem;
  }
  
  .mobile-title {
    font-size: 1.375rem;
    font-weight: 700;
  }
  
  .mobile-subtitle {
    font-size: 0.938rem;
  }
  
  .login-card {
    padding: 1.25rem;
    border-radius: 0.75rem;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  }
  
  .card-header {
    margin-bottom: 1.25rem;
  }
  
  .card-title {
    font-size: 1.125rem;
    margin-bottom: 0.25rem;
  }
  
  .card-subtitle {
    font-size: 0.813rem;
  }
  
  .form-field {
    margin-bottom: 0.25rem;
  }
  
  .field-label {
    font-size: 0.813rem;
    margin-bottom: 0.375rem;
  }
  
  .input-wrapper {
    min-height: 52px;
    border-radius: 0.625rem;
  }
  
  .custom-input :deep(.el-input__inner) {
    height: 52px;
    font-size: 16px !important;
  }
  
  .form-options {
    margin-bottom: 1.25rem;
  }
  
  .form-options :deep(.el-checkbox__label) {
    font-size: 0.813rem;
  }
  
  .forgot-link {
    font-size: 0.813rem;
  }
  
  .login-btn {
    height: 54px;
    font-size: 1rem;
    border-radius: 0.625rem;
  }
  
  .divider {
    margin: 1.25rem 0;
  }
  
  .divider-text {
    font-size: 0.813rem;
  }
  
  .register-section {
    padding: 0;
  }
  
  .register-text {
    font-size: 0.875rem;
    margin-bottom: 0.5rem;
  }
  
  .register-btn {
    height: 50px;
    font-size: 0.938rem;
    border-radius: 0.625rem;
  }
  
  .footer-info {
    margin-top: 1.25rem;
  }
  
  .footer-info p {
    font-size: 0.688rem;
  }
}

/* 小屏幕手机 */
@media (max-width: 375px) {
  .login-form-section {
    padding: 0.875rem;
    padding-top: 1.5rem;
  }
  
  .mobile-title {
    font-size: 1.25rem;
  }
  
  .login-card {
    padding: 1rem;
  }
  
  .card-title {
    font-size: 1rem;
  }
  
  .input-wrapper {
    min-height: 50px;
    padding: 0 0.75rem;
  }
  
  .custom-input :deep(.el-input__inner) {
    height: 50px;
  }
  
  .login-btn {
    height: 52px;
  }
}

/* 横屏手机优化 */
@media (max-width: 896px) and (orientation: landscape) {
  .login-container {
    min-height: auto;
    padding: 1rem 0;
  }
  
  .login-form-section {
    padding: 1rem;
    align-items: center;
  }
  
  .form-wrapper {
    max-width: 100%;
  }
  
  .mobile-header {
    margin-bottom: 1rem;
  }
  
  .login-card {
    padding: 1rem 1.25rem;
  }
  
  .card-header {
    margin-bottom: 1rem;
  }
  
  .form-options {
    margin-bottom: 1rem;
  }
  
  .divider {
    margin: 1rem 0;
  }
  
  .footer-info {
    margin-top: 1rem;
  }
}

/* 触摸设备优化 - 移除 hover 效果 */
@media (hover: none) and (pointer: coarse) {
  .input-wrapper:hover {
    border-color: #e5e7eb;
    background: #f9fafb;
  }
  
  .login-btn:hover,
  .register-btn:hover,
  .feature-item:hover {
    transform: none;
  }
  
  .login-btn:active {
    transform: scale(0.98);
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  /* 可选：添加深色模式样式 */
}

/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  .hero-icon,
  .pattern-circle,
  .shape,
  .hero-content {
    animation: none !important;
  }
  
  .login-btn,
  .register-btn,
  .input-wrapper {
    transition: none !important;
  }
}
</style>
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
          <h1 class="mobile-title">校园交易平台</h1>
          <p class="mobile-subtitle">欢迎回来</p>
        </div>
        
        <!-- 登录卡片 -->
        <div class="login-card">
          <div class="card-header">
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

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)
const rememberMe = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loading.value = true
    const res = await login(loginForm)
    
    if (res.success) {
      ElMessage.success('登录成功')
      localStorage.setItem('user', JSON.stringify(res.user))
      // 使用 replace 避免返回到登录页
      await router.replace('/home')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (error) {
    if (error !== false) {
      ElMessage.error('登录失败：' + error.message)
    }
  } finally {
    loading.value = false
  }
}

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
  margin-bottom: 2rem;
}

@media (min-width: 1024px) {
  .mobile-header {
    display: none;
  }
}

.mobile-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #111827;
  margin: 0 0 0.5rem 0;
}

.mobile-subtitle {
  font-size: 1rem;
  color: #6b7280;
  margin: 0;
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
</style>
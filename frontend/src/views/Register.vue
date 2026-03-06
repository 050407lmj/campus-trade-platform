<template>
  <div class="register-container">
    <!-- 左侧装饰区域 -->
    <div class="register-decoration">
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
              <path d="M16 21V19C16 17.9391 15.5786 16.9217 14.8284 16.1716C14.0783 15.4214 13.0609 15 12 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <circle cx="8.5" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
              <line x1="20" y1="8" x2="20" y2="14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <line x1="23" y1="11" x2="17" y2="11" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <h1 class="hero-title">加入我们</h1>
          <p class="hero-subtitle">创建账号 · 开启校园交易之旅</p>
          
          <!-- 特性列表 -->
          <div class="features-list">
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
              </div>
              <span>免费注册，即刻开始</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
              </div>
              <span>发布闲置，轻松变现</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
              </div>
              <span>结识同学，拓展社交</span>
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
    
    <!-- 右侧注册表单 -->
    <div class="register-form-section">
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
          <p class="mobile-subtitle">创建账号开始使用</p>
        </div>
        
        <!-- 注册卡片 -->
        <div class="register-card">
          <div class="card-header desktop-only">
            <h2 class="card-title">注册账号</h2>
            <p class="card-subtitle">请填写以下信息完成注册</p>
          </div>
          
          <el-form
              :model="form"
              :rules="rules"
              ref="formRef"
              class="register-form"
          >
            <!-- 必填字段 -->
            <div class="form-row">
              <el-form-item prop="username" class="form-field">
                <label class="field-label">用户名<span class="required">*</span></label>
                <div class="input-wrapper">
                  <el-icon class="input-icon"><User /></el-icon>
                  <el-input
                      v-model="form.username"
                      placeholder="请输入用户名"
                      class="custom-input"
                  />
                </div>
              </el-form-item>

              <el-form-item prop="password" class="form-field">
                <label class="field-label">密码 <span class="required">*</span></label>
                <div class="input-wrapper">
                  <el-icon class="input-icon"><Lock /></el-icon>
                  <el-input
                      v-model="form.password"
                      type="password"
                      placeholder="请输入密码"
                      show-password
                      class="custom-input"
                  />
                </div>
              </el-form-item>
            </div>

            <!-- 可选字段 -->
            <div class="optional-section">
              <div class="section-header">
                <span class="section-label">选填信息</span>
                <span class="section-hint">完善资料可获得更好的体验</span>
              </div>
              
              <div class="form-row">
                <el-form-item prop="wechatId" class="form-field">
                  <label class="field-label">微信号</label>
                  <div class="input-wrapper">
                    <el-icon class="input-icon"><ChatDotRound /></el-icon>
                    <el-input
                        v-model="form.wechatId"
                        placeholder="方便买家联系"
                        class="custom-input"
                    />
                  </div>
                </el-form-item>

                <el-form-item prop="major" class="form-field">
                  <label class="field-label">专业</label>
                  <div class="input-wrapper">
                    <el-icon class="input-icon"><School /></el-icon>
                    <el-input
                        v-model="form.major"
                        placeholder="所在专业"
                        class="custom-input"
                    />
                  </div>
                </el-form-item>
              </div>

              <div class="form-row">
                <el-form-item prop="grade" class="form-field">
                  <label class="field-label">年级</label>
                  <div class="input-wrapper">
                    <el-icon class="input-icon"><Calendar /></el-icon>
                    <el-input
                        v-model="form.grade"
                        placeholder="例如：2022级"
                        class="custom-input"
                    />
                  </div>
                </el-form-item>

                <el-form-item prop="gender" class="form-field">
                  <label class="field-label">性别</label>
                  <el-select v-model="form.gender" placeholder="请选择" class="custom-select">
                    <el-option label="男" value="男" />
                    <el-option label="女" value="女" />
                  </el-select>
                </el-form-item>
              </div>
            </div>

            <el-form-item>
              <el-button type="primary" class="register-btn" @click="handleRegister">
                立即注册
                <el-icon class="btn-arrow"><ArrowRight /></el-icon>
              </el-button>
            </el-form-item>
          </el-form>
          
          <!-- 分隔线 -->
          <div class="divider">
            <span class="divider-text">或者</span>
          </div>
          
          <!-- 登录入口 -->
          <div class="login-section">
            <p class="login-text">已有账号？</p>
            <el-button class="login-btn" @click="goToLogin">
              返回登录
              <el-icon class="btn-arrow"><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
        
        <!-- 底部信息 -->
        <div class="footer-info">
          <p>注册即表示同意《用户协议》和《隐私政策》</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, ChatDotRound, School, Calendar, ArrowRight } from '@element-plus/icons-vue'
import { register } from '@/api/user'

/**
 * Register.vue - 注册页面组件
 * 
 * 功能：
 * 1. 提供用户注册表单
 * 2. 验证用户输入（必填项和选填项）
 * 3. 调用后端 API 进行注册
 * 4. 注册成功后跳转到登录页
 */

const router = useRouter()  // 路由实例
const formRef = ref(null)   // 表单引用，用于表单验证

/**
 * 注册表单数据
 */
const form = reactive({
  username: '',    // 用户名（必填）
  password: '',    // 密码（必填）
  wechatId: '',    // 微信号（选填）
  major: '',       // 专业（选填）
  grade: '',       // 年级（选填）
  gender: ''       // 性别（选填）
})

/**
 * 表单验证规则
 * 只验证必填字段
 */
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],  // 必填项
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]     // 必填项
}

/**
 * 处理注册逻辑
 * 1. 验证表单
 * 2. 调用注册 API
 * 3. 注册成功后跳转到登录页
 */
const handleRegister = async () => {
  if (!formRef.value) return

  try {
    // 表单验证
    const valid = await formRef.value.validate()
    if (!valid) return
    
    // 调用注册 API
    const res = await register(form)
    
    // 处理注册结果
    if (res.success) {
      ElMessage.success('注册成功，请登录')
      // 跳转到登录页
      await router.replace('/login')
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (error) {
    // 错误处理
    if (error !== false) {
      ElMessage.error('注册失败：' + error.message)
    }
  }
}

/**
 * 跳转到登录页面
 */
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  background: #f8fafc;
  position: relative;
  overflow: hidden;
}

/* 左侧装饰区域 */
.register-decoration {
  flex: 1;
  background: linear-gradient(135deg, #0d9488 0%, #14b8a6 50%, #10b981 100%);
  position: relative;
  overflow: hidden;
  display: none;
}

@media (min-width: 1024px) {
  .register-decoration {
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

/* 右侧注册表单 */
.register-form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.5rem;
  background: white;
  position: relative;
  overflow-y: auto;
}

.form-wrapper {
  width: 100%;
  max-width: 500px;
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

/* 注册卡片 */
.register-card {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
  border: 1px solid #e5e7eb;
}

@media (min-width: 1024px) {
  .register-card {
    padding: 2rem;
  }
}

.card-header {
  margin-bottom: 1.5rem;
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

/* 表单布局 */
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

@media (max-width: 640px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}

.form-field {
  margin-bottom: 1rem;
}

.field-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
}

.required {
  color: #ef4444;
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
  height: 44px;
  font-size: 1rem;
  color: #1f2937;
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: #9ca3af;
}

/* 选择框 */
.custom-select {
  width: 100%;
}

.custom-select :deep(.el-input__wrapper) {
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 0.5rem;
  box-shadow: none;
  height: 44px;
  transition: all 0.3s ease;
}

.custom-select :deep(.el-input__wrapper:hover) {
  border-color: #d1d5db;
  background: white;
}

.custom-select :deep(.el-input__wrapper.is-focus) {
  border-color: #059669;
  background: white;
  box-shadow: 0 0 0 4px rgba(5, 150, 105, 0.1);
}

/* 可选区域 */
.optional-section {
  background: #f9fafb;
  border-radius: 0.5rem;
  padding: 1rem;
  margin-bottom: 1.5rem;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #e5e7eb;
}

.section-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: #374151;
}

.section-hint {
  font-size: 0.75rem;
  color: #9ca3af;
}

/* 注册按钮 */
.register-btn {
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

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px 0 rgba(5, 150, 105, 0.35);
}

.register-btn:active {
  transform: translateY(0);
}

.btn-arrow {
  font-size: 16px;
  transition: transform 0.2s ease;
}

.register-btn:hover .btn-arrow {
  transform: translateX(3px);
}

/* 分隔线 */
.divider {
  display: flex;
  align-items: center;
  margin: 1.25rem 0;
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

/* 登录区域 */
.login-section {
  text-align: center;
}

.login-text {
  font-size: 0.938rem;
  color: #6b7280;
  margin: 0 0 0.75rem 0;
}

.login-btn {
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

.login-btn:hover {
  background: #f9fafb;
  border-color: #059669;
  color: #059669;
  transform: translateY(-1px);
}

.login-btn .btn-arrow {
  transition: transform 0.2s ease;
}

.login-btn:hover .btn-arrow {
  transform: translateX(3px);
}

/* 底部信息 */
.footer-info {
  text-align: center;
  margin-top: 1.5rem;
}

.footer-info p {
  font-size: 0.813rem;
  color: #9ca3af;
  margin: 0;
}

/* 响应式调整 */
@media (max-width: 640px) {
  .register-form-section {
    padding: 1rem;
  }
  
  .register-card {
    padding: 1.25rem;
  }
  
  .card-title {
    font-size: 1.25rem;
  }
}

/* ==================== 移动端深度优化 ==================== */

/* 平板设备 */
@media (max-width: 1024px) {
  .register-container {
    flex-direction: column;
  }
  
  .register-decoration {
    display: none;
  }
  
  .register-form-section {
    width: 100%;
    padding: 2rem 1.5rem;
  }
  
  .form-wrapper {
    max-width: 480px;
  }
}

/* 大手机/小平板 */
@media (max-width: 768px) {
  .register-form-section {
    padding: 1.5rem 1rem;
    align-items: flex-start;
    padding-top: 2rem;
  }
  
  .mobile-header {
    margin-bottom: 1.25rem;
  }
  
  .mobile-title {
    font-size: 1.375rem;
  }
  
  .register-card {
    padding: 1.25rem;
    border-radius: 0.75rem;
  }
  
  .card-header {
    margin-bottom: 1.25rem;
  }
  
  .card-title {
    font-size: 1.125rem;
  }
  
  /* 输入框移动端优化 */
  .input-wrapper {
    padding: 0 0.875rem;
    min-height: 50px;
  }
  
  .custom-input :deep(.el-input__inner) {
    height: 50px;
    font-size: 16px !important;
  }
  
  .input-icon {
    font-size: 20px;
  }
  
  /* 按钮移动端优化 */
  .submit-btn {
    height: 52px;
    font-size: 1rem;
  }
  
  .login-btn {
    height: 48px;
    font-size: 0.938rem;
  }
  
  .footer-info {
    margin-top: 1.25rem;
  }
}

/* 手机设备 */
@media (max-width: 640px) {
  .register-container {
    min-height: 100vh;
    min-height: -webkit-fill-available;
  }
  
  .register-form-section {
    padding: 1rem;
    padding-top: 1.5rem;
    padding-bottom: max(1rem, env(safe-area-inset-bottom));
    overflow-y: auto;
  }
  
  .mobile-header {
    margin-bottom: 1rem;
  }
  
  .mobile-title {
    font-size: 1.25rem;
    font-weight: 700;
  }
  
  .mobile-subtitle {
    font-size: 0.875rem;
  }
  
  .register-card {
    padding: 1rem;
    border-radius: 0.625rem;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  }
  
  .card-header {
    margin-bottom: 1rem;
  }
  
  .card-title {
    font-size: 1rem;
    margin-bottom: 0.25rem;
  }
  
  .card-subtitle {
    font-size: 0.75rem;
  }
  
  .form-field {
    margin-bottom: 0.25rem;
  }
  
  .field-label {
    font-size: 0.75rem;
    margin-bottom: 0.25rem;
  }
  
  .input-wrapper {
    min-height: 48px;
    border-radius: 0.5rem;
    padding: 0 0.75rem;
  }
  
  .custom-input :deep(.el-input__inner) {
    height: 48px;
    font-size: 16px !important;
  }
  
  .form-options {
    margin-bottom: 1rem;
  }
  
  .form-options :deep(.el-checkbox__label) {
    font-size: 0.75rem;
  }
  
  .submit-btn {
    height: 50px;
    font-size: 0.938rem;
    border-radius: 0.5rem;
    margin-top: 0.5rem;
  }
  
  .divider {
    margin: 1rem 0;
  }
  
  .divider-text {
    font-size: 0.75rem;
  }
  
  .login-section {
    padding: 0;
  }
  
  .login-text {
    font-size: 0.813rem;
    margin-bottom: 0.5rem;
  }
  
  .login-btn {
    height: 48px;
    font-size: 0.875rem;
    border-radius: 0.5rem;
  }
  
  .footer-info {
    margin-top: 1rem;
  }
  
  .footer-info p {
    font-size: 0.625rem;
  }
}

/* 小屏幕手机 */
@media (max-width: 375px) {
  .register-form-section {
    padding: 0.75rem;
    padding-top: 1rem;
  }
  
  .mobile-title {
    font-size: 1.125rem;
  }
  
  .register-card {
    padding: 0.875rem;
  }
  
  .card-title {
    font-size: 0.938rem;
  }
  
  .input-wrapper {
    min-height: 46px;
    padding: 0 0.625rem;
  }
  
  .custom-input :deep(.el-input__inner) {
    height: 46px;
    font-size: 15px !important;
  }
  
  .submit-btn,
  .login-btn {
    height: 48px;
  }
}

/* 横屏手机优化 */
@media (max-width: 896px) and (orientation: landscape) {
  .register-container {
    min-height: auto;
    padding: 0.5rem 0;
  }
  
  .register-form-section {
    padding: 0.75rem;
    align-items: center;
  }
  
  .form-wrapper {
    max-width: 100%;
  }
  
  .mobile-header {
    margin-bottom: 0.75rem;
  }
  
  .register-card {
    padding: 0.875rem 1rem;
  }
  
  .card-header {
    margin-bottom: 0.75rem;
  }
  
  .form-options {
    margin-bottom: 0.75rem;
  }
  
  .divider {
    margin: 0.75rem 0;
  }
  
  .footer-info {
    margin-top: 0.75rem;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .input-wrapper:hover {
    border-color: #e5e7eb;
    background: #f9fafb;
  }
  
  .submit-btn:hover,
  .login-btn:hover {
    transform: none;
  }
  
  .submit-btn:active {
    transform: scale(0.98);
  }
}

/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  .hero-icon,
  .pattern-circle,
  .shape,
  .hero-content {
    animation: none !important;
  }
  
  .submit-btn,
  .login-btn,
  .input-wrapper {
    transition: none !important;
  }
}
</style>
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
          <h1 class="mobile-title">创建账号</h1>
          <p class="mobile-subtitle">填写信息开始使用</p>
        </div>
        
        <!-- 注册卡片 -->
        <div class="register-card">
          <div class="card-header">
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

const router = useRouter()
const formRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  wechatId: '',
  major: '',
  grade: '',
  gender: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleRegister = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    const res = await register(form)
    if (res.success) {
      ElMessage.success('注册成功，请登录')
      await router.replace('/login')
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (error) {
    if (error !== false) {
      ElMessage.error('注册失败：' + error.message)
    }
  }
}

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
</style>
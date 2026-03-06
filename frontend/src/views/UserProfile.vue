<template>
  <div class="profile-container">
    <!-- 顶部导航栏 -->
    <header class="profile-header">
      <div class="header-container">
        <div class="header-left">
          <el-button class="back-button" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            <span>返回首页</span>
          </el-button>
        </div>
        <div class="header-center">
          <h1 class="page-title">个人信息</h1>
        </div>
        <div class="header-right"></div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="profile-main">
      <div class="profile-content">
        <!-- 头像卡片 -->
        <div class="avatar-section card">
          <div class="section-header">
            <h3 class="section-title">
              <el-icon><User /></el-icon>
              我的头像
            </h3>
          </div>
          
          <div class="avatar-content">
            <div class="avatar-preview-wrapper">
              <el-avatar 
                :size="120" 
                :src="userInfo.wechatQrCodeUrl" 
                class="avatar-preview"
              >
                <el-icon :size="48"><User /></el-icon>
              </el-avatar>
              <div class="avatar-overlay" @click="triggerAvatarUpload">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
              </div>
            </div>
            
            <div class="avatar-info">
              <h4 class="avatar-title">{{ userInfo.username }}</h4>
              <p class="avatar-hint">点击头像更换图片</p>
              <p class="avatar-tip">支持 JPG、PNG 格式，建议尺寸 200x200，大小不超过 2MB</p>
            </div>
          </div>

          <!-- 隐藏的文件上传输入框 -->
          <input
            ref="avatarInput"
            type="file"
            accept="image/jpeg,image/png"
            style="display: none"
            @change="handleAvatarChange"
          />
        </div>

        <!-- 基本信息卡片 -->
        <div class="info-section card">
          <div class="section-header">
            <h3 class="section-title">
              <el-icon><EditPen /></el-icon>
              基本信息
            </h3>
          </div>

          <el-form
            ref="formRef"
            :model="userInfo"
            :rules="rules"
            label-position="top"
            class="info-form"
          >
            <div class="form-row">
              <el-form-item label="用户名" prop="username">
                <el-input 
                  v-model="userInfo.username" 
                  disabled 
                  placeholder="用户名不可修改"
                >
                  <template #prefix>
                    <el-icon><User /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="微信ID" prop="wechatId">
                <el-input 
                  v-model="userInfo.wechatId" 
                  placeholder="请输入您的微信号"
                >
                  <template #prefix>
                    <el-icon><ChatDotRound /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item label="专业" prop="major">
                <el-select 
                  v-model="userInfo.major" 
                  placeholder="请选择您的专业"
                  style="width: 100%"
                >
                  <el-option label="计算机科学与技术" value="计算机科学与技术" />
                  <el-option label="软件工程" value="软件工程" />
                  <el-option label="电子信息工程" value="电子信息工程" />
                  <el-option label="机械工程" value="机械工程" />
                  <el-option label="土木工程" value="土木工程" />
                  <el-option label="经济管理" value="经济管理" />
                  <el-option label="外语文学" value="外语文学" />
                  <el-option label="艺术设计" value="艺术设计" />
                  <el-option label="其他" value="其他" />
                </el-select>
              </el-form-item>

              <el-form-item label="年级" prop="grade">
                <el-select 
                  v-model="userInfo.grade" 
                  placeholder="请选择您的年级"
                  style="width: 100%"
                >
                  <el-option label="大一" value="大一" />
                  <el-option label="大二" value="大二" />
                  <el-option label="大三" value="大三" />
                  <el-option label="大四" value="大四" />
                  <el-option label="研究生" value="研究生" />
                </el-select>
              </el-form-item>
            </div>

            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="userInfo.gender" class="gender-group">
                <el-radio label="男">
                  <el-icon><Male /></el-icon>
                  男
                </el-radio>
                <el-radio label="女">
                  <el-icon><Female /></el-icon>
                  女
                </el-radio>
                <el-radio label="保密">保密</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button size="large" class="reset-button" @click="resetForm">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
          <el-button 
            type="primary" 
            size="large" 
            class="save-button" 
            @click="saveProfile" 
            :loading="saving"
          >
            <el-icon><Check /></el-icon>
            {{ saving ? '保存中...' : '保存信息' }}
          </el-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, User, Check, Camera, EditPen, 
  ChatDotRound, Male, Female, RefreshRight
} from '@element-plus/icons-vue'
import { getUserInfo, updateUserInfo } from '@/api/user'
import { uploadFile } from '@/api/file'

const router = useRouter()
const formRef = ref()
const avatarInput = ref()
const saving = ref(false)
const originalUserInfo = ref({})

// 用户信息表单数据
const userInfo = reactive({
  id: null,
  username: '',
  wechatId: '',
  wechatQrCodeUrl: '',
  major: '',
  grade: '',
  gender: ''
})

// 表单验证规则
const rules = {
  wechatId: [
    { required: true, message: '请输入微信ID', trigger: 'blur' },
    { min: 2, max: 30, message: '微信ID长度应在2-30个字符之间', trigger: 'blur' }
  ],
  major: [
    { required: true, message: '请选择专业', trigger: 'change' }
  ],
  grade: [
    { required: true, message: '请选择年级', trigger: 'change' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

// 获取用户信息
onMounted(async () => {
  await loadUserInfo()
})

const loadUserInfo = async () => {
  try {
    const userStr = localStorage.getItem('user')
    if (!userStr) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }

    const currentUser = JSON.parse(userStr)
    const res = await getUserInfo(currentUser.id)
    
    if (res.success && res.user) {
      Object.assign(userInfo, res.user)
      originalUserInfo.value = { ...res.user }
    } else {
      ElMessage.error(res.message || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 触发头像上传
const triggerAvatarUpload = () => {
  avatarInput.value?.click()
}

// 处理头像选择
const handleAvatarChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型
  if (!['image/jpeg', 'image/png'].includes(file.type)) {
    ElMessage.error('只支持 JPG、PNG 格式的图片')
    event.target.value = ''
    return
  }

  // 验证文件大小 (2MB)
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 2MB')
    event.target.value = ''
    return
  }

  try {
    ElMessage.info('正在上传头像...')
    
    // 上传文件到服务器
    const res = await uploadFile(file)
    
    if (res.success && res.url) {
      // 更新头像URL
      userInfo.wechatQrCodeUrl = res.url
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(res.message || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败，请重试')
  }

  // 清空input，允许重复选择同一文件
  event.target.value = ''
}

// 保存个人信息
const saveProfile = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    saving.value = true

    const res = await updateUserInfo(userInfo)
    
    if (res.success) {
      ElMessage.success('个人信息保存成功')
      
      // 更新本地存储的用户信息
      const userStr = localStorage.getItem('user')
      if (userStr) {
        const currentUser = JSON.parse(userStr)
        Object.assign(currentUser, userInfo)
        localStorage.setItem('user', JSON.stringify(currentUser))
      }

      // 更新原始数据
      originalUserInfo.value = { ...userInfo }
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (error) {
    if (error !== false) {
      console.error('保存失败:', error)
      ElMessage.error('保存失败，请重试')
    }
  } finally {
    saving.value = false
  }
}

// 重置表单
const resetForm = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重置所有修改吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    Object.assign(userInfo, originalUserInfo.value)
    ElMessage.success('已重置为原始信息')
  } catch {
    // 用户取消
  }
}

// 返回首页
const goBack = () => {
  router.push('/home')
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}

/* 添加动态背景图案 */
.profile-container::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 10% 20%, rgba(5, 150, 105, 0.05) 0%, transparent 20%),
    radial-gradient(circle at 90% 80%, rgba(16, 185, 129, 0.05) 0%, transparent 20%),
    radial-gradient(circle at 50% 50%, rgba(13, 148, 136, 0.03) 0%, transparent 30%);
  pointer-events: none;
  z-index: 0;
}

/* 顶部导航栏 */
.profile-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 1.5rem;
  max-width: 800px;
  margin: 0 auto;
}

.header-left,
.header-right {
  width: 120px;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  color: #059669;
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  padding: 0.5rem 0.75rem;
  border-radius: 0.5rem;
  transition: all 0.3s ease;
  font-weight: 500;
}

.back-button:hover {
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
  transform: translateX(-2px);
}

.header-center {
  flex: 1;
  text-align: center;
}

.page-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #059669;
  margin: 0;
}

/* 主要内容区域 */
.profile-main {
  flex: 1;
  padding: 2rem 1rem;
  position: relative;
  z-index: 10;
}

.profile-content {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* 卡片样式 */
.card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(5, 150, 105, 0.1);
  border-radius: 1rem;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 8px 20px rgba(5, 150, 105, 0.12);
}

.section-header {
  margin-bottom: 1.25rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
  color: #059669;
  margin: 0;
}

/* 头像区域 */
.avatar-content {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.avatar-preview-wrapper {
  position: relative;
  flex-shrink: 0;
}

.avatar-preview {
  border: 3px solid rgba(5, 150, 105, 0.2);
  box-shadow: 0 4px 16px rgba(5, 150, 105, 0.2);
  background: linear-gradient(135deg, #f0fdf4, #dcfce7);
  cursor: pointer;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  background: rgba(5, 150, 105, 0.8);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: all 0.3s ease;
  cursor: pointer;
}

.avatar-preview-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.avatar-overlay .el-icon {
  font-size: 1.5rem;
  margin-bottom: 0.25rem;
}

.avatar-overlay span {
  font-size: 0.75rem;
}

.avatar-info {
  flex: 1;
}

.avatar-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #111827;
  margin: 0 0 0.5rem 0;
}

.avatar-hint {
  font-size: 0.875rem;
  color: #059669;
  margin: 0 0 0.25rem 0;
}

.avatar-tip {
  font-size: 0.75rem;
  color: #9ca3af;
  margin: 0;
}

/* 表单样式 */
.info-form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

:deep(.el-input__wrapper),
:deep(.el-select__wrapper) {
  border-radius: 0.5rem;
  border: 2px solid rgba(5, 150, 105, 0.15);
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover),
:deep(.el-select__wrapper:hover) {
  border-color: rgba(5, 150, 105, 0.3);
}

:deep(.el-input__wrapper.is-focus),
:deep(.el-select__wrapper.is-focus) {
  border-color: #059669;
  box-shadow: 0 0 0 3px rgba(5, 150, 105, 0.1);
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background: #f9fafb;
  border-color: rgba(5, 150, 105, 0.1);
}

.gender-group {
  display: flex;
  gap: 1.5rem;
}

:deep(.el-radio__label) {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

:deep(.el-radio__input.is-checked .el-radio__inner) {
  background-color: #059669;
  border-color: #059669;
}

:deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #059669;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 0.5rem;
}

.reset-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  padding: 0.75rem 2rem;
  border-radius: 0.5rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.reset-button:hover {
  background: rgba(5, 150, 105, 0.2);
  transform: translateY(-2px);
}

.save-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 0.5rem;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.save-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
}

/* 响应式设计 */
@media (max-width: 640px) {
  .profile-main {
    padding: 1rem 0.75rem;
  }

  .card {
    padding: 1.25rem;
    border-radius: 0.75rem;
  }

  .avatar-content {
    flex-direction: column;
    text-align: center;
  }

  .avatar-info {
    text-align: center;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }

  .reset-button,
  .save-button {
    width: 100%;
  }

  .header-left span {
    display: none;
  }
}
</style>

<template>
  <div class="profile-container">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button @click="goBack" type="primary" link>
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>

    <!-- 个人信息卡片 -->
    <div class="profile-card">
      <div class="card-header">
        <h2 class="card-title">个人信息</h2>
        <p class="card-subtitle">编辑您的个人资料信息</p>
      </div>

      <el-form
        ref="formRef"
        :model="userInfo"
        :rules="rules"
        label-width="100px"
        class="profile-form"
      >
        <!-- 头像上传 -->
        <el-form-item label="头像">
          <div class="avatar-upload">
            <el-avatar :size="80" :src="userInfo.wechatQrCodeUrl" class="user-avatar-preview">
              <el-icon><User /></el-icon>
            </el-avatar>
            <el-button type="primary" link @click="showAvatarDialog = true">
              更换头像
            </el-button>
          </div>
        </el-form-item>

        <!-- 用户名（只读） -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userInfo.username" disabled />
        </el-form-item>

        <!-- 微信ID -->
        <el-form-item label="微信ID" prop="wechatId">
          <el-input v-model="userInfo.wechatId" placeholder="请输入您的微信号" />
        </el-form-item>

        <!-- 专业 -->
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

        <!-- 年级 -->
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

        <!-- 性别 -->
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="userInfo.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
            <el-radio label="保密">保密</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 操作按钮 -->
        <el-form-item class="form-actions">
          <el-button type="primary" @click="saveProfile" :loading="saving">
            <el-icon><Check /></el-icon>
            保存信息
          </el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 头像上传对话框 -->
    <el-dialog
      v-model="showAvatarDialog"
      title="更换头像"
      width="400px"
      class="avatar-dialog"
    >
      <div class="avatar-dialog-content">
        <el-upload
          class="avatar-uploader"
          action="/api/upload/avatar"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="tempAvatarUrl" :src="tempAvatarUrl" class="avatar-preview" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <p class="upload-tip">点击上传头像图片</p>
        <p class="upload-hint">支持 JPG/PNG 格式，大小不超过 2MB</p>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showAvatarDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmAvatarChange">确认更换</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, User, Check, Plus 
} from '@element-plus/icons-vue'
import { getUserInfo, updateUserInfo } from '@/api/user'

const router = useRouter()
const formRef = ref()
const saving = ref(false)
const showAvatarDialog = ref(false)
const tempAvatarUrl = ref('')
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
    { min: 6, max: 20, message: '微信ID长度应在6-20个字符之间', trigger: 'blur' }
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
      router.push('/login')
      return
    }

    const currentUser = JSON.parse(userStr)
    const res = await getUserInfo(currentUser.id)
    
    if (res.success) {
      Object.assign(userInfo, res.user)
      // 保存原始数据用于重置
      originalUserInfo.value = { ...res.user }
    } else {
      ElMessage.error(res.message || '获取用户信息失败')
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 保存个人信息
const saveProfile = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
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
        } else {
          ElMessage.error(res.message || '保存失败')
        }
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        saving.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  ElMessageBox.confirm('确定要重置所有修改吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    Object.assign(userInfo, originalUserInfo.value)
    ElMessage.success('已重置为原始信息')
  })
}

// 头像上传相关方法
const handleAvatarSuccess = (response, file) => {
  tempAvatarUrl.value = URL.createObjectURL(file.raw)
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

const confirmAvatarChange = () => {
  if (tempAvatarUrl.value) {
    userInfo.wechatQrCodeUrl = tempAvatarUrl.value
    showAvatarDialog.value = false
    ElMessage.success('头像已更新')
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
  padding: 20px;
  position: relative;
}

.back-button {
  margin-bottom: 20px;
}

.profile-card {
  max-width: 600px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(5, 150, 105, 0.15);
  overflow: hidden;
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.card-header {
  padding: 32px 32px 24px;
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
  text-align: center;
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.card-subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.profile-form {
  padding: 32px;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.user-avatar-preview {
  border: 3px solid rgba(5, 150, 105, 0.2);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.15);
}

.form-actions {
  margin-top: 32px;
  text-align: center;
}

.form-actions :deep(.el-form-item__content) {
  justify-content: center;
  gap: 16px;
}

/* 头像上传对话框样式 */
.avatar-dialog :deep(.el-dialog__body) {
  padding: 24px;
}

.avatar-dialog-content {
  text-align: center;
}

.avatar-uploader .avatar-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c8c8c;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border: 2px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  transition: border-color 0.3s;
}

.avatar-uploader-icon:hover {
  border-color: #059669;
}

.upload-tip {
  margin: 16px 0 8px;
  font-size: 14px;
  color: #666;
}

.upload-hint {
  margin: 0;
  font-size: 12px;
  color: #999;
}

.dialog-footer {
  text-align: center;
}

@media (max-width: 768px) {
  .profile-container {
    padding: 12px;
  }
  
  .profile-card {
    margin: 0;
  }
  
  .profile-form {
    padding: 24px 20px;
  }
  
  .card-header {
    padding: 24px 20px 20px;
  }
}
</style>
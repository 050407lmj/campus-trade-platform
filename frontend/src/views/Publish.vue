<template>
  <div class="publish-container">
    <!-- 顶部导航栏 -->
    <header class="publish-header">
      <div class="header-container container">
        <div class="header-left">
          <el-button class="back-button" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            <span>返回</span>
          </el-button>
        </div>

        <div class="header-center">
          <h1 class="page-title">发布商品</h1>
        </div>

        <div class="header-right">
          <el-button class="draft-button" @click="saveDraft">
            <el-icon><Document /></el-icon>
            <span>存草稿</span>
          </el-button>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="publish-main">
      <div class="publish-container-inner">
        <!-- 发布表单 -->
        <form class="publish-form" @submit.prevent="handleSubmit">
          <!-- 基本信息 -->
          <section class="form-section card">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><EditPen /></el-icon>
                基本信息
              </h2>
              <p class="section-subtitle">填写商品的基本信息</p>
            </div>

            <div class="form-grid">
              <div class="form-item full-width">
                <label class="form-label required">商品名称</label>
                <el-input
                    v-model="publishForm.name"
                    placeholder="请输入商品名称"
                    size="large"
                    class="modern-input"
                    maxlength="50"
                    show-word-limit
                />
              </div>

              <div class="form-item">
                <label class="form-label required">商品价格</label>
                <div class="price-input-wrapper">
                  <span class="price-prefix">¥</span>
                  <el-input
                      v-model.number="publishForm.price"
                      type="number"
                      placeholder="0.00"
                      size="large"
                      class="price-input"
                      :min="0"
                      :max="99999"
                  />
                </div>
              </div>

              <div class="form-item">
                <label class="form-label required">商品分类</label>
                <el-select
                    v-model="publishForm.category"
                    placeholder="请选择分类"
                    size="large"
                    class="modern-select"
                >
                  <el-option
                      v-for="category in categories"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                  />
                </el-select>
              </div>
            </div>
          </section>

          <!-- 商品描述 -->
          <section class="form-section card">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><Document /></el-icon>
                商品描述
              </h2>
              <p class="section-subtitle">详细描述商品的特点、使用情况等</p>
            </div>

            <div class="form-item full-width">
              <label class="form-label">商品描述</label>
              <el-input
                  v-model="publishForm.description"
                  type="textarea"
                  :rows="6"
                  placeholder="请详细描述商品的新旧程度、使用情况等"
                  class="modern-textarea"
                  maxlength="500"
                  show-word-limit
                  resize="none"
              />
            </div>
          </section>

          <!-- 商品图片 -->
          <section class="form-section card">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><Picture /></el-icon>
                商品图片
              </h2>
              <p class="section-subtitle">上传清晰的商品图片</p>
            </div>

            <div class="image-upload-area">
              <div class="image-list">
                <div
                    v-for="(image, index) in publishForm.images"
                    :key="index"
                    class="image-item"
                >
                  <el-image
                      :src="image.url"
                      fit="cover"
                      class="uploaded-image"
                  />
                  <div class="image-actions">
                    <el-button
                        type="danger"
                        size="small"
                        circle
                        @click="removeImage(index)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                  <div v-if="index === 0" class="image-badge">主图</div>
                </div>

                <!-- 上传按钮 -->
                <div
                    v-if="publishForm.images.length < 5"
                    class="upload-button"
                    @click="triggerUpload"
                >
                  <el-icon class="upload-icon"><Plus /></el-icon>
                  <span class="upload-text">添加图片</span>
                </div>
              </div>

              <input
                  ref="fileInput"
                  type="file"
                  accept="image/*"
                  multiple
                  style="display: none"
                  @change="handleFileChange"
              />
            </div>
          </section>

          <!-- 交易信息 -->
          <section class="form-section card">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><ShoppingCart /></el-icon>
                交易信息
              </h2>
              <p class="section-subtitle">设置交易方式</p>
            </div>

            <div class="form-item full-width">
              <label class="form-label">交易方式</label>
              <el-checkbox-group v-model="publishForm.tradeMethods" class="trade-methods">
                <el-checkbox label="online">线上交易</el-checkbox>
                <el-checkbox label="offline">线下自提</el-checkbox>
              </el-checkbox-group>
            </div>
          </section>

          <!-- 发布按钮 -->
          <section class="form-actions">
            <div class="action-buttons">
              <el-button size="large" class="cancel-button" @click="goBack">
                取消
              </el-button>
              <el-button
                  type="primary"
                  size="large"
                  class="submit-button"
                  @click="handleSubmit"
                  :loading="submitting"
              >
                <el-icon><Promotion /></el-icon>
                <span>{{ submitting ? '发布中...' : '立即发布' }}</span>
              </el-button>
            </div>
          </section>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft, Document, EditPen, Picture, ShoppingCart,
  Plus, Delete, Promotion
} from '@element-plus/icons-vue'
import { publishProduct } from '@/api/product'
import { uploadFile } from '@/api/file'

const router = useRouter()
const fileInput = ref(null)
const submitting = ref(false)

// 发布表单数据
const publishForm = reactive({
  name: '',
  price: null,
  category: '',
  description: '',
  images: [],
  tradeMethods: ['online', 'offline'],
  enableChat: true
})

// 商品分类
const categories = [
  { label: '教材书籍', value: '教材书籍' },
  { label: '电子产品', value: '电子产品' },
  { label: '生活用品', value: '生活用品' },
  { label: '运动户外', value: '运动户外' },
  { label: '其他', value: '其他' }
]

// 初始化
onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (!userStr) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loadDraft()
})

// 触发文件上传
const triggerUpload = () => {
  fileInput.value?.click()
}

// 处理文件选择
const handleFileChange = async (event) => {
  const files = Array.from(event.target.files)

  if (publishForm.images.length + files.length > 5) {
    ElMessage.warning('最多只能上传 5 张图片')
    return
  }

  for (const file of files) {
    // 验证文件大小
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.warning(`图片 ${file.name} 大小超过 5MB`)
      continue
    }

    // 验证文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.warning(`文件 ${file.name} 不是图片格式`)
      continue
    }

    try {
      // 上传到服务器
      const res = await uploadFile(file)
      
      if (res.success && res.url) {
        // 上传成功，添加到表单
        publishForm.images.push({
          url: res.url,  // 服务器返回的 URL
          name: res.fileName,
          size: res.size,
          uploaded: true  // 标记已上传
        })
        ElMessage.success(`图片 ${file.name} 上传成功`)
      } else {
        ElMessage.error(res.message || '上传失败')
      }
    } catch (error) {
      console.error('上传失败:', error)
      ElMessage.error(`图片 ${file.name} 上传失败：${error.message}`)
    }
  }

  event.target.value = ''
}

// 移除图片
const removeImage = (index) => {
  publishForm.images.splice(index, 1)
}

// 表单验证
const validateForm = () => {
  if (!publishForm.name.trim()) {
    ElMessage.error('请输入商品名称')
    return false
  }

  if (!publishForm.price || publishForm.price <= 0) {
    ElMessage.error('请输入有效的商品价格')
    return false
  }

  if (!publishForm.category) {
    ElMessage.error('请选择商品分类')
    return false
  }

  return true
}

// 提交表单
const handleSubmit = async () => {
  if (!validateForm()) return

  submitting.value = true

  try {
    const userStr = localStorage.getItem('user')
    const user = JSON.parse(userStr)

    const productData = {
      name: publishForm.name,
      price: publishForm.price,
      category: publishForm.category,
      description: publishForm.description,
      imageUrl: publishForm.images[0]?.url || '',
      sellerId: user.id
    }

    const res = await publishProduct(productData)

    if (res.success || res.id) {
      ElMessage.success('商品发布成功')
      localStorage.removeItem('product_draft')
      setTimeout(() => {
        router.push('/home')
      }, 1500)
    } else {
      ElMessage.error(res.message || '发布失败')
    }
  } catch (error) {
    console.error('发布商品失败:', error)
    ElMessage.error('发布失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 保存草稿
const saveDraft = () => {
  const draftData = { ...publishForm }
  localStorage.setItem('product_draft', JSON.stringify(draftData))
  ElMessage.success('草稿已保存')
}

// 加载草稿
const loadDraft = () => {
  const draftStr = localStorage.getItem('product_draft')
  if (draftStr) {
    try {
      const draft = JSON.parse(draftStr)
      Object.assign(publishForm, draft)
      ElMessage.info('已加载上次保存的草稿')
    } catch (error) {
      console.error('加载草稿失败:', error)
    }
  }
}

// 返回上一页
const goBack = async () => {
  const hasContent = publishForm.name.trim() ||
      publishForm.description.trim() ||
      publishForm.images.length > 0

  if (hasContent) {
    try {
      await ElMessageBox.confirm(
          '您有未保存的内容，确定要离开吗？',
          '提示',
          {
            confirmButtonText: '离开',
            cancelButtonText: '保存草稿',
            distinguishCancelAndClose: true,
            type: 'warning'
          }
      )
      router.push('/home')
    } catch (action) {
      if (action === 'cancel') {
        saveDraft()
        router.push('/home')
      }
    }
  } else {
    router.push('/home')
  }
}
</script>

<style scoped>
.publish-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}

/* 添加动态背景图案 */
.publish-container::before {
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
.publish-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.3s ease;
}

.publish-header:hover {
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.12);
}

.header-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 1rem;
  max-width: 1200px;
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
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
}

.back-button:hover {
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
  transform: translateX(-2px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.draft-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  padding: 0.5rem 0.75rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.draft-button:hover {
  background: rgba(5, 150, 105, 0.2);
  color: #059669;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.15);
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
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 主要内容区域 */
.publish-main {
  flex: 1;
  padding: 1.5rem 0;
  position: relative;
  z-index: 10;
}

.publish-container-inner {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1rem;
}

/* 发布表单 */
.publish-form {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

/* 表单区块 */
.form-section {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(5, 150, 105, 0.1);
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-section:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(5, 150, 105, 0.12);
}

.section-header {
  margin-bottom: 1.5rem;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: #059669;
  margin: 0 0 0.5rem 0;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-subtitle {
  color: #059669;
  margin: 0;
  font-size: 0.875rem;
  opacity: 0.8;
}

/* 表单布局 */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-item.full-width {
  grid-column: 1 / -1;
}

.form-label {
  font-weight: 500;
  color: #059669;
  font-size: 0.875rem;
}

.form-label.required::after {
  content: ' *';
  color: #ef4444;
}

/* 输入框样式 */
.modern-input {
  width: 100%;
}

:deep(.modern-input .el-input__wrapper) {
  border-radius: 0.5rem;
  border: 2px solid rgba(5, 150, 105, 0.2);
  background: rgba(255, 255, 255, 0.7);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.05);
}

:deep(.modern-input .el-input__wrapper:hover) {
  border-color: #059669;
  background: white;
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
  transform: translateY(-1px);
}

:deep(.modern-input .el-input__wrapper.is-focus) {
  border-color: #059669;
  background: white;
  box-shadow: 0 0 0 4px rgba(5, 150, 105, 0.1), 0 4px 12px rgba(5, 150, 105, 0.15);
  transform: translateY(-2px);
}

/* 选择框样式 */
.modern-select {
  width: 100%;
}

:deep(.modern-select .el-select__wrapper) {
  border-radius: 0.5rem;
  border: 2px solid rgba(5, 150, 105, 0.2);
  background: rgba(255, 255, 255, 0.7);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.05);
}

:deep(.modern-select .el-select__wrapper:hover) {
  border-color: #059669;
  background: white;
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
  transform: translateY(-1px);
}

/* 文本域样式 */
.modern-textarea {
  width: 100%;
}

:deep(.modern-textarea .el-textarea__inner) {
  border-radius: 0.5rem;
  border: 2px solid rgba(5, 150, 105, 0.2);
  background: rgba(255, 255, 255, 0.7);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.05);
  resize: none;
}

:deep(.modern-textarea .el-textarea__inner:hover) {
  border-color: #059669;
  background: white;
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
  transform: translateY(-1px);
}

:deep(.modern-textarea .el-textarea__inner:focus) {
  border-color: #059669;
  background: white;
  box-shadow: 0 0 0 4px rgba(5, 150, 105, 0.1), 0 4px 12px rgba(5, 150, 105, 0.15);
  transform: translateY(-2px);
}

/* 价格输入框 */
.price-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.price-prefix {
  position: absolute;
  left: 0.75rem;
  color: #059669;
  font-weight: 600;
  z-index: 1;
}

.price-input {
  width: 100%;
}

:deep(.price-input .el-input__wrapper) {
  padding-left: 2rem;
}

/* 图片上传区域 */
.image-upload-area {
  margin-top: 1rem;
}

.image-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 1rem;
}

.image-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: 0.5rem;
  overflow: hidden;
  background: rgba(5, 150, 105, 0.05);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.05);
}

.image-item:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 16px rgba(5, 150, 105, 0.1);
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  display: flex;
  gap: 0.25rem;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 10;
}

.image-item:hover .image-actions {
  opacity: 1;
}

.image-badge {
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 600;
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.2);
}

.upload-button {
  aspect-ratio: 1;
  border: 2px dashed rgba(5, 150, 105, 0.3);
  border-radius: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.5);
}

.upload-button:hover {
  border-color: #059669;
  background: rgba(5, 150, 105, 0.1);
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(5, 150, 105, 0.1);
}

.upload-icon {
  font-size: 1.5rem;
  color: #059669;
  margin-bottom: 0.25rem;
  transition: color 0.3s ease;
}

.upload-button:hover .upload-icon {
  color: #047857;
}

.upload-text {
  font-size: 0.875rem;
  color: #059669;
  font-weight: 500;
}

/* 交易方式 */
.trade-methods {
  display: flex;
  flex-wrap: wrap;
  gap: 1.25rem;
}

/* 操作按钮 */
.form-actions {
  background: transparent;
  padding: 0;
  box-shadow: none;
  margin-top: 1.5rem;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  align-items: center;
}

.cancel-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  padding: 0.75rem 2.5rem;
  border-radius: 0.5rem;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.cancel-button:hover {
  background: rgba(5, 150, 105, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
}

.submit-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  padding: 0.75rem 2.5rem;
  border-radius: 0.5rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.submit-button:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(5, 150, 105, 0.3);
  opacity: 0.9;
}

.submit-button:active {
  transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .publish-container-inner {
    padding: 0 0.75rem;
  }

  .form-section {
    padding: 1.25rem;
  }

  .form-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .image-list {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }

  .action-buttons {
    flex-direction: column;
  }

  .cancel-button,
  .submit-button {
    width: 100%;
  }
}

@media (max-width: 640px) {
  .header-left,
  .header-right {
    width: 80px;
  }

  .draft-button span {
    display: none;
  }

  .section-title {
    font-size: 1.125rem;
  }
}
</style>

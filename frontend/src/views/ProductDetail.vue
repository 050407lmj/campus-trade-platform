<template>
  <div class="product-detail-container">
    <!-- 顶部导航栏 -->
    <header class="glass detail-header">
      <div class="container">
        <div class="flex items-center justify-between py-4">
          <div class="flex items-center gap-3">
            <el-button 
              class="back-button" 
              @click="goBack"
              size="small"
            >
              <el-icon><ArrowLeft /></el-icon>
              <span class="back-text">返回商品列表</span>
            </el-button>
            
            <!-- 面包屑导航 -->
            <div class="breadcrumb">
              <span class="breadcrumb-item" @click="goHome">首页</span>
              <el-icon class="breadcrumb-separator"><ArrowRight /></el-icon>
              <span class="breadcrumb-current">商品详情</span>
            </div>
          </div>

          <div class="flex-1 text-center">
            <h1 class="text-xl font-semibold text-neutral-900 m-0">商品详情</h1>
          </div>

          <div class="flex items-center gap-2">
            <button class="btn btn-ghost btn-sm rounded-full">
              <el-icon><Share /></el-icon>
            </button>
            <button class="btn btn-ghost btn-sm rounded-full">
              <el-icon><Star /></el-icon>
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="detail-main">
      <div class="container py-6">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <div class="card">
            <div class="flex items-center justify-center py-12">
              <div class="loading-spinner"></div>
            </div>
          </div>
        </div>

        <!-- 商品详情内容 -->
        <div v-else-if="product" class="product-content">
          <!-- 商品图片区域 -->
          <section class="product-gallery">
            <div class="main-image">
              <el-image
                  :src="product.imageUrl || getDefaultImage()"
                  :alt="product.name"
                  fit="contain"
                  class="gallery-image"
                  :preview-src-list="[product.imageUrl || getDefaultImage()]"
                  :initial-index="0"
                  preview-teleported
              >
                <template #error>
                  <div class="image-error">
                    <el-icon><Picture /></el-icon>
                    <span>暂无图片</span>
                  </div>
                </template>
                <template #placeholder>
                  <div class="image-loading">
                    <el-icon class="is-loading"><Loading /></el-icon>
                    <span>加载中...</span>
                  </div>
                </template>
              </el-image>

              <!-- 图片状态标签 -->
              <div class="image-status" :class="product.status">
                {{ getStatusText(product.status) }}
              </div>
              
              <!-- 图片预览提示 -->
              <div class="image-preview-hint">
                <el-icon><ZoomIn /></el-icon>
                <span>点击查看大图</span>
              </div>
            </div>
          </section>

          <!-- 商品信息区域 -->
          <section class="product-info">
            <!-- 基本信息 -->
            <div class="product-header">
              <h1 class="product-title">{{ product.name }}</h1>

              <div class="product-meta">
                <div class="price-section">
                  <span class="price-symbol">¥</span>
                  <span class="price-value">{{ product.price }}</span>
                  <span class="price-unit">元</span>
                </div>

                <div class="product-tags">
                  <el-tag :type="getStatusTagType(product.status)" size="large">
                    {{ getStatusText(product.status) }}
                  </el-tag>
                </div>
              </div>
            </div>

            <!-- 商品描述 -->
            <div class="product-description">
              <h3 class="section-title">商品描述</h3>
              <div class="description-content">
                <p>{{ product.description || '暂无描述' }}</p>
              </div>
            </div>

            <!-- 卖家信息 -->
            <div class="seller-info">
              <h3 class="section-title">卖家信息</h3>
              <div class="seller-card">
                <div class="seller-avatar">
                  <el-avatar :size="60" icon="User" />
                  <div class="seller-status online"></div>
                </div>

                <div class="seller-details">
                  <h4 class="seller-name">{{ product.seller?.username || '未知卖家' }}</h4>
                  <p class="seller-major">{{ product.seller?.major || '未填写专业' }}</p>
                </div>

                <div class="seller-actions">
                  <el-button type="primary" class="contact-button" @click="contactSeller">
                    <el-icon><ChatDotRound /></el-icon>
                    联系卖家
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 商品详情 -->
            <div class="product-details">
              <h3 class="section-title">商品详情</h3>
              <div class="details-grid">
                <div class="detail-item">
                  <span class="detail-label">发布时间</span>
                  <span class="detail-value">{{ formatTime(product.createTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">商品状态</span>
                  <span class="detail-value">{{ getStatusText(product.status) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">交易方式</span>
                  <span class="detail-value">线上交易 / 线下自提</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">所在校区</span>
                  <span class="detail-value">主校区</span>
                </div>
              </div>
            </div>
          </section>
        </div>

        <!-- 底部操作栏 -->
        <footer class="detail-footer" v-if="product">
          <div class="footer-container">
            <div class="footer-left">
              <el-button 
                class="footer-button favorite-button" 
                :class="{ 'is-favorited': isFavorited }"
                circle 
                @click="toggleFavorite"
                :loading="favoriteLoading"
              >
                <el-icon><StarFilled v-if="isFavorited" /><Star v-else /></el-icon>
              </el-button>
              <el-button class="footer-button" circle>
                <el-icon><Share /></el-icon>
              </el-button>
            </div>

            <div class="footer-right">
              <!-- 卖家视角：显示管理按钮 -->
              <template v-if="isOwner">
                <el-button
                    v-if="product?.status === 'available'"
                    size="large"
                    class="offline-button"
                    @click="handleOffline"
                >
                  <el-icon><Remove /></el-icon>
                  下架商品
                </el-button>
                <el-button
                    v-else-if="product?.status === 'offline'"
                    type="primary"
                    size="large"
                    class="relist-button"
                    @click="handleRelist"
                >
                  <el-icon><RefreshRight /></el-icon>
                  重新上架
                </el-button>
                <el-button
                    size="large"
                    class="delete-button"
                    @click="handleDelete"
                >
                  <el-icon><Delete /></el-icon>
                  删除商品
                </el-button>
              </template>
              
              <!-- 买家视角：显示购买按钮 -->
              <template v-else>
                <el-button
                    v-if="product?.status === 'available'"
                    type="primary"
                    size="large"
                    class="buy-button"
                    @click="handleBuyRequest"
                >
                  <el-icon><ShoppingCart /></el-icon>
                  我想要
                </el-button>

                <el-button
                    v-else
                    size="large"
                    class="disabled-button"
                    disabled
                >
                  {{ getStatusText(product?.status) }}
                </el-button>
              </template>
            </div>
          </div>
        </footer>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft, ArrowRight, Share, Star, StarFilled, Picture, User, ChatDotRound,
  ShoppingCart, Loading, ZoomIn, Remove, RefreshRight, Delete
} from '@element-plus/icons-vue'
import { getProductById, updateProductStatus, deleteProduct } from '@/api/product'
import { createTradeRequest } from '@/api/trade'
import { addFavorite, removeFavorite, checkFavorite } from '@/api/favorite'

const router = useRouter()
const route = useRoute()
const loading = ref(true)
const product = ref(null)
const currentUser = ref(null)
const isFavorited = ref(false)
const favoriteLoading = ref(false)

// 判断当前用户是否是商品发布者
const isOwner = computed(() => {
  return currentUser.value && product.value && 
         currentUser.value.id === product.value.seller?.id
})

// 初始化
onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  }

  loadProductDetail()
})

// 加载商品详情
const loadProductDetail = async () => {
  const productId = route.params.id
  if (!productId) {
    ElMessage.error('商品 ID 不存在')
    router.push('/home')
    return
  }

  loading.value = true
  try {
    const res = await getProductById(productId)
    // 后端返回 { success: true, data: { ... } }
    if (res && res.success && res.data) {
      product.value = res.data
    } else if (res && res.id) {
      // 兼容直接返回商品数据的情况
      product.value = res
    } else {
      ElMessage.error('商品不存在')
      router.push('/home')
    }
    
    // 检查收藏状态
    if (currentUser.value && product.value && !isOwner.value) {
      checkFavoriteStatus()
    }
  } catch (error) {
    console.error('加载商品详情失败:', error)
    ElMessage.error('加载商品详情失败')
    router.push('/home')
  } finally {
    loading.value = false
  }
}

// 检查收藏状态
const checkFavoriteStatus = async () => {
  if (!currentUser.value || !product.value) return
  
  try {
    const res = await checkFavorite(currentUser.value.id, product.value.id)
    if (res.success) {
      isFavorited.value = res.isFavorited
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (isOwner.value) {
    ElMessage.warning('不能收藏自己的商品')
    return
  }
  
  favoriteLoading.value = true
  
  try {
    if (isFavorited.value) {
      // 取消收藏
      const res = await removeFavorite(currentUser.value.id, product.value.id)
      if (res.success) {
        isFavorited.value = false
        ElMessage.success('已取消收藏')
      } else {
        ElMessage.error(res.message || '取消收藏失败')
      }
    } else {
      // 添加收藏
      const res = await addFavorite(currentUser.value.id, product.value.id)
      if (res.success) {
        isFavorited.value = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error(res.message || '收藏失败')
      }
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败，请重试')
  } finally {
    favoriteLoading.value = false
  }
}

// 获取默认图片
const getDefaultImage = () => {
  return 'https://picsum.photos/600/400'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'available': '在售',
    'sold': '已售出',
    'reserved': '已预订',
    'offline': '已下架'
  }
  return statusMap[status] || status
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  const typeMap = {
    'available': 'success',
    'sold': 'danger',
    'reserved': 'warning',
    'offline': 'info'
  }
  return typeMap[status] || 'info'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 联系卖家
const contactSeller = () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  router.push('/chat')
}

// 发起购买请求
const handleBuyRequest = async () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (currentUser.value.id === product.value.seller?.id) {
    ElMessage.warning('不能购买自己的商品')
    return
  }

  try {
    await ElMessageBox.confirm(
        `确定要发送购买请求吗？\n商品：${product.value.name}\n价格：¥${product.value.price}`,
        '确认购买',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    const res = await createTradeRequest({
      buyerId: currentUser.value.id,
      productId: product.value.id
    })

    if (res.success || res.id) {
      ElMessage.success('购买请求已发送，请等待卖家确认')
      setTimeout(() => {
        router.push('/chat')
      }, 1500)
    } else {
      ElMessage.error(res.message || '发送购买请求失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('发送购买请求失败:', error)
      ElMessage.error('发送购买请求失败')
    }
  }
}

// 返回上一页
const goBack = () => {
  // 尝试返回上一页，如果没有则返回首页
  if (window.history.length > 1) {
    router.go(-1)
  } else {
    router.push('/home')
  }
}

// 返回首页
const goHome = () => {
  router.push('/home')
}

// 下架商品
const handleOffline = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要下架此商品吗？下架后其他用户将无法看到此商品。',
      '下架商品',
      {
        confirmButtonText: '确定下架',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await updateProductStatus(product.value.id, 'offline')
    if (res.success) {
      ElMessage.success('商品已下架')
      product.value.status = 'offline'
    } else {
      ElMessage.error(res.message || '下架失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('下架失败:', error)
      ElMessage.error('下架失败')
    }
  }
}

// 重新上架
const handleRelist = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重新上架此商品吗？',
      '重新上架',
      {
        confirmButtonText: '确定上架',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    const res = await updateProductStatus(product.value.id, 'available')
    if (res.success) {
      ElMessage.success('商品已重新上架')
      product.value.status = 'available'
    } else {
      ElMessage.error(res.message || '上架失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('上架失败:', error)
      ElMessage.error('上架失败')
    }
  }
}

// 删除商品
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除此商品吗？删除后将无法恢复！',
      '删除商品',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }
    )

    const res = await deleteProduct(product.value.id)
    if (res.success) {
      ElMessage.success('商品已删除')
      setTimeout(() => {
        router.push('/home')
      }, 1000)
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}
</script>

<style scoped>
.product-detail-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}

/* 添加动态背景图案 */
.product-detail-container::before {
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
.detail-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.3s ease;
}

.detail-header:hover {
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.12);
}

/* 返回按钮样式 */
.back-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 24px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.2);
  display: flex;
  align-items: center;
  gap: 6px;
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
  background: linear-gradient(135deg, #047857, #059669);
}

.back-text {
  font-size: 14px;
}

/* 面包屑导航样式 */
.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.breadcrumb-item {
  color: #059669;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 2px 6px;
  border-radius: 4px;
}

.breadcrumb-item:hover {
  background: rgba(5, 150, 105, 0.1);
  color: #047857;
}

.breadcrumb-separator {
  color: #059669;
  font-size: 12px;
  opacity: 0.6;
}

.breadcrumb-current {
  color: #059669;
  font-size: 13px;
  font-weight: 500;
  opacity: 0.8;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--space-4);
}

/* 主要内容区域 */
.detail-main {
  flex: 1;
  padding-bottom: 80px;
}

.py-6 {
  padding-top: var(--space-6);
  padding-bottom: var(--space-6);
}

.py-4 {
  padding-top: var(--space-4);
  padding-bottom: var(--space-4);
}

.py-12 {
  padding-top: var(--space-12);
  padding-bottom: var(--space-12);
}

/* Flex 工具类 */
.flex { display: flex; }
.flex-1 { flex: 1; }
.flex-col { flex-direction: column; }
.items-center { align-items: center; }
.items-start { align-items: flex-start; }
.justify-between { justify-content: space-between; }
.justify-center { justify-content: center; }
.gap-2 { gap: var(--space-2); }
.gap-3 { gap: var(--space-3); }

/* 加载状态 */
.loading-state {
  padding: var(--space-12);
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 2px solid var(--neutral-200);
  border-top: 2px solid var(--brand-primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 商品内容 */
.product-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-12);
  animation: fadeInUp 0.8s ease-out;
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

/* 商品图片区域 */
.product-gallery {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.main-image {
  position: relative;
  border-radius: 1.5rem;
  overflow: hidden;
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 50%, #f1f5f9 100%);
  box-shadow: 
    0 4px 6px -1px rgba(5, 150, 105, 0.1),
    0 10px 20px -5px rgba(5, 150, 105, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.main-image::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, 
    transparent 0%, 
    transparent 70%, 
    rgba(5, 150, 105, 0.03) 100%);
  pointer-events: none;
  z-index: 1;
}

.main-image:hover {
  transform: translateY(-6px);
  box-shadow: 
    0 8px 12px -2px rgba(5, 150, 105, 0.15),
    0 20px 40px -10px rgba(5, 150, 105, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
}

.gallery-image {
  width: 100%;
  height: 450px;
  display: block;
  cursor: zoom-in;
  transition: transform 0.4s ease;
}

.main-image:hover .gallery-image {
  transform: scale(1.02);
}

.image-error {
  width: 100%;
  height: 100%;
  background: linear-gradient(145deg, #f8fafc 0%, #f1f5f9 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
  gap: 1rem;
}

.image-error .el-icon {
  font-size: 4rem;
  opacity: 0.5;
}

.image-loading {
  width: 100%;
  height: 100%;
  background: linear-gradient(145deg, #f8fafc 0%, #f1f5f9 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #059669;
  gap: 0.75rem;
}

.image-loading .el-icon {
  font-size: 2rem;
}

.image-preview-hint {
  position: absolute;
  bottom: 1rem;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(5, 150, 105, 0.9);
  backdrop-filter: blur(8px);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 10;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
}

.main-image:hover .image-preview-hint {
  opacity: 1;
  transform: translateX(-50%) translateY(-4px);
}

.image-status {
  position: absolute;
  top: var(--space-4);
  right: var(--space-4);
  padding: var(--space-1) var(--space-4);
  border-radius: var(--radius-base);
  font-size: var(--text-xs);
  font-weight: var(--font-weight-semibold);
  color: white;
  backdrop-filter: blur(10px);
}

.image-status.available {
  background: rgba(34, 197, 94, 0.9);
}

.image-status.sold {
  background: rgba(239, 68, 68, 0.9);
}

.image-status.reserved {
  background: rgba(245, 158, 11, 0.9);
}

.image-status.offline {
  background: rgba(107, 114, 128, 0.9);
}

/* 商品信息区域 */
.product-info {
  display: flex;
  flex-direction: column;
  gap: var(--space-8);
}

.product-header,
.product-description,
.seller-info,
.product-details {
  background: var(--glass-bg);
  backdrop-filter: var(--glass-blur);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-2xl);
  padding: var(--space-8);
  box-shadow: var(--shadow-lg);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.product-header:hover,
.product-description:hover,
.seller-info:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-xl);
}

.product-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-weight-bold);
  color: #059669;
  margin: 0 0 var(--space-6) 0;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: var(--space-4);
}

.price-section {
  display: flex;
  align-items: baseline;
  gap: var(--space-1);
}

.price-symbol {
  font-size: var(--text-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--danger-500);
}

.price-value {
  font-size: var(--text-4xl);
  font-weight: var(--font-weight-bold);
  background: var(--gradient-brand);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.price-unit {
  font-size: var(--text-base);
  color: var(--neutral-500);
}

.product-tags {
  display: flex;
  gap: var(--space-2);
}

.section-title {
  font-size: var(--text-lg);
  font-weight: var(--font-weight-semibold);
  color: #059669;
  margin: 0 0 var(--space-4) 0;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.description-content {
  color: #059669;
  line-height: var(--leading-relaxed);
  opacity: 0.8;
}

.description-content p {
  margin: 0;
}

/* 卖家信息 */
.seller-card {
  display: flex;
  align-items: center;
  gap: var(--space-5);
}

.seller-avatar {
  position: relative;
}

.seller-status {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  border: 2px solid white;
  background: var(--success-500);
  box-shadow: 0 0 8px rgba(34, 197, 94, 0.5);
}

.seller-name {
  font-size: var(--text-base);
  font-weight: var(--font-weight-semibold);
  color: #059669;
  margin: 0 0 var(--space-1) 0;
}

.seller-major {
  color: #059669;
  margin: 0;
  font-size: var(--text-sm);
  opacity: 0.7;
}

.contact-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  padding: var(--space-3) var(--space-6);
  border-radius: var(--radius-lg);
  font-weight: var(--font-weight-semibold);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.contact-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
  background: linear-gradient(135deg, #047857, #059669);
}

/* 商品详情网格 */
.product-details {
  background: white;
  padding: var(--space-8);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-base);
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-4);
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--neutral-200);
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-label {
  color: #059669;
  font-size: var(--text-sm);
  opacity: 0.7;
}

.detail-value {
  color: #059669;
  font-size: var(--text-sm);
  font-weight: var(--font-weight-medium);
}

/* 底部操作栏 */
.detail-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: var(--glass-bg);
  backdrop-filter: var(--glass-blur);
  border-top: 1px solid var(--glass-border);
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-4) var(--space-6);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.footer-left {
  display: flex;
  gap: var(--space-2);
}

.footer-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  transition: all 0.3s ease;
}

.footer-button:hover {
  background: rgba(5, 150, 105, 0.2);
  color: #059669;
  transform: translateY(-2px);
  border-color: #059669;
}

.favorite-button.is-favorited {
  background: linear-gradient(135deg, #f59e0b, #fbbf24);
  border-color: #f59e0b;
  color: white;
}

.favorite-button.is-favorited:hover {
  background: linear-gradient(135deg, #d97706, #f59e0b);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.3);
}

.buy-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  padding: var(--space-3) var(--space-8);
  font-weight: var(--font-weight-semibold);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.buy-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
  background: linear-gradient(135deg, #047857, #059669);
}

.disabled-button {
  background: var(--neutral-200);
  color: var(--neutral-500);
  padding: var(--space-3) var(--space-8);
  cursor: not-allowed;
}

/* 卖家操作按钮 */
.offline-button {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #dc2626;
  padding: var(--space-3) var(--space-6);
  font-weight: var(--font-weight-semibold);
  transition: all 0.3s ease;
}

.offline-button:hover {
  background: #dc2626;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3);
}

.relist-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  padding: var(--space-3) var(--space-6);
  font-weight: var(--font-weight-semibold);
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.relist-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
}

.delete-button {
  background: rgba(107, 114, 128, 0.1);
  border: 1px solid rgba(107, 114, 128, 0.3);
  color: #6b7280;
  padding: var(--space-3) var(--space-6);
  font-weight: var(--font-weight-semibold);
  transition: all 0.3s ease;
}

.delete-button:hover {
  background: #ef4444;
  border-color: #ef4444;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .breadcrumb {
    display: none;
  }
  
  .back-text {
    display: none;
  }
  
  .back-button {
    padding: 8px 12px;
  }
}

@media (max-width: 968px) {
  .product-content {
    grid-template-columns: 1fr;
    gap: var(--space-8);
  }

  .gallery-image {
    height: 300px;
  }
}

@media (max-width: 640px) {
  .product-title {
    font-size: var(--text-xl);
  }

  .price-value {
    font-size: var(--text-3xl);
  }

  .seller-card {
    flex-direction: column;
    text-align: center;
  }

  .details-grid {
    grid-template-columns: 1fr;
  }

  .footer-container {
    padding: var(--space-3);
  }

  .buy-button {
    padding: var(--space-2) var(--space-4);
    font-size: var(--text-sm);
  }
}
</style>
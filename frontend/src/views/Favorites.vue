<template>
  <div class="favorites-container">
    <!-- 顶部导航栏 -->
    <header class="favorites-header">
      <div class="header-container">
        <div class="header-left">
          <el-button class="back-button" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            <span>返回</span>
          </el-button>
        </div>
        <div class="header-center">
          <h1 class="page-title">我的收藏</h1>
        </div>
        <div class="header-right">
          <span class="count-badge" v-if="favorites.length > 0">
            {{ favorites.length }} 件
          </span>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="favorites-main">
      <div class="favorites-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 空状态 -->
        <div v-else-if="favorites.length === 0" class="empty-state">
          <el-icon class="empty-icon"><Star /></el-icon>
          <h3>暂无收藏</h3>
          <p>去发现喜欢的商品吧~</p>
          <el-button type="primary" @click="goHome" class="browse-button">
            浏览商品
          </el-button>
        </div>

        <!-- 收藏列表 -->
        <div v-else class="favorites-grid">
          <div
            v-for="item in favorites"
            :key="item.id"
            class="favorite-card"
            @click="goToProduct(item.id)"
          >
            <div class="card-image">
              <el-image
                :src="item.imageUrl || getDefaultImage()"
                :alt="item.name"
                fit="cover"
                class="product-image"
              >
                <template #error>
                  <div class="image-placeholder">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="product-status" :class="item.status">
                {{ getStatusText(item.status) }}
              </div>
              <el-button
                class="remove-button"
                circle
                size="small"
                @click.stop="handleRemove(item.id)"
              >
                <el-icon><Close /></el-icon>
              </el-button>
            </div>

            <div class="card-content">
              <h4 class="product-name">{{ item.name }}</h4>
              <p class="product-desc">{{ item.description || '暂无描述' }}</p>
              
              <div class="card-footer">
                <div class="price-section">
                  <span class="price-symbol">¥</span>
                  <span class="price-value">{{ item.price }}</span>
                </div>
                <div class="seller-info">
                  <el-avatar :size="20" class="seller-avatar">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <span class="seller-name">{{ item.seller?.username || '未知' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Star, Picture, User, Close } from '@element-plus/icons-vue'
import { getUserFavorites, removeFavorite } from '@/api/favorite'

const router = useRouter()
const loading = ref(true)
const favorites = ref([])
const currentUser = ref(null)

onMounted(async () => {
  const userStr = localStorage.getItem('user')
  if (!userStr) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  currentUser.value = JSON.parse(userStr)
  await loadFavorites()
})

const loadFavorites = async () => {
  loading.value = true
  try {
    const res = await getUserFavorites(currentUser.value.id)
    favorites.value = res || []
  } catch (error) {
    console.error('加载收藏失败:', error)
    ElMessage.error('加载收藏列表失败')
  } finally {
    loading.value = false
  }
}

const handleRemove = async (productId) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏该商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await removeFavorite(currentUser.value.id, productId)
    if (res.success) {
      favorites.value = favorites.value.filter(f => f.id !== productId)
      ElMessage.success('已取消收藏')
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消收藏失败:', error)
    }
  }
}

const goToProduct = (productId) => {
  router.push(`/product/${productId}`)
}

const goBack = () => {
  router.back()
}

const goHome = () => {
  router.push('/home')
}

const getDefaultImage = () => {
  return 'https://picsum.photos/300/200'
}

const getStatusText = (status) => {
  const map = {
    available: '在售',
    sold: '已售',
    reserved: '已预订',
    offline: '已下架'
  }
  return map[status] || status
}
</script>

<style scoped>
.favorites-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%);
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.favorites-header {
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

.count-badge {
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.875rem;
  font-weight: 500;
}

/* 主要内容区域 */
.favorites-main {
  flex: 1;
  padding: 1.5rem;
}

.favorites-content {
  max-width: 1200px;
  margin: 0 auto;
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 0;
  color: #059669;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(5, 150, 105, 0.2);
  border-top-color: #059669;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 0;
  color: #6b7280;
}

.empty-icon {
  font-size: 4rem;
  color: #d1d5db;
  margin-bottom: 1rem;
}

.empty-state h3 {
  font-size: 1.25rem;
  color: #374151;
  margin: 0 0 0.5rem 0;
}

.empty-state p {
  margin: 0 0 1.5rem 0;
}

.browse-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 0.5rem;
  font-weight: 600;
}

/* 收藏网格 */
.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 1.25rem;
}

/* 收藏卡片 */
.favorite-card {
  background: white;
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.favorite-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(5, 150, 105, 0.15);
}

.card-image {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f4f6;
  color: #9ca3af;
  font-size: 2rem;
}

.product-status {
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
  color: white;
}

.product-status.available { background: #22c55e; }
.product-status.sold { background: #ef4444; }
.product-status.reserved { background: #f59e0b; }
.product-status.offline { background: #6b7280; }

.remove-button {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background: rgba(239, 68, 68, 0.9);
  border: none;
  color: white;
  opacity: 0;
  transition: all 0.3s ease;
}

.favorite-card:hover .remove-button {
  opacity: 1;
}

.remove-button:hover {
  background: #dc2626;
  transform: scale(1.1);
}

.card-content {
  padding: 1rem;
}

.product-name {
  font-size: 1rem;
  font-weight: 600;
  color: #111827;
  margin: 0 0 0.5rem 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-desc {
  font-size: 0.875rem;
  color: #6b7280;
  margin: 0 0 0.75rem 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-symbol {
  font-size: 0.875rem;
  color: #ef4444;
}

.price-value {
  font-size: 1.125rem;
  font-weight: 700;
  color: #ef4444;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 0.375rem;
}

.seller-avatar {
  background: rgba(5, 150, 105, 0.1);
}

.seller-name {
  font-size: 0.75rem;
  color: #6b7280;
}

/* 响应式 */
@media (max-width: 768px) {
  .favorites-main {
    padding: 1rem;
  }
  
  .favorites-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 1rem;
  }
  
  .card-image {
    height: 120px;
  }
  
  .header-left span {
    display: none;
  }
}
</style>

<template>
  <div class="home-container">
    <!-- 装饰元素 -->
    <div class="decoration-elements">
      <div class="decoration-circle"></div>
      <div class="decoration-circle"></div>
      <div class="decoration-circle"></div>
    </div>
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <div class="navbar-container">
        <div class="navbar-brand">
          <div class="brand-icon">🏪</div>
          <h1 class="brand-title">校园交易平台</h1>
        </div>
        
        <nav class="navbar-nav">
          <el-button type="primary" class="nav-button publish-button" @click="goToPublish">
            <el-icon><Plus /></el-icon>
            <span>发布商品</span>
          </el-button>
          
          <el-button class="nav-button chat-button" @click="goToChat">
            <el-icon><ChatDotRound /></el-icon>
            <span>消息</span>
            <el-badge v-if="unreadCount > 0" :value="unreadCount" class="message-badge" />
          </el-button>
          
          <el-dropdown class="user-dropdown" trigger="click">
            <div class="user-avatar">
              <el-avatar :size="36" :src="currentUser?.wechatQrCodeUrl">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ currentUser?.username || '用户' }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToProfile">
                  <el-icon><User /></el-icon>
                  个人信息
                </el-dropdown-item>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </nav>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="content-container">
        <!-- 搜索和筛选区域 -->
        <section class="search-section">
          <div class="search-header">
            <h2 class="section-title">发现好物</h2>
            <p class="section-subtitle">探索校园生活中的精彩商品</p>
          </div>
          
          <div class="search-bar">
            <div class="search-input-wrapper">
              <el-icon class="search-icon"><Search /></el-icon>
              <el-input
                v-model="searchQuery"
                placeholder="搜索商品名称..."
                size="large"
                class="search-input"
                @keyup.enter="handleSearch"
              />
            </div>
            <el-button type="primary" size="large" class="search-button" @click="handleSearch">
              搜索
            </el-button>
          </div>
        </section>

        <!-- 统计信息 -->
        <section class="stats-section">
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon">📦</div>
              <div class="stat-content">
                <div class="stat-number">{{ totalProducts }}</div>
                <div class="stat-label">全部商品</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">✨</div>
              <div class="stat-content">
                <div class="stat-number">{{ availableProducts }}</div>
                <div class="stat-label">在售商品</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">👥</div>
              <div class="stat-content">
                <div class="stat-number">{{ activeUsers }}</div>
                <div class="stat-label">活跃用户</div>
              </div>
            </div>
          </div>
        </section>

        <!-- 商品列表区域 -->
        <section class="products-section">
          <div class="section-header">
            <h3 class="section-title">最新商品</h3>
            <div class="view-toggle">
              <el-button-group>
                <el-button 
                  :type="viewMode === 'grid' ? 'primary' : 'default'"
                  @click="viewMode = 'grid'"
                >
                  <el-icon><Grid /></el-icon>
                </el-button>
                <el-button 
                  :type="viewMode === 'list' ? 'primary' : 'default'"
                  @click="viewMode = 'list'"
                >
                  <el-icon><List /></el-icon>
                </el-button>
              </el-button-group>
            </div>
          </div>

          <!-- 加载中状态 -->
          <div v-if="loading" class="loading-state">
            <div class="loading-cards">
              <el-skeleton :rows="5" animated v-for="i in 6" :key="i" />
            </div>
          </div>

          <!-- 商品网格视图 -->
          <div v-else-if="products.length > 0 && viewMode === 'grid'" class="products-grid">
            <div
              v-for="product in products"
              :key="product.id"
              class="product-card"
              @click="goToProductDetail(product.id)"
            >
              <div class="product-image">
                <el-image
                  :src="product.imageUrl || getDefaultImage()"
                  :alt="product.name"
                  fit="contain"
                  class="image"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="product-status" :class="product.status">
                  {{ getStatusText(product.status) }}
                </div>
              </div>
              
              <div class="product-content">
                <h4 class="product-name">{{ product.name }}</h4>
                <p class="product-description">{{ product.description }}</p>
                
                <div class="product-meta">
                  <div class="price-section">
                    <span class="price">¥{{ product.price }}</span>
                    <span class="price-unit">元</span>
                  </div>
                  
                  <div class="seller-info">
                    <el-avatar :size="24" class="seller-avatar">
                      <el-icon><User /></el-icon>
                    </el-avatar>
                    <span class="seller-name">{{ product.seller?.username || '未知卖家' }}</span>
                  </div>
                </div>
                
                <div class="product-actions">
                  <el-button size="small" type="primary" class="action-button">
                    <el-icon><View /></el-icon>
                    查看详情
                  </el-button>
                  <el-button size="small" class="action-button contact-button">
                    <el-icon><ChatDotRound /></el-icon>
                    联系卖家
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 商品列表视图 -->
          <div v-else-if="products.length > 0 && viewMode === 'list'" class="products-list">
            <div
              v-for="product in products"
              :key="product.id"
              class="product-list-item"
              @click="goToProductDetail(product.id)"
            >
              <div class="product-list-image">
                <el-image
                  :src="product.imageUrl || getDefaultImage()"
                  :alt="product.name"
                  fit="contain"
                  class="list-image"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              
              <div class="product-list-content">
                <div class="product-list-header">
                  <h4 class="product-name">{{ product.name }}</h4>
                  <el-tag :type="product.status === 'available' ? 'success' : 'info'" size="small">
                    {{ getStatusText(product.status) }}
                  </el-tag>
                </div>
                
                <p class="product-description">{{ product.description }}</p>
                
                <div class="product-list-meta">
                  <div class="price-section">
                    <span class="price">¥{{ product.price }}</span>
                  </div>
                  
                  <div class="product-meta-info">
                    <span class="seller-name">卖家：{{ product.seller?.username || '未知' }}</span>
                    <span class="post-time">{{ formatTime(product.createTime) }}</span>
                  </div>
                </div>
              </div>
              
              <div class="product-list-actions">
                <el-button type="primary" size="small">查看详情</el-button>
                <el-button size="small">联系卖家</el-button>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div v-else class="empty-state">
            <div class="empty-content">
              <div class="empty-icon">📦</div>
              <h3 class="empty-title">暂无商品</h3>
              <p class="empty-description">还没有商品发布，快去成为第一个发布者吧！</p>
              <el-button type="primary" size="large" @click="goToPublish">
                <el-icon><Plus /></el-icon>
                发布商品
              </el-button>
            </div>
          </div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, ChatDotRound, User, ArrowDown, SwitchButton, 
  Search, Grid, List, Picture, View 
} from '@element-plus/icons-vue'
import { getProductList } from '@/api/product'

/**
 * Home.vue - 首页组件（商品列表页）
 * 
 * 功能：
 * 1. 显示商品列表（网格视图和列表视图）
 * 2. 商品搜索功能
 * 3. 统计信息展示
 * 4. 导航栏（发布商品、消息、用户信息、退出登录）
 * 5. 跳转到商品详情、发布页、聊天页等
 */

const router = useRouter()  // 路由实例
const loading = ref(true)   // 加载状态
const products = ref([])    // 商品列表数据
const currentUser = ref(null)  // 当前登录用户信息
const searchQuery = ref('')    // 搜索关键词
const viewMode = ref('grid')   // 视图模式：'grid' 网格视图 | 'list' 列表视图
const unreadCount = ref(0)     // 未读消息数量

/**
 * 计算属性 - 统计数据
 */
// 商品总数
const totalProducts = computed(() => products.value.length)
// 在售商品数量
const availableProducts = computed(() => products.value.filter(p => p.status === 'available').length)
// 活跃用户数（去重后的卖家数）
const activeUsers = computed(() => {
  const uniqueSellers = new Set(products.value.map(p => p.seller?.username).filter(Boolean))
  return uniqueSellers.size
})

/**
 * 组件挂载时执行
 * 1. 获取当前登录用户
 * 2. 加载商品列表
 */
onMounted(() => {
  // 从本地存储获取用户信息
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  } else {
    // 未登录则跳转到登录页
    router.push('/login')
    return
  }

  // 加载商品数据
  loadProducts()
})

/**
 * 加载商品列表
 * 调用后端 API 获取所有商品
 */
const loadProducts = async () => {
  loading.value = true
  try {
    const res = await getProductList()
    products.value = res || []
  } catch (error) {
    ElMessage.error('加载商品失败')
  } finally {
    loading.value = false
  }
}

/**
 * 搜索处理
 * 根据关键词过滤商品列表
 */
const handleSearch = () => {
  // 如果搜索词为空，重新加载所有商品
  if (!searchQuery.value.trim()) {
    loadProducts()
    return
  }
  
  // 过滤商品名称包含关键词的商品
  const filtered = products.value.filter(product => 
    product.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
  products.value = filtered
}

/**
 * 获取默认图片 URL
 * @returns {string} 默认商品图片地址
 */
const getDefaultImage = () => {
  return 'https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=400&h=300&fit=crop'
}

/**
 * 获取状态文本
 * @param {string} status - 商品状态码
 * @returns {string} 中文状态描述
 */
const getStatusText = (status) => {
  const statusMap = {
    'available': '在售',
    'sold': '已售出',
    'reserved': '已预订'
  }
  return statusMap[status] || status
}

/**
 * 格式化时间戳
 * @param {string} time - ISO 格式的时间字符串
 * @returns {string} 格式化后的时间描述（如：刚刚、5 分钟前、2 小时前）
 */
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  return `${Math.floor(diff / 86400000)}天前`
}

/**
 * 页面跳转函数组
 */
const goToPublish = () => router.push('/publish')          // 跳转到发布商品页
const goToChat = () => router.push('/chat')                // 跳转到聊天页
const goToProductDetail = (id) => router.push(`/product/${id}`)  // 跳转到商品详情页
const goToProfile = () => router.push('/profile')          // 跳转到个人信息页

/**
 * 退出登录
 * 确认退出后清除本地存储的用户信息并跳转到登录页
 */
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('user')  // 清除用户信息
    ElMessage.success('已退出登录')
    router.push('/login')  // 跳转到登录页
  })
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%);
  position: relative;
  overflow-x: hidden;
}

/* 添加动态背景图案 */
.home-container::before {
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

/* 导航栏样式 */
.navbar {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  transition: all 0.3s ease;
}

.navbar:hover {
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.12);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--space-8);
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 4rem;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.brand-icon {
  font-size: 1.5rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-5px); }
  60% { transform: translateY(-2px); }
}

.brand-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #059669;
  margin: 0;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.navbar-nav {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.nav-button {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-lg);
  font-weight: 500;
  transition: all 0.2s ease;
}

.publish-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
  transition: all 0.3s ease;
}

.publish-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
  background: linear-gradient(135deg, #047857, #059669);
}



.chat-button {
  background: white;
  border: 2px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  position: relative;
  transition: all 0.3s ease;
}

.chat-button:hover {
  border-color: #059669;
  color: #059669;
  background: rgba(5, 150, 105, 0.05);
  transform: translateY(-1px);
}

.message-badge {
  position: absolute;
  top: -4px;
  right: -4px;
}

.user-dropdown {
  cursor: pointer;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3);
  border-radius: var(--radius-lg);
  transition: background-color 0.2s ease;
}

.user-avatar:hover {
  background-color: rgba(5, 150, 105, 0.1);
}

.username {
  font-weight: 500;
  color: #059669;
}

.dropdown-icon {
  color: #059669;
  font-size: 0.875rem;
}

/* 添加浮动装饰元素 */
.decoration-elements {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  overflow: hidden;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(5, 150, 105, 0.03);
  border: 1px solid rgba(5, 150, 105, 0.05);
}

.decoration-circle:nth-child(1) {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
  animation: floatDecoration 15s ease-in-out infinite;
}

.decoration-circle:nth-child(2) {
  width: 200px;
  height: 200px;
  bottom: 100px;
  left: -50px;
  animation: floatDecoration 12s ease-in-out infinite 2s;
}

.decoration-circle:nth-child(3) {
  width: 150px;
  height: 150px;
  top: 30%;
  right: 10%;
  animation: floatDecoration 18s ease-in-out infinite 4s;
}

@keyframes floatDecoration {
  0%, 100% { 
    transform: translateY(0) translateX(0) rotate(0deg); 
  }
  25% { 
    transform: translateY(-20px) translateX(10px) rotate(90deg); 
  }
  50% { 
    transform: translateY(-40px) translateX(-10px) rotate(180deg); 
  }
  75% { 
    transform: translateY(-20px) translateX(-20px) rotate(270deg); 
  }
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  padding: var(--space-12) 0;
  position: relative;
  z-index: 10;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--space-8);
  position: relative;
  z-index: 20;
}

/* 搜索区域 */
.search-section {
  margin-bottom: var(--space-12);
}

.search-header {
  text-align: center;
  margin-bottom: var(--space-8);
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  color: #059669;
  margin: 0 0 var(--space-3) 0;
  text-align: center;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-subtitle {
  font-size: 1rem;
  color: #059669;
  margin: 0;
  opacity: 0.8;
}

.search-bar {
  display: flex;
  gap: var(--space-4);
  max-width: 600px;
  margin: 0 auto;
}

.search-input-wrapper {
  flex: 1;
  position: relative;
}

.search-icon {
  position: absolute;
  left: var(--space-4);
  top: 50%;
  transform: translateY(-50%);
  color: #059669;
  z-index: 1;
}

:deep(.search-input .el-input__wrapper) {
  padding-left: 2.75rem;
  border-radius: var(--radius-xl);
  border: 2px solid rgba(5, 150, 105, 0.2);
  background: white;
  transition: all 0.2s ease;
}

:deep(.search-input.is-focus .el-input__wrapper) {
  border-color: #059669;
  box-shadow: 0 0 0 3px rgba(5, 150, 105, 0.15);
  transform: scale(1.02);
}

.search-button {
  border-radius: var(--radius-xl);
  padding: 0 var(--space-8);
  font-weight: 600;
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
  transition: all 0.3s ease;
}

.search-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
  background: linear-gradient(135deg, #047857, #059669);
}

/* 统计信息 */
.stats-section {
  margin-bottom: var(--space-12);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--space-4);
}

.stat-card {
  background: white;
  padding: var(--space-8);
  border-radius: var(--radius-xl);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.1);
  display: flex;
  align-items: center;
  gap: var(--space-4);
  transition: all 0.3s ease;
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(5, 150, 105, 0.15);
  border-color: #059669;
}

.stat-icon {
  font-size: 2rem;
  opacity: 0.8;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #059669;
}

.stat-label {
  font-size: 0.875rem;
  color: #059669;
  opacity: 0.8;
}

/* 商品列表区域 */
.products-section {
  margin-bottom: var(--space-12);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-8);
}

.view-toggle {
  display: flex;
  gap: var(--space-3);
}

/* 加载状态 */
.loading-state {
  margin: var(--space-12) 0;
}

.loading-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--space-4);
}

/* 商品网格视图 */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: var(--space-8);
}

.product-card {
  background: white;
  border-radius: var(--radius-xl);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(5, 150, 105, 0.15);
  border-color: #059669;
}

.product-image {
  position: relative;
  height: 220px;
  overflow: hidden;
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
}

.image {
  width: 100%;
  height: 100%;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.product-card:hover .image {
  transform: scale(1.03);
}

.image-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(145deg, #f0fdf4 0%, #dcfce7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #059669;
  font-size: 3rem;
  opacity: 0.5;
}

.product-status {
  position: absolute;
  top: var(--space-4);
  right: var(--space-4);
  padding: var(--space-1) var(--space-3);
  border-radius: var(--radius-base);
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.product-status.available {
  background: rgba(45, 206, 137, 0.9);
  color: white;
}

.product-status.sold {
  background: rgba(245, 54, 92, 0.9);
  color: white;
}

.product-content {
  padding: var(--space-6);
}

.product-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--neutral-900);
  margin: 0 0 var(--space-4) 0;
  line-height: 1.4;
}

.product-description {
  color: #059669;
  font-size: 0.875rem;
  line-height: 1.5;
  margin: 0 0 var(--space-6) 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  opacity: 0.8;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-6);
}

.price-section {
  display: flex;
  align-items: baseline;
  gap: var(--space-1);
}

.price {
  font-size: 1.5rem;
  font-weight: 700;
  color: #059669;
}

.price-unit {
  font-size: 0.875rem;
  color: #059669;
  opacity: 0.7;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.seller-avatar {
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
}

.seller-name {
  font-size: 0.75rem;
  color: #059669;
  opacity: 0.8;
}

.product-actions {
  display: flex;
  gap: var(--space-3);
}

.action-button {
  flex: 1;
  border-radius: var(--radius-base);
  font-weight: 500;
}

.contact-button {
  background: rgba(5, 150, 105, 0.05);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  transition: all 0.3s ease;
}

.contact-button:hover {
  background: #059669;
  border-color: #059669;
  color: white;
  transform: translateY(-1px);
}

/* 商品列表视图 */
.products-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.product-list-item {
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  padding: var(--space-6);
  display: flex;
  gap: var(--space-6);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.product-list-item:hover {
  box-shadow: 0 8px 20px rgba(5, 150, 105, 0.12);
  border-color: #059669;
  transform: translateY(-2px);
}

.product-list-image {
  width: 140px;
  height: 140px;
  border-radius: 1rem;
  overflow: hidden;
  flex-shrink: 0;
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.08);
}

.list-image {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.product-list-item:hover .list-image {
  transform: scale(1.05);
}

.product-list-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-list-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-4);
}

.product-list-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-meta-info {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.post-time {
  font-size: 0.75rem;
  color: #059669;
  opacity: 0.6;
}

.product-list-actions {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  flex-shrink: 0;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: var(--space-12) 0;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: var(--space-6);
  opacity: 0.5;
}

.empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #059669;
  margin: 0 0 var(--space-4) 0;
}

.empty-description {
  color: #059669;
  margin: 0 0 var(--space-8) 0;
  opacity: 0.7;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar-container {
    padding: 0 var(--space-4);
  }
  
  .content-container {
    padding: 0 var(--space-4);
  }
  
  .brand-title {
    font-size: 1rem;
  }
  
  .nav-button span {
    display: none;
  }
  
  .username {
    display: none;
  }
  
  .search-bar {
    flex-direction: column;
    gap: var(--space-3);
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .product-list-item {
    flex-direction: column;
  }
  
  .product-list-image {
    width: 100%;
    height: 200px;
  }
  
  .product-list-actions {
    flex-direction: row;
    justify-content: space-between;
  }
}

@media (max-width: 640px) {
  .section-title {
    font-size: 1.5rem;
  }
  
  .product-card {
    margin: 0 calc(var(--space-4) * -1);
    border-radius: 0;
  }
}
</style>

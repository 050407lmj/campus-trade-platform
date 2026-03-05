<template>
  <div class="chat-container">
    <!-- 装饰元素 -->
    <div class="decoration-elements">
      <div class="decoration-circle"></div>
      <div class="decoration-circle"></div>
      <div class="decoration-circle"></div>
    </div>

    <!-- 顶部导航栏 -->
    <header class="chat-header">
      <div class="header-container container">
        <div class="header-left">
          <el-button class="back-button" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            <span>返回</span>
          </el-button>
        </div>

        <div class="header-center">
          <h1 class="page-title">消息中心</h1>
        </div>

        <div class="header-right">
          <el-button class="search-button" circle>
            <el-icon><Search /></el-icon>
          </el-button>
        </div>
      </div>
    </header>

    <!-- 主要聊天区域 -->
    <main class="chat-main">
      <div class="chat-layout">
        <!-- 左侧：联系人列表 -->
        <aside class="contact-sidebar">
          <div class="sidebar-header">
            <h2 class="sidebar-title">消息列表</h2>
            <div class="online-indicator">
              <div class="online-dot"></div>
              <span class="online-text">{{ onlineCount }} 人在线</span>
            </div>
          </div>

          <div class="contact-list">
            <!-- 搜索联系人 -->
            <div class="contact-search">
              <div class="search-input-wrapper">
                <el-icon class="search-icon"><Search /></el-icon>
                <el-input
                    v-model="contactSearchQuery"
                    placeholder="搜索联系人..."
                    size="small"
                    class="contact-search-input"
                />
              </div>
            </div>

            <!-- 联系人列表 -->
            <div class="contact-items">
              <div
                  v-for="contact in filteredContacts"
                  :key="contact.id"
                  class="contact-item"
                  :class="{
                  active: currentContact?.id === contact.id,
                  unread: contact.unreadCount > 0
                }"
                  @click="selectContact(contact)"
              >
                <div class="contact-avatar">
                  <el-avatar :size="48" icon="User">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <div v-if="contact.isOnline" class="online-badge"></div>
                </div>

                <div class="contact-content">
                  <div class="contact-header">
                    <h4 class="contact-name">{{ contact.username }}</h4>
                    <span class="contact-time">{{ formatTime(contact.lastMessageTime) }}</span>
                  </div>

                  <div class="contact-message">
                    <p class="contact-last-msg">{{ contact.lastMessage || '暂无消息' }}</p>
                    <el-badge
                        v-if="contact.unreadCount > 0"
                        :value="contact.unreadCount"
                        class="unread-badge"
                        :max="99"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <div v-if="filteredContacts.length === 0" class="empty-contacts">
              <div class="empty-icon">💬</div>
              <h3 class="empty-title">暂无消息</h3>
              <p class="empty-description">开始与同学聊天吧！</p>
            </div>
          </div>
        </aside>

        <!-- 右侧：聊天窗口 -->
        <section class="chat-window">
          <div v-if="currentContact" class="chat-active">
            <!-- 聊天头部 -->
            <div class="chat-header-bar">
              <div class="chat-contact-info">
                <div class="contact-avatar">
                  <el-avatar :size="40" icon="User">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <div v-if="currentContact.isOnline" class="online-badge"></div>
                </div>
                <div class="contact-details">
                  <h3 class="contact-name">{{ currentContact.username }}</h3>
                  <p class="contact-status">
                    {{ currentContact.isOnline ? '在线' : '离线' }}
                  </p>
                </div>
              </div>

              <div class="chat-actions">
                <el-button class="action-button" circle>
                  <el-icon><Phone /></el-icon>
                </el-button>
                <el-button class="action-button" circle>
                  <el-icon><VideoCamera /></el-icon>
                </el-button>
                <el-button class="action-button" circle>
                  <el-icon><More /></el-icon>
                </el-button>
              </div>
            </div>

            <!-- 消息列表 -->
            <div class="message-container" ref="messageListRef">
              <div class="message-list">
                <!-- 消息项 -->
                <div
                    v-for="msg in messages"
                    :key="msg.id"
                    class="message-item"
                    :class="msg.senderId === currentUser?.id ? 'message-self' : 'message-other'"
                >
                  <!-- 对方消息 -->
                  <div v-if="msg.senderId !== currentUser?.id" class="message-wrapper">
                    <div class="message-avatar">
                      <el-avatar :size="32" icon="User">
                        <el-icon><User /></el-icon>
                      </el-avatar>
                    </div>
                    <div class="message-content">
                      <div class="message-bubble">
                        <p class="message-text">{{ msg.content }}</p>
                      </div>
                      <span class="message-time">{{ formatMessageTime(msg.createTime) }}</span>
                    </div>
                  </div>

                  <!-- 自己的消息 -->
                  <div v-else class="message-wrapper message-self-wrapper">
                    <div class="message-content">
                      <div class="message-bubble message-bubble-self">
                        <p class="message-text">{{ msg.content }}</p>
                      </div>
                      <span class="message-time">{{ formatMessageTime(msg.createTime) }}</span>
                    </div>
                    <div class="message-avatar">
                      <el-avatar :size="32" icon="User">
                        <el-icon><User /></el-icon>
                      </el-avatar>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 输入区域 -->
            <div class="input-area">
              <div class="input-container">
                <div class="input-toolbar">
                  <el-button class="toolbar-button" circle>
                    <el-icon><Picture /></el-icon>
                  </el-button>
                  <el-button class="toolbar-button" circle>
                    <el-icon><Paperclip /></el-icon>
                  </el-button>
                </div>

                <div class="input-wrapper">
                  <el-input
                      v-model="messageInput"
                      type="textarea"
                      :rows="1"
                      placeholder="输入消息..."
                      class="message-input"
                      @keydown.enter.prevent="sendMessage"
                      resize="none"
                  />
                  <el-button
                      type="primary"
                      class="send-button"
                      @click="sendMessage"
                      :disabled="!messageInput.trim()"
                      circle
                  >
                    <el-icon><Promotion /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 未选择联系人时的欢迎界面 -->
          <div v-else class="chat-welcome">
            <div class="welcome-content">
              <div class="welcome-icon">💬</div>
              <h2 class="welcome-title">欢迎使用消息中心</h2>
              <p class="welcome-description">选择一个联系人开始聊天</p>

              <div class="welcome-features">
                <div class="feature-item">
                  <el-icon class="feature-icon"><Message /></el-icon>
                  <span>实时消息</span>
                </div>
                <div class="feature-item">
                  <el-icon class="feature-icon"><Bell /></el-icon>
                  <span>消息提醒</span>
                </div>
                <div class="feature-item">
                  <el-icon class="feature-icon"><User /></el-icon>
                  <span>在线状态</span>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft, Search, User, Phone, VideoCamera, More,
  Picture, Paperclip, Microphone, Promotion, Message, Bell
} from '@element-plus/icons-vue'
import { getChatHistory, sendMessage as sendMsgApi } from '@/api/message'
import { getAllUsers } from '@/api/user'

const router = useRouter()
const messageListRef = ref(null)
const messageInput = ref('')
const contactSearchQuery = ref('')
const currentUser = ref(null)
const currentContact = ref(null)
const contacts = ref([])
const messages = ref([])
const websocket = ref(null)
const onlineCount = ref(0)

// 计算属性
const filteredContacts = computed(() => {
  if (!contactSearchQuery.value.trim()) return contacts.value
  return contacts.value.filter(contact =>
      contact.username.toLowerCase().includes(contactSearchQuery.value.toLowerCase())
  )
})

// 初始化
onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
    initWebSocket()
    loadContacts()
  } else {
    router.push('/login')
  }
})

onUnmounted(() => {
  if (websocket.value) {
    websocket.value.close()
  }
})

// 初始化 WebSocket 连接
const initWebSocket = () => {
  const wsUrl = `ws://localhost:8080/ws/chat?userId=${currentUser.value.id}`
  websocket.value = new WebSocket(wsUrl)

  websocket.value.onopen = () => {
    console.log('WebSocket 连接成功')
    onlineCount.value = Math.floor(Math.random() * 50) + 1 // 模拟在线人数
  }

  websocket.value.onmessage = (event) => {
    const data = JSON.parse(event.data)
    handleWebSocketMessage(data)
  }

  websocket.value.onerror = (error) => {
    console.error('WebSocket 错误:', error)
  }

  websocket.value.onclose = () => {
    console.log('WebSocket 连接关闭')
    setTimeout(initWebSocket, 3000) // 自动重连
  }
}

// 处理 WebSocket 消息
const handleWebSocketMessage = (data) => {
  console.log('收到 WebSocket 消息:', data)
  
  if (data.type === 'chat') {
    // 收到聊天消息
    if (currentContact.value &&
        (data.senderId === currentContact.value.id || data.receiverId === currentContact.value.id)) {
      const newMessage = {
        id: data.id || Date.now(),
        senderId: data.senderId,
        receiverId: data.receiverId,
        content: data.content,
        createTime: data.createTime
      }
      messages.value.push(newMessage)
      scrollToBottom()
      
      console.log('添加新消息到当前对话:', newMessage)
    }

    // 更新联系人最后消息
    const contact = contacts.value.find(c => c.id === data.senderId)
    if (contact) {
      contact.lastMessage = data.content
      contact.lastMessageTime = data.createTime
      if (!currentContact.value || currentContact.value.id !== data.senderId) {
        contact.unreadCount = (contact.unreadCount || 0) + 1
      }
      console.log('更新联系人消息状态:', contact.username)
    }
  } else if (data.type === 'welcome') {
    console.log('WebSocket 连接成功:', data)
    onlineCount.value = data.onlineCount || 1
  } else if (data.type === 'userStatus') {
    // 处理用户状态变化
    const contact = contacts.value.find(c => c.id === data.userId)
    if (contact) {
      contact.isOnline = data.status === 'online'
      console.log(`用户 ${contact.username} 状态更新为: ${data.status}`)
    }
  }
}

// 加载联系人列表
const loadContacts = async () => {
  try {
    const res = await getAllUsers()
    if (res && Array.isArray(res)) {
      contacts.value = res
          .filter(u => u.id !== currentUser.value.id)
          .map(contact => ({
            ...contact,
            unreadCount: 0,
            isOnline: Math.random() > 0.5,
            lastMessage: '暂无消息',
            lastMessageTime: new Date().toISOString()
          }))
    }
  } catch (error) {
    console.error('加载联系人失败:', error)
  }
}

// 选择联系人
const selectContact = async (contact) => {
  currentContact.value = contact
  contact.unreadCount = 0

  try {
    const res = await getChatHistory(currentUser.value.id, contact.id)
    messages.value = (res || []).sort((a, b) =>
        new Date(a.createTime).getTime() - new Date(b.createTime).getTime()
    )

    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('加载消息失败:', error)
  }
}

// 发送消息
const sendMessage = async () => {
  if (!messageInput.value.trim() || !currentContact.value) return

  const messageContent = messageInput.value.trim()

  try {
    const res = await sendMsgApi({
      senderId: currentUser.value.id,
      receiverId: currentContact.value.id,
      content: messageContent
    })

    if (res.success || res.id) {
      const newMessage = {
        id: res.id || Date.now(),
        senderId: currentUser.value.id,
        receiverId: currentContact.value.id,
        content: messageContent,
        createTime: new Date().toISOString()
      }

      messages.value.push(newMessage)

      const contact = contacts.value.find(c => c.id === currentContact.value.id)
      if (contact) {
        contact.lastMessage = messageContent
        contact.lastMessageTime = newMessage.createTime
      }

      messageInput.value = ''
      await nextTick()
      scrollToBottom()
    }
  } catch (error) {
    ElMessage.error('发送失败：' + error.message)
  }
}

// 滚动到底部
const scrollToBottom = () => {
  if (messageListRef.value) {
    const container = messageListRef.value.querySelector('.message-list')
    if (container) {
      container.scrollTop = container.scrollHeight
    }
  }
}

// 格式化时间
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

const formatMessageTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 返回上一页
const goBack = () => {
  router.push('/home')
}

// 监听消息变化，自动滚动到底部
watch(messages, () => {
  nextTick(() => scrollToBottom())
}, { deep: true })
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 50%, #bbf7d0 100%);
  position: relative;
  overflow: hidden;
}

/* 添加动态背景图案 */
.chat-container::before {
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

/* 顶部导航栏 */
.chat-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.08);
  z-index: 1000;
  position: relative;
}

.chat-header:hover {
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
  width: 80px;
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

.search-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.search-button:hover {
  background: rgba(5, 150, 105, 0.2);
  color: #059669;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.15);
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

/* 主要聊天区域 */
.chat-main {
  flex: 1;
  overflow: hidden;
  padding: 1rem;
  position: relative;
  z-index: 10;
}

.chat-layout {
  height: 100%;
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.1);
  border-radius: 1rem;
  overflow: hidden;
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

/* 左侧联系人列表 */
.contact-sidebar {
  width: 320px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(5, 150, 105, 0.1);
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 1.25rem;
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
}

.sidebar-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #059669;
  margin: 0 0 0.5rem 0;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.online-indicator {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.online-dot {
  width: 8px;
  height: 8px;
  background: #059669;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}

.online-text {
  font-size: 0.75rem;
  color: #059669;
  opacity: 0.8;
}

.contact-list {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.contact-search {
  padding: 0.75rem;
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
}

.search-input-wrapper {
  position: relative;
}

.search-icon {
  position: absolute;
  left: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  color: #059669;
  z-index: 1;
  font-size: 0.875rem;
}

:deep(.contact-search-input .el-input__wrapper) {
  padding-left: 2rem;
  border-radius: 0.5rem;
  border: 1px solid rgba(5, 150, 105, 0.2);
  background: rgba(255, 255, 255, 0.5);
  transition: all 0.3s ease;
}

:deep(.contact-search-input .el-input__wrapper:hover) {
  border-color: #059669;
  background: white;
}

.contact-items {
  flex: 1;
  overflow-y: auto;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-bottom: 1px solid rgba(5, 150, 105, 0.05);
  position: relative;
  overflow: hidden;
}

.contact-item:hover {
  background: rgba(5, 150, 105, 0.05);
  transform: translateX(4px);
}

.contact-item.active {
  background: linear-gradient(135deg, #059669, #10b981);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
  transform: translateX(8px);
}

.contact-item.active .contact-name,
.contact-item.active .contact-last-msg,
.contact-item.active .contact-time {
  color: white;
}

.contact-item.unread {
  background: rgba(5, 150, 105, 0.02);
  border-left: 3px solid #059669;
}

.contact-avatar {
  position: relative;
  margin-right: 0.75rem;
}

.online-badge {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: #059669;
  border: 2px solid white;
  border-radius: 50%;
  box-shadow: 0 0 8px rgba(5, 150, 105, 0.5);
  animation: pulse 2s infinite;
}

.contact-content {
  flex: 1;
  min-width: 0;
}

.contact-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.25rem;
}

.contact-name {
  font-weight: 500;
  color: #059669;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.3s ease;
}

.contact-item:hover .contact-name {
  color: #059669;
}

.contact-time {
  font-size: 0.75rem;
  color: #059669;
  opacity: 0.7;
  white-space: nowrap;
  transition: color 0.3s ease;
}

.contact-message {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.contact-last-msg {
  font-size: 0.875rem;
  color: #059669;
  opacity: 0.8;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.unread-badge {
  margin-left: 0.5rem;
}

/* 空状态 */
.empty-contacts {
  text-align: center;
  padding: 3rem;
  color: #059669;
  opacity: 0.7;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-title {
  font-size: 1rem;
  font-weight: 600;
  color: #059669;
  margin: 0 0 0.25rem 0;
}

.empty-description {
  font-size: 0.875rem;
  margin: 0;
}

/* 右侧聊天窗口 */
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
}

.chat-active {
  height: 100%;
  display: flex;
  flex-direction: column;
  animation: slideInRight 0.8s ease-out;
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 聊天头部 */
.chat-header-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
}

.chat-contact-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.contact-details {
  display: flex;
  flex-direction: column;
}

.contact-details .contact-name {
  font-weight: 600;
  color: #059669;
  margin: 0;
}

.contact-status {
  font-size: 0.75rem;
  color: #059669;
  opacity: 0.7;
  margin: 0;
}

.chat-actions {
  display: flex;
  gap: 0.5rem;
}

.action-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.action-button:hover {
  background: rgba(5, 150, 105, 0.2);
  color: #059669;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
}

/* 消息容器 */
.message-container {
  flex: 1;
  overflow: hidden;
  background: rgba(240, 253, 244, 0.3);
}

.message-list {
  height: 100%;
  overflow-y: auto;
  padding: 1rem;
}

.message-item {
  margin-bottom: 1rem;
  animation: fadeInUp 0.3s ease-out;
}

.message-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
}

.message-self-wrapper {
  flex-direction: row-reverse;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.message-bubble {
  background: white;
  padding: 0.5rem 0.75rem;
  border-radius: 0.5rem;
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.05);
  margin-bottom: 0.25rem;
  transition: all 0.3s ease;
  border: 1px solid rgba(5, 150, 105, 0.1);
}

.message-bubble:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
}

.message-bubble-self {
  background: linear-gradient(135deg, #059669, #10b981);
  color: white;
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
  border: none;
}

.message-bubble-self:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
}

.message-text {
  margin: 0;
  line-height: 1.4;
  word-wrap: break-word;
}

.message-time {
  font-size: 0.75rem;
  color: #059669;
  opacity: 0.7;
  margin: 0;
}

.message-self-wrapper .message-time {
  text-align: right;
}

/* 输入区域 */
.input-area {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(5, 150, 105, 0.1);
  padding: 1rem;
}

.input-container {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-toolbar {
  display: flex;
  gap: 0.5rem;
}

.toolbar-button {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.2);
  color: #059669;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.toolbar-button:hover {
  background: rgba(5, 150, 105, 0.2);
  color: #059669;
  transform: translateY(-2px);
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.1);
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
}

.message-input {
  flex: 1;
}

:deep(.message-input .el-textarea__inner) {
  border-radius: 0.5rem;
  border: 2px solid rgba(5, 150, 105, 0.2);
  background: rgba(255, 255, 255, 0.7);
  resize: none;
  min-height: 40px;
  max-height: 120px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(5, 150, 105, 0.05);
}

:deep(.message-input .el-textarea__inner:hover) {
  border-color: #059669;
  background: white;
  box-shadow: 0 4px 8px rgba(5, 150, 105, 0.1);
  transform: translateY(-1px);
}

:deep(.message-input .el-textarea__inner:focus) {
  border-color: #059669;
  background: white;
  box-shadow: 0 0 0 4px rgba(5, 150, 105, 0.1), 0 4px 12px rgba(5, 150, 105, 0.15);
  transform: translateY(-2px);
}

.send-button {
  background: linear-gradient(135deg, #059669, #10b981);
  border: none;
  color: white;
  flex-shrink: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.2);
}

.send-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(5, 150, 105, 0.3);
  opacity: 0.9;
}

.send-button:disabled {
  background: rgba(5, 150, 105, 0.3);
  transform: none;
  box-shadow: none;
  opacity: 0.7;
}

/* 欢迎界面 */
.chat-welcome {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(240, 253, 244, 0.3);
  animation: fadeInUp 0.8s ease-out;
}

.welcome-content {
  text-align: center;
  max-width: 400px;
  padding: 3rem;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 1rem;
  box-shadow: 0 8px 32px rgba(5, 150, 105, 0.1);
  border: 1px solid rgba(5, 150, 105, 0.1);
  transition: all 0.3s ease;
}

.welcome-content:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(5, 150, 105, 0.15);
}

.welcome-icon {
  font-size: 4rem;
  margin-bottom: 2rem;
  opacity: 0.8;
  transition: all 0.3s ease;
  color: #059669;
}

.welcome-content:hover .welcome-icon {
  transform: scale(1.1);
  opacity: 1;
}

.welcome-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #059669;
  margin: 0 0 1rem 0;
  background: linear-gradient(135deg, #059669, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.welcome-description {
  color: #059669;
  opacity: 0.8;
  margin: 0 0 2rem 0;
}

.welcome-features {
  display: flex;
  justify-content: center;
  gap: 2rem;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
  padding: 0.75rem;
  border-radius: 0.5rem;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(5, 150, 105, 0.05);
  transform: translateY(-2px);
}

.feature-icon {
  font-size: 1.5rem;
  color: #059669;
  transition: all 0.3s ease;
}

.feature-item:hover .feature-icon {
  transform: scale(1.2);
  color: #047857;
}

.feature-item span {
  font-size: 0.875rem;
  color: #059669;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .contact-sidebar {
    width: 280px;
  }
  
  .chat-main {
    padding: 0.5rem;
  }
  
  .chat-layout {
    border-radius: 0;
  }
}

@media (max-width: 640px) {
  .chat-layout {
    flex-direction: column;
  }

  .contact-sidebar {
    width: 100%;
    height: 40vh;
    border-right: none;
    border-bottom: 1px solid rgba(5, 150, 105, 0.1);
  }

  .chat-window {
    height: 60vh;
  }

  .message-content {
    max-width: 85%;
  }

  .welcome-features {
    flex-direction: column;
    gap: 1rem;
  }
  
  .header-left,
  .header-right {
    width: 60px;
  }
  
  .page-title {
    font-size: 1rem;
  }
}
</style>
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './style.css'

/**
 * 创建 Vue 应用实例
 */
const app = createApp(App)

/**
 * 创建 Pinia 状态管理实例
 */
const pinia = createPinia()

/**
 * 注册所有 Element Plus 图标组件
 * 遍历图标库，将每个图标注册为全局组件
 */
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

/**
 * 使用插件
 * - Pinia: 状态管理
 * - Router: 路由
 * - ElementPlus: UI 组件库
 */
app.use(pinia)
app.use(router)
app.use(ElementPlus)

/**
 * 挂载应用到 #app 容器
 * 启动 Vue 应用
 */
app.mount('#app')
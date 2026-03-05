import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置
 * 定义了整个应用的路由路径和对应的组件
 */
const routes = [
    {
        path: '/',                    // 根路径
        redirect: '/login'            // 默认重定向到登录页
    },
    {
        path: '/login',               // 登录页
        name: 'Login',                // 路由名称
        component: () => import('@/views/Login.vue') // 懒加载组件
    },
    {
        path: '/register',            // 注册页
        name: 'Register',
        component: () => import('@/views/Register.vue')
    },
    {
        path: '/home',                // 首页（商品列表）
        name: 'Home',
        component: () => import('@/views/Home.vue')
    },
    {
        path: '/product/:id',         // 商品详情页（带动态参数）
        name: 'ProductDetail',
        component: () => import('@/views/ProductDetail.vue')
    },
    {
        path: '/publish',             // 发布商品页
        name: 'Publish',
        component: () => import('@/views/Publish.vue')
    },
    {
        path: '/chat',                // 聊天页
        name: 'Chat',
        component: () => import('@/views/Chat.vue')
    },
    {
        path: '/profile',             // 个人信息页
        name: 'Profile',
        component: () => import('@/views/UserProfile.vue')
    }
]

/**
 * 创建路由器实例
 * @param {Object} options - 路由配置选项
 * @param {Array} options.routes - 路由配置数组
 * @param {Function} options.history - 历史记录模式（使用 HTML5 History 模式）
 */
const router = createRouter({
    history: createWebHistory(),  // 使用 HTML5 History 模式
    routes                        // 路由配置
})

export default router
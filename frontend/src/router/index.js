import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        redirect: '/login' // 默认跳转到登录页
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue') // 懒加载
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/Register.vue')
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/Home.vue')
    },
    {
        path: '/product/:id',
        name: 'ProductDetail',
        component: () => import('@/views/ProductDetail.vue')
    },
    {
        path: '/publish',
        name: 'Publish',
        component: () => import('@/views/Publish.vue')
    },
    {
        path: '/chat',
        name: 'Chat',
        component: () => import('@/views/Chat.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
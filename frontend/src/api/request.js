import axios from 'axios'
import { ElMessage } from 'element-plus'

/**
 * 创建 Axios 请求实例
 * 统一的 HTTP 请求配置和拦截器
 */
const request = axios.create({
    baseURL: '/api',  // 使用 Vite 代理的相对路径
    timeout: 10000                      // 请求超时时间（毫秒）
})

/**
 * 请求拦截器
 * 在请求发送前执行，可以添加认证信息、设置请求头等
 */
request.interceptors.request.use(
    config => {
        // TODO: 后续可以在这里添加用户认证 Token
        // const token = localStorage.getItem('token')
        // if (token) {
        //     config.headers.Authorization = `Bearer ${token}`
        // }
        return config
    },
    error => {
        // 处理请求错误
        return Promise.reject(error)
    }
)

/**
 * 响应拦截器
 * 在接收到响应后执行，统一处理响应数据和错误
 */
request.interceptors.response.use(
    response => {
        // 直接返回响应数据，不需要再解析 response.data
        return response.data
    },
    error => {
        // 统一错误处理
        ElMessage.error('请求失败：' + error.message)
        return Promise.reject(error)
    }
)

export default request
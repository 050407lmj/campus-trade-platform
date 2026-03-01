import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const request = axios.create({
    baseURL: 'http://localhost:8080', // 后端接口地址
    timeout: 10000 // 超时时间 10 秒
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        // 这里以后可以添加 Token
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        ElMessage.error('请求失败：' + error.message)
        return Promise.reject(error)
    }
)

export default request
import request from './request'

// 用户注册
export function register(data) {
    return request.post('/api/users/register', data)
}

// 用户登录
export function login(data) {
    return request.post('/api/users/login', data)
}

// 获取所有用户
export function getAllUsers() {
    return request.get('/api/users')
}
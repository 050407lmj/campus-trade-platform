import request from './request'

/**
 * 用户注册
 * @param {Object} data - 用户注册信息
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @param {string} data.email - 邮箱（可选）
 * @returns {Promise} 注册结果
 */
export function register(data) {
    return request.post('/users/register', data)
}

/**
 * 用户登录
 * @param {Object} data - 登录信息
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise} 登录结果，包含用户信息
 */
export function login(data) {
    return request.post('/users/login', data)
}

/**
 * 获取所有用户列表
 * @returns {Promise} 用户列表
 */
export function getAllUsers() {
    return request.get('/users')
}

/**
 * 获取用户详细信息
 * @param {number} id - 用户ID
 * @returns {Promise} 用户详细信息
 */
export function getUserInfo(id) {
    return request.get(`/users/${id}/info`)
}

/**
 * 更新用户信息
 * @param {Object} data - 用户信息对象
 * @param {number} data.id - 用户ID
 * @param {string} data.username - 用户名（只读）
 * @param {string} data.wechatId - 微信ID
 * @param {string} data.major - 专业
 * @param {string} data.grade - 年级
 * @param {string} data.gender - 性别
 * @returns {Promise} 更新结果
 */
export function updateUserInfo(data) {
    return request.put(`/users/${data.id}`, data)
}
import request from './request'

/**
 * 添加收藏
 * @param {number} userId - 用户ID
 * @param {number} productId - 商品ID
 * @returns {Promise} 收藏结果
 */
export function addFavorite(userId, productId) {
    return request.post(`/favorites/${userId}/${productId}`)
}

/**
 * 取消收藏
 * @param {number} userId - 用户ID
 * @param {number} productId - 商品ID
 * @returns {Promise} 取消结果
 */
export function removeFavorite(userId, productId) {
    return request.delete(`/favorites/${userId}/${productId}`)
}

/**
 * 检查是否已收藏
 * @param {number} userId - 用户ID
 * @param {number} productId - 商品ID
 * @returns {Promise} 是否已收藏
 */
export function checkFavorite(userId, productId) {
    return request.get(`/favorites/check/${userId}/${productId}`)
}

/**
 * 获取用户的收藏列表
 * @param {number} userId - 用户ID
 * @returns {Promise} 收藏列表
 */
export function getUserFavorites(userId) {
    return request.get(`/favorites/user/${userId}`)
}

/**
 * 获取商品收藏数
 * @param {number} productId - 商品ID
 * @returns {Promise} 收藏数量
 */
export function getFavoriteCount(productId) {
    return request.get(`/favorites/count/${productId}`)
}

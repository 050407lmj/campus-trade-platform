import request from './request'

/**
 * 创建交易请求
 * @param {Object} data - 交易请求信息
 * @param {number} data.productId - 商品ID
 * @param {number} data.buyerId - 买家ID
 * @param {string} data.tradeMethod - 交易方式
 * @returns {Promise} 创建结果
 */
export function createTradeRequest(data) {
    return request.post('/trade-requests', data)
}

/**
 * 获取商品的交易请求列表
 * @param {number} productId - 商品ID
 * @returns {Promise} 交易请求列表
 */
export function getTradeRequests(productId) {
    return request.get(`/trade-requests/product/${productId}`)
}

/**
 * 更新交易请求状态
 * @param {number} id - 交易请求ID
 * @param {string} status - 新状态（pending/accepted/rejected/completed）
 * @returns {Promise} 更新结果
 */
export function updateTradeStatus(id, status) {
    return request.put(`/api/trade-requests/${id}/status`, { status })
}
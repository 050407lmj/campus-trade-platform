import request from './request'

// 创建交易请求
export function createTradeRequest(data) {
    return request.post('/api/trade-requests', data)
}

// 获取交易请求列表
export function getTradeRequests(productId) {
    return request.get(`/api/trade-requests/product/${productId}`)
}

// 更新交易状态
export function updateTradeStatus(id, status) {
    return request.put(`/api/trade-requests/${id}/status`, { status })
}
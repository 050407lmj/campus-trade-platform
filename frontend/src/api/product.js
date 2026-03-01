import request from './request'

// 发布商品
export function publishProduct(data) {
    return request.post('/api/products', data)
}

// 获取商品列表
export function getProductList() {
    return request.get('/api/products')
}

// 获取商品详情
export function getProductById(id) {
    return request.get(`/api/products/${id}`)
}

// 更新商品状态
export function updateProductStatus(id, status) {
    return request.put(`/api/products/${id}/status`, { status })
}
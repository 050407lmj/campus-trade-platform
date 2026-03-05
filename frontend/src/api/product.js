import request from './request'

/**
 * 发布商品
 * @param {Object} data - 商品信息
 * @param {string} data.name - 商品名称
 * @param {string} data.description - 商品描述
 * @param {number} data.price - 商品价格
 * @param {string} data.category - 商品分类
 * @param {string} data.imageUrl - 商品图片URL
 * @param {number} data.sellerId - 卖家ID
 * @returns {Promise} 发布结果
 */
export function publishProduct(data) {
    return request.post('/products', data)
}

/**
 * 获取商品列表
 * @returns {Promise} 商品列表
 */
export function getProductList() {
    return request.get('/products')
}

/**
 * 根据ID获取商品详情
 * @param {number} id - 商品ID
 * @returns {Promise} 商品详情
 */
export function getProductById(id) {
    return request.get(`/products/${id}`)
}

/**
 * 更新商品状态
 * @param {number} id - 商品ID
 * @param {string} status - 新状态（available/sold/reserved）
 * @returns {Promise} 更新结果
 */
export function updateProductStatus(id, status) {
    return request.put(`/api/products/${id}/status`, { status })
}
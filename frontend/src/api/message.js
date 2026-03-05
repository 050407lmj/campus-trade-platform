import request from './request'

/**
 * 发送聊天消息
 * @param {Object} data - 消息对象
 * @param {number} data.senderId - 发送者ID
 * @param {number} data.receiverId - 接收者ID
 * @param {string} data.content - 消息内容
 * @returns {Promise} 发送结果
 */
export function sendMessage(data) {
    return request.post('/messages', data)
}

/**
 * 获取两个用户之间的聊天记录
 * @param {number} userId1 - 用户1的ID
 * @param {number} userId2 - 用户2的ID
 * @returns {Promise} 聊天记录列表
 */
export function getChatHistory(userId1, userId2) {
    return request.get(`/messages/between/${userId1}/${userId2}`)
}
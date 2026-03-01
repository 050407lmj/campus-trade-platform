import request from './request'

// 发送消息
export function sendMessage(data) {
    return request.post('/api/messages', data)
}

// 获取聊天记录
export function getChatHistory(userId1, userId2) {
    return request.get(`/api/messages/between/${userId1}/${userId2}`)
}
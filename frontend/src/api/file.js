import request from './request'

/**
 * 文件上传 API
 * 
 * 提供图片上传功能，支持单张和批量上传
 */

/**
 * 单张图片上传
 * @param {File} file - 要上传的文件对象
 * @returns {Promise} 上传结果（包含 url、fileName、size）
 */
export function uploadFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    
    return request.post('/files/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 多张图片批量上传
 * @param {File[]} files - 要上传的文件数组
 * @returns {Promise} 上传结果（包含 files 数组）
 */
export function uploadFiles(files) {
    const formData = new FormData()
    files.forEach(file => {
        formData.append('files', file)
    })
    
    return request.post('/files/upload/batch', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src') // 配置 @ 指向 src 目录
    }
  },
  // ✅ 添加以下 server 配置
  server: {
    open: true,  // 启动后自动打开浏览器
    port: 5173,  // 指定端口
    proxy: {
      // 配置 API 代理，将 /api 开头的请求转发到后端
      '/api': {
        target: 'http://localhost:8080',  // 后端地址
        changeOrigin: true  // 修改请求头中的 Origin 为目标 URL
        // 不重写路径，因为后端接口已经是 /api 开头
      }
    }
  }
})
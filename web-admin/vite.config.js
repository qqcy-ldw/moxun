import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/auth': {
        target: 'http://localhost:8082',
        changeOrigin: true
      },
      '/admin': {
        target: 'http://localhost:8082',
        changeOrigin: true
      },
      // 分类图标等静态资源（后端若提供 /icons 静态目录）
      '/icons': {
        target: 'http://localhost:8082',
        changeOrigin: true
      }
    }
  }
})

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath } from 'url'
import path from 'path'

const __dirname = path.dirname(fileURLToPath(import.meta.url))

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3001,
    hmr: {
      overlay: true
    },
    proxy: {
      '/auth': {
        target: 'http://localhost:8082',
        changeOrigin: true
      },
      '/student': {
        target: 'http://localhost:8082',
        changeOrigin: true
      }
    }
  }
})

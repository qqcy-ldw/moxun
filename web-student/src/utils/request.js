/**
 * 学生端 Axios 实例配置
 * 👑 面试考点：Axios 实例配置 - baseURL、timeout、请求/响应拦截器
 */
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '',
  timeout: 30000
})

// 请求拦截器：添加 token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('student_token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理错误
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        localStorage.removeItem('student_token')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    if (error.response) {
      if (error.response.status === 401) {
        localStorage.removeItem('student_token')
        router.push('/login')
      } else if (error.response.status === 403) {
        ElMessage.error('没有权限访问')
      } else if (error.response.status === 404) {
        ElMessage.error('请求资源不存在')
      } else if (error.response.status === 500) {
        ElMessage.error('服务器错误')
      } else {
        ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request

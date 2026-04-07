/**
 * 知界教育平台 - Axios配置
 */
import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';

const service = axios.create({
  baseURL: '',
  timeout: 30000
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败');
      if (res.code === 401) {
        localStorage.removeItem('token');
        router.push('/login');
      }
      return Promise.reject(new Error(res.message || '请求失败'));
    }
    return res;
  },
  error => {
    if (error.response) {
      if (error.response.status === 401) {
        localStorage.removeItem('token');
        router.push('/login');
      } else if (error.response.status === 403) {
        ElMessage.error('没有权限访问');
      } else if (error.response.status === 404) {
        ElMessage.error('请求资源不存在');
      } else if (error.response.status === 500) {
        ElMessage.error('服务器错误');
      } else {
        ElMessage.error(error.response.data?.message || '请求失败');
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接');
    }
    return Promise.reject(error);
  }
);

export default service;

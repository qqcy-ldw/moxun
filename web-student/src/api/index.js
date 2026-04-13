/**
 * 学生端 API 模块
 * 👑 面试考点：统一封装 API 调用，便于管理和维护
 */

import request from '@/utils/request'

// ============ 认证相关 API ============

export const authApi = {
  /**
   * 学生登录（POST /auth/api/login）
   * 参数：{ username, password, captcha }
   */
  login: (data) => request.post('/auth/api/login', data),

  /**
   * 获取当前用户信息（GET /auth/api/me/{id}）
   */
  getUserInfo: (id) => request.get(`/auth/api/me/${id}`),

  /**
   * 退出登录（POST /auth/api/logout）
   */
  logout: () => request.post('/auth/api/logout'),

  /**
   * 更新用户信息（PUT /auth/api/users/{id}）
   */
  updateUser: (id, data) => request.put(`/auth/api/users/${id}`, data),

  /**
   * 上传头像（POST /auth/api/upload/user/avatar）
   */
  uploadAvatar: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/auth/api/upload/user/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}

// ============ 课程相关 API ============

export const courseApi = {
  /**
   * 课程列表（支持分类、难度筛选）
   * GET /student/courses
   */
  list: (params) => request.get('/student/courses', { params }),

  /**
   * 课程详情
   * GET /student/courses/{courseId}
   */
  getById: (id) => request.get(`/student/courses/${id}`),

  /**
   * 选课
   * POST /student/courses/{courseId}/join
   */
  join: (id) => request.post(`/student/courses/${id}/join`),

  /**
   * 取消选课
   * DELETE /student/courses/{courseId}/join
   */
  quit: (id) => request.delete(`/student/courses/${id}/join`),

  /**
   * 我的课程列表
   * GET /student/courses/my
   */
  myList: (params) => request.get('/student/courses/my', { params }),

  /**
   * 我的课程列表（带学习进度）
   * GET /student/courses/my/progress
   */
  myListWithProgress: (params) => request.get('/student/courses/my/progress', { params }),

  /**
   * 收藏课程
   * POST /student/courses/{courseId}/favorite
   */
  favorite: (id) => request.post(`/student/courses/${id}/favorite`),

  /**
   * 取消收藏
   * DELETE /student/courses/{courseId}/favorite
   */
  unfavorite: (id) => request.delete(`/student/courses/${id}/favorite`),

  /**
   * 我的收藏列表
   * GET /student/courses/my/favorites
   */
  myFavorites: (params) => request.get('/student/courses/my/favorites', { params }),

  /**
   * 课程评论列表
   * GET /student/courses/{courseId}/comments
   */
  comments: (id, params) => request.get(`/student/courses/${id}/comments`, { params }),

  /**
   * 发表评论
   * POST /student/courses/{courseId}/comments
   */
  addComment: (id, data) => request.post(`/student/courses/${id}/comments`, data)
}

// ============ 作业相关 API ============

export const homeworkApi = {
  /**
   * 课程作业列表
   * GET /student/assignments/course/{courseId}
   */
  listByCourse: (courseId) => request.get(`/student/assignments/course/${courseId}`),

  /**
   * 作业详情
   * GET /student/assignments/{assignmentId}
   */
  getById: (id) => request.get(`/student/assignments/${id}`),

  /**
   * 提交作业
   * POST /student/assignments/submit
   */
  submit: (data) => request.post('/student/assignments/submit', data),

  /**
   * 我的提交记录（单个作业）
   * GET /student/assignments/{assignmentId}/my-submission
   */
  mySubmission: (id) => request.get(`/student/assignments/${id}/my-submission`),

  /**
   * 我的所有提交记录
   * GET /student/assignments/my
   */
  myList: (params) => request.get('/student/assignments/my', { params })
}

// ============ 学习进度相关 API ============

export const learningApi = {
  /**
   * 课程目录（章节+课时）
   * GET /student/learning/courses/{courseId}/catalog
   */
  catalog: (courseId) => request.get(`/student/learning/courses/${courseId}/catalog`),

  /**
   * 更新学习进度
   * POST /student/learning/progress
   */
  updateProgress: (data) => request.post('/student/learning/progress', data),

  /**
   * 课程学习进度概览
   * GET /student/learning/courses/{courseId}/progress
   */
  courseProgress: (courseId) => request.get(`/student/learning/courses/${courseId}/progress`),

  /**
   * 课时学习进度
   * GET /student/learning/sections/{sectionId}/progress
   */
  sectionProgress: (sectionId) => request.get(`/student/learning/sections/${sectionId}/progress`)
}

// ============ 问答社区 API ============

export const questionApi = {
  /**
   * 问题列表（支持关键词搜索和课程筛选）
   * GET /student/questions
   */
  list: (params) => request.get('/student/questions', { params }),

  /**
   * 问题详情（含回答）
   * GET /student/questions/{questionId}
   */
  getById: (id) => request.get(`/student/questions/${id}`),

  /**
   * 发布问题
   * POST /student/questions
   */
  create: (data) => request.post('/student/questions', data),

  /**
   * 发布回答
   * POST /student/questions/answers
   */
  answer: (data) => request.post('/student/questions/answers', data),

  /**
   * 点赞回答
   * POST /student/questions/answers/{answerId}/like
   */
  like: (id) => request.post(`/student/questions/answers/${id}/like`),

  /**
   * 取消点赞
   * DELETE /student/questions/answers/{answerId}/like
   */
  unlike: (id) => request.delete(`/student/questions/answers/${id}/like`)
}

// ============ 通知消息 API ============

export const notificationApi = {
  /**
   * 通知列表
   * GET /student/notifications
   */
  list: (params) => request.get('/student/notifications', { params }),

  /**
   * 未读数量
   * GET /student/notifications/unread-count
   */
  unreadCount: () => request.get('/student/notifications/unread-count'),

  /**
   * 标记已读
   * PUT /student/notifications/{id}/read
   */
  markRead: (id) => request.put(`/student/notifications/${id}/read`),

  /**
   * 全部已读
   * PUT /student/notifications/read-all
   */
  markAllRead: () => request.put('/student/notifications/read-all')
}

// ============ 课程分类 API ============

export const categoryApi = {
  /**
   * 获取课程分类列表
   * GET /student/categories
   */
  list: () => request.get('/student/categories'),

  /**
   * 获取分类详情
   * GET /student/categories/{id}
   */
  getById: (id) => request.get(`/student/categories/${id}`)
}

// 知界教育平台 - API模块

import request from '@/utils/request';

// 认证相关
export const authApi = {
  login: (data) => request.post('/auth/api/login', data),
  logout: () => request.post('/auth/api/logout'),
  getUserInfo: (id) => request.get(`/auth/api/me/${id}`),
  updateProfile: (id, data) => request.put(`/auth/api/users/${id}`, data)
};

// 用户管理
export const userApi = {
  list: (params) => request.get('/admin/users', { params }),
  add: (data) => request.post('/admin/users', data),
  update: (id, data) => request.put(`/admin/users/${id}`, data),
  delete: (ids) => request.delete('/admin/users', { data: ids }),
  setStatus: (id, status) => request.put(`/admin/users/id/${status}?id=${id}`)
};

// 课程管理
export const courseApi = {
  list: (params) => request.get('/admin/courses', { params }),
  getById: (id) => request.get(`/admin/courses/${id}`),
  add: (data) => request.post('/admin/courses', data),
  update: (id, data) => request.put(`/admin/courses/${id}`, data),
  delete: (id) => request.delete(`/admin/courses/${id}`),
  getTeachers: () => request.get('/admin/courses/teacher'),
  // 👑 面试考点：GET 接口传查询参数用 { params }，后端 @RequestParam 接收
  // ⚠️ 注意：后端实际路径是 /admin/courseCategories/list（不是 /category/list）
  getCategories: () => request.get('/admin/courseCategories/list')
};

// 角色管理
export const roleApi = {
  list: (params) => request.get('/admin/roles', { params }),
  getById: (id) => request.get(`/admin/roles/${id}`),
  add: (data) => request.post('/admin/roles', data),
  update: (id, data) => request.put(`/admin/roles/${id}`, data),
  delete: (id) => request.delete(`/admin/roles/${id}`)
};

// 菜单管理
export const menuApi = {
  tree: () => request.get('/admin/menus/tree'),
  list: (params) => request.get('/admin/menus', { params }),
  getById: (id) => request.get(`/admin/menus/${id}`),
  add: (data) => request.post('/admin/menus', data),
  update: (id, data) => request.put(`/admin/menus/${id}`, data),
  delete: (id) => request.delete(`/admin/menus/${id}`)
};

// 问答管理
export const questionApi = {
  list: (params) => request.get('/admin/questions', { params }),
  getDetail: (id) => request.get(`/admin/questions/${id}`),
  delete: (id) => request.delete(`/admin/questions/${id}`),
  acceptAnswer: (answerId) => request.put(`/admin/questions/answers/${answerId}/accept`)
};

// 统计数据
export const statisticsApi = {
  // 👑 面试考点：GET 请求不带 body，POST 请求带 body
  // 后端使用 @RequestBody 接收，前端必须用 POST + 第二个参数传 data
  dashboard: () => request.get('/admin/statistics/dashboard'),
  user: (data) => request.post('/admin/statistics/user', data),
  course: (data) => request.post('/admin/statistics/course', data),
  homework: (data) => request.post('/admin/statistics/homework', data),
  question: (data) => request.post('/admin/statistics/question', data),
  study: (data) => request.post('/admin/statistics/study', data),
  trend: (data) => request.post('/admin/statistics/trend', data),
  rank: (data) => request.post('/admin/statistics/rank', data)
};

// 作业管理
export const assignmentApi = {
  // 👑 面试考点：GET 接口带 query 参数用 { params }，后端 @RequestParam 接收
  // courseId 为可选筛选条件，page/pageSize 用于分页
  list: (params) => request.get('/admin/assignments', { params }),
  // 👑 面试考点：路径变量用模板字符串注入，{id} 对应后端 @PathVariable
  getById: (id) => request.get(`/admin/assignments/${id}`),
  // 👑 面试考点：POST + body 传给后端 @RequestBody
  add: (data) => request.post('/admin/assignments', data),
  update: (id, data) => request.put(`/admin/assignments/${id}`, data),
  delete: (id) => request.delete(`/admin/assignments/${id}`),
  // 获取某作业的提交列表，graded 筛选：0=待批改，1=已批改
  submissions: (assignmentId, params) =>
    request.get(`/admin/assignments/${assignmentId}/submissions`, { params }),
  // 👑 面试考点：批改作业也是 POST + body
  grade: (data) => request.post('/admin/assignments/grade', data)
};

// 章节管理
export const chapterApi = {
  // 👑 面试考点：按课程ID查询章节列表，返回 List<ChapterVO>
  listByCourse: (courseId) => request.get(`/admin/chapters/course/${courseId}`),
  getById: (id) => request.get(`/admin/chapters/${id}`),
  add: (data) => request.post('/admin/chapters', data),
  update: (id, data) => request.put(`/admin/chapters/${id}`, data),
  delete: (id) => request.delete(`/admin/chapters/${id}`)
};

// 课程评论管理
export const commentApi = {
  // 👑 面试考点：GET 接口支持 rating/courseId 筛选，分页返回 PageResult
  list: (params) => request.get('/admin/comments', { params }),
  getById: (id) => request.get(`/admin/comments/${id}`),
  delete: (id) => request.delete(`/admin/comments/${id}`)
};

// 课程分类管理
export const categoryApi = {
  // 👑 面试考点：分页列表 GET，删除接口实际用的是 GET（不是 DELETE）
  list: (params) => request.get('/admin/courseCategories/list', { params }),
  children: (parentId) => request.get(`/admin/courseCategories/children/${parentId}`),
  add: (data) => request.post('/admin/courseCategories/category/add', data),
  update: (data) => request.post('/admin/courseCategories/category/update', data),
  // ⚠️ 注意：后端删除接口是 @GetMapping，不是 @DeleteMapping
  delete: (parentId) => request.get(`/admin/courseCategories/category/delete/${parentId}`)
};

// 课时管理
export const sectionApi = {
  // 👑 面试考点：按章节ID查询课时列表，返回 List<SectionVO>
  listByChapter: (chapterId) => request.get(`/admin/sections/chapter/${chapterId}`),
  getById: (id) => request.get(`/admin/sections/${id}`),
  add: (data) => request.post('/admin/sections', data),
  update: (id, data) => request.put(`/admin/sections/${id}`, data),
  delete: (id) => request.delete(`/admin/sections/${id}`)
};

// 操作日志管理
export const actionLogApi = {
  // 分页查询日志列表
  list: (data) => request.post('/admin/action-log/list', data),
  // 获取日志详情
  getById: (id) => request.get(`/admin/action-log/${id}`),
  // 获取用户最近操作记录
  getUserRecent: (userId, limit) => request.get(`/admin/action-log/user/${userId}`, { params: { limit } }),
  // 获取所有操作类型
  getTypes: () => request.get('/admin/action-log/types'),
  // 清理过期日志
  clean: (days) => request.delete('/admin/action-log/clean', { params: { days } })
};

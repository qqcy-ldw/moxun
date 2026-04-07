import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/system/users.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'roles',
        name: 'Roles',
        component: () => import('@/views/system/roles.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'menus',
        name: 'Menus',
        component: () => import('@/views/system/menus.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'courses',
        name: 'Courses',
        component: () => import('@/views/course/index.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'questions',
        name: 'Questions',
        component: () => import('@/views/question/index.vue'),
        meta: { title: '问答管理' }
      },
      {
        path: 'homework',
        name: 'Homework',
        component: () => import('@/views/homework/index.vue'),
        meta: { title: '作业管理' }
      },
      {
        path: 'chapters',
        name: 'Chapters',
        component: () => import('@/views/chapter/index.vue'),
        meta: { title: '章节管理' }
      },
      {
        path: 'comments',
        name: 'Comments',
        component: () => import('@/views/comment/index.vue'),
        meta: { title: '课程评论' }
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('@/views/category/index.vue'),
        meta: { title: '课程分类' }
      },
      {
        path: 'lessons',
        name: 'Lessons',
        component: () => import('@/views/lesson/index.vue'),
        meta: { title: '课时管理' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/system/logs.vue'),
        meta: { title: '操作日志' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

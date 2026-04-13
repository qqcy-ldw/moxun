import { createRouter, createWebHistory } from 'vue-router'

// 👑 面试考点：Vue Router 路由配置
// 学生端路由结构：
// - 布局页（Layout）：包含顶部导航 + 侧边栏
// - 首页：课程推荐、学习概览
// - 课程相关：课程列表、课程详情、学习页面
// - 作业相关：作业列表、作业详情、提交作业
// - 问答社区：问题列表、问题详情、发布问题
// - 个人中心：我的课程、我的收藏、通知消息

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页', icon: 'House' }
      },
      {
        path: 'courses',
        name: 'CourseList',
        component: () => import('@/views/course/list.vue'),
        meta: { title: '全部课程', icon: 'Reading' }
      },
      {
        path: 'courses/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/detail.vue'),
        meta: { title: '课程详情' }
      },
      {
        path: 'learn/:courseId/:sectionId?',
        name: 'CourseLearn',
        component: () => import('@/views/course/learn.vue'),
        meta: { title: '课程学习' }
      },
      {
        path: 'my-courses',
        name: 'MyCourses',
        component: () => import('@/views/my/courses.vue'),
        meta: { title: '我的课程', icon: 'Collection' }
      },
      {
        path: 'my-favorites',
        name: 'MyFavorites',
        component: () => import('@/views/my/favorites.vue'),
        meta: { title: '我的收藏', icon: 'Star' }
      },
      {
        path: 'my-homework',
        name: 'MyHomework',
        component: () => import('@/views/homework/index.vue'),
        meta: { title: '我的作业', icon: 'Edit' }
      },
      {
        path: 'homework/:id',
        name: 'HomeworkDetail',
        component: () => import('@/views/homework/detail.vue'),
        meta: { title: '作业详情' }
      },
      {
        path: 'questions',
        name: 'QuestionList',
        component: () => import('@/views/question/list.vue'),
        meta: { title: '问答社区', icon: 'ChatDotRound' }
      },
      {
        path: 'questions/:id',
        name: 'QuestionDetail',
        component: () => import('@/views/question/detail.vue'),
        meta: { title: '问题详情' }
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: () => import('@/views/notification/index.vue'),
        meta: { title: '通知消息', icon: 'Bell' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 全局路由守卫：验证登录状态
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 知界教育`
  }

  // 验证登录状态（学生端 token 存在 localStorage）
  const token = localStorage.getItem('student_token')
  if (!token && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router

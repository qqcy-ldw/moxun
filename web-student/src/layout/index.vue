<template>
  <div class="layout-container">
    <!-- 顶部导航 -->
    <header class="layout-header">
      <div class="header-left">
        <el-button text @click="sidebarCollapsed = !sidebarCollapsed" class="collapse-btn">
          <el-icon :size="20">
            <component :is="sidebarCollapsed ? 'Expand' : 'Fold'" />
          </el-icon>
        </el-button>
        <router-link to="/home" class="logo">
          <span class="logo-text">知界教育</span>
          <span class="logo-sub">学生端</span>
        </router-link>
      </div>

      <div class="header-center">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索课程..."
          class="header-search"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <div class="header-right">
        <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
          <el-button text @click="goNotifications">
            <el-icon :size="20"><Bell /></el-icon>
          </el-button>
        </el-badge>

        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="36" :src="userInfo?.avatar" class="user-avatar">
              {{ userInfo?.username?.charAt(0) || '学' }}
            </el-avatar>
            <div class="user-detail" v-if="!sidebarCollapsed">
              <span class="username">{{ userInfo?.username || '学生' }}</span>
              <el-tag size="small" type="success">Lv.5 学霸</el-tag>
            </div>
            <el-icon><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item command="my-courses">
                <el-icon><Collection /></el-icon>
                我的课程
              </el-dropdown-item>
              <el-dropdown-item command="my-favorites">
                <el-icon><Star /></el-icon>
                我的收藏
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="layout-body">
      <!-- 侧边栏 -->
      <aside class="layout-sidebar" :class="{ collapsed: sidebarCollapsed }">
        <el-menu
          :default-active="activeMenu"
          :router="true"
          class="sidebar-menu"
          :collapse="sidebarCollapsed"
          :collapse-transition="true"
        >
          <el-menu-item index="/home">
            <el-icon><House /></el-icon>
            <template #title>首页</template>
          </el-menu-item>

          <el-menu-item index="/courses">
            <el-icon><Reading /></el-icon>
            <template #title>全部课程</template>
          </el-menu-item>

          <el-menu-item index="/my-courses">
            <el-icon><Collection /></el-icon>
            <template #title>我的课程</template>
          </el-menu-item>

          <el-menu-item index="/my-favorites">
            <el-icon><Star /></el-icon>
            <template #title>我的收藏</template>
          </el-menu-item>

          <el-menu-item index="/my-homework">
            <el-icon><Edit /></el-icon>
            <template #title>我的作业</template>
          </el-menu-item>

          <el-menu-item index="/questions">
            <el-icon><ChatDotRound /></el-icon>
            <template #title>问答社区</template>
          </el-menu-item>

          <el-menu-item index="/notifications">
            <el-icon><Bell /></el-icon>
            <template #title>通知消息</template>
          </el-menu-item>
        </el-menu>

        <!-- 底部学习进度 -->
        <div class="sidebar-footer" v-if="!sidebarCollapsed">
          <div class="footer-label">今日学习目标</div>
          <el-progress :percentage="60" :stroke-width="6" :show-text="false" />
          <div class="footer-hint">已完成 60% 的目标</div>
        </div>
      </aside>

      <!-- 主内容区 -->
      <main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="page" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Bell, House, Reading, Collection, Star, Edit, ChatDotRound,
  User, SwitchButton, ArrowDown, Search, Fold, Expand
} from '@element-plus/icons-vue'
import { notificationApi, authApi } from '@/api'

const router = useRouter()
const route = useRoute()

const sidebarCollapsed = ref(false)
const searchKeyword = ref('')
const unreadCount = ref(0)

// 从 localStorage 获取用户信息
const userInfo = computed(() => {
  try {
    const user = localStorage.getItem('student_user')
    if (!user || user === 'undefined') return null
    return JSON.parse(user)
  } catch {
    return null
  }
})

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 获取未读通知数
const fetchUnreadCount = async () => {
  try {
    const res = await notificationApi.unreadCount()
    unreadCount.value = res.data?.count || 0
  } catch (error) {
    console.error('获取未读通知数失败', error)
  }
}

// 跳转通知页面
const goNotifications = () => {
  router.push('/notifications')
}

// 搜索课程
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/courses', query: { keyword: searchKeyword.value } })
  }
}

// 下拉菜单命令
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'my-courses':
      router.push('/my-courses')
      break
    case 'my-favorites':
      router.push('/my-favorites')
      break
    case 'logout':
      try {
        await authApi.logout()
      } catch (e) {
        // 忽略错误
      }
      localStorage.removeItem('student_token')
      localStorage.removeItem('student_user')
      ElMessage.success('已退出登录')
      router.push('/login')
      break
  }
}

onMounted(() => {
  fetchUnreadCount()
})
</script>

<style lang="scss" scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  height: 70px;
  background: var(--ink-white);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  padding: 0 var(--space-lg);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  width: 240px;

  .collapse-btn {
    padding: 8px;
    color: var(--ink-gray-light);
  }

  .logo {
    display: flex;
    align-items: center;
    gap: var(--space-sm);
    text-decoration: none;

    .logo-text {
      font-size: 20px;
      font-weight: 700;
      color: var(--ink-blue);
    }

    .logo-sub {
      font-size: 12px;
      color: var(--ink-gray-lighter);
    }
  }
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;

  .header-search {
    width: 400px;

    :deep(.el-input__wrapper) {
      border-radius: var(--radius-xl);
      box-shadow: none;
      border: 1px solid var(--border-light);
      transition: border-color var(--transition-fast), box-shadow var(--transition-fast);

      &:focus-within {
        border-color: var(--ink-blue);
        box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.15);
      }
    }
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-md);

  .user-info {
    display: flex;
    align-items: center;
    gap: var(--space-sm);
    cursor: pointer;
    padding: 4px 8px;
    border-radius: var(--radius-md);
    transition: background-color var(--transition-fast);

    &:hover {
      background-color: var(--ink-bg);
    }

    .user-detail {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: 2px;

      .username {
        font-size: 14px;
        font-weight: 500;
        color: var(--ink-gray);
        line-height: 1;
      }
    }
  }
}

.layout-body {
  flex: 1;
  display: flex;
}

.layout-sidebar {
  width: 200px;
  background: var(--ink-white);
  border-right: 1px solid var(--border-light);
  position: sticky;
  top: 70px;
  height: calc(100vh - 70px);
  overflow-y: auto;
  overflow-x: hidden;
  transition: width var(--transition-normal);
  display: flex;
  flex-direction: column;

  &.collapsed {
    width: 64px;

    :deep(.el-menu--collapse) {
      width: 64px;
    }
  }

  :deep(.el-menu) {
    border: none;

    &.el-menu--collapse {
      .el-menu-item {
        padding: 0 !important;
        justify-content: center;
      }
    }
  }

  :deep(.el-menu-item) {
    height: 52px;
    line-height: 52px;
    border-radius: var(--radius-md);
    margin: 2px 8px;
    padding: 0 12px !important;
    transition: background-color var(--transition-fast);

    &.is-active {
      background: var(--gradient-primary);
      color: white;
      box-shadow: var(--shadow-button);

      .el-icon {
        color: white;
      }
    }

    &:hover:not(.is-active) {
      background-color: rgba(74, 144, 226, 0.06);
    }
  }
}

.sidebar-footer {
  margin-top: auto;
  padding: var(--space-md);
  border-top: 1px solid var(--border-light);

  .footer-label {
    font-size: 12px;
    color: var(--ink-gray-light);
    margin-bottom: var(--space-sm);
  }

  .footer-hint {
    font-size: 11px;
    color: var(--ink-gray-lighter);
    margin-top: var(--space-xs);
  }
}

.layout-main {
  flex: 1;
  padding: var(--space-lg);
  min-height: calc(100vh - 70px);
  background-color: var(--ink-bg);
}

.page-enter-active,
.page-leave-active {
  transition: opacity var(--transition-normal), transform var(--transition-normal);
}

.page-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.page-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}
</style>

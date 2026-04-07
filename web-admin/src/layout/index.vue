<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'is-collapse': isCollapse }">
      <div class="sidebar-header">
        <div class="logo">
          <span class="logo-text">知界</span>
        </div>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapse"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <template #title>工作台</template>
        </el-menu-item>
        <el-sub-menu index="/courses">
          <template #title>
            <el-icon><Reading /></el-icon>
            <span>课程管理</span>
          </template>
          <el-menu-item index="/courses">课程列表</el-menu-item>
          <el-menu-item index="/chapters">章节管理</el-menu-item>
          <el-menu-item index="/lessons">课时管理</el-menu-item>
          <el-menu-item index="/categories">课程分类</el-menu-item>
          <el-menu-item index="/comments">课程评论</el-menu-item>
          <el-menu-item index="/homework">作业管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/questions">
          <el-icon><ChatDotRound /></el-icon>
          <template #title>问答管理</template>
        </el-menu-item>
        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/users">用户管理</el-menu-item>
          <el-menu-item index="/roles">角色管理</el-menu-item>
          <el-menu-item index="/menus">菜单管理</el-menu-item>
          <el-menu-item index="/logs">
            <el-icon><Document /></el-icon>
            <template #title>操作日志</template>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </aside>

    <!-- 主内容区 -->
    <div class="main-wrapper">
      <!-- 顶部导航 -->
      <header class="header">
        <div class="header-left">
          <el-button text @click="toggleSidebar">
            <el-icon size="20"><Expand v-if="isCollapse" /><Fold v-else /></el-icon>
          </el-button>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentRoute.meta?.title">
              {{ currentRoute.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" style="background-color: #2A5C7A;">
                {{ userStore.userInfo?.username?.charAt(0) || '管' }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.username || '管理员' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 页面内容 -->
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { HomeFilled, Setting, Reading, ChatDotRound, Expand, Fold, ArrowDown, Document } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const currentRoute = route

const activeMenu = computed(() => route.path)

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await authApi.logout()
      userStore.logout()
      router.push('/login')
    } catch (error) {
      userStore.logout()
      router.push('/login')
    }
  } else if (command === 'profile') {
    router.push('/profile')
  }
}

onMounted(async () => {
  const userId = localStorage.getItem('userId')
  if (userId) {
    try {
      await userStore.getUserInfo(userId)
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.layout-container {
  display: flex;
  width: 100%;
  height: 100vh;
  background-color: $ink-white;
}

// 侧边栏
.sidebar {
  width: 220px;
  height: 100vh;
  background-color: $ink-white;
  border-right: 1px solid $border-color;
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
  
  &.is-collapse {
    width: 64px;
  }
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid $border-light;
  background-color: $ink-white;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-text {
  font-size: 24px;
  font-weight: 500;
  color: $ink-blue;
  letter-spacing: 4px;
  font-family: "STKaiti", "KaiTi", serif;
  
  .is-collapse & {
    display: none;
  }
}

.sidebar-menu {
  flex: 1;
  padding: 12px 0;
  border-right: none;
  background-color: transparent;
  
  &:not(.el-menu--collapse) {
    width: 220px;
  }
  
  .el-menu-item, .el-sub-menu__title {
    height: 48px;
    line-height: 48px;
    margin: 2px 8px;
    border-radius: $radius-md;
    transition: $transition;
    
    &:hover {
      background-color: rgba($ink-blue, 0.05);
    }
    
    &.is-active {
      background-color: rgba($ink-blue, 0.1);
      color: $ink-blue;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 8px;
        bottom: 8px;
        width: 3px;
        background-color: $ink-blue;
        border-radius: 0 2px 2px 0;
      }
    }
  }

  // 👑 面试考点：仅用「直接子选择器」限定一级菜单行，二级 .el-menu-item 不在此命中
  // ⚠️ 注意：子菜单标题末尾还有 .el-sub-menu__icon-arrow，必须用 :not 排除，否则会挤乱箭头布局
  &:not(.el-menu--collapse) {
    // 一级 item 与一级 sub-menu 标题的 padding-left 受 --el-menu-level 影响可能不一致，强制统一左缘
    :deep(> .el-menu-item),
    :deep(> .el-sub-menu > .el-sub-menu__title) {
      padding-inline-start: 16px !important;
    }
  }

  :deep(> .el-menu-item) {
    display: flex;
    align-items: center;
  }

  :deep(> .el-menu-item > .el-icon),
  :deep(> .el-sub-menu > .el-sub-menu__title > .el-icon:not(.el-sub-menu__icon-arrow)) {
    width: 24px !important;
    min-width: 24px !important;
    height: 24px;
    margin-inline-end: 8px !important;
    margin-inline-start: 0 !important;
    flex-shrink: 0;
    display: inline-flex !important;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    box-sizing: border-box;
  }

  // 线框/实心 SVG 视觉重心不同，统一 svg 尺寸减少「看起来没对齐」
  :deep(> .el-menu-item > .el-icon svg),
  :deep(> .el-sub-menu > .el-sub-menu__title > .el-icon:not(.el-sub-menu__icon-arrow) svg) {
    width: 1em;
    height: 1em;
  }
}

// 主内容区
.main-wrapper {
  flex: 1;
  margin-left: 220px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  .sidebar.is-collapse + & {
    margin-left: 64px;
  }
}

// 顶部导航
.header {
  height: 60px;
  background-color: $ink-white;
  border-bottom: 1px solid $border-color;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 99;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: $radius-md;
  transition: $transition;
  
  &:hover {
    background-color: rgba($ink-blue, 0.05);
  }
}

.username {
  font-size: 14px;
  color: $ink-gray;
}

// 页面内容
.main-content {
  flex: 1;
  padding: 24px;
  background-color: $ink-white;
}
</style>

<template>
  <div class="notification-page page-container">
    <div class="page-header">
      <h2>通知消息</h2>
      <el-button v-if="hasUnread" type="primary" text @click="handleMarkAllRead">全部已读</el-button>
    </div>

    <div class="filter-tabs">
      <el-radio-group v-model="filterType" @change="handleFilterChange">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="0">未读</el-radio-button>
        <el-radio-button label="1">已读</el-radio-button>
      </el-radio-group>
    </div>

    <div class="notification-list" v-loading="loading">
      <div
        v-for="item in notificationList"
        :key="item.id"
        class="notification-item ink-card"
        :class="{ unread: item.isRead === 0 }"
        @click="handleRead(item)"
      >
        <div class="notification-icon">
          <el-icon :size="24" :color="getIconColor(item.type)">
            <component :is="getIcon(item.type)" />
          </el-icon>
        </div>
        <div class="notification-content">
          <h4>{{ item.title }}</h4>
          <p>{{ item.content }}</p>
          <span class="notification-time">{{ formatTime(item.createTime) }}</span>
        </div>
        <div class="notification-status" v-if="item.isRead === 0">
          <el-badge is-dot />
        </div>
      </div>

      <el-empty v-if="!loading && notificationList.length === 0" description="暂无通知" />
    </div>

    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="pagination.page"
        :page-size="pagination.pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchList"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Bell, ChatDotRound, Message, Check } from '@element-plus/icons-vue'
import { notificationApi } from '@/api'

const loading = ref(false)
const notificationList = ref([])
const total = ref(0)
const filterType = ref('')

const pagination = reactive({ page: 1, pageSize: 15 })

// 判断是否有未读
const hasUnread = computed(() => {
  return notificationList.value.some(n => n.isRead === 0)
})

const fetchList = async () => {
  loading.value = true
  try {
    const isRead = filterType.value === '' ? undefined : Number(filterType.value)
    const res = await notificationApi.list({
      isRead,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    notificationList.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error) {
    ElMessage.error('获取通知失败')
  } finally {
    loading.value = false
  }
}

const handleFilterChange = () => {
  pagination.page = 1
  fetchList()
}

const handleRead = async (item) => {
  if (item.isRead === 0) {
    try {
      await notificationApi.markRead(item.id)
      item.isRead = 1
    } catch (error) {
      console.error('标记已读失败', error)
    }
  }
}

const handleMarkAllRead = async () => {
  try {
    await notificationApi.markAllRead()
    ElMessage.success('已全部标记为已读')
    fetchList()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const getIcon = (type) => {
  const map = {
    'system': Bell,
    'course': Message,
    'comment': ChatDotRound,
    'qa': ChatDotRound
  }
  return map[type] || Bell
}

const getIconColor = (type) => {
  const map = {
    'system': '#2563eb',
    'course': '#22c55e',
    'comment': '#f59e0b',
    'qa': '#8b5cf6'
  }
  return map[type] || '#2563eb'
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchList()
})
</script>

<style lang="scss" scoped>
.notification-page { max-width: 800px; margin: 0 auto; }
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-md);
  h2 { font-size: 22px; font-weight: 600; }
}

.filter-tabs { margin-bottom: var(--space-md); }

.notification-list { display: flex; flex-direction: column; gap: var(--space-sm); }

.notification-item {
  display: flex;
  gap: var(--space-sm);
  padding: var(--space-md);
  cursor: pointer;
  transition: box-shadow var(--transition-fast);

  &:hover { box-shadow: var(--shadow-hover); }

  &.unread {
    background: rgba(74, 144, 226, 0.04);
    border-left: 3px solid var(--ink-blue);
  }

  .notification-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: var(--ink-bg);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .notification-content {
    flex: 1;

    h4 { font-size: 14px; font-weight: 600; margin-bottom: 4px; }
    p { font-size: 13px; color: var(--ink-gray-light); margin-bottom: var(--space-sm); }

    .notification-time {
      font-size: 12px;
      color: var(--ink-gray-lighter);
    }
  }

  .notification-status {
    display: flex;
    align-items: center;
  }
}

.pagination { display: flex; justify-content: center; margin-top: var(--space-md); }
</style>
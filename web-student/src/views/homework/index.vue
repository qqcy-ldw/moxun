<template>
  <div class="homework-page page-container">
    <div class="page-header">
      <h2>我的作业</h2>
    </div>

    <div class="homework-list" v-loading="loading">
      <div v-for="item in homeworkList" :key="item.id" class="homework-card ink-card" @click="goDetail(item)">
        <div class="homework-status">
          <el-tag :type="getStatusType(item)" size="small">
            {{ getStatusText(item) }}
          </el-tag>
        </div>
        <div class="homework-info">
          <h3>{{ item.assignmentTitle }}</h3>
          <p class="homework-course">{{ item.courseTitle }}</p>
          <div class="homework-meta">
            <span>截止: {{ formatDate(item.deadline) }}</span>
            <span v-if="item.submitTime">提交: {{ formatDate(item.submitTime) }}</span>
            <span v-if="item.score">得分: {{ item.score }}</span>
          </div>
        </div>
      <div class="homework-actions">
        <el-button type="primary" size="small" @click.stop="goDetail(item)" class="primary-btn">
          {{ item.submitTime ? '查看' : '去提交' }}
        </el-button>
      </div>
      </div>

      <el-empty v-if="!loading && homeworkList.length === 0" description="暂无作业" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { homeworkApi } from '@/api'

const router = useRouter()
const loading = ref(false)
const homeworkList = ref([])

const fetchMyHomework = async () => {
  loading.value = true
  try {
    const res = await homeworkApi.myList({ page: 1, pageSize: 50 })
    homeworkList.value = res.data?.list || res.data || []
  } catch (error) {
    ElMessage.error('获取作业失败')
  } finally {
    loading.value = false
  }
}

const goDetail = (item) => {
  // 后端 VO 字段可能为 null，依次尝试：assignmentId（驼峰）、assignment_id（下划线）、id
  const id = item.assignmentId ?? item.assignment_id ?? item.id
  if (!id) {
    ElMessage.warning('作业信息不完整，请刷新重试')
    return
  }
  router.push(`/homework/${id}`)
}

const getStatusType = (item) => {
  if (item.score !== null && item.score !== undefined) return 'success'
  if (item.submitTime) return 'warning'
  return 'info'
}

const getStatusText = (item) => {
  if (item.score !== null && item.score !== undefined) return '已评分'
  if (item.submitTime) return '待评分'
  return '未提交'
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchMyHomework()
})
</script>

<style lang="scss" scoped>
.homework-page { max-width: 900px; margin: 0 auto; }
.page-header { margin-bottom: var(--space-md); h2 { font-size: 22px; font-weight: 600; } }

.homework-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.homework-card {
  display: flex;
  gap: var(--space-md);
  padding: var(--space-md);
  cursor: pointer;
  transition: box-shadow var(--transition-fast);

  &:hover { box-shadow: var(--shadow-hover); }

  .homework-status {
    width: 60px;
    flex-shrink: 0;
    display: flex;
    align-items: flex-start;
    padding-top: 4px;
  }

  .homework-info {
    flex: 1;

    h3 {
      font-size: 15px;
      font-weight: 600;
      margin-bottom: var(--space-xs);
    }

    .homework-course {
      font-size: 13px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-sm);
    }

    .homework-meta {
      display: flex;
      gap: var(--space-md);
      font-size: 12px;
      color: var(--ink-gray-lighter);
    }
  }

  .homework-actions {
    display: flex;
    align-items: center;
  }
}
</style>
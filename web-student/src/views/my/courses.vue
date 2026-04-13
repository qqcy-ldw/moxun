<template>
  <div class="my-courses-page page-container">
    <div class="page-header">
      <h2>我的课程</h2>
    </div>

    <div class="course-list" v-loading="loading">
      <div
        v-for="course in courseList"
        :key="course.id"
        class="course-card ink-card"
        @click="goLearn(course)"
      >
        <div class="course-cover">
          <el-image v-if="course.coverImage" :src="course.coverImage" fit="cover" />
          <div v-else class="cover-placeholder">
            <el-icon :size="32"><VideoPlay /></el-icon>
          </div>
        </div>

        <div class="course-info">
          <h3>{{ course.title }}</h3>
          <p class="course-teacher">{{ course.teacher }}</p>

          <div class="course-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: (course.progress || 0) + '%' }"></div>
            </div>
            <span class="progress-text">{{ course.progress || 0 }}%</span>
          </div>
        </div>

        <div class="course-actions">
          <el-button type="primary" size="small" class="primary-btn">
            {{ course.progress > 0 ? '继续学习' : '开始学习' }}
          </el-button>
          <el-button size="small" @click.stop="handleQuit(course)">退出课程</el-button>
        </div>
      </div>

      <el-empty v-if="!loading && courseList.length === 0" description="暂无选课记录" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { VideoPlay } from '@element-plus/icons-vue'
import { courseApi } from '@/api'

const router = useRouter()
const loading = ref(false)
const courseList = ref([])

const fetchMyCourses = async () => {
  loading.value = true
  try {
    const res = await courseApi.myListWithProgress({ page: 1, pageSize: 100 })
    courseList.value = res.data || []
  } catch (error) {
    ElMessage.error('获取我的课程失败')
  } finally {
    loading.value = false
  }
}

const goLearn = (course) => {
  router.push(`/learn/${course.id}`)
}

const handleQuit = async (course) => {
  try {
    await ElMessageBox.confirm(`确定退出课程《${course.title}》吗？`, '提示')
    await courseApi.quit(course.id)
    ElMessage.success('已退出课程')
    fetchMyCourses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('退出失败')
    }
  }
}

onMounted(() => {
  fetchMyCourses()
})
</script>

<style lang="scss" scoped>
.my-courses-page {
  max-width: 1000px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: var(--space-md);
  h2 { font-size: 22px; font-weight: 600; }
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.course-card {
  display: flex;
  gap: var(--space-md);
  padding: var(--space-md);
  cursor: pointer;
  transition: box-shadow var(--transition-fast);

  &:hover { box-shadow: var(--shadow-hover); }

  .course-cover {
    width: 200px;
    height: 112px;
    border-radius: var(--radius-md);
    overflow: hidden;
    flex-shrink: 0;

    .el-image, .cover-placeholder {
      width: 100%;
      height: 100%;
    }

    .cover-placeholder {
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--border-light);
      color: var(--ink-gray-lighter);
    }
  }

  .course-info {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: var(--space-xs);
    }

    .course-teacher {
      font-size: 13px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-sm);
    }

    .course-progress {
      display: flex;
      align-items: center;
      gap: var(--space-sm);

      .progress-bar {
        flex: 1;
        height: 6px;
        background: var(--border-light);
        border-radius: var(--radius-sm);

        .progress-fill {
          height: 100%;
          background: var(--gradient-primary);
          border-radius: var(--radius-sm);
        }
      }

      .progress-text {
        font-size: 12px;
        color: var(--ink-blue);
        font-weight: 600;
      }
    }
  }

  .course-actions {
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: var(--space-sm);
  }
}
</style>
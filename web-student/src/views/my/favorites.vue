<template>
  <div class="my-favorites-page page-container">
    <div class="page-header">
      <h2>我的收藏</h2>
    </div>

    <div class="course-grid" v-loading="loading">
      <div v-for="course in courseList" :key="course.id" class="course-card ink-card" @click="goCourse(course)">
        <div class="course-cover">
          <el-image v-if="course.coverImage" :src="course.coverImage" fit="cover" />
          <div v-else class="cover-placeholder">
            <el-icon :size="32"><VideoPlay /></el-icon>
          </div>
          <el-button class="unfavorite-btn" text @click.stop="handleUnfavorite(course)">
            <el-icon :size="20" color="#f59e0b"><Star /></el-icon>
          </el-button>
        </div>
        <div class="course-info">
          <h4>{{ course.title }}</h4>
          <p class="course-teacher">{{ course.teacher }}</p>
          <div class="course-meta">
            <span>{{ course.studentCount || 0 }} 人学习</span>
            <span v-if="course.rating">
              <el-icon color="#f59e0b"><Star /></el-icon>
              {{ course.rating.toFixed(1) }}
            </span>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && courseList.length === 0" description="暂无收藏课程" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { VideoPlay, Star } from '@element-plus/icons-vue'
import { courseApi } from '@/api'

const router = useRouter()
const loading = ref(false)
const courseList = ref([])

const fetchMyFavorites = async () => {
  loading.value = true
  try {
    const res = await courseApi.myFavorites({ page: 1, pageSize: 100 })
    courseList.value = res.data || []
  } catch (error) {
    ElMessage.error('获取收藏失败')
  } finally {
    loading.value = false
  }
}

const goCourse = (course) => {
  router.push(`/courses/${course.id}`)
}

const handleUnfavorite = async (course) => {
  try {
    await courseApi.unfavorite(course.id)
    ElMessage.success('已取消收藏')
    fetchMyFavorites()
  } catch (error) {
    ElMessage.error('取消收藏失败')
  }
}

onMounted(() => {
  fetchMyFavorites()
})
</script>

<style lang="scss" scoped>
.my-favorites-page { max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: var(--space-md); h2 { font-size: 22px; font-weight: 600; } }

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--space-md);
}

.course-card {
  overflow: hidden;
  cursor: pointer;
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);

  &:hover { transform: translateY(-4px); box-shadow: var(--shadow-hover); }

  .course-cover {
    position: relative;
    height: 150px;
    background: var(--border-light);

    .el-image, .cover-placeholder { width: 100%; height: 100%; }
    .cover-placeholder {
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--ink-gray-lighter);
    }

    .unfavorite-btn {
      position: absolute;
      top: var(--space-sm);
      right: var(--space-sm);
      background: white;
      border-radius: 50%;
      padding: 6px;
    }
  }

  .course-info {
    padding: var(--space-sm) var(--space-md) var(--space-md);

    h4 {
      font-size: 14px;
      font-weight: 600;
      margin-bottom: var(--space-xs);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .course-teacher {
      font-size: 12px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-sm);
    }

    .course-meta {
      display: flex;
      gap: var(--space-sm);
      font-size: 12px;
      color: var(--ink-gray-light);

      span { display: flex; align-items: center; gap: 2px; }
    }
  }
}
</style>
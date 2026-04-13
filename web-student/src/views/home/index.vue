<template>
  <div class="home-page">
    <!-- 欢迎区域 -->
    <div class="welcome-card">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎回来，{{ userInfo?.username || '同学' }}！</h2>
          <p>开始今天的学习吧，持续精进每一天</p>
        </div>
        <div class="welcome-stats">
          <div class="stat-item">
            <div class="stat-value">{{ stats.myCourses }}</div>
            <div class="stat-label">我的课程</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.continueCourse }}</div>
            <div class="stat-label">进行中</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.pendingHomework }}</div>
            <div class="stat-label">待完成作业</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 继续学习 -->
    <section class="section" v-if="continueLearning.length > 0">
      <div class="section-header">
        <h3>继续学习</h3>
        <router-link to="/my-courses" class="more-link">
          查看全部
          <el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      <el-skeleton :loading="loading" animated :rows="2">
        <template #template>
          <div class="course-grid">
            <div v-for="i in 4" :key="i" class="course-card">
              <el-skeleton-item variant="image" style="width: 100%; height: 140px" />
              <div style="padding: 12px">
                <el-skeleton-item variant="h3" style="width: 80%" />
                <el-skeleton-item variant="text" style="width: 50%; margin-top: 8px" />
              </div>
            </div>
          </div>
        </template>
        <div class="course-grid">
          <div
            v-for="course in continueLearning"
            :key="course.id"
            class="course-card"
            @click="goLearn(course)"
          >
            <div class="course-cover">
              <el-image
                v-if="course.coverImage"
                :src="course.coverImage"
                fit="cover"
              />
              <div v-else class="cover-placeholder">
                <el-icon :size="32"><VideoPlay /></el-icon>
              </div>
              <div class="progress-bar">
                <div
                  class="progress-fill"
                  :style="{ width: (course.progress || 0) + '%' }"
                ></div>
              </div>
            </div>
            <div class="course-info">
              <h4>{{ course.title }}</h4>
              <div class="course-meta">
                <span>{{ course.teacherName || course.teacher }}</span>
                <span>{{ course.progress || 0 }}%</span>
              </div>
            </div>
          </div>
        </div>
      </el-skeleton>
    </section>

    <!-- 推荐课程 -->
    <section class="section">
      <div class="section-header">
        <h3>推荐课程</h3>
        <router-link to="/courses" class="more-link">
          查看全部
          <el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      <el-skeleton :loading="loading" animated :rows="2">
        <template #template>
          <div class="course-grid">
            <div v-for="i in 8" :key="i" class="course-card">
              <el-skeleton-item variant="image" style="width: 100%; height: 140px" />
              <div style="padding: 12px">
                <el-skeleton-item variant="h3" style="width: 80%" />
                <el-skeleton-item variant="text" style="width: 50%; margin-top: 8px" />
              </div>
            </div>
          </div>
        </template>
        <div class="course-grid">
          <div
            v-for="course in recommendedCourses"
            :key="course.id"
            class="course-card"
            @click="goCourseDetail(course)"
          >
            <div class="course-cover">
              <el-image
                v-if="course.coverImage"
                :src="course.coverImage"
                fit="cover"
              />
              <div v-else class="cover-placeholder">
                <el-icon :size="32"><VideoPlay /></el-icon>
              </div>
              <div class="course-price" v-if="course.price > 0">
                ¥{{ course.price }}
              </div>
              <div class="course-free" v-else>免费</div>
            </div>
            <div class="course-info">
              <h4>{{ course.title }}</h4>
              <div class="course-meta">
                <span>{{ course.teacherName || course.teacher }}</span>
                <span class="rating" v-if="course.rating">
                  <el-icon color="#f59e0b"><Star /></el-icon>
                  {{ (course.rating || 0).toFixed(1) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </el-skeleton>
      <div v-if="!loading && recommendedCourses.length === 0" class="empty-tip">暂无推荐课程</div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { VideoPlay, Star, ArrowRight } from '@element-plus/icons-vue'
import { courseApi, homeworkApi } from '@/api'

const router = useRouter()

const userInfo = computed(() => {
  const user = localStorage.getItem('student_user')
  return user ? JSON.parse(user) : null
})

const stats = ref({
  myCourses: 0,
  continueCourse: 0,
  pendingHomework: 0
})

const continueLearning = ref([])
const recommendedCourses = ref([])
const loading = ref(false)

// 获取统计数据
const fetchStats = async () => {
  try {
    const myCoursesRes = await courseApi.myListWithProgress({ page: 1, pageSize: 100 })
    const list = myCoursesRes.data || []
    stats.value.myCourses = list.length
    stats.value.continueCourse = list.filter(c => c.progress > 0 && c.progress < 100).length

    const homeworkRes = await homeworkApi.myList({ page: 1, pageSize: 10 })
    const hwList = homeworkRes.data?.list || homeworkRes.data || []
    const pending = hwList.filter(h => !h.score && h.submitTime).length
    stats.value.pendingHomework = pending
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

// 获取继续学习课程
const fetchContinueLearning = async () => {
  try {
    const res = await courseApi.myListWithProgress({ page: 1, pageSize: 4 })
    continueLearning.value = (res.data || []).map(course => ({
      id: course.id,
      title: course.title,
      coverImage: course.cover_image,
      teacherName: course.teacher_name,
      progress: course.progress || 0
    }))
  } catch (error) {
    console.error('获取继续学习课程失败', error)
  }
}

// 获取推荐课程
const fetchRecommendedCourses = async () => {
  loading.value = true
  try {
    const res = await courseApi.list({ page: 1, pageSize: 8 })
    recommendedCourses.value = res.data?.list || res.data?.rows || res.data || []
  } catch (error) {
    console.error('获取推荐课程失败', error)
  } finally {
    loading.value = false
  }
}

// 跳转到学习页面
const goLearn = (course) => {
  router.push(`/learn/${course.id}`)
}

// 跳转到课程详情
const goCourseDetail = (course) => {
  router.push(`/courses/${course.id}`)
}

onMounted(() => {
  fetchStats()
  fetchContinueLearning()
  fetchRecommendedCourses()
})
</script>

<style lang="scss" scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  position: relative;
  overflow: hidden;
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-lg);
  background: var(--gradient-primary);

  .welcome-bg {
    position: absolute;
    inset: 0;
    background:
      radial-gradient(circle at 20% 50%, rgba(255,255,255,0.15) 0%, transparent 50%),
      radial-gradient(circle at 80% 80%, rgba(255,255,255,0.1) 0%, transparent 40%);
  }

  .welcome-content {
    position: relative;
    z-index: 1;
    padding: var(--space-xl);

    .welcome-text {
      margin-bottom: var(--space-lg);

      h2 {
        font-size: 24px;
        font-weight: 700;
        color: white;
        margin-bottom: var(--space-xs);
      }

      p {
        font-size: 14px;
        color: rgba(255,255,255,0.85);
      }
    }

    .welcome-stats {
      display: flex;
      align-items: center;
      gap: var(--space-lg);

      .stat-divider {
        width: 1px;
        height: 36px;
        background: rgba(255,255,255,0.3);
      }

      .stat-item {
        text-align: center;

        .stat-value {
          font-size: 28px;
          font-weight: 700;
          color: white;
          line-height: 1.2;
        }

        .stat-label {
          font-size: 13px;
          color: rgba(255,255,255,0.8);
          margin-top: 4px;
        }
      }
    }
  }
}

.section {
  margin-bottom: var(--space-lg);

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--space-md);

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: var(--ink-gray);
    }

    .more-link {
      display: flex;
      align-items: center;
      gap: 4px;
      color: var(--ink-blue);
      text-decoration: none;
      font-size: 14px;
      transition: gap var(--transition-fast);

      &:hover {
        gap: 8px;
      }
    }
  }
}

.empty-tip {
  text-align: center;
  padding: var(--space-xl);
  color: var(--ink-gray-lighter);
  background: var(--ink-white);
  border-radius: var(--radius-md);
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--space-md);
}

.course-card {
  background: var(--ink-white);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  box-shadow: var(--shadow-card);
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-hover);
  }

  .course-cover {
    position: relative;
    height: 140px;
    background: var(--border-light);

    .el-image {
      width: 100%;
      height: 100%;
    }

    .cover-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--ink-gray-lighter);
    }

    .progress-bar {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: rgba(255,255,255,0.3);

      .progress-fill {
        height: 100%;
        background: var(--ink-blue);
        transition: width 0.3s;
      }
    }

    .course-price {
      position: absolute;
      top: var(--space-sm);
      right: var(--space-sm);
      background: var(--ink-blue);
      color: white;
      padding: 2px var(--space-sm);
      border-radius: var(--radius-sm);
      font-size: 12px;
      font-weight: 600;
    }

    .course-free {
      position: absolute;
      top: var(--space-sm);
      right: var(--space-sm);
      background: var(--ink-green);
      color: white;
      padding: 2px var(--space-sm);
      border-radius: var(--radius-sm);
      font-size: 12px;
      font-weight: 600;
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

    .course-meta {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: var(--ink-gray-light);

      .rating {
        display: flex;
        align-items: center;
        gap: 2px;
      }
    }
  }
}
</style>

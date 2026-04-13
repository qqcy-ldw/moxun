<template>
  <div class="course-list-page">
    <div class="page-header">
      <h2>全部课程</h2>
    </div>

    <!-- 课程列表 -->
    <div class="course-grid" v-loading="loading">
      <div
        v-for="course in courseList"
        :key="course.id"
        class="course-card ink-card"
        @click="goDetail(course)"
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

          <div class="course-tags">
            <el-tag v-if="course.isJoined === 1" type="success" size="small">已选课</el-tag>
            <el-tag v-if="course.isFavorited === 1" type="warning" size="small">已收藏</el-tag>
          </div>

          <div class="course-price" v-if="course.price > 0">
            ¥{{ course.price }}
          </div>
          <div class="course-free" v-else>免费</div>
        </div>

        <div class="course-info">
          <h4>{{ course.title }}</h4>
          <p class="course-desc">{{ course.description }}</p>
          <div class="course-meta">
            <span class="teacher">
              <el-icon><User /></el-icon>
              {{ course.teacher }}
            </span>
            <span class="category">{{ course.category }}</span>
          </div>
          <div class="course-stats">
            <span>
              <el-icon><User /></el-icon>
              {{ course.studentCount || 0 }}
            </span>
            <span v-if="course.rating" class="rating">
              <el-icon color="#f59e0b"><Star /></el-icon>
              {{ course.rating.toFixed(1) }}
            </span>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty
        v-if="!loading && courseList.length === 0"
        description="暂无课程"
        :image-size="100"
      />
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="total"
        :page-sizes="[8, 16, 24, 32]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { VideoPlay, User, Star } from '@element-plus/icons-vue'
import { courseApi, categoryApi } from '@/api'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const courseList = ref([])
const categories = ref([])
const total = ref(0)

const filters = reactive({
  categoryId: null,
  level: '',
  keyword: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 12
})

// 获取课程分类
const fetchCategories = async () => {
  try {
    const res = await categoryApi.list()
    categories.value = res.data || []
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

// 获取课程列表
const fetchCourseList = async () => {
  loading.value = true
  try {
    const res = await courseApi.list({
      title: filters.keyword,
      categoryId: filters.categoryId,
      level: filters.level,
      page: pagination.page,
      pageSize: pagination.pageSize
    })

    courseList.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('获取课程列表失败', error)
  } finally {
    loading.value = false
  }
}

// 筛选
const handleFilter = () => {
  pagination.page = 1
  fetchCourseList()
}

// 重置筛选
const handleReset = () => {
  filters.categoryId = null
  filters.level = ''
  filters.keyword = ''
  pagination.page = 1
  fetchCourseList()
}

// 分页大小变化
const handleSizeChange = () => {
  pagination.page = 1
  fetchCourseList()
}

// 页码变化
const handlePageChange = () => {
  fetchCourseList()
}

// 跳转课程详情
const goDetail = (course) => {
  router.push(`/courses/${course.id}`)
}

onMounted(() => {
  // 从 URL 获取搜索关键词
  if (route.query.keyword) {
    filters.keyword = route.query.keyword
  }

  fetchCategories()
  fetchCourseList()
})
</script>

<style lang="scss" scoped>
.course-list-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: var(--space-md);

  h2 {
    font-size: 22px;
    font-weight: 600;
  }
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: var(--space-md);
  min-height: 300px;
}

.course-card {
  cursor: pointer;
  overflow: hidden;
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-hover);
  }

  .course-cover {
    position: relative;
    height: 160px;
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

    .course-tags {
      position: absolute;
      top: var(--space-sm);
      left: var(--space-sm);
      display: flex;
      gap: var(--space-xs);
    }

    .course-price {
      position: absolute;
      bottom: var(--space-sm);
      right: var(--space-sm);
      background: var(--gradient-primary);
      color: white;
      padding: var(--space-xs) var(--space-sm);
      border-radius: var(--radius-sm);
      font-size: 14px;
      font-weight: 600;
    }

    .course-free {
      position: absolute;
      bottom: var(--space-sm);
      right: var(--space-sm);
      background: var(--ink-green);
      color: white;
      padding: var(--space-xs) var(--space-sm);
      border-radius: var(--radius-sm);
      font-size: 14px;
      font-weight: 600;
    }
  }

  .course-info {
    padding: var(--space-md);

    h4 {
      font-size: 15px;
      font-weight: 600;
      margin-bottom: var(--space-xs);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .course-desc {
      font-size: 13px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-sm);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .course-meta {
      display: flex;
      justify-content: space-between;
      font-size: 13px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-xs);

      .teacher {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }

    .course-stats {
      display: flex;
      gap: var(--space-sm);
      font-size: 13px;
      color: var(--ink-gray-light);

      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }

      .rating {
        color: #f59e0b;
      }
    }
  }
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: var(--space-md);
}
</style>

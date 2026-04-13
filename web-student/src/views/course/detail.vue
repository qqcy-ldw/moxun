<template>
  <div class="course-detail-page" v-loading="loading">
    <!-- 课程信息 -->
    <div class="course-header ink-card">
      <div class="header-left">
        <el-image
          v-if="courseData.coverImage"
          :src="courseData.coverImage"
          fit="cover"
          class="course-cover"
        />
        <div v-else class="cover-placeholder">
          <el-icon :size="48"><VideoPlay /></el-icon>
        </div>
      </div>

      <div class="header-right">
        <h1>{{ courseData.title }}</h1>

        <div class="course-tags">
          <el-tag type="info">{{ courseData.category }}</el-tag>
          <el-tag>{{ getLevelText(courseData.level) }}</el-tag>
          <el-tag type="warning" v-if="courseData.price > 0">¥{{ courseData.price }}</el-tag>
          <el-tag type="success" v-else>免费</el-tag>
        </div>

        <div class="course-stats">
          <span><el-icon><User /></el-icon> {{ courseData.teacher }}</span>
          <span><el-icon><UserFilled /></el-icon> {{ courseData.studentCount || 0 }} 人学习</span>
          <span v-if="courseData.rating">
            <el-icon color="#f59e0b"><Star /></el-icon>
            {{ courseData.rating.toFixed(1) }}
          </span>
        </div>

        <div class="course-actions">
          <el-button
            v-if="courseData.isJoined === 1"
            type="primary"
            size="large"
            class="primary-btn"
            @click="handleLearn"
          >
            继续学习
          </el-button>
          <el-button
            v-else
            type="primary"
            size="large"
            class="primary-btn"
            @click="handleJoin"
            :loading="joining"
          >
            立即选课
          </el-button>

          <el-button
            v-if="courseData.isFavorited === 1"
            @click="handleUnfavorite"
          >
            <el-icon><Star /></el-icon> 已收藏
          </el-button>
          <el-button v-else @click="handleFavorite">
            <el-icon><Star /></el-icon> 收藏
          </el-button>

          <el-button @click="showCommentDialog = true">
            <el-icon><ChatLineRound /></el-icon> 评价
          </el-button>
        </div>
      </div>
    </div>

    <!-- 课程内容 -->
    <div class="course-body">
      <div class="course-tabs ink-card">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="课程介绍" name="intro">
            <div class="course-intro" v-html="courseData.description"></div>
          </el-tab-pane>

          <el-tab-pane label="课程目录" name="catalog">
            <div class="course-catalog" v-if="courseData.chapters?.length">
              <el-collapse v-model="expandedChapters">
                <el-collapse-item
                  v-for="(chapter, cIdx) in courseData.chapters"
                  :key="chapter.id"
                  :name="chapter.id"
                >
                  <template #title>
                    <div class="chapter-title">
                      <span class="chapter-order">{{ cIdx + 1 }}</span>
                      <span class="chapter-name">{{ chapter.title }}</span>
                      <span class="chapter-count">{{ chapter.sections?.length || 0 }} 节</span>
                    </div>
                  </template>

                  <div class="section-list">
                    <div
                      v-for="(section, sIdx) in chapter.sections"
                      :key="section.id"
                      class="section-item"
                      @click="goLearn(section)"
                    >
                      <span class="section-index">{{ sIdx + 1 }}</span>
                      <el-icon class="section-icon"><VideoPlay /></el-icon>
                      <span class="section-name">{{ section.title }}</span>
                      <span class="section-duration">{{ formatDuration(section.duration) }}</span>
                      <el-tag v-if="section.isFree === 1" type="success" size="small">免费</el-tag>
                    </div>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>
            <el-empty v-else description="暂无目录" />
          </el-tab-pane>

          <el-tab-pane label="学员评价" name="comments">
            <div class="comment-list">
              <div
                v-for="comment in comments"
                :key="comment.id"
                class="comment-item"
              >
                <div class="comment-avatar">
                  <el-avatar :size="40">{{ comment.username?.charAt(0) }}</el-avatar>
                </div>
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="comment-user">{{ comment.username }}</span>
                    <el-rate v-model="comment.rating" disabled text-color="#f59e0b" />
                  </div>
                  <p class="comment-text">{{ comment.content }}</p>
                  <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                </div>
              </div>

              <el-empty v-if="comments.length === 0" description="暂无评价" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 发表评价对话框 -->
    <el-dialog v-model="showCommentDialog" title="发表评价" width="500px">
      <el-form :model="commentForm" label-width="60px">
        <el-form-item label="评分">
          <el-rate v-model="commentForm.rating" show-text text-color="#f59e0b" />
        </el-form-item>
        <el-form-item label="评价">
          <el-input
            v-model="commentForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入您的评价..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCommentDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitComment" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { VideoPlay, User, UserFilled, Star, ChatLineRound } from '@element-plus/icons-vue'
import { courseApi } from '@/api'

const router = useRouter()
const route = useRoute()
const courseId = route.params.id

const loading = ref(false)
const courseData = ref({})
const comments = ref([])
const activeTab = ref('intro')
const expandedChapters = ref([])
const showCommentDialog = ref(false)
const joining = ref(false)
const submitting = ref(false)

const commentForm = reactive({
  rating: 5,
  content: ''
})

// 获取课程详情
const fetchCourseDetail = async () => {
  loading.value = true
  try {
    const res = await courseApi.getById(courseId)
    courseData.value = res.data || {}

    // 默认展开第一章
    if (courseData.value.chapters?.length > 0) {
      expandedChapters.value = [courseData.value.chapters[0].id]
    }
  } catch (error) {
    ElMessage.error('获取课程详情失败')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const res = await courseApi.comments(courseId, { page: 1, pageSize: 20 })
    comments.value = res.data?.list || []
  } catch (error) {
    console.error('获取评论失败', error)
  }
}

// 选课
const handleJoin = async () => {
  joining.value = true
  try {
    await courseApi.join(courseId)
    ElMessage.success('选课成功')
    fetchCourseDetail()
  } catch (error) {
    ElMessage.error(error.message || '选课失败')
  } finally {
    joining.value = false
  }
}

// 继续学习
const handleLearn = () => {
  router.push(`/learn/${courseId}`)
}

// 收藏
const handleFavorite = async () => {
  try {
    await courseApi.favorite(courseId)
    ElMessage.success('收藏成功')
    fetchCourseDetail()
  } catch (error) {
    ElMessage.error(error.message || '收藏失败')
  }
}

// 取消收藏
const handleUnfavorite = async () => {
  try {
    await courseApi.unfavorite(courseId)
    ElMessage.success('已取消收藏')
    fetchCourseDetail()
  } catch (error) {
    ElMessage.error(error.message || '取消收藏失败')
  }
}

// 提交评论
const handleSubmitComment = async () => {
  if (!commentForm.content.trim()) {
    ElMessage.warning('请输入评价内容')
    return
  }

  submitting.value = true
  try {
    await courseApi.addComment(courseId, commentForm)
    ElMessage.success('评价成功')
    showCommentDialog.value = false
    commentForm.content = ''
    commentForm.rating = 5
    fetchComments()
  } catch (error) {
    ElMessage.error(error.message || '评价失败')
  } finally {
    submitting.value = false
  }
}

// 去学习课时
const goLearn = (section) => {
  router.push(`/learn/${courseId}/${section.id}`)
}

// 格式化时长
const formatDuration = (minutes) => {
  if (!minutes) return '—'
  const m = Math.floor(minutes)
  const s = (minutes - m) * 60
  return `${m}分${Math.round(s)}秒`
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString('zh-CN')
}

// 获取难度文本
const getLevelText = (level) => {
  const map = {
    'BEGINNER': '初级',
    'INTERMEDIATE': '中级',
    'ADVANCED': '高级'
  }
  return map[level] || level
}

onMounted(() => {
  fetchCourseDetail()
  fetchComments()
})
</script>

<style lang="scss" scoped>
.course-detail-page {
  max-width: 1200px;
  margin: 0 auto;
}

.course-header {
  display: flex;
  gap: var(--space-lg);
  padding: var(--space-lg);
  margin-bottom: var(--space-md);
  border-radius: var(--radius-lg);

  .header-left {
    flex-shrink: 0;

    .course-cover {
      width: 320px;
      height: 180px;
      border-radius: var(--radius-md);
    }

    .cover-placeholder {
      width: 320px;
      height: 180px;
      background: var(--border-light);
      border-radius: var(--radius-md);
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--ink-gray-lighter);
    }
  }

  .header-right {
    flex: 1;

    h1 {
      font-size: 24px;
      font-weight: 700;
      margin-bottom: var(--space-sm);
    }

    .course-tags {
      display: flex;
      gap: var(--space-sm);
      margin-bottom: var(--space-md);
    }

    .course-stats {
      display: flex;
      gap: var(--space-lg);
      font-size: 14px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-md);

      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }

    .course-actions {
      display: flex;
      gap: var(--space-sm);
    }
  }
}

.course-body {
  .course-tabs {
    padding: var(--space-md);
    border-radius: var(--radius-lg);
  }
}

.course-intro {
  font-size: 14px;
  line-height: 1.8;
  color: var(--ink-gray);
  white-space: pre-wrap;
}

.course-catalog {
  .chapter-title {
    display: flex;
    align-items: center;
    gap: 10px;

    .chapter-order {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background: var(--ink-blue);
      color: white;
      font-size: 12px;
      font-weight: 600;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .chapter-name {
      flex: 1;
      font-weight: 600;
    }

    .chapter-count {
      font-size: 12px;
      color: var(--ink-gray-lighter);
    }
  }

  .section-list {
    .section-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 12px 16px;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: rgba(37, 99, 235, 0.04);
      }

      .section-index {
        width: 20px;
        height: 20px;
        border-radius: 50%;
        background: rgba(37, 99, 235, 0.1);
        color: var(--ink-blue);
        font-size: 11px;
        font-weight: 600;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .section-icon {
        color: var(--ink-blue);
      }

      .section-name {
        flex: 1;
        font-size: 14px;
      }

      .section-duration {
        font-size: 12px;
        color: var(--ink-gray-lighter);
      }
    }
  }
}

.comment-list {
  .comment-item {
    display: flex;
    gap: 12px;
    padding: 16px 0;
    border-bottom: 1px solid var(--border-light);

    &:last-child {
      border-bottom: none;
    }

    .comment-content {
      flex: 1;

      .comment-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 8px;

        .comment-user {
          font-weight: 600;
        }
      }

      .comment-text {
        font-size: 14px;
        line-height: 1.6;
        margin-bottom: 8px;
      }

      .comment-time {
        font-size: 12px;
        color: var(--ink-gray-lighter);
      }
    }
  }
}
</style>

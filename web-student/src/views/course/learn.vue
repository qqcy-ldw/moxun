<template>
  <div class="learn-page">
    <div class="learn-container">
      <!-- 左侧视频区域 -->
      <div class="video-section">
        <div class="video-wrapper">
          <video
            ref="videoRef"
            :src="currentSection?.videoUrl"
            controls
            @timeupdate="handleTimeUpdate"
            @ended="handleVideoEnded"
          ></video>
        </div>

        <div class="video-info">
          <h2>{{ currentSection?.title }}</h2>
          <div class="video-progress">
            <span>播放进度: {{ formatTime(currentPosition) }} / {{ formatTime(currentSection?.duration || 0) }}</span>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: progressPercent + '%' }"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧目录区域 -->
      <div class="catalog-section ink-card">
        <div class="catalog-header">
          <h3>课程目录</h3>
          <span class="progress-text">进度: {{ courseProgress }}%</span>
        </div>

        <div class="catalog-list">
          <div
            v-for="(chapter, cIdx) in catalog"
            :key="chapter.id"
            class="chapter-item"
          >
            <div class="chapter-title">
              <span class="chapter-num">{{ cIdx + 1 }}</span>
              {{ chapter.title }}
            </div>

            <div
              v-for="(section, sIdx) in chapter.sections"
              :key="section.id"
              class="section-item"
              :class="{ active: section.id === sectionId, finished: sectionProgress[section.id]?.finished }"
              @click="switchSection(section)"
            >
              <span class="section-index">{{ sIdx + 1 }}</span>
              <span class="section-name">{{ section.title }}</span>
              <el-icon v-if="sectionProgress[section.id]?.finished" color="#22c55e"><CircleCheck /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { CircleCheck } from '@element-plus/icons-vue'
import { learningApi, courseApi } from '@/api'

const router = useRouter()
const route = useRoute()

const courseId = route.params.courseId
const sectionId = route.params.sectionId

const videoRef = ref()
const catalog = ref([])
const currentSection = ref(null)
const currentPosition = ref(0)
const sectionProgress = ref({})
const courseProgress = ref(0)

// 计算进度百分比
const progressPercent = computed(() => {
  if (!currentSection.value?.duration) return 0
  return Math.round((currentPosition.value / currentSection.value.duration) * 100)
})

// 获取课程目录
const fetchCatalog = async () => {
  try {
    const res = await learningApi.catalog(courseId)
    catalog.value = res.data || []

    // 默认选中第一个课时
    if (!sectionId && catalog.value.length > 0 && catalog.value[0].sections?.length > 0) {
      const firstSection = catalog.value[0].sections[0]
      router.replace(`/learn/${courseId}/${firstSection.id}`)
    }
  } catch (error) {
    ElMessage.error('获取目录失败')
  }
}

// 获取课程进度
const fetchCourseProgress = async () => {
  try {
    const res = await learningApi.courseProgress(courseId)
    courseProgress.value = res.data?.progressPercent || 0
  } catch (error) {
    console.error('获取课程进度失败', error)
  }
}

// 获取课时进度
const fetchSectionProgress = async () => {
  try {
    const res = await learningApi.sectionProgress(sectionId)
    currentPosition.value = res.data?.position || 0
    sectionProgress.value[sectionId] = res.data
  } catch (error) {
    console.error('获取课时进度失败', error)
  }
}

// 获取课程详情（找当前课时）
const fetchCourseDetail = async () => {
  try {
    const res = await courseApi.getById(courseId)
    catalog.value = res.data?.chapters || []

    // 找到当前课时
    for (const chapter of catalog.value) {
      const section = chapter.sections?.find(s => s.id === Number(sectionId))
      if (section) {
        currentSection.value = section
        break
      }
    }
  } catch (error) {
    console.error('获取课程详情失败', error)
  }
}

// 切换课时
const switchSection = (section) => {
  router.push(`/learn/${courseId}/${section.id}`)
}

// 播放时间更新
const handleTimeUpdate = () => {
  if (videoRef.value) {
    currentPosition.value = Math.floor(videoRef.value.currentTime)
  }
}

// 视频播放结束
const handleVideoEnded = () => {
  // 标记完成
  updateProgress(true)
}

// 更新学习进度
const updateProgress = async (finished = false) => {
  try {
    await learningApi.updateProgress({
      sectionId: Number(sectionId),
      position: currentPosition.value,
      finished
    })
    sectionProgress.value[sectionId] = { finished, position: currentPosition.value }
    fetchCourseProgress()
  } catch (error) {
    console.error('更新进度失败', error)
  }
}

// 格式化时间
const formatTime = (seconds) => {
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

watch(() => route.params.sectionId, (newId) => {
  if (newId) {
    // 找当前课时
    for (const chapter of catalog.value) {
      const section = chapter.sections?.find(s => s.id === Number(newId))
      if (section) {
        currentSection.value = section
        fetchSectionProgress()
        break
      }
    }
  }
})

onMounted(() => {
  fetchCatalog()
  fetchCourseProgress()
  fetchCourseDetail()
  if (sectionId) {
    fetchSectionProgress()
  }
})
</script>

<style lang="scss" scoped>
.learn-page {
  background: #1a1a2e;
  min-height: 100vh;
  color: white;
}

.learn-container {
  display: flex;
  max-width: 1600px;
  margin: 0 auto;
  gap: 20px;
  padding: 20px;
}

.video-section {
  flex: 1;

  .video-wrapper {
    background: #000;
    border-radius: 8px;
    overflow: hidden;

    video {
      width: 100%;
      display: block;
    }
  }

  .video-info {
    margin-top: 16px;

    h2 {
      font-size: 18px;
      margin-bottom: 12px;
    }

    .video-progress {
      font-size: 14px;
      color: #a0a0a0;

      .progress-bar {
        height: 4px;
        background: #333;
        border-radius: 2px;
        margin-top: 8px;

        .progress-fill {
          height: 100%;
          background: var(--ink-blue);
          border-radius: 2px;
        }
      }
    }
  }
}

.catalog-section {
  width: 360px;
  max-height: calc(100vh - 40px);
  overflow-y: auto;
  background: #16213e;
  border: none;

  .catalog-header {
    padding: 16px;
    border-bottom: 1px solid #333;
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      font-size: 16px;
    }

    .progress-text {
      font-size: 12px;
      color: var(--ink-blue);
    }
  }

  .catalog-list {
    padding: 8px;
  }

  .chapter-item {
    margin-bottom: 8px;

    .chapter-title {
      padding: 8px 12px;
      font-size: 14px;
      font-weight: 600;
      color: #a0a0a0;
      display: flex;
      align-items: center;
      gap: 8px;

      .chapter-num {
        width: 20px;
        height: 20px;
        border-radius: 50%;
        background: #333;
        font-size: 11px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }

    .section-item {
      padding: 10px 12px 10px 36px;
      font-size: 13px;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      border-radius: 6px;
      transition: background-color 0.2s;

      &:hover {
        background: rgba(255, 255, 255, 0.05);
      }

      &.active {
        background: var(--ink-blue);
      }

      &.finished {
        color: #22c55e;
      }

      .section-index {
        font-size: 11px;
        color: #666;
      }

      .section-name {
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
</style>
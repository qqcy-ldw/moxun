<template>
  <div class="homework-detail-page page-container" v-loading="loading">
    <div class="detail-header ink-card">
      <el-button @click="goBack" size="small">返回</el-button>
      <h2>{{ homework.title }}</h2>
    </div>

    <div class="homework-content ink-card">
      <div class="content-section">
        <h3>作业描述</h3>
        <p>{{ homework.description }}</p>
      </div>

      <div class="content-section">
        <h3>作业内容</h3>
        <div class="homework-body" v-html="homework.content"></div>
      </div>

      <div class="content-section">
        <div class="info-row">
          <span>截止时间: <strong>{{ formatDate(homework.deadline) }}</strong></span>
          <span>总分: <strong>{{ homework.totalScore }}分</strong></span>
        </div>
      </div>
    </div>

    <div class="submit-section ink-card" v-if="!mySubmission">
      <h3>提交作业</h3>
      <el-input v-model="submitForm.content" type="textarea" :rows="8" placeholder="请输入作业内容..." />
      <div class="submit-actions">
        <el-button type="primary" @click="handleSubmit" :loading="submitting">提交作业</el-button>
      </div>
    </div>

    <div class="submission-section ink-card" v-else>
      <h3>我的提交</h3>
      <div class="submission-info">
        <span>提交时间: {{ formatDate(mySubmission.submitTime) }}</span>
        <span v-if="mySubmission.score">得分: <strong>{{ mySubmission.score }}分</strong></span>
        <span v-if="mySubmission.feedback">评语: {{ mySubmission.feedback }}</span>
      </div>
      <div class="submission-content">
        <h4>提交内容</h4>
        <p>{{ mySubmission.content }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { homeworkApi } from '@/api'

const router = useRouter()
const route = useRoute()
// params.id 是字符串，可能为 'undefined'、'NaN' 或空，需要 parseInt 安全转换
const homeworkId = parseInt(route.params.id, 10)

// 如果 id 无效，重定向回列表
if (isNaN(homeworkId)) {
  router.replace('/my-homework')
}

const loading = ref(false)
const submitting = ref(false)
const homework = ref({})
const mySubmission = ref(null)

const submitForm = reactive({ content: '' })

const fetchHomeworkDetail = async () => {
  loading.value = true
  try {
    const res = await homeworkApi.getById(homeworkId)
    homework.value = res.data || {}
  } catch (error) {
    ElMessage.error('获取作业详情失败')
  } finally {
    loading.value = false
  }
}

const fetchMySubmission = async () => {
  try {
    const res = await homeworkApi.mySubmission(homeworkId)
    mySubmission.value = res.data
  } catch (error) {
    // 没有提交记录
  }
}

const handleSubmit = async () => {
  if (!submitForm.content.trim()) {
    ElMessage.warning('请输入作业内容')
    return
  }

  submitting.value = true
  try {
    await homeworkApi.submit({
      assignmentId: Number(homeworkId),
      content: submitForm.content
    })
    ElMessage.success('提交成功')
    fetchMySubmission()
  } catch (error) {
    ElMessage.error(error.message || '提交失败')
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.back()
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchHomeworkDetail()
  fetchMySubmission()
})
</script>

<style lang="scss" scoped>
.homework-detail-page { max-width: 900px; margin: 0 auto; }

.detail-header {
  padding: var(--space-md) var(--space-lg);
  margin-bottom: var(--space-sm);
  display: flex;
  align-items: center;
  gap: var(--space-md);
  border-radius: var(--radius-md);

  h2 { font-size: 18px; font-weight: 600; }
}

.homework-content, .submit-section, .submission-section {
  padding: var(--space-lg);
  margin-bottom: var(--space-sm);
  border-radius: var(--radius-md);

  h3 { font-size: 15px; font-weight: 600; margin-bottom: var(--space-sm); }
}

.content-section {
  margin-bottom: var(--space-md);

  h3 { font-size: 14px; color: var(--ink-gray-light); margin-bottom: var(--space-sm); }
  p { font-size: 14px; line-height: 1.6; }

  .info-row {
    display: flex;
    gap: var(--space-lg);
    font-size: 14px;
    color: var(--ink-gray-light);

    strong { color: var(--ink-gray); }
  }
}

.homework-body {
  font-size: 14px;
  line-height: 1.8;
  white-space: pre-wrap;
}

.submit-section {
  h3 { border-bottom: 1px solid var(--border-light); padding-bottom: var(--space-sm); }

  .submit-actions {
    margin-top: var(--space-md);
    display: flex;
    justify-content: flex-end;
  }
}

.submission-section {
  h3 { border-bottom: 1px solid var(--border-light); padding-bottom: var(--space-sm); }

  .submission-info {
    display: flex;
    gap: var(--space-lg);
    font-size: 13px;
    color: var(--ink-gray-light);
    margin-bottom: var(--space-md);

    span { display: flex; align-items: center; gap: 4px; }
    strong { color: var(--ink-blue); font-size: 16px; }
  }

  .submission-content {
    background: var(--ink-bg);
    padding: var(--space-md);
    border-radius: var(--radius-md);

    h4 { font-size: 13px; color: var(--ink-gray-light); margin-bottom: var(--space-sm); }
    p { font-size: 14px; line-height: 1.6; white-space: pre-wrap; }
  }
}
</style>
<template>
  <div class="question-detail-page page-container" v-loading="loading">
    <el-button @click="goBack" size="small">返回</el-button>

    <div class="question-header ink-card">
      <h1>{{ question.title }}</h1>
      <div class="question-meta">
        <span class="author">
          <el-avatar :size="24">{{ question.username?.charAt(0) }}</el-avatar>
          {{ question.username }}
        </span>
        <span>{{ question.courseName }}</span>
        <span>{{ formatTime(question.createTime) }}</span>
        <span>{{ question.viewCount }} 浏览</span>
      </div>
    </div>

    <div class="question-body ink-card">
      <div class="content" v-html="question.content"></div>
    </div>

    <div class="answers-section">
      <h3>回答 ({{ answers.length }})</h3>

      <div v-for="answer in answers" :key="answer.id" class="answer-item ink-card">
        <div class="answer-header">
          <div class="answer-author">
            <el-avatar :size="32">{{ answer.username?.charAt(0) }}</el-avatar>
            <span>{{ answer.username }}</span>
          </div>
          <span class="answer-time">{{ formatTime(answer.createTime) }}</span>
        </div>
        <div class="answer-content" v-html="answer.content"></div>
        <div class="answer-actions">
          <el-button
            :type="answer.isLiked === 1 ? 'warning' : 'default'"
            size="small"
            @click="handleLike(answer)"
          >
            <el-icon><Star /></el-icon>
            {{ answer.likeCount || 0 }}
          </el-button>
          <el-tag v-if="answer.isAccepted === 1" type="success" size="small">已采纳</el-tag>
        </div>
      </div>

      <el-empty v-if="answers.length === 0" description="暂无回答" />
    </div>

    <div class="answer-form ink-card">
      <h3>发表回答</h3>
      <el-input v-model="answerForm.content" type="textarea" :rows="5" placeholder="请输入您的回答..." />
      <div class="form-actions">
        <el-button type="primary" @click="handleSubmitAnswer" :loading="submitting">提交回答</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star } from '@element-plus/icons-vue'
import { questionApi } from '@/api'

const router = useRouter()
const route = useRoute()
const questionId = route.params.id

const loading = ref(false)
const submitting = ref(false)
const question = ref({})
const answers = ref([])
const answerForm = reactive({ content: '' })

const fetchQuestionDetail = async () => {
  loading.value = true
  try {
    const res = await questionApi.getById(questionId)
    question.value = res.data || {}
    answers.value = res.data?.answers || []
  } catch (error) {
    ElMessage.error('获取问题详情失败')
  } finally {
    loading.value = false
  }
}

const handleLike = async (answer) => {
  try {
    if (answer.isLiked === 1) {
      await questionApi.unlike(answer.id)
      answer.isLiked = 0
      answer.likeCount = (answer.likeCount || 0) - 1
    } else {
      await questionApi.like(answer.id)
      answer.isLiked = 1
      answer.likeCount = (answer.likeCount || 0) + 1
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmitAnswer = async () => {
  if (!answerForm.content.trim()) {
    ElMessage.warning('请输入回答内容')
    return
  }

  submitting.value = true
  try {
    await questionApi.answer({ questionId: Number(questionId), content: answerForm.content })
    ElMessage.success('回答成功')
    answerForm.content = ''
    fetchQuestionDetail()
  } catch (error) {
    ElMessage.error('回答失败')
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.back()
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchQuestionDetail()
})
</script>

<style lang="scss" scoped>
.question-detail-page { max-width: 900px; margin: 0 auto; }

.question-header {
  padding: 20px;
  margin: 16px 0;

  h1 { font-size: 22px; font-weight: 700; margin-bottom: 12px; }

  .question-meta {
    display: flex;
    gap: 16px;
    font-size: 13px;
    color: var(--ink-gray-light);

    .author {
      display: flex;
      align-items: center;
      gap: 6px;
    }
  }
}

.question-body {
  padding: 20px;
  margin-bottom: 16px;

  .content { font-size: 14px; line-height: 1.8; }
}

.answers-section {
  margin-bottom: 16px;

  h3 {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-left: 8px;
    border-left: 3px solid var(--ink-blue);
  }
}

.answer-item {
  padding: 16px;
  margin-bottom: 12px;

  .answer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .answer-author {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 600;
    }

    .answer-time {
      font-size: 12px;
      color: var(--ink-gray-lighter);
    }
  }

  .answer-content {
    font-size: 14px;
    line-height: 1.6;
    margin-bottom: 12px;
  }

  .answer-actions {
    display: flex;
    gap: 12px;
    align-items: center;
  }
}

.answer-form {
  padding: 20px;

  h3 { font-size: 15px; font-weight: 600; margin-bottom: 12px; }

  .form-actions {
    margin-top: 12px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
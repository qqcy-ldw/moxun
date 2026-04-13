<template>
  <div class="question-list-page page-container">
    <div class="page-header">
      <h2>问答社区</h2>
      <el-button type="primary" @click="showAskDialog = true">发布问题</el-button>
    </div>

    <div class="filter-bar ink-card">
      <el-input v-model="filters.keyword" placeholder="搜索问题..." clearable @keyup.enter="handleSearch">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <div class="question-list" v-loading="loading">
      <div v-for="q in questionList" :key="q.id" class="question-card ink-card" @click="goDetail(q)">
        <div class="question-vote">
          <span class="answer-count">{{ q.answerCount || 0 }}</span>
          <span class="label">回答</span>
        </div>
        <div class="question-content">
          <h3>{{ q.title }}</h3>
          <p class="question-excerpt">{{ q.content }}</p>
          <div class="question-meta">
            <span class="author">
              <el-avatar :size="20">{{ q.username?.charAt(0) }}</el-avatar>
              {{ q.username }}
            </span>
            <span>{{ q.courseName }}</span>
            <span>{{ formatTime(q.createTime) }}</span>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && questionList.length === 0" description="暂无问题" />
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

    <el-dialog v-model="showAskDialog" title="发布问题" width="600px">
      <el-form :model="askForm" label-width="70px">
        <el-form-item label="问题标题">
          <el-input v-model="askForm.title" placeholder="请输入问题标题" />
        </el-form-item>
        <el-form-item label="关联课程">
          <el-select v-model="askForm.courseId" placeholder="请选择课程" style="width: 100%">
            <el-option v-for="c in myCourses" :key="c.id" :label="c.title" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题内容">
          <el-input v-model="askForm.content" type="textarea" :rows="5" placeholder="请详细描述您的问题..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAskDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAsk" :loading="asking">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { questionApi, courseApi } from '@/api'

const router = useRouter()
const loading = ref(false)
const questionList = ref([])
const total = ref(0)
const showAskDialog = ref(false)
const asking = ref(false)

const filters = reactive({ keyword: '' })
const pagination = reactive({ page: 1, pageSize: 10 })

const askForm = reactive({ title: '', content: '', courseId: null })
const myCourses = ref([])

const fetchList = async () => {
  loading.value = true
  try {
    const res = await questionApi.list({
      keyword: filters.keyword,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    questionList.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error) {
    ElMessage.error('获取问题列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchList()
}

const goDetail = (q) => {
  router.push(`/questions/${q.id}`)
}

const fetchMyCourses = async () => {
  try {
    const res = await courseApi.myList({ page: 1, pageSize: 100 })
    myCourses.value = res.data || []
  } catch (error) {
    console.error('获取我的课程失败', error)
  }
}

const handleAsk = async () => {
  if (!askForm.title.trim() || !askForm.content.trim()) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (!askForm.courseId) {
    ElMessage.warning('请选择关联课程')
    return
  }

  asking.value = true
  try {
    await questionApi.create(askForm)
    ElMessage.success('发布成功')
    showAskDialog.value = false
    Object.assign(askForm, { title: '', content: '', courseId: null })
    fetchList()
  } catch (error) {
    ElMessage.error('发布失败')
  } finally {
    asking.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchList()
  fetchMyCourses()
})
</script>

<style lang="scss" scoped>
.question-list-page { max-width: 900px; margin: 0 auto; }
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-md);
  h2 { font-size: 22px; font-weight: 600; }
}

.filter-bar {
  display: flex;
  gap: var(--space-sm);
  padding: var(--space-md);
  margin-bottom: var(--space-md);
  border-radius: var(--radius-md);

  .el-input { width: 300px; }
}

.question-list { display: flex; flex-direction: column; gap: var(--space-sm); }

.question-card {
  display: flex;
  gap: var(--space-md);
  padding: var(--space-md);
  cursor: pointer;
  transition: box-shadow var(--transition-fast);

  &:hover { box-shadow: var(--shadow-hover); }

  .question-vote {
    width: 60px;
    text-align: center;
    flex-shrink: 0;

    .answer-count {
      font-size: 24px;
      font-weight: 700;
      color: var(--ink-blue);
    }

    .label {
      font-size: 12px;
      color: var(--ink-gray-light);
    }
  }

  .question-content {
    flex: 1;

    h3 {
      font-size: 15px;
      font-weight: 600;
      margin-bottom: var(--space-sm);
    }

    .question-excerpt {
      font-size: 13px;
      color: var(--ink-gray-light);
      margin-bottom: var(--space-sm);
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .question-meta {
      display: flex;
      gap: var(--space-md);
      font-size: 12px;
      color: var(--ink-gray-lighter);

      .author {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }
}

.pagination { display: flex; justify-content: center; margin-top: var(--space-md); }
</style>
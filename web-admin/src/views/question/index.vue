<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar ink-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="请输入关键词" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">问答列表</h3>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="问题标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="courseId" label="课程ID" width="100" align="center">
          <template #default="{ row }">
            <span class="id-text">{{ row.courseId || '—' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="answerCount" label="回答数" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="info">{{ row.answerCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览次数" width="100" align="center">
          <template #default="{ row }">
            <span class="view-count">{{ row.viewCount || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提问时间" width="170" align="center" />
        <el-table-column label="操作" min-width="120" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleViewDetail(row)">查看</el-button>
              <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="pagination.page"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="问答详情"
      width="800px"
      destroy-on-close
    >
      <div v-loading="detailLoading">
        <template v-if="detailData">
          <!-- 问题信息 -->
          <div class="question-section">
            <h4 class="section-title">问题内容</h4>
            <div class="question-title">{{ detailData.title }}</div>
            <div class="question-meta">
              <span>用户ID：{{ detailData.userId || '—' }}</span>
              <span>浏览 {{ detailData.viewCount || 0 }} 次</span>
              <span>{{ detailData.createTime }}</span>
            </div>
            <div class="question-content">{{ detailData.content }}</div>
          </div>

          <!-- 回答列表 -->
          <div class="answers-section">
            <h4 class="section-title">
              回答列表
              <span class="answer-count">({{ detailData.answerCount || 0 }})</span>
            </h4>
            <div v-if="!detailData.answers?.length" class="empty-answers">
              暂无回答
            </div>
            <div v-else class="answer-list">
              <div
                v-for="answer in detailData.answers"
                :key="answer.id"
                class="answer-item"
                :class="{ 'is-accepted': answer.isAccepted === 1 }"
              >
                <div class="answer-header">
                  <span class="answer-user">用户 {{ answer.userId }}</span>
                  <el-tag v-if="answer.isAccepted === 1" type="success" size="small">最佳答案</el-tag>
                  <el-tag v-else type="info" size="small">{{ answer.likeCount || 0 }} 点赞</el-tag>
                  <span class="answer-time">{{ answer.createTime }}</span>
                </div>
                <div class="answer-content">{{ answer.content }}</div>
                <div class="answer-actions">
                  <el-button
                    v-if="answer.isAccepted !== 1"
                    type="primary"
                    text
                    size="small"
                    @click="handleAcceptAnswer(answer.id)"
                  >
                    采纳为最佳答案
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { questionApi } from '@/api'

const loading = ref(false)
const dialogVisible = ref(false)
const detailLoading = ref(false)
const detailData = ref(null)

const searchForm = reactive({
  keyword: '',
  courseId: null
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

// 获取问答列表
const fetchQuestions = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchForm.keyword || undefined,
      courseId: searchForm.courseId || undefined
    }
    const res = await questionApi.list(params)
    tableData.value = res.data.records || res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取问答列表失败', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchQuestions()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.courseId = null
  handleSearch()
}

// 分页
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  fetchQuestions()
}

const handleCurrentChange = (val) => {
  pagination.page = val
  fetchQuestions()
}

// 查看详情
const handleViewDetail = async (row) => {
  detailLoading.value = true
  dialogVisible.value = true
  detailData.value = null
  try {
    const res = await questionApi.getDetail(row.id)
    detailData.value = res.data
  } catch (error) {
    console.error('获取问答详情失败', error)
  } finally {
    detailLoading.value = false
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该问答吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await questionApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchQuestions()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

// 采纳回答
const handleAcceptAnswer = async (answerId) => {
  try {
    await ElMessageBox.confirm('确定要采纳该回答吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await questionApi.acceptAnswer(answerId)
    ElMessage.success('已采纳')
    if (detailData.value) {
      handleViewDetail(detailData.value)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('采纳失败', error)
    }
  }
}

onMounted(() => {
  fetchQuestions()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.page-container {
  font-size: 15px;
}

.search-bar {
  margin-bottom: 24px;
  padding: 20px 24px;
}

.search-form {
  :deep(.el-form-item) {
    margin-bottom: 0;
  }

  :deep(.el-form-item__label) {
    font-size: 15px;
    color: $ink-gray;
  }

  :deep(.el-input__inner) {
    font-size: 14px;
  }
}

.table-area {
  padding: 20px 24px;
}

.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;

  h3 {
    font-size: 17px;
    font-weight: 600;
    color: $ink-gray;
    margin: 0;
  }
}

.action-group {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

:deep(.el-table) {
  font-size: 14px;
  th.el-table__cell > .cell {
    font-size: 14px;
    font-weight: 600;
    color: $ink-gray;
  }
  td.el-table__cell > .cell {
    font-size: 14px;
  }
}

// 问答详情样式
.question-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid $border-light;
}

.section-title {
  font-size: 17px;
  font-weight: 500;
  color: $ink-gray;
  margin-bottom: 16px;
  padding-left: 12px;
  border-left: 3px solid $ink-blue;
}

.question-title {
  font-size: 20px;
  font-weight: 600;
  color: $ink-gray;
  margin-bottom: 10px;
}

.question-meta {
  font-size: 14px;
  color: $ink-gray-lighter;
  margin-bottom: 16px;

  span {
    margin-right: 20px;
  }
}

.question-content {
  font-size: 15px;
  color: $ink-gray-light;
  line-height: 1.8;
}

.answers-section {
  .answer-count {
    font-weight: normal;
    color: $ink-gray-lighter;
    margin-left: 4px;
  }
}

.empty-answers {
  text-align: center;
  padding: 40px;
  color: $ink-gray-lighter;
  font-size: 15px;
}

.answer-list {
  .answer-item {
    padding: 18px;
    border: 1px solid $border-light;
    border-radius: $radius-md;
    margin-bottom: 16px;
    transition: $transition;

    &:hover {
      border-color: rgba($ink-blue, 0.3);
    }

    &.is-accepted {
      border-color: rgba($success, 0.5);
      background-color: rgba($success, 0.03);
    }
  }

  .answer-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
  }

  .answer-user {
    font-size: 14px;
    font-weight: 500;
    color: $ink-gray;
  }

  .answer-content {
    font-size: 15px;
    color: $ink-gray-light;
    line-height: 1.8;
    margin-bottom: 12px;
  }

  .answer-actions {
    text-align: right;
  }
}

.id-text {
  font-size: 13px;
  color: $ink-gray-light;
}

.view-count {
  font-size: 14px;
  color: $ink-gray-light;
}

.answer-time {
  font-size: 13px;
  color: $ink-gray-lighter;
  margin-left: auto;
}
</style>

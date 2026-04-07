<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar ink-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关联课程">
          <el-select
            v-model="searchForm.courseId"
            placeholder="全部课程"
            clearable
            filterable
            style="width: 200px;"
          >
            <el-option
              v-for="c in courseList"
              :key="c.id"
              :label="c.title"
              :value="Number(c.id)"
            />
          </el-select>
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
        <h3 class="ink-title">作业列表</h3>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增作业
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="作业标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="courseId" label="关联课程" width="150" align="center">
          <template #default="{ row }">
            {{ getCourseName(row.courseId) }}
          </template>
        </el-table-column>
        <el-table-column prop="deadline" label="截止时间" width="170" align="center">
          <template #default="{ row }">
            <span :class="{ 'deadline-over': isOverdue(row.deadline) }">
              {{ row.deadline || '—' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="totalScore" label="总分" width="80" align="center">
          <template #default="{ row }">
            <span class="score-text">{{ row.totalScore ?? '—' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="submissionCount" label="提交数" width="90" align="center">
          <template #default="{ row }">
            <el-tag type="info" effect="plain">{{ row.submissionCount ?? 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="200" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleViewSubmissions(row)">查看提交</el-button>
              <el-button type="primary" text @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="关联课程" prop="courseId">
          <el-select
            v-model="form.courseId"
            placeholder="请选择关联课程"
            filterable
            style="width: 100%;"
          >
            <el-option
              v-for="c in courseList"
              :key="c.id"
              :label="c.title"
              :value="Number(c.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="作业标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入作业标题" />
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker
            v-model="form.deadline"
            type="datetime"
            placeholder="请选择截止时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input-number v-model="form.totalScore" :min="0" :max="999" placeholder="请输入总分" />
        </el-form-item>
        <el-form-item label="作业描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入作业描述" />
        </el-form-item>
        <el-form-item label="作业内容">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入作业内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看提交对话框 -->
    <el-dialog
      v-model="submissionDialogVisible"
      title="作业提交列表"
      width="800px"
      destroy-on-close
    >
      <div class="submission-toolbar">
        <el-radio-group v-model="submissionFilter" @change="fetchSubmissions">
          <el-radio-button :value="undefined">全部</el-radio-button>
          <el-radio-button :value="0">待批改</el-radio-button>
          <el-radio-button :value="1">已批改</el-radio-button>
        </el-radio-group>
      </div>
      <el-table :data="submissionList" v-loading="submissionLoading" stripe class="submission-table">
        <el-table-column prop="id" label="提交ID" width="90" align="center" />
        <el-table-column prop="studentId" label="学生ID" width="100" align="center" />
        <el-table-column prop="submitTime" label="提交时间" width="170" align="center" />
        <el-table-column prop="score" label="得分" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.score !== null && row.score !== undefined" type="success">
              {{ row.score }}
            </el-tag>
            <span v-else class="pending-text">待批改</span>
          </template>
        </el-table-column>
        <el-table-column prop="feedback" label="教师反馈" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.score === null || row.score === undefined"
              type="primary"
              text
              size="small"
              @click="handleGrade(row)"
            >
              去批改
            </el-button>
            <el-tag v-else type="info" size="small">已批改</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="submissionPagination.page"
        v-model:page-size="submissionPagination.pageSize"
        :total="submissionPagination.total"
        :page-sizes="[5, 10, 20]"
        layout="total, prev, pager, next"
        @size-change="fetchSubmissions"
        @current-change="fetchSubmissions"
        style="margin-top: 16px; justify-content: flex-end;"
      />
    </el-dialog>

    <!-- 批改对话框 -->
    <el-dialog
      v-model="gradeDialogVisible"
      title="批改作业"
      width="500px"
      destroy-on-close
    >
      <el-form :model="gradeForm" label-width="80px">
        <el-form-item label="得分">
          <el-input-number v-model="gradeForm.score" :min="0" :max="currentSubmissionTotalScore" />
          <span style="margin-left: 8px; color: #999;">满分 {{ currentSubmissionTotalScore }}</span>
        </el-form-item>
        <el-form-item label="反馈">
          <el-input
            v-model="gradeForm.feedback"
            type="textarea"
            :rows="4"
            placeholder="请输入批改反馈"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleGradeSubmit">提交成绩</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { assignmentApi, courseApi } from '@/api'
import { unwrapList } from '@/utils/apiResult'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增作业')
const formRef = ref(null)

const searchForm = reactive({
  courseId: null
})

const courseList = ref([])

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

const form = reactive({
  id: null,
  courseId: null,
  title: '',
  description: '',
  deadline: '',
  totalScore: 100,
  content: ''
})

const rules = {
  courseId: [{ required: true, message: '请选择关联课程', trigger: 'change' }],
  title: [{ required: true, message: '请输入作业标题', trigger: 'blur' }],
  totalScore: [{ required: true, message: '请输入总分', trigger: 'blur' }]
}

const submissionDialogVisible = ref(false)
const submissionLoading = ref(false)
const submissionFilter = ref(undefined)
const submissionList = ref([])
const submissionPagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})
const currentSubmissionId = ref(null)
const currentSubmissionTotalScore = ref(100)

const gradeDialogVisible = ref(false)
const gradeForm = reactive({
  submissionId: null,
  score: 0,
  feedback: ''
})

const getCourseName = (courseId) => {
  if (courseId == null) return '—'
  const c = courseList.value.find((item) => Number(item.id) === Number(courseId))
  return c ? c.title : `课程${courseId}`
}

const isOverdue = (deadline) => {
  if (!deadline) return false
  return new Date(deadline) < new Date()
}

const fetchCourses = async () => {
  try {
    const res = await courseApi.list({ page: 1, pageSize: 1000 })
    courseList.value = unwrapList(res)
  } catch (error) {
    console.error('获取课程列表失败', error)
  }
}

const fetchAssignments = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...(searchForm.courseId && { courseId: searchForm.courseId })
    }
    const res = await assignmentApi.list(params)
    const data = res.data
    // 👑 面试考点：后端返回 PageResult，分页数据在 records 字段
    tableData.value = data?.records || []
    pagination.total = data?.total || 0
  } catch (error) {
    console.error('获取作业列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchAssignments()
}

const handleReset = () => {
  searchForm.courseId = null
  handleSearch()
}

const handleSizeChange = () => {
  pagination.page = 1
  fetchAssignments()
}

const handleCurrentChange = () => {
  fetchAssignments()
}

const handleAdd = () => {
  form.id = null
  form.courseId = null
  form.title = ''
  form.description = ''
  form.deadline = ''
  form.totalScore = 100
  form.content = ''
  dialogTitle.value = '新增作业'
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  try {
    const res = await assignmentApi.getById(row.id)
    const d = res.data
    form.id = d.id
    form.courseId = d.courseId
    form.title = d.title
    form.description = d.description || ''
    form.deadline = d.deadline || ''
    form.totalScore = d.totalScore || 100
    form.content = d.content || ''
    currentSubmissionTotalScore.value = d.totalScore || 100
    dialogTitle.value = '编辑作业'
    dialogVisible.value = true
  } catch (error) {
    console.error('获取作业详情失败', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (form.id) {
        await assignmentApi.update(form.id, {
          title: form.title,
          description: form.description,
          deadline: form.deadline || null,
          totalScore: form.totalScore,
          content: form.content
        })
        ElMessage.success('编辑成功')
      } else {
        await assignmentApi.add({
          courseId: form.courseId,
          title: form.title,
          description: form.description,
          deadline: form.deadline || null,
          totalScore: form.totalScore,
          content: form.content
        })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchAssignments()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该作业吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await assignmentApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchAssignments()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const handleViewSubmissions = async (row) => {
  currentSubmissionId.value = row.id
  currentSubmissionTotalScore.value = row.totalScore || 100
  submissionDialogVisible.value = true
  submissionPagination.page = 1
  submissionFilter.value = undefined
  await fetchSubmissions()
}

const fetchSubmissions = async () => {
  submissionLoading.value = true
  try {
    const params = {
      page: submissionPagination.page,
      pageSize: submissionPagination.pageSize,
      ...(submissionFilter.value !== undefined && { graded: submissionFilter.value })
    }
    const res = await assignmentApi.submissions(currentSubmissionId.value, params)
    submissionList.value = res.data?.records || []
    submissionPagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取提交列表失败', error)
  } finally {
    submissionLoading.value = false
  }
}

const handleGrade = (row) => {
  gradeForm.submissionId = row.id
  gradeForm.score = 0
  gradeForm.feedback = ''
  gradeDialogVisible.value = true
}

const handleGradeSubmit = async () => {
  if (gradeForm.score === null || gradeForm.score === undefined) {
    ElMessage.warning('请输入得分')
    return
  }
  try {
    await assignmentApi.grade({
      submissionId: gradeForm.submissionId,
      score: gradeForm.score,
      feedback: gradeForm.feedback
    })
    ElMessage.success('批改成功')
    gradeDialogVisible.value = false
    fetchSubmissions()
  } catch (error) {
    console.error('批改失败', error)
  }
}

onMounted(() => {
  fetchCourses()
  fetchAssignments()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.page-container { font-size: 15px; }
.search-bar { margin-bottom: 24px; padding: 20px 24px; }
.search-form { :deep(.el-form-item) { margin-bottom: 0; } }
.table-area { padding: 20px 24px; }
.table-header {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;
  h3 { font-size: 17px; font-weight: 600; color: $ink-gray; margin: 0; }
}
.action-group { display: flex; align-items: center; justify-content: center; gap: 4px; }
.deadline-over { color: $danger; }
.score-text { font-weight: 600; color: $ink-blue; }
.pending-text { color: $warning; font-size: 13px; }
.submission-toolbar { margin-bottom: 16px; }
.submission-table { margin-top: 0; }
</style>

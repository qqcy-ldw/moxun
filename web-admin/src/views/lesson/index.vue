<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar ink-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="选择课程">
          <el-select
            v-model="searchForm.courseId"
            placeholder="请先选择课程"
            clearable
            filterable
            style="width: 240px;"
            @change="handleCourseChange"
          >
            <el-option
              v-for="c in courseList"
              :key="c.id"
              :label="c.title"
              :value="Number(c.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择章节">
          <el-select
            v-model="searchForm.chapterId"
            placeholder="请先选择章节"
            clearable
            :disabled="!searchForm.courseId"
            style="width: 200px;"
            @change="handleChapterChange"
          >
            <el-option
              v-for="ch in chapterList"
              :key="ch.id"
              :label="ch.title"
              :value="Number(ch.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="searchForm.chapterId">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增课时
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 课时列表 -->
    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">
          <template v-if="searchForm.courseId">
            {{ getCourseName(searchForm.courseId) }}
            <template v-if="searchForm.chapterId"> — {{ getChapterName(searchForm.chapterId) }}</template>
            <span class="header-sub">课时列表</span>
          </template>
          <template v-else>
            课时列表
          </template>
        </h3>
        <span v-if="!searchForm.chapterId" class="tip-text">
          请先选择课程和章节
        </span>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="课时ID" width="100" align="center" />
        <el-table-column prop="title" label="课时标题" min-width="200" />
        <el-table-column prop="videoUrl" label="视频地址" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <a v-if="row.videoUrl" :href="row.videoUrl" target="_blank" class="video-link">
              {{ row.videoUrl }}
            </a>
            <span v-else class="no-data">—</span>
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长" width="100" align="center">
          <template #default="{ row }">
            <span class="duration-text">{{ formatDuration(row.duration) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isFree" label="是否免费" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isFree === 1 ? 'success' : 'info'" effect="plain">
              {{ row.isFree === 1 ? '免费试看' : '付费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column label="操作" min-width="140" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select
            v-model="form.courseId"
            placeholder="请选择所属课程"
            filterable
            style="width: 100%;"
            :disabled="!!form.id"
            @change="handleFormCourseChange"
          >
            <el-option
              v-for="c in courseList"
              :key="c.id"
              :label="c.title"
              :value="Number(c.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属章节" prop="chapterId">
          <el-select
            v-model="form.chapterId"
            placeholder="请先选择课程"
            style="width: 100%;"
            :disabled="!!form.id"
            >
              <el-option
                v-for="ch in formChapterList"
                :key="ch.id"
                :label="ch.title"
                :value="Number(ch.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课时标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入课时标题" />
        </el-form-item>
        <el-form-item label="视频地址">
          <el-input v-model="form.videoUrl" placeholder="请输入视频URL" />
        </el-form-item>
        <el-form-item label="时长（秒）">
          <el-input-number v-model="form.duration" :min="0" :max="86400" />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="form.isFree">
            <el-radio :label="1">免费试看</el-radio>
            <el-radio :label="0">付费</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { courseApi, chapterApi, sectionApi } from '@/api'
import { unwrapList } from '@/utils/apiResult'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增课时')
const formRef = ref(null)

const courseList = ref([])
const chapterList = ref([])
const formChapterList = ref([])

const searchForm = reactive({
  courseId: null,
  chapterId: null
})

const tableData = ref([])

const getCourseName = (courseId) => {
  if (courseId == null) return '—'
  const c = courseList.value.find((item) => Number(item.id) === Number(courseId))
  return c ? c.title : `课程${courseId}`
}

const getChapterName = (chapterId) => {
  if (chapterId == null) return '—'
  const ch = chapterList.value.find((item) => Number(item.id) === Number(chapterId))
  return ch ? ch.title : `章节${chapterId}`
}

const formatDuration = (seconds) => {
  if (!seconds) return '—'
  const s = Number(seconds)
  if (s >= 3600) return `${Math.floor(s / 3600)}小时${Math.floor((s % 3600) / 60)}分`
  if (s >= 60) return `${Math.floor(s / 60)}分${s % 60}秒`
  return `${s}秒`
}

const handleCourseChange = async (courseId) => {
  searchForm.chapterId = null
  tableData.value = []
  if (courseId) {
    await fetchChaptersByCourse(courseId)
  } else {
    chapterList.value = []
  }
}

const handleChapterChange = async (chapterId) => {
  if (chapterId) {
    await fetchSections()
  } else {
    tableData.value = []
  }
}

const fetchCourses = async () => {
  try {
    const res = await courseApi.list({ page: 1, pageSize: 1000 })
    courseList.value = unwrapList(res)
  } catch (error) {
    console.error('获取课程列表失败', error)
  }
}

const fetchChaptersByCourse = async (courseId) => {
  try {
    const res = await chapterApi.listByCourse(courseId)
    chapterList.value = unwrapList(res)
  } catch (error) {
    console.error('获取章节列表失败', error)
  }
}

const fetchSections = async () => {
  if (!searchForm.chapterId) return
  loading.value = true
  try {
    const res = await sectionApi.listByChapter(searchForm.chapterId)
    tableData.value = unwrapList(res)
  } catch (error) {
    console.error('获取课时列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleFormCourseChange = async (courseId) => {
  form.chapterId = null
  formChapterList.value = []
  if (courseId) {
    try {
      const res = await chapterApi.listByCourse(courseId)
      formChapterList.value = unwrapList(res)
    } catch (error) {
      console.error('获取章节列表失败', error)
    }
  }
}

const handleAdd = () => {
  form.id = null
  form.courseId = searchForm.courseId
  form.chapterId = searchForm.chapterId
  form.title = ''
  form.videoUrl = ''
  form.duration = 0
  form.isFree = 0
  form.sort = tableData.value.length + 1
  dialogTitle.value = '新增课时'
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  form.id = row.id
  form.courseId = row.courseId || searchForm.courseId
  form.chapterId = row.chapterId
  form.title = row.title
  form.videoUrl = row.videoUrl || ''
  form.duration = row.duration || 0
  form.isFree = row.isFree ?? 0
  form.sort = row.sort || 0
  dialogTitle.value = '编辑课时'
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (form.id) {
        await sectionApi.update(form.id, {
          title: form.title,
          videoUrl: form.videoUrl,
          duration: form.duration,
          isFree: form.isFree,
          sort: form.sort
        })
        ElMessage.success('编辑成功')
      } else {
        await sectionApi.add({
          chapterId: form.chapterId,
          title: form.title,
          videoUrl: form.videoUrl,
          duration: form.duration,
          isFree: form.isFree,
          sort: form.sort
        })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchSections()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该课时吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await sectionApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchSections()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

onMounted(() => {
  fetchCourses()
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
}

.table-area {
  padding: 20px 24px;
}

.table-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  h3 {
    font-size: 17px;
    font-weight: 600;
    color: $ink-gray;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.header-sub {
  font-size: 14px;
  font-weight: 400;
  color: $ink-gray-lighter;
}

.tip-text {
  font-size: 13px;
  color: $ink-gray-lighter;
}

.action-group {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.duration-text {
  font-family: monospace;
  font-size: 13px;
  color: $ink-gray-light;
}

.video-link {
  color: $ink-blue;
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
}

.no-data {
  color: $ink-gray-lighter;
}
</style>

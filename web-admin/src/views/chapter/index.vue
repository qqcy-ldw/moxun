<template>
  <div class="page-container">
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
            <el-option v-for="c in courseList" :key="c.id" :label="c.title" :value="Number(c.id)" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="searchForm.courseId">
          <el-button type="primary" @click="handleAddChapter">
            <el-icon><Plus /></el-icon>
            新增章节
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">
          {{ searchForm.courseId ? getCourseName(searchForm.courseId) + ' - 章节列表' : '章节列表' }}
        </h3>
        <span v-if="!searchForm.courseId" class="tip-text">请先从上方选择课程</span>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="章节ID" width="100" align="center" />
        <el-table-column prop="title" label="章节标题" min-width="200" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="courseId" label="所属课程" min-width="180">
          <template #default>{{ getCourseName(searchForm.courseId) }}</template>
        </el-table-column>
        <el-table-column label="课时数" width="90" align="center">
          <template #default="{ row }">
            <el-tag type="info" effect="plain">{{ row.sections?.length ?? row.sectionCount ?? 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleAddSection(row)">添课时</el-button>
              <el-button type="primary" text @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="expandedSections.size > 0" class="section-expand-area">
        <el-table :data="expandedSectionList" stripe size="small" class="section-child-table">
          <el-table-column prop="id" label="课时ID" width="100" align="center" />
          <el-table-column prop="title" label="课时标题" min-width="180" />
          <el-table-column prop="duration" label="时长" width="90" align="center">
            <template #default="{ row }">{{ formatDuration(row.duration) }}</template>
          </el-table-column>
          <el-table-column prop="isFree" label="是否免费" width="90" align="center">
            <template #default="{ row }">
              <el-tag :type="row.isFree === 1 ? 'success' : 'info'" size="small">
                {{ row.isFree === 1 ? '免费试看' : '付费' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" align="center" />
          <el-table-column label="操作" width="120" align="center">
            <template #default="{ row }">
              <div class="action-group">
                <el-button type="primary" text size="small" @click="handleEditSection(row)">编辑</el-button>
                <el-button type="danger" text size="small" @click="handleDeleteSection(row)">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 章节对话框 -->
    <el-dialog v-model="chapterDialogVisible" :title="chapterDialogTitle" width="500px" destroy-on-close>
      <el-form ref="chapterFormRef" :model="chapterForm" :rules="chapterRules" label-width="100px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="chapterForm.courseId" placeholder="请选择所属课程" filterable style="width: 100%;" :disabled="!!chapterForm.id">
            <el-option v-for="c in courseList" :key="c.id" :label="c.title" :value="Number(c.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="章节标题" prop="title">
          <el-input v-model="chapterForm.title" placeholder="请输入章节标题" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="chapterForm.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="chapterDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChapterSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 课时对话框 -->
    <el-dialog v-model="sectionDialogVisible" :title="sectionDialogTitle" width="600px" destroy-on-close>
      <el-form ref="sectionFormRef" :model="sectionForm" :rules="sectionRules" label-width="100px">
        <el-form-item label="所属章节" prop="chapterId">
          <el-select v-model="sectionForm.chapterId" placeholder="请选择所属章节" style="width: 100%;" :disabled="!!sectionForm.id">
            <el-option v-for="ch in tableData" :key="ch.id" :label="ch.title" :value="Number(ch.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="课时标题" prop="title">
          <el-input v-model="sectionForm.title" placeholder="请输入课时标题" />
        </el-form-item>
        <el-form-item label="视频地址">
          <el-input v-model="sectionForm.videoUrl" placeholder="请输入视频URL" />
        </el-form-item>
        <el-form-item label="时长（秒）">
          <el-input-number v-model="sectionForm.duration" :min="0" :max="86400" />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="sectionForm.isFree">
            <el-radio :label="1">免费试看</el-radio>
            <el-radio :label="0">付费</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="sectionForm.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="sectionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSectionSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { chapterApi, courseApi, sectionApi } from '@/api'
import { unwrapList } from '@/utils/apiResult'

const loading = ref(false)
const courseList = ref([])
const searchForm = reactive({ courseId: null })
const tableData = ref([])
const expandedSections = reactive(new Set())
const expandedSectionList = ref([])

const getCourseName = (courseId) => {
  if (courseId == null || courseId === '') return '—'
  const c = courseList.value.find((item) => Number(item.id) === Number(courseId))
  return c ? c.title : `课程${courseId}`
}

const formatDuration = (seconds) => {
  if (!seconds) return '—'
  const s = Number(seconds)
  if (s >= 3600) return `${Math.floor(s / 3600)}小时${Math.floor((s % 3600) / 60)}分`
  if (s >= 60) return `${Math.floor(s / 60)}分${s % 60}秒`
  return `${s}秒`
}

const handleCourseChange = async (courseId) => {
  expandedSections.clear()
  expandedSectionList.value = []
  if (courseId) await fetchChapters()
  else tableData.value = []
}

const fetchCourses = async () => {
  try {
    const res = await courseApi.list({ page: 1, pageSize: 1000 })
    courseList.value = unwrapList(res)
  } catch (error) {
    console.error('获取课程列表失败', error)
  }
}

const fetchChapters = async () => {
  if (!searchForm.courseId) return
  loading.value = true
  try {
    const res = await chapterApi.listByCourse(searchForm.courseId)
    tableData.value = unwrapList(res)
  } catch (error) {
    console.error('获取章节列表失败', error)
  } finally {
    loading.value = false
  }
}

const chapterDialogVisible = ref(false)
const chapterDialogTitle = ref('新增章节')
const chapterFormRef = ref(null)
const chapterForm = reactive({ id: null, courseId: null, title: '', sort: 0 })
const chapterRules = {
  courseId: [{ required: true, message: '请选择所属课程', trigger: 'change' }],
  title: [{ required: true, message: '请输入章节标题', trigger: 'blur' }]
}

const handleAddChapter = () => {
  chapterForm.id = null
  chapterForm.courseId = searchForm.courseId
  chapterForm.title = ''
  chapterForm.sort = tableData.value.length + 1
  chapterDialogTitle.value = '新增章节'
  chapterDialogVisible.value = true
}

const handleEdit = async (row) => {
  Object.assign(chapterForm, {
    id: row.id,
    courseId: row.courseId ?? searchForm.courseId,
    title: row.title,
    sort: row.sort
  })
  chapterDialogTitle.value = '编辑章节'
  chapterDialogVisible.value = true
}

const handleChapterSubmit = async () => {
  if (!chapterFormRef.value) return
  await chapterFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (chapterForm.id) {
        await chapterApi.update(chapterForm.id, { title: chapterForm.title, sort: chapterForm.sort })
        ElMessage.success('编辑成功')
      } else {
        await chapterApi.add({ courseId: chapterForm.courseId, title: chapterForm.title, sort: chapterForm.sort })
        ElMessage.success('新增成功')
      }
      chapterDialogVisible.value = false
      fetchChapters()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

const handleDelete = async (row) => {
  const sectionLen = row.sections?.length ?? row.sectionCount ?? 0
  if (sectionLen > 0) {
    ElMessage.warning('该章节下有课时，请先删除课时')
    return
  }
  try {
    await ElMessageBox.confirm('确定要删除该章节吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    await chapterApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchChapters()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

const sectionDialogVisible = ref(false)
const sectionDialogTitle = ref('新增课时')
const sectionFormRef = ref(null)
const sectionForm = reactive({ id: null, chapterId: null, title: '', videoUrl: '', duration: 0, isFree: 0, sort: 0 })
const sectionRules = {
  chapterId: [{ required: true, message: '请选择所属章节', trigger: 'change' }],
  title: [{ required: true, message: '请输入课时标题', trigger: 'blur' }]
}

const handleAddSection = async (chapterRow) => {
  Object.assign(sectionForm, { id: null, chapterId: chapterRow.id, title: '', videoUrl: '', duration: 0, isFree: 0, sort: 0 })
  sectionDialogTitle.value = '新增课时'
  sectionDialogVisible.value = true
}

const handleEditSection = async (row) => {
  Object.assign(sectionForm, {
    id: row.id, chapterId: row.chapterId, title: row.title,
    videoUrl: row.videoUrl || '', duration: row.duration || 0,
    isFree: row.isFree ?? 0, sort: row.sort || 0
  })
  sectionDialogTitle.value = '编辑课时'
  sectionDialogVisible.value = true
}

const handleSectionSubmit = async () => {
  if (!sectionFormRef.value) return
  await sectionFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (sectionForm.id) {
        await sectionApi.update(sectionForm.id, {
          title: sectionForm.title, videoUrl: sectionForm.videoUrl,
          duration: sectionForm.duration, isFree: sectionForm.isFree, sort: sectionForm.sort
        })
        ElMessage.success('编辑成功')
      } else {
        await sectionApi.add({
          chapterId: sectionForm.chapterId, title: sectionForm.title,
          videoUrl: sectionForm.videoUrl, duration: sectionForm.duration,
          isFree: sectionForm.isFree, sort: sectionForm.sort
        })
        ElMessage.success('新增成功')
      }
      sectionDialogVisible.value = false
      fetchChapters()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

const handleDeleteSection = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该课时吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    await sectionApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchChapters()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

onMounted(() => { fetchCourses() })
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;
.page-container { font-size: 15px; }
.search-bar { margin-bottom: 24px; padding: 20px 24px; }
.search-form { :deep(.el-form-item) { margin-bottom: 0; } }
.table-area { padding: 20px 24px; }
.table-header { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; h3 { font-size: 17px; font-weight: 600; color: $ink-gray; margin: 0; } }
.tip-text { font-size: 13px; color: $ink-gray-lighter; }
.action-group { display: flex; align-items: center; justify-content: center; gap: 4px; }
.section-expand-area { margin-top: 16px; padding: 16px; border: 1px dashed $border-light; border-radius: $radius-md; background: rgba($ink-white, 0.6); }
</style>

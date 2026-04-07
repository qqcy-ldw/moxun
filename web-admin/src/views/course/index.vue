<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar ink-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.title" placeholder="请输入课程名称" clearable />
        </el-form-item>
        <el-form-item label="课程状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已下架" value="OFFLINE" />
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
        <h3 class="ink-title">课程列表</h3>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增课程
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column label="课程封面" width="120" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.coverImage"
              :src="row.coverImage"
              fit="cover"
              style="width: 80px; height: 45px; border-radius: 4px;"
            />
            <div v-else class="cover-placeholder">
              <el-icon><Picture /></el-icon>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="课程名称" min-width="200" />
        <el-table-column prop="teacher" label="讲师" width="120" align="center" />
        <el-table-column prop="category" label="分类" width="120" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="100" align="center">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentCount" label="学生数" width="90" align="center" />
        <el-table-column prop="rating" label="评分" width="80" align="center">
          <template #default="{ row }">
            <span class="rating-text">{{ row.rating ? row.rating.toFixed(1) : '—' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="操作" min-width="140" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleEdit(row)">编辑</el-button>
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

    <!--
      新增/编辑对话框
      ⚠️ 注意：dialog 不使用 destroy-on-close（否则每次打开都重建内部组件）
      Cascader 放在 dialog 内，整个 dialog 实例复用，options 变化通过 reactive 更新
      不触发 Element Plus 内部 parentNode 为 null 的崩溃
    -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      align-center
      append-to-body
      class="course-edit-dialog"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="课程名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程分类" prop="categoryId">
          <!-- 👑 面试考点：el-cascader 用树形面板展示多级分类，emitPath=false 绑定叶子 id，与后端 categoryId 一致 -->
          <!-- ⚠️ 注意：dialog 不 destroy-on-close 保证 Cascader 只在 onMounted 时创建一次，后续 options 变化通过
               computed reactive 更新，不触发 Element Plus 内部 DOM 重新挂载，彻底规避 parentNode null 报错 -->
          <el-cascader
            v-model="form.categoryId"
            class="category-cascader"
            :options="categoryCascaderOptions"
            :props="cascaderProps"
            placeholder="请选择分类"
            clearable
            filterable
            :show-all-levels="false"
            popper-class="category-cascader-popper"
          />
        </el-form-item>
        <el-form-item label="授课讲师" prop="teacherId">
          <el-select v-model="form.teacherId" placeholder="请选择讲师" style="width: 100%;" filterable>
            <el-option
              v-for="teacher in mergedTeachers"
              :key="teacher.id"
              :label="teacher.username"
              :value="Number(teacher.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程封面">
          <el-input v-model="form.coverImage" placeholder="请输入封面图片URL" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入课程描述" />
        </el-form-item>
        <el-form-item label="课程价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="课程状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%;">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已下架" value="OFFLINE" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度级别">
          <el-select v-model="form.level" placeholder="请选择难度" style="width: 100%;">
            <el-option label="初级" value="BEGINNER" />
            <el-option label="中级" value="INTERMEDIATE" />
            <el-option label="高级" value="ADVANCED" />
          </el-select>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus, Picture } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { courseApi } from '@/api'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增课程')
const formRef = ref(null)

const searchForm = reactive({
  title: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const teachers = ref([])
// 👑 面试考点：分类接口返回 List<CourseCategoryVO>，每项含 id、name、children 等
const categories = ref([])
const fetchCategories = async () => {
  try {
    const res = await courseApi.getCategories()
    categories.value = res.data || []
  } catch (error) {
    console.error('获取分类列表失败', error)
  }
}

// 分类树规范化：id 统一为 Number，children 保持数组（空则 [])
const normalizeCategoryTree = (nodes) => {
  if (!Array.isArray(nodes)) return []
  return nodes.map((n) => {
    const rawChildren = n.children
    const children =
      Array.isArray(rawChildren) && rawChildren.length > 0 ? normalizeCategoryTree(rawChildren) : []
    return {
      ...n,
      id: Number(n.id),
      name: n.name,
      children
    }
  })
}

const idExistsInCategoryTree = (nodes, id) => {
  if (id == null || !Array.isArray(nodes)) return false
  for (const n of nodes) {
    if (Number(n.id) === id) return true
    if (n.children?.length && idExistsInCategoryTree(n.children, id)) return true
  }
  return false
}

// 兜底分组 id（库内自增 id 为正，不会冲突）
const CATEGORY_ORPHAN_GROUP_ID = -999999

const categoryFallback = ref(null)
const teacherFallback = ref(null)

// ⚠️ 注意：computed 开头返回 [] 兜底，防止 categories.value 未到位时 el-cascader 内部 .slice() 报错
const categoryCascaderOptions = computed(() => {
  if (!categories.value || !Array.isArray(categories.value)) return []
  const base = normalizeCategoryTree(categories.value)
  const fb = categoryFallback.value
  if (!fb || idExistsInCategoryTree(base, fb.id)) return base
  return [
    ...base,
    {
      id: CATEGORY_ORPHAN_GROUP_ID,
      name: '▸ 当前分类（树未同步）',
      children: [{ id: fb.id, name: fb.name, children: [] }]
    }
  ]
})

// 👑 面试考点：el-cascader props：value/id 字段名、label/name 展示名、children 子节点字段
// emitPath=false：v-model 只绑定叶子 categoryId，与后端字段一致
// checkStrictly=false：必须选满叶子节点，不允许只选父级
const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  emitPath: false,
  expandTrigger: 'hover',
  checkStrictly: false
}

const mergedTeachers = computed(() => {
  const base = teachers.value
  const fb = teacherFallback.value
  if (!fb) return base
  if (base.some((t) => Number(t.id) === fb.id)) return base
  return [...base, fb]
})

const form = reactive({
  id: null,
  title: '',
  categoryId: null,
  teacherId: null,
  coverImage: '',
  description: '',
  price: 0,
  status: 'DRAFT',
  level: ''
})

const rules = {
  title: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择课程分类', trigger: 'change' }],
  teacherId: [{ required: true, message: '请选择授课讲师', trigger: 'change' }],
  status: [{ required: true, message: '请选择课程状态', trigger: 'change' }]
}

// 👑 面试考点：后端可能返回 draft / published（小写），表格展示前统一转大写再 if 判断
const getStatusType = (status) => {
  if (status == null || status === '') return 'info'
  const s = String(status).toUpperCase()
  if (s === 'DRAFT') return 'info'
  if (s === 'PUBLISHED') return 'success'
  if (s === 'OFFLINE') return 'warning'
  return 'info'
}

const getStatusName = (status) => {
  if (status == null || status === '') return '—'
  const s = String(status).toUpperCase()
  if (s === 'DRAFT') return '草稿'
  if (s === 'PUBLISHED') return '已发布'
  if (s === 'OFFLINE') return '已下架'
  return String(status)
}

// 表单/搜索用的状态值统一为大写，与 el-option 的 value 一致
const normalizeStatus = (status) => {
  if (status == null || status === '') return 'DRAFT'
  return String(status).toUpperCase()
}

// 获取课程列表
const fetchCourses = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      title: searchForm.title || undefined,
      status: searchForm.status || undefined
    }
    const res = await courseApi.list(params)
    tableData.value = res.data.records || res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取课程列表失败', error)
  } finally {
    loading.value = false
  }
}

// 获取讲师列表
const fetchTeachers = async () => {
  try {
    const res = await courseApi.getTeachers()
    teachers.value = res.data || []
  } catch (error) {
    console.error('获取讲师列表失败', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchCourses()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.status = ''
  handleSearch()
}

// 分页
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  fetchCourses()
}

const handleCurrentChange = (val) => {
  pagination.page = val
  fetchCourses()
}

// 新增
const handleAdd = () => {
  categoryFallback.value = null
  teacherFallback.value = null
  form.id = null
  form.title = ''
  form.categoryId = null
  form.teacherId = null
  form.coverImage = ''
  form.description = ''
  form.price = 0
  form.status = 'DRAFT'
  form.level = ''
  dialogTitle.value = '新增课程'
  dialogVisible.value = true
}

// 编辑
// ⚠️ 注意：CourseDetailVO 返回的是 teacherId（Integer），teacherName 不在 VO 中
// 通过 teachers 列表反查 id 对应的 username，作为展示用
const handleEdit = async (row) => {
  try {
    const res = await courseApi.getById(row.id)
    const courseData = res.data
    const cid = courseData.categoryId != null ? Number(courseData.categoryId) : null
    const tid = courseData.teacherId != null ? Number(courseData.teacherId) : null

    // 兜底：当前 id 不在已加载的 options 里时，用列表行上的名称补一条 option
    categoryFallback.value = null
    teacherFallback.value = null
    if (cid != null && row.category) {
      const inTree = idExistsInCategoryTree(normalizeCategoryTree(categories.value), cid)
      if (!inTree) {
        categoryFallback.value = { id: cid, name: String(row.category) }
      }
    }
    if (tid != null && row.teacher) {
      const inList = teachers.value.some((t) => Number(t.id) === tid)
      if (!inList) {
        teacherFallback.value = { id: tid, username: String(row.teacher) }
      }
    }

    form.id = courseData.id
    form.title = courseData.title
    form.categoryId = cid
    form.teacherId = tid
    form.coverImage = courseData.coverImage || ''
    form.description = courseData.description || ''
    form.price = courseData.price || 0
    form.status = normalizeStatus(courseData.status)
    form.level = courseData.level || ''
    dialogTitle.value = '编辑课程'
    dialogVisible.value = true
  } catch (error) {
    console.error('获取课程详情失败', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (form.id) {
        await courseApi.update(form.id, form)
        ElMessage.success('编辑成功')
      } else {
        await courseApi.add(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchCourses()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该课程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await courseApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchCourses()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

onMounted(() => {
  fetchCourses()
  fetchTeachers()
  fetchCategories()
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

.cover-placeholder {
  width: 80px;
  height: 45px;
  border-radius: 4px;
  background-color: $border-light;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $ink-gray-lighter;
}

.price-text {
  color: $danger;
  font-weight: 500;
}

.rating-text {
  color: $warning;
  font-weight: 600;
}

/* align-center 时若仍偏上，可收紧顶部外边距 */
:deep(.course-edit-dialog.el-dialog) {
  margin-top: 0;
}

/* 级联选择器占满表单项宽度；输入区只显示末级（show-all-levels=false） */
.category-cascader {
  width: 100%;
}

/* cascader 弹层 z-index 高于 dialog overlay（dialog overlay=2000，dialog=2001，cascader 默认>dialog） */
/* 若弹层被 dialog 遮挡，通过 popper-class 强制拉高 */
:deep(.category-cascader-popper.el-popper) {
  z-index: 9999 !important;
}
</style>

<template>
  <div class="page-container">
    <div class="search-bar ink-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关联课程">
          <el-select v-model="searchForm.courseId" placeholder="全部课程" clearable filterable style="width: 200px;">
            <el-option v-for="c in courseList" :key="c.id" :label="c.title" :value="Number(c.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="评分">
          <el-select v-model="searchForm.rating" placeholder="全部评分" clearable style="width: 120px;">
            <el-option v-for="r in 5" :key="r" :label="r + '星'" :value="r" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">课程评论列表</h3>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="courseId" label="关联课程" min-width="180">
          <template #default="{ row }">{{ getCourseName(row.courseId) }}</template>
        </el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100" align="center" />
        <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip />
        <el-table-column prop="rating" label="评分" width="100" align="center">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled :max="5" :colors="['#F59E0B', '#F59E0B', '#F59E0B']" style="display: inline-flex;" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="170" align="center" />
        <el-table-column label="操作" min-width="120" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleView(row)">查看</el-button>
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

    <el-dialog v-model="detailDialogVisible" title="评论详情" width="600px" destroy-on-close>
      <div v-loading="detailLoading">
        <template v-if="detailData">
          <div class="detail-meta">
            <span class="meta-item"><span class="meta-label">用户ID：</span>{{ detailData.userId }}</span>
            <span class="meta-item"><span class="meta-label">评论时间：</span>{{ detailData.createTime }}</span>
          </div>
          <div class="detail-course"><span class="meta-label">关联课程：</span>{{ getCourseName(detailData.courseId) }}</div>
          <div class="detail-rating"><span class="meta-label">评分：</span><el-rate v-model="detailData.rating" disabled :max="5" /></div>
          <div class="detail-content">
            <div class="content-label">评论内容</div>
            <div class="content-body">{{ detailData.content }}</div>
          </div>
        </template>
        <el-empty v-else description="暂无数据" />
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { commentApi, courseApi } from '@/api'
import { unwrapList } from '@/utils/apiResult'

const loading = ref(false)
const detailLoading = ref(false)
const detailDialogVisible = ref(false)
const detailData = ref(null)
const courseList = ref([])
const searchForm = reactive({ courseId: null, rating: null })
const pagination = reactive({ page: 1, pageSize: 10, total: 0 })
const tableData = ref([])

const getCourseName = (courseId) => {
  if (courseId == null) return '—'
  const c = courseList.value.find((item) => Number(item.id) === Number(courseId))
  return c ? c.title : `课程${courseId}`
}

const fetchCourses = async () => {
  try {
    const res = await courseApi.list({ page: 1, pageSize: 1000 })
    courseList.value = unwrapList(res)
  } catch (error) {
    console.error('获取课程列表失败', error)
  }
}

const fetchComments = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...(searchForm.courseId && { courseId: searchForm.courseId }),
      ...(searchForm.rating && { rating: searchForm.rating })
    }
    const res = await commentApi.list(params)
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取评论列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { pagination.page = 1; fetchComments() }
const handleReset = () => { searchForm.courseId = null; searchForm.rating = null; handleSearch() }
const handleSizeChange = () => { pagination.page = 1; fetchComments() }
const handleCurrentChange = () => { fetchComments() }

const handleView = async (row) => {
  detailLoading.value = true
  detailDialogVisible.value = true
  detailData.value = null
  try {
    const res = await commentApi.getById(row.id)
    detailData.value = res.data
  } catch (error) {
    console.error('获取评论详情失败', error)
  } finally {
    detailLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该评论吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    await commentApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchComments()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

onMounted(() => { fetchCourses(); fetchComments() })
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;
.page-container { font-size: 15px; }
.search-bar { margin-bottom: 24px; padding: 20px 24px; }
.search-form { :deep(.el-form-item) { margin-bottom: 0; } :deep(.el-form-item__label) { font-size: 15px; color: $ink-gray; } }
.table-area { padding: 20px 24px; }
.table-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; h3 { font-size: 17px; font-weight: 600; color: $ink-gray; margin: 0; } }
.action-group { display: flex; align-items: center; justify-content: center; gap: 4px; }
.detail-meta { display: flex; gap: 24px; margin-bottom: 16px; flex-wrap: wrap; }
.meta-item { font-size: 14px; color: $ink-gray; }
.meta-label { font-weight: 600; color: $ink-gray; }
.detail-course, .detail-rating { font-size: 14px; color: $ink-gray; margin-bottom: 12px; }
.detail-content { .content-label { font-size: 14px; font-weight: 600; color: $ink-gray; margin-bottom: 8px; } .content-body { font-size: 15px; color: $ink-gray-light; line-height: 1.8; padding: 12px 16px; background: $border-light; border-radius: $radius-md; } }
</style>

<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar ink-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.actionType" placeholder="请选择操作类型" clearable>
            <el-option
              v-for="item in actionTypes"
              :key="item.code"
              :label="item.description"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="操作状态">
          <el-select v-model="searchForm.loginStatus" placeholder="请选择状态" clearable>
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
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
        <h3 class="ink-title">操作日志</h3>
        <div class="header-actions">
          <el-button @click="handleClean" type="danger" plain>
            <el-icon><Delete /></el-icon>
            清理日志
          </el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="userId" label="用户ID" width="100" align="center" />
        <el-table-column prop="username" label="用户名" width="120" align="center">
          <template #default="{ row }">
            {{ row.username || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="actionType" label="操作类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag type="info">{{ row.actionType || 'OTHER' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="actionDesc" label="操作描述" min-width="160" />
        <el-table-column prop="requestUrl" label="请求路径" min-width="200" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="方法" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="getMethodType(row.requestMethod)" size="small">
              {{ row.requestMethod || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responseCode" label="响应码" width="100" align="center">
          <template #default="{ row }">
            <span :class="row.responseCode >= 200 && row.responseCode < 300 ? 'success-text' : 'error-text'">
              {{ row.responseCode || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="loginStatus" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.loginStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.loginStatus === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140" align="center" />
        <el-table-column prop="duration" label="耗时" width="100" align="center">
          <template #default="{ row }">
            <span :class="row.duration > 1000 ? 'error-text' : ''">
              {{ row.duration ? row.duration + 'ms' : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180" align="center" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" text @click="handleViewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="日志详情" width="700px" destroy-on-close>
      <el-descriptions :column="2" border v-if="currentLog">
        <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ currentLog.userId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentLog.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag type="info">{{ currentLog.actionType || 'OTHER' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作描述" :span="2">{{ currentLog.actionDesc || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">
          <span class="url-text">{{ currentLog.requestUrl || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="请求方法">
          <el-tag :type="getMethodType(currentLog.requestMethod)" size="small">
            {{ currentLog.requestMethod || '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="响应状态码">
          <span :class="currentLog.responseCode >= 200 && currentLog.responseCode < 300 ? 'success-text' : 'error-text'">
            {{ currentLog.responseCode || '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作状态">
          <el-tag :type="currentLog.loginStatus === 1 ? 'success' : 'danger'" size="small">
            {{ currentLog.loginStatus === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作耗时">{{ currentLog.duration ? currentLog.duration + 'ms' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求时间">{{ currentLog.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <div class="json-content" v-if="currentLog.requestParams">
            <pre>{{ formatJson(currentLog.requestParams) }}</pre>
          </div>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="错误响应" :span="2" v-if="currentLog.responseResult">
          <div class="json-content error-content">
            <pre>{{ formatJson(currentLog.responseResult) }}</pre>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="失败原因" :span="2" v-if="currentLog.failReason">
          <span class="error-text">{{ currentLog.failReason }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 清理确认对话框 -->
    <el-dialog v-model="cleanVisible" title="清理日志" width="400px">
      <el-form label-width="80px">
        <el-form-item label="保留天数">
          <el-input-number
            v-model="cleanDays"
            :min="1"
            :max="365"
            placeholder="请输入保留天数"
          />
          <span class="clean-tip">将清理 {{ cleanDays }} 天之前的日志</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cleanVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmClean">确认清理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { actionLogApi } from '@/api'

const loading = ref(false)
const detailVisible = ref(false)
const cleanVisible = ref(false)
const currentLog = ref(null)
const actionTypes = ref([])
const dateRange = ref([])

const searchForm = reactive({
  username: '',
  actionType: '',
  loginStatus: null,
  startDate: '',
  endDate: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const cleanDays = ref(30)

const tableData = ref([])

const getMethodType = (method) => {
  const typeMap = {
    GET: '',
    POST: 'success',
    PUT: 'warning',
    DELETE: 'danger',
    PATCH: 'info'
  }
  return typeMap[method] || 'info'
}

const formatJson = (str) => {
  if (!str) return ''
  try {
    const obj = typeof str === 'string' ? JSON.parse(str) : str
    return JSON.stringify(obj, null, 2)
  } catch {
    return str
  }
}

const handleDateChange = (val) => {
  if (val && val.length === 2) {
    searchForm.startDate = val[0]
    searchForm.endDate = val[1]
  } else {
    searchForm.startDate = ''
    searchForm.endDate = ''
  }
}

// 获取日志列表
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      username: searchForm.username || undefined,
      actionType: searchForm.actionType || undefined,
      loginStatus: searchForm.loginStatus ?? undefined,
      startDate: searchForm.startDate || undefined,
      endDate: searchForm.endDate || undefined
    }
    const res = await actionLogApi.list(params)
    tableData.value = res.data.records || res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取日志列表失败', error)
  } finally {
    loading.value = false
  }
}

// 获取操作类型
const fetchActionTypes = async () => {
  try {
    const res = await actionLogApi.getTypes()
    actionTypes.value = res.data || []
  } catch (error) {
    console.error('获取操作类型失败', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

// 重置
const handleReset = () => {
  searchForm.username = ''
  searchForm.actionType = ''
  searchForm.loginStatus = null
  searchForm.startDate = ''
  searchForm.endDate = ''
  dateRange.value = []
  handleSearch()
}

// 分页大小改变
const handleSizeChange = () => {
  pagination.page = 1
  fetchData()
}

// 页码改变
const handleCurrentChange = () => {
  fetchData()
}

// 查看详情
const handleViewDetail = async (row) => {
  try {
    const res = await actionLogApi.getById(row.id)
    currentLog.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error('获取日志详情失败', error)
  }
}

// 清理日志
const handleClean = () => {
  cleanVisible.value = true
}

// 确认清理
const confirmClean = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要清理 ${cleanDays.value} 天之前的所有日志吗？此操作不可恢复！`,
      '警告',
      { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    )
    const res = await actionLogApi.clean(cleanDays.value)
    ElMessage.success(res.data || '清理成功')
    cleanVisible.value = false
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理失败', error)
    }
  }
}

onMounted(() => {
  fetchData()
  fetchActionTypes()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

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
  justify-content: space-between;
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.success-text {
  color: $ink-green;
  font-weight: 500;
}

.error-text {
  color: $ink-red;
  font-weight: 500;
}

.url-text {
  font-family: 'Consolas', monospace;
  font-size: 12px;
  color: $ink-blue;
}

.json-content {
  max-height: 200px;
  overflow: auto;
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 8px 12px;
  margin: 0;

  pre {
    margin: 0;
    font-size: 12px;
    color: #333;
    white-space: pre-wrap;
    word-break: break-all;
  }
}

.error-content {
  background-color: #fef0f0;
  border: 1px solid #fde2e2;

  pre {
    color: $ink-red;
  }
}

.clean-tip {
  margin-left: 12px;
  color: #999;
  font-size: 12px;
}

:deep(.el-descriptions__label) {
  width: 120px;
  background-color: #fafafa;
}
</style>

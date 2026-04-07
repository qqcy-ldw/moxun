<template>
  <div class="page-container">
    <!-- 表格区域 -->
    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">角色列表</h3>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增角色
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column label="角色说明" min-width="200">
          <template #default="{ row }">
            {{ row.roleDesc || '—' }}
          </template>
        </el-table-column>
        <el-table-column prop="roleName" label="权限标识" min-width="160" />
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
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
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
        <el-form-item label="权限标识" prop="roleName">
          <el-input
            v-model="form.roleName"
            placeholder="如 ROLE_ADMIN"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item label="角色说明" prop="roleDesc">
          <el-input v-model="form.roleDesc" type="textarea" :rows="3" placeholder="角色说明" />
        </el-form-item>
        <el-form-item label="菜单权限" prop="menuIds">
          <el-tree
            ref="menuTreeRef"
            :data="menuTreeData"
            :props="{ label: 'menuName', children: 'children' }"
            node-key="id"
            :default-expand-all="true"
            show-checkbox
            check-strictly
          />
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
import { roleApi, menuApi } from '@/api'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const formRef = ref(null)
const menuTreeRef = ref(null)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

/** 接口返回的完整列表（后端未分页时用于前端切片） */
const fullRoleList = ref([])
/** 为 true 时表示 data 为数组，分页仅前端切片，翻页不重复请求 */
const useClientPagination = ref(false)

const tableData = ref([])
const menuTreeData = ref([])

const form = reactive({
  id: null,
  roleName: '',
  roleDesc: '',
  menuIds: []
})

const rules = {
  roleName: [{ required: true, message: '请输入权限标识', trigger: 'blur' }]
}

function normalizeListPayload(data) {
  if (Array.isArray(data)) {
    return { list: data, total: data.length, rawIsArray: true }
  }
  const list = data?.records ?? data?.list ?? []
  const total = data?.total ?? list.length
  return { list, total, rawIsArray: false }
}

function applyTablePage() {
  const list = fullRoleList.value
  const start = (pagination.page - 1) * pagination.pageSize
  tableData.value = list.slice(start, start + pagination.pageSize)
}

// 获取角色列表
const fetchRoles = async () => {
  loading.value = true
  try {
    const res = await roleApi.list({
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    const { list, total, rawIsArray } = normalizeListPayload(res.data)
    fullRoleList.value = list
    pagination.total = total
    useClientPagination.value = rawIsArray
    if (rawIsArray) {
      applyTablePage()
    } else {
      tableData.value = list
    }
  } catch (error) {
    console.error('获取角色列表失败', error)
  } finally {
    loading.value = false
  }
}

// 获取菜单树
const fetchMenuTree = async () => {
  try {
    const res = await menuApi.tree()
    menuTreeData.value = res.data || []
  } catch (error) {
    console.error('获取菜单树失败', error)
  }
}

// 新增
const handleAdd = () => {
  form.id = null
  form.roleName = ''
  form.roleDesc = ''
  form.menuIds = []
  dialogTitle.value = '新增角色'
  dialogVisible.value = true

  setTimeout(() => {
    if (menuTreeRef.value) {
      menuTreeRef.value.setCheckedKeys([])
    }
  }, 100)
}

// 编辑
const handleEdit = async (row) => {
  try {
    const res = await roleApi.getById(row.id)
    const roleData = res.data
    form.id = roleData.id
    form.roleName = roleData.roleName
    form.roleDesc = roleData.roleDesc || ''
    form.menuIds = roleData.menuIds || []
    dialogTitle.value = '编辑角色'
    dialogVisible.value = true

    setTimeout(() => {
      if (menuTreeRef.value) {
        menuTreeRef.value.setCheckedKeys(form.menuIds)
      }
    }, 100)
  } catch (error) {
    console.error('获取角色详情失败', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    const checkedKeys = menuTreeRef.value?.getCheckedKeys() || []
    const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() || []
    const menuIds = [...checkedKeys, ...halfCheckedKeys]

    try {
      const data = {
        roleName: form.roleName,
        roleDesc: form.roleDesc,
        menuIds
      }

      if (form.id) {
        await roleApi.update(form.id, data)
        ElMessage.success('编辑成功')
      } else {
        await roleApi.add(data)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchRoles()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await roleApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchRoles()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

// 分页
const handleSizeChange = () => {
  pagination.page = 1
  if (useClientPagination.value) {
    applyTablePage()
    return
  }
  fetchRoles()
}

const handleCurrentChange = () => {
  if (useClientPagination.value) {
    applyTablePage()
    return
  }
  fetchRoles()
}

onMounted(() => {
  fetchRoles()
  fetchMenuTree()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

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

:deep(.el-tree) {
  background-color: transparent;

  .el-tree-node__content {
    border-radius: $radius-sm;

    &:hover {
      background-color: rgba($ink-blue, 0.05);
    }
  }

  .el-tree-node.is-checked > .el-tree-node__content {
    background-color: rgba($ink-blue, 0.1);
  }
}
</style>

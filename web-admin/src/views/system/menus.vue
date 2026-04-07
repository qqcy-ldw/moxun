<template>
  <div class="page-container">
    <!-- 表格区域 -->
    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">菜单列表</h3>
        <el-button type="primary" @click="handleAdd(null)">
          <el-icon><Plus /></el-icon>
          新增菜单
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="menuName" label="菜单名称" min-width="180" />
        <el-table-column prop="icon" label="图标" width="100" align="center">
          <template #default="{ row }">
            <el-icon size="20"><component :is="row.icon" v-if="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column prop="perms" label="权限标识" min-width="150" />
        <el-table-column prop="path" label="路由地址" min-width="150" />
        <el-table-column prop="menuType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getMenuTypeTag(row.menuType)">
              {{ getMenuTypeName(row.menuType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'danger'">
              {{ row.status === '1' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" text @click="handleAdd(row)">新增</el-button>
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
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio label="M">目录</el-radio>
            <el-radio label="C">菜单</el-radio>
            <el-radio label="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTreeData"
            :props="{ label: 'menuName', children: 'children', value: 'id' }"
            check-strictly
            placeholder="请选择上级菜单"
            clearable
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="form.menuType !== 'F'">
          <el-input v-model="form.icon" placeholder="请输入图标名称">
            <template #prefix>
              <el-icon><component :is="form.icon" v-if="form.icon" /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="路由地址" prop="path" v-if="form.menuType !== 'F'">
          <el-input v-model="form.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="权限标识" prop="perms">
          <el-input v-model="form.perms" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">正常</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
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
import { menuApi } from '@/api'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const formRef = ref(null)

const tableData = ref([])
const menuTreeData = ref([])

const form = reactive({
  id: null,
  menuType: 'C',
  parentId: 0,
  menuName: '',
  icon: '',
  orderNum: 0,
  path: '',
  perms: '',
  status: '1'
})

const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  orderNum: [{ required: true, message: '请输入排序', trigger: 'blur' }]
}

const getMenuTypeTag = (type) => {
  const typeMap = { 'M': '', 'C': 'success', 'F': 'warning' }
  return typeMap[type] || ''
}

const getMenuTypeName = (type) => {
  const nameMap = { 'M': '目录', 'C': '菜单', 'F': '按钮' }
  return nameMap[type] || type
}

// 获取菜单列表
const fetchMenus = async () => {
  loading.value = true
  try {
    const res = await menuApi.list()
    // 👑 面试考点：后端分页格式 - 数据在 res.data.records 中，不在 res.data
    // res.data = { total: 17, records: [...] }
    const records = res.data?.records || []
    // 👑 面试考点：字段映射 - 后端字段名与前端模板不一致，需手动对齐
    // 后端: permission → 前端期望: perms
    // 后端: sort → 前端期望: orderNum
    // 后端: menuType "DIR"/"MENU"/"BUTTON" → 前端期望: "M"/"C"/"F"
    // 后端: status 数字 1/0 → 前端期望: 字符串 "1"/"0"
    tableData.value = records.map((item) => ({
      ...item,
      perms: item.permission,
      orderNum: item.sort,
      status: String(item.status)
    }))
    menuTreeData.value = [{ id: 0, menuName: '主目录', children: tableData.value }]
  } catch (error) {
    console.error('获取菜单列表失败', error)
    tableData.value = []
    menuTreeData.value = [{ id: 0, menuName: '主目录', children: [] }]
  } finally {
    loading.value = false
  }
}

// 新增
const handleAdd = (parent) => {
  form.id = null
  form.menuType = 'C'
  form.parentId = parent?.id || 0
  form.menuName = ''
  form.icon = ''
  form.orderNum = 0
  form.path = ''
  form.perms = ''
  form.status = '1'
  dialogTitle.value = '新增菜单'
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  try {
    const res = await menuApi.getById(row.id)
    const menuData = res.data
    form.id = menuData.id
    form.menuType = menuData.menuType
    form.parentId = menuData.parentId
    form.menuName = menuData.menuName
    form.icon = menuData.icon || ''
    form.orderNum = menuData.orderNum
    form.path = menuData.path || ''
    form.perms = menuData.perms || ''
    form.status = menuData.status
    dialogTitle.value = '编辑菜单'
    dialogVisible.value = true
  } catch (error) {
    console.error('获取菜单详情失败', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      // 👑 面试考点：提交时反向映射字段
      // 前端字段 perms/sort → 后端字段 permission/sort
      const payload = {
        ...form,
        permission: form.perms,
        sort: form.orderNum
      }
      // 👑 面试考点：delete 移除前端多出的字段，避免后端收到未知字段报错
      delete payload.perms
      delete payload.orderNum
      if (form.id) {
        await menuApi.update(form.id, payload)
        ElMessage.success('编辑成功')
      } else {
        await menuApi.add(payload)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchMenus()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

// 删除
const handleDelete = async (row) => {
  if (row.children?.length > 0) {
    ElMessage.warning('请先删除子菜单')
    return
  }
  try {
    await ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await menuApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchMenus()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

onMounted(() => {
  fetchMenus()
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
}

:deep(.el-table) {
  .el-table__expand-icon {
    color: $ink-gray-light;
  }
}

.action-group {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}
</style>

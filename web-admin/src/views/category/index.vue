<template>
  <div class="page-container">
    <div class="table-area ink-card">
      <div class="table-header">
        <h3 class="ink-title">课程分类列表</h3>
        <el-button type="primary" @click="handleAdd(null)">
          <el-icon><Plus /></el-icon>
          新增分类
        </el-button>
      </div>

      <!-- 👑 面试考点：el-table + tree-props 由表格统一处理缩进与展开，避免嵌套 el-table 列宽错位 -->
      <el-table
        :data="categoryList"
        v-loading="loading"
        stripe
        row-key="id"
        :tree-props="{ children: 'children' }"
        class="category-tree-table"
        :default-expand-all="false"
        table-layout="fixed"
        style="width: 100%"
        :indent="30"
        :row-class-name="tableRowClassName"
      >
        <!-- 首列不设 width，仅用 min-width，在 fixed 布局下吃掉剩余宽度，避免右侧留白 -->
        <el-table-column prop="name" label="分类名称" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span class="category-name-text">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="ID" width="72" align="center" />
        <el-table-column label="图标" width="84" align="center">
          <template #default="{ row }">
            <img
              v-if="row.icon && isImagePath(row.icon)"
              :src="resolveIconUrl(row.icon)"
              class="category-icon-img"
              alt=""
              @error="onImgError"
            />
            <el-icon v-else-if="row.icon" :size="20">
              <component :is="row.icon" />
            </el-icon>
            <span v-else class="no-icon">—</span>
          </template>
        </el-table-column>
        <el-table-column label="父分类" width="112" align="center" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="isTopLevel(row)" class="top-category">顶级</span>
            <span v-else class="parent-name">{{ getParentLabel(row.parentId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="68" align="center" />
        <el-table-column label="子分类数" width="88" align="center">
          <template #default="{ row }">
            <el-tag type="info" effect="plain">{{ row.children?.length ?? 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <div class="action-group">
              <el-button type="primary" link @click="handleAdd(row)">新增子分类</el-button>
              <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="父分类" prop="parentId">
          <el-select v-model="form.parentId" placeholder="顶级分类" clearable style="width: 100%;">
            <el-option label="顶级分类" :value="0" />
            <el-option
              v-for="c in flattenOptions"
              :key="c.id"
              :label="c.label"
              :value="Number(c.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类图标">
          <el-input v-model="form.icon" placeholder="图标路径或 Element Plus 图标名" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { categoryApi } from '@/api'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const formRef = ref(null)

const categoryList = ref([])

const form = reactive({
  id: null,
  parentId: 0,
  name: '',
  icon: '',
  sort: 0
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const isImagePath = (val) => /\.(png|jpg|jpeg|gif|svg|webp|ico)$/i.test(val)

// 顶级：null / 0 / 空串（与后端约定一致）
const isTopLevel = (row) => {
  const p = row.parentId
  return p == null || p === '' || Number(p) === 0
}

const topCategories = computed(() => categoryList.value)

/** 将嵌套树拍平，用于下拉选项，支持任意层级缩进 */
const flattenOptions = (() => {
  const indent = (depth) => (depth > 0 ? '　'.repeat(depth) + '├─ ' : '')
  const flat = []
  const walk = (list, depth = 0) => {
    list.forEach((node) => {
      flat.push({ id: node.id, label: indent(depth) + node.name, depth })
      if (node.children?.length) walk(node.children, depth + 1)
    })
  }
  return computed(() => {
    flat.length = 0
    walk(categoryList.value)
    return flat
  })
})()

/** 在整棵树中按 id 查找节点（用于解析父级名称） */
const findNodeById = (id, list = categoryList.value) => {
  if (id == null || list == null) return null
  const nid = Number(id)
  for (const node of list) {
    if (Number(node.id) === nid) return node
    if (node.children?.length) {
      const found = findNodeById(id, node.children)
      if (found) return found
    }
  }
  return null
}

/** 树深度：顶级=0，每向下一层 +1（用于行样式） */
const getTreeDepth = (row) => {
  let d = 0
  let cur = row
  while (cur && !isTopLevel(cur)) {
    d++
    cur = findNodeById(cur.parentId)
    if (!cur) break
  }
  return d
}

const tableRowClassName = ({ row }) => {
  const depth = Math.min(getTreeDepth(row), 4)
  return `category-tree-row depth-${depth}`
}

const getParentLabel = (parentId) => {
  const node = findNodeById(parentId)
  return node ? node.name : `分类${parentId}`
}

/** 静态资源：相对路径走当前站点根（开发环境可配 Vite 代理 /icons → 后端） */
const resolveIconUrl = (icon) => {
  if (!icon || !isImagePath(icon)) return ''
  if (/^https?:\/\//i.test(icon)) return icon
  if (icon.startsWith('/')) return icon
  return `/${icon.replace(/^\//, '')}`
}

const onImgError = (e) => {
  e.target.style.display = 'none'
}

const fetchCategories = async () => {
  loading.value = true
  try {
    const res = await categoryApi.list({ page: 1, pageSize: 1000 })
    categoryList.value = Array.isArray(res?.data) ? res.data : []
  } catch (error) {
    console.error('获取分类列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = (parent) => {
  form.id = null
  form.parentId = parent ? Number(parent.id) : 0
  form.name = ''
  form.icon = ''
  form.sort = 0
  dialogTitle.value = parent ? `新增「${parent.name}」子分类` : '新增分类'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.id = Number(row.id)
  // 编辑时：parentId 为 null → 0（对应"顶级分类"），非 null → Number
  form.parentId = isTopLevel(row) ? 0 : Number(row.parentId)
  form.name = row.name
  form.icon = row.icon || ''
  form.sort = row.sort ?? 0
  dialogTitle.value = `编辑分类「${row.name}」`
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const payload = {
        name: form.name,
        icon: form.icon || null,
        // 提交时：0 → null（后端约定顶级用 null）
        parentId: form.parentId === 0 ? null : form.parentId,
        sort: form.sort
      }
      if (form.id) {
        payload.id = form.id
        await categoryApi.update(payload)
        ElMessage.success('编辑成功')
      } else {
        await categoryApi.add(payload)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchCategories()
    } catch (error) {
      console.error('提交失败', error)
    }
  })
}

const handleDelete = async (row) => {
  if ((row.children?.length ?? 0) > 0) {
    ElMessage.warning('该分类下有子分类，请先删除子分类')
    return
  }
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await categoryApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchCategories()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.page-container {
  font-size: 15px;
  width: 100%;
  min-width: 0;
  box-sizing: border-box;
}

.table-area {
  padding: 20px 24px;
  width: 100%;
  box-sizing: border-box;
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
  flex-wrap: wrap;
  gap: 4px;
}

.category-name-text {
  font-weight: 500;
  color: $ink-gray;
}

.parent-name {
  color: $ink-gray-light;
  font-size: 14px;
}

.top-category {
  font-weight: 600;
  color: $ink-blue;
}

.no-icon {
  color: $ink-gray-lighter;
}

.category-icon-img {
  width: 24px;
  height: 24px;
  object-fit: contain;
  border-radius: $radius-sm;
  vertical-align: middle;
}

/* 树形表格：加宽每层缩进、层级色条、展开图标略放大；表头表体拉满容器，消除右侧空白 */
.category-tree-table {
  width: 100%;

  :deep(.el-table__inner-wrapper) {
    width: 100%;
  }

  :deep(.el-table__header),
  :deep(.el-table__body) {
    width: 100% !important;
  }

  :deep(.el-table__placeholder) {
    width: 14px;
  }

  :deep(.el-table__expand-icon) {
    font-size: 14px;
    margin-right: 6px;
  }

  :deep(.depth-0 .category-name-text) {
    font-weight: 600;
    font-size: 15px;
    color: $ink-gray;
  }

  :deep(.depth-1 .category-name-text) {
    font-weight: 500;
    font-size: 14px;
    color: $ink-blue;
  }

  :deep(.depth-2 .category-name-text),
  :deep(.depth-3 .category-name-text),
  :deep(.depth-4 .category-name-text) {
    font-weight: 400;
    font-size: 14px;
    color: $ink-gray-light;
  }

  /* 首列左侧色条：层级越深颜色越浅，形成“树轨” */
  :deep(.depth-1 td:first-child) {
    box-shadow: inset 4px 0 0 rgba($ink-blue, 0.55);
  }
  :deep(.depth-2 td:first-child) {
    box-shadow: inset 4px 0 0 rgba($ink-blue, 0.38);
  }
  :deep(.depth-3 td:first-child) {
    box-shadow: inset 4px 0 0 rgba($ink-blue, 0.26);
  }
  :deep(.depth-4 td:first-child) {
    box-shadow: inset 4px 0 0 rgba($ink-blue, 0.18);
  }
}
</style>

<template>
  <div class="page-container profile-page">
    <div class="ink-card profile-card">
      <h3 class="ink-title">个人中心</h3>
      <p class="profile-tip">查看并维护当前账号资料</p>

      <el-form
        ref="formRef"
        v-loading="loading"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="profile-form"
      >
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="用户ID">
          <el-input :model-value="String(form.id ?? '')" disabled />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="登录用户名" clearable />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" clearable />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱" clearable />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" clearable />
        </el-form-item>

        <el-divider content-position="left">账号信息</el-divider>
        <el-form-item label="账号状态">
          <el-tag :type="form.status === 1 ? 'success' : 'info'">
            {{ statusText }}
          </el-tag>
        </el-form-item>
        <el-form-item label="最后登录IP">
          <span class="muted">{{ form.lastLoginIp || '—' }}</span>
        </el-form-item>
        <el-form-item label="最后登录">
          <span class="muted">{{ formatTime(form.lastLoginTime) }}</span>
        </el-form-item>
        <el-form-item label="注册时间">
          <span class="muted">{{ formatTime(form.createTime) }}</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const saving = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  username: '',
  realName: '',
  email: '',
  phone: '',
  status: null,
  lastLoginIp: '',
  lastLoginTime: null,
  createTime: null
})

const rules = {
  username: [
    {
      validator: (_rule, value, callback) => {
        if (value == null || String(value).trim() === '') {
          callback()
          return
        }
        const len = String(value).length
        if (len < 3 || len > 50) {
          callback(new Error('用户名长度为 3～50 个字符'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}

const statusText = computed(() => {
  if (form.status === 1) return '正常'
  if (form.status === 0) return '禁用'
  if (form.status === 2) return '锁定'
  return '—'
})

function formatTime(val) {
  if (val == null || val === '') return '—'
  const s = typeof val === 'string' ? val.replace('T', ' ') : String(val)
  return s.length > 19 ? s.slice(0, 19) : s
}

function applyProfile(data) {
  if (!data) return
  form.id = data.id
  form.username = data.username ?? ''
  form.realName = data.realName ?? ''
  form.email = data.email ?? ''
  form.phone = data.phone ?? ''
  form.status = data.status
  form.lastLoginIp = data.lastLoginIp ?? ''
  form.lastLoginTime = data.lastLoginTime
  form.createTime = data.createTime
}

async function loadProfile() {
  const userId = localStorage.getItem('userId')
  if (!userId) {
    ElMessage.warning('未找到登录信息，请重新登录')
    return
  }
  loading.value = true
  try {
    const data = await userStore.getUserInfo(userId)
    applyProfile(data)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  if (!formRef.value) return
  const userId = localStorage.getItem('userId')
  if (!userId) {
    ElMessage.warning('未找到登录信息')
    return
  }
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      await authApi.updateProfile(Number(userId), {
        username: form.username || undefined,
        realName: form.realName || undefined,
        email: form.email || undefined,
        phone: form.phone || undefined
      })
      ElMessage.success('保存成功')
      await userStore.getUserInfo(userId)
      applyProfile(userStore.userInfo)
    } catch (e) {
      console.error(e)
    } finally {
      saving.value = false
    }
  })
}

onMounted(() => {
  if (userStore.userInfo) {
    applyProfile(userStore.userInfo)
  }
  loadProfile()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.profile-page {
  max-width: 720px;
}

.profile-card {
  padding: 20px 24px;
}

.profile-tip {
  margin: 0 0 20px;
  font-size: 13px;
  color: $ink-gray;
}

.profile-form {
  max-width: 520px;
}

.muted {
  color: $ink-gray;
  font-size: 14px;
}

:deep(.el-divider__text) {
  font-weight: 500;
  color: $ink-blue;
}
</style>

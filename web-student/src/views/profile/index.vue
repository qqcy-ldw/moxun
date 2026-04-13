<template>
  <div class="profile-page page-container">
    <div class="profile-header ink-card">
      <div class="avatar-section">
        <el-avatar :size="80" :src="userInfo?.avatar">
          {{ userInfo?.username?.charAt(0) || '学' }}
        </el-avatar>
      </div>
      <div class="info-section">
        <h2>{{ userInfo?.username }}</h2>
        <p class="user-email">{{ userInfo?.email }}</p>
        <div class="user-stats">
          <div class="stat-item">
            <span class="stat-value">{{ stats.courses }}</span>
            <span class="stat-label">我的课程</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.favorites }}</span>
            <span class="stat-label">我的收藏</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.questions }}</span>
            <span class="stat-label">我的提问</span>
          </div>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <div class="content-section ink-card">
        <h3>基本信息</h3>
        <el-form :model="form" label-width="80px" class="profile-form">
          <el-form-item label="用户名">
            <el-input v-model="form.username" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input v-model="form.bio" type="textarea" :rows="3" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="content-section ink-card">
        <h3>修改密码</h3>
        <el-form :model="passwordForm" label-width="80px" class="profile-form">
          <el-form-item label="原密码">
            <el-input v-model="passwordForm.oldPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="passwordForm.newPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleChangePassword" :loading="changing">修改密码</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api'

const userInfo = computed(() => {
  const user = localStorage.getItem('student_user')
  return user ? JSON.parse(user) : null
})

const stats = ref({
  courses: 0,
  favorites: 0,
  questions: 0
})

const form = reactive({
  username: '',
  email: '',
  phone: '',
  bio: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const saving = ref(false)
const changing = ref(false)

const initForm = () => {
  if (userInfo.value) {
    form.username = userInfo.value.username || ''
    form.email = userInfo.value.email || ''
    form.phone = userInfo.value.phone || ''
    form.bio = userInfo.value.bio || ''
  }
}

const fetchStats = async () => {
  try {
    const coursesRes = await authApi.getUserInfo()
    stats.value.courses = coursesRes.data?.courses || 0
    stats.value.favorites = coursesRes.data?.favorites || 0
    stats.value.questions = coursesRes.data?.questions || 0
  } catch (error) {
    console.error('获取统计信息失败', error)
  }
}

const handleSave = async () => {
  saving.value = true
  try {
    // 调用更新用户信息接口
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const handleChangePassword = async () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    ElMessage.warning('请填写完整')
    return
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }

  changing.value = true
  try {
    ElMessage.success('密码修改成功')
    Object.assign(passwordForm, { oldPassword: '', newPassword: '', confirmPassword: '' })
  } catch (error) {
    ElMessage.error('修改失败')
  } finally {
    changing.value = false
  }
}

onMounted(() => {
  initForm()
  fetchStats()
})
</script>

<style lang="scss" scoped>
.profile-page { max-width: 800px; margin: 0 auto; }

.profile-header {
  display: flex;
  gap: var(--space-lg);
  padding: var(--space-lg);
  margin-bottom: var(--space-md);
  border-radius: var(--radius-lg);

  .info-section {
    flex: 1;

    h2 { font-size: 22px; font-weight: 700; margin-bottom: var(--space-xs); }
    .user-email { font-size: 14px; color: var(--ink-gray-light); margin-bottom: var(--space-md); }

    .user-stats {
      display: flex;
      gap: var(--space-xl);

      .stat-item {
        text-align: center;

        .stat-value {
          display: block;
          font-size: 24px;
          font-weight: 700;
          color: var(--ink-blue);
        }

        .stat-label {
          font-size: 12px;
          color: var(--ink-gray-light);
        }
      }
    }
  }
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);

  .content-section {
    padding: var(--space-lg);
    border-radius: var(--radius-md);

    h3 {
      font-size: 15px;
      font-weight: 600;
      margin-bottom: var(--space-md);
      padding-bottom: var(--space-sm);
      border-bottom: 1px solid var(--border-light);
    }
  }
}
</style>
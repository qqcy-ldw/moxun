<template>
  <div class="login-page">
    <div class="login-card ink-card">
      <div class="login-header">
        <h1>知界教育</h1>
        <p>学生端登录</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            size="large"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <div class="captcha-row">
            <el-input
              v-model="form.captcha"
              placeholder="请输入验证码"
              size="large"
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><Key /></el-icon>
              </template>
            </el-input>
            <div class="captcha-img" @click="refreshCaptcha">
              <img v-if="captchaUrl" :src="captchaUrl" alt="验证码" />
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            native-type="submit"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import { authApi } from '@/api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const captchaUrl = ref('')

const form = reactive({
  username: '',
  password: '',
  captcha: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 刷新验证码
const refreshCaptcha = () => {
  captchaUrl.value = `/auth/api/captcha?t=${Date.now()}`
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const res = await authApi.login({
        username: form.username,
        password: form.password,
        captcha: form.captcha
      })

      // 保存 token
      if (res.data?.token) {
        localStorage.setItem('student_token', res.data.token)
      }
      if (res.data?.user) {
        localStorage.setItem('student_user', JSON.stringify(res.data.user))
      }

      ElMessage.success('登录成功')
      router.push('/home')
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
      refreshCaptcha()
      form.captcha = ''
    } finally {
      loading.value = false
    }
  })
}

// 初始化验证码
refreshCaptcha()
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 420px;
  padding: 40px;
  border-radius: 12px;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;

  h1 {
    font-size: 28px;
    font-weight: 700;
    color: var(--ink-blue);
    margin-bottom: 8px;
  }

  p {
    color: var(--ink-gray-light);
    font-size: 14px;
  }
}

.login-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }
}

.captcha-row {
  display: flex;
  gap: 12px;

  :deep(.el-input) {
    flex: 1;
  }
}

.captcha-img {
  width: 110px;
  height: 40px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  background: #f5f3ee;
  flex-shrink: 0;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
}
</style>

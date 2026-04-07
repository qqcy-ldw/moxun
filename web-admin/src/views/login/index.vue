<template>
  <div class="login-page">
    <!-- 左侧：深蓝渐变区域 -->
    <section class="login-hero">
      <div class="hero-bg" />

      <div class="hero-content">
        <h1 class="hero-title">知界</h1>
        <p class="hero-subtitle">知行合一 · 境界无限</p>
      </div>

      <div class="hero-bottom">
        <p class="hero-quote">知识界定你所处世界的范围</p>
      </div>
    </section>

    <!-- 右侧：登录表单区 -->
    <section class="login-panel">
      <div class="login-card">
        <header class="card-header">
          <h2 class="card-title">管理员登录</h2>
          <p class="card-desc">欢迎回来，请输入账号信息</p>
        </header>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          class="login-form"
          label-position="top"
          @submit.prevent
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              size="large"
              clearable
              class="form-input"
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon class="input-icon"><User /></el-icon>
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
              class="form-input"
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon class="input-icon"><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="captcha">
            <div class="captcha-row">
              <el-input
                v-model="form.captcha"
                placeholder="请输入验证码"
                size="large"
                clearable
                class="captcha-input"
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Key /></el-icon>
                </template>
              </el-input>
              <div class="captcha-img-wrap" @click="refreshCaptcha">
                <img :src="captchaUrl" alt="验证码" class="captcha-img" />
              </div>
            </div>
          </el-form-item>

          <el-form-item class="form-actions">
            <el-button
              type="primary"
              size="large"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
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

const refreshCaptcha = () => {
  captchaUrl.value = `/auth/api/captcha?t=${Date.now()}`
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await request.post('/auth/api/login', form)
      userStore.setToken(res.data.token)
      localStorage.setItem('userId', res.data.userId)
      ElMessage.success('登录成功')
      router.push('/')
    } catch {
      refreshCaptcha()
      form.captcha = ''
    } finally {
      loading.value = false
    }
  })
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.login-page {
  display: flex;
  min-height: 100vh;
  width: 100%;
}

// ============================================================
// 左侧：深蓝渐变
// ============================================================
.login-hero {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 60px 48px;
  background: linear-gradient(
    160deg,
    #0d2137 0%,
    #1b3f60 35%,
    #256080 60%,
    #2e7a9a 100%
  );
  overflow: hidden;

  // 底部微光
  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 40%;
    background: linear-gradient(0deg, rgba(10, 25, 42, 0.4) 0%, transparent 100%);
    pointer-events: none;
  }
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse 60% 50% at 80% 50%, rgba(60, 140, 180, 0.15) 0%, transparent 70%);
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: #fff;
}

.hero-title {
  margin: 0;
  font-size: clamp(64px, 8vw, 96px);
  font-weight: 400;
  letter-spacing: 0.5em;
  text-indent: 0.5em;
  font-family: 'STKaiti', 'KaiTi', 'Songti SC', 'Noto Serif SC', serif;
  line-height: 1;
  text-shadow: 0 4px 32px rgba(0, 0, 0, 0.35);
}

.hero-subtitle {
  margin: 20px 0 0;
  font-size: clamp(13px, 1.4vw, 16px);
  letter-spacing: 0.35em;
  opacity: 0.7;
  font-weight: 300;
  color: rgba(200, 230, 248, 0.9);
}

.hero-bottom {
  position: relative;
  z-index: 1;
  margin-top: 48px;
}

.hero-quote {
  margin: 0;
  font-size: 13px;
  letter-spacing: 0.25em;
  color: rgba(255, 255, 255, 0.4);
}

// ============================================================
// 右侧：白色表单区
// ============================================================
.login-panel {
  flex: 0 0 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px 40px;
  background: $ink-white;
}

.login-card {
  width: 100%;
  max-width: 380px;
}

.card-header {
  margin-bottom: 36px;
}

.card-title {
  margin: 0 0 8px;
  font-size: 22px;
  font-weight: 600;
  color: $ink-gray;
  letter-spacing: 0.06em;
}

.card-desc {
  margin: 0;
  font-size: 13px;
  color: $ink-gray-light;
}

// ============================================================
// 表单
// ============================================================
.login-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }
}

.form-input {
  :deep(.el-input__wrapper) {
    border-radius: 10px;
    padding: 4px 14px;
    background: #fff;
    box-shadow: 0 0 0 1px rgba($ink-blue, 0.12) inset;
    transition: box-shadow 0.2s ease;
  }

  :deep(.el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px rgba($ink-blue, 0.28) inset;
  }

  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 2px rgba($ink-blue, 0.3) inset, 0 0 0 4px rgba($ink-blue, 0.08);
  }

  :deep(.el-input__inner) {
    font-size: 14px;
    color: $ink-gray;
  }

  :deep(.el-input__inner::placeholder) {
    color: $ink-gray-lighter;
  }
}

.input-icon {
  color: rgba($ink-blue, 0.5);
  font-size: 15px;
}

.captcha-row {
  display: flex;
  gap: 10px;
  align-items: stretch;
}

.captcha-input {
  flex: 1;
}

.captcha-img-wrap {
  flex-shrink: 0;
  width: 110px;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  background: #f5f3ee;
  box-shadow: 0 0 0 1px rgba($ink-blue, 0.1) inset;
  transition: box-shadow 0.2s ease;

  &:hover {
    box-shadow: 0 0 0 1px rgba($ink-blue, 0.22) inset;
  }
}

.captcha-img {
  display: block;
  width: 100%;
  height: 40px;
  object-fit: cover;
}

.form-actions {
  margin-top: 28px;
  margin-bottom: 0 !important;
}

.login-btn {
  width: 100%;
  height: 50px;
  border-radius: 10px;
  font-size: 15px;
  letter-spacing: 0.45em;
  text-indent: 0.45em;
  font-weight: 500;
  background: linear-gradient(135deg, #1b4f72 0%, $ink-blue 50%, #2a6f94 100%);
  border-color: rgba($ink-blue, 0.6);
  box-shadow: 0 6px 20px rgba($ink-blue, 0.25);
  transition: all 0.2s ease;

  &:hover {
    filter: brightness(1.08);
    box-shadow: 0 8px 28px rgba($ink-blue, 0.32);
    transform: translateY(-1px);
  }

  &:active {
    transform: translateY(0);
    filter: brightness(0.97);
    box-shadow: 0 4px 12px rgba($ink-blue, 0.2);
  }
}

// ============================================================
// 响应式
// ============================================================
@media (max-width: 860px) {
  .login-page {
    flex-direction: column;
  }

  .login-hero {
    flex: none;
    min-height: auto;
    padding: 40px 32px 32px;
  }

  .hero-bottom {
    margin-top: 24px;
  }

  .login-panel {
    flex: none;
    padding: 36px 24px 48px;
  }
}
</style>

<template>
  <div class="dashboard">
    <!-- 概览数据：渐变背景大数字卡片 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="8">
        <div class="stat-card stat-card--blue">
          <div class="stat-card__bg-icon">
            <el-icon size="80" color="rgba(255,255,255,0.08)"><Reading /></el-icon>
          </div>
          <div class="stat-card__icon">
            <el-icon size="22" color="rgba(255,255,255,0.9)"><Reading /></el-icon>
          </div>
          <div class="stat-card__value">{{ dashboardData.courseCount || 0 }}</div>
          <div class="stat-card__label">课程总数</div>
          <div class="stat-card__trend">
            <span class="stat-card__trend-dot stat-card__trend-dot--up" />
            较上月
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card--green">
          <div class="stat-card__bg-icon">
            <el-icon size="80" color="rgba(255,255,255,0.08)"><User /></el-icon>
          </div>
          <div class="stat-card__icon">
            <el-icon size="22" color="rgba(255,255,255,0.9)"><User /></el-icon>
          </div>
          <div class="stat-card__value">{{ dashboardData.userCount || 0 }}</div>
          <div class="stat-card__label">用户总数</div>
          <div class="stat-card__trend">
            <span class="stat-card__trend-dot stat-card__trend-dot--up" />
            较上月
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card--yellow">
          <div class="stat-card__bg-icon">
            <el-icon size="80" color="rgba(255,255,255,0.08)"><Document /></el-icon>
          </div>
          <div class="stat-card__icon">
            <el-icon size="22" color="rgba(255,255,255,0.9)"><Document /></el-icon>
          </div>
          <div class="stat-card__value">{{ dashboardData.homeworkCount || 0 }}</div>
          <div class="stat-card__label">作业总数</div>
          <div class="stat-card__trend">
            <span class="stat-card__trend-dot stat-card__trend-dot--up" />
            较上月
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 + 筛选栏 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="16">
        <div class="ink-card chart-card">
          <!-- 图表区标题栏：标题 + 筛选 -->
          <div class="chart-header">
            <h3 class="chart-title">数据趋势</h3>
            <div class="filter-bar">
              <el-date-picker
                v-model="filterDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始"
                end-placeholder="结束"
                value-format="YYYY-MM-DD"
                :clearable="false"
                size="small"
                class="filter-date-picker"
                @change="handleFilterChange"
              />
              <el-select v-model="filterTopN" size="small" class="filter-select" @change="handleFilterChange">
                <el-option :value="3" label="TOP 3" />
                <el-option :value="5" label="TOP 5" />
                <el-option :value="10" label="TOP 10" />
                <el-option :value="20" label="TOP 20" />
              </el-select>
              <el-button type="primary" size="small" :loading="loading" :icon="Search" @click="fetchAllData">
                查询
              </el-button>
              <el-button size="small" :icon="RefreshRight" @click="resetFilter">重置</el-button>
            </div>
          </div>
          <div class="chart-container" ref="trendChartRef"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="ink-card chart-card">
          <div class="chart-header">
            <h3 class="chart-title">数据分布</h3>
          </div>
          <div class="chart-container" ref="pieChartRef"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 排行榜 -->
    <div class="section-header">
      <span class="section-title">数据排行榜</span>
      <span class="section-sub">近 {{ dateRangeText }}</span>
    </div>
    <el-row :gutter="16" class="rank-section">
      <el-col v-for="section in rankSections" :key="section.key" :span="8" class="rank-col">
        <div class="ink-card rank-card">
          <div class="rank-card__header">
            <el-icon v-if="section.icon" size="16" :color="section.color">
              <component :is="section.icon" />
            </el-icon>
            <span class="rank-card__title">{{ section.title }}</span>
          </div>
          <el-empty
            v-if="!section.rows.length"
            description="暂无数据"
            :image-size="60"
            class="rank-empty"
          />
          <div v-else class="rank-list">
            <div
              v-for="(row, idx) in section.rows"
              :key="`${section.key}-${idx}`"
              class="rank-item"
            >
              <div class="rank-item__meta">
                <span
                  class="rank-item__badge"
                  :class="{
                    'rank-item__badge--gold': idx === 0,
                    'rank-item__badge--silver': idx === 1,
                    'rank-item__badge--bronze': idx === 2
                  }"
                >{{ idx + 1 }}</span>
                <span class="rank-item__name">{{ row.primary }}</span>
                <span class="rank-item__value">{{ row.secondary }}</span>
              </div>
              <div class="rank-item__bar-bg">
                <div
                  class="rank-item__bar-fill"
                  :style="{
                    width: section.maxValue > 0 ? `${(row.rawValue / section.maxValue) * 100}%` : '0%',
                    background: section.color
                  }"
                />
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import {
  Reading,
  User,
  Document,
  ChatDotRound,
  Clock,
  UserFilled,
  ChatLineRound,
  OfficeBuilding,
  Search,
  RefreshRight
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { statisticsApi } from '@/api'

// 👑 面试考点：el-date-picker v-model 绑定的是 [Date, Date]（JS Date 对象），后端接收 "yyyy-MM-dd" 字符串
// value-format="YYYY-MM-DD" 保证 format 后传给接口的格式与后端 @DateTimeFormat 一致
const today = new Date()
const sevenDaysAgo = new Date(today)
sevenDaysAgo.setDate(today.getDate() - 6)

const filterDateRange = ref([
  sevenDaysAgo.toISOString().slice(0, 10),
  today.toISOString().slice(0, 10)
])
const filterTopN = ref(5)
const loading = ref(false)

// 构造 POST body，与后端 StatisticsDto 对应
// 👑 面试考点：只有传了值才放入 body；后端 @RequestBody(required = false)
// 不传时后端自动计算默认值（近7天），避免传空字符串导致参数绑定失败
const buildFilterBody = () => {
  const [startDate, endDate] = filterDateRange.value || []
  return {
    ...(startDate && { startDate }),
    ...(endDate && { endDate }),
    ...(filterTopN.value && { topN: filterTopN.value })
  }
}

// 计算属性：当前筛选的日期范围文案，用于排行榜顶部显示
const dateRangeText = computed(() => {
  const [start, end] = filterDateRange.value || []
  return start && end ? `${start} 至 ${end}` : '近 7 天'
})

const trendChartRef = ref(null)
const pieChartRef = ref(null)
let trendChart = null
let pieChart = null

// 👑 面试考点：ref 和 reactive 的区别
// ref 用于基本类型和简单对象，reactive 用于复杂对象
// 概览数据 - 从 /admin/statistics/dashboard 获取
// 概览仅展示课程/用户/作业三项（不展示问答总数）
const dashboardData = ref({
  courseCount: 0,
  userCount: 0,
  homeworkCount: 0
})

// 👑 面试考点：后端 Result.data 为 HashMap<String, List<HashMap<String,Object>>>
// 每组排行是「行对象数组」，每行含 username/title、duration 或 count 等字段，不能用 Object.entries 当 Map 解析
const rankSections = ref([])

// 趋势图数据（ECharts）
const trendData = ref({
  dates: [],
  users: [],
  courses: [],
  assignments: []
})

// 初始化图表（首次渲染时使用空数据，等接口返回后通过 updateCharts 更新）
const initCharts = () => {
  // 👑 面试考点：ECharts.init() 创建图表实例，ref 模板引用获取 DOM 节点
  // ⚠️ 注意：必须在 DOM 渲染完成后再 init，因此放在 onMounted 中调用
  trendChart = echarts.init(trendChartRef.value)
  pieChart = echarts.init(pieChartRef.value)
  updateCharts()
}

// 👑 面试考点：ECharts setOption / mergeOption 的使用
// setOption 默认会替换全部配置，notMerge: false 则与旧配置合并
// 这里用 updateCharts 函数统一更新两张图，保证数据和配置同步
const updateCharts = () => {
  // 👑 面试考点：xAxis.type = 'category' 表示类目轴，数据为离散字符串
  // xAxis.type = 'time' 为时间轴，'value' 为数值轴
  trendChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['用户', '课程', '作业']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      // ⚠️ 注意：dates 为空时 ECharts 会自动显示暂无数据，不会报错
      data: trendData.value.dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '用户',
        type: 'line',
        smooth: true,
        data: trendData.value.users
      },
      {
        name: '课程',
        type: 'line',
        smooth: true,
        data: trendData.value.courses
      },
      {
        name: '作业',
        type: 'line',
        smooth: true,
        data: trendData.value.assignments
      }
    ]
  })

  // 👑 面试考点：饼图 / 环形图
  // radius: ['40%', '70%'] 构成环形，innerRadius > 0 时自动是环形图
  pieChart.setOption({
    tooltip: {
      trigger: 'item',
      // 👑 面试考点：模板字符串 + toLocaleString() 格式化数字
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        // ⚠️ 注意：直接从概览数据 dashboardData 取值，结构与环形图 data 格式一致
        data: [
          { value: dashboardData.value.userCount, name: '用户' },
          { value: dashboardData.value.courseCount, name: '课程' },
          { value: dashboardData.value.homeworkCount, name: '作业' }
        ]
      }
    ]
  })
}

// 响应窗口大小变化
const handleResize = () => {
  trendChart?.resize()
  pieChart?.resize()
}

// 👑 面试考点：async/await + Promise.all 的并发请求优化
// 并行请求多个无依赖的接口，避免串行等待，大幅提升页面加载速度
const fetchDashboardData = async () => {
  try {
    // 接口1：概览数据（课程/用户/作业）
    const overviewRes = await statisticsApi.dashboard()
    Object.assign(dashboardData.value, overviewRes.data)
  } catch (error) {
    console.error('获取概览数据失败', error)
  }
}

// ⚠️ 注意：接口已按 SQL order by 排序并 limit，前端不再排序，只负责展示字段
const num = (v) => {
  const n = Number(v)
  return Number.isFinite(n) ? n : 0
}

// 👑 面试考点：学习时长多为秒累计，格式化成可读文案，避免直接拼接 Object
const formatDurationSec = (sec) => {
  const s = num(sec)
  if (s >= 3600) {
    const h = Math.floor(s / 3600)
    const m = Math.floor((s % 3600) / 60)
    return m > 0 ? `${h} 小时 ${m} 分钟` : `${h} 小时`
  }
  if (s >= 60) return `${Math.floor(s / 60)} 分钟`
  return `${s} 秒`
}

const asList = (raw) => (Array.isArray(raw) ? raw : [])

// 👑 面试考点：进度条宽度 = 当前值 / maxValue * 100%，maxValue 即该组第一名数值
// color 用于进度条填充色 + 标题图标色，与各卡片主题呼应
const buildRankSections = (payload) => {
  const data = payload || {}
  const extractRows = (arr, durationKey) =>
    asList(arr).map((row) => ({
      primary: String(row.username ?? row.title ?? ''),
      secondary:
        durationKey
          ? `累计学习 ${formatDurationSec(row[durationKey])}`
          : `${num(row.count)} 次`,
      rawValue: num(durationKey ? row[durationKey] : row.count)
    }))

  const sections = [
    { key: 'userstudyRank', title: '学生学习时长', icon: Clock, color: '#2A5C7A', extract: () => extractRows(data.userstudyRank, 'duration') },
    { key: 'useractiveRank', title: '学生活跃（登录）', icon: UserFilled, color: '#5B8C5A', extract: () => extractRows(data.useractiveRank, null) },
    { key: 'useranswerRank', title: '学生回答次数', icon: ChatDotRound, color: '#7A8B99', extract: () => extractRows(data.useranswerRank, null) },
    { key: 'usercourseRank', title: '课程选课热度', icon: Reading, color: '#C9A96E', extract: () => extractRows(data.usercourseRank, null) },
    { key: 'userdiscussionRank', title: '讨论活跃（问答）', icon: ChatLineRound, color: '#B85C5C', extract: () => extractRows(data.userdiscussionRank, null) },
    { key: 'teacherRank', title: '教师课程热度', icon: OfficeBuilding, color: '#3A7A9A', extract: () => extractRows(data.teacherRank, null) }
  ]

  return sections.map((s) => {
    const rows = s.extract()
    const maxValue = rows.reduce((mx, r) => Math.max(mx, r.rawValue), 0)
    return { ...s, rows, maxValue }
  })
}

// 筛选条件变化时自动刷新（仅在日期/排名选择后立刻生效，或由查询按钮触发完整刷新）
const handleFilterChange = () => {
  fetchAllData()
}

const resetFilter = () => {
  filterDateRange.value = [
    new Date(Date.now() - 6 * 86400000).toISOString().slice(0, 10),
    new Date().toISOString().slice(0, 10)
  ]
  filterTopN.value = 5
  fetchAllData()
}

// 👑 面试考点：POST + body 参数，请求体与后端 StatisticsDto 一一对应
// startDate/endDate 为 "yyyy-MM-dd" 字符串，topN 为整数
const fetchRankData = async () => {
  const body = buildFilterBody()
  const res = await statisticsApi.rank(body)
  rankSections.value = buildRankSections(res.data)
}

const fetchTrendData = async () => {
  const body = buildFilterBody()
  const res = await statisticsApi.trend(body)
  const data = res.data || {}
  // 👑 面试考点：LocalDate 后端序列化后，前端拿到的是 ISO 格式字符串（如 "2026-03-15"）
  trendData.value = {
    dates: data.dates || [],
    users: data.users || [],
    courses: data.courses || [],
    assignments: data.assignments || []
  }
  // 数据到达后立即刷新图表
  if (trendChart && pieChart) {
    updateCharts()
  }
}

// 👑 面试考点：loading 状态控制并发请求，期间禁用按钮防止重复提交
// dashboard 接口无筛选参数，排行榜和趋势图都带 body 参数
const fetchAllData = async () => {
  loading.value = true
  try {
    await Promise.all([fetchDashboardData(), fetchRankData(), fetchTrendData()])
  } finally {
    loading.value = false
  }
}

// ⚠️ 注意：onMounted 中多个异步请求并行发起（Promise.all）
// 好处：利用浏览器并发下载能力，减少总等待时间
onMounted(async () => {
  await fetchAllData()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChart?.dispose()
  pieChart?.dispose()
})
</script>

<style lang="scss" scoped>
@use '@/style/variables.scss' as *;

.dashboard {
  padding: 0;
  font-size: 15px;  /* 全局基准字号上调 */
}

/* ── 概览卡片 ─────────────────────────────── */
.overview-cards {
  margin-bottom: 16px;
}

.stat-card {
  position: relative;
  overflow: hidden;
  padding: 28px 24px 24px;
  border-radius: $radius-lg;
  cursor: default;
  transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              box-shadow 0.25s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    transform: translateY(-3px);
    box-shadow: $shadow-md;
  }

  // 黛青 / 苔绿 / 枯黄 — 降饱和度，莫兰迪色系，与米白背景协调
  &--blue {
    background: linear-gradient(145deg, #4A7A94 0%, #6A9AB0 100%);
    color: #fff;
  }
  &--green {
    background: linear-gradient(145deg, #6A9A74 0%, #7EAA8A 100%);
    color: #fff;
  }
  &--yellow {
    background: linear-gradient(145deg, #C4A870 0%, #D4BC8A 100%);
    color: #fff;
  }

  // 大图标背景装饰
  &__bg-icon {
    position: absolute;
    right: -10px;
    bottom: -16px;
    opacity: 0.12;
    pointer-events: none;
    user-select: none;
  }

  // 左上角图标
  &__icon {
    width: 40px;
    height: 40px;
    border-radius: $radius-md;
    background: rgba(255, 255, 255, 0.15);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 12px;
  }

  // 大数字
  &__value {
    font-size: 48px;
    font-weight: 700;
    line-height: 1;
    letter-spacing: -1px;
    margin-bottom: 8px;
    color: #fff;
  }

  // 标签
  &__label {
    font-size: 16px;
    opacity: 0.85;
    margin-bottom: 8px;
  }

  // 底部趋势文字
  &__trend {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    opacity: 0.75;
  }

  &__trend-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    display: inline-block;

    &--up {
      background: #86efac;
    }
    &--down {
      background: #fca5a5;
    }
  }
}

/* ── 图表区 ───────────────────────────────── */
.chart-section {
  margin-bottom: 16px;
}

.chart-card {
  padding: 20px 24px 16px;
}

.chart-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  flex-wrap: wrap;
  gap: 8px;
}

.chart-title {
  font-size: 17px;
  font-weight: 600;
  color: $ink-gray;
  margin: 0;
}

// 筛选内嵌在图表标题栏
.filter-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-date-picker {
  width: 220px;
}

.filter-select {
  width: 100px;
}

.chart-container {
  width: 100%;
  height: 340px;
}

/* ── 排行榜标题栏 ─────────────────────────── */
.section-header {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-bottom: 12px;
  padding: 0 2px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: $ink-gray;
}

.section-sub {
  font-size: 13px;
  color: $ink-gray-lighter;
}

/* ── 排行榜卡片 ───────────────────────────── */
.rank-section {
  margin-bottom: 24px;
}

.rank-col {
  margin-bottom: 16px;
}

.rank-card {
  padding: 16px 20px;
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: $shadow-sm;
  }

  &__header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 14px;
    padding-bottom: 10px;
    border-bottom: 1px solid $border-light;
  }

  &__title {
    font-size: 15px;
    font-weight: 600;
    color: $ink-gray;
  }
}

.rank-empty {
  padding: 12px 0 8px;
}

/* ── 排行榜行 ─────────────────────────────── */
.rank-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rank-item {
  &__meta {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 6px;
  }

  &__badge {
    flex-shrink: 0;
    width: 22px;
    height: 22px;
    border-radius: 50%;
    font-size: 12px;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba($ink-blue, 0.1);
    color: $ink-blue;

    &--gold { background: #FEF3C7; color: #92400E; }
    &--silver { background: #F3F4F6; color: #374151; }
    &--bronze { background: #FEF2F2; color: #991B1B; }
  }

  &__name {
    flex: 1;
    font-size: 14px;
    color: $ink-gray;
    font-weight: 500;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__value {
    font-size: 13px;
    color: $ink-gray-lighter;
    white-space: nowrap;
  }

  &__bar-bg {
    height: 6px;
    border-radius: 3px;
    background: $border-light;
    overflow: hidden;
  }

  &__bar-fill {
    height: 100%;
    border-radius: 3px;
    transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
    opacity: 0.85;
  }
}
</style>

<template>
  <div class="dsp-dashboard" :class="{ 'dark-mode': isDark }">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-blue">
          <div class="stat-icon">
            <el-icon :size="40"><View /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatNumber(stats.impressions) }}</div>
            <div class="stat-label">广告展现</div>
            <div class="stat-change positive">
              <el-icon><Top /></el-icon>
              <span>+12.5%</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-green">
          <div class="stat-icon">
            <el-icon :size="40"><Pointer /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatNumber(stats.clicks) }}</div>
            <div class="stat-label">广告点击</div>
            <div class="stat-change positive">
              <el-icon><Top /></el-icon>
              <span>+8.3%</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-orange">
          <div class="stat-icon">
            <el-icon :size="40"><Coin /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ formatMoney(stats.revenue) }}</div>
            <div class="stat-label">预估收益</div>
            <div class="stat-change positive">
              <el-icon><Top /></el-icon>
              <span>+15.2%</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card stat-card-purple">
          <div class="stat-icon">
            <el-icon :size="40"><Wallet /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ formatMoney(stats.profit) }}</div>
            <div class="stat-label">利润</div>
            <div class="stat-change positive">
              <el-icon><Top /></el-icon>
              <span>+10.8%</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 趋势图 -->
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><TrendCharts /></el-icon>
                数据趋势分析
              </span>
              <el-radio-group v-model="trendPeriod" size="small" @change="updateTrendChart">
                <el-radio-button value="twelve">近12天</el-radio-button>
                <el-radio-button value="month">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 点击率饼图 -->
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><PieChart /></el-icon>
                广告位分布
              </span>
            </div>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <!-- 收益分析 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Histogram /></el-icon>
                收益分析
              </span>
            </div>
          </template>
          <div ref="revenueChartRef" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 实时数据 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><DataLine /></el-icon>
                实时数据监控
              </span>
              <div style="display: flex; gap: 8px; align-items: center;">
                <el-button type="primary" size="small" @click="showFillRateDetail">
                  填充率详细
                </el-button>
                <el-tag type="success" size="small">实时更新</el-tag>
              </div>
            </div>
          </template>
          <div ref="realtimeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细数据表格 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Document /></el-icon>
                今日详细数据
              </span>
              <el-button type="primary" size="small" @click="refreshData">
                <el-icon><Refresh /></el-icon>
                刷新数据
              </el-button>
            </div>
          </template>
          <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="time" label="时间段" width="180" />
            <el-table-column prop="impressions" label="展现量" sortable>
              <template #default="{ row }">
                {{ formatNumber(row.impressions) }}
              </template>
            </el-table-column>
            <el-table-column prop="clicks" label="点击量" sortable>
              <template #default="{ row }">
                {{ formatNumber(row.clicks) }}
              </template>
            </el-table-column>
            <el-table-column prop="ctr" label="点击率" sortable>
              <template #default="{ row }">
                {{ row.ctr }}%
              </template>
            </el-table-column>
            <el-table-column prop="revenue" label="收益" sortable>
              <template #default="{ row }">
                ¥{{ formatMoney(row.revenue) }}
              </template>
            </el-table-column>
            <el-table-column prop="profit" label="利润" sortable>
              <template #default="{ row }">
                ¥{{ formatMoney(row.profit) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 填充率详情对话框 -->
    <el-dialog
      v-model="fillRateDialogVisible"
      title="近7天填充率详情"
      width="80%"
      :close-on-click-modal="false"
    >
      <div ref="fillRateChartRef" style="width: 100%; height: 500px;"></div>
    </el-dialog>
  </div>
</template>

<script setup name="Index">
import { ref, onMounted, onUnmounted, reactive, computed } from 'vue'
import * as echarts from 'echarts'
import useSettingsStore from '@/store/modules/settings'
import { getMonthlySummary, getTrendData, getRevenueData, getTodayFillRate, getFillRateData } from '@/api/data/dataDspSlot'
import { getAdTypeDistribution } from '@/api/ad/type'

const settingsStore = useSettingsStore()
const isDark = computed(() => settingsStore.isDark)
import {
  View,
  Pointer,
  Coin,
  Wallet,
  Top,
  TrendCharts,
  PieChart,
  Histogram,
  DataLine,
  Document,
  Refresh
} from '@element-plus/icons-vue'

// 统计数据
const stats = reactive({
  impressions: 0,
  clicks: 0,
  revenue: 0,
  profit: 0
})

// 趋势周期
const trendPeriod = ref('twelve')

// 图表引用
const trendChartRef = ref(null)
const pieChartRef = ref(null)
const revenueChartRef = ref(null)
const realtimeChartRef = ref(null)

// 图表实例
let trendChart = null
let pieChart = null
let revenueChart = null
let realtimeChart = null
let fillRateDetailChart = null

// 数据刷新定时器
let dataRefreshTimer = null
let realtimeRefreshTimer = null

// 填充率详情对话框
const fillRateDialogVisible = ref(false)
const fillRateChartRef = ref(null)

// 表格数据
const tableData = ref([])

// 格式化数字
function formatNumber(num) {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// 格式化金额
function formatMoney(num) {
  return num.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// 初始化趋势图
function initTrendChart() {
  trendChart = echarts.init(trendChartRef.value)
  updateTrendChart()
}

// 加载趋势数据
async function loadTrendData(days) {
  try {
    // 计算日期范围
    const endDate = new Date()
    const startDate = new Date()
    startDate.setDate(startDate.getDate() - days + 1)

    // 格式化日期为 yyyyMMdd
    const formatDate = (date) => {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}${month}${day}`
    }

    const startDateStr = formatDate(startDate)
    const endDateStr = formatDate(endDate)

    // 调用后端接口
    const response = await getTrendData(startDateStr, endDateStr)
    const data = response.data || []

    // 格式化返回的数据
    const dates = []
    const impressions = []
    const clicks = []

    // 填充数据（可能有些日期没有数据）
    const dateMap = {}
    data.forEach(item => {
      dateMap[item.date] = {
        show_pv: item.show_pv || 0,
        click_pv: item.click_pv || 0
      }
    })

    // 生成日期序列
    const currentDate = new Date(startDate)
    while (currentDate <= endDate) {
      const dateStr = formatDate(currentDate)
      const month = String(currentDate.getMonth() + 1).padStart(2, '0')
      const day = String(currentDate.getDate()).padStart(2, '0')
      dates.push(`${month}-${day}`)

      const dayData = dateMap[dateStr]
      impressions.push(dayData ? dayData.show_pv : 0)
      clicks.push(dayData ? dayData.click_pv : 0)

      currentDate.setDate(currentDate.getDate() + 1)
    }

    return { dates, impressions, clicks }
  } catch (error) {
    console.error('获取趋势数据失败:', error)
    return { dates: [], impressions: [], clicks: [] }
  }
}

// 更新趋势图
async function updateTrendChart() {
  let days = 12 // 默认12天

  if (trendPeriod.value === 'twelve') {
    days = 12 // 近12天
  } else if (trendPeriod.value === 'month') {
    days = 30 // 近30天
  }

  // 加载数据
  const trendData = await loadTrendData(days)
  const dates = trendData.dates
  const impressions = trendData.impressions
  const clicks = trendData.clicks

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      data: ['展现量', '点击量'],
      textStyle: { color: '#666' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#ddd' } },
      axisLabel: { color: '#666' }
    },
    yAxis: [
      {
        type: 'value',
        name: '展现量',
        splitLine: { lineStyle: { type: 'dashed', color: '#eee' } },
        axisLabel: { color: '#666' }
      },
      {
        type: 'value',
        name: '点击量',
        splitLine: { show: false },
        axisLabel: { color: '#666' }
      }
    ],
    series: [
      {
        name: '展现量',
        type: 'line',
        smooth: true,
        lineStyle: { width: 3 },
        showSymbol: false,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        },
        data: impressions
      },
      {
        name: '点击量',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        lineStyle: { width: 3 },
        showSymbol: false,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        },
        data: clicks
      }
    ]
  }

  trendChart.setOption(option)
}

// 初始化饼图
async function initPieChart() {
  pieChart = echarts.init(pieChartRef.value)

  try {
    // 调用后端接口获取广告类型分布数据
    const response = await getAdTypeDistribution()
    const distribution = response.data || []

    // 颜色数组
    const colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#9a60b4', '#ef6666', '#f39c12']

    // 计算总数
    const total = distribution.reduce((sum, item) => sum + (item.slot_count || 0), 0)

    // 转换为 ECharts 需要的格式
    const chartData = distribution.map((item, index) => ({
      value: item.slot_count || 0,
      name: item.name,
      itemStyle: { color: colors[index % colors.length] }
    }))

    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { color: '#666' }
      },
      series: [
        {
          name: '广告类型',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}\n{d}%'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 16,
              fontWeight: 'bold'
            }
          },
          data: chartData
        }
      ]
    }

    pieChart.setOption(option)
  } catch (error) {
    console.error('获取广告类型分布失败:', error)

    // 失败时显示空图表
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { color: '#666' }
      },
      series: [
        {
          name: '广告类型',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}\n{d}%'
          },
          data: []
        }
      ]
    }

    pieChart.setOption(option)
  }
}

// 初始化收益图
async function initRevenueChart() {
  revenueChart = echarts.init(revenueChartRef.value)

  try {
    // 调用后端接口获取前7天的收益数据
    const response = await getRevenueData(7)
    const revenueData = response.data || []

    // 格式化数据
    const dates = []
    const income = [] // 收入（单位：元）
    const spend = [] // 成本/支出（单位：元）

    // 填充数据
    const dateMap = {}
    revenueData.forEach(item => {
      dateMap[item.date] = {
        spend: (item.spend || 0) / 100, // 分转元
        income: (item.income || 0) / 100 // 分转元
      }
    })

    // 生成最近7天的日期序列
    for (let i = 6; i >= 0; i--) {
      const d = new Date()
      d.setDate(d.getDate() - i)
      const dateStr = d.getFullYear().toString() +
                     String(d.getMonth() + 1).padStart(2, '0') +
                     String(d.getDate()).padStart(2, '0')
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      dates.push(`${month}-${day}`)

      const dayData = dateMap[dateStr]
      income.push(dayData ? dayData.income : 0)
      spend.push(dayData ? dayData.spend : 0)
    }

    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      legend: {
        data: ['收入', '预算流水'],
        textStyle: { color: '#666' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dates,
        axisLine: { lineStyle: { color: '#ddd' } },
        axisLabel: { color: '#666' }
      },
      yAxis: {
        type: 'value',
        name: '金额(元)',
        splitLine: { lineStyle: { type: 'dashed', color: '#eee' } },
        axisLabel: {
          formatter: '¥{value}',
          color: '#666'
        }
      },
      series: [
        {
          name: '收入',
          type: 'bar',
          data: income,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ffd666' },
              { offset: 1, color: '#ffa940' }
            ]),
            borderRadius: [5, 5, 0, 0]
          }
        },
        {
          name: '预算流水',
          type: 'bar',
          data: spend,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#b37feb' },
              { offset: 1, color: '#722ed1' }
            ]),
            borderRadius: [5, 5, 0, 0]
          }
        }
      ]
    }

    revenueChart.setOption(option)
  } catch (error) {
    console.error('获取收益数据失败:', error)

    // 失败时显示空图表
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      legend: {
        data: ['收入', '预算流水'],
        textStyle: { color: '#666' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: [],
        axisLine: { lineStyle: { color: '#ddd' } },
        axisLabel: { color: '#666' }
      },
      yAxis: {
        type: 'value',
        name: '金额(元)',
        splitLine: { lineStyle: { type: 'dashed', color: '#eee' } },
        axisLabel: {
          formatter: '¥{value}',
          color: '#666'
        }
      },
      series: [
        {
          name: '收入',
          type: 'bar',
          data: [],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ffd666' },
              { offset: 1, color: '#ffa940' }
            ]),
            borderRadius: [5, 5, 0, 0]
          }
        },
        {
          name: '预算流水',
          type: 'bar',
          data: [],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#b37feb' },
              { offset: 1, color: '#722ed1' }
            ]),
            borderRadius: [5, 5, 0, 0]
          }
        }
      ]
    }

    revenueChart.setOption(option)
  }
}

// 加载最近12小时填充率数据
async function loadTodayFillRateData() {
  try {
    // 生成当前月份的表名（格式：data_dsp_slot_day_YYYYMM）
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const tableName = `data_dsp_slot_day_${year}${month}`

    // 调用后端接口获取最近12小时填充率数据
    const response = await getTodayFillRate(tableName)
    const fillRateData = response.data || []

    // 生成连续的12个小时标签
    const hours = []
    const fillRates = []

    // 创建数据映射，key为yyyyMMddHH格式
    const dataMap = {}
    fillRateData.forEach(item => {
      let hourDate = item.hour_date || item.date
      if (hourDate) {
        const key = typeof hourDate === 'number' ? hourDate.toString() : hourDate.toString()
        dataMap[key] = {
          ret_pv: item.ret_pv || 0,
          show_pv: item.show_pv || 0
        }
      }
    })

    // 生成最近12个小时的时间序列
    for (let i = 11; i >= 0; i--) {
      const d = new Date(now.getTime() - i * 60 * 60 * 1000)
      const hourStr = String(d.getHours()).padStart(2, '0')
      hours.push(`${hourStr}:00`)

      // 构建yyyyMMddHH格式的key
      const yyyy = d.getFullYear()
      const MM = String(d.getMonth() + 1).padStart(2, '0')
      const dd = String(d.getDate()).padStart(2, '0')
      const HH = hourStr
      const key = `${yyyy}${MM}${dd}${HH}`

      // 从映射中获取数据并计算填充率
      const data = dataMap[key]
      const retPv = data ? data.ret_pv : 0
      const showPv = data ? data.show_pv : 0
      const fillRate = showPv > 0 ? (retPv / showPv * 100).toFixed(2) : '0.00'
      fillRates.push(parseFloat(fillRate))
    }

    return { hours, fillRates }
  } catch (error) {
    console.error('获取12小时填充率数据失败:', error)

    // 失败时返回空数据
    const hours = []
    const fillRates = []
    const now = new Date()

    for (let i = 11; i >= 0; i--) {
      const d = new Date(now.getTime() - i * 60 * 60 * 1000)
      const hourStr = String(d.getHours()).padStart(2, '0')
      hours.push(`${hourStr}:00`)
      fillRates.push(0)
    }

    return { hours, fillRates }
  }
}

// 初始化实时图
async function initRealtimeChart() {
  realtimeChart = echarts.init(realtimeChartRef.value)

  // 加载今日填充率数据
  const { hours, fillRates } = await loadTodayFillRateData()

  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        params = params[0]
        return `${params.axisValue}<br/>填充率: ${params.value}%`
      },
      axisPointer: {
        animation: false
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: hours,
      splitLine: { show: false },
      axisLabel: {
        color: '#666',
        interval: 0,
        rotate: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '填充率 (%)',
      boundaryGap: [0, '100%'],
      splitLine: { lineStyle: { type: 'dashed', color: '#eee' } },
      axisLabel: {
        formatter: '{value}%',
        color: '#666'
      },
      min: 0,
      max: 100
    },
    series: [
      {
        name: '填充率',
        type: 'line',
        showSymbol: true,
        symbolSize: 6,
        hoverAnimation: false,
        data: fillRates,
        lineStyle: {
          width: 3,
          color: '#52c41a'
        },
        itemStyle: {
          color: '#52c41a'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(82, 196, 26, 0.3)' },
            { offset: 1, color: 'rgba(82, 196, 26, 0.05)' }
          ])
        }
      }
    ]
  }

  realtimeChart.setOption(option)
}

// 加载近7天填充率数据
async function load7DayFillRateData() {
  try {
    // 调用后端接口获取近7天填充率数据
    const response = await getFillRateData(7)
    const fillRateData = response.data || []

    // 格式化数据：计算填充率 = (ret_pv / show_pv * 100)
    const dates = []
    const fillRates = []

    // 填充数据
    const dateMap = {}
    fillRateData.forEach(item => {
      dateMap[item.date] = {
        ret_pv: item.ret_pv || 0,
        show_pv: item.show_pv || 0
      }
    })

    // 生成最近7天的日期序列
    for (let i = 6; i >= 0; i--) {
      const d = new Date()
      d.setDate(d.getDate() - i)
      const dateStr = d.getFullYear().toString() +
                     String(d.getMonth() + 1).padStart(2, '0') +
                     String(d.getDate()).padStart(2, '0')
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      dates.push(`${month}-${day}`)

      // 计算填充率
      const dayData = dateMap[dateStr]
      const retPv = dayData ? dayData.ret_pv : 0
      const showPv = dayData ? dayData.show_pv : 0
      const fillRate = showPv > 0 ? (retPv / showPv * 100).toFixed(2) : '0.00'
      fillRates.push(parseFloat(fillRate))
    }

    return { dates, fillRates }
  } catch (error) {
    console.error('获取近7天填充率数据失败:', error)

    // 失败时返回空数据
    const dates = []
    const fillRates = []
    for (let i = 6; i >= 0; i--) {
      const d = new Date()
      d.setDate(d.getDate() - i)
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      dates.push(`${month}-${day}`)
      fillRates.push(0)
    }
    return { dates, fillRates }
  }
}

// 显示填充率详情对话框
async function showFillRateDetail() {
  fillRateDialogVisible.value = true

  // 等待对话框渲染完成后初始化图表
  setTimeout(async () => {
    if (!fillRateDetailChart) {
      fillRateDetailChart = echarts.init(fillRateChartRef.value)
    }

    // 加载近7天填充率数据
    const { dates, fillRates } = await load7DayFillRateData()

    const option = {
      tooltip: {
        trigger: 'axis',
        formatter: function(params) {
          params = params[0]
          return `${params.axisValue}<br/>填充率: ${params.value}%`
        }
      },
      legend: {
        data: ['填充率'],
        textStyle: { color: '#666' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: dates,
        axisLine: { lineStyle: { color: '#ddd' } },
        axisLabel: { color: '#666' }
      },
      yAxis: {
        type: 'value',
        name: '填充率 (%)',
        splitLine: { lineStyle: { type: 'dashed', color: '#eee' } },
        axisLabel: {
          formatter: '{value}%',
          color: '#666'
        },
        min: 0,
        max: 100
      },
      series: [
        {
          name: '填充率',
          type: 'line',
          smooth: true,
          lineStyle: { width: 3 },
          showSymbol: true,
          symbolSize: 8,
          data: fillRates,
          itemStyle: {
            color: '#52c41a'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(82, 196, 26, 0.3)' },
              { offset: 1, color: 'rgba(82, 196, 26, 0.05)' }
            ])
          }
        }
      ]
    }

    fillRateDetailChart.setOption(option)
  }, 100)
}

// 刷新数据
async function refreshData() {
  try {
    // 生成当前月份的表名（格式：data_dsp_slot_day_YYYYMM）
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const tableName = `data_dsp_slot_day_${year}${month}`

    // 调用后端接口获取当月汇总数据
    const response = await getMonthlySummary(tableName)
    const data = response.data

    // 更新统计数据（注意单位转换）
    // 广告展示：show_pv（直接显示）
    stats.impressions = data.impressions || 0

    // 广告点击：click_pv（直接显示）
    stats.clicks = data.clicks || 0

    // 预估收益：spend（单位：分，需要转换为元）
    stats.revenue = (data.spend || 0) / 100

    // 利润：income（单位：分，需要转换为元）
    stats.profit = (data.income || 0) / 100

    console.log('首页统计数据已更新:', stats)
  } catch (error) {
    console.error('获取统计数据失败:', error)
    // 失败时保持数据为0或原值
  }

  // 更新图表
  updateTrendChart()
}

// 窗口大小变化时重新渲染图表
function handleResize() {
  trendChart?.resize()
  pieChart?.resize()
  revenueChart?.resize()
  realtimeChart?.resize()
  fillRateDetailChart?.resize()
}

onMounted(() => {
  // 加载统计数据
  refreshData()

  // 初始化所有图表
  setTimeout(() => {
    initTrendChart()
    initPieChart()
    initRevenueChart()
    initRealtimeChart()
  }, 100)

  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)

  // 设置定时器，每隔10秒刷新一次数据（主要统计和趋势图）
  dataRefreshTimer = setInterval(() => {
    refreshData()
  }, 10000)

  // 设置定时器，每隔10分钟刷新一次实时监控图表
  realtimeRefreshTimer = setInterval(() => {
    initRealtimeChart()
  }, 600000) // 10分钟 = 600000毫秒
})

onUnmounted(() => {
  // 清除数据刷新定时器
  if (dataRefreshTimer) {
    clearInterval(dataRefreshTimer)
    dataRefreshTimer = null
  }

  // 清除实时监控刷新定时器
  if (realtimeRefreshTimer) {
    clearInterval(realtimeRefreshTimer)
    realtimeRefreshTimer = null
  }

  // 销毁图表实例
  trendChart?.dispose()
  pieChart?.dispose()
  revenueChart?.dispose()
  realtimeChart?.dispose()
  fillRateDetailChart?.dispose()

  // 移除事件监听
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped lang="scss">
.dsp-dashboard {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
  transition: background-color 0.3s ease;

  &.dark-mode {
    background: #1a1a1a;

    .chart-card {
      :deep(.el-card__header) {
        background: #2a2a2a !important;
        border-bottom-color: #3a3a3a !important;

        .card-title {
          color: #e0e0e0 !important;
        }
      }

      :deep(.el-card__body) {
        background: #2a2a2a !important;
      }
    }

    :deep(.el-table) {
      background-color: #2a2a2a !important;
      color: #e0e0e0 !important;

      th {
        background-color: #1f1f1f !important;
        color: #b0b0b0 !important;
      }

      tr {
        background-color: #2a2a2a !important;

        &:hover {
          background-color: #3a3a3a !important;
        }
      }

      td {
        border-color: #3a3a3a !important;
      }
    }

    :deep(.el-radio-group) {
      .el-radio-button {
        .el-radio-button__inner {
          background: #2a2a2a;
          border-color: #3a3a3a;
          color: #e0e0e0;

          &:hover {
            color: var(--el-color-primary);
          }
        }

        &.is-active {
          .el-radio-button__inner {
            background: var(--el-color-primary);
            border-color: var(--el-color-primary);
            color: #fff;
          }
        }
      }
    }
  }

  .stats-cards {
    margin-bottom: 20px;

    .stat-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 16px;
      padding: 24px;
      color: #fff;
      display: flex;
      align-items: center;
      gap: 20px;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s, box-shadow 0.3s;
      cursor: pointer;
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
        transition: transform 0.5s;
      }

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);

        &::before {
          transform: scale(1.2);
        }
      }

      .stat-icon {
        width: 70px;
        height: 70px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        backdrop-filter: blur(10px);
      }

      .stat-content {
        flex: 1;

        .stat-value {
          font-size: 28px;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 8px;
        }

        .stat-change {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 13px;

          &.positive {
            color: #95de64;
          }

          &.negative {
            color: #ff4d4f;
          }
        }
      }

      &.stat-card-blue {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.stat-card-green {
        background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
      }

      &.stat-card-orange {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.stat-card-purple {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }
  }

  .charts-row {
    margin-bottom: 20px;

    .chart-card {
      border-radius: 12px;
      overflow: hidden;
      transition: all 0.3s ease;

      :deep(.el-card) {
        border-radius: 12px;
        border: none;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
      }

      :deep(.el-card__header) {
        padding: 18px 24px;
        border-bottom: 1px solid #f0f0f0;
        background: #fff;
        transition: all 0.3s ease;
      }

      :deep(.el-card__body) {
        padding: 24px;
        transition: all 0.3s ease;
      }

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .card-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }
      }

      .chart-container {
        width: 100%;
        height: 350px;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .dsp-dashboard {
    padding: 10px;

    .stats-cards {
      .stat-card {
        padding: 16px;
        margin-bottom: 10px;

        .stat-icon {
          width: 50px;
          height: 50px;

          :deep(.el-icon) {
            font-size: 28px !important;
          }
        }

        .stat-content {
          .stat-value {
            font-size: 22px;
          }
        }
      }
    }

    .charts-row {
      .chart-card {
        margin-bottom: 10px;

        .chart-container {
          height: 280px;
        }
      }
    }
  }
}
</style>

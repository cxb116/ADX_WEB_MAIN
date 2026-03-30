<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="媒体用户" prop="mediaId">
        <el-select
          v-model="queryParams.mediaId"
          placeholder="请选择媒体用户"
          clearable
          filterable
        >
          <el-option
            v-for="item in mediaList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="应用" prop="appId">
        <el-select
          v-model="queryParams.appId"
          placeholder="请选择应用"
          clearable
          filterable
        >
          <el-option
            v-for="item in appList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="媒体广告位" label-width="90" prop="sspSlotId">
        <el-input
          v-model="queryParams.sspSlotId"
          placeholder="请输入媒体广告位"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预算广告位" label-width="90" prop="dspSlotId">
        <el-input
          v-model="queryParams.dspSlotId"
          placeholder="请输入预算广告位"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预算广告位编号" label-width="110" prop="dspSlotCode">
        <el-input
          v-model="queryParams.dspSlotCode"
          placeholder="请输入预算广告位编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="tableType === 'day' ? '日期' : '时间'" label-width="90">
        <el-date-picker
          v-model="dateRange"
          :value-format="tableType === 'day' ? 'YYYYMMDD' : 'YYYYMMDDHH'"
          :type="tableType === 'day' ? 'daterange' : 'datetimerange'"
          range-separator="-"
          :start-placeholder="tableType === 'day' ? '开始日期' : '开始时间'"
          :end-placeholder="tableType === 'day' ? '结束日期' : '结束时间'"
          :format="tableType === 'day' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:00'"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-radio-group v-model="tableType" @change="handleTableTypeChange">
          <el-radio-button label="day">天表</el-radio-button>
          <el-radio-button label="hour">小时表</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="TrendCharts"
          @click="handleShowChart"
        >图表</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['data:data_ssp_slot:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="data_ssp_slotList" @selection-change="handleSelectionChange">
      <el-table-column label="日期" align="center" width="180" prop="date">
        <template #default="scope">
          <span v-if="scope.row.date">{{ formatDate(scope.row.date) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="主键" align="center" prop="id" />-->
<!--      <el-table-column label="媒体用户" align="center" width="55" prop="mediaId" />-->
      <el-table-column label="媒体" align="center" width="150" prop="mediaName">
        <template #default="scope">
          <span v-if="scope.row.mediaName">{{ scope.row.mediaName }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="应用" align="center" width="150" prop="appName">
        <template #default="scope">
          <span v-if="scope.row.appName">{{ scope.row.appName }}（{{ scope.row.appId }}）</span>
          <span v-else>{{ scope.row.appId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="媒体广告位" align="center" width="200" prop="sspSlotName">
        <template #default="scope">
          <span v-if="scope.row.sspSlotName">{{ scope.row.sspSlotName }}（{{ scope.row.sspSlotId }}）</span>
          <span v-else>{{ scope.row.sspSlotId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预算广告位" align="center" width="200" prop="dspSlotName">
        <template #default="scope">
          <span v-if="scope.row.dspSlotName">{{ scope.row.dspSlotName }}（{{ scope.row.dspSlotId }}）</span>
          <span v-else>{{ scope.row.dspSlotId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预算广告位编号" align="center" width="270" prop="dspSlotCode" />
      <el-table-column label="展示PV" align="center" width="100" prop="showPv" />
      <el-table-column label="点击PV" align="center" width="100" prop="clickPv" />
      <el-table-column label="请求PV" align="center" width="100" prop="reqPv" />
      <el-table-column label="丢弃请求" align="center" width="100" prop="discard" />
      <el-table-column label="请求丢失率(%)" align="center" width="130" prop="requestLossRate">
        <template #default="scope">
          <span v-if="scope.row.requestLossRate !== null && scope.row.requestLossRate !== undefined">
            {{ scope.row.requestLossRate.toFixed(2) }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="返回PV" align="center" width="100" prop="retPv" />
      <el-table-column label="展现率(%)" align="center" width="120" prop="showRate">
        <template #default="scope">
          <span v-if="scope.row.showRate !== null && scope.row.showRate !== undefined">
            {{ scope.row.showRate.toFixed(2) }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="点击率(%)" align="center" width="120" prop="clickRate">
        <template #default="scope">
          <span v-if="scope.row.clickRate !== null && scope.row.clickRate !== undefined">
            {{ scope.row.clickRate.toFixed(2) }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="填充率(%)" align="center" width="120" prop="fillRate">
        <template #default="scope">
          <span v-if="scope.row.fillRate !== null && scope.row.fillRate !== undefined">
            {{ scope.row.fillRate.toFixed(2) }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="预算流水(元)" align="center" width="120" prop="spend">
        <template #default="scope">
          <span v-if="scope.row.spend !== null && scope.row.spend !== undefined">
            {{ (scope.row.spend / 100).toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="收入(元)" align="center" width="100" prop="income">
        <template #default="scope">
          <span v-if="scope.row.income !== null && scope.row.income !== undefined">
            {{ (scope.row.income / 100).toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="eCPM(元)" align="center" width="120" prop="ecpm">
        <template #default="scope">
          <span v-if="scope.row.ecpm !== null && scope.row.ecpm !== undefined">
            {{ scope.row.ecpm.toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="CPC" align="center" width="100" prop="activatePv" />
<!--      <el-table-column label="折后点击" align="center" width="100"  prop="discountClickPv" />-->
<!--      <el-table-column label="折后展示" align="center" width="100" prop="discountShowPv" />-->
<!--      <el-table-column label="调起成功" align="center" width="100" prop="dplsuccPv" />-->
<!--      <el-table-column label="完成量" align="center" width="100" prop="completePv" />-->
<!--      <el-table-column label="安装量" align="center" width="100" prop="installPv" />-->
<!--      <el-table-column label="激活量" align="center" width="100" prop="activatePv" />-->

<!--      <el-table-column label="日期 yyyyMMdd" align="center" prop="date" />-->
<!--      <el-table-column label="创建时间" align="center" width="230" prop="createdAt" :formatter="formatTimestamp" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template #default="scope">-->
<!--          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['data:data_ssp_slot:edit']">修改</el-button>-->
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['data:data_ssp_slot:remove']">删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 图表对话框 -->
    <el-dialog
      v-model="chartDialogVisible"
      title="填充率与展现率趋势图"
      width="80%"
      :close-on-click-modal="false"
      @opened="handleChartDialogOpened"
      @closed="handleChartDialogClosed"
    >
      <div ref="chartRef" style="width: 100%; height: 500px;"></div>
    </el-dialog>

  </div>
</template>

<script setup name="Data_ssp_slot">
import { listData_ssp_slot, getData_ssp_slot, delData_ssp_slot, addData_ssp_slot, updateData_ssp_slot } from "@/api/data/dataSspSlot.js"
import { listMedia } from "@/api/flow/media.js"
import { listApp } from "@/api/flow/app.js"
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const chartRef = ref(null)
let chartInstance = null

const data_ssp_slotList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const dateRange = ref([])
const tableType = ref('day') // 表类型: 'day' 或 'hour'
const mediaList = ref([]) // 媒体列表
const appList = ref([]) // 应用列表
const chartDialogVisible = ref(false) // 图表对话框显示状态

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    mediaId: null,
    appId: null,
    sspSlotId: null,
    dspSlotId: null,
    dspSlotCode: null,
    showPv: null,
    showUv: null,
    clickPv: null,
    clickUv: null,
    reqPv: null,
    reqUv: null,
    discard: null,
    retPv: null,
    retUv: null,
    spend: null,
    income: null,
    discountClickPv: null,
    discountShowPv: null,
    dplsuccPv: null,
    completePv: null,
    installPv: null,
    activatePv: null,
    date: null,
    createdAt: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 格式化时间戳 */
function formatTimestamp(row, column, cellValue) {
  if (!cellValue) return ''
  const timestamp = parseInt(cellValue)
  // 如果时间戳是秒级的，转换为毫秒
  const date = new Date(timestamp.toString().length === 10 ? timestamp * 1000 : timestamp)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

/** 格式化日期 yyyyMMddHH */
function formatDate(dateValue) {
  if (!dateValue) return ''
  const dateStr = dateValue.toString()
  // yyyyMMddHH -> yyyy-MM-dd HH:00
  if (dateStr.length === 10) {
    return `${dateStr.substring(0, 4)}-${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)} ${dateStr.substring(8, 10)}:00`
  }
  // yyyyMMdd -> yyyy-MM-dd
  if (dateStr.length === 8) {
    return `${dateStr.substring(0, 4)}-${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)}`
  }
  return dateStr
}

/** 获取媒体列表 */
function getMediaList() {
  listMedia().then(response => {
    mediaList.value = response.rows || []
  })
}

/** 获取应用列表 */
function getAppList() {
  listApp().then(response => {
    appList.value = response.rows || []
  })
}

/** 生成表名 */
function generateTableName() {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')

  if (tableType.value === 'day') {
    // 天表: data_ssp_slot_day_YYYYMM
    return `data_ssp_slot_day_${year}${month}`
  } else {
    // 小时表: data_ssp_slot_hour_YYYYMM
    return `data_ssp_slot_hour_${year}${month}`
  }
}

/** 表类型切换处理 */
function handleTableTypeChange() {
  queryParams.value.pageNum = 1
  // 清空日期范围，因为天表和小时表的日期格式不同
  dateRange.value = []
  getList()
}

/** 查询媒体数据报表列表 */
function getList() {
  loading.value = true
  const params = { ...queryParams.value }

  // 处理日期范围参数
  if (dateRange.value && dateRange.value.length === 2) {
    params.beginDate = dateRange.value[0]
    params.endDate = dateRange.value[1]
  }

  // 添加动态表名参数
  params.tableName = generateTableName()

  listData_ssp_slot(params).then(response => {
    data_ssp_slotList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    mediaId: null,
    appId: null,
    sspSlotId: null,
    dspSlotId: null,
    dspSlotCode: null,
    showPv: null,
    showUv: null,
    clickPv: null,
    clickUv: null,
    reqPv: null,
    reqUv: null,
    discard: null,
    retPv: null,
    retUv: null,
    spend: null,
    income: null,
    discountClickPv: null,
    discountShowPv: null,
    dplsuccPv: null,
    completePv: null,
    installPv: null,
    activatePv: null,
    date: null,
    createdAt: null
  }
  proxy.resetForm("data_ssp_slotRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  dateRange.value = []
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加媒体数据报表"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getData_ssp_slot(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改媒体数据报表"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["data_ssp_slotRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateData_ssp_slot(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addData_ssp_slot(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除媒体数据报表编号为"' + _ids + '"的数据项？').then(function() {
    return delData_ssp_slot(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('data/data_ssp_slot/export', {
    ...queryParams.value,
    tableName: generateTableName()
  }, `data_ssp_slot_${new Date().getTime()}.xlsx`)
}

/** 显示图表对话框 */
function handleShowChart() {
  chartDialogVisible.value = true
}

/** 图表对话框打开后的回调 */
function handleChartDialogOpened() {
  initChart()
  updateChart()
}

/** 图表对话框关闭后的回调 */
function handleChartDialogClosed() {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
}

/** 初始化图表 */
function initChart() {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  const option = {
    title: {
      text: '填充率与展现率趋势',
      left: 'center',
      top: 10
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['填充率', '展现率'],
      top: 40
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
      data: []
    },
    yAxis: {
      type: 'value',
      name: '百分比(%)',
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [
      {
        name: '填充率',
        type: 'line',
        smooth: true,
        data: [],
        itemStyle: {
          color: '#67C23A'
        },
        areaStyle: {
          opacity: 0.3
        }
      },
      {
        name: '展现率',
        type: 'line',
        smooth: true,
        data: [],
        itemStyle: {
          color: '#409EFF'
        },
        areaStyle: {
          opacity: 0.3
        }
      }
    ]
  }
  chartInstance.setOption(option)

  // 响应式调整
  window.addEventListener('resize', handleChartResize)
}

/** 图表响应式调整 */
function handleChartResize() {
  chartInstance?.resize()
}

/** 更新图表数据 */
function updateChart() {
  if (!chartInstance || !data_ssp_slotList.value || data_ssp_slotList.value.length === 0) {
    return
  }

  // 提取数据并按日期排序
  const chartData = [...data_ssp_slotList.value].sort((a, b) => {
    return (a.date || '').toString().localeCompare((b.date || '').toString())
  })

  const dates = chartData.map(item => {
    const dateValue = item.date
    if (!dateValue) return '-'
    const dateStr = dateValue.toString()
    if (dateStr.length === 10) {
      return `${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)} ${dateStr.substring(8, 10)}:00`
    }
    if (dateStr.length === 8) {
      return `${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)}`
    }
    return dateStr
  })

  const fillRates = chartData.map(item =>
    item.fillRate !== null && item.fillRate !== undefined ? item.fillRate.toFixed(2) : 0
  )

  const showRates = chartData.map(item =>
    item.showRate !== null && item.showRate !== undefined ? item.showRate.toFixed(2) : 0
  )

  chartInstance.setOption({
    xAxis: {
      data: dates
    },
    series: [
      {
        data: fillRates
      },
      {
        data: showRates
      }
    ]
  })
}

getList()
getMediaList()
getAppList()
</script>

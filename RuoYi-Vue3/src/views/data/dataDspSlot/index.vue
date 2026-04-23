<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"  label-width="68px"
             :label-style="{ color: '#000' }">
      <el-form-item  prop="companyId">
        <el-select
          v-model="queryParams.companyId"
          placeholder="请选择公司"
          clearable
          filterable
          @change="handleCompanyChange"
          style="width: 160px;"
        >
          <el-option
            v-for="item in companyList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label-width="100" prop="dspSlotId">
        <el-select
          v-model="queryParams.dspSlotId"
          placeholder="请选择预算广告位"
          clearable
          filterable
          :disabled="!queryParams.companyId"
          style="width: 160px;"
        >
          <el-option
            v-for="item in filteredDspSlotList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item   prop="dspSlotCode">
        <el-input
          v-model="queryParams.dspSlotCode"
          placeholder="请输入预算广告位编码"
          clearable
          @keyup.enter="handleQuery"
          style="width: 160px;"
        />
      </el-form-item>
      <el-form-item prop="dspPayType">
        <el-select
          v-model="queryParams.dspPayType"
          placeholder="请选择结算方式"
          clearable
          style="width: 160px;"
        >
          <el-option label="分成" :value="1" />
          <el-option label="RTB" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button class="btn-blue" type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重置</el-button>
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
            class="btn-blue"
          type="success"
          plain
          icon="TrendCharts"
          @click="handleShowChart"
        >图表</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            class="btn-regge"
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:dataDspSlot:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataDspSlotList" @selection-change="handleSelectionChange"
              style="width: 100%"
              :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
              :cell-style="{ color: '#000' }"
              border
              table-layout="auto"
              highlight-current-row="true"
    >
      <el-table-column label="日期" align="center" width="150"  prop="date">
        <template #default="scope">
          <span v-if="scope.row.date">{{ formatDate(scope.row.date) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="预算广告位" align="center" width="300" show-overflow-tooltip prop="dspSlotName">
        <template #default="scope">
          <span v-if="scope.row.dspSlotName">{{ scope.row.dspSlotName }}（{{ scope.row.dspSlotId }}）</span>
          <span v-else>{{ scope.row.dspSlotId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预算广告位编号" align="center" width="250" show-overflow-tooltip prop="dspSlotCode" />
      <el-table-column label="结算方式" align="center" width="100" prop="dspPayType">
        <template #default="scope">
          <span v-if="scope.row.dspPayType === 1">分成</span>
          <span v-else-if="scope.row.dspPayType === 2">RTB</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="请求PV" align="center" width="120" prop="reqPv" />
      <el-table-column label="返回PV" align="center" width="120" prop="retPv" />
      <el-table-column label="展示PV" align="center" width="120" prop="showPv" />
      <el-table-column label="点击PV" align="center" width="120" prop="clickPv" />

      <el-table-column label="丢弃请求" align="center" width="120" prop="discard" />
      <el-table-column label="请求丢失率(%)" align="center" width="130" prop="requestLossRate">
        <template #default="scope">
          <span v-if="scope.row.requestLossRate !== null && scope.row.requestLossRate !== undefined">
            {{ scope.row.requestLossRate.toFixed(2) }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
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

      <el-table-column label="eCPM(元)" align="center" width="120" prop="ecpm">
        <template #default="scope">
          <span v-if="scope.row.ecpm !== null && scope.row.ecpm !== undefined">
            {{ scope.row.ecpm.toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column label="成本(元)" align="center" width="120" prop="spend">
        <template #default="scope">
          <span v-if="scope.row.spend !== null && scope.row.spend !== undefined">
            {{ (scope.row.spend / 100).toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="收入(元)" align="center" width="120" prop="income">
        <template #default="scope">
          <span v-if="scope.row.income !== null && scope.row.income !== undefined">
            {{ (scope.row.income / 100).toFixed(2) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="折后点击" align="center" width="100" prop="discountClickPv" />-->
<!--      <el-table-column label="折后展示" align="center" width="100" prop="discountShowPv" />-->
<!--      <el-table-column label="调起成功" align="center" prop="dplsuccPv" />-->
<!--      <el-table-column label="完成量" align="center" prop="completePv" />-->
<!--      <el-table-column label="安装量" align="center" prop="installPv" />-->
<!--      <el-table-column label="激活量" align="center" prop="activatePv" />-->
<!--      <el-table-column label="时间(yyyyMMdd / yyyyMMddHH)" align="center" prop="date" />-->
<!--      <el-table-column label="创建时间" align="center" width="270" prop="createdAt" :formatter="formatTimestamp" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template #default="scope">-->
<!--          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:dataDspSlot:edit']">修改</el-button>-->
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:dataDspSlot:remove']">删除</el-button>-->
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
      <!-- 图表查询条件 -->
      <el-form :model="chartQueryParams" :inline="true" label-width="100px">
        <el-form-item label="公司">
          <el-select
            v-model="chartQueryParams.companyId"
            placeholder="请选择公司"
            clearable
            filterable
            @change="handleChartCompanyChange"
            style="width: 200px"
          >
            <el-option
              v-for="item in companyList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算广告位">
          <el-select
            v-model="chartQueryParams.dspSlotId"
            placeholder="请选择预算广告位"
            clearable
            filterable
            :disabled="!chartQueryParams.companyId"
            style="width: 250px"
          >
            <el-option
              v-for="item in chartFilteredDspSlotList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型">
          <el-radio-group v-model="chartQueryParams.tableType">
            <el-radio-button label="day">天表</el-radio-button>
            <el-radio-button label="hour">小时表</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleChartQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <div ref="chartRef" style="width: 100%; height: 450px;"></div>
    </el-dialog>
  </div>
</template>

<script setup name="DataDspSlot">
import { listDataDspSlot, getDataDspSlot, delDataDspSlot, addDataDspSlot, updateDataDspSlot } from "@/api/data/dataDspSlot"
import { listInfo } from "@/api/budget/info"
import { listCompany } from "@/api/budget/company"
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const chartRef = ref(null)
let chartInstance = null

const dataDspSlotList = ref([])
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
const companyList = ref([]) // 公司列表
const dspSlotList = ref([]) // 预算广告位列表
const chartDialogVisible = ref(false) // 图表对话框显示状态

// 图表查询参数
const chartQueryParams = reactive({
  companyId: null,
  dspSlotId: null,
  tableType: 'day' // 'day' 或 'hour'
})

// 根据选择的公司过滤预算广告位
const filteredDspSlotList = computed(() => {
  if (!queryParams.value.companyId) {
    return dspSlotList.value
  }
  return dspSlotList.value.filter(item => item.companyId === queryParams.value.companyId)
})

// 图表中根据选择的公司过滤预算广告位
const chartFilteredDspSlotList = computed(() => {
  if (!chartQueryParams.companyId) {
    return dspSlotList.value
  }
  return dspSlotList.value.filter(item => item.companyId === chartQueryParams.companyId)
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    companyId: null,
    dspSlotId: null,
    dspSlotCode: null,
    dspPayType: null
  },
  rules: {
    dspSlotCode: [
      { required: true, message: "预算广告位不能为空", trigger: "blur" }
    ],
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

/** 格式化日期 yyyyMMdd / yyyyMMddHH */
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

/** 获取公司列表 */
function getCompanyList() {
  listCompany().then(response => {
    companyList.value = response.rows || []
  })
}

/** 获取预算广告位列表 */
function getDspSlotList() {
  listInfo().then(response => {
    dspSlotList.value = response.rows || []
  })
}

/** 公司选择变化处理 */
function handleCompanyChange() {
  // 清空预算广告位选择
  queryParams.value.dspSlotId = null
}

/** 生成表名 */
function generateTableName() {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')

  if (tableType.value === 'day') {
    // 天表: data_dsp_slot_day_YYYYMM
    return `data_dsp_slot_day_${year}${month}`
  } else {
    // 小时表: data_dsp_slot_hour_YYYYMM
    return `data_dsp_slot_hour_${year}${month}`
  }
}

/** 表类型切换处理 */
function handleTableTypeChange() {
  queryParams.value.pageNum = 1
  // 清空日期范围，因为天表和小时表的日期格式不同
  dateRange.value = []
  getList()
}

/** 查询预算报表列表 */
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

  listDataDspSlot(params).then(response => {
    dataDspSlotList.value = response.rows
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
  proxy.resetForm("dataDspSlotRef")
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
  title.value = "添加预算报表"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getDataDspSlot(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预算报表"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dataDspSlotRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateDataDspSlot(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addDataDspSlot(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除预算报表编号为"' + _ids + '"的数据项？').then(function() {
    return delDataDspSlot(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/dataDspSlot/export', {
    ...queryParams.value,
    tableName: generateTableName()
  }, `dataDspSlot_${new Date().getTime()}.xlsx`)
}

/** 显示图表对话框 */
function handleShowChart() {
  // 如果列表页已经选择了公司，自动填充到图表查询
  if (queryParams.value.companyId) {
    chartQueryParams.companyId = queryParams.value.companyId
  }
  if (queryParams.value.dspSlotId) {
    chartQueryParams.dspSlotId = queryParams.value.dspSlotId
  }
  chartDialogVisible.value = true
}

/** 图表公司选择变化 */
function handleChartCompanyChange() {
  chartQueryParams.dspSlotId = null
}

/** 图表查询 */
function handleChartQuery() {
  if (!chartQueryParams.dspSlotId) {
    proxy.$modal.msgWarning("请先选择预算广告位")
    return
  }
  loadChartData()
}

/** 图表对话框打开后的回调 */
function handleChartDialogOpened() {
  initChart()
  // 如果已经选择了预算广告位，自动查询
  if (chartQueryParams.dspSlotId) {
    loadChartData()
  }
}

/** 图表对话框关闭后的回调 */
function handleChartDialogClosed() {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
  window.removeEventListener('resize', handleChartResize)
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
      top: 15,
      right: 10
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

/** 生成表名（用于图表查询） */
function generateChartTableName() {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')

  if (chartQueryParams.tableType === 'day') {
    // 天表: data_dsp_slot_day_YYYYMM
    return `data_dsp_slot_day_${year}${month}`
  } else {
    // 小时表: data_dsp_slot_hour_YYYYMM
    return `data_dsp_slot_hour_${year}${month}`
  }
}

/** 加载图表数据 */
function loadChartData() {
  if (!chartQueryParams.dspSlotId) {
    proxy.$modal.msgWarning("请先选择预算广告位")
    return
  }

  // 根据表类型计算日期范围
  const now = new Date()
  let beginDate, endDate

  if (chartQueryParams.tableType === 'day') {
    // 天表：获取最近10天
    const endDateObj = new Date(now)
    const beginDateObj = new Date(now)
    beginDateObj.setDate(beginDateObj.getDate() - 9) // 最近10天（包括今天）

    endDate = endDateObj.getFullYear().toString() +
              String(endDateObj.getMonth() + 1).padStart(2, '0') +
              String(endDateObj.getDate()).padStart(2, '0')
    beginDate = beginDateObj.getFullYear().toString() +
                String(beginDateObj.getMonth() + 1).padStart(2, '0') +
                String(beginDateObj.getDate()).padStart(2, '0')
  } else {
    // 小时表：获取最近10小时
    const endDateObj = new Date(now)
    const beginDateObj = new Date(now)
    beginDateObj.setHours(beginDateObj.getHours() - 9) // 最近10小时（包括当前小时）

    endDate = endDateObj.getFullYear().toString() +
              String(endDateObj.getMonth() + 1).padStart(2, '0') +
              String(endDateObj.getDate()).padStart(2, '0') +
              String(endDateObj.getHours()).padStart(2, '0')
    beginDate = beginDateObj.getFullYear().toString() +
                String(beginDateObj.getMonth() + 1).padStart(2, '0') +
                String(beginDateObj.getDate()).padStart(2, '0') +
                String(beginDateObj.getHours()).padStart(2, '0')
  }

  // 构建查询参数
  const params = {
    pageNum: 1,
    pageSize: 100, // 获取足够的数据
    dspSlotId: chartQueryParams.dspSlotId,
    beginDate: beginDate,
    endDate: endDate,
    tableName: generateChartTableName()
  }

  // 显示加载状态
  const loading = proxy.$loading({
    lock: true,
    text: '正在加载图表数据...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  // 调用接口获取数据
  listDataDspSlot(params).then(response => {
    loading.close()
    const chartData = response.rows || []

    if (chartData.length === 0) {
      proxy.$modal.msgWarning("暂无数据")
      return
    }

    // 按日期排序
    chartData.sort((a, b) => {
      return (a.date || '').toString().localeCompare((b.date || '').toString())
    })

    // 格式化日期标签
    const dates = chartData.map(item => {
      const dateValue = item.date
      if (!dateValue) return '-'
      const dateStr = dateValue.toString()
      // yyyyMMddHH -> MM-dd HH:00
      if (dateStr.length === 10) {
        return `${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)} ${dateStr.substring(8, 10)}:00`
      }
      // yyyyMMdd -> MM-dd
      if (dateStr.length === 8) {
        return `${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)}`
      }
      return dateStr
    })

    // 提取填充率和展现率数据
    const fillRates = chartData.map(item =>
      item.fillRate !== null && item.fillRate !== undefined ? item.fillRate.toFixed(2) : 0
    )

    const showRates = chartData.map(item =>
      item.showRate !== null && item.showRate !== undefined ? item.showRate.toFixed(2) : 0
    )

    // 更新图表
    chartInstance.setOption({
      title: {
        text: chartQueryParams.tableType === 'day' ? '填充率与展现率趋势（最近10天）' : '填充率与展现率趋势（最近10小时）',
        left: 'center',
        top: 10
      },
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
  }).catch(() => {
    loading.close()
  })
}

getList()
getCompanyList()
getDspSlotList()
</script>
<style scoped>
.btn-blue {
  background-color: #2A5FB7 !important;
  border-color: #2A5FB7 !important;
  color: #fff !important;
}

.btn-blue:hover {
  background-color: #1f4f96 !important;
  border-color: #1f4f96 !important;
}

.btn-regge {
  background-color: #DCA550 !important;
  border-color: #f1b965 !important;
  color: #fff !important;
}

.btn-regge:hover {
  background-color: #df9318 !important;
  border-color: #df9318 !important;
}

.el-form--inline .el-form-item {
  margin-right: 8px;  /* 默认一般是 18px+ */
  margin-bottom: 8px;
}


.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
}



.status-wrap {
  display: inline-flex;
  align-items: center;
  white-space: nowrap; /* 关键：禁止换行 */
}

.status-dot {
  margin-right: 6px;
}

/* Drawer 头部样式 */
.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0;
  background: #fff;
}


.drawer-header {
  display: flex;
  flex-direction: column;   /* 👈 改成纵向 */
  align-items: flex-start;
  background: #fff;
}

/* 标题 */
.drawer-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 15px 15px 8px;
}

/* 按钮区域 */
.drawer-actions {
  display: flex;
  gap: 8px;
  padding: 0 15px 10px;
}

/* 确保 Drawer 头部使用白色背景 */
:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 0;
  background: #fff;
}

/* 确保 Drawer body 有正确的背景色 */
:deep(.el-drawer__body) {
  padding: 16px;
  background: #f2f5f7;
}


</style>
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"  label-width="68px"
             :label-style="{ color: '#000' }">
      <el-form-item  prop="companyId">
        <el-select
          v-model="queryParams.companyId"
          placeholder="公司名称"
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
      <el-form-item prop="osType">
        <el-select
          v-model="queryParams.osType"
          placeholder="应用平台"
          clearable
          style="width: 160px;"
        >
          <el-option label="Android" :value="1" />
          <el-option label="iOS" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item prop="adType">
        <el-select
          v-model="queryParams.adType"
          placeholder="广告类型"
          clearable
          filterable
          style="width: 160px;"
        >
          <el-option
            v-for="item in adTypeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="productId">
        <el-select
          v-model="queryParams.productId"
          placeholder="产品管理"
          clearable
          filterable
          style="width: 160px;"
        >
          <el-option
            v-for="item in filteredProductList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label-width="100" prop="dspSlotId">
        <el-select
          v-model="queryParams.dspSlotId"
          placeholder="预算广告位"
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
          type="textarea"
          :rows="2"
          placeholder="批量预算广告位ID(逗号/换行分隔)"
          clearable
          style="width: 180px;"
        />
      </el-form-item>
      <el-form-item prop="dspPayType">
        <el-select
          v-model="queryParams.dspPayType"
          placeholder="结算方式"
          clearable
          style="width: 160px;"
        >
          <el-option label="分成" :value="1" />
          <el-option label="RTB" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item prop="dateRange">
        <el-date-picker
          v-if="tableType === 'day'"
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYYMMDD"
          style="width: 240px;"
        />
        <el-date-picker
          v-else
          v-model="dateRange"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYYMMDDHH"
          format="YYYY-MM-DD HH:00"
          style="width: 320px;"
        />
      </el-form-item>
      <el-form-item>
        <el-button class="btn-blue" type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-radio-group v-model="tableType" @change="handleTableTypeChange">
          <el-radio-button label="day">日报表</el-radio-button>
          <el-radio-button label="hour">小时报表</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="1.5">
        <el-button
            class="btn-blue"
          type="success"
          plain
          icon="TrendCharts"
          @click="handleShowChart"
        >查看折线数据</el-button>
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

    <el-table
              ref="mainTableRef"
              v-loading="loading"
              :data="dataDspSlotList"
              @selection-change="handleSelectionChange"
              @expand-change="handleExpandChange"
              @row-click="handleRowClick"
              style="width: 100%"
              :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
              :cell-style="{ color: '#000' }"
              border
              table-layout="auto"
              highlight-current-row="true"
    >
      <el-table-column type="expand" width="50">
        <template #default="scope">
          <div class="subtable-wrapper">
            <el-table
              v-loading="isSubTableLoading(scope.row)"
              :data="getSubTableRows(scope.row)"
              size="small"
              border
              style="width: 100%"
            >
              <el-table-column label="时间" align="center" width="150">
                <template #default="subScope">
                  <span>{{ formatDate(subScope.row.date) }}</span>
                </template>
              </el-table-column>

              <el-table-column label="媒体名称" align="center" prop="mediaName" width="250" />
              <el-table-column label="应用名称" align="center" prop="appName" width="200" />
              <el-table-column label="广告名称" align="center" width="200" show-overflow-tooltip>
                <template #default="subScope">
                  <span v-if="subScope.row.sspAliseName">{{ subScope.row.sspAliseName }}（{{ subScope.row.sspSlotId }}）</span>
                  <span v-else>{{ subScope.row.sspSlotId }}</span>
                </template>
              </el-table-column>
              <el-table-column label="广告位ID" align="center" prop="sspSlotId" width="100" />

              <el-table-column label="请求" align="center" prop="reqPv" width="100" />
              <el-table-column label="返回" align="center" prop="retPv" width="100" />
              <el-table-column label="展示" align="center" prop="showPv" width="100" />
              <el-table-column label="点击" align="center" prop="clickPv" width="100" />

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

            </el-table>
            <el-empty v-if="!isSubTableLoading(scope.row) && getSubTableRows(scope.row).length === 0" description="未查询到子表数据" />
          </div>
        </template>
      </el-table-column>
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
      <el-table-column label="请求" align="center" width="120" prop="reqPv" />
      <el-table-column label="返回" align="center" width="120" prop="retPv" />
      <el-table-column label="展示" align="center" width="120" prop="showPv" />
      <el-table-column label="点击" align="center" width="120" prop="clickPv" />

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
import {listData_ssp_slot, listData_ssp_slot_list} from "@/api/data/dataSspSlot"
import { listInfo } from "@/api/budget/info"
import { listCompany } from "@/api/budget/company"
import { listProduct } from "@/api/budget/product"
import { listType } from "@/api/ad/type"
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const chartRef = ref(null)
let chartInstance = null

const dataDspSlotList = ref([])
const mainTableRef = ref(null)
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
const productList = ref([]) // 产品列表
const adTypeList = ref([]) // 广告类型列表
const loadingSubMap = ref({})
const subTableMap = ref({})
const expandedRowKeys = ref(new Set())
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

const filteredProductList = computed(() => {
  const companyId = queryParams.value.companyId
  if (!companyId) {
    return productList.value
  }
  const companyIdNumber = Number(companyId)
  return productList.value.filter(item => {
    const productCompanyId = Number(item.companyId ?? item.company_id)
    return productCompanyId === companyIdNumber
  })
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    companyId: null,
    osType: null,
    adType: null,
    productId: null,
    dspSlotId: null,
    dspSlotCode: null,
    dspSlotCodeList: null,
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

/** 获取产品列表 */
function getProductList() {
  listProduct({ pageNum: 1, pageSize: 1000 }).then(response => {
    productList.value = response.rows || []
  })
}

/** 获取广告类型列表 */
function getAdTypeList() {
  listType({ pageNum: 1, pageSize: 1000 }).then(response => {
    adTypeList.value = response.rows || []
  })
}

/** 公司选择变化处理 */
function handleCompanyChange() {
  // 清空公司相关筛选
  queryParams.value.productId = null
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

function normalizeBatchValue(value) {
  if (value === null || value === undefined || value === '') {
    return null
  }
  const parts = String(value)
    .split(/[\n,，\s]+/)
    .map(item => item.trim())
    .filter(item => item !== '')
  return parts
}

function buildQueryParams() {
  const params = { ...queryParams.value }
  const codeList = normalizeBatchValue(params.dspSlotCode)
  if (codeList && codeList.length > 0) {
    params.dspSlotCodeList = codeList
    if (codeList.length === 1) {
      params.dspSlotCode = codeList[0]
    } else {
      params.dspSlotCode = null
    }
  } else {
    params.dspSlotCodeList = null
  }
  return params
}

function getRowKey(row) {
  return `${row?.dspSlotId || ''}-${row?.dspSlotCode || ''}-${row?.date || ''}-${row?.id || ''}`
}

function getSubTableRows(row) {
  return subTableMap.value[getRowKey(row)] || []
}

function isSubTableLoading(row) {
  return !!loadingSubMap.value[getRowKey(row)]
}

function generateDspTableNameByRow(row) {
  const dateValue = String(row?.date || '')
  if (dateValue.length < 6) {
    return null
  }
  const yearMonth = dateValue.substring(0, 6)
  return tableType.value === 'day'
    ? `data_dsp_slot_day_${yearMonth}`
    : `data_dsp_slot_hour_${yearMonth}`
}

function generateSspTableNameByRow(row) {
  const dateValue = String(row?.date || '')
  if (dateValue.length < 6) {
    return null
  }
  const yearMonth = dateValue.substring(0, 6)
  return tableType.value === 'day'
    ? `data_ssp_slot_day_${yearMonth}`
    : `data_ssp_slot_hour_${yearMonth}`
}

function toggleRowExpand(row) {
  const key = getRowKey(row)
  const expanded = expandedRowKeys.value.has(key)
  if (expanded) {
    expandedRowKeys.value.delete(key)
  } else {
    expandedRowKeys.value.add(key)
  }
  if (mainTableRef.value?.toggleRowExpansion) {
    mainTableRef.value.toggleRowExpansion(row, !expanded)
  }
}

function handleRowClick(row) {
  toggleRowExpand(row)
}

async function handleExpandChange(row, expandedRows) {
  const key = getRowKey(row)
  const expanded = expandedRows.some(item => getRowKey(item) === key)
  if (!expanded) {
    expandedRowKeys.value.delete(key)
    return
  }
  expandedRowKeys.value.add(key)

  const dspSlotId = row?.dspSlotId
  if (!dspSlotId) {
    return
  }
  if (subTableMap.value[key] || loadingSubMap.value[key]) {
    return
  }

  const tableName = generateSspTableNameByRow(row)
  if (!tableName) {
    subTableMap.value[key] = []
    return
  }

  loadingSubMap.value[key] = true
  try {
    const response = await listData_ssp_slot_list({
      pageNum: 1,
      pageSize: 1000,
      tableName,
      dspSlotId,
      beginDate: row?.date,
      endDate: row?.date
    })
    subTableMap.value[key] = response?.rows || []
  } catch (error) {
    subTableMap.value[key] = []
    proxy.$modal.msgError(`检索子表失败（dsp_slot_id=${dspSlotId}）`)
  } finally {
    loadingSubMap.value[key] = false
  }
}

/** 查询预算报表列表 */
function getList() {
  loading.value = true
  expandedRowKeys.value = new Set()
  subTableMap.value = {}
  loadingSubMap.value = {}
  const params = buildQueryParams()

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
  const params = buildQueryParams()
  if (dateRange.value && dateRange.value.length === 2) {
    params.beginDate = dateRange.value[0]
    params.endDate = dateRange.value[1]
  }
  params.tableName = generateTableName()
  delete params.pageNum
  delete params.pageSize

  const dateFlag = new Date().toISOString().slice(0, 10).replace(/-/g, '')
  proxy.download('system/dataDspSlot/export', {
    ...params
  }, `dataDspSlot_${tableType.value}_${dateFlag}.xlsx`)
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
getProductList()
getAdTypeList()
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

.subtable-wrapper {
  padding: 12px 8px;
  background: #fff4bf;
}

.subtable-wrapper :deep(.el-table) {
  font-family: "PingFang SC", "Microsoft YaHei", "Helvetica Neue", Arial, sans-serif;
  color: #111111;
}

.subtable-wrapper :deep(.el-table th.el-table__cell),
.subtable-wrapper :deep(.el-table th) {
  background-color: #ffe89a;
  color: #111111;
  font-weight: 700;
}

.subtable-wrapper :deep(.el-table td.el-table__cell),
.subtable-wrapper :deep(.el-table td) {
  background-color: #fff7d6;
  color: #111111;
  font-weight: 500;
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

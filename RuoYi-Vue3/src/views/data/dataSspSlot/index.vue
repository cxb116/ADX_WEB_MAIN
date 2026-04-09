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
      <el-table-column label="结算方式" align="center" width="100" prop="sspPayType">
        <template #default="scope">
          <dict-tag :options="ssp_pay_type" :value="String(scope.row.sspPayType)" />
        </template>
      </el-table-column>
      <el-table-column label="分成系数(%)" align="center" width="120" prop="sspDealRatio">
        <template #default="scope">
          <span v-if="scope.row.sspPayType === 1 && scope.row.sspDealRatio !== null && scope.row.sspDealRatio !== undefined">
            {{ scope.row.sspDealRatio }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="千次收益(分)" align="center" width="120" prop="sspEcpm">
        <template #default="scope">
          <span v-if="scope.row.sspPayType === 1 && scope.row.sspEcpm !== null && scope.row.sspEcpm !== undefined">
            {{ scope.row.sspEcpm }}
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template #default="scope">
<!--          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['data:data_ssp_slot:edit']">修改</el-button>-->
          <el-button link type="warning" icon="Correct" @click="handleCorrect(scope.row)">修正</el-button>
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['data:data_ssp_slot:remove']">删除</el-button>-->
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

    <!-- 修正对话框 -->
    <el-dialog
      v-model="correctDialogVisible"
      title="修正流水"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="correctForm" :rules="correctRules" ref="correctFormRef" label-width="120px">
        <el-form-item label="日期">
          <el-input v-model="correctForm.date" disabled />
        </el-form-item>
        <el-form-item label="媒体广告位">
          <el-input v-model="correctForm.sspSlotName" disabled />
        </el-form-item>
        <el-form-item label="预算广告位编号">
          <el-input v-model="correctForm.dspSlotCode" disabled />
        </el-form-item>
        <el-form-item label="结算方式" prop="sspPayType">
          <el-radio-group v-model="correctForm.sspPayType">
            <el-radio :label="1">分成</el-radio>
            <el-radio :label="2">RTB</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分成系数(%)" prop="sspDealRatio">
          <el-input-number
            v-model="correctForm.sspDealRatio"
            :min="0"
            :max="100"
            :precision="2"
            placeholder="请输入分成系数"
            :disabled="correctForm.sspPayType !== 1"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="千次收益(分)" prop="sspEcpm">
          <el-input-number
            v-model="correctForm.sspEcpm"
            :min="0"
            :precision="0"
            placeholder="请输入千次收益"
            :disabled="correctForm.sspPayType !== 1"
            style="width: 100%"
          />
        </el-form-item>
        <el-divider>计算结果</el-divider>
        <el-form-item label="展示PV">
          <el-input v-model="correctForm.showPv" disabled />
        </el-form-item>
        <el-form-item label="预算流水(分)">
          <el-input v-model="correctForm.spend" disabled />
        </el-form-item>
        <el-form-item label="利润收入(分)">
          <el-input v-model="correctForm.income" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelCorrect">取消</el-button>
          <el-button type="primary" @click="submitCorrect">确定</el-button>
        </div>
      </template>
    </el-dialog>

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
import { useDict } from "@/utils/dict"
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const { ssp_pay_type } = useDict('ssp_pay_type')
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
const correctDialogVisible = ref(false) // 修正对话框显示状态
// 修正表单
const correctForm = ref({
  id: null,
  tableName: '',
  date: '',
  sspSlotName: '',
  dspSlotCode: '',
  sspPayType: 1,
  sspDealRatio: null,
  sspEcpm: null,
  showPv: 0,
  spend: 0,
  income: 0
})
// 修正表单验证规则
const correctRules = {
  sspPayType: [
    { required: true, message: '结算方式不能为空', trigger: 'change' }
  ],
  sspDealRatio: [
    {
      validator: (_rule, value, callback) => {
        // 如果结算方式选择分成，则分成系数必填
        if (correctForm.value.sspPayType === 1 && !value) {
          callback(new Error('分成系数不能为空'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  sspEcpm: [
    {
      validator: (_rule, value, callback) => {
        // 如果结算方式选择分成，则千次收益必填
        if (correctForm.value.sspPayType === 1 && !value) {
          callback(new Error('千次收益不能为空'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

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
    data_ssp_slotList.value = response.rows.map(item => {
      // 如果是分成模式，自动计算预算流水和利润收入
      if (item.sspPayType === 1 && item.showPv && item.sspEcpm) {
        // 预算流水（分）= 展示PV × 千次收益（分） / 1000
        const spend = Math.floor(item.sspEcpm * item.showPv / 1000)
        item.spend = spend
        // 利润收入（分）= 预算流水（分）× 分成系数（%） / 100
        const income = item.sspDealRatio ? Math.floor(spend * item.sspDealRatio / 100) : 0
        item.income = income
      }
      return item
    })
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

/** 修正按钮操作 */
function handleCorrect(row) {
  // 重置修正表单
  correctForm.value = {
    id: row.id,
    tableName: generateTableName(),
    date: row.date,
    sspSlotName: row.sspSlotName || `${row.sspSlotId}`,
    dspSlotCode: row.dspSlotCode || '',
    sspPayType: row.sspPayType || 1,
    sspDealRatio: row.sspDealRatio || null,
    sspEcpm: row.sspEcpm || null,
    showPv: row.showPv || 0,
    spend: row.spend || 0,
    income: row.income || 0
  }
  // 清除验证状态
  if (proxy.$refs["correctFormRef"]) {
    proxy.$refs["correctFormRef"].clearValidate()
  }
  correctDialogVisible.value = true
}

/** 取消修正 */
function cancelCorrect() {
  correctDialogVisible.value = false
}

/** 提交修正 */
function submitCorrect() {
  proxy.$refs["correctFormRef"].validate(valid => {
    if (valid) {
      // 如果是分成模式，重新计算成本和收入
      if (correctForm.value.sspPayType === 1) {
        // 预算流水（分）= 展示PV × 千次收益（分） / 1000
        const newSpend = Math.floor(correctForm.value.sspEcpm * (correctForm.value.showPv || 0) / 1000)
        correctForm.value.spend = newSpend
        // 利润收入（分）= 预算流水（分）× 分成系数（%） / 100
        const newIncome = Math.floor(newSpend * (correctForm.value.sspDealRatio || 0) / 100)
        correctForm.value.income = newIncome
      }

      // 调用更新接口
      updateData_ssp_slot({
        id: correctForm.value.id,
        tableName: correctForm.value.tableName,
        spend: correctForm.value.spend,
        income: correctForm.value.income
      }).then(() => {
        proxy.$modal.msgSuccess("修正成功")
        correctDialogVisible.value = false
        getList()
      })
    }
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

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true"
             v-show="showSearch"
             label-width="68px"
             :label-style="{ color: '#000' }"
    >
      <el-form-item  prop="mediaId">
        <el-select
          v-model="queryParams.mediaId"
          placeholder="媒体简称"
          clearable
          filterable
          style="width: 120px;"
        >
          <el-option
            v-for="item in mediaList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="appId">
        <el-select
          v-model="queryParams.appId"
          placeholder="应用名称"
          clearable
          filterable
          style="width: 120px;"
        >
          <el-option
            v-for="item in appList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="appOsType">
        <el-select
          v-model="queryParams.appOsType"
          placeholder="应用平台"
          clearable
          style="width: 120px;"
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
          style="width: 120px;"
        >
          <el-option
            v-for="item in adTypeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label-width="90" prop="sspSlotId">
        <el-select
          v-model="queryParams.sspSlotId"
          placeholder="流量广告位"
          clearable
          filterable
          style="width: 120px;"
        >
          <el-option
            v-for="item in mediaAdList"
            :key="item.id"
            :label="`${item.name || '流量广告位'}(${item.id})`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label-width="90" prop="sspSlotIds">
        <el-input
          v-model="queryParams.sspSlotIds"
          type="textarea"
          :rows="2"
          placeholder="批量流量广告位ID(逗号/换行分隔)"
          clearable
          style="width: 180px;"
        />
      </el-form-item>
      <el-form-item label-width="110" prop="sspAliseNames">
        <el-input
          v-model="queryParams.sspAliseNames"
          type="textarea"
          :rows="2"
          placeholder="批量内部广告位名称(逗号/换行分隔)"
          clearable
          style="width: 200px;"
        />
      </el-form-item>

      <el-form-item label-width="90">
        <el-date-picker
          v-model="dateRange"
          :value-format="tableType === 'day' ? 'YYYYMMDD' : 'YYYYMMDDHH'"
          :type="tableType === 'day' ? 'daterange' : 'datetimerange'"
          range-separator="-"
          :start-placeholder="tableType === 'day' ? '开始日期' : '开始时间'"
          :end-placeholder="tableType === 'day' ? '结束日期' : '结束时间'"
          :format="tableType === 'day' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:00'"
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item>
        <el-button class="btn-blue"  type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button class="btn-blue"  icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-radio-group v-model="tableType" class="btn-blue" @change="handleTableTypeChange">
          <el-radio-button  class="btn-blue" label="day">日报表</el-radio-button>
          <el-radio-button  class="btn-blue" label="hour">小时报表</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="TrendCharts"
          @click="handleShowChart"
          class="btn-blue"
        >查看折现数据</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['data:data_ssp_slot:export']"
          class="btn-regge"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
              ref="mainTableRef"
              v-loading="loading"
              :data="data_ssp_slotList"
              @selection-change="handleSelectionChange"
              @expand-change="handleExpandChange"
              @row-click="handleRowClick"
              style="width: 100%"
              :header-cell-style="{ background: '#F5F7FA', color: '#00030b' }"
              :cell-style="{ color: '#000309' }"
              border
              table-layout="auto"

              highlight-current-row="true">
      <el-table-column type="expand" width="50">
        <template #default="scope">
          <div class="dsp-subtable-wrapper">
            <el-table
              v-loading="isSubTableLoading(scope.row)"
              :data="getSubTableRows(scope.row)"
              size="small"
              border
              style="width: 100%"
            >
              <el-table-column label="时间" align="center" width="160">
                <template #default="subScope">
                  <span>{{ formatDate(subScope.row.date) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="公司名称" align="center" prop="dspCompany" width="130" />
              <el-table-column label="产品名称" align="center" prop="productName" width="130" />
              <el-table-column label="广告类型" align="center" prop="adTypeName" width="130" />
              <el-table-column label="应用系统" align="center" prop="osType" width="130">
                <template #default="{ row }">
                  <!-- 根据 osType 的值显示 Android 或 iOS -->
                  {{ row.osType === 1 ? 'Android' : row.osType === 2 ? 'iOS' : '未知' }}
                </template>
              </el-table-column>


              <el-table-column label="预算广告位" align="center" width="260" show-overflow-tooltip>
                <template #default="subScope">
                  <span v-if="subScope.row.dspSlotName">{{ subScope.row.dspSlotName }}({{ subScope.row.dspSlotId }})</span>
                  <span v-else>{{ subScope.row.dspSlotId }}</span>
                </template>
              </el-table-column>
              <el-table-column label="预算广告位编码" align="center" prop="dspSlotCode" width="250" show-overflow-tooltip />
              <el-table-column label="请求" align="center" prop="reqPv" width="100" />
              <el-table-column label="返回" align="center" prop="retPv" width="100" />
              <el-table-column label="展示" align="center" prop="showPv" width="100" />
              <el-table-column label="点击" align="center" prop="clickPv" width="100" />

              <el-table-column label="丢弃请求" align="center" prop="discard" width="100" />
              <el-table-column label="请求丢失率(%)" align="center" prop="requestLossRate" width="130" />
              <el-table-column label="展现率(%)" align="center" prop="showRate" width="120" />
              <el-table-column label="点击率(%)" align="center" prop="clickRate" width="120" />
              <el-table-column label="填充率(%)" align="center" prop="fillRate" width="120" />
              <el-table-column label="ecpm" align="center" prop="ecpm" width="100" />



              <el-table-column label="成本(元)" align="center" width="120">
                <template #default="subScope">
                  <span>{{ subScope.row.spend !== null && subScope.row.spend !== undefined ? (subScope.row.spend / 100).toFixed(2) : '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="收入(元)" align="center" width="120">
                <template #default="subScope">
                  <span>{{ subScope.row.income !== null && subScope.row.income !== undefined ? (subScope.row.income / 100).toFixed(2) : '-' }}</span>
                </template>
              </el-table-column>
            </el-table>
<!--            <el-empty v-if="!isSubTableLoading(scope.row) && getSubTableRows(scope.row).length === 0" description="未查询到子表数据" />-->
          </div>
        </template>
      </el-table-column>
      <el-table-column label="日期" align="center" width="150" prop="date">
        <template #default="scope">
          <span v-if="scope.row.date">{{ formatDate(scope.row.date) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="媒体" align="center" width="150" show-overflow-tooltip  prop="mediaName">
        <template #default="scope">
          <span v-if="scope.row.mediaName">{{ scope.row.mediaName }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="应用平台" align="center" width="150" prop="appOsType">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.appOsType === 1 ? 'Android' : 'iOS' }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="应用平台" align="center" width="100" prop="appOsType">
        <template #default="scope">
          <span>{{ scope && scope.row ? (scope.row.appOsType === 1 ? 'Android' : (scope.row.appOsType === 2 ? 'iOS' : '未知')) : '未知' }}</span>
        </template>
      </el-table-column>

      <el-table-column label="应用名称" align="center" width="250" show-overflow-tooltip  prop="appName">
      </el-table-column>
      <el-table-column label="内部广告位名称" align="center" width="250" show-overflow-tooltip  prop="sspSlotName">
        <template #default="scope">
          <span v-if="scope.row.sspAliseName">{{ scope.row.sspAliseName }}（{{ scope.row.sspSlotId }}）</span>
          <span v-else>{{ scope.row.sspSlotId }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="预算广告位" align="center" width="280" show-overflow-tooltip  prop="dspSlotName">-->
<!--        <template #default="scope">-->
<!--          <span v-if="scope.row.dspSlotName">{{ scope.row.dspSlotName }}（{{ scope.row.dspSlotId }}）</span>-->
<!--          <span v-else>{{ scope.row.dspSlotId }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="广告类型" align="center" width="100" prop="adType">
        <template #default="scope">
          <span>{{ getAdTypeName(scope.row.adType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="媒体广告位" align="center" width="150" show-overflow-tooltip  prop="sspSlotId" />
      <el-table-column label="请求" align="center" width="100" prop="reqPv" />
      <el-table-column label="返回" align="center" width="100" prop="retPv" />
      <el-table-column label="展示" align="center" width="100" prop="showPv" />
      <el-table-column label="点击" align="center" width="100" prop="clickPv" />
      <el-table-column label="丢弃请求" align="center" width="100" prop="discard" />
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
<!--      <el-table-column label="千次收益(分)" align="center" width="120" prop="sspEcpm">-->
<!--        <template #default="scope">-->
<!--          <span v-if="scope.row.sspPayType === 1 && scope.row.sspEcpm !== null && scope.row.sspEcpm !== undefined">-->
<!--            {{ scope.row.sspEcpm }}-->
<!--          </span>-->
<!--          <span v-else>-</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="成本(元)" align="center" width="120" prop="spend">
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
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">-->
<!--        <template #default="scope">-->
<!--&lt;!&ndash;          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['data:data_ssp_slot:edit']">修改</el-button>&ndash;&gt;-->
<!--          <el-button link type="warning" icon="Correct" @click="handleCorrect(scope.row)">修正</el-button>-->
<!--&lt;!&ndash;          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['data:data_ssp_slot:remove']">删除</el-button>&ndash;&gt;-->
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
import { listDataDspSlot } from "@/api/data/dataDspSlot.js"
import { listMedia } from "@/api/flow/media.js"
import { listApp } from "@/api/flow/app.js"
import { listMediaAd } from "@/api/flow/mediaAd.js"
import { listType } from "@/api/ad/type"
import { useDict } from "@/utils/dict"
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const { ssp_pay_type } = useDict('ssp_pay_type')
const chartRef = ref(null)
let chartInstance = null

const data_ssp_slotList = ref([])
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
const mediaList = ref([]) // 媒体列表
const appList = ref([]) // 应用列表
const mediaAdList = ref([]) // 媒体广告位列表
const adTypeList = ref([]) // 广告类型列表
const loadingDetailMap = ref({})
const dspSubTableMap = ref({})
const expandedRowKeys = ref(new Set())
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
    appOsType: null,
    adType: null,
    sspSlotId: null,
    sspSlotIds: null,
    sspAliseNames: null,
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

/** 获取媒体广告位列表 */
function getMediaAdList() {
  listMediaAd({ pageNum: 1, pageSize: 1000 }).then(response => {
    mediaAdList.value = response.rows || []
  })
}

/** 获取广告类型列表 */
function getAdTypeList() {
  listType({ pageNum: 1, pageSize: 1000 }).then(response => {
    adTypeList.value = response.rows || []
  })
}

/** 根据广告类型ID获取广告类型名称 */
function getAdTypeName(typeId) {
  if (typeId === null || typeId === undefined || typeId === '') {
    return '-'
  }
  const type = adTypeList.value.find(item => String(item.id) === String(typeId))
  return type ? type.name : typeId
}

function getRowKey(row) {
  return `${row?.sspSlotId || ''}-${row?.date || ''}-${row?.id || ''}`
}

function getSubTableRows(row) {
  const key = getRowKey(row)
  return dspSubTableMap.value[key] || []
}

function isSubTableLoading(row) {
  const key = getRowKey(row)
  return !!loadingDetailMap.value[key]
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

  const sspSlotId = row?.sspSlotId
  if (!sspSlotId) {
    return
  }
  if (dspSubTableMap.value[key] || loadingDetailMap.value[key]) {
    return
  }

  const tableName = generateDspTableNameByRow(row)
  if (!tableName) {
    dspSubTableMap.value[key] = []
    return
  }

  loadingDetailMap.value[key] = true
  try {
    const response = await listDataDspSlot({
      pageNum: 1,
      pageSize: 1000,
      tableName,
      sspSlotId,
      beginDate: row?.date,
      endDate: row?.date
    })
    dspSubTableMap.value[key] = response?.rows || []
  } catch (error) {
    dspSubTableMap.value[key] = []
    proxy.$modal.msgError(`获取子表数据失败（sspSlotId=${sspSlotId}）`)
  } finally {
    loadingDetailMap.value[key] = false
  }
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

function normalizeBatchValue(value) {
  if (value === null || value === undefined || value === '') {
    return null
  }
  const parts = String(value)
    .split(/[\n,，\s]+/)
    .map(item => item.trim())
    .filter(item => item !== '')
  return parts.length > 0 ? parts.join(',') : null
}

function buildQueryParams() {
  const params = { ...queryParams.value }
  params.sspSlotIds = normalizeBatchValue(params.sspSlotIds)
  params.sspAliseNames = normalizeBatchValue(params.sspAliseNames)
  return params
}

/** 查询媒体数据报表列表 */
function getList() {
  loading.value = true
  expandedRowKeys.value = new Set()
  loadingDetailMap.value = {}
  dspSubTableMap.value = {}
  const params = buildQueryParams()

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
      console.log("data_ssp_slotList============== :",data_ssp_slotList)

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
    appOsType: null,
    sspSlotId: null,
    sspSlotIds: null,
    sspAliseNames: null,
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

// /** 修正按钮操作 */
// function handleCorrect(row) {
//   // 重置修正表单
//   correctForm.value = {
//     id: row.id,
//     tableName: generateTableName(),
//     date: row.date,
//     sspSlotName: row.sspSlotName || `${row.sspSlotId}`,
//     dspSlotCode: row.dspSlotCode || '',
//     sspPayType: row.sspPayType || 1,
//     sspDealRatio: row.sspDealRatio || null,
//     sspEcpm: row.sspEcpm || null,
//     showPv: row.showPv || 0,
//     spend: row.spend || 0,
//     income: row.income || 0
//   }
//   // 清除验证状态
//   if (proxy.$refs["correctFormRef"]) {
//     proxy.$refs["correctFormRef"].clearValidate()
//   }
//   correctDialogVisible.value = true
// }

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
    ...buildQueryParams(),
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
getMediaAdList()
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

.dsp-subtable-wrapper {
  padding: 12px 8px;
  background: #fff4bf;
}

.dsp-subtable-wrapper :deep(.el-table) {
  font-family: "PingFang SC", "Microsoft YaHei", "Helvetica Neue", Arial, sans-serif;
  color: #111111;
}

.dsp-subtable-wrapper :deep(.el-table th.el-table__cell) {
  background-color: #ffe89a;
  color: #111111;
  font-weight: 700;
}

.dsp-subtable-wrapper :deep(.el-table td.el-table__cell) {
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

/* 绿色（正常） */
.dot-success {
  background-color: #67c23a;
}

/* 红色（禁用/拒绝） */
.dot-danger {
  background-color: #f56c6c;
}

/* 黄色（审核中） */
.dot-warning {
  background-color: #e6a23c;
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

/* 设置表头字体颜色为黑色，背景颜色为黄色 */
.custom-column-header {
  background-color: yellow;
  color: black;
}

/* 设置表格数据的字体颜色为黑色，背景颜色为黄色 */
.custom-column-cell {
  background-color: yellow;
  color: black;
}

</style>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公司" prop="companyId">
        <el-select
          v-model="queryParams.companyId"
          placeholder="请选择公司"
          clearable
          filterable
          @change="handleCompanyChange"
        >
          <el-option
            v-for="item in companyList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预算广告位" label-width="100" prop="dspSlotId">
        <el-select
          v-model="queryParams.dspSlotId"
          placeholder="请选择预算广告位"
          clearable
          filterable
          :disabled="!queryParams.companyId"
        >
          <el-option
            v-for="item in filteredDspSlotList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预算广告位编码" label-width="120" prop="dspSlotCode">
        <el-input
          v-model="queryParams.dspSlotCode"
          placeholder="请输入预算广告位编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结算方式" prop="dspPayType">
        <el-select
          v-model="queryParams.dspPayType"
          placeholder="请选择结算方式"
          clearable
        >
          <el-option label="分成" :value="1" />
          <el-option label="RTB" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="日期" label-width="90">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYYMMDD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:dataDspSlot:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataDspSlotDetailList" @selection-change="handleSelectionChange">
      <el-table-column label="日期" align="center" width="150" prop="date">
        <template #default="scope">
          <span v-if="scope.row.date">{{ formatDate(scope.row.date) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="公司名称" align="center" width="150" prop="companyId">
        <template #default="scope">
          <span>{{ getCompanyNameByDspSlotId(scope.row.dspSlotId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品名称" align="center" width="150" prop="productId">
        <template #default="scope">
          <span>{{ getProductNameByDspSlotId(scope.row.dspSlotId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作系统" align="center" width="100" prop="osType">
        <template #default="scope">
          <span>{{ getOsTypeNameByDspSlotId(scope.row.dspSlotId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="广告类型" align="center" width="120" prop="adTypeId">
        <template #default="scope">
          <span>{{ getAdTypeNameByDspSlotId(scope.row.dspSlotId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预算广告位" align="center" width="200" prop="dspSlotName">
        <template #default="scope">
          <span v-if="scope.row.dspSlotName">{{ scope.row.dspSlotName }}（{{ scope.row.dspSlotId }}）</span>
          <span v-else>{{ scope.row.dspSlotId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预算广告位编号" align="center" width="270" prop="dspSlotCode" />
      <el-table-column label="展示PV" align="center" width="100" prop="showPv">
        <template #default="scope">
          <span>{{ scope.row.showPv || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="点击PV" align="center" width="100" prop="clickPv">
        <template #default="scope">
          <span>{{ scope.row.clickPv || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请求PV" align="center" width="100" prop="reqPv">
        <template #default="scope">
          <span>{{ scope.row.reqPv || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="丢弃请求" align="center" width="100" prop="discard">
        <template #default="scope">
          <span>{{ scope.row.discard || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="返回PV" align="center" width="100" prop="retPv">
        <template #default="scope">
          <span>{{ scope.row.retPv || '-' }}</span>
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
      <el-table-column label="结算方式" align="center" width="100" prop="dspPayType">
        <template #default="scope">
    <span>
      {{ scope.row.dspPayType === 1 ? '分成' : scope.row.dspPayType === 2 ? 'RTB' : '-' }}
    </span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" width="100" fixed="right">-->
<!--        <template #default="scope">-->
<!--          <el-button link type="primary" icon="Edit" @click="handleCorrectSpend(scope.row)">修正</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <!-- 修正成本对话框 -->
    <el-dialog
      v-model="correctDialogVisible"
      title="修正成本"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="correctForm" :rules="correctRules" ref="correctFormRef" label-width="100px">
        <el-form-item label="结算方式" prop="payType">
          <el-radio-group v-model="correctForm.payType">
            <el-radio :label="1">分成</el-radio>
            <el-radio :label="2">RTB</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="当前成本" prop="currentSpend">
          <el-input v-model="correctForm.currentSpend" disabled>
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="修正成本" prop="newSpend">
          <el-input-number
            v-model="correctForm.newSpend"
            :min="0"
            :precision="2"
            :step="0.01"
            style="width: 100%"
          />
          <span style="margin-left: 10px; color: #909399;">单位：元</span>
        </el-form-item>
        <el-form-item label="日期">
          <el-input v-model="correctForm.date" disabled />
        </el-form-item>
        <el-form-item label="预算广告位">
          <el-input v-model="correctForm.dspSlotName" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelCorrect">取消</el-button>
          <el-button type="primary" @click="submitCorrectSpend">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="DataDspSlotDetail">
import { listDataDspSlotDetail, getDataDspSlotDetail, delDataDspSlotDetail, addDataDspSlotDetail, updateDataDspSlotDetail } from "@/api/data/dataDspSlotDetail"
import { listInfo } from "@/api/budget/info"
import { listCompany } from "@/api/budget/company"
import { listProduct } from "@/api/budget/product"
import { listType } from "@/api/ad/type"

const { proxy } = getCurrentInstance()
const correctFormRef = ref(null)

const dataDspSlotDetailList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const dateRange = ref([])
const companyList = ref([]) // 公司列表
const dspSlotList = ref([]) // 预算广告位列表
const productList = ref([]) // 产品列表
const adTypeList = ref([]) // 广告类型列表
const correctDialogVisible = ref(false) // 修正对话框显示状态

// 修正表单
const correctForm = reactive({
  id: null,
  currentSpend: 0,
  newSpend: 0,
  date: '',
  dspSlotName: '',
  dspSlotId: null
})

const correctRules = {
  newSpend: [
    { required: true, message: '请输入修正成本', trigger: 'blur' }
  ]
}

// 根据选择的公司过滤预算广告位
const filteredDspSlotList = computed(() => {
  if (!queryParams.value.companyId) {
    return dspSlotList.value
  }
  return dspSlotList.value.filter(item => item.companyId === queryParams.value.companyId)
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

/** 根据公司ID获取公司名称 */
function getCompanyName(companyId) {
  const company = companyList.value.find(item => item.id === companyId || item.company_id === companyId)
  return company ? company.name : '-'
}

/** 根据预算广告位ID获取公司名称 */
function getCompanyNameByDspSlotId(dspSlotId) {
  if (!dspSlotId) return '-'
  // 先找到预算广告位
  const dspSlot = dspSlotList.value.find(item => item.id === dspSlotId)
  if (!dspSlot) return '-'
  // 通过公司ID找到公司名称
  return getCompanyName(dspSlot.companyId)
}

/** 根据预算广告位ID获取产品名称 */
function getProductNameByDspSlotId(dspSlotId) {
  if (!dspSlotId) return '-'
  // 先找到预算广告位
  const dspSlot = dspSlotList.value.find(item => item.id === dspSlotId)
  if (!dspSlot || !dspSlot.productId) return '-'
  // 再通过产品ID找到产品名称
  const product = productList.value.find(item => item.id === dspSlot.productId || item.product_id === dspSlot.productId)
  return product ? product.name : '-'
}

/** 根据预算广告位ID获取操作系统名称 */
function getOsTypeNameByDspSlotId(dspSlotId) {
  if (!dspSlotId) return '-'
  // 先找到预算广告位
  const dspSlot = dspSlotList.value.find(item => item.id === dspSlotId)
  if (!dspSlot) return '-'
  // osType: 1=Android，2=iOS
  if (dspSlot.osType === 1 || dspSlot.osType === '1') return 'Android'
  if (dspSlot.osType === 2 || dspSlot.osType === '2') return 'iOS'
  return '-'
}

/** 根据预算广告位ID获取广告类型名称 */
function getAdTypeNameByDspSlotId(dspSlotId) {
  if (!dspSlotId) return '-'
  // 先找到预算广告位
  const dspSlot = dspSlotList.value.find(item => item.id === dspSlotId)
  if (!dspSlot || !dspSlot.adTypeId) return '-'
  // 再通过广告类型ID找到广告类型名称
  const adType = adTypeList.value.find(item => item.id === dspSlot.adTypeId)
  return adType ? adType.name : '-'
}

/** 根据预算广告位ID获取结算方式名称 */
function getPayTypeName(dspSlotId) {
  if (!dspSlotId) return '-'
  // 先找到预算广告位
  const dspSlot = dspSlotList.value.find(item => item.id === dspSlotId)
  if (!dspSlot) return '-'
  // dspPayType: 1=分成，2=RTB
  if (dspSlot.dspPayType === 1 || dspSlot.dspPayType === '1') return '分成'
  if (dspSlot.dspPayType === 2 || dspSlot.dspPayType === '2') return 'RTB'
  return '-'
}

/** 修正成本按钮操作 */
function handleCorrectSpend(row) {
  correctForm.id = row.id
  correctForm.currentSpend = row.spend ? (row.spend / 100).toFixed(2) : '0.00'
  correctForm.newSpend = row.spend ? (row.spend / 100) : 0
  correctForm.payType = row.dspPayType || 1 // 默认分成
  correctForm.date = row.date ? formatDate(row.date) : ''
  correctForm.dspSlotName = row.dspSlotName || ''
  correctForm.dspSlotId = row.dspSlotId
  correctDialogVisible.value = true
}

/** 提交修正成本 */
function submitCorrectSpend() {
  proxy.$refs["correctFormRef"].validate(valid => {
    if (valid) {
      // 将元转换为分
      const newSpendInCents = Math.round(correctForm.newSpend * 100)

      // 使用数据的日期生成表名
      const tableName = generateTableName(correctForm.date)

      // 准备提交数据：只传 id、payType、spend、tableName
      const submitData = {
        id: correctForm.id,
        payType: correctForm.payType,
        spend: newSpendInCents,
        tableName: tableName
      }

      // 调用更新接口
      updateDataDspSlotDetail(submitData).then(response => {
        proxy.$modal.msgSuccess("修正成功")
        correctDialogVisible.value = false
        getList()
      }).catch(() => {
        proxy.$modal.msgError("修正失败")
      })
    }
  })
}

/** 取消修正 */
function cancelCorrect() {
  correctDialogVisible.value = false
  proxy.resetForm("correctFormRef")
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
  listProduct().then(response => {
    productList.value = response.rows || []
  })
}

/** 获取广告类型列表 */
function getAdTypeList() {
  listType({ pageNum: 1, pageSize: 100 }).then(response => {
    adTypeList.value = response.rows || []
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
  // 天表: data_dsp_slot_day_YYYYMM
  return `data_dsp_slot_day_${year}${month}`
}

/** 查询预算数据详细报表列表 */
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

  listDataDspSlotDetail(params).then(response => {
    dataDspSlotDetailList.value = response.rows
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
  proxy.resetForm("dataDspSlotDetailRef")
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
  title.value = "添加预算数据详细报表"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getDataDspSlotDetail(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预算数据详细报表"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dataDspSlotDetailRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateDataDspSlotDetail(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addDataDspSlotDetail(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除预算数据详细报表编号为"' + _ids + '"的数据项？').then(function() {
    return delDataDspSlotDetail(_ids)
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
  }, `dataDspSlotDetail_${new Date().getTime()}.xlsx`)
}

getList()
getCompanyList()
getDspSlotList()
getProductList()
getAdTypeList()
</script>

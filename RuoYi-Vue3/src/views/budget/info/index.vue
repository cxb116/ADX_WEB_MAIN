<template>
  <div class="app-container">
    <div class="app-container-list">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
             label-width="68px"
             :label-style="{ color: '#000' }"
    >
      <el-form-item prop="companyId">
        <el-select
          v-model="queryParams.companyId"
          placeholder="公司名称"
          clearable
          filterable
          @change="handleQueryCompanyChange"
          style="width: 150px;"
        >
          <el-option
            v-for="company in companyList"
            :key="company.id"
            :label="company.name"
            :value="company.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item prop="productId">
        <el-select
          v-model="queryParams.productId"
          placeholder="产品名称"
          clearable
          filterable
          style="width: 150px;"
        >
          <el-option
            v-for="product in filteredQueryProductList"
            :key="product.id"
            :label="product.name"
            :value="product.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item prop="osType">
        <el-select
            v-model="queryParams.osType"
            placeholder="应用平台"
            clearable
            style="width: 150px;"
        >
          <el-option
              v-for="dict in os_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="name" label-width="100">
        <el-select
          v-model="queryParams.name"
          placeholder="预算位名称"
          clearable
          filterable
          :loading="nameOptionsLoading"
          style="width: 180px;"
        >
          <el-option
            v-for="item in adSlotNameOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item  prop="adTypeId">
        <el-select
          v-model="queryParams.adTypeId"
          placeholder="请选择广告类型"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="item in adTypeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item  prop="adSceneId">
        <el-select
          v-model="queryParams.adSceneId"
          placeholder="请选择广告场景"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="item in adSceneList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item  prop="dspSlotCode" label-width="100">
        <el-input
          v-model="batchDspSlotCodeInput"
          type="textarea"
          :rows="2"
          resize="none"
          placeholder="预算方广告位（支持每行一个）"
          clearable
          style="width: 180px;"
        />
      </el-form-item>

      <el-form-item label-width="120px">
        <el-input
          v-model="batchBudgetSlotIdsInput"
          type="textarea"
          :rows="2"
          resize="none"
          placeholder="批量预算位ID（每行一个）"
          clearable
          style="width: 180px;"
        />
      </el-form-item>


      <el-form-item class="left-button">
        <el-button class="btn-blue" type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            class="btn-blue"
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['budget:info:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            class="btn-regge"
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['budget:info:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="infoList"
      @selection-change="handleSelectionChange"
      class="budget-table"
      style="width: 100%"
      :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
      :cell-style="{ color: '#000' }"
      border
      table-layout="auto"
      highlight-current-row="true"
    >
      <el-table-column type="selection" width="55" align="center" fixed />
      <el-table-column label="广告位名称" align="center" prop="name" width="250" fixed>
        <template #default="scope">
          {{ scope.row.name }}({{ scope.row.id }})
        </template>
      </el-table-column>
      <el-table-column label="产品" align="center" prop="productName" width="200">
        <template #default="scope">
          {{ scope.row.productName}}
        </template>
      </el-table-column>
      <el-table-column label="公司" align="center" prop="companyName" width="150">
        <template #default="scope">
          {{ scope.row.companyName }}
        </template>
      </el-table-column>
      <el-table-column label="操作系统" align="center" prop="osType" width="100">
        <template #default="scope">
          <dict-tag :options="os_type" :value="String(scope.row.osType)" />
        </template>
      </el-table-column>
      <el-table-column label="广告类型" align="center" prop="adTypeId" width="120">
        <template #default="scope">
          {{ getAdTypeName(scope.row.adTypeId) }}
        </template>
      </el-table-column>
      <el-table-column label="广告场景" align="center" prop="adSceneId" width="120">
        <template #default="scope">
          {{ scope.row.adSceneId ? getAdSceneName(scope.row.adSceneId) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="广告尺寸" align="center" prop="adSizeId" width="120">
        <template #default="scope">
          {{ scope.row.adSizeId ? getAdSizeName(scope.row.adSizeId) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="预算方广告位" align="center" prop="dspSlotCode" width="150" />
      <el-table-column label="预算方APPKEY" align="center" prop="dspAppKey" width="150" />
      <el-table-column label="预算方AppId" align="center" prop="dspAppId" width="150" />
      <el-table-column label="结算方式" align="center" prop="dsp_pay_type" width="100">
        <template #default="scope">
          <span v-if="scope.row.dsp_pay_type === 2 || scope.row.dsp_pay_type === '2'">RTB</span>
          <span v-else-if="scope.row.dsp_pay_type === 1 || scope.row.dsp_pay_type === '1'">分成</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="成交系数" align="center" prop="dsp_deal_ratio" width="100">
        <template #default="scope">
          <span v-if="scope.row.dsp_deal_ratio !== null && scope.row.dsp_deal_ratio !== undefined">{{ scope.row.dsp_deal_ratio }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" width="150" fixed="right" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['budget:info:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['budget:info:remove']">删除</el-button>
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
    </div>

    <el-drawer
      v-model="open"
      :title="title"
      direction="rtl"
      size="1200px"
      append-to-body
      destroy-on-close
    >
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="140px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="公司名称" prop="companyId">
                    <el-select
                      v-model="editForm.companyId"
                      placeholder="请选择公司"
                      clearable
                      filterable
                      style="width: 100%"
                      @change="handleEditCompanyChange"
                    >
                      <el-option
                        v-for="company in companyList"
                        :key="company.id"
                        :label="company.name"
                        :value="company.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="产品名称" prop="productId">
                    <el-select
                      v-model="editForm.productId"
                      placeholder="请选择产品"
                      clearable
                      filterable
                      style="width: 100%"
                    >
                      <el-option
                        v-for="product in filteredEditProductList"
                        :key="product.id"
                        :label="product.name"
                        :value="product.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="预算位名称" prop="name">
                    <el-input v-model="editForm.name" placeholder="请输入预算位名称" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="操作系统" prop="osType">
                    <el-select v-model="editForm.osType" placeholder="请选择操作系统" style="width: 100%">
                      <el-option
                          v-for="dict in os_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">

                <el-col :span="12">
                  <el-form-item label="广告类型" prop="adTypeId">
                    <el-select
                      v-model="editForm.adTypeId"
                      placeholder="请选择广告类型"
                      clearable
                      filterable
                      style="width: 100%"
                      @change="handleAdTypeChange"
                    >
                      <el-option
                        v-for="item in adTypeList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="广告场景" prop="adSceneId">
                    <el-select
                        v-model="editForm.adSceneId"
                        placeholder="请选择广告场景"
                        clearable
                        filterable
                        style="width: 100%"
                        :disabled="!editForm.adTypeId"
                    >
                      <el-option
                          v-for="item in filteredAdSceneList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">

                <el-col :span="12">
                  <el-form-item label="样式尺寸" prop="adSizeId">
                    <el-select
                      v-model="editForm.adSizeId"
                      placeholder="请选择样式尺寸"
                      clearable
                      filterable
                      style="width: 100%"
                      :disabled="!editForm.adTypeId"
                    >
                      <el-option
                        v-for="item in filteredAdSizeList"
                        :key="item.id"
                        :label="item.size"
                        :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="结算方式" prop="dspPayType">
                    <el-select v-model="editForm.dspPayType" placeholder="请选择结算方式" style="width: 100%" @change="handleDspPayTypeChange">
                      <el-option label="RTB" :value="2" />
                      <el-option label="分成" :value="1" />
                      <el-option label="固价" :value="3" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="RTB成交系数" prop="dspDealRatio">
                    <el-input
                        v-model="editForm.dspDealRatio"
                        placeholder="请输入RTB成交系数"
                        :disabled="editForm.dspPayType !== 2 && editForm.dspPayType !== '2'"
                    >
                      <template #append>%</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="预算方广告位ID" prop="dspSlotCode">
                    <el-input v-model="editForm.dspSlotCode" placeholder="请输入预算方广告位ID" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="预算方appKey" prop="dspAppKey">
                    <el-input v-model="editForm.dspAppKey" placeholder="请输入预算方appKey" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="应用版本号" prop="dspAppVer">
                    <el-input v-model="editForm.dspAppVer" placeholder="请输入应用版本号" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="预算方appSecret" prop="dspAppSecret">
                    <el-input v-model="editForm.dspAppSecret" placeholder="请输入预算方appSecret" />
                  </el-form-item>
                </el-col>
              </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用商店版本号" prop="dspAppStoreVer">
              <el-input v-model="editForm.dspAppStoreVer" placeholder="请输入应用商店版本号" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="预算方AppId" prop="dspAppId">
              <el-input v-model="editForm.dspAppId" placeholder="请输入预算方AppId" />
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item label="预算方AppId" prop="dspAppId">-->
<!--              <el-input v-model="editForm.dspAppId" placeholder="请输入预算方AppId" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                <el-form-item label="价格加密Key" prop="priceEncryptKey">
                  <el-input v-model="editForm.priceEncryptKey" placeholder="请输入价格加密Key" />
                </el-form-item>
              </el-col>
                <el-col :span="12">
                  <el-form-item label="预算端包名" prop="dspAppPkg">
                    <el-input v-model="editForm.dspAppPkg" placeholder="请输入预算端包名" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item label="备注" prop="remark">
                    <el-input v-model="editForm.remark" type="textarea" placeholder="请输入备注" />
                  </el-form-item>
                </el-col>
              </el-row>
      </el-form>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="handleBackToList">取消</el-button>
          <el-button type="primary" @click="handleEditSave">保存</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup name="Info">
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/budget/info"
import { listCompany } from "@/api/budget/company"
import { listProduct } from "@/api/budget/product"
import { listType } from "@/api/ad/type"
import { listScene } from "@/api/ad/scene"
import { listSize } from "@/api/ad/size"
import { useDict } from "@/utils/dict"
import { ref, reactive, toRefs, computed, getCurrentInstance } from "vue"

const { proxy } = getCurrentInstance()
const { os_type, ssp_pay_type } = useDict('os_type', 'ssp_pay_type')

const infoList = ref([])
const companyList = ref([])
const productList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const createTimeRange = ref([])
const batchBudgetSlotIdsInput = ref('')
const batchDspSlotCodeInput = ref('')
const adSlotNameSource = ref([])
const nameOptionsLoading = ref(false)

// 广告类型、广告场景、样式尺寸列表
const adTypeList = ref([])
const adSceneList = ref([])
const adSizeList = ref([])

const open = ref(false)
const title = ref('')
const editInfo = ref(null)

const editForm = ref({
  companyId: null,
  productId: null,
  name: null,
  osType: null,
  adTypeId: null,
  adSceneId: null,
  adSizeId: null,
  dspSlotCode: null,
  dspAppKey: null,
  dspAppSecret: null,
  dspAppId: null,
  dspAppPkg: null,
  dspAppVer: null,
  dspAppStoreVer: null,
  priceEncryptKey: null,
  dspAppStoreLink: null,
  dspPayType: null,
  dspDealRatio: null,
  remark: null
})

// 计算属性：筛选后的广告场景列表
const filteredAdSceneList = computed(() => {
  if (!editForm.value.adTypeId) {
    return []
  }
  return adSceneList.value.filter(scene => scene.typeId === editForm.value.adTypeId)
})

// 计算属性：筛选后的样式尺寸列表
const filteredAdSizeList = computed(() => {
  if (!editForm.value.adTypeId) {
    return []
  }
  return adSizeList.value.filter(size => size.typeId === editForm.value.adTypeId)
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    adTypeId: null,
    adSceneId: null,
    osType: null,
    dspSlotCode: null,
    dspSlotCodeList: [],
    dspAppKey: null,
    idList: [],
    companyId: null,
    productId: null
  },
  rules: {
    name: [
      { required: true, message: "广告位名称不能为空", trigger: "blur" }
    ],
    companyId: [
      { required: true, message: "公司不能为空", trigger: "change" }
    ],
    productId: [
      { required: true, message: "产品不能为空", trigger: "change" }
    ],
    osType: [
      { required: true, message: "操作系统类型不能为空", trigger: "change" }
    ],
    dspSlotCode: [
      { required: true, message: "预算方广告位不能为空", trigger: "blur" }
    ],
    dspPayType: [
      { required: true, message: "结算方式不能为空", trigger: "change" }
    ],
  }
})

// 编辑表单验证规则
const editRules = {
  companyId: [
    { required: true, message: "公司不能为空", trigger: "change" }
  ],
  productId: [
    { required: true, message: "产品不能为空", trigger: "change" }
  ],
  name: [
    { required: true, message: "广告位名称不能为空", trigger: "blur" }
  ],
  osType: [
    { required: true, message: "操作系统类型不能为空", trigger: "change" }
  ],
  adTypeId: [
    { required: true, message: "广告类型不能为空", trigger: "change" }
  ],
  dspSlotCode: [
    { required: true, message: "预算方广告位不能为空", trigger: "blur" }
  ],
  dspPayType: [
    { required: true, message: "结算方式不能为空", trigger: "change" }
  ],
}

const { queryParams, form, rules } = toRefs(data)

/** 广告位名称下拉选项（去重） */
const adSlotNameOptions = computed(() => {
  const uniqueNameMap = new Map()
  adSlotNameSource.value.forEach(item => {
    if (item?.name && !uniqueNameMap.has(item.name)) {
      uniqueNameMap.set(item.name, {
        label: item.name,
        value: item.name
      })
    }
  })
  return Array.from(uniqueNameMap.values())
})

/** 查询产品下拉列表（按公司筛选） */
const filteredQueryProductList = computed(() => {
  if (queryParams.value.companyId === null || queryParams.value.companyId === undefined || queryParams.value.companyId === '') {
    return productList.value
  }
  const companyId = Number(queryParams.value.companyId)
  return productList.value.filter(product => {
    const productCompanyId = Number(product.companyId ?? product.company_id)
    return productCompanyId === companyId
  })
})

/** 编辑表单产品下拉列表（按公司筛选） */
const filteredEditProductList = computed(() => {
  if (editForm.value.companyId === null || editForm.value.companyId === undefined || editForm.value.companyId === '') {
    return productList.value
  }
  const companyId = Number(editForm.value.companyId)
  return productList.value.filter(product => {
    const productCompanyId = Number(product.companyId ?? product.company_id)
    return productCompanyId === companyId
  })
})

/** 查询公司列表 */
function getCompanyList() {
  listCompany().then(response => {
    companyList.value = response.rows || []
  })
}

/** 查询产品列表 */
function getProductList() {
  listProduct().then(response => {
    productList.value = response.rows || []
  })
}

/** 加载广告位名称下拉数据 */
async function loadAdSlotNameOptions() {
  const pageSize = 200
  let pageNum = 1
  let total = 0
  const rows = []

  nameOptionsLoading.value = true
  try {
    do {
      const response = await listInfo({ pageNum, pageSize })
      const currentRows = response.rows || []
      total = Number(response.total || 0)
      rows.push(...currentRows)
      if (!currentRows.length) {
        break
      }
      pageNum += 1
    } while (rows.length < total && pageNum <= 50)

    adSlotNameSource.value = rows
  } catch (error) {
    console.error("加载广告位名称选项失败", error)
    adSlotNameSource.value = []
  } finally {
    nameOptionsLoading.value = false
  }
}

// /** 根据公司ID获取公司名称 */
// function getCompanyName(companyId) {
//   const company = companyList.value.find(item => item.id === companyId || item.company_id === companyId)
//   return company ? company.name : '-'
// }
//
// /** 根据产品ID获取产品名称 */
// function getProductName(productId) {
//   const product = productList.value.find(item => item.id === productId || item.product_id === productId)
//   return product ? product.name : '-'
// }

/** 加载广告类型列表 */
function loadAdTypeList() {
  listType({ pageNum: 1, pageSize: 100 }).then(response => {
    adTypeList.value = response.rows
  })
}

/** 加载广告场景列表 */
function loadAdSceneList() {
  listScene({ pageNum: 1, pageSize: 100 }).then(response => {
    adSceneList.value = response.rows
  })
}

/** 加载样式尺寸列表 */
function loadAdSizeList() {
  listSize({ pageNum: 1, pageSize: 100 }).then(response => {
    adSizeList.value = response.rows
  })
}

/** 根据广告类型ID获取广告类型名称 */
function getAdTypeName(typeId) {
  const type = adTypeList.value.find(item => item.id === typeId)
  return type ? type.name : typeId
}

/** 根据广告场景ID获取广告场景名称 */
function getAdSceneName(sceneId) {
  const scene = adSceneList.value.find(item => item.id === sceneId)
  return scene ? scene.name : sceneId
}

/** 根据样式尺寸ID获取样式尺寸 */
function getAdSizeName(sizeId) {
  const size = adSizeList.value.find(item => item.id === sizeId)
  return size ? size.size : sizeId
}

/** 编辑表单公司变化处理 */
function handleEditCompanyChange() {
  editForm.value.productId = null
}

/** 查询条件公司变化处理 */
function handleQueryCompanyChange() {
  queryParams.value.productId = null
}

/** 结算方式改变时触发成交系数验证 */
function handleDspPayTypeChange() {
  // 分成模式（1）不需要成交系数，清空
  if (editForm.value.dspPayType === 1 || editForm.value.dspPayType === '1') {
    editForm.value.dspDealRatio = null
  }
  proxy.$refs["editFormRef"].validateField('dspDealRatio')
}

/** 广告类型改变时的处理 */
function handleAdTypeChange() {
  editForm.value.adSceneId = null
  editForm.value.adSizeId = null
}

/** 查询预算广告位列表 */
function getList() {
  loading.value = true
  listInfo(queryParams.value).then(response => {
    console.log("InfoList::",response)
    infoList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    name: null,
    osType: null,
    adTypeId: null,
    adSceneId: null,
    adSizeId: null,
    dspSlotCode: null,
    dspAppKey: null,
    dspAppSecret: null,
    dspAppId: null,
    dspAppPkg: null,
    dspAppVer: null,
    dspAppStoreVer: null,
    priceEncryptKey: null,
    dspAppStoreLink: null,
    dspPayType: null,
    dspDealRatio: null,
    companyId: null,
    productId: null,
    idList: [],
    remark: null
  }
  proxy.resetForm("infoRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  const slotCodePayload = buildDspSlotCodePayload(batchDspSlotCodeInput.value)
  queryParams.value.dspSlotCode = slotCodePayload.dspSlotCode
  queryParams.value.dspSlotCodeList = slotCodePayload.dspSlotCodeList

  const { validIds, invalidValues } = parseBatchBudgetSlotIds(batchBudgetSlotIdsInput.value)
  queryParams.value.idList = validIds
  if (invalidValues.length > 0) {
    const preview = invalidValues.slice(0, 5).join("、")
    const suffix = invalidValues.length > 5 ? " 等" : ""
    proxy.$modal.msgWarning(`检测到无效预算位ID，已忽略: ${preview}${suffix}`)
  }
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  batchDspSlotCodeInput.value = ''
  batchBudgetSlotIdsInput.value = ''
  queryParams.value.idList = []
  queryParams.value.dspSlotCode = null
  queryParams.value.dspSlotCodeList = []
  proxy.resetForm("queryRef")
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
  editInfo.value = null
  title.value = "新增预算方广告位"
  editForm.value = {
    id: null,
    companyId: null,
    productId: null,
    name: null,
    osType: null,
    adTypeId: null,
    adSceneId: null,
    adSizeId: null,
    dspSlotCode: null,
    dspAppKey: null,
    dspAppSecret: null,
    dspAppId: null,
    dspAppPkg: null,
    dspAppVer: null,
    dspAppStoreVer: null,
    priceEncryptKey: null,
    dspAppStoreLink: null,
    dspPayType: null,
    dspDealRatio: null,
    remark: null
  }
  open.value = true
}

/** 修改按钮操作 */
function handleUpdate(row) {
  const _id = row.id || ids.value
  getInfo(_id).then(response => {
    const data = response.data
    const rawCompanyId = data.companyId ?? data.company_id
    const rawProductId = data.productId ?? data.product_id
    const companyId = rawCompanyId !== null && rawCompanyId !== undefined && rawCompanyId !== '' ? Number(rawCompanyId) : null
    const productId = rawProductId !== null && rawProductId !== undefined && rawProductId !== '' ? Number(rawProductId) : null
    editInfo.value = data

    // 调试：打印后端返回的完整数据
    console.log('后端返回的完整数据:', data)
    console.log('dsp_pay_type:', data.dsp_pay_type, 'dspPayType:', data.dspPayType)

    editForm.value = {
      id: data.id,
      companyId,
      productId,
      name: data.name,
      osType: data.osType !== null ? String(data.osType) : null,
      adTypeId: data.adTypeId,
      adSceneId: data.adSceneId,
      adSizeId: data.adSizeId,
      dspSlotCode: data.dspSlotCode,
      dspAppKey: data.dspAppKey,
      dspAppSecret: data.dspAppSecret,
      dspAppId: data.dspAppId,
      dspAppPkg: data.dspAppPkg,
      dspAppVer: data.dspAppVer,
      dspAppStoreVer: data.dspAppStoreVer,
      priceEncryptKey: data.priceEncryptKey,
      dspAppStoreLink: data.dspAppStoreLink,
      dspPayType: data.dsp_pay_type !== null && data.dsp_pay_type !== undefined ? Number(data.dsp_pay_type) : null,
      dspDealRatio: data.dsp_deal_ratio !== null ? data.dsp_deal_ratio : null,
      remark: data.remark
    }

    ids.value = []
    single.value = true
    multiple.value = true

    title.value = `编辑: ${data.name}(${data.id})`
    open.value = true
  })
}

/** 编辑页面保存按钮 */
function handleEditSave() {
  proxy.$refs["editFormRef"].validate(valid => {
    if (valid) {
      // 确保 dspPayType 不为 null，默认为分成模式（1）
      if (!editForm.value.dspPayType) {
        editForm.value.dspPayType = 1
      }

      // 分成模式（1）不需要成交系数，清空后再保存
      if (editForm.value.dspPayType === 1 || editForm.value.dspPayType === '1') {
        editForm.value.dspDealRatio = null
      }

      // 转换为下划线命名以匹配后端 @JSONField 注解
      const submitData = {
        ...editForm.value,
        dsp_pay_type: editForm.value.dspPayType,
        dsp_deal_ratio: editForm.value.dspDealRatio
      }
      // 删除驼峰命名的字段
      delete submitData.dspPayType
      delete submitData.dspDealRatio

      if (editForm.value.id) {
        // 修改操作
        updateInfo(submitData).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          editInfo.value = null
          getList()
          loadAdSlotNameOptions()
        })
      } else {
        // 新增操作：检查预算方广告位是否已存在
        checkDspSlotCodeExists(submitData)
      }
    }
  })
}

/** 检查预算方广告位编码是否已存在 */
function checkDspSlotCodeExists(submitData) {
  const queryParams = {
    pageNum: 1,
    pageSize: 1,
    dspSlotCode: editForm.value.dspSlotCode
  }

  listInfo(queryParams).then(response => {
    if (response.total > 0) {
      // 预算方广告位已存在
      proxy.$modal.msgWarning(`预算方广告位编码 "${editForm.value.dspSlotCode}" 已存在，请使用其他编码`)
    } else {
      // 不存在，可以添加
      addInfo(submitData).then(() => {
        proxy.$modal.msgSuccess("新增成功")
        open.value = false
        editInfo.value = null
        getList()
        loadAdSlotNameOptions()
      })
    }
  }).catch(() => {
    proxy.$modal.msgError("检查预算方广告位编码失败")
  })
}

/** 返回列表 */
function handleBackToList() {
  open.value = false
  editInfo.value = null
  if (proxy.$refs["editFormRef"]) {
    proxy.$refs["editFormRef"].clearValidate()
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除预算方广告位编号为"' + _ids + '"的数据项？').then(function() {
    return delInfo(_ids)
  }).then(() => {
    getList()
    loadAdSlotNameOptions()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  const slotCodePayload = buildDspSlotCodePayload(batchDspSlotCodeInput.value)
  const { validIds } = parseBatchBudgetSlotIds(batchBudgetSlotIdsInput.value)
  proxy.download('budget/info/export', {
    ...queryParams.value,
    dspSlotCode: slotCodePayload.dspSlotCode,
    dspSlotCodeList: slotCodePayload.dspSlotCodeList,
    idList: validIds
  }, `info_${new Date().getTime()}.xlsx`)
}

function buildDspSlotCodePayload(inputText) {
  const slotCodeList = parseBatchBudgetSlotCodes(inputText)
  const isBatchQuery = slotCodeList.length > 1
  return {
    dspSlotCode: isBatchQuery ? null : (slotCodeList[0] || null),
    dspSlotCodeList: isBatchQuery ? slotCodeList : []
  }
}

function parseBatchBudgetSlotCodes(inputText) {
  if (!inputText) {
    return []
  }
  const codes = String(inputText)
    .split(/[\s,;\uFF0C\uFF1B]+/)
    .map(item => item.trim())
    .filter(Boolean)
  return Array.from(new Set(codes))
}

function parseBatchBudgetSlotIds(inputText) {
  if (!inputText) {
    return { validIds: [], invalidValues: [] }
  }
  const tokens = String(inputText)
    .split(/[\s,;\uFF0C\uFF1B]+/)
    .map(item => item.trim())
    .filter(Boolean)

  const seen = new Set()
  const validIds = []
  const invalidValues = []

  tokens.forEach(token => {
    if (/^\d+$/.test(token)) {
      const id = Number(token)
      if (!seen.has(id)) {
        seen.add(id)
        validIds.push(id)
      }
    } else {
      invalidValues.push(token)
    }
  })

  return { validIds, invalidValues }
}

// 页面加载时获取数据
getCompanyList()
getProductList()
loadAdTypeList()
loadAdSceneList()
loadAdSizeList()
loadAdSlotNameOptions()
getList()
</script>

<style scoped>
.app-container-list {
  padding: 0;
}

/* 表格容器样式 */
.budget-table {
  width: 100%;
}

/* 表格内部包装器 */
.budget-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto !important;
  overflow-y: visible !important;
}

/* 表头和表格体也要横向滚动 */
.budget-table :deep(.el-table__header-wrapper),
.budget-table :deep(.el-table__body-wrapper) {
  overflow-x: auto !important;
  overflow-y: visible !important;
}

/* 固定列的样式 */
.budget-table :deep(.el-table__fixed) {
  height: 100% !important;
}

/* 确保表格列宽度不会被压缩 */
.budget-table :deep(.el-table__header),
.budget-table :deep(.el-table__body) {
  width: max-content !important;
}

.budget-table :deep(table) {
  width: 100%;
  table-layout: auto;
}

.app-container-edit {
  padding: 20px;
}

.edit-card {
  margin-bottom: 20px;
}

.empty-edit {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.btn-blue {
  background-color: #2A5FB7 !important;
  border-color: #2A5FB7 !important;
  color: #fff !important;
}

.btn-blue:hover {
  background-color: #1f4f96 !important;
  border-color: #1f4f96 !important;
}

:deep(.el-form--inline .el-form-item) {
  margin-right: 8px;   /* 👈 调小间距 */
  margin-bottom: 8px;  /* 👈 行间距也可以顺便优化 */
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

.drawer-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
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
.left-button {
  margin-left: 55px;
}
</style>

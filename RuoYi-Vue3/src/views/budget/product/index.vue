<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
             label-width="68px"
             :label-style="{ color: '#000' }"
    >
      <el-form-item  prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter="handleQuery"
          style="width: 150px;"
        />
      </el-form-item>
      <el-form-item  prop="companyId">
        <el-select
          v-model="queryParams.companyId"
          placeholder="请选择公司"
          clearable
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
      <el-form-item>
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
          v-hasPermi="['budget:product:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            class="btn-regge"
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['budget:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange"
              style="width: 100%"
              :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
              :cell-style="{ color: '#000' }"
              border
              table-layout="auto"
              highlight-current-row="true"
    >
      <el-table-column label="ID" width="66" align="center" prop="id"/>
      <el-table-column label="预算公司" align="center"  prop="companyId">
        <template #default="scope">
          {{ getCompanyName(scope.row.companyId) }}
        </template>
      </el-table-column>
      <el-table-column label="产品名称" align="center" prop="name">
        <template #default="scope">
          {{ formatProductName(scope.row) }}
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="200" align="center" prop="createTime"/>
      <el-table-column label="修改时间" width="200" align="center" prop="updateTime"/>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['budget:product:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['budget:product:remove']">删除</el-button>
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

    <!-- 添加或修改产品管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="productRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预算公司" prop="companyId">
          <el-select v-model="form.companyId" placeholder="请选择公司">
            <el-option
                v-for="company in companyList"
                :key="company.id"
                :label="company.name"
                :value="company.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Product">
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/budget/product"
import { listCompany } from "@/api/budget/company"

const { proxy } = getCurrentInstance()

const productList = ref([])
const companyList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    companyId: null,
  },
  rules: {
    name: [
      { required: true, message: "产品名称不能为空", trigger: "blur" }
    ],
    companyId: [
      { required: true, message: "公司不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)


/** 格式化媒体名称：name(公司ID) */
function formatProductName(row) {
  return `${row.name}:(${row.id})`
}
/** 查询公司列表 */
function getCompanyList() {
  listCompany().then(response => {
    companyList.value = response.rows || []
  })
}

/** 根据公司ID获取公司名称 */
function getCompanyName(companyId) {
  const company = companyList.value.find(item => item.id === companyId)
  return company ? company.name : '-'
}

/** 查询产品管理列表 */
function getList() {
  loading.value = true
  listProduct(queryParams.value).then(response => {
    productList.value = response.rows
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
    name: null,
    companyId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("productRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
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
  reset()
  open.value = true
  title.value = "添加产品管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getProduct(_id).then(response => {
    const data = response.data
    form.value = {
      ...data,
      companyId: data.companyId !== null ? Number(data.companyId) : null,
    }
    open.value = true
    title.value = "修改产品管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["productRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateProduct(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addProduct(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除产品管理编号为"' + _ids + '"的数据项？').then(function() {
    return delProduct(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('budget/product/export', {
    ...queryParams.value
  }, `product_${new Date().getTime()}.xlsx`)
}

getCompanyList()
getList()
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

</style>
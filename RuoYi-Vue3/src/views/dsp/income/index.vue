<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="预算位Id" prop="dspSlotId">
        <el-input
          v-model="queryParams.dspSlotId"
          placeholder="请输入预算位Id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预算位code" prop="dspSlotCode">
        <el-input
          v-model="queryParams.dspSlotCode"
          placeholder="请输入预算位code"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预算放收益" prop="income">
        <el-input
          v-model="queryParams.income"
          placeholder="请输入预算放收益"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="媒体位id" prop="sspSlotId">
        <el-input
          v-model="queryParams.sspSlotId"
          placeholder="请输入媒体位id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司id" prop="companyId">
        <el-input
          v-model="queryParams.companyId"
          placeholder="请输入公司id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="导入数据条数" prop="table">
        <el-input
          v-model="queryParams.table"
          placeholder="请输入导入数据条数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="导入时间" prop="inputTime">
        <el-date-picker clearable
          v-model="queryParams.inputTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择导入时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dsp:income:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['dsp:income:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['dsp:income:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['dsp:income:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="incomeList" @selection-change="handleSelectionChange" border >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="预算名称" align="center" prop="dspSlotId" />
      <el-table-column label="预算位编码" align="center" prop="dspSlotCode" />
      <el-table-column label="预算放收益" align="center" prop="income" />
      <el-table-column label="媒体名称" align="center" prop="sspSlotId" />
      <el-table-column label="结算方式" align="center" prop="dspPayType" />
      <el-table-column label="公司名称" align="center" prop="companyId" />
      <el-table-column label="导入数据条数" align="center" prop="table" />
      <el-table-column label="导入时间" align="center" prop="inputTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.inputTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['dsp:income:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['dsp:income:remove']">删除</el-button>
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

    <!-- 添加或修改预算收益导入对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="incomeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预算位Id" prop="dspSlotId">
          <el-input v-model="form.dspSlotId" placeholder="请输入预算位Id" />
        </el-form-item>
        <el-form-item label="预算位code" prop="dspSlotCode">
          <el-input v-model="form.dspSlotCode" placeholder="请输入预算位code" />
        </el-form-item>
        <el-form-item label="预算放收益" prop="income">
          <el-input v-model="form.income" placeholder="请输入预算放收益" />
        </el-form-item>
        <el-form-item label="媒体位id" prop="sspSlotId">
          <el-input v-model="form.sspSlotId" placeholder="请输入媒体位id" />
        </el-form-item>
        <el-form-item label="公司id" prop="companyId">
          <el-input v-model="form.companyId" placeholder="请输入公司id" />
        </el-form-item>
        <el-form-item label="导入数据条数" prop="table">
          <el-input v-model="form.table" placeholder="请输入导入数据条数" />
        </el-form-item>
        <el-form-item label="导入时间" prop="inputTime">
          <el-date-picker clearable
            v-model="form.inputTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择导入时间">
          </el-date-picker>
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

<script setup name="Income">
import { listIncome, getIncome, delIncome, addIncome, updateIncome } from "@/api/dsp/income"

const { proxy } = getCurrentInstance()

const incomeList = ref([])
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
    dspSlotId: null,
    dspSlotCode: null,
    income: null,
    sspSlotId: null,
    dspPayType: null,
    companyId: null,
    table: null,
    inputTime: null,
  },
  rules: {
    dspSlotId: [
      { required: true, message: "预算位Id不能为空", trigger: "blur" }
    ],
    dspSlotCode: [
      { required: true, message: "预算位code不能为空", trigger: "blur" }
    ],
    sspSlotId: [
      { required: true, message: "媒体位id不能为空", trigger: "blur" }
    ],
    dspPayType: [
      { required: true, message: "结算方式(1分成 2 RTB)不能为空", trigger: "change" }
    ],
    companyId: [
      { required: true, message: "公司id不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询预算收益导入列表 */
function getList() {
  loading.value = true
  listIncome(queryParams.value).then(response => {
    incomeList.value = response.rows
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
    income: null,
    sspSlotId: null,
    dspPayType: null,
    companyId: null,
    table: null,
    inputTime: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("incomeRef")
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
  title.value = "添加预算收益导入"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getIncome(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预算收益导入"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["incomeRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateIncome(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addIncome(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除预算收益导入编号为"' + _ids + '"的数据项？').then(function() {
    return delIncome(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('dsp/income/export', {
    ...queryParams.value
  }, `income_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['data:corrected:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['data:corrected:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['data:corrected:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['data:corrected:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="correctedList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="媒体数据报表id" align="center" prop="dataSspSlotDayId" />
      <el-table-column label="成交方式" align="center" prop="sspPayType">
        <template #default="scope">
          <dict-tag :options="ssp_pay_type" :value="scope.row.sspPayType"/>
        </template>
      </el-table-column>
      <el-table-column label="成本(分)" align="center" prop="spend" />
      <el-table-column label="收入(分)" align="center" prop="income" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['data:corrected:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['data:corrected:remove']">删除</el-button>
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

    <!-- 添加或修改支出修正对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="correctedRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="媒体数据报表id" prop="dataSspSlotDayId">
          <el-input v-model="form.dataSspSlotDayId" placeholder="请输入媒体数据报表id" />
        </el-form-item>
        <el-form-item label="成交方式" prop="sspPayType">
          <el-select v-model="form.sspPayType" placeholder="请选择成交方式">
            <el-option
              v-for="dict in ssp_pay_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成本(分)" prop="spend">
          <el-input v-model="form.spend" placeholder="请输入成本(分)" />
        </el-form-item>
        <el-form-item label="收入(分)" prop="income">
          <el-input v-model="form.income" placeholder="请输入收入(分)" />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
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

<script setup name="Corrected">
import { listCorrected, getCorrected, delCorrected, addCorrected, updateCorrected } from "@/api/data/corrected"

const { proxy } = getCurrentInstance()
const { ssp_pay_type } = proxy.useDict('ssp_pay_type')

const correctedList = ref([])
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
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询支出修正列表 */
function getList() {
  loading.value = true
  listCorrected(queryParams.value).then(response => {
    correctedList.value = response.rows
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
    dataSspSlotDayId: null,
    sspPayType: null,
    spend: null,
    income: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("correctedRef")
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
  title.value = "添加支出修正"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getCorrected(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改支出修正"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["correctedRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateCorrected(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCorrected(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除支出修正编号为"' + _ids + '"的数据项？').then(function() {
    return delCorrected(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('data/corrected/export', {
    ...queryParams.value
  }, `corrected_${new Date().getTime()}.xlsx`)
}

getList()
</script>

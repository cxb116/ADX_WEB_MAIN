<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
             label-width="68px"
             :label-style="{ color: '#000' }"
    >
      <el-form-item prop="companyId">
        <el-input
          v-model="queryParams.companyId"
          placeholder="请选择预算公司名称"
          clearable
          @keyup.enter="handleQuery"
          style="width: 150px;"
        />
      </el-form-item>
      <el-form-item  prop="createTime">
        <el-date-picker clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间"
                        style="width: 150px;"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button class="btn-blue" type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading" :data="exceList" @selection-change="handleSelectionChange"
              style="width: 100%"
              :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
              :cell-style="{ color: '#000' }"
              border
              table-layout="auto"
              highlight-current-row="true"
    >
      <el-table-column width="90" align="center">预算方</el-table-column>
      <el-table-column label="统计日期" align="center" prop="createTime"/>
      <el-table-column label="预算公司名称" align="center" prop="companyName" />
      <el-table-column label="导入条数" align="center" prop="tables" />
      <el-table-column label="初次导入时间" align="center" prop="inputTime" width="180">
      </el-table-column>
      <el-table-column label="最终导入时间" align="center" prop="updateTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="upload" @click="handleImport" >导入EXCEL</el-button>
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

    <!-- 用户导入Exce -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :on-change="handleFileChange" :on-remove="handleFileRemove" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
<!--            <div class="el-upload__tip">-->
<!--              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据-->
<!--            </div>-->
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" underline="never" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Exce">
import { listExce, getExce, delExce, addExce, updateExce } from "@/api/system/exce"
import {getToken} from "@/utils/auth.js";

const { proxy } = getCurrentInstance()

const exceList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 用户导入
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/system/exce/importData"
})
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    companyId: null,
    companyName: null,
    tables: null,
    createTime:null,
    inputTime: null,
    updateTime: null,
  },
  rules: {
    companyId: [
      { required: true, message: "预算公司id不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询导入流水列表 */
function getList() {
  loading.value = true
  listExce(queryParams.value).then(response => {
    exceList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true
}

/** 文件选择处理 */
const handleFileChange = (file, fileList) => {
  upload.selectedFile = file
}

/** 文件删除处理 */
const handleFileRemove = (file, fileList) => {
  upload.selectedFile = null
}

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false
  upload.isUploading = false
  proxy.$refs["uploadRef"].handleRemove(file)
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  getList()
}

/** 提交上传文件 */
function submitFileForm() {
  const file = upload.selectedFile
  if (!file || file.length === 0 || !file.name.toLowerCase().endsWith('.xls') && !file.name.toLowerCase().endsWith('.xlsx')) {
    proxy.$modal.msgError("请选择后缀为 “xls”或“xlsx”的文件。")
    return
  }
  proxy.$refs["uploadRef"].submit()
}
/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/exce/importTemplate", {
  }, `通用收益-预算方名称-${formatDate(new Date())}.xlsx`)
}
/** 导入按钮操作 */
function handleImport() {
  upload.title = "用户导入"
  upload.open = true
  upload.selectedFile = null
}
// 日期格式
function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
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
    companyId: null,
    tables: null,
    companyName: null,
    inputTime: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("exceRef")
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
  title.value = "添加导入流水"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getExce(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改导入流水"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["exceRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateExce(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addExce(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除导入流水编号为"' + _ids + '"的数据项？').then(function() {
    return delExce(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/exce/export', {
    ...queryParams.value
  }, `exce_${new Date().getTime()}.xlsx`)
}

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
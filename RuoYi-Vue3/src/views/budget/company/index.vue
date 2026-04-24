<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
            <el-form-item  prop="name">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入公司名称"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item  prop="dspCode">
                <el-input
                        v-model="queryParams.dspCode"
                        placeholder="请输入预算映射值"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item  prop="method">
                <el-select
                        v-model="queryParams.method"
                        placeholder="请选择请求方法"
                        clearable
                        @keyup.enter="handleQuery"
                >
                    <el-option
                            v-for="dict in request_method"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
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
                        v-hasPermi="['budget:company:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    class="btn-regge"
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['budget:company:export']"
                >导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="companyList" @selection-change="handleSelectionChange"
                  style="width: 100%"
                  :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
                  :cell-style="{ color: '#000' }"
                  border
                  table-layout="auto"
                  highlight-current-row="true"
        >
            <el-table-column label="ID" align="center" width="66" prop="id" />
            <el-table-column label="公司名称" align="center" width="180" prop="name">
              <template #default="scope">
                {{ formatCompanyName(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column label="预算映射值" align="center" width="100" prop="dspCode" />
            <el-table-column label="请求地址" align="center"   prop="url" />
            <el-table-column label="请求方法" align="center" width="100" prop="method">
                <template #default="scope">
                    <dict-tag :options="request_method" :value="scope.row.method" />
                </template>
            </el-table-column>
          <el-table-column label="创建时间" width="180" align="center" prop="createTime"/>
          <el-table-column label="修改时间" width="180" align="center" prop="updateTime"/>
            <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['budget:company:edit']">修改</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['budget:company:remove']">删除</el-button>
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

        <!-- 添加或修改公司管理对话框 -->
        <el-drawer
            v-model="open"
            direction="rtl"
            size="1400px"
            :show-close="false"
            style="background: #f2f5f7"
        >
            <template #header>
                <div class="drawer-header">
                    <div class="header-top">
                        <el-icon class="back-icon" @click="cancel">
                            <Close />
                        </el-icon>
                        <span class="drawer-title">{{ title }}</span>
                    </div>
                    <el-divider class="dividerClass" />
                    <div class="drawer-actions">
                        <el-button class="btn-blue" @click="submitForm">
                            <el-icon>
                                <Position />
                            </el-icon>
                            提 交
                        </el-button>
                    </div>
                </div>
            </template>
            <div class="drawer-content">
                <el-form
                    ref="companyRef"
                    :model="form"
                    :rules="rules"
                    label-width="100px"
                    style="padding: 20px; border-radius: 8px;"
                >
                    <div class="title-content1">
                        <div class="title-content2">基本信息</div>
                        <el-divider class="dividerClass1" />
                    </div>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="公司名称" prop="name">
                                <el-input v-model="form.name" placeholder="请输入公司名称" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="预算映射值" prop="dspCode">
                                <el-input v-model="form.dspCode" placeholder="请输入预算映射值" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="请求地址" prop="url">
                                <el-input v-model="form.url" placeholder="请输入请求地址" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="请求方法" prop="method">
                                <el-select v-model="form.method" placeholder="请选择请求方法" style="width: 100%">
                                    <el-option
                                        v-for="dict in request_method"
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
                            <el-form-item label="超时时间" prop="timeout">
                                <el-input v-model="form.timeout" placeholder="请输入超时时间(单位:ms)" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                    </el-form-item>
                </el-form>
            </div>
        </el-drawer>
    </div>
</template>

<script setup name="Company">
import { listCompany, getCompany, delCompany, addCompany, updateCompany } from "@/api/budget/company"
import { Close, Position } from "@element-plus/icons-vue"
const { proxy } = getCurrentInstance()

// 获取字典数据
const { request_method } = proxy.useDict("request_method")

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
        dspCode: null,
        url: null,
        method: null,
        timeout: null,
    },
    rules: {
        name: [
            { required: true, message: "公司名称不能为空", trigger: "blur" }
        ],
        dspCode: [
            { required: true, message: "预算映射值不能为空", trigger: "blur" }
        ],
        url: [
            { required: true, message: "请求地址不能为空", trigger: "blur" }
        ],
        method: [
            { required: true, message: "请求方法不能为空", trigger: "change" }
        ],
    }
})

const { queryParams, form, rules } = toRefs(data)

function formatCompanyName(row) {
  return `${row.name}:(${row.id})`
}

/** 查询公司管理列表 */
function getList() {
    loading.value = true
    listCompany(queryParams.value).then(response => {
        companyList.value = response.rows
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
        dspCode: null,
        url: null,
        method: null,
        timeout: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
    }
    proxy.resetForm("companyRef")
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
    title.value = "添加公司管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset()
    const _id = row.id || ids.value
    getCompany(_id).then(response => {
        const data = response.data
        form.value = {
            ...data,
            timeout: data.timeout !== null ? Number(data.timeout) : null,
            method: data.method !== null ? String(data.method) : null,
        }
        open.value = true
        title.value = "修改公司管理"
    })
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["companyRef"].validate(valid => {
        if (valid) {
            if (form.value.id != null) {
                updateCompany(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addCompany(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除公司管理编号为"' + _ids + '"的数据项？').then(function() {
        return delCompany(_ids)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download('budget/company/export', {
        ...queryParams.value
    }, `company_${new Date().getTime()}.xlsx`)
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

.drawer-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 1000px;
  padding: 0;
  background: #fff;
  border-bottom: 1px solid #dfe7f2;
}

.header-top {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 0 0 13px;
}

.drawer-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 15px 15px 8px;
}

.back-icon {
  cursor: pointer;
  font-size: 18px;
  margin-left: 20px;
  margin-top: 8px;
}

.drawer-actions {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
  padding: 0 20px 13px;
  width: 100%;
}

.drawer-actions .el-icon {
  margin-right: 6px;
}

.dividerClass {
  margin-top: -10px;
}

:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 0;
  background: #fff;
}

:deep(.el-drawer__body) {
  padding: 16px;
  background: #f2f5f7;
}

.drawer-content {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 20px;
  height: 100%;
  width: 900px;
  margin: 0 auto;
  overflow-y: auto;
  background: #fff;
}

.title-content1 {
  width: 800px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 12px;
}

.title-content2 {
  width: 100%;
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 8px;
}

.dividerClass1 {
  width: 100% !important;
  margin: 0 !important;
}
</style>

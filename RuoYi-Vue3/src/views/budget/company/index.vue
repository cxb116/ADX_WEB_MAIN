<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
            <el-form-item label="公司名称" prop="name">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入公司名称"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="预算映射值" prop="dspCode">
                <el-input
                        v-model="queryParams.dspCode"
                        placeholder="请输入预算映射值"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="请求地址" prop="url">
                <el-input
                        v-model="queryParams.url"
                        placeholder="请输入请求地址"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="请求方法" prop="method">
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
            <el-form-item label="超时时间" prop="timeout">
                <el-input
                        v-model="queryParams.timeout"
                        placeholder="请输入超时时间(单位:ms)"
                        clearable
                        @keyup.enter="handleQuery"
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
                        type="primary"
                        plain
                        icon="Plus"
                        @click="handleAdd"
                        v-hasPermi="['budget:company:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['budget:company:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['budget:company:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['budget:company:export']"
                >导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="companyList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="公司名称" align="center" width="120" prop="name">
              <template #default="scope">
                {{ formatCompanyName(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column label="映射" align="center" width="60" prop="dspCode" />
            <el-table-column label="请求地址" align="center" width="320"  prop="url" />
            <el-table-column label="请求方法" align="center" width="80" prop="method">
                <template #default="scope">
                    <dict-tag :options="request_method" :value="scope.row.method" />
                </template>
            </el-table-column>
          <el-table-column label="创建时间" width="180" align="center" prop="createTime"/>
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
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="companyRef" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="公司名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入公司名称" />
                </el-form-item>
                <el-form-item label="预算映射值" prop="dspCode">
                    <el-input v-model="form.dspCode" placeholder="请输入预算映射值" />
                </el-form-item>
                <el-form-item label="请求地址" prop="url">
                    <el-input v-model="form.url" placeholder="请输入请求地址" />
                </el-form-item>
                <el-form-item label="请求方法" prop="method">
                    <el-select v-model="form.method" placeholder="请选择请求方法">
                        <el-option
                                v-for="dict in request_method"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="超时时间" prop="timeout">
                    <el-input v-model="form.timeout" placeholder="请输入超时时间(单位:ms)" />
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

<script setup name="Company">
import { listCompany, getCompany, delCompany, addCompany, updateCompany } from "@/api/budget/company"

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
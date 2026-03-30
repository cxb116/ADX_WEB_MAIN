<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="媒体名称" prop="name">
                <el-input
                    v-model="queryParams.name"
                    placeholder="请输入媒体名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="公司名称" prop="mediaCompanyName">
                <el-input
                    v-model="queryParams.mediaCompanyName"
                    placeholder="请输入公司名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="公司简称" prop="mediaCompanyShort">
                <el-input
                    v-model="queryParams.mediaCompanyShort"
                    placeholder="请输入公司简称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="enable">
                <el-select
                    v-model="queryParams.enable"
                    placeholder="请选择状态"
                    clearable
                    @keyup.enter="handleQuery"
                >
                    <el-option
                        v-for="dict in media_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                    />
                </el-select>
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
                    v-hasPermi="['flow:media:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="success"
                    plain
                    icon="Edit"
                    :disabled="single"
                    @click="handleUpdate"
                    v-hasPermi="['flow:media:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    plain
                    icon="Delete"
                    :disabled="multiple"
                    @click="handleDelete"
                    v-hasPermi="['flow:media:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="warning"
                    plain
                    icon="Download"
                    @click="handleExport"
                    v-hasPermi="['flow:media:export']"
                >导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="mediaList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="媒体名称" align="center" width="260" prop="name">
                <template #default="scope">
                    {{ formatMediaName(scope.row) }}
                </template>
            </el-table-column>
            <el-table-column label="公司名称" align="center" show-overflow-tooltip prop="mediaCompanyName" />
            <el-table-column label="公司简称" align="center" prop="mediaCompanyShort" />
          <el-table-column label="法人姓名" align="center" prop="mediaOwnerName" />
          <el-table-column label="联系人" align="center" prop="contactName" />
          <el-table-column label="联系电话" align="center" show-overflow-tooltip prop="contactPhone" />
          <el-table-column label="联系邮箱" align="center" show-overflow-tooltip prop="contactEmail" />
            <el-table-column label="状态" align="center" prop="enable">
                <template #default="scope">
                    <dict-tag :options="media_status" :value="scope.row.enable"/>
                </template>
            </el-table-column>

            <el-table-column label="创建时间" align="center" prop="createTime" />

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['flow:media:edit']">修改</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['flow:media:remove']">删除</el-button>
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

        <!-- 添加或修改媒体管理对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="mediaRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="媒体名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入媒体名称" />
                </el-form-item>
                <el-form-item label="账号名" prop="account">
                    <el-input v-model="form.account" placeholder="请输入账号名" />
                </el-form-item>
<!--                <el-form-item label="密码" prop="password">-->
<!--                    <el-input v-model="form.password" placeholder="请输入密码" />-->
<!--                </el-form-item>-->
                <el-form-item label="公司名称" prop="mediaCompanyName">
                    <el-input v-model="form.mediaCompanyName" placeholder="请输入公司名称" />
                </el-form-item>
                <el-form-item label="公司简称" prop="mediaCompanyShort">
                    <el-input v-model="form.mediaCompanyShort" placeholder="请输入公司简称" />
                </el-form-item>
                <el-form-item label="统一社会信用代码" prop="mediaCompanyCode">
                    <el-input v-model="form.mediaCompanyCode" placeholder="请输入统一社会信用代码" />
                </el-form-item>
                <el-form-item label="营业执照照片" prop="mediaCompanyLicense">
                    <el-input v-model="form.mediaCompanyLicense" placeholder="请输入营业执照照片" />
                </el-form-item>
                <el-form-item label="公司地址" prop="mediaCompanyAddress">
                    <el-input v-model="form.mediaCompanyAddress" placeholder="请输入公司地址" />
                </el-form-item>
                <el-form-item label="法人姓名" prop="mediaOwnerName">
                    <el-input v-model="form.mediaOwnerName" placeholder="请输入法人姓名" />
                </el-form-item>
                <el-form-item label="联系人" prop="contactName">
                    <el-input v-model="form.contactName" placeholder="请输入联系人" />
                </el-form-item>
                <el-form-item label="联系电话" prop="contactPhone">
                    <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
                </el-form-item>
                <el-form-item label="联系邮箱" prop="contactEmail">
                    <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
                </el-form-item>
                <el-form-item label="状态" prop="enable">
                    <el-select v-model="form.enable" placeholder="请选择状态">
                        <el-option
                            v-for="dict in media_status"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                        />
                    </el-select>
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

<script setup name="Media">
import { listMedia, getMedia, delMedia, addMedia, updateMedia } from "@/api/flow/media"

const { proxy } = getCurrentInstance()

// 使用 proxy.useDict 获取字典数据，参考菜单管理页面的实现
const { media_status } = proxy.useDict("media_status")

const mediaList = ref([])
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
        mediaCompanyName: null,
        mediaCompanyShort: null,
        mediaCompanyAddress: null,
        mediaOwnerName: null,
        contactPhone: null,
        contactEmail: null,
        enable: null,
    },
    rules: {
        account: [
            { required: true, message: "账号名不能为空", trigger: "blur" }
        ],
        password: [
            { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        enable: [
            { required: true, message: "状态不能为空", trigger: "blur" }
        ],
    }
})

const { queryParams, form, rules } = toRefs(data)

/** 格式化媒体名称：name(公司ID) */
function formatMediaName(row) {
    return `${row.name}:(${row.id})`
}

/** 查询媒体管理列表 */
function getList() {
    loading.value = true
    listMedia(queryParams.value).then(response => {
        mediaList.value = response.rows
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
        account: null,
        password: null,
        mediaCompanyName: null,
        mediaCompanyShort: null,
        mediaCompanyCode: null,
        mediaCompanyLicense: null,
        mediaCompanyAddress: null,
        mediaOwnerName: null,
        contactName: null,
        contactPhone: null,
        contactEmail: null,
        enable: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
    }
    proxy.resetForm("mediaRef")
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
    title.value = "添加媒体管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset()
    const _id = row.id || ids.value
    getMedia(_id).then(response => {
        form.value = response.data
        // 将 enable 从 int 转换为 string，适配 el-select
        if (form.value.enable !== null && form.value.enable !== undefined) {
            form.value.enable = String(form.value.enable)
        }
        open.value = true
        title.value = "修改媒体管理"
    })
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["mediaRef"].validate(valid => {
        if (valid) {
            if (form.value.id != null) {
                updateMedia(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addMedia(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除媒体管理编号为"' + _ids + '"的数据项？').then(function() {
        return delMedia(_ids)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download('flow/media/export', {
        ...queryParams.value
    }, `media_${new Date().getTime()}.xlsx`)
}

getList()
</script>
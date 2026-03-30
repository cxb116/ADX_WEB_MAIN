<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="媒体名称" prop="mediaId">
                <el-select
                        v-model="queryParams.mediaId"
                        placeholder="请选择媒体"
                        clearable
                        filterable
                        style="width: 200px"
                >
                    <el-option
                            v-for="item in mediaOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="应用名称" prop="name">
                <el-select
                        v-model="queryParams.name"
                        placeholder="请选择应用"
                        clearable
                        filterable
                        style="width: 200px"
                >
                    <el-option
                            v-for="item in appOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="下载地址" prop="downloadUrl">
                <el-input
                        v-model="queryParams.downloadUrl"
                        placeholder="请输入下载地址"
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
            <el-form-item label="创建时间" prop="createTimeRange">
                <el-date-picker
                        v-model="createTimeRange"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="YYYY-MM-DD"
                        style="width: 240px"
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
                        v-hasPermi="['flow:app:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['flow:app:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['flow:app:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['flow:app:export']"
                >导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="appList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="应用名称" align="center" prop="name">
            <template #default="scope">
              {{ getMediaAppName(scope.row) }}
            </template>
          </el-table-column>

            <el-table-column label="媒体名称" align="center" prop="mediaId">
                <template #default="scope">
                    {{ getMediaCompanyName(scope.row.mediaId) }}({{ scope.row.mediaId }})
                </template>
            </el-table-column>

            <el-table-column label="操作系统类型" align="center" prop="osType">
              <template #default="scope">
                <dict-tag :options="os_type" :value="scope.row.osType" />
              </template>
            </el-table-column>
            <el-table-column label="接入方式" align="center" prop="accessType">
                <template #default="scope">
                    <span v-if="scope.row.accessType === 1">API</span>
                    <span v-else-if="scope.row.accessType === 2">SDK</span>
                    <span v-else>{{ scope.row.accessType }}</span>
                </template>
            </el-table-column>
            <el-table-column label="包名" align="center" prop="pkg" />
            <el-table-column label="下载地址" align="center" prop="downloadUrl" />
            <el-table-column label="状态" align="center" prop="enable">
                <template #default="scope">
                    <dict-tag :options="media_status" :value="scope.row.enable" />
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['flow:app:edit']">修改</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['flow:app:remove']">删除</el-button>
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

        <!-- 添加或修改应用管理对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="appRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="媒体公司" prop="mediaId">
                    <el-select
                        v-model="form.mediaId"
                        placeholder="请选择媒体公司"
                        filterable
                        style="width: 100%"
                    >
                        <el-option
                            v-for="item in mediaOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>

                <el-form-item label="应用名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入应用名称" />
                </el-form-item>
                <el-form-item label="操作系统" prop="osType">
                    <el-select v-model="form.osType" placeholder="请选择操作系统">
                        <el-option
                                v-for="dict in os_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="接入方式" prop="accessType">
                    <el-select v-model="form.accessType" placeholder="请选择接入方式">
                        <el-option
                                v-for="dict in access_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="包名" prop="pkg">
                    <el-input v-model="form.pkg" placeholder="请输入包名" />
                </el-form-item>
                <el-form-item label="下载地址" prop="downloadUrl">
                    <el-input v-model="form.downloadUrl" placeholder="请输入下载地址" />
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

<script setup name="App">
import { listApp, getApp, delApp, addApp, updateApp } from "@/api/flow/app"
import { listMedia } from "@/api/flow/media"
const { proxy } = getCurrentInstance()

// 获取字典数据
const { media_status, os_type, access_type } = proxy.useDict("media_status", "os_type", "access_type")

const appList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 创建媒体ID到公司名称的映射
const mediaCompanyNameMap = ref(new Map())
// 媒体选项列表
const mediaOptions = ref([])
// 应用选项列表
const appOptions = ref([])
// 创建时间范围
const createTimeRange = ref([])

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        mediaId: null,
        mediaName: null,
        name: null,
        osType: null,
        accessType: null,
        pkg: null,
        downloadUrl: null,
        enable: null,
        params: {
            startTime: null,
            endTime: null
        }
    },
    rules: {
        mediaId: [
            { required: true, message: "媒体Id不能为空", trigger: "blur" }
        ],
        name: [
            { required: true, message: "应用名称不能为空", trigger: "blur" }
        ],
        osType: [
            { required: true, message: "操作系统类型不能为空", trigger: "change" }
        ],
        accessType: [
            { required: true, message: "接入方式不能为空", trigger: "change" }
        ],
        enable: [
            { required: true, message: "状态不能为空", trigger: "blur" }
        ],
    }
})

const { queryParams, form, rules } = toRefs(data)

/** 加载媒体列表，建立媒体ID到公司名称的映射 */
function loadMediaList() {
    listMedia({ pageNum: 1, pageSize: 1000 }).then(response => {
        mediaCompanyNameMap.value.clear()
        mediaOptions.value = []
        response.rows.forEach(media => {
            mediaCompanyNameMap.value.set(media.id, media.mediaCompanyName)
            // 添加到选项列表，用于下拉选择
            mediaOptions.value.push({
                value: media.id,
                label: `${media.mediaCompanyName}(${media.id})`
            })
        })
    })
}

/** 加载应用选项列表 */
function loadAppOptions() {
    listApp({ pageNum: 1, pageSize: 1000 }).then(response => {
        appOptions.value = []
        response.rows.forEach(app => {
            appOptions.value.push({
                value: app.name,
                label: `${app.name}(${app.id})`
            })
        })
    })
}

/** 根据媒体ID获取公司名称 */
function getMediaCompanyName(mediaId) {
    return mediaCompanyNameMap.value.get(mediaId) || mediaId
}

function getMediaAppName(row) {
  return `${row.name}:(${row.id})`
}

/** 查询应用管理列表 */
function getList() {
    loading.value = true
    listApp(queryParams.value).then(response => {
        appList.value = response.rows
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
        name: null,
        osType: null,
        accessType: null,
        pkg: null,
        downloadUrl: null,
        enable: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
    }
    proxy.resetForm("appRef")
}

/** 搜索按钮操作 */
function handleQuery() {
    // 处理时间范围
    if (createTimeRange.value && createTimeRange.value.length === 2) {
        queryParams.value.params.startTime = createTimeRange.value[0]
        queryParams.value.params.endTime = createTimeRange.value[1]
    } else {
        queryParams.value.params.startTime = null
        queryParams.value.params.endTime = null
    }
    queryParams.value.pageNum = 1
    getList()
}

/** 重置按钮操作 */
function resetQuery() {
    createTimeRange.value = []
    queryParams.value.params.startTime = null
    queryParams.value.params.endTime = null
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
    title.value = "添加应用管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset()
    const _id = row.id || ids.value
    getApp(_id).then(response => {
        // 将数字类型的字典值转换为字符串，以便与字典value匹配
        const data = response.data
        if (data.osType !== null && data.osType !== undefined) {
            data.osType = String(data.osType)
        }
        if (data.accessType !== null && data.accessType !== undefined) {
            data.accessType = String(data.accessType)
        }
        if (data.enable !== null && data.enable !== undefined) {
            data.enable = String(data.enable)
        }
        form.value = data
        open.value = true
        title.value = "修改应用管理"
    })
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["appRef"].validate(valid => {
        if (valid) {
            if (form.value.id != null) {
                updateApp(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addApp(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除应用管理编号为"' + _ids + '"的数据项？').then(function() {
        return delApp(_ids)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download('flow/app/export', {
        ...queryParams.value
    }, `app_${new Date().getTime()}.xlsx`)
}

// 页面加载时获取媒体列表和应用选项
loadMediaList()
loadAppOptions()
getList()
</script>
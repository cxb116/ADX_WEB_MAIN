<template>
    <div class="app-container">
        <el-form :model="queryParams"
                 ref="queryRef"
                 :inline="true"
                 v-show="showSearch"
                 label-width="68px"
                 :label-style="{ color: '#000' }"
        >
            <el-form-item  prop="mediaId">
                <el-select
                        v-model="queryParams.mediaId"
                        placeholder="请选择媒体"
                        clearable
                        filterable
                        style="width: 150px;"
                >
                    <el-option
                            v-for="item in mediaOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item  prop="name">
                <el-select
                        v-model="queryParams.name"
                        placeholder="请选择应用名称"
                        clearable
                        filterable
                        style="width: 150px"
                >
                    <el-option
                            v-for="item in appOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item prop="enable">
                <el-select
                        v-model="queryParams.enable"
                        placeholder="请选择状态"
                        clearable
                        @keyup.enter="handleQuery"
                        style="width: 150px">
                    <el-option
                            v-for="dict in media_status"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"/>
                </el-select>
            </el-form-item>
            <el-form-item  prop="createTimeRange">
                <el-date-picker
                        v-model="createTimeRange"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="创建时间"
                        end-placeholder="开始时间"
                        value-format="YYYY-MM-DD"
                        style="width: 250px"
                />
            </el-form-item>
            <el-form-item>
                <el-button class="btn-blue" type="primary" icon="Search" @click="handleQuery">查 询</el-button>
                <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重 置</el-button>
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
                        v-hasPermi="['flow:app:add']"
                >添 加</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    class="btn-regge"
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['flow:app:export']"
                >导 入</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="appList" @selection-change="handleSelectionChange"
                  style="width: 100%"
                  :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
                  :cell-style="{ color: '#000' }"
                  border
                  table-layout="auto"
                  highlight-current-row="true"
        >
          <el-table-column label="ID" width="66" align="center" prop="id" />
          <el-table-column label="应用名称" show-overflow-tooltip align="center" width="180" prop="name">
            <template #default="scope">
              {{ getMediaAppName(scope.row) }}
            </template>
          </el-table-column>

            <el-table-column label="媒体名称" align="center" show-overflow-tooltip width="250" prop="mediaId">
                <template #default="scope">
                    {{ getMediaCompanyName(scope.row.mediaId) }}({{ scope.row.mediaId }})
                </template>
            </el-table-column>

            <el-table-column label="操作系统" align="center" prop="osType">
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
            <el-table-column label="包名" align="center" width="100" prop="pkg" />
            <el-table-column label="下载地址" align="center" prop="downloadUrl" />
<!--            <el-table-column label="鐘舵€? align="center" prop="enable">-->
<!--                <template #default="scope">-->
<!--                    <dict-tag :options="media_status" :value="scope.row.enable" />-->
<!--                </template>-->
<!--            </el-table-column>-->
          <el-table-column
              label="状 态"
              align="center"
              prop="enable"
              min-width="160"
          >
            <template #default="scope">
              <div class="status-wrap">
      <span
          class="status-dot"
          :class="getStatusClass(scope.row.enable)"></span>
                <dict-tag
                    :options="media_status"
                    :value="scope.row.enable"/>
              </div>
            </template>
          </el-table-column>

            <el-table-column label="创建时间" align="center" prop="createTime" />
            <el-table-column label="修改时间" align="center" prop="updateTime" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['flow:app:edit']">编 辑</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['flow:app:remove']">删 除</el-button>
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

        <!-- 娣诲姞鎴栦慨鏀瑰簲鐢ㄧ鐞嗗璇濇 -->
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
                    ref="appRef"
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
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="应用名称" prop="name">
                                <el-input v-model="form.name" placeholder="请输入应用名称" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="操作系统" prop="osType">
                                <el-select v-model="form.osType" placeholder="请选择操作系统" style="width: 100%">
                                    <el-option
                                        v-for="dict in os_type"
                                        :key="dict.value"
                                        :label="dict.label"
                                        :value="dict.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="接入方式" prop="accessType">
                                <el-select v-model="form.accessType" placeholder="请选择接入方式" style="width: 100%">
                                    <el-option
                                        v-for="dict in access_type"
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
                            <el-form-item label="包名" prop="pkg">
                                <el-input v-model="form.pkg" placeholder="请输入包名" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="下载地址" prop="downloadUrl">
                                <el-input v-model="form.downloadUrl" placeholder="请输入下载地址" />
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="状态" prop="enable">
                                <el-select v-model="form.enable" placeholder="请选择状态" style="width: 100%">
                                    <el-option
                                        v-for="dict in media_status"
                                        :key="dict.value"
                                        :label="dict.label"
                                        :value="dict.value"
                                    />
                                </el-select>
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

<script setup name="App">
import { listApp, getApp, delApp, addApp, updateApp } from "@/api/flow/app"
import { listMedia } from "@/api/flow/media"
import { Position } from "@element-plus/icons-vue"
const { proxy } = getCurrentInstance()

// 鑾峰彇瀛楀吀鏁版嵁
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

// 鍒涘缓濯掍綋ID鍒板叕鍙稿悕绉扮殑鏄犲皠
const mediaCompanyNameMap = ref(new Map())
// 濯掍綋閫夐」鍒楄〃
const mediaOptions = ref([])
// 搴旂敤閫夐」鍒楄〃
const appOptions = ref([])
// 鍒涘缓鏃堕棿鑼冨洿
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
            { required: true, message: "请选择媒体", trigger: "blur" }
        ],
        name: [
            { required: true, message: "请选择应用名称", trigger: "blur" }
        ],
        osType: [
            { required: true, message: "请选择操作系统", trigger: "change" }
        ],
        accessType: [
            { required: true, message: "请选择接入方式", trigger: "change" }
        ],
        enable: [
            { required: true, message: "请选择状态", trigger: "blur" }
        ],
        pkg: [
           {required:true,message: "请输入包名", trigger: "blur"}
      ],
        downloadUrl: [
            {required:true,message: "请输入下载地址", trigger: "blur"}
      ],
    }
})

const { queryParams, form, rules } = toRefs(data)

/** 鍔犺浇濯掍綋鍒楄〃锛屽缓绔嬪獟浣揑D鍒板叕鍙稿悕绉扮殑鏄犲皠 */
function loadMediaList() {
    listMedia({ pageNum: 1, pageSize: 1000 }).then(response => {
        mediaCompanyNameMap.value.clear()
        mediaOptions.value = []
        response.rows.forEach(media => {
            mediaCompanyNameMap.value.set(media.id, media.mediaCompanyName)
            // 娣诲姞鍒伴€夐」鍒楄〃锛岀敤浜庝笅鎷夐€夋嫨
            mediaOptions.value.push({
                value: media.id,
                label: `${media.mediaCompanyShort}(${media.id})`
            })
        })
    })
}

/** 鍔犺浇搴旂敤閫夐」鍒楄〃 */
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

/** 鏍规嵁濯掍綋ID鑾峰彇鍏徃鍚嶇О */
function getMediaCompanyName(mediaId) {
    return mediaCompanyNameMap.value.get(mediaId) || mediaId
}

function getMediaAppName(row) {
  return `${row.name}:(${row.id})`
}

/** 鏌ヨ搴旂敤绠＄悊鍒楄〃 */
function getList() {
    loading.value = true
    listApp(queryParams.value).then(response => {
        appList.value = response.rows
        total.value = response.total
        loading.value = false
    })
}

// 鍙栨秷鎸夐挳
function cancel() {
    open.value = false
    reset()
}

// 琛ㄥ崟閲嶇疆
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

/** 鎼滅储鎸夐挳鎿嶄綔 */
function handleQuery() {
    // 澶勭悊鏃堕棿鑼冨洿
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

/** 閲嶇疆鎸夐挳鎿嶄綔 */
function resetQuery() {
    createTimeRange.value = []
    queryParams.value.params.startTime = null
    queryParams.value.params.endTime = null
    proxy.resetForm("queryRef")
    handleQuery()
}

// 澶氶€夋閫変腑鏁版嵁
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 鏂板鎸夐挳鎿嶄綔 */
function handleAdd() {
    reset()
    open.value = true
    title.value = "娣诲姞搴旂敤绠＄悊"
}

/** 淇敼鎸夐挳鎿嶄綔 */
function handleUpdate(row) {
    reset()
    const _id = row.id || ids.value
    getApp(_id).then(response => {
        // 灏嗘暟瀛楃被鍨嬬殑瀛楀吀鍊艰浆鎹负瀛楃涓诧紝浠ヤ究涓庡瓧鍏竩alue鍖归厤
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
        title.value = "修改应用信息"
    })
}

/** 鎻愪氦鎸夐挳 */
function submitForm() {
    proxy.$refs["appRef"].validate(valid => {
        if (valid) {
            if (form.value.id != null) {
                updateApp(form.value).then(response => {
                    proxy.$modal.msgSuccess("编辑成功")
                    open.value = false
                    getList()
                })
            } else {
                addApp(form.value).then(response => {
                    proxy.$modal.msgSuccess("添加成功")
                    open.value = false
                    getList()
                })
            }
        }
    })
}

// 鐘舵€侀鑹?
const getStatusClass = (status) => {
  switch (status) {
    case 1:
      return 'dot-success'
    case 0:
      return 'dot-danger'
    case 2:
      return 'dot-warning'
    case 3:
      return 'dot-danger'
    default:
      return ''
  }
}

/** 鍒犻櫎鎸夐挳鎿嶄綔 */
function handleDelete(row) {
    const _ids = row.id || ids.value
    proxy.$modal.confirm('是否确认删除应用编号为' + _ids + '"的数据项？').then(function() {
        return delApp(_ids)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除应用成功")
    }).catch(() => {})
}

/** 瀵煎嚭鎸夐挳鎿嶄綔 */
function handleExport() {
    proxy.download('flow/app/export', {
        ...queryParams.value
    }, `app_${new Date().getTime()}.xlsx`)
}

// 椤甸潰鍔犺浇鏃惰幏鍙栧獟浣撳垪琛ㄥ拰搴旂敤閫夐」
loadMediaList()
loadAppOptions()
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
  margin-right: 8px;   /* 馃憟 璋冨皬闂磋窛 */
  margin-bottom: 8px;
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


/* 缁胯壊锛堟甯革級 */
.dot-success {
  background-color: #67c23a;
}

/* 绾㈣壊锛堢鐢?鎷掔粷锛?*/
.dot-danger {
  background-color: #f56c6c;
}

/* 榛勮壊锛堝鏍镐腑锛?*/
.dot-warning {
  background-color: #e6a23c;
}

.el-form--inline .el-form-item {
  margin-right: 8px;  /* 榛樿涓€鑸槸 18px+ */
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
  white-space: nowrap; /* 鍏抽敭锛氱姝㈡崲琛?*/
}

.status-dot {
  margin-right: 6px;
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

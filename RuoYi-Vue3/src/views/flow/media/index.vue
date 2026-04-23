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
            <el-form-item label="" prop="mediaCompanyName">
                <el-input
                    v-model="queryParams.mediaCompanyName"
                    placeholder="请输入公司名称"
                    clearable
                    style="width: 150px;"
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="" prop="mediaCompanyShort">
                <el-input
                    v-model="queryParams.mediaCompanyShort"
                    placeholder="请输入公司简称"
                    clearable
                    style="width: 150px;"
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="" prop="enable">
                <el-select
                    v-model="queryParams.enable"
                    placeholder="请选择状态"
                    clearable
                    @keyup.enter="handleQuery"
                    style="width: 150px;"
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
                <el-button class="btn-blue" icon="Search" @click="handleQuery">查 询</el-button>
                <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重 置</el-button>
            </el-form-item>
        </el-form>

      <el-row :gutter="10" class="mb8" type="flex" :wrap="false">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    icon="Plus"
                    @click="handleAdd"
                    v-hasPermi="['flow:media:add']"
                    class="btn-blue"
                >添 加</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    class="btn-regge"
                    type="warning"
                    plain
                    icon="Download"
                    @click="handleExport"
                    v-hasPermi="['flow:media:export']"
                >导 入</el-button>
            </el-col>

        </el-row>

        <el-table
            v-loading="loading"
            :data="mediaList"
            @selection-change="handleSelectionChange"
            style="width: 100%"
            :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
            :cell-style="{ color: '#000' }"
            border
            table-layout="auto"
            highlight-current-row="true"
        >
          <el-table-column label="ID" align="center" width="66" prop="id" />
            <el-table-column label="媒体名称" align="center" width="260" show-overflow-tooltip prop="name">
                <template #default="scope">
                    {{ formatMediaName(scope.row) }}
                </template>
            </el-table-column>
            <el-table-column label="公司名称" align="center" show-overflow-tooltip width="150" prop="mediaCompanyName" />
            <el-table-column label="公司简称" align="center" prop="mediaCompanyShort" />
          <el-table-column label="法人姓名" align="center" prop="mediaOwnerName" />
          <el-table-column label="联系人" align="center" prop="contactName" />
          <el-table-column label="联系电话" align="center" show-overflow-tooltip prop="contactPhone" />
          <el-table-column label="联系邮箱" align="center" show-overflow-tooltip prop="contactEmail" />
          <el-table-column
              label="状态"
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
          <el-table-column label="跟新时间" align="center" prop="updateTime" />

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link   type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['flow:media:edit']">编 辑</el-button>
                    <el-button link   type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['flow:media:remove']">删 除</el-button>
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

        <!-- 娣诲姞鎴栦慨鏀瑰獟浣撶鐞嗕晶杈规爮 -->
        <el-drawer
            v-model="open"
            direction="rtl"
            size="72%"
            class="media-drawer"
            :show-close="false"
            style="background: #f2f5f7"
        >
          <template #header>
            <div class="drawer-header">
              <!-- 第一行：返回 + 标题 -->
              <div class="header-line">
                <el-icon class="back-icon" @click="cancel">
                  <Close />
                </el-icon>
                <span class="drawer-title">{{ title }}</span>
              </div>

              <!-- 分割线 -->
              <el-divider class="dividerClass" />

              <!-- 第二行：按钮 -->
              <div class="drawer-actions">
                <el-button class="btn-blue" @click="submitForm">
                  <el-icon>
                    <Position />
                  </el-icon>
                  提交
                </el-button>
              </div>
            </div>
          </template>
          <div class="drawer-content">
            <el-form ref="mediaRef"
                     class="drawer-form"
                     :model="form"
                     :rules="rules"
                     label-width="88px">
              <div class="title-content1">
                <div class="title-content2">基本信息</div>
                <el-divider class="dividerClass1" />
              </div>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="媒体名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入媒体名称" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="公司名称" prop="mediaCompanyName">
                    <el-input v-model="form.mediaCompanyName" placeholder="请输入公司名称" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="账号名" prop="account">
                    <el-input v-model="form.account" placeholder="请求输入账户名" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="密 码" prop="password" show-password>
                    <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
              <el-form-item label="公司简称" prop="mediaCompanyShort">
                <el-input v-model="form.mediaCompanyShort" placeholder="请输入公司简称" />
              </el-form-item>
                </el-col>
                <el-col :span="12">
              <el-form-item label="信用代码" prop="mediaCompanyCode">
                <el-input v-model="form.mediaCompanyCode" placeholder="请输入信用代码" />
              </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
<!--                <el-col :span="12">-->
<!--              <el-form-item label="钀ヤ笟鎵х収" prop="mediaCompanyLicense">-->
<!--                <el-input v-model="form.mediaCompanyLicense" placeholder="璇疯緭鍏ヨ惀涓氭墽鐓х収鐗? />-->
<!--              </el-form-item>-->
<!--                </el-col>-->
                <el-col :span="12">
                  <el-form-item label="状 态" prop="enable">
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
                <el-col :span="12">
                <el-form-item label="公司地址" prop="mediaCompanyAddress">
                <el-input v-model="form.mediaCompanyAddress" placeholder="请输入公司地址" />
              </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
              <el-form-item label="法人姓名" prop="mediaOwnerName">
                <el-input v-model="form.mediaOwnerName" placeholder="请输入法人姓名" />
              </el-form-item>
                </el-col>
                <el-col :span="12">
              <el-form-item label="联系人" prop="contactName">
                <el-input v-model="form.contactName" placeholder="请输入联系人" />
              </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
              <el-form-item label="联系电话" prop="contactPhone">
                <el-input v-model="form.contactPhone" placeholder="请输入联系人电话" />
              </el-form-item>
                </el-col>
                <el-col :span="12">
              <el-form-item label="联系邮箱" prop="contactEmail">
                <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />

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

<script setup name="Media">
import { listMedia, getMedia, delMedia, addMedia, updateMedia } from "@/api/flow/media"
import {Position} from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance()

// 浣跨敤 proxy.useDict 鑾峰彇瀛楀吀鏁版嵁锛屽弬鑰冭彍鍗曠鐞嗛〉闈㈢殑瀹炵幇
const { media_status } = proxy.useDict("media_status")
// 濯掍綋閫夐」鍒楄〃
const mediaOptions = ref([])
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
        mediaId: null,
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
            { required: true, message: "请输入账户", trigger: "blur" }
        ],
        password: [
            { required: true, message: "请输入密码", trigger: "blur" }
        ],
        enable: [
            { required: true, message: "请输入用户状态", trigger: "blur" }
        ],
    }
})

const { queryParams, form, rules } = toRefs(data)

/** 鏍煎紡鍖栧獟浣撳悕绉帮細name(鍏徃ID) */
function formatMediaName(row) {
    return `${row.name}:(${row.id})`
}

/** 鏌ヨ濯掍綋绠＄悊鍒楄〃 */
function getList() {
    loading.value = true
    listMedia(queryParams.value).then(response => {
        mediaList.value = response.rows
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

/** 鍔犺浇濯掍綋鍒楄〃锛屽缓绔嬪獟浣揑D鍒板叕鍙稿悕绉扮殑鏄犲皠 */
function loadMediaList() {
  listMedia({ pageNum: 1, pageSize: 1000 })
    .then(response => {
      const rows = Array.isArray(response?.rows) ? response.rows : []
      mediaOptions.value = rows.map(media => ({
        value: media.id,
        label: `${media.mediaCompanyName || media.name || media.id}(${media.id})`
      }))
    })
    .catch(() => {
      mediaOptions.value = []
    })
}


/** 鎼滅储鎸夐挳鎿嶄綔 */
function handleQuery() {
    queryParams.value.pageNum = 1
    getList()
}

/** 閲嶇疆鎸夐挳鎿嶄綔 */
function resetQuery() {
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
    title.value = "添加媒体信息"
}

/** 淇敼鎸夐挳鎿嶄綔 */
function handleUpdate(row) {
    reset()
    const _id = row.id || ids.value
    getMedia(_id).then(response => {
        form.value = response.data
        // 灏?enable 浠?int 杞崲涓?string锛岄€傞厤 el-select
        if (form.value.enable !== null && form.value.enable !== undefined) {
            form.value.enable = String(form.value.enable)
        }
        open.value = true
        title.value = "修改媒体信息"
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

/** 鎻愪氦鎸夐挳 */
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
                    proxy.$modal.msgSuccess("添加成功")
                    open.value = false
                    getList()
                })
            }
        }
    })
}

/** 鍒犻櫎鎸夐挳鎿嶄綔 */
function handleDelete(row) {
    const _ids = row.id || ids.value
    proxy.$modal.confirm('是否确认删除媒体编号为"' + _ids + '"的数据项？').then(function() {
        return delMedia(_ids)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {})
}

/** 瀵煎嚭鎸夐挳鎿嶄綔 */
function handleExport() {
    proxy.download('flow/media/export', {
        ...queryParams.value
    }, `media_${new Date().getTime()}.xlsx`)
}

getList()
loadMediaList()
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
  margin-right: 8px;
  margin-bottom: 8px;
}

.status-wrap {
  display: inline-flex;
  align-items: center;
  white-space: nowrap;
}

.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
}

.dot-success {
  background-color: #67c23a;
}

.dot-danger {
  background-color: #f56c6c;
}

.dot-warning {
  background-color: #e6a23c;
}



:deep(.media-drawer .el-drawer__header) {
  margin-bottom: 0;
  padding: 0;
  background: transparent;
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

.back-btn {
  width: 34px;
  height: 34px;
  color: #2A5FB7;
  background: #ffffff;
}

.back-btn:hover {
  color: #1f4f96;
  background: #f5f9ff;
  border-color: #bdd2ef;
}

.back-icon {
  cursor: pointer;
  font-size: 18px;
}

.header-title-wrap {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.drawer-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.drawer-subtitle {
  font-size: 12px;
  color: #6b7b91;
  line-height: 1.4;
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

.submit-btn {
  min-width: 96px;
  box-shadow: 0 8px 18px rgba(42, 95, 183, 0.2);
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

/* 第一行 */
.header-line {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 20px 0 0 20px;
}


.drawer-form {
  width: min(980px, 100%);
  margin: 0 auto;
  padding: 24px 26px 18px;
  background: #ffffff;

  border: 1px solid #e3eaf5;
  box-shadow: 0 12px 30px rgba(36, 76, 140, 0.08);
}

.title-content1 {
  width: 100%;
  margin-bottom: 18px;
  padding-bottom: 10px;
  border-bottom: 1px solid #edf1f7;
}

.title-content2 {
  width: 100%;
  position: relative;
  padding-left: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #253043;
}

.title-content2::before {
  content: "";
  position: absolute;
  left: 0;
  top: 3px;
  width: 4px;
  height: 16px;
  background: #2A5FB7;
}

.dividerClass1 {
  margin: 10px 0;
}

.drawer-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.drawer-form :deep(.el-form-item__label) {
  color: #4d5e74;
  font-weight: 500;
}

.drawer-form :deep(.el-input__wrapper),
.drawer-form :deep(.el-textarea__inner) {
  border-radius: 8px;
}

@media (max-width: 1200px) {
  .drawer-content {
    padding: 16px;
  }

  .drawer-form {
    padding: 20px;
  }
}

@media (max-width: 768px) {
  .drawer-header {
    flex-direction: column;
    align-items: flex-start;
    padding: 14px 16px;
  }

  .drawer-actions {
    display: flex;
    justify-content: flex-start;  /* 靠左 */
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

  .drawer-form {
    padding: 16px 14px;
    border-radius: 10px;
  }
}
</style>



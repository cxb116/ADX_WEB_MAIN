<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流量广告位Id" prop="sspSlotId">
        <el-input
          v-model="queryParams.sspSlotId"
          placeholder="请输入流量广告位Id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预算广告位id" prop="dspSlotId">
        <el-input
          v-model="queryParams.dspSlotId"
          placeholder="请输入预算广告位id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流量权重" prop="trafficWeight">
        <el-input
          v-model="queryParams.trafficWeight"
          placeholder="请输入流量权重"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投放策略: 1对接第三方，2：自主投放" prop="launchStrategy">
        <el-input
          v-model="queryParams.launchStrategy"
          placeholder="请输入投放策略: 1对接第三方，2：自主投放"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="底价 (给上游媒体底价,预算是rtb的时候就需要这个值)" prop="floorPrice">
        <el-input
          v-model="queryParams.floorPrice"
          placeholder="请输入底价 (给上游媒体底价,预算是rtb的时候就需要这个值)"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ip限流次数" prop="ipLimit">
        <el-input
          v-model="queryParams.ipLimit"
          placeholder="请输入ip限流次数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="捕获日志时长" prop="logCaptureAt">
        <el-input
          v-model="queryParams.logCaptureAt"
          placeholder="请输入捕获日志时长"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上报黑名单" prop="trackSchwarz">
        <el-input
          v-model="queryParams.trackSchwarz"
          placeholder="请输入上报黑名单"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求次数" prop="req">
        <el-input
          v-model="queryParams.req"
          placeholder="请输入请求次数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="展现次数" prop="ims">
        <el-input
          v-model="queryParams.ims"
          placeholder="请输入展现次数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点击次数" prop="clk">
        <el-input
          v-model="queryParams.clk"
          placeholder="请输入点击次数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投放时段 1全时段 2 自定义" prop="launchTime">
        <el-input
          v-model="queryParams.launchTime"
          placeholder="请输入投放时段 1全时段 2 自定义"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地域定向 1 不限制，2 定向，3排除" prop="regionDirection">
        <el-input
          v-model="queryParams.regionDirection"
          placeholder="请输入地域定向 1 不限制，2 定向，3排除"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品牌定向 1 不限制，2 定向，3排除" prop="brandDirection">
        <el-input
          v-model="queryParams.brandDirection"
          placeholder="请输入品牌定向 1 不限制，2 定向，3排除"
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
          v-hasPermi="['dspLaunch:launch:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['dspLaunch:launch:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['dspLaunch:launch:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['dspLaunch:launch:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="launchList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="流量广告位Id" align="center" prop="sspSlotId" />
      <el-table-column label="预算广告位id" align="center" prop="dspSlotId" />
      <el-table-column label="流量权重" align="center" prop="trafficWeight" />
      <el-table-column label="投放策略: 1对接第三方，2：自主投放" align="center" prop="launchStrategy" />
      <el-table-column label="底价 (给上游媒体底价,预算是rtb的时候就需要这个值)" align="center" prop="floorPrice" />
      <el-table-column label="ip限流次数" align="center" prop="ipLimit" />
      <el-table-column label="捕获日志时长" align="center" prop="logCaptureAt" />
      <el-table-column label="上报黑名单" align="center" prop="trackSchwarz" />
      <el-table-column label="请求次数" align="center" prop="req" />
      <el-table-column label="展现次数" align="center" prop="ims" />
      <el-table-column label="点击次数" align="center" prop="clk" />
      <el-table-column label="投放时段 1全时段 2 自定义" align="center" prop="launchTime" />
      <el-table-column label="地域定向 1 不限制，2 定向，3排除" align="center" prop="regionDirection" />
      <el-table-column label="品牌定向 1 不限制，2 定向，3排除" align="center" prop="brandDirection" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['dspLaunch:launch:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['dspLaunch:launch:remove']">删除</el-button>
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

    <!-- 添加或修改广告生成对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="launchRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流量广告位Id" prop="sspSlotId">
          <el-input v-model="form.sspSlotId" placeholder="请输入流量广告位Id" />
        </el-form-item>
        <el-form-item label="预算广告位id" prop="dspSlotId">
          <el-input v-model="form.dspSlotId" placeholder="请输入预算广告位id" />
        </el-form-item>
        <el-form-item label="流量权重" prop="trafficWeight">
          <el-input v-model="form.trafficWeight" placeholder="请输入流量权重" />
        </el-form-item>
        <el-form-item label="投放策略: 1对接第三方，2：自主投放" prop="launchStrategy">
          <el-input v-model="form.launchStrategy" placeholder="请输入投放策略: 1对接第三方，2：自主投放" />
        </el-form-item>
        <el-form-item label="底价 (给上游媒体底价,预算是rtb的时候就需要这个值)" prop="floorPrice">
          <el-input v-model="form.floorPrice" placeholder="请输入底价 (给上游媒体底价,预算是rtb的时候就需要这个值)" />
        </el-form-item>
        <el-form-item label="ip限流次数" prop="ipLimit">
          <el-input v-model="form.ipLimit" placeholder="请输入ip限流次数" />
        </el-form-item>
        <el-form-item label="捕获日志时长" prop="logCaptureAt">
          <el-input v-model="form.logCaptureAt" placeholder="请输入捕获日志时长" />
        </el-form-item>
        <el-form-item label="上报黑名单" prop="trackSchwarz">
          <el-input v-model="form.trackSchwarz" placeholder="请输入上报黑名单" />
        </el-form-item>
        <el-form-item label="请求次数" prop="req">
          <el-input v-model="form.req" placeholder="请输入请求次数" />
        </el-form-item>
        <el-form-item label="展现次数" prop="ims">
          <el-input v-model="form.ims" placeholder="请输入展现次数" />
        </el-form-item>
        <el-form-item label="点击次数" prop="clk">
          <el-input v-model="form.clk" placeholder="请输入点击次数" />
        </el-form-item>
        <el-form-item label="投放时段 1全时段 2 自定义" prop="launchTime">
          <el-input v-model="form.launchTime" placeholder="请输入投放时段 1全时段 2 自定义" />
        </el-form-item>
        <el-form-item label="地域定向 1 不限制，2 定向，3排除" prop="regionDirection">
          <el-input v-model="form.regionDirection" placeholder="请输入地域定向 1 不限制，2 定向，3排除" />
        </el-form-item>
        <el-form-item label="品牌定向 1 不限制，2 定向，3排除" prop="brandDirection">
          <el-input v-model="form.brandDirection" placeholder="请输入品牌定向 1 不限制，2 定向，3排除" />
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

<script setup name="Launch">
import { listLaunch, getLaunch, delLaunch, addLaunch, updateLaunch } from "@/api/dspLaunch/launch"

const { proxy } = getCurrentInstance()

const launchList = ref([])
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
    sspSlotId: null,
    dspSlotId: null,
    trafficWeight: null,
    launchStrategy: null,
    floorPrice: null,
    ipLimit: null,
    logCaptureAt: null,
    trackSchwarz: null,
    req: null,
    ims: null,
    clk: null,
    launchTime: null,
    regionDirection: null,
    brandDirection: null,
  },
  rules: {
    sspSlotId: [
      { required: true, message: "流量广告位Id不能为空", trigger: "blur" }
    ],
    dspSlotId: [
      { required: true, message: "预算广告位id不能为空", trigger: "blur" }
    ],
    trafficWeight: [
      { required: true, message: "流量权重不能为空", trigger: "blur" }
    ],
    launchStrategy: [
      { required: true, message: "投放策略: 1对接第三方，2：自主投放不能为空", trigger: "blur" }
    ],
    floorPrice: [
      { required: true, message: "底价 (给上游媒体底价,预算是rtb的时候就需要这个值)不能为空", trigger: "blur" }
    ],
    logCaptureAt: [
      { required: true, message: "捕获日志时长不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询广告生成列表 */
function getList() {
  loading.value = true
  listLaunch(queryParams.value).then(response => {
    launchList.value = response.rows
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
    sspSlotId: null,
    dspSlotId: null,
    trafficWeight: null,
    launchStrategy: null,
    floorPrice: null,
    ipLimit: null,
    logCaptureAt: null,
    trackSchwarz: null,
    req: null,
    ims: null,
    clk: null,
    launchTime: null,
    regionDirection: null,
    brandDirection: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("launchRef")
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
  title.value = "添加广告生成"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getLaunch(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改广告生成"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["launchRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateLaunch(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addLaunch(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除广告生成编号为"' + _ids + '"的数据项？').then(function() {
    return delLaunch(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('dspLaunch/launch/export', {
    ...queryParams.value
  }, `launch_${new Date().getTime()}.xlsx`)
}

getList()
</script>

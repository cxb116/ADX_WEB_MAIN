<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="广告场景" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入广告场景"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="广告类型" prop="typeId">
        <el-select
          v-model="queryParams.typeId"
          placeholder="请选择广告类型"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="item in typeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间">
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
          v-hasPermi="['ad:scene:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ad:scene:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ad:scene:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['ad:scene:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sceneList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" width="40"  prop="id" />
      <el-table-column label="广告类型" align="center" width="120" prop="typeId">
        <template #default="scope">
          {{ getTypeName(scope.row.typeId) }}
        </template>
      </el-table-column>
      <el-table-column label="广告场景" align="center" width="120" prop="name" />

      <el-table-column label="创建时间" align="center" width="200" prop="createTime" />
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['ad:scene:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['ad:scene:remove']">删除</el-button>
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

    <!-- 添加或修改广告场景对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="sceneRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="广告类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择广告类型" style="width: 100%">
            <el-option
              v-for="item in typeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="广告场景" prop="name">
          <el-input v-model="form.name" placeholder="请输入广告场景" />
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

<script setup name="Scene">
import { listScene, getScene, delScene, addScene, updateScene } from "@/api/ad/scene"
import { listType, getType, delType, addType, updateType } from "@/api/ad/type"
const { proxy } = getCurrentInstance()

const sceneList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 广告类型列表
const typeList = ref([])

// 根据 typeId 获取广告类型名称
function getTypeName(typeId) {
  const type = typeList.value.find(item => item.id === typeId)
  return type ? type.name : typeId
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    typeId: null,
    createTime: null,
  },
  rules: {
    name: [
      { required: true, message: "广告场景不能为空", trigger: "blur" }
    ],
    typeId: [
      { required: true, message: "广告类型Id不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 加载广告类型列表 */
function loadTypeList() {
  listType().then(response => {
    typeList.value = response.rows
  })
}

/** 查询广告场景列表 */
function getList() {
  loading.value = true
  listScene(queryParams.value).then(response => {
    sceneList.value = response.rows
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
    typeId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("sceneRef")
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
  title.value = "添加广告场景"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getScene(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改广告场景"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["sceneRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateScene(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addScene(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除广告场景编号为"' + _ids + '"的数据项？').then(function() {
    return delScene(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ad/scene/export', {
    ...queryParams.value
  }, `scene_${new Date().getTime()}.xlsx`)
}

// 页面加载时获取广告类型列表
loadTypeList()
getList()
</script>

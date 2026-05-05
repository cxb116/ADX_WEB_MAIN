<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="广告位列表" name="list">
        <div class="app-container-list">
    <el-form :model="queryParams"
             ref="queryRef" :inline="true"
             v-show="showSearch" label-width="68px">

      <el-form-item label="" prop="mediaId">
        <el-select
            v-model="queryParams.mediaId"
            placeholder="请选择媒体"
            clearable
            filterable
            style="width: 150px;"
            @change="handleSearchMediaChange"
        >
          <el-option
              v-for="item in searchMediaOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="" prop="appId">
        <el-select
            v-model="queryParams.appId"
            placeholder="请选择应用"
            clearable
            filterable
            style="width: 150px;"
        >
          <el-option
              v-for="item in searchAppOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="" prop="osType">
        <el-select
            v-model="queryParams.osType"
            placeholder="请选择操作系统"
            clearable
            style="width: 150px;"
        >
          <el-option
              v-for="dict in os_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="" prop="adTypeId">
        <el-select
          v-model="queryParams.adTypeId"
          placeholder="请选择广告类型"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="item in adTypeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="" label-width="110px" prop="nameAlise">
        <el-input
          v-model="queryParams.nameAlise"
          placeholder="请输入内部广告位名称"
          clearable
          @keyup.enter="handleQuery"
          style="width: 150px;"
        />
      </el-form-item>

      <el-form-item label="" prop="accessType">
        <el-select
          v-model="queryParams.accessType"
          placeholder="请选择接入方式"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="dict in access_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="sspPayType">
        <el-select
          v-model="queryParams.sspPayType"
          placeholder="请选择结算方式"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="dict in ssp_pay_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="enable">
        <el-select
          v-model="queryParams.enable"
          placeholder="请选择状态"
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="dict in audit_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label-width="110px" prop="id">
        <el-input
            v-model="queryParams.idStr"
            type="textarea"
            :rows="2"
            placeholder="请输入广告位ID(每行一个)"
            clearable
            style="width: 150px;"
        />
      </el-form-item>
      <el-form-item>
        <el-button class="btn-blue" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button class="btn-blue" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            plain
            icon="Plus"
            class="btn-blue"
            @click="handleAdd"
            v-hasPermi="['flow:media:add']"
        >
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['flow:mediaAd:export']"
          class="btn-regge"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

          <el-table
              v-loading="loading"
              :data="mediaAdList"
              @selection-change="handleSelectionChange"
              class="media-ad-table my-table"
              style="width: 100%"
              :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
              :cell-style="{ color: '#000' }"
              border
              table-layout="auto"
              highlight-current-row="true"
          >
      <el-table-column label="广告位ID" align="center" prop="id" width="100" fixed />
      <el-table-column label="广告位名称" align="center" prop="name" width="250" fixed>
        <template #default="scope">
          {{ scope.row.name }}({{ scope.row.id }})
        </template>
      </el-table-column>
      <el-table-column label="内部广告位名称" align="center" prop="nameAlise" width="230" />
      <el-table-column label="广告类型" align="center" prop="adTypeId" width="150">
        <template #default="scope">
          {{ getAdTypeName(scope.row.adTypeId) }}
        </template>
      </el-table-column>
      <el-table-column label="广告场景" align="center" prop="adSceneId" width="160">
        <template #default="scope">
          {{ scope.row.adSceneId ? getAdSceneName(scope.row.adSceneId) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="广告尺寸" align="center" prop="adSizeId" width="120">
        <template #default="scope">
          {{ scope.row.adSizeId ? getAdSizeName(scope.row.adSizeId) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="操作系统" align="center" prop="osType" width="120">
        <template #default="scope">
          <span v-if="scope.row.osType && scope.row.osType != '0'">
            <dict-tag :options="os_type" :value="String(scope.row.osType)" />
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>

<!--            <el-table-column label="状态" align="center" width="120" prop="enable">-->
<!--              <template #default="scope">-->
<!--                <dict-tag :options="audit_status" :value="String(scope.row.enable)" />-->
<!--              </template>-->
<!--            </el-table-column>-->
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
            <el-table-column label="结算方式" align="center" width="120" prop="sspPayType">
              <template #default="scope">
                <dict-tag :options="ssp_pay_type" :value="String(scope.row.sspPayType)" />
              </template>
            </el-table-column>
      <el-table-column label="接入方式" align="center" prop="accessType" width="120">
        <template #default="scope">
          <span v-if="scope.row.accessType && scope.row.accessType != '0'">
            <dict-tag :options="access_type" :value="String(scope.row.accessType)" />
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="应用" align="center" prop="appId" width="170">
        <template #default="scope">
          {{ getAppName(scope.row.appId) }}
        </template>
      </el-table-column>

<!--      <el-table-column label="交互类型" align="center" width="120" prop="interactionType" />-->

      <el-table-column label="创建时间" align="center" width="200" prop="createTime"/>
      <el-table-column label="修改时间" align="center" width="200" prop="updateTime"/>
      <el-table-column label="操作" align="center" width="200" fixed="right" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['flow:mediaAd:edit']">修改</el-button>
          <el-button link type="primary" icon="Setting" @click="handleConfig(scope.row)" v-hasPermi="['flow:mediaAd:config']">配置</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['flow:mediaAd:remove']">删除</el-button>
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
        </div>
      </el-tab-pane>

      <!-- 编辑 Tab -->
      <el-tab-pane v-if="false" label="编辑广告位" name="edit">
        <div class="app-container-edit" v-if="editMediaAd || activeTab === 'edit'">
<el-card class="edit-card">
            <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="媒体" prop="mediaId">
                    <el-select
                      v-model="editForm.mediaId"
                      placeholder="请选择媒体"
                      clearable
                      filterable
                      style="width: 100%"
                      @change="handleEditMediaChange"
                    >
                      <el-option
                        v-for="item in editMediaOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="应用" prop="appId">
                    <el-select
                      v-model="editForm.appId"
                      placeholder="请选择应用"
                      clearable
                      filterable
                      style="width: 100%"
                      :disabled="!editForm.mediaId"
                    >
                      <el-option
                        v-for="item in editAppOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="广告位名称" prop="name">
                    <el-input v-model="editForm.name" placeholder="请输入广告位名称" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="内部广告位名称" prop="nameAlise">
                    <el-input v-model="editForm.nameAlise" placeholder="请输入内部广告位名称" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="广告类型" prop="adTypeId">
                    <el-select
                      v-model="editForm.adTypeId"
                      placeholder="请先选择广告类型"
                      clearable
                      filterable
                      style="width: 100%"
                      @change="handleAdTypeChange"
                    >
                      <el-option
                        v-for="item in adTypeList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="广告场景" prop="adSceneId">
                    <el-select
                      v-model="editForm.adSceneId"
                      placeholder="请选择广告场景"
                      clearable
                      filterable
                      style="width: 100%"
                      :disabled="!editForm.adTypeId"
                    >
                      <el-option
                        v-for="item in filteredAdSceneList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="样式尺寸" prop="adSizeId">
                    <el-select
                      v-model="editForm.adSizeId"
                      placeholder="请选择样式尺寸"
                      clearable
                      filterable
                      style="width: 100%"
                      :disabled="!editForm.adTypeId"
                    >
                      <el-option
                        v-for="item in filteredAdSizeList"
                        :key="item.id"
                        :label="item.size"
                        :value="item.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="操作系统" prop="osType">
                    <el-select v-model="editForm.osType" placeholder="请选择操作系统" style="width: 100%" disabled>
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
                    <el-select v-model="editForm.accessType" placeholder="请选择接入方式" style="width: 100%" disabled>
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
                  <el-form-item label="结算方式" prop="sspPayType">
                    <el-select v-model="editForm.sspPayType" placeholder="请选择结算方式" style="width: 100%" @change="handleEditSspPayTypeChange">
                      <el-option
                        v-for="dict in ssp_pay_type"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="分成系数" prop="sspDealRatio">
                    <el-input
                      v-model="editForm.sspDealRatio"
                      placeholder="请输入分成系数"
                      :disabled="editForm.sspPayType === '2'"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
<!--                <el-col :span="12">-->
<!--                  <el-form-item label="交互类型" prop="interactionType">-->
<!--                    <div class="interaction-type-selector-wrapper">-->
<!--                      <el-checkbox-group v-model="selectedInteractionTypes" @change="handleInteractionTypeGroupChange">-->
<!--                        <el-checkbox-->
<!--                          v-for="item in interactionTypeOptions"-->
<!--                          :key="item.value"-->
<!--                          :value="item.value"-->
<!--                          :label="item.value"-->
<!--                          border-->
<!--                        >-->
<!--                          {{ item.label }}-->
<!--                        </el-checkbox>-->
<!--                      </el-checkbox-group>-->
<!--                      <div class="interaction-hint">-->
<!--                        <el-icon color="#909399"><InfoFilled /></el-icon>-->
<!--                        <span>已选择 {{ getSelectedInteractionTypes().length }} 种交互类型，位掩码值：{{ editForm.interactionType }}</span>-->
<!--                      </div>-->
<!--                    </div>-->
<!--                  </el-form-item>-->
<!--                </el-col>-->
                <el-col :span="12">
                  <el-form-item label="广告位图片" prop="adImage">
                    <image-upload v-model="editForm.adImage"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="状态" prop="enable">
                    <el-select v-model="editForm.enable" placeholder="请选择状态" style="width: 100%">
                      <el-option
                        v-for="dict in audit_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item label="备注" prop="remark">
                    <el-input v-model="editForm.remark" type="textarea" placeholder="请输入备注" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </div>
        <div v-else class="empty-edit">
          <el-empty description="请选择一个广告位进行编辑" />
        </div>
      </el-tab-pane>
    </el-tabs>

      <!-- 配置抽屉 -->
      <el-drawer
        v-model="configOpen"
        direction="rtl"
        size="1400px"
        :show-close="false"
        style="background: #f2f5f7"
        @close="closeConfigDrawer"
      >
                <template #header>
          <div class="drawer-header">
            <div class="header-top">
              <el-icon class="back-icon" @click="closeConfigDrawer">
                <Close />
              </el-icon>
              <span class="drawer-title">配置</span>
            </div>
            <el-divider class="dividerClass" />
            <div class="drawer-actions">
              <el-button class="btn-blue" @click="handleSaveConfig" :disabled="!configMediaAd">保存配置</el-button>
            </div>
          </div>
        </template>
<div class="app-container-config" v-if="configMediaAd">
<el-card class="config-card">
            <template #header>
              <div class="card-header">
                <span>基础信息</span>
              </div>
            </template>
            <div class="basic-info-container">
              <table class="basic-info-table">
                <tbody>
                <tr>
                  <th>广告位名称</th>
                  <td>{{ configMediaAd.name ? `${configMediaAd.name}(${configMediaAd.id})` : '-' }}</td>
                  <th>应用</th>
                  <td>{{ getAppName(configMediaAd.appId) || '-' }}</td>

                </tr>
                <tr>
                  <th>媒体简称</th>
                  <td>{{ getMediaName(configMediaAd.mediaId) || '-' }}</td>
                  <th>内部名称</th>
                  <td>{{ configMediaAd.nameAlise || '-' }}</td>
                </tr>
                <tr>
                  <th>操作系统</th>
                  <td>
                    <template v-if="appInfo">
                      <el-tag v-if="appInfo.osType === 1" >android</el-tag>
                      <el-tag v-else-if="appInfo.osType === 2">Ios</el-tag>
                      <el-tag v-else type="info">未知</el-tag>
                    </template>
                  </td>
                  <th>广告类型</th>
                  <td>{{ getAdTypeName(configMediaAd.adTypeId) || '-' }}</td>
                </tr>
                </tbody>
              </table>

              <div class="settlement-section">
                <el-form :model="configMediaAd" label-width="86px" class="settlement-form" size="small">
                  <el-row :gutter="16">
                    <el-col :lg="12" :md="12" :sm="24" :xs="24">
                      <el-form-item label="结算方式">
                        <el-select
                          v-model="configMediaAd.sspPayType"
                          placeholder="请选择结算方式"
                          style="width: 100%"
                          @change="handleConfigSspPayTypeChange"
                        >
                          <el-option
                            v-for="dict in ssp_pay_type"
                            :key="dict.value"
                            :label="dict.label"
                            :value="String(dict.value)"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :lg="12" :md="12" :sm="24" :xs="24">
                      <el-form-item label="分成系数" v-if="String(configMediaAd.sspPayType) === '1'">
                        <el-input
                          v-model="configMediaAd.sspDealRatio"
                          placeholder="请输入分成系数"
                        />
                      </el-form-item>
                      <el-form-item label="CPM"  v-else-if="String(configMediaAd.sspPayType) === '3'">
                        <el-input
                        v-model="configMediaAd.fixedPrice"
                        placeholder="请输入千次收益">
                        </el-input>
                      </el-form-item>

                    </el-col>
                  </el-row>
                </el-form>
              </div>
            </div>
          </el-card>

          <el-card class="config-card flow-config-card" style="margin-top: 20px;">
            <template #header>
              <div class="card-header">
                <span>流量模块配置</span>
                <div class="header-actions">
                  <el-tag :type="totalTrafficWeight === 100 ? 'success' : 'warning'" size="small">
                    权重总和: {{ totalTrafficWeight }} / 100
                  </el-tag>
                  <el-divider direction="vertical" />
                  <el-button type="primary" size="small" icon="Plus" @click="handleAddSlot">添加流量</el-button>
                </div>
              </div>
            </template>

            <div v-if="flowIndexList.length === 0" class="empty-slots">
              <el-empty description="暂未添加流量模块">
                <el-text type="info" size="small">点击"添加流量"按钮新增流量模块</el-text>
                <template #image>
                  <el-icon :size="60" color="#909399"><Operation /></el-icon>
                </template>
              </el-empty>
            </div>

            <div v-for="flowIndex in flowIndexList" :key="flowIndex" class="flow-module">
              <el-card shadow="never" class="flow-module-card">
                <template #header>
                  <div class="slot-card-header flow-module-header">
                    <div class="header-left">
                      <el-tag size="small" type="primary" style="margin-right: 8px;">流量{{ flowIndex }}</el-tag>
                      <span class="title-text">预算数量: {{ getFlowSlotList(flowIndex).length }}</span>
                    </div>
                    <div class="header-right">
                      <div class="weight-input-wrapper">
                        <span class="weight-label">流量权重:</span>
                        <el-input-number
                          :model-value="getFlowWeight(flowIndex)"
                          @update:model-value="value => handleFlowWeightChange(flowIndex, value)"
                          :min="0"
                          :max="100"
                          size="small"
                          :precision="0"
                          style="width: 100px"
                        />
                      </div>
                      <el-button type="primary" size="small" icon="Plus" @click="handleAddBudget(flowIndex)">添加预算</el-button>
                    </div>
                  </div>
                </template>

                <div v-if="getFlowSlotList(flowIndex).length === 0" class="empty-flow-slots">
                  <el-empty description="该流量模块暂无预算">
                    <el-text type="info" size="small">点击"添加预算"按钮为流量{{ flowIndex }}添加预算</el-text>
                  </el-empty>
                </div>

                <div
                  v-for="(slot, index) in getFlowSlotList(flowIndex)"
                  :key="slot.id || slot.dspSlotInfoId || `${flowIndex}-${index}`"
                  :class="['slot-card', slot.deleted ? 'slot-card-deleted' : '']"
                >
                  <el-card shadow="hover">
                    <template #header>
                      <div class="slot-card-header">
                        <div class="header-left">
                          <el-tag size="small" type="info" style="margin-right: 8px;">{{ index + 1 }}</el-tag>
                          <span class="title-text">
                            {{ slot.name || 'DSP广告位' }}（ID: {{ slot.dspSlotInfoId }} 操作系统：{{ slot.osType == 1 ? 'Android' : slot.osType == 2 ? 'iOS' : '未知' }}）
                          </span>
                          <el-tag size="small" :type="Number(slot.dspPayType) === 1 ? 'success' : 'primary'" style="margin-left: 8px;">
                            {{ Number(slot.dspPayType) === 1 ? '分成' : 'RTB' }}
                          </el-tag>
                          <el-tag v-if="slot.deleted" size="small" type="danger" style="margin-left: 8px;">已删除（未保存）</el-tag>
                        </div>
                        <div class="header-right">
                          <el-button type="warning" size="small" icon="Document" :disabled="slot.deleted" @click="handleCaptureLog(slot)">捕获日志</el-button>
                          <el-button v-if="slot.deleted" type="success" size="small" icon="Refresh" @click="handleRestoreSlot(slot)">恢复</el-button>
                          <el-button v-else type="danger" size="small" icon="Delete" @click="handleDeleteSlot(slot)">删除</el-button>
                        </div>
                      </div>
                    </template>

                    <!-- 投放配置（可编辑） -->
                    <el-collapse v-model="slot.activeCollapse" class="slot-collapse">
                      <el-collapse-item name="launch">
                        <template #title>
                          <div class="collapse-title">
                            <el-icon color="#409eff"><Setting /></el-icon>
                            <span>投放配置</span>
                          </div>
                        </template>
                        <el-form :model="slot" label-width="120px" size="small" class="launch-config-form">
                          <!-- 第一行：投放策略、捕获日志时长、IP限流 -->
                          <!-- 第二行：底价 -->
                          <!-- 第三行：请求、展现、点击 -->
                          <el-row :gutter="16">
                            <el-col :span="8">
                              <el-form-item label="请求次数">
                                <el-input-number
                                  v-model="slot.req"
                                  :min="0"
                                  placeholder="请输入"
                                  :controls="false"
                                  style="width: 100%"
                                />
                              </el-form-item>
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="展现次数">
                                <el-input-number
                                  v-model="slot.ims"
                                  :min="0"
                                  placeholder="请输入"
                                  :controls="false"
                                  style="width: 100%"
                                />
                              </el-form-item>
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="点击次数">
                                <el-input-number
                                  v-model="slot.clk"
                                  :min="0"
                                  placeholder="请输入"
                                  :controls="false"
                                  style="width: 100%"
                                />
                              </el-form-item>
                            </el-col>
                          </el-row>

                          <!-- 第四行：投放时段、地域定向、品牌定向 -->
                          <el-row :gutter="16">
                            <el-col :span="8">
                              <el-form-item label="投放时段" required>
                                <el-select v-model="slot.launchTime" placeholder="请选择" style="width: 100%">
                                  <el-option label="全时段" :value="1" />
                                  <el-option label="自定义" :value="2" />
                                </el-select>
                              </el-form-item>
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="包透传" required>
                                <el-select v-model="slot.pkgTransfer" placeholder="请选择" style="width: 100%">
                                  <el-option label="不透传" :value="0" />
                                  <el-option label="透传" :value="1" />
                                </el-select>
                              </el-form-item>
                            </el-col>
                            <el-col :span="8" v-if="shouldShowFloorPrice(slot)">
                              <el-form-item label="底价(分)">
                                <el-input-number
                                    v-model="slot.floorPrice"
                                    :min="0"
                                    placeholder="请输入底价"
                                    :controls="false"
                                    style="width: 100%"
                                />
                              </el-form-item>
                            </el-col>
                            <!-- 调试信息：显示结算方式 -->
                            <el-col :span="8" v-if="false">
                              <el-tag>结算方式: {{ slot.dspPayType }} ({{ typeof slot.dspPayType }})</el-tag>
                            </el-col>
                      </el-row>
                        <el-row v-if="isSlotRtb(slot)">
                          <el-col :span="8">
                            <el-form-item label="RTB结算比例">
                              <el-input-number
                                  v-model="slot.dspDealRatio"
                                  @change="value => handleRtbDealRatioChange(slot, value)"
                                  :min="0"
                                  placeholder="请输入RTB结算比例"
                                  :controls="false"
                                  style="width: 100%"
                              />
                            </el-form-item>
                          </el-col>
                        </el-row>

                        </el-form>
                      </el-collapse-item>
                    </el-collapse>
                  </el-card>
                </div>
              </el-card>
            </div>
          </el-card>
        </div>
        <div v-else class="empty-config">
          <el-empty description="请选择一个广告位进行配置" />
        </div>
      </el-drawer>

    <el-drawer
      v-model="open"
      direction="rtl"
      size="1400px"
      :show-close="false"
      style="background: #f2f5f7"
      @close="cancel"
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
            <el-button class="btn-blue" @click="handleEditSave">
              <el-icon>
                <Position />
              </el-icon>
              提 交
            </el-button>
          </div>
        </div>
      </template>
      <div class="drawer-content">
        <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="120px" class="edit-form">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="媒体" prop="mediaId">
                <el-select
                  v-model="editForm.mediaId"
                  placeholder="请选择媒体"
                  clearable
                  filterable
                  style="width: 100%"
                  @change="handleEditMediaChange"
                >
                  <el-option
                    v-for="item in editMediaOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="应用" prop="appId">
                <el-select
                  v-model="editForm.appId"
                  placeholder="请选择应用"
                  clearable
                  filterable
                  style="width: 100%"
                  :disabled="!editForm.mediaId"
                >
                  <el-option
                    v-for="item in editAppOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="广告位名称" prop="name">
                <el-input v-model="editForm.name" placeholder="请输入广告位名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="内部广告位名称" prop="nameAlise">
                <el-input v-model="editForm.nameAlise" placeholder="请输入内部广告位名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="广告类型" prop="adTypeId">
                <el-select
                  v-model="editForm.adTypeId"
                  placeholder="请先选择广告类型"
                  clearable
                  filterable
                  style="width: 100%"
                  @change="handleAdTypeChange"
                >
                  <el-option
                    v-for="item in adTypeList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="操作系统" prop="osType">
                <el-select v-model="editForm.osType" placeholder="请选择操作系统" style="width: 100%" disabled>
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
                <el-select v-model="editForm.accessType" placeholder="请选择接入方式" style="width: 100%" disabled>
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
              <el-form-item label="结算方式" prop="sspPayType">
                <el-select v-model="editForm.sspPayType" placeholder="请选择结算方式" style="width: 100%" @change="handleEditSspPayTypeChange">
                  <el-option
                    v-for="dict in ssp_pay_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="分成系数" prop="sspDealRatio">
                <el-input
                  v-model="editForm.sspDealRatio"
                  placeholder="请输入分成系数"
                  :disabled="editForm.sspPayType === '2'"
                />
              </el-form-item>
            </el-col>
<!--            <el-col :span="6">-->
<!--              <el-form-item label="广告宽度" width="200" prop="width">-->
<!--                <el-input v-model="editForm.width"  placeholder="请输入广告宽度" />-->
<!--              </el-form-item>-->
<!--            </el-col>-->
<!--            <el-col :span="6">-->
<!--              <el-form-item label="广告高度" prop="height">-->
<!--                <el-input v-model="editForm.height" placeholder="请输入广告高度" />-->
<!--              </el-form-item>-->
<!--            </el-col>-->
          </el-row>
          <el-row :gutter="20">
<!--            <el-col :span="12">-->
<!--              <el-form-item label="交互类型" prop="interactionType">-->
<!--                <div class="interaction-type-selector-wrapper">-->
<!--                  <el-checkbox-group v-model="selectedInteractionTypes" @change="handleInteractionTypeGroupChange">-->
<!--                    <el-checkbox-->
<!--                      v-for="item in interactionTypeOptions"-->
<!--                      :key="item.value"-->
<!--                      :value="item.value"-->
<!--                      :label="item.value"-->
<!--                      border-->
<!--                    >-->
<!--                      {{ item.label }}-->
<!--                    </el-checkbox>-->
<!--                  </el-checkbox-group>-->
<!--                  <div class="interaction-hint">-->
<!--                    <el-icon color="#909399"><InfoFilled /></el-icon>-->
<!--                    <span>已选择 {{ getSelectedInteractionTypes().length }} 种交互类型，位掩码值：{{ editForm.interactionType }}</span>-->
<!--                  </div>-->
<!--                </div>-->
<!--              </el-form-item>-->
<!--              <el-col :span="12">-->
<!--                <el-form-item label="广告位图片" prop="adImage">-->
<!--                  <image-upload v-model="editForm.adImage"/>-->
<!--                </el-form-item>-->
<!--              </el-col>-->
<!--            </el-col>-->
            <el-col :span="12">
              <el-form-item label="状态" prop="enable">
                <el-select v-model="editForm.enable" placeholder="请选择状态" style="width: 100%">
                  <el-option
                    v-for="dict in audit_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="editForm.remark" type="textarea" placeholder="请输入备注" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-drawer>

    <!-- 选择DSP广告位对话框 -->
    <el-dialog
      v-model="selectSlotDialogVisible"
      title="流量拆分 - 选择预算广告位"
      width="1600px"
      append-to-body
    >
      <!-- 检索表单 -->
      <el-form :model="slotQueryParams" ref="slotQueryRef" :inline="true" label-width="80px" style="margin-bottom: 16px;">
        <el-form-item prop="companyId">
          <el-select
              v-model="slotQueryParams.companyId"
              placeholder="公司名称"
              clearable
              filterable
              style="width: 160px"
              @change="handleSlotCompanyChange"
          >
            <el-option
                v-for="item in slotCompanyOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="productId">
          <el-select
              v-model="slotQueryParams.productId"
              placeholder="产品名称"
              clearable
              filterable
              style="width: 160px"
              @change="handleSlotProductChange"
          >
            <el-option
                v-for="item in slotProductOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="osType">
          <el-select
            v-model="slotQueryParams.osType"
            placeholder="操作系统"
            clearable
            style="width: 150px"
          >
            <el-option label="Android" :value="1" />
            <el-option label="iOS" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item  prop="adTypeId">
          <el-select
            v-model="slotQueryParams.adTypeId"
            placeholder="广告类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in adTypeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item prop="budgetSlotId">
          <el-select
            v-model="slotQueryParams.budgetSlotId"
            placeholder="预算名称"
            clearable
            filterable
            style="width: 220px"
          >
            <el-option
              v-for="item in slotBudgetNameOptions"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="dspSlotCode">
          <el-input
            v-model="slotQueryParams.dspSlotCode"
            placeholder="预算广告位ID"
            clearable
            style="width: 150px"
            @keyup.enter="handleSlotQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button class="btn-blue" type="primary" icon="Search" @click="handleSlotQuery">搜索</el-button>
          <el-button class="btn-blue" icon="Refresh" @click="handleResetSlotQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        :data="filteredMatchedDspSlots"
        @selection-change="handleSlotSelectionChange"
        style="width: 100%"
        :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
        :cell-style="{ color: '#000' }"
        border
        table-layout="auto"
        highlight-current-row="true"
      >
        <el-table-column type="selection" width="55" align="center"  />
        <el-table-column label="ID" align="center" prop="id" width="66"  />
        <el-table-column label="公司名称" align="center" prop="companyName" width="250"  />
        <el-table-column label="产品名称" align="center" prop="productName" width="250"  />
        <el-table-column label="预算方名称" align="center" prop="name" width="250"  />
        <el-table-column label="操作系统" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="getOsTypeValue(scope.row) === 1" type="success">Android</el-tag>
            <el-tag v-else-if="getOsTypeValue(scope.row) === 2" type="primary">iOS</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="广告类型" align="center" width="120">
          <template #default="scope">
            {{ getAdTypeName(getAdTypeIdValue(scope.row)) }}
          </template>
        </el-table-column>
        <el-table-column label="广告位编码" align="center" width="180">
          <template #default="scope">
            {{ getDspSlotCodeValue(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="结算方式" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="getDspPayTypeValue(scope.row) == 1" type="success">分成</el-tag>
            <el-tag v-else-if="getDspPayTypeValue(scope.row) == 2" type="primary">RTB</el-tag>
            <el-tag v-else-if="getDspPayTypeValue(scope.row) == 3" type="primary">固价</el-tag>

          </template>
        </el-table-column>
        <el-table-column label="ID" align="center" prop="id" width="66"  />

      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button class="btn-blue" @click="handleCancelSelectSlot">取 消</el-button>
          <el-button class="btn-blue" type="primary" @click="handleConfirmSelectSlot">
            确定添加 ({{ selectedDspSlotIds.length }})
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MediaAd">
import { listMediaAd, getMediaAd, delMediaAd, addMediaAd, updateMediaAd, getMatchedDspSlots, saveLaunchConfig, getLaunchConfig } from "@/api/flow/mediaAd"
import { getMediaAppCascader, listMedia } from "@/api/flow/media"
import { getApp } from "@/api/flow/app"
import { listType } from "@/api/ad/type"
import { listScene } from "@/api/ad/scene"
import { listSize } from "@/api/ad/size"
import { updateInfo } from "@/api/budget/info"
import { listCompany } from "@/api/budget/company"
import { listProduct } from "@/api/budget/product"
import { useDict } from "@/utils/dict"
import { ref, reactive, toRefs, computed, getCurrentInstance, watch } from "vue"
import { Close, InfoFilled, Link, Operation, Position, Setting } from '@element-plus/icons-vue'

const { proxy } = getCurrentInstance()
// 使用 proxy.useDict 获取字典数据，参考菜单管理页面的实现
const { media_status } = proxy.useDict("media_status")

const { audit_status, ssp_pay_type, ad_scene, os_type, access_type } = useDict('audit_status', 'ssp_pay_type', 'ad_scene', 'os_type', 'access_type')

const mediaAdList = ref([])
const cascaderData = ref([])
const open = ref(false)
const configOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 创建媒体和应用ID到名称的映射
const mediaNameMap = ref(new Map())
const appNameMap = ref(new Map())
const mediaAppOptionsMap = ref(new Map())
const searchMediaOptions = ref([])
const searchAppOptions = ref([])
const allSearchAppOptions = ref([])
// 创建媒体ID到公司名称的映射
const mediaCompanyNameMap = ref(new Map())
// 创建时间范围
const createTimeRange = ref([])

// Tab 相关状态
const activeTab = ref('list')
const configMediaAd = ref(null)
const editMediaAd = ref(null)
const configForm = ref({
  item1: '',
  item2: '',
  item3: ''
})
const editForm = ref({
  mediaAppCascade: [],
  name: null,
  nameAlise: null,
  adSceneId: null,
  adTypeId: null,
  adSizeId: null,
  osType: null,
  accessType: null,
  sspPayType: null,
  sspDealRatio: null,
  sspEcpm: null,
  width: null,
  height: null,
  adImage: null,
  interactionType: 0,
  enable: null,
  remark: null,
  fixedPrice: null
})

// 广告类型、广告场景、样式尺寸列表
const adTypeList = ref([])
const adSceneList = ref([])
const adSizeList = ref([])

// 交互类型选项
const interactionTypeOptions = [
  { value: 1, label: '打开网页' },
  { value: 2, label: 'deeplink' },
  { value: 3, label: '直接下载应用' },
  { value: 4, label: '广点通' },
  { value: 5, label: '小程序跳转' },
  { value: 6, label: '应用商店下载' },
  { value: 7, label: '快应用' }
]

// 选中的交互类型列表（用于checkbox group）
const selectedInteractionTypes = ref([])

// 计算属性：筛选后的广告场景列表
const filteredAdSceneList = computed(() => {
  if (!editForm.value.adTypeId) {
    return []
  }
  return adSceneList.value.filter(scene => scene.typeId === editForm.value.adTypeId)
})

// 计算属性：筛选后的样式尺寸列表
const filteredAdSizeList = computed(() => {
  if (!editForm.value.adTypeId) {
    return []
  }
  return adSizeList.value.filter(size => size.typeId === editForm.value.adTypeId)
})

// 计算属性：显示宽度×高度
const widthHeightDisplay = computed(() => {
  if (editForm.value.sizeId && filteredAdSizeList.value.length > 0) {
    const size = filteredAdSizeList.value.find(s => s.id === editForm.value.sizeId)
    if (size) {
      return size.size
    }
  }
  return editForm.value.width && editForm.value.height ? `${editForm.value.width}×${editForm.height}` : ''
})

// 流量分配 - 已绑定的DSP广告位列表（通过 dsp_launch 关联）
const slotList = ref([])
// 流量模块索引列表（如 1,2,3）
const flowIndexList = ref([])
// 当前操作中的流量模块
const activeFlowIndex = ref(null)
// 每个流量模块对应一个统一权重
const flowWeightByIndex = ref({})
// 应用信息
const appInfo = ref(null)
// 选择DSP广告位对话框
const selectSlotDialogVisible = ref(false)
// 匹配的DSP广告位列表（从 dsp_slot_info 表查询）
const matchedDspSlots = ref([])
// 选中的DSP广告位ID列表
const selectedDspSlotIds = ref([])
// 预算公司与产品列表（用于流量拆分筛选展示）
const budgetCompanyList = ref([])
const budgetProductList = ref([])

// 弹窗检索参数
const slotQueryParams = ref({
  companyId: null,
  productId: null,
  budgetSlotId: null,
  osType: null,
  adTypeId: null,
  dspSlotCode: null
})

const slotCompanyOptions = computed(() => {
  const optionMap = new Map()
  matchedDspSlots.value.forEach(slot => {
    const companyId = getSlotCompanyId(slot)
    if (companyId === null || companyId === undefined || companyId === '') {
      return
    }
    if (!optionMap.has(String(companyId))) {
      optionMap.set(String(companyId), {
        id: companyId,
        name: getSlotCompanyName(slot)
      })
    }
  })
  return Array.from(optionMap.values())
})

const slotProductOptions = computed(() => {
  const optionMap = new Map()
  matchedDspSlots.value.forEach(slot => {
    const companyId = getSlotCompanyId(slot)
    if (!isEmptyValue(slotQueryParams.value.companyId) && !isSameId(companyId, slotQueryParams.value.companyId)) {
      return
    }
    const productId = getSlotProductId(slot)
    if (productId === null || productId === undefined || productId === '') {
      return
    }
    if (!optionMap.has(String(productId))) {
      optionMap.set(String(productId), {
        id: productId,
        name: getSlotProductName(slot)
      })
    }
  })
  return Array.from(optionMap.values())
})

const slotBudgetNameOptions = computed(() => {
  const optionMap = new Map()
  matchedDspSlots.value.forEach(slot => {
    if (!isEmptyValue(slotQueryParams.value.companyId) && !isSameId(getSlotCompanyId(slot), slotQueryParams.value.companyId)) {
      return
    }
    if (!isEmptyValue(slotQueryParams.value.productId) && !isSameId(getSlotProductId(slot), slotQueryParams.value.productId)) {
      return
    }
    const slotId = slot?.id
    if (slotId === null || slotId === undefined || slotId === '') {
      return
    }
    if (!optionMap.has(String(slotId))) {
      optionMap.set(String(slotId), {
        id: slotId,
        label: getBudgetOptionLabel(slot)
      })
    }
  })
  return Array.from(optionMap.values())
})

// 过滤后的DSP广告位列表
const filteredMatchedDspSlots = computed(() => {
  let filtered = matchedDspSlots.value

  if (!isEmptyValue(slotQueryParams.value.companyId)) {
    filtered = filtered.filter(slot => isSameId(getSlotCompanyId(slot), slotQueryParams.value.companyId))
  }

  if (!isEmptyValue(slotQueryParams.value.productId)) {
    filtered = filtered.filter(slot => isSameId(getSlotProductId(slot), slotQueryParams.value.productId))
  }

  if (!isEmptyValue(slotQueryParams.value.budgetSlotId)) {
    filtered = filtered.filter(slot => isSameId(slot?.id, slotQueryParams.value.budgetSlotId))
  }

  // 按操作系统过滤（兼容驼峰和下划线）
  if (slotQueryParams.value.osType !== null && slotQueryParams.value.osType !== '') {
    filtered = filtered.filter(slot => getOsTypeValue(slot) === slotQueryParams.value.osType)
  }

  // 按广告类型过滤（兼容驼峰和下划线）
  if (slotQueryParams.value.adTypeId !== null && slotQueryParams.value.adTypeId !== '') {
    filtered = filtered.filter(slot => getAdTypeIdValue(slot) === slotQueryParams.value.adTypeId)
  }

  // 按广告位编码过滤（模糊匹配，兼容驼峰和下划线）
  if (slotQueryParams.value.dspSlotCode) {
    const code = slotQueryParams.value.dspSlotCode.toLowerCase()
    filtered = filtered.filter(slot => {
      const slotCode = getDspSlotCodeValue(slot)
      return slotCode && slotCode.toLowerCase().includes(code)
    })
  }

  return filtered
})

function getSlotFlowIndex(slot) {
  const indexValue = Number(slot?.indexs)
  return Number.isInteger(indexValue) && indexValue > 0 ? indexValue : 1
}

function getFlowSlotList(flowIndex) {
  return slotList.value.filter(slot => getSlotFlowIndex(slot) === Number(flowIndex))
}

function getFlowWeight(flowIndex) {
  const key = String(flowIndex)
  const value = Number(flowWeightByIndex.value[key])
  return Number.isFinite(value) ? value : 0
}

function handleFlowWeightChange(flowIndex, value) {
  const key = String(flowIndex)
  const weight = Number(value) || 0
  flowWeightByIndex.value[key] = weight
  slotList.value.forEach(slot => {
    if (getSlotFlowIndex(slot) === Number(flowIndex)) {
      slot.trafficWeight = weight
    }
  })
}

function syncFlowStateFromSlotList() {
  const flowSet = new Set()
  const nextWeights = {}

  slotList.value.forEach(slot => {
    const flowIndex = getSlotFlowIndex(slot)
    flowSet.add(flowIndex)
    if (nextWeights[String(flowIndex)] === undefined) {
      nextWeights[String(flowIndex)] = Number(slot.trafficWeight) || 0
    }
  })

  flowIndexList.value = Array.from(flowSet).sort((a, b) => a - b)
  flowWeightByIndex.value = nextWeights
  activeFlowIndex.value = flowIndexList.value.length > 0 ? flowIndexList.value[0] : null

  slotList.value.forEach(slot => {
    slot.trafficWeight = getFlowWeight(getSlotFlowIndex(slot))
  })
}

// 计算总流量权重（所有流量模块权重之和）
const totalTrafficWeight = computed(() => {
  return flowIndexList.value.reduce((sum, flowIndex) => {
    return sum + getFlowWeight(flowIndex)
  }, 0)
})

const data = reactive({
  form: {},
  queryParams: {
    idStr: null,
    idList: null,
    pageNum: 1,
    pageSize: 10,
    mediaId: null,
    appId: null,
    mediaAppCascade: [],  // 级联选择器的值
    adTypeId: null,
    name: null,
    nameAlise: null,
    osType: null,
    accessType: null,
    sspPayType: null,
    enable: null,
    params: {
      startTime: null,
      endTime: null
    }
  },
  rules: {
    mediaAppCascade: [
      { required: true, message: "媒体/应用不能为空", trigger: "change", type: 'array' }
    ],
    name: [
      { required: true, message: "广告位名称不能为空", trigger: "blur" }
    ],
    sspPayType: [
      { required: true, message: "结算方式不能为空", trigger: "change" }
    ],
    sspDealRatio: [
      {
        validator: (_rule, value, callback) => {
          // 如果结算方式选择分成，则分成系数必填
          if (form.value.sspPayType === '1' && !value) {
            callback(new Error('分成系数不能为空'))
          } else {
            callback()
          }
        },
        trigger: 'blur'
      }
    ],
    enable: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
  }
})

// 编辑表单验证规则
const editRules = {
  mediaAppCascade: [
    { required: true, message: "媒体/应用不能为空", trigger: "change", type: 'array' }
  ],
  name: [
    { required: true, message: "广告位名称不能为空", trigger: "blur" }
  ],
  sspPayType: [
    { required: true, message: "结算方式不能为空", trigger: "change" }
  ],
  sspDealRatio: [
    {
      validator: (_rule, value, callback) => {
        // 如果结算方式选择分成，则分成系数必填
        if (editForm.value.sspPayType === '1' && !value) {
          callback(new Error('分成系数不能为空'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  sspEcpm: [
    {
      validator: (_rule, value, callback) => {
        // 如果结算方式选择分成，则千次收益必填
        if (editForm.value.sspPayType === '1' && !value) {
          callback(new Error('千次收益不能为空'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  enable: [
    { required: true, message: "状态不能为空", trigger: "change" }
  ],
}

const { queryParams, form, rules } = toRefs(data)

/** 查询媒体广告位列表 */
function getList() {
  loading.value = true
  listMediaAd(queryParams.value).then(response => {
    mediaAdList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// /** 加载媒体应用级联数据 */
function loadCascaderData() {
  getMediaAppCascader().then(response => {
    const data = Array.isArray(response.data) ? response.data : []
    cascaderData.value = data
    // 构建媒体和应用ID到名称的映射
    mediaNameMap.value.clear()
    appNameMap.value.clear()
    mediaAppOptionsMap.value.clear()

    const mediaOptions = []
    const allAppsMap = new Map()

    data.forEach(media => {
      mediaNameMap.value.set(media.value, media.label)
      mediaOptions.push({ value: media.value, label: media.label })

      const appOptions = Array.isArray(media.children)
        ? media.children.map(app => ({ value: app.value, label: app.label }))
        : []

      mediaAppOptionsMap.value.set(media.value, appOptions)

      appOptions.forEach(app => {
        appNameMap.value.set(app.value, app.label)
        if (!allAppsMap.has(app.value)) {
          allAppsMap.set(app.value, app)
        }
      })
    })

    searchMediaOptions.value = mediaOptions
    allSearchAppOptions.value = Array.from(allAppsMap.values())
    syncCascaderMediaDisplayName()

    if (queryParams.value.mediaId) {
      handleSearchMediaChange(queryParams.value.mediaId)
    } else {
      searchAppOptions.value = allSearchAppOptions.value
    }
  })
}

function handleSearchMediaChange(mediaId) {
  queryParams.value.appId = null
  if (!mediaId) {
    searchAppOptions.value = allSearchAppOptions.value
    return
  }
  searchAppOptions.value = mediaAppOptionsMap.value.get(mediaId) || []
}

function syncCascaderMediaDisplayName() {
  if (!Array.isArray(cascaderData.value) || cascaderData.value.length === 0) {
    return
  }

  const nextMediaNameMap = new Map()
  const nextSearchMediaOptions = []

  cascaderData.value = cascaderData.value.map(media => {
    const displayLabel = mediaCompanyNameMap.value.get(media.value) || media.label
    nextMediaNameMap.set(media.value, displayLabel)
    nextSearchMediaOptions.push({
      value: media.value,
      label: displayLabel
    })
    return {
      ...media,
      label: displayLabel
    }
  })

  mediaNameMap.value = nextMediaNameMap
  searchMediaOptions.value = nextSearchMediaOptions
}

/** 加载媒体列表，建立媒体ID到公司名称的映射 */
function loadMediaList() {
  listMedia({ pageNum: 1, pageSize: 1000 }).then(response => {
    mediaCompanyNameMap.value.clear()
    response.rows.forEach(media => {
      mediaCompanyNameMap.value.set(media.id, media.mediaCompanyShort)
    })
    syncCascaderMediaDisplayName()
  })
}

/** 加载广告类型列表 */
function loadAdTypeList() {
  listType({ pageNum: 1, pageSize: 100 }).then(response => {
    adTypeList.value = response.rows
  })
}

/** 加载广告场景列表 */
function loadAdSceneList() {
  listScene({ pageNum: 1, pageSize: 100 }).then(response => {
    adSceneList.value = response.rows
  })
}

/** 加载样式尺寸列表 */
function loadAdSizeList() {
  listSize({ pageNum: 1, pageSize: 100 }).then(response => {
    adSizeList.value = response.rows
  })
}

/** 加载预算公司和产品名称映射（用于流量拆分筛选展示） */
function loadBudgetCompanyAndProduct() {
  listCompany({ pageNum: 1, pageSize: 1000 }).then(response => {
    budgetCompanyList.value = response.rows || []
  }).catch(() => {
    budgetCompanyList.value = []
  })

  listProduct({ pageNum: 1, pageSize: 1000 }).then(response => {
    budgetProductList.value = response.rows || []
  }).catch(() => {
    budgetProductList.value = []
  })
}

/** 根据媒体ID获取媒体名称 */
function getMediaName(mediaId) {
  return mediaCompanyNameMap.value.get(mediaId) || mediaId
}

/** 根据应用ID获取应用名称 */
function getAppName(appId) {
  return appNameMap.value.get(appId) || appId
}

/** 根据广告类型ID获取广告类型名称 */
function getAdTypeName(typeId) {
  const type = adTypeList.value.find(item => item.id === typeId)
  return type ? type.name : typeId
}

/** 根据操作系统值获取名称 */
function getOsTypeName(osTypeValue) {
  if (osTypeValue === null || osTypeValue === undefined || osTypeValue === '') {
    return ''
  }
  const os = os_type.value.find(item => String(item.value) === String(osTypeValue))
  return os ? os.label : ''
}

/** 自动拼接内部广告位名称：媒体-应用-操作系统-广告类型 */
function autoFillInternalAdName() {
  // 仅在新增时自动生成，避免覆盖编辑态的历史名称
  if (editMediaAd.value) {
    return
  }

  if (!editForm.value.mediaId || !editForm.value.appId) {
    return
  }

  const mediaId = editForm.value.mediaId
  const appId = editForm.value.appId
  const mediaName = mediaNameMap.value.get(mediaId)
  const appName = appNameMap.value.get(appId)
  const osName = getOsTypeName(editForm.value.osType)
  const adTypeName = getAdTypeName(editForm.value.adTypeId)

  if (!mediaName || !appName || !osName || !adTypeName) {
    return
  }

  editForm.value.nameAlise = `${mediaName}-${appName}-${osName}-${adTypeName}`
}

/** 根据广告场景ID获取广告场景名称 */
function getAdSceneName(sceneId) {
  const scene = adSceneList.value.find(item => item.id === sceneId)
  return scene ? scene.name : sceneId
}

/** 根据样式尺寸ID获取样式尺寸 */
function getAdSizeName(sizeId) {
  const size = adSizeList.value.find(item => item.id === sizeId)
  return size ? size.size : sizeId
}

// 取消按钮
function cancel() {
  open.value = false
  editMediaAd.value = null
  reset()
}

// 表单重置
function reset() {
  editForm.value = {
    id: null,
    mediaId: null,
    appId: null,
    name: null,
    nameAlise: null,
    adSceneId: null,
    adTypeId: null,
    adSizeId: null,
    osType: null,
    accessType: null,
    sspPayType: null,
    sspDealRatio: null,
    sspEcpm: null,
    width: null,
    height: null,
    adImage: null,
    interactionType: 0,
    enable: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  selectedInteractionTypes.value = []
  if (proxy.$refs["editFormRef"]) {
    proxy.$refs["editFormRef"].clearValidate()
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  // 处理批量广告位ID
  if (queryParams.value.idStr && queryParams.value.idStr.trim()) {
    // 将换行分隔的字符串转换为数组
    const idArray = queryParams.value.idStr.split('\n')
      .map(id => id.trim())
      .filter(id => id !== '')
      .map(id => Number(id))
      .filter(id => !isNaN(id))
    queryParams.value.idList = idArray.length > 0 ? idArray : null
  } else {
    queryParams.value.idList = null
  }

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
  queryParams.value.idStr = null
  queryParams.value.idList = null
  queryParams.value.sspPayType = null
  proxy.resetForm("queryRef")
  searchAppOptions.value = allSearchAppOptions.value
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 结算方式改变时触发分成系数验证 */
function handleSspPayTypeChange() {
  // 如果结算方式切换为RTB，清空分成系数
  if (form.value.sspPayType === '2') {
    form.value.sspDealRatio = null
  }
  // 触发分成系数字段的验证
  proxy.$refs["mediaAdRef"].validateField('sspDealRatio')
}

/** 编辑页面结算方式改变时触发分成系数和千次收益验证 */
function handleEditSspPayTypeChange() {
  // 如果结算方式切换为RTB，清空分成系数和千次收益
  if (editForm.value.sspPayType === '2') {
    editForm.value.sspDealRatio = null
    editForm.value.sspEcpm = null
  }
  // 触发分成系数和千次收益字段的验证
  if (proxy.$refs["editFormRef"]) {
    proxy.$refs["editFormRef"].validateField('sspDealRatio')
    proxy.$refs["editFormRef"].validateField('sspEcpm')
  }
}

/** 检查某个交互类型是否被选中 */
function isInteractionTypeSelected(typeValue) {
  return (editForm.value.interactionType & (1 << (typeValue - 1))) !== 0
}

/** 获取选中的交互类型列表 */
function getSelectedInteractionTypes() {
  return interactionTypeOptions.filter(option => isInteractionTypeSelected(option.value))
}

/** 获取交互类型标签文本 */
function getInteractionTypeLabel() {
  const selected = getSelectedInteractionTypes()
  if (selected.length === 0) {
    return '选择交互类型'
  }
  return selected.map(item => item.label).join('、')
}

/** 处理交互类型checkbox group变化 */
function handleInteractionTypeGroupChange(checkedValues) {
  // 计算位掩码
  let mask = 0
  checkedValues.forEach(value => {
    mask |= (1 << (value - 1))
  })
  editForm.value.interactionType = mask
}

/** 处理交互类型选择变化 */
function handleInteractionTypeChange(typeValue) {
  if (isInteractionTypeSelected(typeValue)) {
    // 取消选中：使用位掩码清除该位
    editForm.value.interactionType = editForm.value.interactionType & ~(1 << (typeValue - 1))
  } else {
    // 选中：使用位掩码设置该位
    editForm.value.interactionType = editForm.value.interactionType | (1 << (typeValue - 1))
  }
  // 更新checkbox group的选中状态
  updateSelectedInteractionTypes()
}

/** 更新checkbox group的选中状态 */
function updateSelectedInteractionTypes() {
  selectedInteractionTypes.value = interactionTypeOptions
    .filter(option => isInteractionTypeSelected(option.value))
    .map(option => option.value)
}

/** 广告类型改变时的处理 */
function handleAdTypeChange() {
  // 清空广告场景和样式尺寸的选择
  editForm.value.adSceneId = null
  editForm.value.adSizeId = null
  // 清空宽度和高度
  editForm.value.width = null
  editForm.value.height = null
  autoFillInternalAdName()
}

/** 监听样式尺寸变化，自动填充宽度和高度 */
watch(() => editForm.value.adSizeId, (newSizeId) => {
  if (newSizeId && filteredAdSizeList.value.length > 0) {
    const size = filteredAdSizeList.value.find(s => s.id === newSizeId)
    if (size && size.size) {
      // 解析尺寸字符串，格式如 "1080*1920"
      const dimensions = size.size.split('*')
      if (dimensions.length === 2) {
        editForm.value.width = dimensions[0]
        editForm.value.height = dimensions[1]
      }
    }
  }
})

function handleEditMediaChange() {
  editForm.value.appId = null
  editForm.value.osType = null
  editForm.value.accessType = null
}

/** 监听应用选择变化，自动填充osType和accessType */
watch(() => editForm.value.appId, async (newAppId) => {
  if (!newAppId) {
    editForm.value.osType = null
    editForm.value.accessType = null
    return
  }

  try {
    const response = await getApp(newAppId)
    const appData = response.data
    if (appData.osType !== null && appData.osType !== undefined) {
      editForm.value.osType = String(appData.osType)
    }
    if (appData.accessType !== null && appData.accessType !== undefined) {
      editForm.value.accessType = String(appData.accessType)
    }
    autoFillInternalAdName()
  } catch (error) {
    console.error('获取应用信息失败:', error)
  }
})

/** 新增按钮操作 */
function handleAdd() {
  editMediaAd.value = null
  reset()
  title.value = '新增广告位'
  open.value = true
}

/** 修改按钮操作 */
function handleUpdate(row) {
  const _id = row.id || ids.value
  getMediaAd(_id).then(response => {
    const data = response.data
    editMediaAd.value = data

    // 将数字类型的字段转为字符串，以便字典匹配
    if (data.sspPayType !== null && data.sspPayType !== undefined) {
      data.sspPayType = String(data.sspPayType)
    }
    if (data.enable !== null && data.enable !== undefined) {
      data.enable = String(data.enable)
    }
    if (data.osType !== null && data.osType !== undefined) {
      data.osType = String(data.osType)
    }
    if (data.accessType !== null && data.accessType !== undefined) {
      data.accessType = String(data.accessType)
    }
    // 复制其他字段到编辑表单
    editForm.value = {
      id: data.id,
      mediaId: data.mediaId ?? null,
      appId: data.appId ?? null,
      name: data.name,
      nameAlise: data.nameAlise,
      adSceneId: data.adSceneId,
      adTypeId: data.adTypeId,
      adSizeId: data.adSizeId,
      osType: data.osType,
      accessType: data.accessType,
      sspPayType: data.sspPayType,
      sspDealRatio: data.sspDealRatio,
      sspEcpm: data.sspEcpm,
      width: data.width,
      height: data.height,
      adImage: data.adImage,
      interactionType: data.interactionType || 0,
      enable: data.enable,
      remark: data.remark
    }

    // 更新交互类型的checkbox选中状态
    updateSelectedInteractionTypes()

    // 清除表格选中状态
    ids.value = []
    single.value = true
    multiple.value = true

    title.value = '修改广告位'
    open.value = true
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["mediaAdRef"].validate(valid => {
    if (valid) {
      // 从级联选择器中提取 mediaId 和 appId
      if (form.value.mediaAppCascade && form.value.mediaAppCascade.length > 0) {
        form.value.mediaId = form.value.mediaAppCascade[0]
        form.value.appId = form.value.mediaAppCascade[1] || null
      }

      // 如果结算方式是RTB，将分成系数设置为null
      if (form.value.sspPayType === '2') {
        form.value.sspDealRatio = null
      }

      if (form.value.id != null) {
        updateMediaAd(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMediaAd(form.value).then(() => {
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
  proxy.$modal.confirm('是否确认删除媒体广告位编号为"' + _ids + '"的数据项？').then(function() {
    return delMediaAd(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('flow/mediaAd/export', {
    ...queryParams.value
  }, `mediaAd_${new Date().getTime()}.xlsx`)
}

/** Tab 切换事件 */
function handleTabChange(tabName) {
  console.log('切换到 Tab:', tabName)
}

/** 配置按钮操作 */
function handleConfig(row) {
  getMediaAd(row.id).then(response => {
    configMediaAd.value = response.data
    if (configMediaAd.value?.sspPayType !== null && configMediaAd.value?.sspPayType !== undefined) {
      configMediaAd.value.sspPayType = String(configMediaAd.value.sspPayType)
    }
    // 重置配置表单
    configForm.value = {
      item1: '',
      item2: '',
      item3: ''
    }
    // 加载应用信息（获取操作系统类型）
    if (configMediaAd.value.appId) {
      loadAppInfo(configMediaAd.value.appId)
    }
    // 加载该广告位的预算方列表
    loadSlotList(row.id)
    // 打开配置抽屉
    configOpen.value = true
  })
}

function handleConfigSspPayTypeChange() {
  if (String(configMediaAd.value?.sspPayType) === '2') {
    configMediaAd.value.sspDealRatio = null
  }
}

function closeConfigDrawer() {
  configOpen.value = false
  configMediaAd.value = null
  slotList.value = []
  flowIndexList.value = []
  activeFlowIndex.value = null
  flowWeightByIndex.value = {}
  appInfo.value = null
}

/** 加载应用信息 */
function loadAppInfo(appId) {
  getApp(appId).then(response => {
    appInfo.value = response.data
  }).catch(() => {
    proxy.$modal.msgError('获取应用信息失败')
  })
}

/** 加载已绑定的DSP广告位列表 */
function loadSlotList(mediaAdId) {
  getLaunchConfig(mediaAdId).then(response => {
    const launchConfigList = response.data || []
    // 将后端返回的 dsp_launch 数据转换为前端需要的格式
    // dsp_launch 通过 ssp_slot_id 和 dsp_slot_id 关联三张表
    slotList.value = launchConfigList.map(launch => {
      const slotInfo = launch.dspSlotInfo || {}
      const slot = {
        id: launch.id, // dsp_launch 表的 ID
        dspSlotInfoId: launch.dspSlotId, // dsp_slot_info 表的 ID
        indexs: Number(launch.indexs) > 0 ? Number(launch.indexs) : 1,
        trafficWeight: launch.trafficWeight ?? 0,
        launchStrategy: launch.launchStrategy ?? 1,
        floorPrice: launch.floorPrice,
        ipLimit: launch.ipLimit,
        logCaptureAt: launch.logCaptureAt,
        trackSchwarz: launch.trackSchwarz || '',
        req: launch.req ?? 0,
        ims: launch.ims ?? 0,
        clk: launch.clk ?? 0,
        launchTime: launch.launchTime ?? 1,
        regionDirection: launch.regionDirection ?? 1,
        brandDirection: launch.brandDirection ?? 1,
        priceTransfer: launch.priceTransfer ?? 0,
        pkgTransfer: launch.pkgTransfer ?? 0,
        // DspSlotInfo 的字段（从 dsp_slot_info 表获取）
        name: slotInfo.name || '',
        osType: slotInfo.osType,
        adTypeId: slotInfo.adTypeId,
        adSceneId: slotInfo.adSceneId,
        adSizeId: slotInfo.adSizeId,
        dspSlotCode: slotInfo.dspSlotCode || '',
        dspAppKey: slotInfo.dspAppKey || '',
        dspAppSecret: slotInfo.dspAppSecret || '',
        dspAppId: slotInfo.dspAppId || '',
        dspAppPkg: slotInfo.dspAppPkg || '',
        dspAppVer: slotInfo.dspAppVer || '',
        dspAppStoreVer: slotInfo.dspAppStoreVer || '',
        priceEncryptKey: slotInfo.priceEncryptKey || '',
        dspAppStoreLink: slotInfo.dspAppStoreLink || '',
        dspPayType: slotInfo.dsp_pay_type,  // 使用下划线命名匹配后端返回
        sspPayType: launch.sspPayType ?? launch.ssp_pay_type ?? configMediaAd.value?.sspPayType,
        dspDealRatio: slotInfo.dsp_deal_ratio,
        activeCollapse: ['launch'] // 默认展开投放配置
      }
      // 调试：输出 dspPayType 信息
      console.log(`加载配置 [${slot.name}]: dspPayType=${slot.dspPayType}, 类型=${typeof slot.dspPayType}, 是否RTB=${Number(slot.dspPayType) === 2}`)
      return slot
    })
    syncFlowStateFromSlotList()
  }).catch(() => {
    proxy.$modal.msgError('获取投放配置失败')
    slotList.value = []
    flowIndexList.value = []
    activeFlowIndex.value = null
    flowWeightByIndex.value = {}
  })
}

// 状态颜色
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


/** 添加DSP广告位绑定 */
function handleAddSlot() {
  const sortedIndexList = [...flowIndexList.value].sort((a, b) => a - b)
  const lastIndex = sortedIndexList.length > 0 ? sortedIndexList[sortedIndexList.length - 1] : 0
  const nextIndex = lastIndex + 1

  flowIndexList.value.push(nextIndex)
  flowWeightByIndex.value[String(nextIndex)] = flowIndexList.value.length === 1 ? 100 : 0
  activeFlowIndex.value = nextIndex
}

/** 在指定流量模块中添加预算 */
async function handleAddBudget(flowIndex) {
  activeFlowIndex.value = Number(flowIndex)
  // 从数据库查询匹配的 DSP 广告位数据
  const sspSlotId = configMediaAd.value?.id

  if (!sspSlotId) {
    proxy.$modal.msgError('无法获取当前 SSP 广告位 ID')
    return
  }

  try {
    // 调用接口查询匹配的 DSP 广告位
    const response = await getMatchedDspSlots(sspSlotId)
    let matchedSlots = response.data || []

    console.log('========== 流量拆分调试信息 ==========')
    console.log('SSP广告位ID:', sspSlotId)
    console.log('当前SSP广告位信息:', configMediaAd.value)
    console.log('后端返回的原始数据:', response.data)
    if (matchedSlots.length > 0) {
      console.log('第一条数据示例:', JSON.stringify(matchedSlots[0], null, 2))
    }
    console.log('matchedSlots 数量:', matchedSlots.length)
    console.log('已绑定的DSP广告位数量:', slotList.value.length)

    // 过滤掉已经绑定的 DSP 广告位（排除已删除未保存的）
    const addedSlotIds = slotList.value
      .filter(slot => !slot.deleted)  // 排除已删除的
      .map(slot => slot.dspSlotInfoId)
      .filter(id => id)
    console.log('已绑定的DSP广告位ID列表:', addedSlotIds)

    // 过滤：排除掉已经绑定的 DSP 广告位（通过 dsp_slot_info 的 id 判断）
    matchedSlots = matchedSlots.filter(slot => {
      if (addedSlotIds.includes(slot.id)) {
        return false
      }
      return true
    })

    console.log('过滤后的 matchedSlots 数量:', matchedSlots.length)

    if (matchedSlots.length > 0) {
      console.log('打开选择对话框，可选DSP广告位:', matchedSlots)
      // 如果有匹配的数据，打开选择对话框
      matchedDspSlots.value = matchedSlots
      selectedDspSlotIds.value = []
      handleResetSlotQuery()
      selectSlotDialogVisible.value = true
    } else {
      console.warn('没有可用的DSP广告位')
      // 所有匹配的数据都已绑定，提示用户
      if (response.data && response.data.length > 0) {
        proxy.$modal.msgWarning('所有匹配的 DSP 广告位都已被添加')
      } else {
        proxy.$modal.msgWarning('未找到匹配的 DSP 广告位，请检查：\n1. 是否存在相同广告类型的预算方广告位\n2. 广告类型、操作系统等配置是否匹配')
      }
    }
  } catch (error) {
    console.error('查询 DSP 广告位数据失败:', error)
    proxy.$modal.msgError('查询 DSP 广告位数据失败: ' + (error.message || '未知错误'))
  }
}

/** 确认绑定DSP广告位 */
function handleConfirmSelectSlot() {
  if (selectedDspSlotIds.value.length === 0) {
    proxy.$modal.msgWarning('请至少选择一个 DSP 广告位')
    return
  }

  let addedCount = 0
  const currentFlowIndex = Number(activeFlowIndex.value) > 0 ? Number(activeFlowIndex.value) : 1

  if (!flowIndexList.value.includes(currentFlowIndex)) {
    flowIndexList.value.push(currentFlowIndex)
  }
  if (flowWeightByIndex.value[String(currentFlowIndex)] === undefined) {
    flowWeightByIndex.value[String(currentFlowIndex)] = flowIndexList.value.length === 1 ? 100 : 0
  }

  // 根据选中的 ID 找到对应的数据并添加到列表
  selectedDspSlotIds.value.forEach(slotId => {
    const matchedSlot = matchedDspSlots.value.find(slot => slot.id === slotId)
    if (matchedSlot) {
      const newSlot = {
        id: null,
        dspSlotInfoId: matchedSlot.id,
        name: matchedSlot.name || 'DSP广告位',
        osType: getOsTypeValue(matchedSlot),
        adTypeId: getAdTypeIdValue(matchedSlot),
        adSceneId: matchedSlot.ad_scene_id !== undefined ? matchedSlot.ad_scene_id : matchedSlot.adSceneId,
        adSizeId: matchedSlot.ad_size_id !== undefined ? matchedSlot.ad_size_id : matchedSlot.adSizeId,
        dspSlotCode: getDspSlotCodeValue(matchedSlot) || '',
        dspAppKey: getDspAppKeyValue(matchedSlot) || '',
        dspAppSecret: matchedSlot.dsp_app_secret !== undefined ? matchedSlot.dsp_app_secret : matchedSlot.dspAppSecret || '',
        dspAppId: matchedSlot.dsp_app_id !== undefined ? matchedSlot.dsp_app_id : matchedSlot.dspAppId || '',
        dspAppPkg: matchedSlot.dsp_app_pkg !== undefined ? matchedSlot.dsp_app_pkg : matchedSlot.dspAppPkg || '',
        dspAppVer: matchedSlot.dsp_app_ver !== undefined ? matchedSlot.dsp_app_ver : matchedSlot.dspAppVer || '',
        dspAppStoreVer: matchedSlot.dsp_app_store_ver !== undefined ? matchedSlot.dsp_app_store_ver : matchedSlot.dspAppStoreVer || '',
        priceEncryptKey: matchedSlot.price_encrypt_key !== undefined ? matchedSlot.price_encrypt_key : matchedSlot.priceEncryptKey || '',
        dspAppStoreLink: matchedSlot.dsp_app_store_link !== undefined ? matchedSlot.dsp_app_store_link : matchedSlot.dspAppStoreLink || '',
        dspPayType: getDspPayTypeValue(matchedSlot),
        sspPayType: configMediaAd.value?.sspPayType ?? null,
        dspDealRatio: matchedSlot.dsp_deal_ratio !== undefined ? matchedSlot.dsp_deal_ratio : matchedSlot.dspDealRatio,
        indexs: currentFlowIndex,
        // 投放配置字段
        trafficWeight: getFlowWeight(currentFlowIndex),
        launchStrategy: 1,
        floorPrice: null,
        ipLimit: null,
        logCaptureAt: 300,
        trackSchwarz: '',
        req: 0,
        ims: 0,
        clk: 0,
        launchTime: 1,
        regionDirection: 1,
        brandDirection: 1,
        priceTransfer: 0,
        pkgTransfer: 0,
        activeCollapse: ['launch']
      }
      console.log(`添加 DSP 广告位 [${newSlot.name}]: dspPayType=${newSlot.dspPayType}, 类型=${typeof newSlot.dspPayType}`)
      slotList.value.push(newSlot)
      addedCount += 1
    }
  })

  if (addedCount > 0) {
    slotList.value.forEach(slot => {
      slot.trafficWeight = getFlowWeight(getSlotFlowIndex(slot))
    })
  }

  // 关闭对话框
  selectSlotDialogVisible.value = false
  proxy.$modal.msgSuccess(`成功添加 ${selectedDspSlotIds.value.length} 个 DSP 广告位`)
}

/** 取消选择DSP广告位 */
function handleCancelSelectSlot() {
  selectSlotDialogVisible.value = false
  // 重置检索条件
  handleResetSlotQuery()
}

/** DSP广告位弹窗检索 */
function handleSlotQuery() {
  // 检索逻辑由 computed 属性 filteredMatchedDspSlots 自动处理
  console.log('执行检索，检索条件:', slotQueryParams.value)
}

function handleSlotCompanyChange() {
  slotQueryParams.value.productId = null
  slotQueryParams.value.budgetSlotId = null
}

function handleSlotProductChange() {
  slotQueryParams.value.budgetSlotId = null
}

/** DSP广告位弹窗重置检索 */
function handleResetSlotQuery() {
  slotQueryParams.value = {
    companyId: null,
    productId: null,
    budgetSlotId: null,
    osType: null,
    adTypeId: null,
    dspSlotCode: null
  }
}

function isEmptyValue(value) {
  return value === null || value === undefined || value === ''
}

function isSameId(a, b) {
  if (isEmptyValue(a) || isEmptyValue(b)) {
    return false
  }
  return String(a) === String(b)
}

function getSlotCompanyId(row) {
  return row.company_id !== undefined ? row.company_id : (row.companyId !== undefined ? row.companyId : null)
}

function getSlotProductId(row) {
  if (row.product_id !== undefined) {
    return row.product_id
  }
  if (row.dsp_product_id !== undefined) {
    return row.dsp_product_id
  }
  return row.productId !== undefined ? row.productId : null
}

function getSlotCompanyName(row) {
  const companyName = row.company_name !== undefined ? row.company_name : row.companyName
  if (companyName) {
    return companyName
  }
  const companyId = getSlotCompanyId(row)
  const company = budgetCompanyList.value.find(item => isSameId(item.id ?? item.company_id, companyId))
  return company?.name || companyId || ''
}

function getSlotProductName(row) {
  const productName = row.product_name !== undefined ? row.product_name : row.productName
  if (productName) {
    return productName
  }
  const productId = getSlotProductId(row)
  const product = budgetProductList.value.find(item => isSameId(item.id ?? item.product_id, productId))
  return product?.name || productId || ''
}

function getBudgetOptionLabel(row) {
  const name = row?.name || '未命名'
  const id = row?.id ?? '-'
  return `预算 ${name}(${id})`
}

/** 获取操作系统类型值（兼容驼峰和下划线） */
function getOsTypeValue(row) {
  return row.os_type !== undefined ? row.os_type : row.osType
}

/** 获取广告类型ID值（兼容驼峰和下划线） */
function getAdTypeIdValue(row) {
  return row.ad_type_id !== undefined ? row.ad_type_id : row.adTypeId
}

/** 获取广告位编码值（兼容驼峰和下划线） */
function getDspSlotCodeValue(row) {
  return row.dsp_slot_code !== undefined ? row.dsp_slot_code : row.dspSlotCode
}

/** 获取DSP AppKey值（兼容驼峰和下划线） */
function getDspAppKeyValue(row) {
  return row.dsp_app_key !== undefined ? row.dsp_app_key : row.dspAppKey
}

/** 获取结算方式值（兼容驼峰和下划线） */
function getDspPayTypeValue(row) {
  return row.dsp_pay_type !== undefined ? row.dsp_pay_type : row.dspPayType
}

function isSlotRtb(slot) {
  return Number(getDspPayTypeValue(slot)) === 2
}

function shouldShowFloorPrice(slot) {
  const basePayType = String(configMediaAd.value?.sspPayType ?? slot?.sspPayType ?? '')
  return isSlotRtb(slot) && (basePayType === '1' || basePayType === '3')
}

/** DSP广告位表格选择变化 */
function handleSlotSelectionChange(selection) {
  selectedDspSlotIds.value = selection.map(item => item.id)
}

/** 复制DSP广告位配置 */
function handleCopySlot(index) {
  const originalSlot = slotList.value[index]
  const copiedSlot = {
    ...originalSlot,
    id: null,
    name: originalSlot.name + ' (副本)',
    activeCollapse: ['launch']
  }
  slotList.value.splice(index + 1, 0, copiedSlot)
  proxy.$modal.msgSuccess('复制成功')
}

/** 删除DSP广告位（软删除） */
function handleDeleteSlot(slot) {
  proxy.$modal.confirm('确认删除该 DSP 广告位吗？').then(() => {
    // 软删除：标记 deleted 为 true，而不是直接从数组移除
    slot.deleted = true
    proxy.$modal.msgSuccess('删除成功（未保存）')
  }).catch(() => {})
}

/** 恢复DSP广告位（撤销删除） */
function handleRestoreSlot(slot) {
  proxy.$modal.confirm('确认恢复该 DSP 广告位吗？').then(() => {
    slot.deleted = false
    proxy.$modal.msgSuccess('已恢复')
  }).catch(() => {})
}

/** 捕获日志 */
function handleCaptureLog(slot) {
  // 获取当前时间的秒级时间戳
  const now = Math.floor(Date.now() / 1000)
  // 加上3分钟（180秒）
  const captureTime = now + 180
  // 设置到 slot
  slot.logCaptureAt = captureTime

  // 显示提示
  const date = new Date(captureTime * 1000)
  const timeStr = date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })

  // 立即提交到后端和 etcd（使用下划线命名匹配后端 @JsonProperty）
  const saveData = {
    mediaAdId: configMediaAd.value.id,
    slotList: [{
      dsp_slot_id: slot.dspSlotInfoId,
      indexs: getSlotFlowIndex(slot),
      traffic_weight: getFlowWeight(getSlotFlowIndex(slot)),
      launch_strategy: slot.launchStrategy ?? 1,
      floor_price: slot.floorPrice,
      ip_limit: slot.ipLimit,
      log_capture_at: slot.logCaptureAt,
      track_schwarz: slot.trackSchwarz || '',
      req: slot.req ?? 0,
      ims: slot.ims ?? 0,
      clk: slot.clk ?? 0,
      launch_time: slot.launchTime ?? 1,
      region_direction: slot.regionDirection ?? 1,
      brand_direction: slot.brandDirection ?? 1
    }]
  }

  console.log('捕获日志 - 提交配置数据:', saveData)

  // 调用 API 保存投放配置
  saveLaunchConfig(saveData).then(() => {
    proxy.$modal.msgSuccess(`捕获日志设置成功，已同步至: ${timeStr} (${captureTime}秒)`)
    // 保存成功后重新加载配置数据
    if (configMediaAd.value?.id) {
      loadSlotList(configMediaAd.value.id)
    }
  }).catch(() => {
    proxy.$modal.msgError('捕获日志设置失败')
  })
}

const rtbDealRatioSavingIds = new Set()

/** RTB结算比例修改后立即保存 */
function handleRtbDealRatioChange(slot, value) {
  if (!slot || !slot.dspSlotInfoId) {
    proxy.$modal.msgError('保存失败：缺少广告位ID')
    return
  }

  if (value === null || value === undefined || value === '') {
    return
  }

  const slotId = Number(slot.dspSlotInfoId)
  const ratio = Number(value)
  if (!slotId || Number.isNaN(ratio)) {
    proxy.$modal.msgError('保存失败：RTB结算比例格式错误')
    return
  }
  const ratioInt = Math.round(ratio)

  if (rtbDealRatioSavingIds.has(slotId)) {
    return
  }
  rtbDealRatioSavingIds.add(slotId)

  updateInfo({
    id: slotId,
    dspDealRatio: ratioInt,
    dsp_deal_ratio: ratioInt
  }).then(() => {
    slot.dspDealRatio = ratioInt
    proxy.$modal.msgSuccess('RTB结算比例已自动保存')
  }).catch(() => {
    proxy.$modal.msgError('RTB结算比例保存失败')
  }).finally(() => {
    rtbDealRatioSavingIds.delete(slotId)
  })
}

/** 返回列表 */
function handleBackToList() {
  activeTab.value = 'list'
  configMediaAd.value = null
  editMediaAd.value = null
  // 清空编辑表单验证状态
  if (proxy.$refs["editFormRef"]) {
    proxy.$refs["editFormRef"].clearValidate()
  }
}

/** 编辑页面保存按钮 */
function handleEditSave() {
  proxy.$refs["editFormRef"].validate(valid => {
    if (valid) {
      // 如果结算方式是RTB，将分成系数设置为null
      if (editForm.value.sspPayType === '2') {
        editForm.value.sspDealRatio = null
      }

      // 移除不需要提交的字段（osType和accessType从app表获取，不需要保存）
      const submitData = {
        id: editForm.value.id,
        mediaId: editForm.value.mediaId,
        appId: editForm.value.appId,
        name: editForm.value.name,
        nameAlise: editForm.value.nameAlise,
        adTypeId: editForm.value.adTypeId,
        adSceneId: editForm.value.adSceneId,
        adSizeId: editForm.value.adSizeId,
        sspPayType: editForm.value.sspPayType,
        sspDealRatio: editForm.value.sspDealRatio,
        sspEcpm: editForm.value.sspEcpm,
        width: editForm.value.width,
        height: editForm.value.height,
        adImage: editForm.value.adImage,
        interactionType: editForm.value.interactionType,
        enable: editForm.value.enable,
        remark: editForm.value.remark
      }

      // 根据是否有id判断是新增还是修改
      if (submitData.id) {
        updateMediaAd(submitData).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          editMediaAd.value = null
          getList()
        })
      } else {
        addMediaAd(submitData).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          editMediaAd.value = null
          getList()
        })
      }
    }
  })
}

/** 保存投放配置 */
function handleSaveConfig() {
  if (!configMediaAd.value) {
    proxy.$modal.msgWarning('未找到广告位基础信息')
    return
  }

  if (String(configMediaAd.value.sspPayType) === '1') {
    if (configMediaAd.value.sspDealRatio === null || configMediaAd.value.sspDealRatio === undefined || configMediaAd.value.sspDealRatio === '') {
      proxy.$modal.msgWarning('分成结算时，分成系数不能为空')
      return
    }
  }

  // 统计未删除的项
  const activeSlots = slotList.value.filter(slot => !slot.deleted)

  // 空列表时，提示确认是否清空配置
  if (activeSlots.length === 0) {
    proxy.$modal.confirm('DSP 广告位为空，将清空所有投放配置，是否继续？').then(() => {
      saveConfigBasicInfo().then(() => {
        doSaveConfig([])
      }).catch(() => {})
    }).catch(() => {})
    return
  }

  // 验证流量权重总和必须等于100
  const totalWeight = totalTrafficWeight.value
  if (totalWeight !== 100) {
    proxy.$modal.msgWarning(`流量权重总和必须为 100，当前为：${totalWeight}`)
    return
  }

  const activeFlowIndexes = Array.from(new Set(activeSlots.map(slot => getSlotFlowIndex(slot))))
  for (const flowIndex of activeFlowIndexes) {
    if (getFlowWeight(flowIndex) <= 0) {
      proxy.$modal.msgWarning(`流量${flowIndex}的流量权重必须大于 0`)
      return
    }
  }

  // 验证每个DSP广告位的必填字段（跳过已删除的项）
  for (let i = 0; i < slotList.value.length; i++) {
    const slot = slotList.value[i]

    // 跳过已删除的项
    if (slot.deleted) {
      continue
    }

    // 验证其他必填字段
    if (!slot.launchStrategy) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的投放策略不能为空`)
      return
    }
    if (!slot.logCaptureAt && slot.logCaptureAt !== 0) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的捕获日志时长不能为空`)
      return
    }
    if (!slot.launchTime) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的投放时段不能为空`)
      return
    }
    if (!slot.regionDirection) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的地域定向不能为空`)
      return
    }
    if (!slot.brandDirection) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的品牌定向不能为空`)
      return
    }
    if (slot.priceTransfer === null || slot.priceTransfer === undefined) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的价格透传不能为空`)
      return
    }
    if (slot.pkgTransfer === null || slot.pkgTransfer === undefined) {
      proxy.$modal.msgWarning(`第 ${i + 1} 个 DSP 广告位的包透传不能为空`)
      return
    }
  }

  // 执行保存
  saveConfigBasicInfo().then(() => {
    doSaveConfig(slotList.value)
  }).catch(() => {})
}

function saveConfigBasicInfo() {
  const payType = configMediaAd.value.sspPayType !== null && configMediaAd.value.sspPayType !== undefined
    ? String(configMediaAd.value.sspPayType)
    : null

  const saveBasicData = {
    id: configMediaAd.value.id,
    mediaId: configMediaAd.value.mediaId,
    appId: configMediaAd.value.appId,
    name: configMediaAd.value.name,
    nameAlise: configMediaAd.value.nameAlise,
    adTypeId: configMediaAd.value.adTypeId,
    adSceneId: configMediaAd.value.adSceneId,
    adSizeId: configMediaAd.value.adSizeId,
    sspPayType: payType,
    sspDealRatio: payType === '2' ? null : configMediaAd.value.sspDealRatio,
    sspEcpm: configMediaAd.value.sspEcpm,
    width: configMediaAd.value.width,
    height: configMediaAd.value.height,
    adImage: configMediaAd.value.adImage,
    interactionType: configMediaAd.value.interactionType,
    enable: configMediaAd.value.enable,
    remark: configMediaAd.value.remark
  }

  return updateMediaAd(saveBasicData).catch(() => {
    proxy.$modal.msgError('基础信息保存失败')
    throw new Error('save basic info failed')
  })
}

/** 执行保存配置 */
function doSaveConfig(slotList) {
  // 过滤掉已删除的项，只保存未删除的配置
  const activeSlotList = slotList.filter(slot => !slot.deleted)

  // 构建保存数据（使用下划线命名匹配后端 @JsonProperty）
  const saveData = {
    mediaAdId: configMediaAd.value.id,
    slotList: activeSlotList.map(slot => ({
      dsp_slot_id: slot.dspSlotInfoId,
      indexs: getSlotFlowIndex(slot),
      traffic_weight: getFlowWeight(getSlotFlowIndex(slot)),
      launch_strategy: slot.launchStrategy ?? 1,
      floor_price: slot.floorPrice,
      ip_limit: slot.ipLimit,
      log_capture_at: slot.logCaptureAt ?? 300,
      track_schwarz: slot.trackSchwarz || '',
      req: slot.req ?? 0,
      ims: slot.ims ?? 0,
      clk: slot.clk ?? 0,
      launch_time: slot.launchTime ?? 1,
      region_direction: slot.regionDirection ?? 1,
      brand_direction: slot.brandDirection ?? 1,
      price_transfer: slot.priceTransfer ?? 0,
      pkg_transfer: slot.pkgTransfer ?? 0
    }))
  }

  console.log('保存配置数据:', saveData)

  // 调用 API 保存投放配置
  saveLaunchConfig(saveData).then(() => {
    proxy.$modal.msgSuccess('配置保存成功')
    // 保存成功后重新加载配置数据
    if (configMediaAd.value?.id) {
      loadSlotList(configMediaAd.value.id)
    }
  }).catch(() => {
    proxy.$modal.msgError('配置保存失败')
  })
}

// 页面加载时获取级联数据和媒体列表
loadCascaderData()
loadMediaList()
loadAdTypeList()
loadAdSceneList()
loadAdSizeList()
loadBudgetCompanyAndProduct()
getList()
</script>

<style scoped>
.app-container-list {
  padding: 0;
}

/* 表格容器样式 */
.media-ad-table {
  width: 100%;
}

/* 表格内部包装器 - 这是关键 */
.media-ad-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto !important;
  overflow-y: visible !important;
}

/* 表头和表格体也要横向滚动 */
.media-ad-table :deep(.el-table__header-wrapper),
.media-ad-table :deep(.el-table__body-wrapper) {
  overflow-x: auto !important;
  overflow-y: visible !important;
}

/* 固定列的样式 */
.media-ad-table :deep(.el-table__fixed) {
  height: 100% !important;
}

/* 确保表格列宽度不会被压缩 */
.media-ad-table :deep(.el-table__header),
.media-ad-table :deep(.el-table__body) {
  width: max-content !important;
}

.media-ad-table :deep(table) {
  width: 100%;
  table-layout: auto;
}

.app-container-edit {
  padding: 20px;
}

.app-container-config {
  padding: 4px;
}

.config-card {
  height: 270px;
  margin-bottom: 20px;
}

.flow-config-card {
  height: auto;
}

.edit-card {
  margin-bottom: 20px;
}

/* 交互类型选择器样式 */
.interaction-type-selector-wrapper {
  width: 100%;
}

.interaction-type-selector-wrapper :deep(.el-checkbox-group) {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.interaction-type-selector-wrapper :deep(.el-checkbox.is-bordered) {
  margin-right: 0;
  padding: 10px 16px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.interaction-type-selector-wrapper :deep(.el-checkbox.is-bordered:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.interaction-type-selector-wrapper :deep(.el-checkbox.is-bordered.is-checked) {
  background: #409eff !important;
  border-color: #409eff !important;
  color: #fff !important;
}

.interaction-type-selector-wrapper :deep(.el-checkbox.is-bordered.is-checked .el-checkbox__label) {
  color: #fff !important;
}

.interaction-type-selector-wrapper :deep(.el-checkbox.is-bordered.is-checked .el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #fff !important;
  border-color: #fff !important;
}

.interaction-type-selector-wrapper :deep(.el-checkbox.is-bordered.is-checked .el-checkbox__input.is-checked .el-checkbox__inner::after) {
  border-color: #409eff !important;
}

.interaction-hint {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
  padding: 8px 12px;
  background: #f4f4f5;
  border-radius: 4px;
  font-size: 13px;
  color: #606266;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.empty-config {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.empty-edit {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.empty-slots {
  padding: 40px 0;
}

.flow-module {
  margin-bottom: 16px;
}

.flow-module:last-child {
  margin-bottom: 0;
}

.flow-module-card {
  border: 1px solid #e4e7ed;
  background: #fbfcff;
}

.flow-module-header {
  min-height: 32px;
}

.empty-flow-slots {
  padding: 16px 0;
}

.slot-card {
  margin-bottom: 20px;
}

.slot-card-deleted {
  opacity: 0.6;
}

.slot-card-deleted .el-card {
  background-color: #fef0f0;
  border-color: #fbc4c4;
}

.slot-card:last-child {
  margin-bottom: 0;
}

.slot-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.weight-input-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: #f5f7fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

.weight-label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
}

.title-text {
  font-size: 15px;
  color: #303133;
  font-weight: 600;
}

.info-highlight {
  font-weight: 600;
  color: #409eff;
}

.info-code {
  font-family: 'Courier New', monospace;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 12px;
  color: #606266;
}

.info-link {
  color: #409eff;
  word-break: break-all;
}

.collapse-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.slot-collapse {
  margin-top: 0;
}

.slot-collapse :deep(.el-collapse-item__header) {
  font-weight: 500;
  background-color: #f5f7fa;
}

.slot-collapse :deep(.el-collapse-item__content) {
  padding: 20px;
  background-color: #fafafa;
  border-radius: 4px;
}

.os-tip {
  margin-left: 8px;
  color: #909399;
  font-size: 12px;
}

.os-loading {
  color: #909399;
  font-size: 12px;
}

.form-tip {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
  line-height: 1.4;
}

/* 基础信息容器样式 */
.basic-info-container {
  padding: 1px 0;
}

.basic-info-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  overflow: hidden;
}

.basic-info-table th,
.basic-info-table td {
  border: 1px solid #ebeef5;
  padding: 10px 12px;
  font-size: 13px;
  line-height: 1.5;
  vertical-align: middle;
  word-break: break-all;
}

.basic-info-table th {
  width: 15%;
  background: #f8f9fb;
  color: #606266;
  font-weight: 500;
}

.basic-info-table td {
  width: 35%;
  color: #303133;
  font-weight: 500;
  background: #fff;
}

.settlement-section {
  margin-top: 5px 16px;
  padding: 19px 14px 10px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 6px;
}

.settlement-title {
  margin-bottom: 10px;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.settlement-form {
  margin-top: 0;
}

.settlement-form :deep(.el-form-item) {
  margin-bottom: 12px;
}

.settlement-form :deep(.el-form-item__label) {
  color: #606266;
  font-weight: 500;
}

/* 投放配置表单样式 */
.launch-config-form {
  margin-top: 8px;
}

.launch-config-form :deep(.el-form-item) {
  margin-bottom: 16px;
}

.launch-config-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

.launch-config-form :deep(.el-input-number) {
  width: 100%;
}

.launch-config-form :deep(.el-input-number .el-input__inner) {
  text-align: left;
}

.launch-config-form :deep(.el-select) {
  width: 100%;
}
.my-table tbody tr:hover > td {
  background-color: #e6f7ff !important;
}


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
  margin-right: 20px;  /* 默认一般是 18px+ */
  margin-bottom: 20px;
}

:deep(.el-form--inline .el-form-item) {
  margin-right: 8px;   /* 👈 调小间距 */
  margin-bottom: 8px;  /* 👈 行间距也可以顺便优化 */
}

.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
}

/* 绿色（正常） */
.dot-success {
  background-color: #67c23a;
}

/* 红色（禁用/拒绝） */
.dot-danger {
  background-color: #f56c6c;
}

/* 黄色（审核中） */
.dot-warning {
  background-color: #e6a23c;
}


.status-wrap {
  display: inline-flex;
  align-items: center;
  white-space: nowrap; /* 关键：禁止换行 */
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
  margin: 15px 10px 8px;
}

.back-icon {
  cursor: pointer;
  font-size: 18px;
  margin-left: 20px;
  margin-top: 8px;
}

.drawer-actions {
  display: flex;
  justify-content: flex-end;  /* 👉 改这里 */
  gap: 1px;
  padding: 0 20px 10px;
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
  width: 1000px;
  margin: 0 auto;
  overflow-y: auto;
  background: #fff;
}

.edit-form {
  width: 900px;
  padding: 20px;
}


</style>

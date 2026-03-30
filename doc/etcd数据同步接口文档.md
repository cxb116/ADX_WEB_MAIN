# DSP 数据同步接口文档

## 📋 文档说明

本文档描述后台系统如何通过 etcd 将配置数据实时同步给 DSP 引擎。

**适用人群**：后台开发工程师
**最后更新**：2026-02-26

---

## 🎯 概述

当后台修改以下配置时，需要通过 etcd 推送变化给 DSP 引擎：

1. **预算广告位**（DspSlotInfo）：预算方的广告位配置
2. **预算公司**（DspCompany）：预算方公司信息
3. **投放绑定**（DspLaunch）：媒体广告位与预算广告位的绑定关系
4. **媒体广告位**（SspSlotInfo）：媒体方的广告位配置

DSP 引擎会实时监听 etcd 变化，自动更新内存中的配置数据。

**数据同步流程**：
1. DSP 启动时从数据库加载全量数据到内存
2. 启动 etcd Watch 监听器，实时监听配置变化
3. 后台通过 etcd 推送配置变更
4. DSP 收到事件后更新内存数据

---

## 📌 etcd Key 规范

### 1. 预算广告位（DspSlotInfo）

```
格式：{ETCD_PREFIX}/dsp/{action}/{id}

示例：
- /dsp/add/123        添加预算广告位
- /dsp/update/123     更新预算广告位
- /dsp/delete/123     删除预算广告位
```

**参数说明**：
- `{ETCD_PREFIX}`：etcd 前缀（由配置文件指定，默认为 `/dsp/config`）
- `{action}`：操作类型，固定值 `add`、`update`、`delete`
- `{id}`：预算广告位的主键 ID

---

### 2. 预算公司（DspCompany）

```
格式：{ETCD_PREFIX}/company/{action}/{id}

示例：
- /company/add/10     添加预算公司
- /company/update/10  更新预算公司
- /company/delete/10  删除预算公司
```

**参数说明**：
- `{action}`：操作类型，固定值 `add`、`update`、`delete`
- `{id}`：预算公司的主键 ID

---

### 3. 投放绑定（DspLaunch）

```
格式：{ETCD_PREFIX}/launch/{action}/{id}

示例：
- /launch/add/50      添加投放绑定
- /launch/update/50   更新投放绑定
- /launch/delete/50   删除投放绑定
```

**参数说明**：
- `{action}`：操作类型，固定值 `add`、`update`、`delete`
- `{id}`：投放绑定的主键 ID

---

### 4. 媒体广告位（SspSlotInfo）

```
格式：{ETCD_PREFIX}/sspslot/{action}/{id}

示例：
- /sspslot/add/1001   添加媒体广告位
- /sspslot/update/1001 更新媒体广告位
- /sspslot/delete/1001 删除媒体广告位
```

**参数说明**：
- `{action}`：操作类型，固定值 `add`、`update`、`delete`
- `{id}`：媒体广告位的主键 ID

---

## 📦 数据格式（JSON）

### 1. 预算广告位（DspSlotInfo）

**Key**：`/dsp/add/123` 或 `/dsp/update/123`

**Value**：
```json
{
  "id": 123,
  "name": "预算位A",
  "scene_id": 1001,
  "os_type": 1,
  "dsp_slot_code": "slot_001",
  "dsp_app_key": "app_key_xxx",
  "dsp_app_id": "app_id_xxx",
  "dsp_app_pkg": "com.example.app",
  "dsp_app_ver": "1.0.0,1.1.0",
  "dsp_app_store_ver": "1.0,2.0",
  "price_encrypt_key": "encrypt_key_xxx",
  "dsp_app_store_link": "https://example.com/app,https://example.com/app2",
  "dsp_pay_type": "2",
  "dsp_deal_ratio": 80,
  "dsp_company_id": 10
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | int64 | 是 | 预算广告位主键 ID |
| name | string | 否 | 预算名称 |
| scene_id | int64 | 否 | 预算方广告类型 |
| os_type | int | 否 | 预算操作系统：1=Android，2=iOS |
| dsp_slot_code | string | 是 | 预算位编码（唯一标识） |
| dsp_app_key | string | 是 | 预算方 AppKey |
| dsp_app_id | string | 否 | 预算方 AppId |
| dsp_app_pkg | string | 否 | 应用包名 |
| dsp_app_ver | string | 否 | 应用版本（多个用逗号分隔） |
| dsp_app_store_ver | string | 否 | 应用商店版本（多个用逗号分隔） |
| price_encrypt_key | string | 是 | 价格加密密钥 |
| dsp_app_store_link | string | 否 | 应用商店地址（多个用逗号分隔） |
| dsp_pay_type | string | 是 | 结算方式：`1`=分成，`2`=RTB |
| dsp_deal_ratio | int | 否 | 成交系数（0-100），RTB 模式有效 |
| dsp_company_id | int64 | 是 | 所属预算公司 ID |

**代码位置**：`impl/DspSlotInfo.go:3`

---

### 2. 预算公司（DspCompany）

**Key**：`/company/add/10` 或 `/company/update/10`

**Value**：
```json
{
  "id": 10,
  "name": "预算公司A",
  "dsp_code": 10001,
  "url": "https://api.example.com/bid",
  "method": "POST",
  "timeout": 650
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | int64 | 是 | 预算公司主键 ID |
| name | string | 是 | 公司名称 |
| dsp_code | int64 | 是 | DSP 编码（唯一标识） |
| url | string | 是 | 预算方请求地址 |
| method | string | 是 | 请求方法：`POST` 或 `GET` |
| timeout | int64 | 是 | 超时时间（毫秒） |

**代码位置**：`impl/DspCompany.go:3`

**⚠️ 注意**：
- `dsp_code` 在代码中是 `int64` 类型，不是 `string`
- `method` 字段 TODO 注释表示后续可能改为 `int` 类型

---

### 3. 投放绑定（DspLaunch）

**Key**：`/launch/add/50` 或 `/launch/update/50`

**Value**：
```json
{
  "id": 50,
  "ssp_slot_id": 1001,
  "dsp_slot_id": 123,
  "traffic_weight": 100,
  "launch_strategy": 1,
  "floor_price": 100,
  "ip_limit": 5,
  "track_schwarz": "black1,black2",
  "log_capture_at": 300,
  "request_count": 0,
  "show_count": 0,
  "click_count": 0,
  "launch_time": 1,
  "crowd_direction": 1,
  "region_direction": 1,
  "brand_direction": 1
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | int64 | 是 | 投放绑定主键 ID |
| ssp_slot_id | int64 | 是 | 媒体广告位 ID |
| dsp_slot_id | int64 | 是 | 预算广告位 ID |
| traffic_weight | int | 是 | 流量权重（整数类型） |
| launch_strategy | int | 否 | 投放策略：1=对接第三方，2=自主投放 |
| floor_price | int | 否 | 底价（单位：分） |
| ip_limit | int | 否 | IP 限流次数 |
| track_schwarz | string | 否 | 上报黑名单 |
| log_capture_at | int | 否 | 日志捕获时长（秒） |
| request_count | int | 否 | 请求次数（统计字段） |
| show_count | int | 否 | 展现次数（统计字段） |
| click_count | int | 否 | 点击次数（统计字段） |
| launch_time | int | 否 | 投放时段：1=全时段，2=自定义 |
| crowd_direction | int | 否 | 人群定向：1=不限制，2=定向，3=排除 |
| region_direction | int | 否 | 地域定向：1=不限制，2=定向，3=排除 |
| brand_direction | int | 否 | 品牌定向：1=不限制，2=定向，3=排除 |

**代码位置**：`impl/DspLaunch.go:3`

**⚠️ 注意**：
- `traffic_weight` 是 `int` 类型，不是 `string`
- 包含统计字段 `request_count`、`show_count`、`click_count`

---

### 4. 媒体广告位（SspSlotInfo）

**Key**：`/sspslot/add/1001` 或 `/sspslot/update/1001`

**Value**：
```json
{
  "id": 1001,
  "media_id": 201,
  "app_id": 301,
  "os_type": 1,
  "scene_id": 1001,
  "ssp_pay_type": 2,
  "ssp_deal_ratio": 70,
  "height": 500,
  "width": 320,
  "interaction_type": 1
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | int | 是 | 媒体广告位主键 ID |
| media_id | int | 是 | 媒体公司 ID |
| app_id | int | 是 | App 应用 ID |
| os_type | int | 是 | 操作系统类型：1=Android，2=iOS |
| scene_id | int | 是 | 媒体方广告类型 |
| ssp_pay_type | int | 是 | 下游媒体结算方式：1=分成，2=RTB |
| ssp_deal_ratio | int | 否 | 下游媒体分成系数（0-100），仅分成模式有效 |
| height | int | 是 | 广告位高度（像素） |
| width | int | 是 | 广告位宽度（像素） |
| interaction_type | int | 是 | 交互类型：1=打开网页，2=deeplink，3=直接下载，4=广点通，5=小程序，6=应用商店下载，7=快应用 |

**代码位置**：`impl/SspSlotInfo.go:3`

**⚠️ 注意**：
- `id` 字段是 `int` 类型，不是 `int64`
- `interaction_type` 支持多种交互方式，具体含义见字段说明

---

## 🔧 操作说明

### 1. 添加数据

**场景**：后台新增一条记录时

**步骤**：
1. 将数据写入数据库
2. 将数据 JSON 序列化
3. 调用 etcd 的 `Put` 接口，key 格式为 `{ETCD_PREFIX}/{type}/add/{id}`

**示例（伪代码）**：
```go
// 1. 数据库新增成功
sspSlotInfo := SspSlotInfo{
    Id:      1001,
    MediaId: 201,
    AppId:   301,
    OsType:  1,
    SceneId: 1001,
    // ... 其他字段
}
db.Create(&sspSlotInfo)

// 2. 序列化为 JSON
jsonData, _ := json.Marshal(sspSlotInfo)

// 3. 写入 etcd
etcdClient.Put(ctx, "/dsp/config/sspslot/add/1001", string(jsonData))
```

---

### 2. 更新数据

**场景**：后台修改一条记录时

**步骤**：
1. 更新数据库
2. 将最新数据 JSON 序列化
3. 调用 etcd 的 `Put` 接口，key 格式为 `{ETCD_PREFIX}/{type}/update/{id}`

**示例（伪代码）**：
```go
// 1. 数据库更新
db.Model(&SspSlotInfo{}).Where("id = ?", 1001).Updates(map[string]interface{}{
    "ssp_deal_ratio": 75,
})

// 2. 查询最新数据
var sspSlotInfo SspSlotInfo
db.First(&sspSlotInfo, 1001)

// 3. 写入 etcd
jsonData, _ := json.Marshal(sspSlotInfo)
etcdClient.Put(ctx, "/dsp/config/sspslot/update/1001", string(jsonData))
```

---

### 3. 删除数据

**场景**：后台删除一条记录时

**步骤**：
1. 删除数据库记录（软删除或硬删除）
2. 调用 etcd 的 `Delete` 接口，key 格式为 `{ETCD_PREFIX}/{type}/delete/{id}`

**示例（伪代码）**：
```go
// 1. 删除数据库记录
db.Delete(&SspSlotInfo{}, 1001)

// 2. 通知 etcd（使用 Delete 操作）
etcdClient.Delete(ctx, "/dsp/config/sspslot/delete/1001")
```

**⚠️ 注意**：
- 删除操作的 value 为空，只需要发送 Delete 事件即可
- DSP 收到删除事件后，会从内存中移除该数据
- 删除操作使用 `DELETE` 方法，不是 `PUT`

---

## ⚠️ 注意事项

### 1. Key 格式必须严格匹配

- ✅ 正确：`/dsp/config/sspslot/add/1001`
- ❌ 错误：`/dsp/sspslot/add/1001`（缺少前缀）
- ❌ 错误：`/dsp/config/sspslot/add/1001/`（末尾多了斜杠）

---

### 2. JSON 字段命名使用 snake_case

- ✅ 正确：`{"media_id": 201, "ssp_pay_type": 2}`
- ❌ 错误：`{"MediaId": 201, "SspPayType": 2}`

**原因**：Go 的 `json` tag 使用 snake_case

---

### 3. 操作类型必须正确

- 只能使用：`add`、`update`、`delete`
- 大小写敏感，必须小写

---

### 4. ID 必须与数据库一致

- etcd key 中的 `{id}` 必须与数据库主键完全一致
- DSP 通过 ID 关联不同表的数据
- 注意：`SspSlotInfo.Id` 是 `int` 类型，其他表的 ID 大多是 `int64`

---

### 5. 删除操作的特殊处理

- 删除时使用 etcd 的 `Delete` 方法，不是 `Put`
- 不需要传 value（value 为空）
- 只需要发送 Delete 事件，DSP 会自动清理内存

---

### 6. 数据类型注意事项

- `DspCompany.DspCode` 是 `int64` 类型，不是 `string`
- `DspLaunch.TrafficWeight` 是 `int` 类型，不是 `string`
- `DspSlotInfo.DspPayType` 是 `string` 类型，值为 `"1"` 或 `"2"`
- `SspSlotInfo.Id` 是 `int` 类型，不是 `int64`

---

### 7. 交互类型说明

`SspSlotInfo.InteractionType` 支持的值：
- `1` = 打开网页
- `2` = deeplink
- `3` = 直接下载应用
- `4` = 广点通
- `5` = 小程序跳转
- `6` = 应用商店下载
- `7` = 快应用

---

### 8. 顺序要求

当修改多个关联数据时，建议按以下顺序推送：

1. 先推送 `DspCompany`（如果有修改）
2. 再推送 `DspSlotInfo` 或 `SspSlotInfo`
3. 最后推送 `DspLaunch`（投放绑定）

**原因**：DSP 会通过外键 ID 关联不同表的数据

---

## 🧪 测试示例

### 使用 etcdctl 命令行工具测试

#### 1. 添加预算广告位

```bash
etcdctl put /dsp/config/dsp/add/123 '{
  "id": 123,
  "name": "预算位A",
  "scene_id": 1001,
  "os_type": 1,
  "dsp_slot_code": "slot_001",
  "dsp_app_key": "app_key_xxx",
  "dsp_app_id": "app_id_xxx",
  "price_encrypt_key": "encrypt_key_xxx",
  "dsp_pay_type": "2",
  "dsp_deal_ratio": 80,
  "dsp_company_id": 10
}'
```

#### 2. 更新预算广告位

```bash
etcdctl put /dsp/config/dsp/update/123 '{
  "id": 123,
  "name": "预算位A",
  "dsp_slot_code": "slot_001",
  "dsp_deal_ratio": 90,
  "dsp_company_id": 10
}'
```

#### 3. 删除预算广告位

```bash
etcdctl del /dsp/config/dsp/delete/123
```

#### 4. 添加预算公司

```bash
etcdctl put /dsp/config/company/add/10 '{
  "id": 10,
  "name": "预算公司A",
  "dsp_code": 10001,
  "url": "https://api.example.com/bid",
  "method": "POST",
  "timeout": 650
}'
```

#### 5. 添加投放绑定

```bash
etcdctl put /dsp/config/launch/add/50 '{
  "id": 50,
  "ssp_slot_id": 1001,
  "dsp_slot_id": 123,
  "traffic_weight": 100
}'
```

#### 6. 添加媒体广告位

```bash
etcdctl put /dsp/config/sspslot/add/1001 '{
  "id": 1001,
  "media_id": 201,
  "app_id": 301,
  "os_type": 1,
  "scene_id": 1001,
  "ssp_pay_type": 2,
  "ssp_deal_ratio": 70,
  "height": 500,
  "width": 320,
  "interaction_type": 1
}'
```

#### 7. 更新媒体广告位

```bash
etcdctl put /dsp/config/sspslot/update/1001 '{
  "id": 1001,
  "media_id": 201,
  "app_id": 301,
  "ssp_deal_ratio": 75,
  "height": 600,
  "width": 400
}'
```

#### 8. 删除媒体广告位

```bash
etcdctl del /dsp/config/sspslot/delete/1001
```

---

## 📊 DSP 控制台输出示例

当后台推送数据后，DSP 控制台会打印如下日志：

### 添加预算广告位

```
========== 收到 etcd 事件 ==========
Key: /dsp/config/dsp/add/123
Type: dsp, Action: add, ID: 123

✅ 添加预算广告位: ID=123, Name=预算位A
========== 数据处理完成 ==========
```

### 更新预算广告位

```
========== 收到 etcd 事件 ==========
Key: /dsp/config/dsp/update/123
Type: dsp, Action: update, ID: 123

✅ 更新预算广告位: ID=123, Name=预算位A
========== 数据处理完成 ==========
```

### 删除预算广告位

```
========== 收到 etcd 事件 ==========
Key: /dsp/config/dsp/delete/123
Type: dsp, Action: delete, ID: 123

🗑️ 删除预算广告位: ID=123
========== 数据处理完成 ==========
```

### 添加投放绑定

```
========== 收到 etcd 事件 ==========
Key: /dsp/config/launch/add/50
Type: launch, Action: add, ID: 50

✅ 添加投放绑定: ID=50, SspSlotId=1001, DspSlotId=123
========== 数据处理完成 ==========
```

### 添加媒体广告位

```
========== 收到 etcd 事件 ==========
Key: /dsp/config/sspslot/add/1001
Type: sspslot, Action: add, ID: 1001

✅ 添加媒体广告位: SspSlotId=1001, OsType=1, SceneId=1001
========== 数据处理完成 ==========
```

### 删除媒体广告位

```
========== 收到 etcd 事件 ==========
Key: /dsp/config/sspslot/delete/1001
Type: sspslot, Action: delete, ID: 1001

🗑️ 删除媒体广告位: SspSlotId=1001
========== 数据处理完成 ==========
```

---

## 🔗 相关代码

### 核心实现文件

- **etcd 监听实现**：`impl/LoopSubscribeMessage.go:452`
- **事件处理器**：`impl/LoopSubscribeMessage.go:493`
- **Key 解析逻辑**：`impl/LoopSubscribeMessage.go:494`

### 数据结构定义

- **预算广告位**：`impl/DspSlotInfo.go`
- **预算公司**：`impl/DspCompany.go`
- **投放绑定**：`impl/DspLaunch.go`
- **媒体广告位**：`impl/SspSlotInfo.go`
- **综合管理器**：`impl/DspSlotManager.go`

### 常量定义

- **etcd Key 常量**：`constant/constant.go:8`
- **事件类型常量**：`constant/constant.go:26`

### 配置文件

- **etcd 地址和前缀配置**：`config.json`（`etcd` 和 `etcdPrefix` 字段）

---

## 🔗 相关资源

- **etcd 官方文档**：https://etcd.io/docs/
- **Go etcd 客户端**：https://github.com/etcd-io/etcd/tree/main/client/v3
- **DSP 技术支持**：联系 DSP 团队

---

## ❓ 常见问题

### Q1：etcd 连接失败怎么办？

**A**：检查以下几点：
1. etcd 服务是否正常运行
2. `ETCD_PREFIX` 配置是否正确
3. 网络是否可达

---

### Q2：推送后 DSP 没有收到数据？

**A**：检查以下几点：
1. Key 格式是否正确（必须有前缀）
2. JSON 格式是否正确（使用 snake_case）
3. 查看 DSP 日志，确认是否有错误信息
4. 使用 `etcdctl get` 命令检查数据是否写入成功

---

### Q3：如何验证数据是否同步成功？

**A**：
1. 查看 DSP 控制台日志，确认有打印数据处理信息
2. 查看 DSP 日志中的 `✅` 或 `🗑️` 标记
3. 发送测试请求，验证竞价逻辑是否正常

---

### Q4：支持批量操作吗？

**A**：
- 不支持批量操作
- 每次只能操作一条记录
- 如果需要批量修改，请循环调用 etcd 接口

---

### Q5：删除操作为什么不用 PUT？

**A**：
- 删除操作应该使用 etcd 的 `Delete` 方法
- 这会触发 `DELETE` 类型事件，DSP 会据此从内存中移除数据
- 使用 PUT 方法可能会导致数据残留

---

### Q6：DSP 启动时数据如何加载？

**A**：
1. DSP 启动时先从数据库加载全量数据（`AllDbData` 方法）
2. 然后构建内存映射关系（`ProcessingData` 方法）
3. 最后启动 etcd 监听器，监听后续变化（`DspSlotInfoWatchChan` 方法）

---

### Q7：媒体广告位（SspSlotInfo）和预算广告位（DspSlotInfo）有什么区别？

**A**：
- **媒体广告位**（SspSlotInfo）：媒体方提供的广告位信息，如尺寸、交互类型等
- **预算广告位**（DspSlotInfo）：预算方（需求方）的广告位配置，包含预算方的认证信息
- 两者通过 **投放绑定**（DspLaunch）关联起来，实现流量分发

---

### Q8：为什么 SspSlotInfo 的 Id 是 int 类型？

**A**：
- 这是代码实现的历史设计，与数据库表结构保持一致
- 在使用 etcd 同步时，需要注意这个类型差异
- 其他表的 ID 大多是 `int64` 类型

---

## 📞 技术支持

如有疑问，请联系 DSP 团队：

- **技术负责人**：XXX
- **邮箱**：xxx@example.com
- **企业微信**：XXX

---

**文档版本**：v2.1
**最后更新**：2026-02-26
**基于代码版本**：main 分支

# DSP Etcd 数据同步接口文档 V2

## 📋 文档说明

本文档描述后台系统如何通过 etcd 将配置数据实时同步给 DSP 引擎。

**适用人群**：后台开发工程师
**最后更新**：2026-03-04
**版本**：V2.0

---

## 🎯 概述

当后台修改以下配置时，需要通过 etcd 推送变化给 DSP 引擎：

1. **预算广告位**（DspSlotInfo）：预算方的广告位配置
2. **预算公司**（DspCompany）：预算方公司信息
3. **投放绑定**（DspLaunch）：媒体广告位与预算广告位的绑定关系
4. **媒体广告位**（SspSlotInfo）：媒体方的广告位配置

DSP 引擎会实时监听 etcd 变化，自动更新内存中的配置数据。

---

## 📌 etcd Key 规范

### 通用格式

```
{ETCD_PREFIX}/{data_type}/{action}/{id}
```

**参数说明**：
- `{ETCD_PREFIX}`：etcd 前缀（配置文件指定，默认 `/dsp/config`）
- `{data_type}`：数据类型，固定值 `dsp`、`company`、`launch`、`sspslot`
- `{action}`：操作类型，固定值 `add`、`update`、`delete`
- `{id}`：对应数据表的主键 ID

### 1. 预算广告位（DspSlotInfo）

```
格式：{ETCD_PREFIX}/dsp/{action}/{id}

示例：
- /dsp/config/dsp/add/123        添加预算广告位
- /dsp/config/dsp/update/123     更新预算广告位
- /dsp/config/dsp/delete/123     删除预算广告位
```

### 2. 预算公司（DspCompany）

```
格式：{ETCD_PREFIX}/company/{action}/{id}

示例：
- /dsp/config/company/add/10     添加预算公司
- /dsp/config/company/update/10  更新预算公司
- /dsp/config/company/delete/10  删除预算公司
```

### 3. 投放绑定（DspLaunch）

```
格式：{ETCD_PREFIX}/launch/{action}/{id}

示例：
- /dsp/config/launch/add/50      添加投放绑定
- /dsp/config/launch/update/50   更新投放绑定
- /dsp/config/launch/delete/50   删除投放绑定
```

### 4. 媒体广告位（SspSlotInfo）

```
格式：{ETCD_PREFIX}/sspslot/{action}/{id}

示例：
- /dsp/config/sspslot/add/100    添加媒体广告位
- /dsp/config/sspslot/update/100 更新媒体广告位
- /dsp/config/sspslot/delete/100 删除媒体广告位
```

---

## 📦 数据格式（JSON）

### ⚠️ 重要提示

**所有 JSON 字段必须使用 snake_case 命名，与 Go 结构体的 json tag 严格对应！**

### 1. 预算广告位（DspSlotInfo）

**Go 结构体定义**：
```go
type DspSlotInfo struct {
    Id              int64  `json:"id"`                 // 预算广告位id
    Name            string `json:"name"`               // 预算名称
    AdTypeId         int  `json:"ad_type_id"`           // 预算放广告类型
    AdSceneId       int    `json:"ad_scene_id"`        // 广告场景
    OsType          int    `json:"os_type"`            // 预算操作系统
    DspSlotCode     string `json:"dsp_slot_code"`      // 预算广告位
    DspAppKey       string `json:"dsp_app_key"`        // 预算方APPKEY
    DspAppId        string `json:"dsp_app_id"`         // 预算方AppId
    DspAppPkg       string `json:"dsp_app_pkg"`        // 预算方应用包名
    DspAppVer       string `json:"dsp_app_ver"`        // 应用版本号，支持多个，英文逗号分隔
    DspAppStoreVer  string `json:"dsp_app_store_ver"`  // 应用商店版本号，支持多个，英文逗号分隔
    PriceEncryptKey string `json:"price_encrypt_key"`  // 价格加密key
    DspAppStoreLink string `json:"dsp_app_store_link"` // 应用商店地址，支持多个，英文逗号分隔
    DspPayType      int `json:"dsp_pay_type"`       // 上游预算结算方式，1=分成，2=RTB
    DspDealRatio    int    `json:"dsp_deal_ratio"`     // 成交系数，0到100，单位%
    DspCompanyId    int64  `json:"dsp_company_id"`     // 公司Id
}
```

**etcd Value 示例**（添加/更新）：
```json
{
    "id": 123,
    "name": "android-分说值-500万",
    "ad_type_id": 1,
    "os_type": 1,
    "dsp_slot_code": "slot_001",
    "dsp_app_key": "app_key_xxx",
    "dsp_app_id": "app_id_xxx",
    "dsp_app_pkg": "com.example.app",
    "dsp_app_ver": "1.0.0,1.1.0",
    "dsp_app_store_ver": "1.0,2.0",
    "price_encrypt_key": "encrypt_key_xxx",
    "dsp_app_store_link": "https://example.com/app,https://example.com/app2",
    "dsp_pay_type": 2,
    "dsp_deal_ratio": 80,
    "dsp_company_id": 10
}
```

**字段说明**：

| 字段 | 类型     | 必填 | 说明 |
|------|--------|------|------|
| id | int64  | 是 | 预算广告位主键 ID |
| name | string | 是 | 预算名称 |
| scene_id | int64  | 是 | 广告类型（1=开屏, 2=信息流, 3=横幅等） |
| os_type | int    | 是 | 操作系统（1=Android, 2=iOS） |
| dsp_slot_code | string | 是 | 预算广告位代码 |
| dsp_app_key | string | 否 | 预算方 APP KEY |
| dsp_app_id | string | 否 | 预算方 App ID |
| dsp_app_pkg | string | 否 | 应用包名 |
| dsp_app_ver | string | 否 | 应用版本号，多个用逗号分隔 |
| dsp_app_store_ver | string | 否 | 应用商店版本号 |
| price_encrypt_key | string | 否 | 价格加密密钥 |
| dsp_app_store_link | string | 否 | 应用商店地址 |
| dsp_pay_type | int    | 是 | 结算方式（"1"=分成, "2"=RTB） |
| dsp_deal_ratio | int    | 否 | 成交系数 0-100 |
| dsp_company_id | int64  | 是 | 所属公司 ID |

**删除操作**：etcd Value 为空或只需发送 delete 事件

---

### 2. 预算公司（DspCompany）

**Go 结构体定义**：
```go
type DspCompany struct {
    Id      int64  `json:"id"`       // 预算公司Id
    Name    string `json:"name"`     // 公司名称
    DspCode int64  `json:"dsp_code"` // 自定义预算位Code
    Url     string `json:"url"`      // 预算请求url
    Method  int    `json:"method"`   // 请求方式 1=post 2=get
    Timeout int64  `json:"timeout"`  // 请求预算响应时间,毫秒时间戳
}
```

**etcd Value 示例**（添加/更新）：
```json
{
    "id": 1,
    "name": "未来计划",
    "dsp_code": 123,
    "url": "https://api.example.com/bid",
    "method": 1,
    "timeout": 500
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | int64 | 是 | 公司主键 ID |
| name | string | 是 | 公司名称 |
| dsp_code | int64 | 是 | DSP 代码（用于路由识别） |
| url | string | 是 | DSP 请求 URL |
| method | int | 是 | 请求方式（1=POST, 2=GET） |
| timeout | int64 | 是 | 超时时间（毫秒） |

**⚠️ 注意事项**：
- `dsp_code` 字段在 V2 中修复了 JSON tag，确保发送时字段名为 `dsp_code`
- 如果之前发送的 DspCode 无法解析，请检查后台是否使用了正确的字段名

---

### 3. 投放绑定（DspLaunch）

**Go 结构体定义**：
```go
type DspLaunch struct {
    Id              int64  `json:"id"`                // 权重表id
    SspSlotId       int64  `json:"ssp_slot_id"`       // 流量广告位Id
    DspSlotId       int64  `json:"dsp_slot_id"`       // 预算广告位id
    TrafficWeight   int    `json:"traffic_weight"`    // 权重 int
    LaunchStrategy  int    `json:"launch_strategy"`   // 投放策略
    FloorPrice      int    `json:"floor_price"`       // 底价
    IpLimit         int    `json:"ip_limit"`          // ip 限流次数
    TrackSchwarz    string `json:"track_schwarz"`      // 上报黑名单
    LogCaptureAt    int    `json:"log_capture_at"`    // 捕获日志时长
    LaunchTime      int    `json:"launch_time"`       // 投放时段
    CrowdDirection  int    `json:"crowd_direction"`   // 人群定向 1 不限制，2 定向，3排除
    RegionDirection int    `json:"region_direction"`  // 地域定向 1 不限制，2 定向，3排除
    BrandDirection  int    `json:"brand_direction"`   // 品牌定向 1 不限制，2 定向，3排除
}
```

**etcd Value 示例**（添加/更新）：
```json
{
    "id": 12,
    "ssp_slot_id": 1,
    "dsp_slot_id": 3,
    "traffic_weight": 100,
    "launch_strategy": 1,
    "floor_price": 50,
    "ip_limit": 100,
    "track_schwarz": "24h",
    "log_capture_at": 1,
    "request_count": 0,
    "show_count": 0,
    "click_count": 0,
    "launch_time": 0,
    "crowd_direction": 1,
    "region_direction": 1,
    "brand_direction": 1
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明                     |
|------|------|------|------------------------|
| id | int64 | 是 | 投放绑定主键 ID              |
| ssp_slot_id | int64 | 是 | 媒体广告位 ID               |
| dsp_slot_id | int64 | 是 | 预算广告位 ID               |
| traffic_weight | int | 是 | 流量权重（用于权重分配算法）         |
| launch_strategy | int | 否 | 投放策略                   |
| floor_price | int | 否 | 底价（单位：分）               |
| ip_limit | int | 否 | IP 限流次数                |
| track_schwarz | string | 否 | 上报黑名单                 |
| log_capture_at | int | 否 |  日志捕获时长                 |
| request_count | int | 否 | 请求次数统计                 |
| show_count | int | 否 | 展现次数统计                 |
| click_count | int | 否 | 点击次数统计                 |
| launch_time | int | 否 | 投放时段                   |
| crowd_direction | int | 否 | 人群定向（1=不限, 2=定向, 3=排除） |
| region_direction | int | 否 | 地域定向（1=不限, 2=定向, 3=排除） |
| brand_direction | int | 否 | 品牌定向（1=不限, 2=定向, 3=排除） |

---

### 4. 媒体广告位（SspSlotInfo）

**Go 结构体定义**：
```go
type SspSlotInfo struct {
    Id              int    `json:"id"`                // 给媒体分派的广告位id
    MediaId         int    `json:"media_id"`          // 媒体公司Id
    AppId           int    `json:"app_id"`            // app应用Id
    OsType          int    `json:"os_type"`           // 操作系统类型，1=Android，2=iOS
    AdTypeId         int    `json:"ad_type_id"`          // 媒体方广告类型
	AdSceneId       int     'json:"ad_scene_id"'         // 广告场景
    SspPayType      int    `json:"ssp_pay_type"`      // 下游媒体结算方式，1=分成，2=RTB
    SspDealRatio    int    `json:"ssp_deal_ratio"`    // 下游媒体分成系数，0到100，单位%
    Height          int    `json:"height"`            // 广告位高
    Width           int    `json:"width"`             // 广告位宽
    InteractionType int    `json:"interaction_type"`  // 交互类型
}
```

**etcd Value 示例**（添加/更新）：
```json
{
    "id": 1,
    "media_id": 100,
    "app_id": 1000,
    "os_type": 1,
    "ad_type_id": 1,
    "ssp_pay_type": 2,
    "ssp_deal_ratio": 70,
    "height": 1920,
    "width": 1080,
    "interaction_type": 1
}
```

**字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | int | 是 | 媒体广告位主键 ID |
| media_id | int | 否 | 媒体公司 ID |
| app_id | int | 否 | 应用 ID |
| os_type | int | 是 | 操作系统（1=Android, 2=iOS） |
| ad_type_id | int | 是 | 广告类型 |
| ssp_pay_type | int | 是 | 结算方式（1=分成, 2=RTB） |
| ssp_deal_ratio | int | 否 | 分成系数 0-100 |
| height | int | 是 | 广告位高度（像素） |
| width | int | 是 | 广告位宽度（像素） |
| interaction_type | int | 否 | 交互类型（1=网页, 2=deeplink, 3=下载等） |

---

## 🔧 后台集成指南

### 1. 添加/更新数据

```python
# Python 示例
import etcd3

etcd = etcd3.client(host='117.72.67.186', port=2379)

# 添加预算公司
key = '/dsp/config/company/add/1'
value = json.dumps({
    "id": 1,
    "name": "未来计划",
    "dsp_code": 123,
    "url": "https://api.example.com/bid",
    "method": 1,
    "timeout": 500
})
etcd.put(key, value)

# 更新预算公司（DspCode 改为 456）
key = '/dsp/config/company/update/1'
value = json.dumps({
    "id": 1,
    "name": "未来计划",
    "dsp_code": 456,  # 注意：字段名必须是 dsp_code
    "url": "https://api.example.com/bid",
    "method": 1,
    "timeout": 500
})
etcd.put(key, value)
```

### 2. 删除数据

```python
# 删除预算公司
key = '/dsp/config/company/delete/1'
etcd.delete(key)  # Value 可以为空
```

### 3. Java 示例

```java
// Maven 依赖
// <dependency>
//   <groupId>io.etcd</groupId>
//   <artifactId>jetcd-core</artifactId>
//   <version>0.7.5</version>
// </dependency>

import io.etcd.jetcd.Client;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.ByteSequence;

Client client = Client.builder()
    .endpoints("http://117.72.67.186:2379")
    .build();

// 添加/更新预算公司
String key = "/dsp/config/company/update/1";
String value = "{\"id\":1,\"name\":\"未来计划\",\"dsp_code\":123,\"url\":\"https://api.example.com/bid\",\"method\":1,\"timeout\":500}";

ByteSequence keyByteSeq = ByteSequence.from(key, StandardCharsets.UTF_8);
ByteSequence valueByteSeq = ByteSequence.from(value, StandardCharsets.UTF_8);

client.getKVClient().put(keyByteSeq, valueByteSeq).get();
```

---

## 🐛 常见问题排查

### 问题 1：DspCode 字段更新后为 0

**现象**：后台更新 DspCode 为 123，但 DSP 引擎显示为 0

**原因**：
- Go 结构体 `DspCompany` 之前缺少 JSON tag `json:"dsp_code"`
- etcd 发送的 JSON 字段名与 Go 结构体不匹配

**解决方案**：
1. 确保后台发送的 JSON 字段名是 `dsp_code`（snake_case）
2. 确保 DSP 引擎已更新到 V2 版本（包含 JSON tag）
3. 检查日志中的 "📦 原始 JSON 数据" 确认数据格式

**验证方法**：
```bash
# 查看日志，确认原始 JSON
grep "原始 JSON 数据" logs/xxx.log

# 应该看到：
# 📦 原始 JSON 数据: {"id":1,"name":"未来计划","dsp_code":123,...}
```

---

### 问题 2：字段类型不匹配

**现象**：JSON 解析失败

**原因**：字段类型不一致

**检查项**：
| 字段 | 正确类型 | 错误类型 |
|------|---------|---------|
| id | int64 | "123"（字符串） |
| dsp_code | int64 | "123"（字符串） |
| traffic_weight | int | "100"（字符串） |

**解决方案**：
确保 JSON 中的数值类型不带引号

---

### 问题 3：Key 格式错误

**错误格式**：
```
/dsp/config/dsp/123           ❌ 缺少 action
/dsp/config/dsp/add/           ❌ 缺少 id
/dsp/config/company/1/add      ❌ 顺序错误
```

**正确格式**：
```
/dsp/config/dsp/add/123        ✅
/dsp/config/company/update/1   ✅
```

---

## 📊 DSP 引擎响应日志

### 成功更新日志示例

```
13:41:37:========== 收到 etcd 事件 ==========
13:41:37:Key: /dsp/config/company/update/1
13:41:37:Type: company, Action: update, ID: 1
13:41:37:📦 原始 JSON 数据: {"id":1,"name":"未来计划","dsp_code":123,"url":"https://api.example.com/bid","method":1,"timeout":500}
13:41:37:✅ 添加/更新预算公司: ID=1, Name=未来计划
13:41:37:✅ update 预算公司: ID=1, Name=未来计划, DspCode=123
13:41:37:========== 数据处理完成 ==========
13:41:37:========== 开始打印内存 Map 数据 ==========
13:41:37:========== 内存 DspCompany 数据 (共 3 条) ==========
13:41:37:DspCompany - Id: 1, Name: 未来计划, DspCode: 123, Url: https://api.example.com/bid
```

### 错误日志示例

```
13:41:37:========== 收到 etcd 事件 ==========
13:41:37:Key: /dsp/config/company/update/1
13:41:37:Type: company, Action: update, ID: 1
13:41:37:📦 原始 JSON 数据: {"id":1,"name":"未来计划","DspCode":123}  ❌ 错误：大写驼峰
13:41:37:解析 DspCompany JSON 失败: json: unknown field "DspCode", 原始数据: {"id":1,"name":"未来计划","DspCode":123}
```

---

## 📝 数据流转流程

```
┌─────────────┐     ┌─────────┐     ┌─────────────┐     ┌─────────────┐
│   后台系统   │ ──→ │  etcd   │ ──→ │  DSP引擎    │ ──→ │   内存Map   │
│ (数据变更)   │     │ (存储/  │     │ (Watch监听) │     │ (实时更新)   │
│             │     │  通知)  │     │             │     │             │
└─────────────┘     └─────────┘     └─────────────┘     └─────────────┘
                            │
                            └──→ 日志记录 (原始JSON + 解析结果)
```

**流程说明**：
1. 后台系统修改配置数据
2. 后台将变更数据按指定格式写入 etcd
3. DSP 引擎通过 Watch 机制实时监听 etcd 变化
4. 收到事件后，解析 JSON 并更新内存 Map
5. 打印日志记录更新过程和结果

---

## 🔄 版本更新记录

### V2.0 (2026-03-04)

**修复**：
- 修复 `DspCompany` 结构体缺少 JSON tag 的问题
- 添加 `json:"id"`、`json:"name"`、`json:"dsp_code"` 等 tags
- 添加调试日志，打印原始 JSON 数据

**影响**：
- 后台必须使用 snake_case 字段名（如 `dsp_code`）
- DspCode 字段现在可以正确更新

### V1.0 (2026-02-18)

- 初始版本
- 定义基础数据结构和 etcd Key 规范

---

## 📞 技术支持

如有问题，请查看日志文件：
- DSP 引擎日志：`logs/business/*.log`
- 错误日志：`logs/error/*.log`

**关键日志标识**：
- `📦 原始 JSON 数据` - 查看从 etcd 接收的原始数据
- `✅ 添加/更新` - 操作成功
- `🗑️ 删除` - 删除操作
- `解析失败` - JSON 格式错误

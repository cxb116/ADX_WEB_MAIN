# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 项目概述

DSP-ADMIN 是基于若依(RuoYi)框架构建的全栈管理系统，后端采用 Java Spring Boot，前端采用 Vue 3。项目采用 monorepo 结构，后端各模块与前端应用清晰分离。

## 技术栈

**后端：**
- Spring Boot 2.5.15 + Java 8
- MyBatis 作为数据库 ORM 框架
- MySQL 数据库 + Druid 连接池
- Redis 缓存和会话管理
- Spring Security + JWT 认证
- Swagger 3.0 API 文档
- Quartz 定时任务管理

**前端：**
- Vue 3.5.26 (Composition API)
- Element Plus 2.13.1 UI 组件库
- Vite 6.4.1 构建工具
- Pinia 3.0.4 状态管理
- Vue Router 4.6.4 路由管理
- Axios 1.13.2 HTTP 请求

## 常用开发命令

### 后端 (Java/Maven)

**构建整个项目：**
```bash
mvn clean install
```

**运行后端应用：**
```bash
# 在根目录执行
mvn spring-boot:run -pl ruoyi-admin

# 或直接运行 JAR 包
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

**打包部署：**
```bash
mvn clean package
```

后端默认运行在 8080 端口。主类为 `com.ruoyi.RuoYiApplication`。

### 前端 (Vue 3/pnpm)

**安装依赖：**
```bash
cd RuoYi-Vue3
pnpm install
```

**运行开发服务器：**
```bash
pnpm dev
```

前端开发服务器运行在 80 端口，通过 `/dev-api` 前缀代理 API 请求到 `http://localhost:8080`。

**生产环境构建：**
```bash
pnpm build:prod
```

**预发布环境构建：**
```bash
pnpm build:stage
```

**预览生产构建：**
```bash
pnpm preview
```

### 开发环境配置

VS Code 工作空间 (`dsp-admin.code-workspace`) 包含启动配置：
- **Backend**: 通过 Java 调试器启动 Spring Boot 应用
- **Frontend**: 使用 pnpm 启动 Vite 开发服务器
- **Both (Backend & Frontend)**: 同时启动前后端

## 架构设计

### 后端模块结构

后端采用**分层架构**，按 Maven 多模块组织：

```
ruoyi-admin/          # Web 层 - 控制器和应用入口
ruoyi-framework/      # 核心框架 - 安全、配置、AOP、Web 基础设施
ruoyi-system/         # 业务逻辑 - 系统管理（用户、角色、部门、菜单）
ruoyi-common/         # 共享工具类 - 通用辅助类、注解、常量
ruoyi-quartz/         # 定时任务 - Quartz 任务管理
ruoyi-generator/      # 代码生成 - 数据库到代码的生成工具
```

**标准请求流程：**
1. `ruoyi-admin` 接收 HTTP 请求
2. Controller 委托给 `ruoyi-system` 或其他模块的 Service 层
3. Service 通过 MyBatis Mapper 进行数据库操作
4. `ruoyi-framework` 提供横切关注点（安全、日志、缓存）

**分层约定：**
- **Controller** (`controller/`): REST 端点，请求/响应处理
- **Service** (`service/`): 业务逻辑，事务边界
- **Mapper** (`mapper/`): MyBatis 数据访问层
- **Domain** (`domain/`): 实体类及注解

### 前端结构

前端组织为 **Vue 3 SPA**：
- `src/api/`: 后端通信的 Axios 服务方法
- `src/views/`: 按模块组织的页面级组件
- `src/components/`: 可复用组件
- `src/store/`: Pinia 全局状态存储
- `src/router/`: Vue Router 路由配置
- `src/utils/`: 工具函数和辅助方法

**路径别名：**
- `@/`: 指向 `src/`
- `~/`: 指向项目根目录

### 代码生成器

`ruoyi-generator` 模块提供全栈代码生成功能：

**工作流程：**
1. 通过 `/tool/gen/importTable` 从数据库导入表
2. 配置表选项（包名、模块名、模板类型）
3. 通过 `/tool/gen/preview/{tableId}` 预览生成的代码
4. 下载 ZIP 或直接生成到文件系统

**模板类型：**
- **CRUD**: 标准增删改查操作
- **TREE**: 树形结构的层次数据
- **SUB**: 主从表关系

**生成内容：**
- 后端: Domain、Mapper、Service、Controller 层
- 前端: Vue 组件（Element Plus UI）和 API 服务
- 数据库: 菜单和权限 SQL 脚本

模板位于 `ruoyi-generator/src/main/resources/vm/`，使用 Velocity 模板引擎。

## 核心模式和约定

### 后端

- **安全**: 所有 Controller 受 Spring Security 保护。使用 `@PreAuthorize` 进行权限检查。
- **日志**: 使用 `@Log` 注解配合 `@GetMapping`/`@PostMapping` 自动记录操作日志。
- **校验**: 在 Domain 类上使用 JSR-303 注解进行参数校验。
- **API 文档**: Swagger 注解（`@Api`、`@ApiOperation`）自动发现。
- **分页**: 使用 `PageHelper.startPage()` 进行查询分页。
- **缓存**: 对频繁访问的数据使用 `@Cacheable`/`@CacheEvict` 配合 Redis。

### 前端

- **API 调用**: 所有 API 调用通过 `/dev-api` 代理到后端
- **认证**: JWT token 存储在 cookies 中，每次请求自动携带
- **权限指令**: 使用 `v-hasPermi` 指令控制 UI 元素级权限
- **表单校验**: Element Plus 表单校验支持异步规则
- **状态管理**: 使用 Pinia stores 管理共享状态（用户信息、权限、设置）

### 数据库

- **命名约定**: 数据库列使用 snake_case，Java 字段使用 camelCase
- **审计字段**: 大部分表包含 `create_by`、`create_time`、`update_by`、`update_time`
- **软删除**: 使用 `del_flag` 进行逻辑删除（0=正常，2=删除）
- **菜单系统**: 菜单权限存储在 `sys_menu` 表中，采用层次结构

## 配置文件

### 后端配置文件

位于 `ruoyi-admin/src/main/resources/`：
- `application.yml`: 主配置（环境配置、服务器、监控）
- `application-druid.yml`: 数据库连接池设置
- `logback.xml`: 日志配置

### 前端配置

- `vite.config.js`: Vite 构建配置和代理设置
- `.env.development`: 开发环境变量
- `.env.production`: 生产环境变量
- `.env.staging`: 预发布环境变量

## Git 提交规范

本项目遵循 **Google git commit log** 风格进行代码提交，**必须使用中文编写提交信息**。

### 提交消息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type（类型）

必须使用以下类型之一：

- **feat**: 新功能
- **fix**: 修复 bug
- **docs**: 文档变更
- **style**: 代码格式调整（不影响代码运行的变动）
- **refactor**: 重构（既不是新增功能，也不是修复 bug）
- **perf**: 性能优化
- **test**: 增加测试
- **chore**: 构建过程或辅助工具的变动
- **revert**: 回滚之前的提交

### Scope（范围）

用于指定提交影响的模块，例如：
- `backend`: 后端相关
- `frontend`: 前端相关
- `database`: 数据库相关
- `api`: API 接口相关
- 或具体模块名，如 `budget`、`user`、`product`、`company` 等

### Subject（主题）

- **必须使用中文**
- 使用动词开头（如"添加"、"修复"、"优化"、"移除"、"更新"等）
- 简洁明了，不超过 50 个字符
- 不以句号结尾
- 清晰描述做了什么

### Body（正文）

- **必须使用中文**
- 详细说明本次提交的内容和原因
- 可以分多条说明，每条一行

### 示例

```bash
# 新增功能
git commit -m "feat(budget): 添加预算管理模块"

# 修复 bug
git commit -m "fix(backend): 修复用户登录时 token 验证失败的问题"

# 文档更新
git commit -m "docs: 更新 CLAUDE.md 添加 Git 提交规范"

# 重构代码
git commit -m "refactor(frontend): 优化产品列表页面的组件结构"

# 性能优化
git commit -m "perf(database): 为查询频繁的字段添加索引"

# 移除功能
git commit -m "remove(frontend): 移除产品管理列表中的备注字段展示"

# 完整示例（包含 body）
git commit -m "feat(budget): 实现公司管理模块

新增公司信息的增删改查功能，包括：
- 公司名称、预算映射值配置
- 请求地址和方法设置
- 超时时间配置

关联产品管理，支持下拉选择公司"
```

### 注意事项

- **提交信息必须使用中文编写**
- 每次提交只做一件事，保持提交的原子性
- 提交消息应该清晰地描述"做了什么"和"为什么"
- 避免使用如 "update files"、"fix bug"、"修改代码" 等模糊的描述
- Subject 保持简短，详细内容放在 Body 中
- 使用专业的中文技术术语

## 重要说明

- 项目前端使用 **pnpm**（非 npm 或 yarn）管理依赖
- 后端使用根目录的 **Maven** 构建工具
- 所有数据库迁移应同时包含 schema 变更和菜单权限
- 添加新功能时，使用代码生成器保持结构一致
- 系统支持基于 **profile 的配置**（dev/test/prod），通过 Spring profiles 切换
- 前端 API 代理将 `/dev-api/*` 映射到后端 `http://localhost:8080`
- 后端启动后，Swagger UI 可通过 `http://localhost:8080/swagger-ui.html` 访问
- **所有代码提交必须使用中文编写提交信息，并遵循 Google git commit log 风格规范**

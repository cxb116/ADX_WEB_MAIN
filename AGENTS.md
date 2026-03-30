# AGENTS.md - 提交规范指南

本文档定义了 DSP-ADMIN 项目的 Git 提交规范，基于 [约定式提交（Conventional Commits）](https://www.conventionalcommits.org/zh-hans/v1.0.0/) 规范。

## 目录

- [提交格式](#提交格式)
- [类型（Type）](#类型type)
- [范围（Scope）](#范围scope)
- [提交规则](#提交规则)
- [仅提交暂存区文件](#仅提交暂存区文件)
- [提交示例](#提交示例)
- [常见问题](#常见问题)

---

## 提交格式

```
<类型>(<范围>): <描述>

[可选 正文]

[可选 脚注]
```

### 结构说明

| 部分 | 必需 | 说明 |
|------|------|------|
| 类型（Type） | ✅ | 提交的类型，如 feat、fix |
| 范围（Scope） | ❌ | 影响的模块或范围 |
| 描述（Description） | ✅ | 简短的变更描述 |
| 正文（Body） | ❌ | 详细的变更说明 |
| 脚注（Footer） | ❌ | 破坏性变更、关联 issue 等 |

### 格式要求

- **类型**和**范围**使用小写字母
- **描述**使用中文，以动词开头（添加、修复、优化、移除等）
- **正文**与描述之间必须有一个空行
- **脚注**与正文之间必须有一个空行
- 每行长度不超过 100 个字符

---

## 类型（Type）

必须使用以下类型之一：

| 类型 | 说明 | 版本语义 |
|------|------|----------|
| **feat** | 新功能、新特性 | MINOR |
| **fix** | Bug 修复 | PATCH |
| **docs** | 文档变更（仅修改文档） | - |
| **style** | 代码格式调整（不影响运行） | - |
| **refactor** | 重构（既非新功能也非修复） | - |
| **perf** | 性能优化 | PATCH |
| **test** | 测试相关 | - |
| **chore** | 构建/工具/辅助变动 | - |
| **revert** | 回滚之前的提交 | - |

### 类型详解

- **feat**: 添加新功能、接口、模块等
  - 示例：`feat(user): 添加用户登录接口`

- **fix**: 修复 bug、错误、异常
  - 示例：`fix(auth): 修复 Token 过期后无法刷新的问题`

- **docs**: 文档变更
  - 示例：`docs: 更新 API 文档`

- **style**: 代码格式调整
  - 示例：`style: 统一代码缩进为 4 空格`

- **refactor**: 代码重构
  - 示例：`refactor(user): 优化用户查询逻辑`

- **perf**: 性能优化
  - 示例：`perf(database): 添加索引提升查询效率`

- **test**: 测试相关
  - 示例：`test: 添加用户注册单元测试`

- **chore**: 其他变动
  - 示例：`chore: 升级 Spring Boot 版本`

- **revert**: 回滚
  - 示例：`revert: 回滚 "feat(user): 添加用户注册功能"`

---

## 范围（Scope）

范围用于指定提交影响的模块，应使用小写名词。

### 推荐范围

| 范围 | 说明 |
|------|------|
| **backend** | 后端相关 |
| **frontend** | 前端相关 |
| **database** | 数据库相关 |
| **api** | API 接口相关 |
| **auth** | 认证/授权相关 |
| **user** | 用户模块 |
| **budget** | 预算模块 |
| **product** | 产品模块 |
| **company** | 公司模块 |
| **ad** | 广告模块 |
| **data** | 数据模块 |

### 范围使用示例

```
feat(backend): 添加用户管理模块
fix(frontend): 修复列表页分页问题
refactor(api): 优化 REST 接口响应格式
```

---

## 提交规则

### 强制规则

1. **必须**使用有效的类型前缀（feat、fix 等）
2. **必须**提供简短的描述（描述必须以中文动词开头）
3. 描述**必须**简洁明了，不超过 50 个字符
4. 描述末尾**不能**使用句号
5. 正文和脚注**必须**使用中文

### 推荐规则

1. 提交应该保持**原子性**（一次提交只做一件事）
2. 破坏性变更**应该**在脚注中标注 `BREAKING CHANGE:`
3. 关联 Issue **可以**在脚注中标注 `Closes #123` 或 `Refs #456`
4. 使用否定句式描述问题时，**不应**使用双重否定

### 语义化版本对应

| 提交类型 | 版本升级 |
|----------|----------|
| feat | MINOR (+1.0.0) |
| fix | PATCH (+0.1.0) |
| feat + BREAKING CHANGE | MAJOR (+1.0.0) |
| 任何类型 + BREAKING CHANGE | MAJOR (+1.0.0) |

---

## 仅提交暂存区文件

### 核心原则

**只提交已暂存的文件，未加入暂存区的一律不提交。**

### 操作流程

### 1. 查看文件状态

```bash
git status
```

输出示例：
```
On branch main
Changes to be committed:
  (staged)  modified:   src/user/UserService.java    # 已暂存 ✓
  (staged)  modified:   src/user/UserController.java # 已暂存 ✓
Changes not staged for commit:
  (unstaged) modified:   src/product/Product.java    # 未暂存 ✗
  (unstaged) new file:   temp/debug.txt               # 未暂存 ✗
```

### 2. 暂存文件（git add）

只暂存需要提交的文件：

```bash
# 暂存单个文件
git add src/user/UserService.java

# 暂存多个文件
git add src/user/UserService.java src/user/UserController.java

# 暂存整个目录
git add src/user/

# 暂存所有已跟踪文件的修改
git add -u
```

**重要**：不要使用 `git add .` 或 `git add -A`，这会暂存所有文件，包括不需要提交的文件。

### 3. 提交（git commit）

只提交已暂存的文件：

```bash
# 提交暂存区的文件
git commit -m "fix(user): 修复用户查询空指针异常"

# 提交并添加正文
git commit -m "fix(user): 修复用户查询空指针异常

- 添加空值判断逻辑
- 优化错误提示信息"

# 提交并添加脚注
git commit -m "fix(auth): 修复 Token 验证失败问题

BREAKING CHANGE: 认证接口返回值格式变更"
```

### 4. 验证提交

```bash
# 查看最后一次提交的内容
git show --stat

# 查看暂存区状态
git status
```

---

## 提交示例

### 正确示例

```
feat(user): 添加用户注册功能

新增用户注册接口，支持手机号和邮箱注册：
- 添加注册请求参数校验
- 添加密码加密存储
- 添加注册成功后自动登录

Closes #123
```

```
fix(auth): 修复 Token 刷新接口超时问题

在 Token 刷新时添加重试机制，避免网络波动导致认证失败。

Refs #456
```

```
refactor(backend): 优化数据库查询性能

- 将 N+1 查询改为联表查询
- 添加查询缓存
- 优化索引结构
```

```
docs: 更新 README 文件

添加项目部署说明和 API 文档链接。
```

```
chore: 升级 Maven 依赖版本

升级 Spring Boot 从 2.5.14 到 2.5.15
```

### 错误示例

| 错误类型 | 示例 | 修正 |
|----------|------|------|
| 缺少类型 | `user: 添加功能` | `feat(user): 添加功能` |
| 类型错误 | `add: 添加功能` | `feat: 添加功能` |
| 描述过长 | `fix: 修复了用户登录时出现的无法登录的 bug 问题` | `fix(auth): 修复用户登录失败问题` |
| 使用句号 | `fix: 修复了 bug.` | `fix: 修复 bug` |
| 英文描述 | `fix: fix login bug` | `fix: 修复登录问题` |
| 提交未暂存文件 | `git commit -a` | `git add <file> && git commit -m "..."` |

---

## 常见问题

### Q: 如何处理包含多种类型的提交？

尽量拆分提交，保持原子性。如果确实需要合并，使用主要类型。

```
# 不好：混合类型
git commit -m "feat(user): 添加用户功能 and fix: 修复 bug"

# 好：拆分为两次提交
git commit -m "feat(user): 添加用户注册功能"
git commit -m "fix(user): 修复注册时密码加密失败问题"
```

### Q: 提交信息写错了怎么办？

```bash
# 修改最后一次提交（未推送）
git commit --amend -m "fix(user): 修复用户查询问题"

# 修改历史提交（谨慎使用）
git rebase -i HEAD~3
```

### Q: 如何关联 Issue？

在脚注中使用 `Closes`、`Fixes`、`Refs`：

```
feat(user): 添加用户管理模块

Closes #123
Closes #456, #789
Refs #111
```

### Q: 如何标注破坏性变更？

两种方式：

1. 在脚注中标注（推荐）：
```
feat(api): 修改用户信息接口

BREAKING CHANGE: 请求参数从 userId 改为 id
```

2. 在类型后加 `!`：
```
feat(api)!: 修改用户信息接口

请求参数从 userId 改为 id
```

---

## 快速参考

### 提交命令速查

```bash
# 1. 查看状态
git status

# 2. 暂存文件（只暂存需要的）
git add <文件路径>

# 3. 提交（只提交暂存区的）
git commit -m "<类型>(<范围>): <描述>"

# 4. 推送git push

```

### 类型选择指南

```
我需要做...
├── 添加新功能          → feat
├── 修复 bug           → fix  
├── 修改文档           → docs
├── 调整代码格式        → style
├── 重构代码            → refactor
├── 优化性能            → perf
├── 添加/修改测试       → test
├── 构建/工具/其他      → chore
└── 回滚提交           → revert
```

---

## 参考资料

- [约定式提交规范 v1.0.0](https://www.conventionalcommits.org/zh-hans/v1.0.0/)
- [语义化版本 2.0.0](https://semver.org/lang/zh-CN/)
- [Git 提交信息规范](https://github.com/angular/angular/blob/22b96b9/CONTRIBUTING.md#-commit-message-guidelines)

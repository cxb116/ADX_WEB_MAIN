-- 插入媒体数据报表分成模式数据计算定时任务
-- 执行此SQL后，需要重启应用才能生效
-- 注意：cron_expression中的'?'表示每年的该月份执行，请根据需要修改为具体的年月（如202604）

-- 删除已存在的定时任务（如果有）
DELETE FROM sys_job WHERE invoke_target = 'dataSspSlotCalculateTask.calculateCurrentMonth';

-- 插入新的定时任务
INSERT INTO `sys_job` (
  `job_name`,
  `job_group`,
  `invoke_target`,
  `cron_expression`,
  `misfire_policy`,
  `concurrent`,
  `status`,
  `create_by`,
  `create_time`,
  `remark`
) VALUES (
  '媒体数据报表分成模式计算',
  'DEFAULT',
  'dataSspSlotCalculateTask.calculateCurrentMonth',
  '0 */1 * * * ?',  -- ?表示每年的该月份，如需指定具体年月请替换（如202604）
  '1',  -- 1表示立即执行一次错过的任务
  '0',  -- 0表示禁止并发执行
  '0',  -- 0表示正常状态
  'admin',
  NOW(),
  '每隔1分钟计算一次当前月分成模式的预算流水和利润收入（Cron中的?表示年占位符）'
);

-- 如果想指定具体的年月，请使用下面的更新语句（例如2026年4月）
-- UPDATE sys_job SET cron_expression = '0 */1 * * * 202604' WHERE invoke_target = 'dataSspSlotCalculateTask.calculateCurrentMonth';

-- 查询插入的定时任务
SELECT
  job_id AS '任务ID',
  job_name AS '任务名称',
  invoke_target AS '调用目标',
  cron_expression AS 'Cron表达式',
  status AS '状态(0=正常,1=暂停)',
  concurrent AS '并发(0=禁止,1=允许)',
  create_time AS '创建时间',
  remark AS '备注'
FROM sys_job
WHERE invoke_target = 'dataSspSlotCalculateTask.calculateCurrentMonth';

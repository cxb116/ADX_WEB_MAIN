-- ============================================
-- 修改数据表字段名：meida_id -> media_id
-- ============================================

-- 1. 修改天表（data_ssp_slot_day_YYYYMM）
-- 请根据实际存在的表执行，以下是示例

-- 2026年3月天表
ALTER TABLE data_ssp_slot_day_202603 CHANGE COLUMN media_id media_id BIGINT COMMENT '媒体用户Id';

-- 2026年4月天表（如果存在）
-- ALTER TABLE data_ssp_slot_day_202604 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- 2026年5月天表（如果存在）
-- ALTER TABLE data_ssp_slot_day_202605 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- 2026年6月天表（如果存在）
-- ALTER TABLE data_ssp_slot_day_202606 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- ... 根据实际情况添加其他月份的天表


-- 2. 修改小时表（data_ssp_slot_hour_YYYYMM）
-- 请根据实际存在的表执行，以下是示例

-- 2026年3月小时表
ALTER TABLE data_ssp_slot_hour_202603 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- 2026年4月小时表（如果存在）
-- ALTER TABLE data_ssp_slot_hour_202604 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- 2026年5月小时表（如果存在）
-- ALTER TABLE data_ssp_slot_hour_202605 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- 2026年6月小时表（如果存在）
-- ALTER TABLE data_ssp_slot_hour_202606 CHANGE COLUMN meida_id media_id BIGINT COMMENT '媒体用户Id';

-- ... 根据实际情况添加其他月份的小时表


-- ============================================
-- 查询所有需要修改的表（可选，用于查找所有相关表）
-- ============================================

-- 查询所有包含 meida_id 字段的表
SELECT
    TABLE_NAME,
    COLUMN_NAME,
    COLUMN_TYPE,
    COLUMN_COMMENT
FROM
    INFORMATION_SCHEMA.COLUMNS
WHERE
    TABLE_SCHEMA = 'ruoyi'  -- 请替换为你的数据库名
    AND COLUMN_NAME = 'meida_id'
    AND TABLE_NAME LIKE 'data_ssp_slot_%'
ORDER BY
    TABLE_NAME;


-- ============================================
-- 批量生成修改语句（可选）
-- ============================================

-- 使用以下查询自动生成所有需要执行的 ALTER 语句
SELECT CONCAT(
    'ALTER TABLE ',
    TABLE_NAME,
    ' CHANGE COLUMN meida_id media_id BIGINT COMMENT ''媒体用户Id'';'
) AS alter_statement
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = 'ruoyi'  -- 请替换为你的数据库名
    AND TABLE_NAME LIKE 'data_ssp_slot_%'
    AND TABLE_NAME NOT IN (
        SELECT TABLE_NAME
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = 'ruoyi'
        AND COLUMN_NAME = 'media_id'
        AND TABLE_NAME LIKE 'data_ssp_slot_%'
    );

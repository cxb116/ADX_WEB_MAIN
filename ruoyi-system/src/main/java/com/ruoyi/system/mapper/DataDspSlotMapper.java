package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.DataDspSlot;
import org.apache.ibatis.annotations.Param;

/**
 * 预算报表Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-01
 */
public interface DataDspSlotMapper
{
    /**
     * 查询预算报表
     *
     * @param dataDspSlot 包含 id 和动态表名 tableName
     * @return 预算报表
     */
    public DataDspSlot selectDataDspSlotById(DataDspSlot dataDspSlot);

    /**
     * 查询预算报表列表
     *
     * @param dataDspSlot 预算报表
     * @return 预算报表集合
     */
    public List<DataDspSlot> selectDataDspSlotList(DataDspSlot dataDspSlot);

    /**
     * 修改预算报表
     *
     * @param dataDspSlot 预算报表
     * @return 结果
     */
    public int updateDataDspSlot(DataDspSlot dataDspSlot);

    /**
     * 查询当月汇总统计数据
     *
     * @param tableName 动态表名（格式：data_dsp_slot_day_YYYYMM）
     * @return 汇总统计数据
     */
    public Map<String, Object> selectMonthlySummary(String tableName);

    /**
     * 查询日期范围内的趋势数据（跨表查询）
     *
     * @param tableNames 表名列表
     * @param startDate 开始日期（格式：yyyyMMdd）
     * @param endDate 结束日期（格式：yyyyMMdd）
     * @return 按日期分组的数据列表
     */
    public List<Map<String, Object>> selectTrendData(@Param("tableNames") List<String> tableNames, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询指定天数的收益数据（跨表查询）
     *
     * @param tableNames 表名列表
     * @param startDate 开始日期（格式：yyyyMMdd）
     * @param endDate 结束日期（格式：yyyyMMdd）
     * @return 按日期分组的收益数据列表
     */
    public List<Map<String, Object>> selectRevenueData(@Param("tableNames") List<String> tableNames, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询今日每小时填充率数据
     *
     * @param tableName 动态表名（格式：data_dsp_slot_day_YYYYMM）
     * @return 按小时分组的填充率数据列表
     */
    public List<Map<String, Object>> selectTodayFillRate(@Param("tableName") String tableName);

    /**
     * 查询近7天填充率数据（跨表查询）
     *
     * @param tableNames 表名列表
     * @param startDate 开始日期（格式：yyyyMMdd）
     * @param endDate 结束日期（格式：yyyyMMdd）
     * @return 按日期分组的填充率数据列表
     */
    public List<Map<String, Object>> selectFillRateData(@Param("tableNames") List<String> tableNames, @Param("startDate") String startDate, @Param("endDate") String endDate);
}

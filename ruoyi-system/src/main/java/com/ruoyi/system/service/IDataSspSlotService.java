package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DataSspSlot;

/**
 * 媒体数据报表Service接口
 *
 * @author ruoyi
 * @date 2026-03-01
 */
public interface IDataSspSlotService
{
    /**
     * 查询媒体数据报表
     *
     * @param dataSspSlot 包含 id 和动态表名 tableName
     * @return 媒体数据报表
     */
    public DataSspSlot selectDataSspSlotById(DataSspSlot dataSspSlot);

    /**
     * 查询媒体数据报表列表
     *
     * @param dataSspSlot 媒体数据报表
     * @return 媒体数据报表集合
     */
    public List<DataSspSlot> selectDataSspSlotList(DataSspSlot dataSspSlot);



    public List<DataSspSlot> selectDataSspSlotListToDspData(DataSspSlot dataSspSlot);

    /**
     * 修改媒体数据报表
     *
     * @param dataSspSlot 媒体数据报表
     * @return 结果
     */
    public int updateDataSspSlot(DataSspSlot dataSspSlot);

    /**
     * 计算当前月分成模式的预算流水和利润收入（定时任务使用）
     *
     * @return 计算的记录数
     */
    public int calculateCurrentMonthData();

    /**
     * 定时任务专用：批量更新分成模式的预算流水和收入
     *
     * @param tableName 表名
     * @param updateList 更新数据列表
     * @return 更新的记录数
     */
    public int batchUpdateForSchedule(String tableName, java.util.List<java.util.Map<String, Object>> updateList);
}

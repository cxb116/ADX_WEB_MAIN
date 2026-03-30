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
}

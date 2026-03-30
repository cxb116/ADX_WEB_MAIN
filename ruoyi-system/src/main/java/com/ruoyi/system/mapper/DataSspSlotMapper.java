package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DataSspSlot;
import org.apache.ibatis.annotations.Param;

/**
 * 媒体数据报表Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-01
 */
public interface DataSspSlotMapper
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

    /**
     * 根据 dsp_slot_id、dsp_slot_code 和 date 批量更新 spend
     *
     * @param tableName 表名
     * @param dspSlotId 预算广告位ID
     * @param dspSlotCode 预算广告位编码
     * @param date 日期
     * @param spend 成本（分）
     * @return 结果
     */
    public int updateSpendByDspSlotInfo(@Param("tableName") String tableName,
                                        @Param("dspSlotId") Long dspSlotId,
                                        @Param("dspSlotCode") String dspSlotCode,
                                        @Param("date") Long date,
                                        @Param("spend") Long spend);
}

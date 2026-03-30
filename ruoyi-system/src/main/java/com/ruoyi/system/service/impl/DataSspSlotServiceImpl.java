package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DataSspSlotMapper;
import com.ruoyi.system.domain.DataSspSlot;
import com.ruoyi.system.service.IDataSspSlotService;

/**
 * 媒体数据报表Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-01
 */
@Service
public class DataSspSlotServiceImpl implements IDataSspSlotService
{
    @Autowired
    private DataSspSlotMapper dataSspSlotMapper;

    /**
     * 生成当前月份的表名
     * 格式：data_ssp_slot_hour_YYYYMM
     *
     * @return 表名
     */
    private String generateCurrentTableName()
    {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        return "data_ssp_slot_hour_" + currentMonth;
    }

    /**
     * 查询媒体数据报表
     *
     * @param dataSspSlot 包含 id 和动态表名 tableName
     * @return 媒体数据报表
     */
    @Override
    public DataSspSlot selectDataSspSlotById(DataSspSlot dataSspSlot)
    {
        // 如果没有设置表名，使用当前月份的表名
        if (dataSspSlot.getTableName() == null || dataSspSlot.getTableName().trim().isEmpty())
        {
            dataSspSlot.setTableName(generateCurrentTableName());
        }
        return dataSspSlotMapper.selectDataSspSlotById(dataSspSlot);
    }

    /**
     * 查询媒体数据报表列表
     *
     * @param dataSspSlot 媒体数据报表
     * @return 媒体数据报表
     */
    @Override
    public List<DataSspSlot> selectDataSspSlotList(DataSspSlot dataSspSlot)
    {
        // 如果没有设置表名，使用当前月份的表名
        if (dataSspSlot.getTableName() == null || dataSspSlot.getTableName().trim().isEmpty())
        {
            dataSspSlot.setTableName(generateCurrentTableName());
        }

        // 查询数据
        List<DataSspSlot> list = dataSspSlotMapper.selectDataSspSlotList(dataSspSlot);

        // 计算填充率、请求丢失率、展现率、点击率、eCPM（只计算，不存数据库）
        if (list != null && !list.isEmpty())
        {
            for (DataSspSlot data : list)
            {
                // 填充率 = 返回PV / 请求PV * 100%
                if (data.getReqPv() != null && data.getReqPv() > 0)
                {
                    double fillRate = (data.getRetPv() != null ? data.getRetPv() : 0.0) * 100.0 / data.getReqPv();
                    // 保留两位小数
                    data.setFillRate(Math.round(fillRate * 100.0) / 100.0);
                }
                else
                {
                    data.setFillRate(0.0);
                }

                // 请求丢失率 = 丢弃请求 / 请求PV * 100%
                if (data.getReqPv() != null && data.getReqPv() > 0)
                {
                    double requestLossRate = (data.getDiscard() != null ? data.getDiscard() : 0.0) * 100.0 / data.getReqPv();
                    // 保留两位小数
                    data.setRequestLossRate(Math.round(requestLossRate * 100.0) / 100.0);
                }
                else
                {
                    data.setRequestLossRate(0.0);
                }

                // 展现率 = 展示PV / 返回PV * 100%
                if (data.getRetPv() != null && data.getRetPv() > 0)
                {
                    double showRate = (data.getShowPv() != null ? data.getShowPv() : 0.0) * 100.0 / data.getRetPv();
                    // 保留两位小数
                    data.setShowRate(Math.round(showRate * 100.0) / 100.0);
                }
                else
                {
                    data.setShowRate(0.0);
                }

                // 点击率 = 点击PV / 返回PV * 100%
                if (data.getRetPv() != null && data.getRetPv() > 0)
                {
                    double clickRate = (data.getClickPv() != null ? data.getClickPv() : 0.0) * 100.0 / data.getRetPv();
                    // 保留两位小数
                    data.setClickRate(Math.round(clickRate * 100.0) / 100.0);
                }
                else
                {
                    data.setClickRate(0.0);
                }

                // eCPM = (收入 / 100) / 展示PV × 1000 = 收入 / 展示PV × 10
                if (data.getShowPv() != null && data.getShowPv() > 0)
                {
                    double ecpm = (data.getIncome() != null ? data.getIncome() : 0.0) * 10.0 / data.getShowPv();
                    // 保留两位小数
                    data.setEcpm(Math.round(ecpm * 100.0) / 100.0);
                }
                else
                {
                    data.setEcpm(0.0);
                }
            }
        }

        return list;
    }
}

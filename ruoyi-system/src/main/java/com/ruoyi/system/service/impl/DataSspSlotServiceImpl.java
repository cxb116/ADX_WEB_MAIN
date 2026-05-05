package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(DataSspSlotServiceImpl.class);

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
     * 生成当前月份的表名
     * 格式：data_dsp_slot_day_YYYYMM
     *
     * @return 表名
     */
    private String DspGenerateCurrentTableName()
    {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        return "data_dsp_slot_day_" + currentMonth;
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
        // 对于分成模式，自动计算并更新预算流水和利润收入
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

                // 如果是分成模式，自动计算预算流水和利润收入
                if (data.getSspPayType() != null && data.getSspPayType() == 1L
                    && data.getShowPv() != null && data.getShowPv() > 0
                    && data.getSspEcpm() != null && data.getSspEcpm() > 0)
                {
                    // 预算流水（分）= 展示PV × 千次收益（分） / 1000
                    long spend = data.getSspEcpm() * data.getShowPv() / 1000;
                    long spends = spend / 10;
                    data.setSpend(spends);

                    // 利润收入（分）= 预算流水（分）× 分成系数（%） / 100
                    long incomes = 0L;
                    if (data.getSspDealRatio() != null && data.getSspDealRatio() > 0)
                    {
                        long  income = spend * data.getSspDealRatio() / 100;
                         incomes = income / 10;
                    }
                    data.setIncome(incomes);

                } else {
                    if (data.getSpend() > 0) {
                        data.setSpend(data.getSpend() /10 );
                    }
                    if (data.getIncome() > 0) {
                        data.setIncome(data.getIncome() /10 );
                    }
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



    @Override
    public List<DataSspSlot> selectDataSspSlotListToDspData(DataSspSlot dataSspSlot) {
        // 如果没有设置表名，使用当前月份的表名
        if (dataSspSlot.getTableName() == null || dataSspSlot.getTableName().trim().isEmpty())
        {
            dataSspSlot.setTableName(generateCurrentTableName());
        }
        // 查询数据
        List<DataSspSlot> list = dataSspSlotMapper.selectDataSspSlotListToDspData(dataSspSlot);
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

                // 如果是分成模式，自动计算预算流水和利润收入
                if (data.getSspPayType() != null && data.getSspPayType() == 1L
                        && data.getShowPv() != null && data.getShowPv() > 0
                        && data.getSspEcpm() != null && data.getSspEcpm() > 0)
                {
                    // 预算流水（分）= 展示PV × 千次收益（分） / 1000
                    long spend = data.getSspEcpm() * data.getShowPv() / 1000;
                    data.setSpend(spend);

                    // 利润收入（分）= 预算流水（分）× 分成系数（%） / 100
                    long income = 0L;
                    if (data.getSspDealRatio() != null && data.getSspDealRatio() > 0)
                    {
                        income = spend * data.getSspDealRatio() / 100;
                    }
                    data.setIncome(income);

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

    /**
     * 修改媒体数据报表
     *
     * @param dataSspSlot 媒体数据报表
     * @return 结果
     */
    @Override
    public int updateDataSspSlot(DataSspSlot dataSspSlot)
    {
        // 如果没有设置表名，使用当前月份的表名
        if (dataSspSlot.getTableName() == null || dataSspSlot.getTableName().trim().isEmpty())
        {
            // 根据前端传来的日期或数据中的日期确定表名
            if (dataSspSlot.getDate() != null)
            {
                String dateStr = String.valueOf(dataSspSlot.getDate());
                if (dateStr.length() == 8)
                {
                    String yearMonth = dateStr.substring(0, 6); // yyyyMM
                    dataSspSlot.setTableName("data_ssp_slot_day_" + yearMonth);
                }
                else if (dateStr.length() == 10)
                {
                    String yearMonth = dateStr.substring(0, 6); // yyyyMM
                    dataSspSlot.setTableName("data_ssp_slot_hour_" + yearMonth);
                }
                else
                {
                    dataSspSlot.setTableName(generateCurrentTableName());
                }
            }
            else
            {
                dataSspSlot.setTableName(generateCurrentTableName());
            }
        }

        return dataSspSlotMapper.updateDataSspSlot(dataSspSlot);
    }

    /**
     * 计算当前月分成模式的预算流水和利润收入（定时任务使用）
     *
     * @return 计算的记录数
     */
    @Override
    public int calculateCurrentMonthData()
    {
        log.info("【Service层】开始计算当前月分成模式的预算流水和利润收入");

        // 生成当前月份的表名（格式：data_ssp_slot_day_YYYYMM）
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String tableName = "data_ssp_slot_day_" + currentMonth;
        log.info("【Service层】当前月份表名：{}", tableName);

        // 查询需要计算的数据（分成模式，有展示PV，有千次收益，spend或income为空或为0）
        log.info("【Service层】开始查询需要计算的数据...");
        List<DataSspSlot> dataList = dataSspSlotMapper.selectCurrentMonthDataForCalculation(tableName);

        if (dataList == null || dataList.isEmpty())
        {
            log.warn("【Service层】没有找到需要计算的数据，返回0");
            return 0;
        }

        log.info("【Service层】找到 {} 条需要计算的数据", dataList.size());

        // 计算每条数据的预算流水和利润收入
        java.util.List<java.util.Map<String, Object>> updateList = new java.util.ArrayList<>();
        for (DataSspSlot data : dataList)
        {
            long sspEcpm = data.getSspEcpm() != null ? data.getSspEcpm() : 0L;
            long showPv = data.getShowPv() != null ? data.getShowPv() : 0L;
            long sspDealRatio = data.getSspDealRatio() != null ? data.getSspDealRatio() : 0L;
            Long id = data.getId();

            // 预算流水（分）= 展示PV × 千次收益（分） / 1000
            long spend = sspEcpm * showPv / 1000;

            // 利润收入（分）= 预算流水（分）× 分成系数（%） / 100
            long income = spend * sspDealRatio / 100;

            log.info("【Service层】记录ID={}：展示PV={}，千次收益={}分，分成系数={}%，计算：预算流水={}分，利润收入={}分",
                     id, showPv, sspEcpm, sspDealRatio, spend, income);

            // 构建更新数据
            java.util.Map<String, Object> updateData = new java.util.HashMap<>();
            updateData.put("id", id);
            updateData.put("spend", spend);
            updateData.put("income", income);

            updateList.add(updateData);
        }

        // 批量更新数据库
        if (!updateList.isEmpty())
        {
            log.info("【Service层】开始批量更新数据库，共 {} 条记录", updateList.size());
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("tableName", tableName);
            params.put("list", updateList);

            int result = dataSspSlotMapper.batchUpdateSpendAndIncome(params);
            log.info("【Service层】批量更新完成，影响 {} 条记录", result);
            return result;
        }

        log.warn("【Service层】更新列表为空，返回0");
        return 0;
    }

    /**
     * 定时任务专用：批量更新分成模式的预算流水和收入
     *
     * @param tableName 表名
     * @param updateList 更新数据列表
     * @return 更新的记录数
     */
    @Override
    public int batchUpdateForSchedule(String tableName, java.util.List<java.util.Map<String, Object>> updateList)
    {
        log.info("【定时任务专用】开始批量更新数据，表名：{}", tableName);
        log.info("【定时任务专用】需要更新 {} 条记录", updateList.size());

        int result = dataSspSlotMapper.batchUpdateForSchedule(tableName, updateList);
        log.info("【定时任务专用】批量更新完成，影响 {} 条记录", result);
        return result;
    }
}

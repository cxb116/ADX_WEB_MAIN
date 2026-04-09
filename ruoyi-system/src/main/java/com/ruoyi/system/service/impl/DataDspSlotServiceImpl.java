package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DataDspSlotMapper;
import com.ruoyi.system.mapper.DataSspSlotMapper;
import com.ruoyi.system.domain.DataDspSlot;
import com.ruoyi.system.service.IDataDspSlotService;

/**
 * 预算报表Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-01
 */
@Service
public class DataDspSlotServiceImpl implements IDataDspSlotService
{
    @Autowired
    private DataDspSlotMapper dataDspSlotMapper;

    @Autowired
    private DataSspSlotMapper dataSspSlotMapper;

    /**
     * 生成当前月份的表名
     * 格式：data_dsp_slot_day_YYYYMM
     *
     * @return 表名
     */
    private String generateCurrentTableName()
    {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        return "data_dsp_slot_day_" + currentMonth;
    }

    /**
     * 查询预算报表
     *
     * @param dataDspSlot 包含 id 和动态表名 tableName
     * @return 预算报表
     */
    @Override
    public DataDspSlot selectDataDspSlotById(DataDspSlot dataDspSlot)
    {
        // 如果没有设置表名，使用当前月份的表名
        if (dataDspSlot.getTableName() == null || dataDspSlot.getTableName().trim().isEmpty())
        {
            dataDspSlot.setTableName(generateCurrentTableName());
        }
        return dataDspSlotMapper.selectDataDspSlotById(dataDspSlot);
    }

    /**
     * 查询预算报表列表
     *
     * @param dataDspSlot 预算报表
     * @return 预算报表
     */
    @Override
    public List<DataDspSlot> selectDataDspSlotList(DataDspSlot dataDspSlot)
    {
        // 如果没有设置表名，使用当前月份的表名
        if (dataDspSlot.getTableName() == null || dataDspSlot.getTableName().trim().isEmpty())
        {
            dataDspSlot.setTableName(generateCurrentTableName());
        }

        // 查询数据
        List<DataDspSlot> list = dataDspSlotMapper.selectDataDspSlotList(dataDspSlot);

        // 计算填充率、请求丢失率、展现率、点击率、eCPM（只计算，不存数据库）
        if (list != null && !list.isEmpty())
        {
            for (DataDspSlot data : list)
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

    /**
     * 查询当月汇总统计数据
     *
     * @param tableName 动态表名（格式：data_dsp_slot_day_YYYYMM）
     * @return 汇总统计数据
     */
    @Override
    public Map<String, Object> selectMonthlySummary(String tableName)
    {
        // 如果表名为空，使用当前月份的表名
        if (tableName == null || tableName.trim().isEmpty())
        {
            tableName = generateCurrentTableName();
        }
        return dataDspSlotMapper.selectMonthlySummary(tableName);
    }

    /**
     * 查询日期范围内的趋势数据（跨表查询）
     *
     * @param startDate 开始日期（格式：yyyyMMdd）
     * @param endDate 结束日期（格式：yyyyMMdd）
     * @return 按日期分组的数据列表
     */
    @Override
    public List<Map<String, Object>> selectTrendData(String startDate, String endDate)
    {
        // 解析日期，确定需要查询的表
        List<String> tableNames = new ArrayList<>();

        // 从开始日期到结束日期，找出所有涉及的月份表
        String startMonth = startDate.substring(0, 6); // yyyyMM
        String endMonth = endDate.substring(0, 6); // yyyyMM

        // 生成需要查询的表名列表
        String currentMonth = startMonth;
        while (true)
        {
            tableNames.add("data_dsp_slot_day_" + currentMonth);

            if (currentMonth.equals(endMonth))
            {
                break;
            }

            // 计算下一个月
            int year = Integer.parseInt(currentMonth.substring(0, 4));
            int month = Integer.parseInt(currentMonth.substring(4, 6));
            month++;
            if (month > 12)
            {
                month = 1;
                year++;
            }
            currentMonth = String.format("%04d%02d", year, month);
        }

        // 调用 mapper 进行跨表查询
        return dataDspSlotMapper.selectTrendData(tableNames, startDate, endDate);
    }

    /**
     * 查询指定天数的收益数据（跨表查询）
     *
     * @param days 查询天数
     * @return 按日期分组的收益数据列表
     */
    @Override
    public List<Map<String, Object>> selectRevenueData(int days)
    {
        // 计算日期范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        // 格式化日期为 yyyyMMdd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        // 确定需要查询的表名列表
        List<String> tableNames = new ArrayList<>();

        // 从开始月份到结束月份，找出所有涉及的月份表
        String startMonth = startDateStr.substring(0, 6); // yyyyMM
        String endMonth = endDateStr.substring(0, 6); // yyyyMM

        // 生成需要查询的表名列表
        String currentMonth = startMonth;
        while (true)
        {
            tableNames.add("data_dsp_slot_day_" + currentMonth);

            if (currentMonth.equals(endMonth))
            {
                break;
            }

            // 计算下一个月
            int year = Integer.parseInt(currentMonth.substring(0, 4));
            int month = Integer.parseInt(currentMonth.substring(4, 6));
            month++;
            if (month > 12)
            {
                month = 1;
                year++;
            }
            currentMonth = String.format("%04d%02d", year, month);
        }

        // 调用 mapper 进行跨表查询
        return dataDspSlotMapper.selectRevenueData(tableNames, startDateStr, endDateStr);
    }

    /**
     * 查询今日每小时填充率数据
     *
     * @param tableName 动态表名（格式：data_dsp_slot_day_YYYYMM）
     * @return 按小时分组的填充率数据列表
     */
    @Override
    public List<Map<String, Object>> selectTodayFillRate(String tableName)
    {
        // 如果表名为空，使用当前月份的表名
        if (tableName == null || tableName.trim().isEmpty())
        {
            tableName = generateCurrentTableName();
        }
        return dataDspSlotMapper.selectTodayFillRate(tableName);
    }

    /**
     * 查询近7天填充率数据（跨表查询）
     *
     * @param days 查询天数
     * @return 按日期分组的填充率数据列表
     */
    @Override
    public List<Map<String, Object>> selectFillRateData(int days)
    {
        // 计算日期范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        // 格式化日期为 yyyyMMdd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        // 确定需要查询的表名列表
        List<String> tableNames = new ArrayList<>();

        // 从开始月份到结束月份，找出所有涉及的月份表
        String startMonth = startDateStr.substring(0, 6); // yyyyMM
        String endMonth = endDateStr.substring(0, 6); // yyyyMM

        // 生成需要查询的表名列表
        String currentMonth = startMonth;
        while (true)
        {
            tableNames.add("data_dsp_slot_day_" + currentMonth);

            if (currentMonth.equals(endMonth))
            {
                break;
            }

            // 计算下一个月
            int year = Integer.parseInt(currentMonth.substring(0, 4));
            int month = Integer.parseInt(currentMonth.substring(4, 6));
            month++;
            if (month > 12)
            {
                month = 1;
                year++;
            }
            currentMonth = String.format("%04d%02d", year, month);
        }

        // 调用 mapper 进行跨表查询
        return dataDspSlotMapper.selectFillRateData(tableNames, startDateStr, endDateStr);
    }

    /**
     * 修改预算报表
     *
     * @param dataDspSlot 预算报表
     * @return 结果
     */
    @Override
    public int updateDataDspSlot(DataDspSlot dataDspSlot)
    {
        // 如果没有设置表名，使用当前月份的表名
        if (dataDspSlot.getTableName() == null || dataDspSlot.getTableName().trim().isEmpty())
        {
            dataDspSlot.setTableName(generateCurrentTableName());
        }

        // 如果 payType 为空，尝试从查询结果中获取 dspPayType
        if (dataDspSlot.getPayType() == null && dataDspSlot.getId() != null)
        {
            DataDspSlot fullData = dataDspSlotMapper.selectDataDspSlotById(dataDspSlot);
            if (fullData != null && fullData.getDspPayType() != null)
            {
                dataDspSlot.setPayType(fullData.getDspPayType());
            }
        }

        // 计算需要更新到两个表的数值
        Long dspSpend = dataDspSlot.getSpend(); // 预算流水（用户输入的原始值）
        Long sspIncome = 0L; // 收入（根据系数计算）
        Long sspSlotIncome = 0L; // 下游媒体收入（RTB模式：输入值 * RTB系数/100，分成模式：输入值 * 分成系数/100）

        // 先查询完整的数据以获取 dsp_slot_id
        DataDspSlot fullData = dataDspSlotMapper.selectDataDspSlotById(dataDspSlot);

        if (fullData != null && fullData.getDspSlotId() != null)
        {
            // 如果是 RTB 模式（payType == 2），需要查询 dsp_deal_ratio 并计算分成
            if (dataDspSlot.getPayType() != null && dataDspSlot.getPayType() == 2)
            {
                // 从 dsp_slot_info 表查询成交系数
                Integer dspDealRatio = dataDspSlotMapper.selectDspDealRatio(fullData.getDspSlotId());

                if (dspDealRatio != null && dspDealRatio > 0)
                {
                    // 计算收入：sspIncome = 输入值 * (1 - RTB系数/100)
                    sspIncome = dataDspSlot.getSpend() * (100 - dspDealRatio) / 100;
                    // 下游媒体收入：sspSlotIncome = 输入值 * RTB系数/100
                    sspSlotIncome = dataDspSlot.getSpend() * dspDealRatio / 100;
                }
            }
            // 如果是分成模式（payType == 1），需要查询媒体分成系数并计算
            else if (dataDspSlot.getPayType() != null && dataDspSlot.getPayType() == 1)
            {
                // 从 dsp_launch 表查询媒体广告位ID
                Long sspSlotId = dataDspSlotMapper.selectSspSlotIdByDspSlotId(fullData.getDspSlotId());

                if (sspSlotId != null)
                {
                    // 从 ssp_slot_info 表查询媒体ID
                    Long mediaId = dataDspSlotMapper.selectMediaIdBySspSlotId(sspSlotId);

                    if (mediaId != null)
                    {
                        // 从 ssp_slot_info 表查询媒体分成系数
                        Integer sspDealRatio = dataDspSlotMapper.selectSspDealRatioByMediaId(mediaId);

                        if (sspDealRatio != null && sspDealRatio > 0)
                        {
                            // dsp表收入 = 输入值 * 媒体分成系数/100
                            sspIncome = dataDspSlot.getSpend() * sspDealRatio / 100;
                            // 下游媒体收入 = 输入值 * (1 - 媒体分成系数/100)
                            sspSlotIncome = dataDspSlot.getSpend() * (100 - sspDealRatio) / 100;
                        }
                    }
                }
            }
        }

        // 更新 data_dsp_slot_day 表（更新 spend 和 income）
        DataDspSlot updateDsp = new DataDspSlot();
        updateDsp.setId(dataDspSlot.getId());
        updateDsp.setTableName(dataDspSlot.getTableName());
        updateDsp.setSpend(dspSpend); // 预算流水使用用户输入的原始值
        updateDsp.setIncome(sspIncome); // 收入使用计算后的值
        int rows = dataDspSlotMapper.updateDataDspSlot(updateDsp);

        // 如果更新成功，则同步更新 data_ssp_slot_day 表（给下游看的数据）
        if (rows > 0 && sspSlotIncome != null && dataDspSlot.getId() != null)
        {
            if (fullData != null && fullData.getDate() != null)
            {
                // 生成对应的 SSP 表名（data_ssp_slot_day_YYYYMM）
                String sspTableName = dataDspSlot.getTableName().replace("data_dsp_slot_day_", "data_ssp_slot_day_");

                // 同步更新 data_ssp_slot_day 表中的 income 字段（下游媒体收入）
                dataSspSlotMapper.updateIncomeByDspSlotInfo(
                    sspTableName,
                    fullData.getDspSlotId(),
                    fullData.getDspSlotCode(),
                    fullData.getDate(),
                    sspSlotIncome
                );
            }
        }

        return rows;
    }
}

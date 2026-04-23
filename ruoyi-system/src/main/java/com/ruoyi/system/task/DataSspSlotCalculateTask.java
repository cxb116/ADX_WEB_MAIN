package com.ruoyi.system.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.DspCompany;
import com.ruoyi.system.domain.DspInputCompanyExce;
import com.ruoyi.system.service.IDspCompanyService;
import com.ruoyi.system.service.IDspInputCompanyExceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ruoyi.system.service.IDataSspSlotService;

/**
 * 媒体数据报表分成模式数据计算定时任务
 * 每隔1分钟执行一次，计算当前月分成模式的预算流水和利润收入
 *
 * @author ruoyi
 */
@Component("dataSspSlotCalculateTask")
public class DataSspSlotCalculateTask
{
    private static final Logger log = LoggerFactory.getLogger(DataSspSlotCalculateTask.class);

    @Autowired
    private IDataSspSlotService dataSspSlotService;

    @Autowired
    private IDspCompanyService dspCompanyService;


    @Autowired
    private IDspInputCompanyExceService dspInputCompanyExceService;

    /**
     *  每天去执行一次生成预算方的公司数
     */
    @Scheduled(cron = "0 1 9 * * ?")
    public void calculateCurrentMonth()
    {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("========== 定时任务开始执行 ========== 时间：{}", timestamp);

        try
        {
            log.info("【定时任务】开始调用Service层计算当前月分成模式数据");
            List<DspCompany> dspCompanies = dspCompanyService.selectDspCompanyList(null);
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            String dateStr = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            for (DspCompany company : dspCompanies) {
                DspInputCompanyExce dspInputCompanyExce = new DspInputCompanyExce();
                dspInputCompanyExce.setCompanyId(company.getId());
                dspInputCompanyExce.setCreateTime(dateStr);
                dspInputCompanyExceService.insertDspInputCompanyExce(dspInputCompanyExce);
            }

            log.info("========== 定时任务执行完成 ========== 时间：{}", timestamp);
        }
        catch (Exception e)
        {
            log.error("【定时任务】媒体数据报表分成模式数据计算任务执行失败，错误信息：{}", e.getMessage(), e);
        }
    }

    /**
     * 定时任务专用：手动触发执行（用于测试）
     */
    @Scheduled(cron = "-")
    public void executeCalculateTask()
    {
        log.info("========== 手动触发定时任务 ==========");
        calculateCurrentMonth();
    }
}

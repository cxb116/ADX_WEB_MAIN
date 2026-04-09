package com.ruoyi.system.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * 计算当前月分成模式的数据
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void calculateCurrentMonth()
    {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("========== 定时任务开始执行 ========== 时间：{}", timestamp);

        try
        {
            log.info("【定时任务】开始调用Service层计算当前月分成模式数据");

            // 调用服务层方法计算数据
            int count = dataSspSlotService.calculateCurrentMonthData();

            if (count > 0)
            {
                log.info("【定时任务】媒体数据报表分成模式数据计算完成，共计算 {} 条记录", count);
            }
            else
            {
                log.info("【定时任务】媒体数据报表分成模式数据计算完成，无需要计算的数据");
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

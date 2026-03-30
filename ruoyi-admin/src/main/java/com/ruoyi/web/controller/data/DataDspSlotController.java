package com.ruoyi.web.controller.data;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.DataDspSlot;
import com.ruoyi.system.service.IDataDspSlotService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 预算报表Controller
 *
 * @author ruoyi
 * @date 2026-03-01
 */
@RestController
@RequestMapping("/system/dataDspSlot")
public class DataDspSlotController extends BaseController
{
    @Autowired
    private IDataDspSlotService dataDspSlotService;

    /**
     * 查询预算报表列表
     */
    @PreAuthorize("@ss.hasPermi('system:dataDspSlot:list')")
    @GetMapping("/list")
    public TableDataInfo list(DataDspSlot dataDspSlot)
    {
        startPage();
        List<DataDspSlot> list = dataDspSlotService.selectDataDspSlotList(dataDspSlot);
        return getDataTable(list);
    }

    /**
     * 导出预算报表列表
     */
    @PreAuthorize("@ss.hasPermi('system:dataDspSlot:export')")
    @Log(title = "预算报表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, DataDspSlot dataDspSlot)
    {
        List<DataDspSlot> list = dataDspSlotService.selectDataDspSlotList(dataDspSlot);
        ExcelUtil<DataDspSlot> util = new ExcelUtil<DataDspSlot>(DataDspSlot.class);
        util.exportExcel(response, list, "预算报表数据");
    }

    /**
     * 获取预算报表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dataDspSlot:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id, DataDspSlot dataDspSlot)
    {
        dataDspSlot.setId(id);
        return success(dataDspSlotService.selectDataDspSlotById(dataDspSlot));
    }

    /**
     * 修改预算报表
     */
    @PreAuthorize("@ss.hasPermi('system:dataDspSlot:edit')")
    @Log(title = "预算报表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DataDspSlot dataDspSlot)
    {
        return toAjax(dataDspSlotService.updateDataDspSlot(dataDspSlot));
    }

    /**
     * 获取当月汇总统计数据
     */
    @GetMapping("/summary")
    public AjaxResult getMonthlySummary(@RequestParam(value = "tableName", required = false) String tableName)
    {
        Map<String, Object> summary = dataDspSlotService.selectMonthlySummary(tableName);
        return success(summary);
    }

    /**
     * 获取趋势数据
     */
    @GetMapping("/trend")
    public AjaxResult getTrendData(
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate)
    {
        List<Map<String, Object>> trendData = dataDspSlotService.selectTrendData(startDate, endDate);
        return success(trendData);
    }

    /**
     * 获取收益数据
     */
    @GetMapping("/revenue")
    public AjaxResult getRevenueData(@RequestParam(value = "days", defaultValue = "7") int days)
    {
        List<Map<String, Object>> revenueData = dataDspSlotService.selectRevenueData(days);
        return success(revenueData);
    }

    /**
     * 获取今日填充率数据
     */
    @GetMapping("/todayFillRate")
    public AjaxResult getTodayFillRate(@RequestParam(value = "tableName", required = false) String tableName)
    {
        List<Map<String, Object>> fillRateData = dataDspSlotService.selectTodayFillRate(tableName);
        return success(fillRateData);
    }

    /**
     * 获取近7天填充率数据
     */
    @GetMapping("/fillRate")
    public AjaxResult getFillRateData(@RequestParam(value = "days", defaultValue = "7") int days)
    {
        List<Map<String, Object>> fillRateData = dataDspSlotService.selectFillRateData(days);
        return success(fillRateData);
    }
}

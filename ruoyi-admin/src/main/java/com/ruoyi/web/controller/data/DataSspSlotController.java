package com.ruoyi.web.controller.data;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.DataSspSlot;
import com.ruoyi.system.service.IDataSspSlotService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 媒体数据报表Controller
 *
 * @author ruoyi
 * @date 2026-03-01
 */
@RestController
@RequestMapping("/data/data_ssp_slot")
public class DataSspSlotController extends BaseController
{
    @Autowired
    private IDataSspSlotService dataSspSlotService;

    /**
     * 查询媒体数据报表列表
     */
    @PreAuthorize("@ss.hasPermi('data:data_ssp_slot:list')")
    @GetMapping("/list")
    public TableDataInfo list(DataSspSlot dataSspSlot)
    {
        startPage();
        List<DataSspSlot> list = dataSspSlotService.selectDataSspSlotList(dataSspSlot);

        for (DataSspSlot sspSlot : list) {

        }

        return getDataTable(list);
    }

    /**
     * 导出媒体数据报表列表
     */
    @PreAuthorize("@ss.hasPermi('data:data_ssp_slot:export')")
    @Log(title = "媒体数据报表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, DataSspSlot dataSspSlot)
    {
        List<DataSspSlot> list = dataSspSlotService.selectDataSspSlotList(dataSspSlot);
        ExcelUtil<DataSspSlot> util = new ExcelUtil<DataSspSlot>(DataSspSlot.class);
        util.exportExcel(response, list, "媒体数据报表数据");
    }

    /**
     * 获取媒体数据报表详细信息
     */
    @PreAuthorize("@ss.hasPermi('data:data_ssp_slot:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id, DataSspSlot dataSspSlot)
    {
        dataSspSlot.setId(id);
        return success(dataSspSlotService.selectDataSspSlotById(dataSspSlot));
    }

    /**
     * 修改媒体数据报表
     */
    @PreAuthorize("@ss.hasPermi('data:data_ssp_slot:edit')")
    @Log(title = "修改媒体数据报表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DataSspSlot dataSspSlot)
    {
        return toAjax(dataSspSlotService.updateDataSspSlot(dataSspSlot));
    }

    /**
     * 手动执行分成模式数据计算（测试用）
     */
    @GetMapping("/calculate")
    public AjaxResult calculate()
    {
        int count = dataSspSlotService.calculateCurrentMonthData();
        return success("分成模式数据计算完成，共计算 " + count + " 条记录");
    }
}

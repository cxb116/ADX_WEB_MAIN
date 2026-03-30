package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.DspDataCorrected;
import com.ruoyi.system.service.IDspDataCorrectedService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 支出修正Controller
 * 
 * @author ruoyi
 * @date 2026-03-22
 */
@RestController
@RequestMapping("/data/corrected")
public class DspDataCorrectedController extends BaseController
{
    @Autowired
    private IDspDataCorrectedService dspDataCorrectedService;

    /**
     * 查询支出修正列表
     */
    @PreAuthorize("@ss.hasPermi('data:corrected:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspDataCorrected dspDataCorrected)
    {
        startPage();
        List<DspDataCorrected> list = dspDataCorrectedService.selectDspDataCorrectedList(dspDataCorrected);
        return getDataTable(list);
    }

    /**
     * 导出支出修正列表
     */
    @PreAuthorize("@ss.hasPermi('data:corrected:export')")
    @Log(title = "支出修正", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspDataCorrected dspDataCorrected)
    {
        List<DspDataCorrected> list = dspDataCorrectedService.selectDspDataCorrectedList(dspDataCorrected);
        ExcelUtil<DspDataCorrected> util = new ExcelUtil<DspDataCorrected>(DspDataCorrected.class);
        util.exportExcel(response, list, "支出修正数据");
    }

    /**
     * 获取支出修正详细信息
     */
    @PreAuthorize("@ss.hasPermi('data:corrected:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspDataCorrectedService.selectDspDataCorrectedById(id));
    }

    /**
     * 新增支出修正
     */
    @PreAuthorize("@ss.hasPermi('data:corrected:add')")
    @Log(title = "支出修正", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspDataCorrected dspDataCorrected)
    {
        return toAjax(dspDataCorrectedService.insertDspDataCorrected(dspDataCorrected));
    }

    /**
     * 修改支出修正
     */
    @PreAuthorize("@ss.hasPermi('data:corrected:edit')")
    @Log(title = "支出修正", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspDataCorrected dspDataCorrected)
    {
        return toAjax(dspDataCorrectedService.updateDspDataCorrected(dspDataCorrected));
    }

    /**
     * 删除支出修正
     */
    @PreAuthorize("@ss.hasPermi('data:corrected:remove')")
    @Log(title = "支出修正", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspDataCorrectedService.deleteDspDataCorrectedByIds(ids));
    }
}

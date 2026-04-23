package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.DspIncome;
import com.ruoyi.system.service.IDspIncomeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 预算收益导入Controller
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
@RestController
@RequestMapping("/dsp/income")
public class DspIncomeController extends BaseController
{
    @Autowired
    private IDspIncomeService dspIncomeService;

    /**
     * 查询预算收益导入列表
     */
    @PreAuthorize("@ss.hasPermi('dsp:income:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspIncome dspIncome)
    {
        startPage();
        List<DspIncome> list = dspIncomeService.selectDspIncomeList(dspIncome);
        return getDataTable(list);
    }

    /**
     * 导出预算收益导入列表
     */
    @PreAuthorize("@ss.hasPermi('dsp:income:export')")
    @Log(title = "预算收益导入", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspIncome dspIncome)
    {
        List<DspIncome> list = dspIncomeService.selectDspIncomeList(dspIncome);
        ExcelUtil<DspIncome> util = new ExcelUtil<DspIncome>(DspIncome.class);
        util.exportExcel(response, list, "预算收益导入数据");
    }

    /**
     * 获取预算收益导入详细信息
     */
    @PreAuthorize("@ss.hasPermi('dsp:income:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspIncomeService.selectDspIncomeById(id));
    }

    /**
     * 新增预算收益导入
     */
    @PreAuthorize("@ss.hasPermi('dsp:income:add')")
    @Log(title = "预算收益导入", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspIncome dspIncome)
    {
        return toAjax(dspIncomeService.insertDspIncome(dspIncome));
    }

    /**
     * 修改预算收益导入
     */
    @PreAuthorize("@ss.hasPermi('dsp:income:edit')")
    @Log(title = "预算收益导入", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspIncome dspIncome)
    {
        return toAjax(dspIncomeService.updateDspIncome(dspIncome));
    }

    /**
     * 删除预算收益导入
     */
    @PreAuthorize("@ss.hasPermi('dsp:income:remove')")
    @Log(title = "预算收益导入", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspIncomeService.deleteDspIncomeByIds(ids));
    }
}

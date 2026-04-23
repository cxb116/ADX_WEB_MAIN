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
import com.ruoyi.system.domain.DspInputExce;
import com.ruoyi.system.service.IDspInputExceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 收益导入exceController
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
@RestController
@RequestMapping("/exce/exce")
public class DspInputExceController extends BaseController
{
    @Autowired
    private IDspInputExceService dspInputExceService;

    /**
     * 查询收益导入exce列表
     */
    @PreAuthorize("@ss.hasPermi('exce:exce:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspInputExce dspInputExce)
    {
        startPage();
        List<DspInputExce> list = dspInputExceService.selectDspInputExceList(dspInputExce);
        return getDataTable(list);
    }

    /**
     * 导出收益导入exce列表
     */
    @PreAuthorize("@ss.hasPermi('exce:exce:export')")
    @Log(title = "收益导入exce", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspInputExce dspInputExce)
    {
        List<DspInputExce> list = dspInputExceService.selectDspInputExceList(dspInputExce);
        ExcelUtil<DspInputExce> util = new ExcelUtil<DspInputExce>(DspInputExce.class);
        util.exportExcel(response, list, "收益导入exce数据");
    }

    /**
     * 获取收益导入exce详细信息
     */
    @PreAuthorize("@ss.hasPermi('exce:exce:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspInputExceService.selectDspInputExceById(id));
    }

    /**
     * 新增收益导入exce
     */
    @PreAuthorize("@ss.hasPermi('exce:exce:add')")
    @Log(title = "收益导入exce", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspInputExce dspInputExce)
    {
        return toAjax(dspInputExceService.insertDspInputExce(dspInputExce));
    }

    /**
     * 修改收益导入exce
     */
    @PreAuthorize("@ss.hasPermi('exce:exce:edit')")
    @Log(title = "收益导入exce", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspInputExce dspInputExce)
    {
        return toAjax(dspInputExceService.updateDspInputExce(dspInputExce));
    }

    /**
     * 删除收益导入exce
     */
    @PreAuthorize("@ss.hasPermi('exce:exce:remove')")
    @Log(title = "收益导入exce", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspInputExceService.deleteDspInputExceByIds(ids));
    }
}

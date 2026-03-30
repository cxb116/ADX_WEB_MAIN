package com.ruoyi.web.controller.dsp;

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
import com.ruoyi.system.domain.DspCompany;
import com.ruoyi.system.service.IDspCompanyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公司管理Controller
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/budget/company")
public class DspCompanyController extends BaseController
{
    @Autowired
    private IDspCompanyService dspCompanyService;

    /**
     * 查询公司管理列表
     */
    @PreAuthorize("@ss.hasPermi('budget:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspCompany dspCompany)
    {
        startPage();
        List<DspCompany> list = dspCompanyService.selectDspCompanyList(dspCompany);
        return getDataTable(list);
    }

    /**
     * 导出公司管理列表
     */
    @PreAuthorize("@ss.hasPermi('budget:company:export')")
    @Log(title = "公司管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspCompany dspCompany)
    {
        List<DspCompany> list = dspCompanyService.selectDspCompanyList(dspCompany);
        ExcelUtil<DspCompany> util = new ExcelUtil<DspCompany>(DspCompany.class);
        util.exportExcel(response, list, "公司管理数据");
    }

    /**
     * 获取公司管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('budget:company:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspCompanyService.selectDspCompanyById(id));
    }

    /**
     * 新增公司管理
     */
    @PreAuthorize("@ss.hasPermi('budget:company:add')")
    @Log(title = "公司管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspCompany dspCompany)
    {
        return toAjax(dspCompanyService.insertDspCompany(dspCompany));
    }

    /**
     * 修改公司管理
     */
    @PreAuthorize("@ss.hasPermi('budget:company:edit')")
    @Log(title = "公司管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspCompany dspCompany)
    {
        return toAjax(dspCompanyService.updateDspCompany(dspCompany));
    }

    /**
     * 删除公司管理
     */
    @PreAuthorize("@ss.hasPermi('budget:company:remove')")
    @Log(title = "公司管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspCompanyService.deleteDspCompanyByIds(ids));
    }
}

package com.ruoyi.web.controller.ssp;

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
import com.ruoyi.system.domain.SspApp;
import com.ruoyi.system.service.ISspAppService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应用管理Controller
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/flow/app")
public class SspAppController extends BaseController
{
    @Autowired
    private ISspAppService sspAppService;

    /**
     * 查询应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('flow:app:list')")
    @GetMapping("/list")
    public TableDataInfo list(SspApp sspApp)
    {
        startPage();
        List<SspApp> list = sspAppService.selectSspAppList(sspApp);
        return getDataTable(list);
    }

    /**
     * 导出应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('flow:app:export')")
    @Log(title = "应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SspApp sspApp)
    {
        List<SspApp> list = sspAppService.selectSspAppList(sspApp);
        ExcelUtil<SspApp> util = new ExcelUtil<SspApp>(SspApp.class);
        util.exportExcel(response, list, "应用管理数据");
    }

    /**
     * 获取应用管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('flow:app:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sspAppService.selectSspAppById(id));
    }

    /**
     * 新增应用管理
     */
    @PreAuthorize("@ss.hasPermi('flow:app:add')")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SspApp sspApp)
    {
        return toAjax(sspAppService.insertSspApp(sspApp));
    }

    /**
     * 修改应用管理
     */
    @PreAuthorize("@ss.hasPermi('flow:app:edit')")
    @Log(title = "应用管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SspApp sspApp)
    {
        return toAjax(sspAppService.updateSspApp(sspApp));
    }

    /**
     * 删除应用管理
     */
    @PreAuthorize("@ss.hasPermi('flow:app:remove')")
    @Log(title = "应用管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sspAppService.deleteSspAppByIds(ids));
    }
}

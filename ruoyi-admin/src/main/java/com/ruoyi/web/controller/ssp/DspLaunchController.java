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
import com.ruoyi.system.domain.DspLaunch;
import com.ruoyi.system.service.IDspLaunchService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告生成Controller
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/dspLaunch/launch")
public class DspLaunchController extends BaseController
{
    @Autowired
    private IDspLaunchService dspLaunchService;

    /**
     * 查询广告生成列表
     */
    @PreAuthorize("@ss.hasPermi('dspLaunch:launch:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspLaunch dspLaunch)
    {
        startPage();
        List<DspLaunch> list = dspLaunchService.selectDspLaunchList(dspLaunch);
        return getDataTable(list);
    }

    /**
     * 导出广告生成列表
     */
    @PreAuthorize("@ss.hasPermi('dspLaunch:launch:export')")
    @Log(title = "广告生成", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspLaunch dspLaunch)
    {
        List<DspLaunch> list = dspLaunchService.selectDspLaunchList(dspLaunch);
        ExcelUtil<DspLaunch> util = new ExcelUtil<DspLaunch>(DspLaunch.class);
        util.exportExcel(response, list, "广告生成数据");
    }

    /**
     * 获取广告生成详细信息
     */
    @PreAuthorize("@ss.hasPermi('dspLaunch:launch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspLaunchService.selectDspLaunchById(id));
    }

    /**
     * 新增广告生成
     */
    @PreAuthorize("@ss.hasPermi('dspLaunch:launch:add')")
    @Log(title = "广告生成", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspLaunch dspLaunch)
    {
        return toAjax(dspLaunchService.insertDspLaunch(dspLaunch));
    }

    /**
     * 修改广告生成
     */
    @PreAuthorize("@ss.hasPermi('dspLaunch:launch:edit')")
    @Log(title = "广告生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspLaunch dspLaunch)
    {
        return toAjax(dspLaunchService.updateDspLaunch(dspLaunch));
    }

    /**
     * 删除广告生成
     */
    @PreAuthorize("@ss.hasPermi('dspLaunch:launch:remove')")
    @Log(title = "广告生成", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspLaunchService.deleteDspLaunchByIds(ids));
    }
}

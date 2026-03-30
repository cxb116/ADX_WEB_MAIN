package com.ruoyi.web.controller.ad;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.system.domain.DspAdType;
import com.ruoyi.system.service.IDspAdTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告类型Controller
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
@RestController
@RequestMapping("/ad/type")
public class DspAdTypeController extends BaseController
{
    @Autowired
    private IDspAdTypeService dspAdTypeService;

    /**
     * 查询广告类型列表
     */
    @PreAuthorize("@ss.hasPermi('ad:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspAdType dspAdType)
    {
        startPage();
        List<DspAdType> list = dspAdTypeService.selectDspAdTypeList(dspAdType);
        return getDataTable(list);
    }

    /**
     * 导出广告类型列表
     */
    @PreAuthorize("@ss.hasPermi('ad:type:export')")
    @Log(title = "广告类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspAdType dspAdType)
    {
        List<DspAdType> list = dspAdTypeService.selectDspAdTypeList(dspAdType);
        ExcelUtil<DspAdType> util = new ExcelUtil<DspAdType>(DspAdType.class);
        util.exportExcel(response, list, "广告类型数据");
    }

    /**
     * 获取广告类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('ad:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspAdTypeService.selectDspAdTypeById(id));
    }

    /**
     * 新增广告类型
     */
    @PreAuthorize("@ss.hasPermi('ad:type:add')")
    @Log(title = "广告类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspAdType dspAdType)
    {
        return toAjax(dspAdTypeService.insertDspAdType(dspAdType));
    }

    /**
     * 修改广告类型
     */
    @PreAuthorize("@ss.hasPermi('ad:type:edit')")
    @Log(title = "广告类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspAdType dspAdType)
    {
        return toAjax(dspAdTypeService.updateDspAdType(dspAdType));
    }

    /**
     * 删除广告类型
     */
    @PreAuthorize("@ss.hasPermi('ad:type:remove')")
    @Log(title = "广告类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspAdTypeService.deleteDspAdTypeByIds(ids));
    }

    /**
     * 获取广告类型分布统计
     */
    @GetMapping("/distribution")
    public AjaxResult getDistribution()
    {
        List<Map<String, Object>> distribution = dspAdTypeService.selectAdTypeDistribution();
        return success(distribution);
    }
}

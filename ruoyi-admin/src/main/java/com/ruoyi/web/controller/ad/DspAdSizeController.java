package com.ruoyi.web.controller.ad;

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
import com.ruoyi.system.domain.DspAdSize;
import com.ruoyi.system.service.IDspAdSizeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 样式尺寸Controller
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
@RestController
@RequestMapping("/ad/size")
public class DspAdSizeController extends BaseController
{
    @Autowired
    private IDspAdSizeService dspAdSizeService;

    /**
     * 查询样式尺寸列表
     */
    @PreAuthorize("@ss.hasPermi('ad:size:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspAdSize dspAdSize)
    {
        startPage();
        List<DspAdSize> list = dspAdSizeService.selectDspAdSizeList(dspAdSize);
        return getDataTable(list);
    }

    /**
     * 导出样式尺寸列表
     */
    @PreAuthorize("@ss.hasPermi('ad:size:export')")
    @Log(title = "样式尺寸", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspAdSize dspAdSize)
    {
        List<DspAdSize> list = dspAdSizeService.selectDspAdSizeList(dspAdSize);
        ExcelUtil<DspAdSize> util = new ExcelUtil<DspAdSize>(DspAdSize.class);
        util.exportExcel(response, list, "样式尺寸数据");
    }

    /**
     * 获取样式尺寸详细信息
     */
    @PreAuthorize("@ss.hasPermi('ad:size:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspAdSizeService.selectDspAdSizeById(id));
    }

    /**
     * 新增样式尺寸
     */
    @PreAuthorize("@ss.hasPermi('ad:size:add')")
    @Log(title = "样式尺寸", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspAdSize dspAdSize)
    {
        return toAjax(dspAdSizeService.insertDspAdSize(dspAdSize));
    }

    /**
     * 修改样式尺寸
     */
    @PreAuthorize("@ss.hasPermi('ad:size:edit')")
    @Log(title = "样式尺寸", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspAdSize dspAdSize)
    {
        return toAjax(dspAdSizeService.updateDspAdSize(dspAdSize));
    }

    /**
     * 删除样式尺寸
     */
    @PreAuthorize("@ss.hasPermi('ad:size:remove')")
    @Log(title = "样式尺寸", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspAdSizeService.deleteDspAdSizeByIds(ids));
    }
}

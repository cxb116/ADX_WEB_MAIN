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
import com.ruoyi.system.domain.DspProduct;
import com.ruoyi.system.service.IDspProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 产品管理Controller
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/budget/product")
public class DspProductController extends BaseController
{
    @Autowired
    private IDspProductService dspProductService;

    /**
     * 查询产品管理列表
     */
    @PreAuthorize("@ss.hasPermi('budget:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspProduct dspProduct)
    {
        startPage();
        List<DspProduct> list = dspProductService.selectDspProductList(dspProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品管理列表
     */
    @PreAuthorize("@ss.hasPermi('budget:product:export')")
    @Log(title = "产品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspProduct dspProduct)
    {
        List<DspProduct> list = dspProductService.selectDspProductList(dspProduct);
        ExcelUtil<DspProduct> util = new ExcelUtil<DspProduct>(DspProduct.class);
        util.exportExcel(response, list, "产品管理数据");
    }

    /**
     * 获取产品管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('budget:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspProductService.selectDspProductById(id));
    }

    /**
     * 新增产品管理
     */
    @PreAuthorize("@ss.hasPermi('budget:product:add')")
    @Log(title = "产品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspProduct dspProduct)
    {
        return toAjax(dspProductService.insertDspProduct(dspProduct));
    }

    /**
     * 修改产品管理
     */
    @PreAuthorize("@ss.hasPermi('budget:product:edit')")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspProduct dspProduct)
    {
        return toAjax(dspProductService.updateDspProduct(dspProduct));
    }

    /**
     * 删除产品管理
     */
    @PreAuthorize("@ss.hasPermi('budget:product:remove')")
    @Log(title = "产品管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspProductService.deleteDspProductByIds(ids));
    }
}

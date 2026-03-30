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
import com.ruoyi.system.domain.DspAdScene;
import com.ruoyi.system.service.IDspAdSceneService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告场景Controller
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
@RestController
@RequestMapping("/ad/scene")
public class DspAdSceneController extends BaseController
{
    @Autowired
    private IDspAdSceneService dspAdSceneService;

    /**
     * 查询广告场景列表
     */
    @PreAuthorize("@ss.hasPermi('ad:scene:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspAdScene dspAdScene)
    {
        startPage();
        List<DspAdScene> list = dspAdSceneService.selectDspAdSceneList(dspAdScene);
        return getDataTable(list);
    }

    /**
     * 导出广告场景列表
     */
    @PreAuthorize("@ss.hasPermi('ad:scene:export')")
    @Log(title = "广告场景", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DspAdScene dspAdScene)
    {
        List<DspAdScene> list = dspAdSceneService.selectDspAdSceneList(dspAdScene);
        ExcelUtil<DspAdScene> util = new ExcelUtil<DspAdScene>(DspAdScene.class);
        util.exportExcel(response, list, "广告场景数据");
    }

    /**
     * 获取广告场景详细信息
     */
    @PreAuthorize("@ss.hasPermi('ad:scene:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspAdSceneService.selectDspAdSceneById(id));
    }

    /**
     * 新增广告场景
     */
    @PreAuthorize("@ss.hasPermi('ad:scene:add')")
    @Log(title = "广告场景", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspAdScene dspAdScene)
    {
        return toAjax(dspAdSceneService.insertDspAdScene(dspAdScene));
    }

    /**
     * 修改广告场景
     */
    @PreAuthorize("@ss.hasPermi('ad:scene:edit')")
    @Log(title = "广告场景", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspAdScene dspAdScene)
    {
        return toAjax(dspAdSceneService.updateDspAdScene(dspAdScene));
    }

    /**
     * 删除广告场景
     */
    @PreAuthorize("@ss.hasPermi('ad:scene:remove')")
    @Log(title = "广告场景", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspAdSceneService.deleteDspAdSceneByIds(ids));
    }
}

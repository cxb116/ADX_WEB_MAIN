package com.ruoyi.web.controller.ssp;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.ruoyi.system.domain.SspMedia;
import com.ruoyi.system.domain.SspApp;
import com.ruoyi.system.service.ISspMediaService;
import com.ruoyi.system.service.ISspAppService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 媒体管理Controller
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/flow/media")
public class SspMediaController extends BaseController
{
    @Autowired
    private ISspMediaService sspMediaService;

    @Autowired
    private ISspAppService sspAppService;

    /**
     * 查询媒体管理列表
     */
    @PreAuthorize("@ss.hasPermi('flow:media:list')")
    @GetMapping("/list")
    public TableDataInfo list(SspMedia sspMedia)
    {
        startPage();
        List<SspMedia> list = sspMediaService.selectSspMediaList(sspMedia);
        return getDataTable(list);
    }

    /**
     * 导出媒体管理列表
     */
    @PreAuthorize("@ss.hasPermi('flow:media:export')")
    @Log(title = "媒体管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SspMedia sspMedia)
    {
        List<SspMedia> list = sspMediaService.selectSspMediaList(sspMedia);
        ExcelUtil<SspMedia> util = new ExcelUtil<SspMedia>(SspMedia.class);
        util.exportExcel(response, list, "媒体管理数据");
    }

    /**
     * 获取媒体管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('flow:media:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sspMediaService.selectSspMediaById(id));
    }

    /**
     * 新增媒体管理
     */
    @PreAuthorize("@ss.hasPermi('flow:media:add')")
    @Log(title = "媒体管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SspMedia sspMedia)
    {
        return toAjax(sspMediaService.insertSspMedia(sspMedia));
    }

    /**
     * 修改媒体管理
     */
    @PreAuthorize("@ss.hasPermi('flow:media:edit')")
    @Log(title = "媒体管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SspMedia sspMedia)
    {
        return toAjax(sspMediaService.updateSspMedia(sspMedia));
    }

    /**
     * 删除媒体管理
     */
    @PreAuthorize("@ss.hasPermi('flow:media:remove')")
    @Log(title = "媒体管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sspMediaService.deleteSspMediaByIds(ids));
    }

    /**
     * 获取媒体应用级联数据
     */
    @GetMapping("/cascader")
    public AjaxResult getCascaderData()
    {
        // 查询所有启用的媒体
        SspMedia mediaQuery = new SspMedia();
        mediaQuery.setEnable(1L);
        List<SspMedia> mediaList = sspMediaService.selectSspMediaList(mediaQuery);

        List<Map<String, Object>> cascaderData = new ArrayList<>();

        for (SspMedia media : mediaList) {
            Map<String, Object> mediaNode = new HashMap<>();
            mediaNode.put("value", media.getId());
            mediaNode.put("label", media.getName());

            // 查询该媒体下的所有启用的应用
            SspApp appQuery = new SspApp();
            appQuery.setMediaId(media.getId());
            appQuery.setEnable(1L);
            List<SspApp> appList = sspAppService.selectSspAppList(appQuery);

            List<Map<String, Object>> children = new ArrayList<>();
            for (SspApp app : appList) {
                Map<String, Object> appNode = new HashMap<>();
                appNode.put("value", app.getId());
                appNode.put("label", app.getName());
                children.add(appNode);
            }

            mediaNode.put("children", children);
            cascaderData.add(mediaNode);
        }

        return success(cascaderData);
    }
}

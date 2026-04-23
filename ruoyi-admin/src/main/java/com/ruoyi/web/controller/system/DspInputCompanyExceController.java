package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.DspIncome;
import com.ruoyi.system.service.IDspIncomeService;
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
import com.ruoyi.system.domain.DspInputCompanyExce;
import com.ruoyi.system.service.IDspInputCompanyExceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导入流水Controller
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
@RestController
@RequestMapping("/system/exce")
public class DspInputCompanyExceController extends BaseController
{
    @Autowired
    private IDspInputCompanyExceService dspInputCompanyExceService;


    @Autowired
    private IDspIncomeService dspIncomeService;

    /**
     * 查询导入流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:exce:list')")
    @GetMapping("/list")
    public TableDataInfo list(DspInputCompanyExce dspInputCompanyExce)
    {
        startPage();
        List<DspInputCompanyExce> list = dspInputCompanyExceService.selectDspInputCompanyExceList(dspInputCompanyExce);
        return getDataTable(list);
    }



    /**
     * 获取导入流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:exce:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dspInputCompanyExceService.selectDspInputCompanyExceById(id));
    }

    /**
     * 新增导入流水
     */
    @PreAuthorize("@ss.hasPermi('system:exce:add')")
    @Log(title = "导入流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DspInputCompanyExce dspInputCompanyExce)
    {
        return toAjax(dspInputCompanyExceService.insertDspInputCompanyExce(dspInputCompanyExce));
    }

    /**
     * 修改导入流水
     */
    @PreAuthorize("@ss.hasPermi('system:exce:edit')")
    @Log(title = "导入流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DspInputCompanyExce dspInputCompanyExce)
    {
        return toAjax(dspInputCompanyExceService.updateDspInputCompanyExce(dspInputCompanyExce));
    }

    /**
     * 删除导入流水
     */
    @PreAuthorize("@ss.hasPermi('system:exce:remove')")
    @Log(title = "导入流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dspInputCompanyExceService.deleteDspInputCompanyExceByIds(ids));
    }

    // 下载模板 dspIncome 这个表的模板到处
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<DspIncome> util = new ExcelUtil<DspIncome>(DspIncome.class);
        util.importTemplateExcel(response, "导入数据模板");
    }

    @Log(title = "我方收益导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<DspIncome> income = new ExcelUtil<DspIncome>(DspIncome.class);
        List<DspIncome> DspIncomeList = income.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dspInputCompanyExceService.importIncomeExcel(DspIncomeList, updateSupport, operName);
        return success(message);

    }


}

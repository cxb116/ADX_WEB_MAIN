package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DspIncome;

/**
 * 预算收益导入Service接口
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
public interface IDspIncomeService 
{

    // exce 导入收益
    public  String  importIncomeExcel(List<DspIncome> dspIncomeList, boolean updateSupport, String operName);


    /**
     * 查询预算收益导入
     * 
     * @param id 预算收益导入主键
     * @return 预算收益导入
     */
    public DspIncome selectDspIncomeById(Long id);

    /**
     * 查询预算收益导入列表
     * 
     * @param dspIncome 预算收益导入
     * @return 预算收益导入集合
     */
    public List<DspIncome> selectDspIncomeList(DspIncome dspIncome);

    /**
     * 新增预算收益导入
     * 
     * @param dspIncome 预算收益导入
     * @return 结果
     */
    public int insertDspIncome(DspIncome dspIncome);

    /**
     * 修改预算收益导入
     * 
     * @param dspIncome 预算收益导入
     * @return 结果
     */
    public int updateDspIncome(DspIncome dspIncome);

    /**
     * 批量删除预算收益导入
     * 
     * @param ids 需要删除的预算收益导入主键集合
     * @return 结果
     */
    public int deleteDspIncomeByIds(Long[] ids);

    /**
     * 删除预算收益导入信息
     * 
     * @param id 预算收益导入主键
     * @return 结果
     */
    public int deleteDspIncomeById(Long id);
}

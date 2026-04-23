package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DspIncome;

/**
 * 预算收益导入Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
public interface DspIncomeMapper 
{
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
     * 删除预算收益导入
     * 
     * @param id 预算收益导入主键
     * @return 结果
     */
    public int deleteDspIncomeById(Long id);

    /**
     * 批量删除预算收益导入
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDspIncomeByIds(Long[] ids);
}

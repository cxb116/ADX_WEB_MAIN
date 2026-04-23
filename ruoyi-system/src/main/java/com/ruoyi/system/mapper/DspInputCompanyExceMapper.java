package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DspInputCompanyExce;

/**
 * 导入流水Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
public interface DspInputCompanyExceMapper 
{
    /**
     * 查询导入流水
     * 
     * @param id 导入流水主键
     * @return 导入流水
     */
    public DspInputCompanyExce selectDspInputCompanyExceById(Long id);

    /**
     * 查询导入流水列表
     * 
     * @param dspInputCompanyExce 导入流水
     * @return 导入流水集合
     */
    public List<DspInputCompanyExce> selectDspInputCompanyExceList(DspInputCompanyExce dspInputCompanyExce);

    /**
     * 新增导入流水
     * 
     * @param dspInputCompanyExce 导入流水
     * @return 结果
     */
    public int insertDspInputCompanyExce(DspInputCompanyExce dspInputCompanyExce);

    /**
     * 修改导入流水
     * 
     * @param dspInputCompanyExce 导入流水
     * @return 结果
     */
    public int updateDspInputCompanyExce(DspInputCompanyExce dspInputCompanyExce);

    /**
     * 删除导入流水
     * 
     * @param id 导入流水主键
     * @return 结果
     */
    public int deleteDspInputCompanyExceById(Long id);

    /**
     * 批量删除导入流水
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDspInputCompanyExceByIds(Long[] ids);
}

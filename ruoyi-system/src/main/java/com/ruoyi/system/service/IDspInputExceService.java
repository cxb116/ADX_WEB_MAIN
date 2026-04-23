package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DspInputExce;

/**
 * 收益导入exceService接口
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
public interface IDspInputExceService 
{
    /**
     * 查询收益导入exce
     * 
     * @param id 收益导入exce主键
     * @return 收益导入exce
     */
    public DspInputExce selectDspInputExceById(Long id);

    /**
     * 查询收益导入exce列表
     * 
     * @param dspInputExce 收益导入exce
     * @return 收益导入exce集合
     */
    public List<DspInputExce> selectDspInputExceList(DspInputExce dspInputExce);

    /**
     * 新增收益导入exce
     * 
     * @param dspInputExce 收益导入exce
     * @return 结果
     */
    public int insertDspInputExce(DspInputExce dspInputExce);

    /**
     * 修改收益导入exce
     * 
     * @param dspInputExce 收益导入exce
     * @return 结果
     */
    public int updateDspInputExce(DspInputExce dspInputExce);

    /**
     * 批量删除收益导入exce
     * 
     * @param ids 需要删除的收益导入exce主键集合
     * @return 结果
     */
    public int deleteDspInputExceByIds(Long[] ids);

    /**
     * 删除收益导入exce信息
     * 
     * @param id 收益导入exce主键
     * @return 结果
     */
    public int deleteDspInputExceById(Long id);
}

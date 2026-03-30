package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DspDataCorrected;

/**
 * 支出修正Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-22
 */
public interface DspDataCorrectedMapper 
{
    /**
     * 查询支出修正
     * 
     * @param id 支出修正主键
     * @return 支出修正
     */
    public DspDataCorrected selectDspDataCorrectedById(Long id);

    /**
     * 查询支出修正列表
     * 
     * @param dspDataCorrected 支出修正
     * @return 支出修正集合
     */
    public List<DspDataCorrected> selectDspDataCorrectedList(DspDataCorrected dspDataCorrected);

    /**
     * 新增支出修正
     * 
     * @param dspDataCorrected 支出修正
     * @return 结果
     */
    public int insertDspDataCorrected(DspDataCorrected dspDataCorrected);

    /**
     * 修改支出修正
     * 
     * @param dspDataCorrected 支出修正
     * @return 结果
     */
    public int updateDspDataCorrected(DspDataCorrected dspDataCorrected);

    /**
     * 删除支出修正
     * 
     * @param id 支出修正主键
     * @return 结果
     */
    public int deleteDspDataCorrectedById(Long id);

    /**
     * 批量删除支出修正
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDspDataCorrectedByIds(Long[] ids);
}

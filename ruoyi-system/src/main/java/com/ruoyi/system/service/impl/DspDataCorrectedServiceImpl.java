package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspDataCorrectedMapper;
import com.ruoyi.system.domain.DspDataCorrected;
import com.ruoyi.system.service.IDspDataCorrectedService;

/**
 * 支出修正Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-22
 */
@Service
public class DspDataCorrectedServiceImpl implements IDspDataCorrectedService 
{
    @Autowired
    private DspDataCorrectedMapper dspDataCorrectedMapper;

    /**
     * 查询支出修正
     * 
     * @param id 支出修正主键
     * @return 支出修正
     */
    @Override
    public DspDataCorrected selectDspDataCorrectedById(Long id)
    {
        return dspDataCorrectedMapper.selectDspDataCorrectedById(id);
    }

    /**
     * 查询支出修正列表
     * 
     * @param dspDataCorrected 支出修正
     * @return 支出修正
     */
    @Override
    public List<DspDataCorrected> selectDspDataCorrectedList(DspDataCorrected dspDataCorrected)
    {
        return dspDataCorrectedMapper.selectDspDataCorrectedList(dspDataCorrected);
    }

    /**
     * 新增支出修正
     * 
     * @param dspDataCorrected 支出修正
     * @return 结果
     */
    @Override
    public int insertDspDataCorrected(DspDataCorrected dspDataCorrected)
    {
        dspDataCorrected.setCreateTime(DateUtils.getNowDate());
        return dspDataCorrectedMapper.insertDspDataCorrected(dspDataCorrected);
    }

    /**
     * 修改支出修正
     * 
     * @param dspDataCorrected 支出修正
     * @return 结果
     */
    @Override
    public int updateDspDataCorrected(DspDataCorrected dspDataCorrected)
    {
        dspDataCorrected.setUpdateTime(DateUtils.getNowDate());
        return dspDataCorrectedMapper.updateDspDataCorrected(dspDataCorrected);
    }

    /**
     * 批量删除支出修正
     * 
     * @param ids 需要删除的支出修正主键
     * @return 结果
     */
    @Override
    public int deleteDspDataCorrectedByIds(Long[] ids)
    {
        return dspDataCorrectedMapper.deleteDspDataCorrectedByIds(ids);
    }

    /**
     * 删除支出修正信息
     * 
     * @param id 支出修正主键
     * @return 结果
     */
    @Override
    public int deleteDspDataCorrectedById(Long id)
    {
        return dspDataCorrectedMapper.deleteDspDataCorrectedById(id);
    }
}

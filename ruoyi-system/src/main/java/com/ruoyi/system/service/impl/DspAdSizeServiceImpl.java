package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspAdSizeMapper;
import com.ruoyi.system.domain.DspAdSize;
import com.ruoyi.system.service.IDspAdSizeService;

/**
 * 样式尺寸Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
@Service
public class DspAdSizeServiceImpl implements IDspAdSizeService 
{
    @Autowired
    private DspAdSizeMapper dspAdSizeMapper;

    /**
     * 查询样式尺寸
     * 
     * @param id 样式尺寸主键
     * @return 样式尺寸
     */
    @Override
    public DspAdSize selectDspAdSizeById(Long id)
    {
        return dspAdSizeMapper.selectDspAdSizeById(id);
    }

    /**
     * 查询样式尺寸列表
     * 
     * @param dspAdSize 样式尺寸
     * @return 样式尺寸
     */
    @Override
    public List<DspAdSize> selectDspAdSizeList(DspAdSize dspAdSize)
    {
        return dspAdSizeMapper.selectDspAdSizeList(dspAdSize);
    }

    /**
     * 新增样式尺寸
     * 
     * @param dspAdSize 样式尺寸
     * @return 结果
     */
    @Override
    public int insertDspAdSize(DspAdSize dspAdSize)
    {
        dspAdSize.setCreateTime(DateUtils.getNowDate());
        return dspAdSizeMapper.insertDspAdSize(dspAdSize);
    }

    /**
     * 修改样式尺寸
     * 
     * @param dspAdSize 样式尺寸
     * @return 结果
     */
    @Override
    public int updateDspAdSize(DspAdSize dspAdSize)
    {
        dspAdSize.setUpdateTime(DateUtils.getNowDate());
        return dspAdSizeMapper.updateDspAdSize(dspAdSize);
    }

    /**
     * 批量删除样式尺寸
     * 
     * @param ids 需要删除的样式尺寸主键
     * @return 结果
     */
    @Override
    public int deleteDspAdSizeByIds(Long[] ids)
    {
        return dspAdSizeMapper.deleteDspAdSizeByIds(ids);
    }

    /**
     * 删除样式尺寸信息
     * 
     * @param id 样式尺寸主键
     * @return 结果
     */
    @Override
    public int deleteDspAdSizeById(Long id)
    {
        return dspAdSizeMapper.deleteDspAdSizeById(id);
    }
}

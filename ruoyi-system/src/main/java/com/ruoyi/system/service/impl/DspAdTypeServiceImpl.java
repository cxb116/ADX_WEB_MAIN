package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspAdTypeMapper;
import com.ruoyi.system.domain.DspAdType;
import com.ruoyi.system.service.IDspAdTypeService;

/**
 * 广告类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
@Service
public class DspAdTypeServiceImpl implements IDspAdTypeService 
{
    @Autowired
    private DspAdTypeMapper dspAdTypeMapper;

    /**
     * 查询广告类型
     * 
     * @param id 广告类型主键
     * @return 广告类型
     */
    @Override
    public DspAdType selectDspAdTypeById(Long id)
    {
        return dspAdTypeMapper.selectDspAdTypeById(id);
    }

    /**
     * 查询广告类型列表
     * 
     * @param dspAdType 广告类型
     * @return 广告类型
     */
    @Override
    public List<DspAdType> selectDspAdTypeList(DspAdType dspAdType)
    {
        return dspAdTypeMapper.selectDspAdTypeList(dspAdType);
    }

    /**
     * 新增广告类型
     * 
     * @param dspAdType 广告类型
     * @return 结果
     */
    @Override
    public int insertDspAdType(DspAdType dspAdType)
    {
        dspAdType.setCreateTime(DateUtils.getNowDate());
        return dspAdTypeMapper.insertDspAdType(dspAdType);
    }

    /**
     * 修改广告类型
     * 
     * @param dspAdType 广告类型
     * @return 结果
     */
    @Override
    public int updateDspAdType(DspAdType dspAdType)
    {
        dspAdType.setUpdateTime(DateUtils.getNowDate());
        return dspAdTypeMapper.updateDspAdType(dspAdType);
    }

    /**
     * 批量删除广告类型
     * 
     * @param ids 需要删除的广告类型主键
     * @return 结果
     */
    @Override
    public int deleteDspAdTypeByIds(Long[] ids)
    {
        return dspAdTypeMapper.deleteDspAdTypeByIds(ids);
    }

    /**
     * 删除广告类型信息
     *
     * @param id 广告类型主键
     * @return 结果
     */
    @Override
    public int deleteDspAdTypeById(Long id)
    {
        return dspAdTypeMapper.deleteDspAdTypeById(id);
    }

    /**
     * 查询广告类型分布统计数据
     *
     * @return 广告类型分布数据
     */
    @Override
    public List<Map<String, Object>> selectAdTypeDistribution()
    {
        return dspAdTypeMapper.selectAdTypeDistribution();
    }
}

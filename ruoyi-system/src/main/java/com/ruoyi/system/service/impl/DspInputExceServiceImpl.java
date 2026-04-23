package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspInputExceMapper;
import com.ruoyi.system.domain.DspInputExce;
import com.ruoyi.system.service.IDspInputExceService;

/**
 * 收益导入exceService业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
@Service
public class DspInputExceServiceImpl implements IDspInputExceService 
{
    @Autowired
    private DspInputExceMapper dspInputExceMapper;

    /**
     * 查询收益导入exce
     * 
     * @param id 收益导入exce主键
     * @return 收益导入exce
     */
    @Override
    public DspInputExce selectDspInputExceById(Long id)
    {
        return dspInputExceMapper.selectDspInputExceById(id);
    }

    /**
     * 查询收益导入exce列表
     * 
     * @param dspInputExce 收益导入exce
     * @return 收益导入exce
     */
    @Override
    public List<DspInputExce> selectDspInputExceList(DspInputExce dspInputExce)
    {
        return dspInputExceMapper.selectDspInputExceList(dspInputExce);
    }

    /**
     * 新增收益导入exce
     * 
     * @param dspInputExce 收益导入exce
     * @return 结果
     */
    @Override
    public int insertDspInputExce(DspInputExce dspInputExce)
    {
        dspInputExce.setCreateTime(DateUtils.getNowDate());
        return dspInputExceMapper.insertDspInputExce(dspInputExce);
    }

    /**
     * 修改收益导入exce
     * 
     * @param dspInputExce 收益导入exce
     * @return 结果
     */
    @Override
    public int updateDspInputExce(DspInputExce dspInputExce)
    {
        dspInputExce.setUpdateTime(DateUtils.getNowDate());
        return dspInputExceMapper.updateDspInputExce(dspInputExce);
    }

    /**
     * 批量删除收益导入exce
     * 
     * @param ids 需要删除的收益导入exce主键
     * @return 结果
     */
    @Override
    public int deleteDspInputExceByIds(Long[] ids)
    {
        return dspInputExceMapper.deleteDspInputExceByIds(ids);
    }

    /**
     * 删除收益导入exce信息
     * 
     * @param id 收益导入exce主键
     * @return 结果
     */
    @Override
    public int deleteDspInputExceById(Long id)
    {
        return dspInputExceMapper.deleteDspInputExceById(id);
    }
}

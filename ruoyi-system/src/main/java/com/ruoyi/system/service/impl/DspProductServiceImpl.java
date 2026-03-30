package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspProductMapper;
import com.ruoyi.system.domain.DspProduct;
import com.ruoyi.system.service.IDspProductService;

/**
 * 产品管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@Service
public class DspProductServiceImpl implements IDspProductService 
{
    @Autowired
    private DspProductMapper dspProductMapper;

    /**
     * 查询产品管理
     * 
     * @param id 产品管理主键
     * @return 产品管理
     */
    @Override
    public DspProduct selectDspProductById(Long id)
    {
        return dspProductMapper.selectDspProductById(id);
    }

    /**
     * 查询产品管理列表
     * 
     * @param dspProduct 产品管理
     * @return 产品管理
     */
    @Override
    public List<DspProduct> selectDspProductList(DspProduct dspProduct)
    {
        return dspProductMapper.selectDspProductList(dspProduct);
    }

    /**
     * 新增产品管理
     * 
     * @param dspProduct 产品管理
     * @return 结果
     */
    @Override
    public int insertDspProduct(DspProduct dspProduct)
    {
        dspProduct.setCreateTime(DateUtils.getNowDate());
        return dspProductMapper.insertDspProduct(dspProduct);
    }

    /**
     * 修改产品管理
     * 
     * @param dspProduct 产品管理
     * @return 结果
     */
    @Override
    public int updateDspProduct(DspProduct dspProduct)
    {
        dspProduct.setUpdateTime(DateUtils.getNowDate());
        return dspProductMapper.updateDspProduct(dspProduct);
    }

    /**
     * 批量删除产品管理
     * 
     * @param ids 需要删除的产品管理主键
     * @return 结果
     */
    @Override
    public int deleteDspProductByIds(Long[] ids)
    {
        return dspProductMapper.deleteDspProductByIds(ids);
    }

    /**
     * 删除产品管理信息
     * 
     * @param id 产品管理主键
     * @return 结果
     */
    @Override
    public int deleteDspProductById(Long id)
    {
        return dspProductMapper.deleteDspProductById(id);
    }
}

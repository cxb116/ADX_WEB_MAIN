package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DspProduct;

/**
 * 产品管理Service接口
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public interface IDspProductService 
{
    /**
     * 查询产品管理
     * 
     * @param id 产品管理主键
     * @return 产品管理
     */
    public DspProduct selectDspProductById(Long id);

    /**
     * 查询产品管理列表
     * 
     * @param dspProduct 产品管理
     * @return 产品管理集合
     */
    public List<DspProduct> selectDspProductList(DspProduct dspProduct);

    /**
     * 新增产品管理
     * 
     * @param dspProduct 产品管理
     * @return 结果
     */
    public int insertDspProduct(DspProduct dspProduct);

    /**
     * 修改产品管理
     * 
     * @param dspProduct 产品管理
     * @return 结果
     */
    public int updateDspProduct(DspProduct dspProduct);

    /**
     * 批量删除产品管理
     * 
     * @param ids 需要删除的产品管理主键集合
     * @return 结果
     */
    public int deleteDspProductByIds(Long[] ids);

    /**
     * 删除产品管理信息
     * 
     * @param id 产品管理主键
     * @return 结果
     */
    public int deleteDspProductById(Long id);
}

package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DspAdSize;

/**
 * 样式尺寸Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
public interface DspAdSizeMapper 
{
    /**
     * 查询样式尺寸
     * 
     * @param id 样式尺寸主键
     * @return 样式尺寸
     */
    public DspAdSize selectDspAdSizeById(Long id);

    /**
     * 查询样式尺寸列表
     * 
     * @param dspAdSize 样式尺寸
     * @return 样式尺寸集合
     */
    public List<DspAdSize> selectDspAdSizeList(DspAdSize dspAdSize);

    /**
     * 新增样式尺寸
     * 
     * @param dspAdSize 样式尺寸
     * @return 结果
     */
    public int insertDspAdSize(DspAdSize dspAdSize);

    /**
     * 修改样式尺寸
     * 
     * @param dspAdSize 样式尺寸
     * @return 结果
     */
    public int updateDspAdSize(DspAdSize dspAdSize);

    /**
     * 删除样式尺寸
     * 
     * @param id 样式尺寸主键
     * @return 结果
     */
    public int deleteDspAdSizeById(Long id);

    /**
     * 批量删除样式尺寸
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDspAdSizeByIds(Long[] ids);
}

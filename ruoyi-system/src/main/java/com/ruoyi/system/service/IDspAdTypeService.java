package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.DspAdType;

/**
 * 广告类型Service接口
 *
 * @author ruoyi
 * @date 2026-02-28
 */
public interface IDspAdTypeService
{
    /**
     * 查询广告类型
     *
     * @param id 广告类型主键
     * @return 广告类型
     */
    public DspAdType selectDspAdTypeById(Long id);

    /**
     * 查询广告类型列表
     *
     * @param dspAdType 广告类型
     * @return 广告类型集合
     */
    public List<DspAdType> selectDspAdTypeList(DspAdType dspAdType);

    /**
     * 新增广告类型
     *
     * @param dspAdType 广告类型
     * @return 结果
     */
    public int insertDspAdType(DspAdType dspAdType);

    /**
     * 修改广告类型
     *
     * @param dspAdType 广告类型
     * @return 结果
     */
    public int updateDspAdType(DspAdType dspAdType);

    /**
     * 批量删除广告类型
     *
     * @param ids 需要删除的广告类型主键集合
     * @return 结果
     */
    public int deleteDspAdTypeByIds(Long[] ids);

    /**
     * 删除广告类型信息
     *
     * @param id 广告类型主键
     * @return 结果
     */
    public int deleteDspAdTypeById(Long id);

    /**
     * 查询广告类型分布统计数据
     *
     * @return 广告类型分布数据
     */
    public List<Map<String, Object>> selectAdTypeDistribution();
}

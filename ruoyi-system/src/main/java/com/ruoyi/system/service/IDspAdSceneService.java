package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DspAdScene;

/**
 * 广告场景Service接口
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
public interface IDspAdSceneService 
{
    /**
     * 查询广告场景
     * 
     * @param id 广告场景主键
     * @return 广告场景
     */
    public DspAdScene selectDspAdSceneById(Long id);

    /**
     * 查询广告场景列表
     * 
     * @param dspAdScene 广告场景
     * @return 广告场景集合
     */
    public List<DspAdScene> selectDspAdSceneList(DspAdScene dspAdScene);

    /**
     * 新增广告场景
     * 
     * @param dspAdScene 广告场景
     * @return 结果
     */
    public int insertDspAdScene(DspAdScene dspAdScene);

    /**
     * 修改广告场景
     * 
     * @param dspAdScene 广告场景
     * @return 结果
     */
    public int updateDspAdScene(DspAdScene dspAdScene);

    /**
     * 批量删除广告场景
     * 
     * @param ids 需要删除的广告场景主键集合
     * @return 结果
     */
    public int deleteDspAdSceneByIds(Long[] ids);

    /**
     * 删除广告场景信息
     * 
     * @param id 广告场景主键
     * @return 结果
     */
    public int deleteDspAdSceneById(Long id);
}

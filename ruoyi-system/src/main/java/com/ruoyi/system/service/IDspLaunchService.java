package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DspLaunch;
import com.ruoyi.system.domain.DspLaunchDTO;

/**
 * 投放配置Service接口
 *
 * @author ruoyi
 * @date 2026-02-25
 */
public interface IDspLaunchService
{
    /**
     * 查询投放配置
     *
     * @param id 投放配置主键
     * @return 投放配置
     */
    public DspLaunch selectDspLaunchById(Long id);

    /**
     * 查询投放配置列表
     *
     * @param dspLaunch 投放配置
     * @return 投放配置集合
     */
    public List<DspLaunch> selectDspLaunchList(DspLaunch dspLaunch);

    /**
     * 根据媒体广告位ID查询投放配置列表
     *
     * @param sspSlotId 媒体广告位ID
     * @return 投放配置集合
     */
    public List<DspLaunch> selectDspLaunchBySspSlotId(Long sspSlotId);

    /**
     * 根据媒体广告位ID查询投放配置DTO列表（包含预算方广告位信息）
     *
     * @param sspSlotId 媒体广告位ID
     * @return 投放配置DTO集合
     */
    public List<DspLaunchDTO> selectDspLaunchDTOBySspSlotId(Long sspSlotId);

    /**
     * 新增投放配置
     *
     * @param dspLaunch 投放配置
     * @return 结果
     */
    public int insertDspLaunch(DspLaunch dspLaunch);

    /**
     * 批量保存投放配置（先删除旧的，再插入新的）
     *
     * @param sspSlotId 媒体广告位ID
     * @param dspLaunchList 投放配置集合
     * @return 结果
     */
    public int batchSaveDspLaunch(Long sspSlotId, List<DspLaunch> dspLaunchList);

    /**
     * 修改投放配置
     *
     * @param dspLaunch 投放配置
     * @return 结果
     */
    public int updateDspLaunch(DspLaunch dspLaunch);

    /**
     * 批量删除投放配置
     *
     * @param ids 需要删除的投放配置主键集合
     * @return 结果
     */
    public int deleteDspLaunchByIds(Long[] ids);

    /**
     * 删除投放配置信息
     *
     * @param id 投放配置主键
     * @return 结果
     */
    public int deleteDspLaunchById(Long id);

    /**
     * 根据媒体广告位ID删除投放配置
     *
     * @param sspSlotId 媒体广告位ID
     * @return 结果
     */
    public int deleteDspLaunchBySspSlotId(Long sspSlotId);
}

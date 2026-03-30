package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DspSlotInfo;

/**
 * 预算广告位Service接口
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public interface IDspSlotInfoService 
{
    /**
     * 查询预算广告位
     * 
     * @param id 预算广告位主键
     * @return 预算广告位
     */
    public DspSlotInfo selectDspSlotInfoById(Long id);

    /**
     * 查询预算广告位列表
     * 
     * @param dspSlotInfo 预算广告位
     * @return 预算广告位集合
     */
    public List<DspSlotInfo> selectDspSlotInfoList(DspSlotInfo dspSlotInfo);

    /**
     * 新增预算广告位
     * 
     * @param dspSlotInfo 预算广告位
     * @return 结果
     */
    public int insertDspSlotInfo(DspSlotInfo dspSlotInfo);

    /**
     * 修改预算广告位
     * 
     * @param dspSlotInfo 预算广告位
     * @return 结果
     */
    public int updateDspSlotInfo(DspSlotInfo dspSlotInfo);

    /**
     * 批量删除预算广告位
     * 
     * @param ids 需要删除的预算广告位主键集合
     * @return 结果
     */
    public int deleteDspSlotInfoByIds(Long[] ids);

    /**
     * 删除预算广告位信息
     * 
     * @param id 预算广告位主键
     * @return 结果
     */
    public int deleteDspSlotInfoById(Long id);
}

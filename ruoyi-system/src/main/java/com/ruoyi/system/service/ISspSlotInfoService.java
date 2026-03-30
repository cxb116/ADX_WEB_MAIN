package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SspSlotInfo;
import com.ruoyi.system.domain.DspSlotInfo;

/**
 * 媒体广告位Service接口
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public interface ISspSlotInfoService 
{
    /**
     * 查询媒体广告位
     * 
     * @param id 媒体广告位主键
     * @return 媒体广告位
     */
    public SspSlotInfo selectSspSlotInfoById(Long id);

    /**
     * 查询媒体广告位列表
     * 
     * @param sspSlotInfo 媒体广告位
     * @return 媒体广告位集合
     */
    public List<SspSlotInfo> selectSspSlotInfoList(SspSlotInfo sspSlotInfo);

    /**
     * 新增媒体广告位
     * 
     * @param sspSlotInfo 媒体广告位
     * @return 结果
     */
    public int insertSspSlotInfo(SspSlotInfo sspSlotInfo);

    /**
     * 修改媒体广告位
     * 
     * @param sspSlotInfo 媒体广告位
     * @return 结果
     */
    public int updateSspSlotInfo(SspSlotInfo sspSlotInfo);

    /**
     * 批量删除媒体广告位
     * 
     * @param ids 需要删除的媒体广告位主键集合
     * @return 结果
     */
    public int deleteSspSlotInfoByIds(Long[] ids);

    /**
     * 删除媒体广告位信息
     *
     * @param id 媒体广告位主键
     * @return 结果
     */
    public int deleteSspSlotInfoById(Long id);

    /**
     * 根据ssp广告位id查询匹配的预算方广告位列表
     *
     * @param sspSlotId ssp广告位主键
     * @return 匹配的预算方广告位集合
     */
    public List<DspSlotInfo> selectMatchedDspSlotInfo(Long sspSlotId);
}

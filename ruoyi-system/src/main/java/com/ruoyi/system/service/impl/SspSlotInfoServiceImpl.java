package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.core.etcd.EtcdTemplate;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SspSlotInfoMapper;
import com.ruoyi.system.domain.SspSlotInfo;
import com.ruoyi.system.domain.DspSlotInfo;
import com.ruoyi.system.service.ISspSlotInfoService;

/**
 * 媒体广告位Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@Service
public class SspSlotInfoServiceImpl implements ISspSlotInfoService
{
    @Autowired
    private SspSlotInfoMapper sspSlotInfoMapper;

    @Autowired(required = false)
    private EtcdTemplate etcdTemplate;

    /**
     * 查询媒体广告位
     * 
     * @param id 媒体广告位主键
     * @return 媒体广告位
     */
    @Override
    public SspSlotInfo selectSspSlotInfoById(Long id)
    {
        return sspSlotInfoMapper.selectSspSlotInfoById(id);
    }

    /**
     * 查询媒体广告位列表
     * 
     * @param sspSlotInfo 媒体广告位
     * @return 媒体广告位
     */
    @Override
    public List<SspSlotInfo> selectSspSlotInfoList(SspSlotInfo sspSlotInfo)
    {
        return sspSlotInfoMapper.selectSspSlotInfoList(sspSlotInfo);
    }

    /**
     * 新增媒体广告位
     * 
     * @param sspSlotInfo 媒体广告位
     * @return 结果
     */
    @Override
    public int insertSspSlotInfo(SspSlotInfo sspSlotInfo)
    {
        sspSlotInfo.setCreateTime(DateUtils.getNowDate());
        int rows = sspSlotInfoMapper.insertSspSlotInfo(sspSlotInfo);

        // 数据库插入成功后同步到 etcd
        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncAdd("sspslot", sspSlotInfo.getId(), sspSlotInfo);
            }
            catch (Exception e)
            {
                // 仅记录日志，不影响返回结果
            }
        }

        return rows;
    }

    /**
     * 修改媒体广告位
     * 
     * @param sspSlotInfo 媒体广告位
     * @return 结果
     */
    @Override
    public int updateSspSlotInfo(SspSlotInfo sspSlotInfo)
    {
        sspSlotInfo.setUpdateTime(DateUtils.getNowDate());
        int rows = sspSlotInfoMapper.updateSspSlotInfo(sspSlotInfo);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                // 查询最新数据
                SspSlotInfo latest = sspSlotInfoMapper.selectSspSlotInfoById(sspSlotInfo.getId());
                etcdTemplate.syncUpdate("sspslot", latest.getId(), latest);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }

    /**
     * 批量删除媒体广告位
     * 
     * @param ids 需要删除的媒体广告位主键
     * @return 结果
     */
    @Override
    public int deleteSspSlotInfoByIds(Long[] ids)
    {
        int rows = sspSlotInfoMapper.deleteSspSlotInfoByIds(ids);

        if (rows > 0 && etcdTemplate != null)
        {
            for (Long id : ids)
            {
                try
                {
                    etcdTemplate.syncDelete("sspslot", id);
                }
                catch (Exception e)
                {
                    // 仅记录日志
                }
            }
        }

        return rows;
    }

    /**
     * 删除媒体广告位信息
     *
     * @param id 媒体广告位主键
     * @return 结果
     */
    @Override
    public int deleteSspSlotInfoById(Long id)
    {
        int rows = sspSlotInfoMapper.deleteSspSlotInfoById(id);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncDelete("sspslot", id);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }

    /**
     * 根据ssp广告位id查询匹配的预算方广告位列表
     *
     * @param sspSlotId ssp广告位主键
     * @return 匹配的预算方广告位集合
     */
    @Override
    public List<DspSlotInfo> selectMatchedDspSlotInfo(Long sspSlotId)
    {
        return sspSlotInfoMapper.selectMatchedDspSlotInfo(sspSlotId);
    }
}

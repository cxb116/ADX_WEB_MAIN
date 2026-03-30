package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import com.ruoyi.common.core.etcd.EtcdTemplate;
import com.ruoyi.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.DspLaunchMapper;
import com.ruoyi.system.domain.DspLaunch;
import com.ruoyi.system.domain.DspLaunchDTO;
import com.ruoyi.system.service.IDspLaunchService;

/**
 * 投放配置Service业务层处理
 *
 * @author ruoyi
 * @date 2026-02-25
 */
@Service
public class DspLaunchServiceImpl implements IDspLaunchService
{
    private static final Logger logger = LoggerFactory.getLogger(DspLaunchServiceImpl.class);

    @Autowired
    private DspLaunchMapper dspLaunchMapper;

    @Autowired(required = false)
    private EtcdTemplate etcdTemplate;

    /**
     * 查询投放配置
     *
     * @param id 投放配置主键
     * @return 投放配置
     */
    @Override
    public DspLaunch selectDspLaunchById(Long id)
    {
        return dspLaunchMapper.selectDspLaunchById(id);
    }

    /**
     * 查询投放配置列表
     *
     * @param dspLaunch 投放配置
     * @return 投放配置
     */
    @Override
    public List<DspLaunch> selectDspLaunchList(DspLaunch dspLaunch)
    {
        return dspLaunchMapper.selectDspLaunchList(dspLaunch);
    }

    /**
     * 根据媒体广告位ID查询投放配置列表
     *
     * @param sspSlotId 媒体广告位ID
     * @return 投放配置集合
     */
    @Override
    public List<DspLaunch> selectDspLaunchBySspSlotId(Long sspSlotId)
    {
        return dspLaunchMapper.selectDspLaunchBySspSlotId(sspSlotId);
    }

    /**
     * 根据媒体广告位ID查询投放配置DTO列表（包含预算方广告位信息）
     *
     * @param sspSlotId 媒体广告位ID
     * @return 投放配置DTO集合
     */
    @Override
    public List<DspLaunchDTO> selectDspLaunchDTOBySspSlotId(Long sspSlotId)
    {
        return dspLaunchMapper.selectDspLaunchDTOBySspSlotId(sspSlotId);
    }

    /**
     * 新增投放配置
     *
     * @param dspLaunch 投放配置
     * @return 结果
     */
    @Override
    public int insertDspLaunch(DspLaunch dspLaunch)
    {
        dspLaunch.setCreateTime(DateUtils.getNowDate());
        int rows = dspLaunchMapper.insertDspLaunch(dspLaunch);

        // 数据库插入成功后同步到 etcd
        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncAdd("launch", dspLaunch.getId(), dspLaunch);
            }
            catch (Exception e)
            {
                // 仅记录日志，不影响返回结果
            }
        }

        return rows;
    }

    /**
     * 批量保存投放配置（先删除旧的，再插入新的）
     *
     * @param sspSlotId 媒体广告位ID
     * @param dspLaunchList 投放配置集合
     * @return 结果
     */
    @Override
    @Transactional
    public int batchSaveDspLaunch(Long sspSlotId, List<DspLaunch> dspLaunchList)
    {
        logger.info("========== 批量保存投放配置开始 ==========");
        logger.info("媒体广告位ID: {}", sspSlotId);
        logger.info("前端传来的配置数量: {}", dspLaunchList != null ? dspLaunchList.size() : 0);

        // 1. 先查询该媒体广告位的所有旧配置
        List<DspLaunch> oldLaunchList = dspLaunchMapper.selectDspLaunchBySspSlotId(sspSlotId);
        logger.info("数据库中已存在的配置数量: {}", oldLaunchList != null ? oldLaunchList.size() : 0);

        int rows = 0;
        java.util.Date nowDate = DateUtils.getNowDate();
        List<Long> processedDspSlotIds = new java.util.ArrayList<>();

        // 2. 遍历前端传来的配置列表，逐个处理（更新或插入）
        if (dspLaunchList != null && !dspLaunchList.isEmpty())
        {
            for (DspLaunch dspLaunch : dspLaunchList)
            {
                dspLaunch.setSspSlotId(sspSlotId);
                dspLaunch.setUpdateTime(nowDate);

                // 查找是否已存在该配置（通过 ssp_slot_id 和 dsp_slot_id 判断）
                DspLaunch existingLaunch = null;
                if (oldLaunchList != null)
                {
                    for (DspLaunch old : oldLaunchList)
                    {
                        if (old.getDspSlotId().equals(dspLaunch.getDspSlotId()))
                        {
                            existingLaunch = old;
                            break;
                        }
                    }
                }

                if (existingLaunch != null)
                {
                    // 存在则更新
                    logger.info("更新配置 - dspSlotId: {}, ID: {}", dspLaunch.getDspSlotId(), existingLaunch.getId());
                    dspLaunch.setId(existingLaunch.getId());
                    dspLaunch.setCreateTime(existingLaunch.getCreateTime()); // 保持原创建时间
                    dspLaunchMapper.updateDspLaunch(dspLaunch);

                    // 同步更新 etcd
                    if (etcdTemplate != null)
                    {
                        try
                        {
                            etcdTemplate.syncUpdate("launch", dspLaunch.getId(), dspLaunch);
                            logger.info("✅ etcd 更新成功 - ID: {}", dspLaunch.getId());
                        }
                        catch (Exception e)
                        {
                            logger.error("❌ etcd 更新失败 - ID: {}, 错误: {}", dspLaunch.getId(), e.getMessage());
                        }
                    }
                    else
                    {
                        logger.warn("⚠️ etcdTemplate 为 null，跳过 etcd 同步");
                    }
                }
                else
                {
                    // 不存在则插入
                    logger.info("新增配置 - dspSlotId: {}", dspLaunch.getDspSlotId());
                    dspLaunch.setCreateTime(nowDate);
                    dspLaunchMapper.insertDspLaunch(dspLaunch);

                    // 重新查询获取数据库生成的自增 ID
                    List<DspLaunch> newList = dspLaunchMapper.selectDspLaunchBySspSlotId(sspSlotId);
                    for (DspLaunch newLaunch : newList)
                    {
                        if (newLaunch.getDspSlotId().equals(dspLaunch.getDspSlotId()))
                        {
                            // 同步新增到 etcd
                            if (etcdTemplate != null && newLaunch.getId() != null)
                            {
                                try
                                {
                                    etcdTemplate.syncAdd("launch", newLaunch.getId(), newLaunch);
                                    logger.info("✅ etcd 新增成功 - ID: {}", newLaunch.getId());
                                }
                                catch (Exception e)
                                {
                                    logger.error("❌ etcd 新增失败 - ID: {}, 错误: {}", newLaunch.getId(), e.getMessage());
                                }
                            }
                            break;
                        }
                    }
                }

                processedDspSlotIds.add(dspLaunch.getDspSlotId());
                rows++;
            }
        }

        // 3. 删除前端没有传的配置（用户删除的配置）
        if (oldLaunchList != null && !oldLaunchList.isEmpty())
        {
            for (DspLaunch oldLaunch : oldLaunchList)
            {
                // 如果旧配置不在前端传来的列表中，说明被用户删除了
                if (!processedDspSlotIds.contains(oldLaunch.getDspSlotId()))
                {
                    logger.info("删除配置 - dspSlotId: {}, ID: {}", oldLaunch.getDspSlotId(), oldLaunch.getId());
                    // 删除数据库记录
                    dspLaunchMapper.deleteDspLaunchById(oldLaunch.getId());

                    // 同步删除 etcd
                    if (etcdTemplate != null)
                    {
                        try
                        {
                            etcdTemplate.syncDelete("launch", oldLaunch.getId());
                            logger.info("✅ etcd 删除成功 - ID: {}", oldLaunch.getId());
                        }
                        catch (Exception e)
                        {
                            logger.error("❌ etcd 删除失败 - ID: {}, 错误: {}", oldLaunch.getId(), e.getMessage());
                        }
                    }
                }
            }
        }

        logger.info("========== 批量保存投放配置完成 - 总数: {} ==========", rows);
        return rows;
    }

    /**
     * 修改投放配置
     *
     * @param dspLaunch 投放配置
     * @return 结果
     */
    @Override
    public int updateDspLaunch(DspLaunch dspLaunch)
    {
        dspLaunch.setUpdateTime(DateUtils.getNowDate());
        int rows = dspLaunchMapper.updateDspLaunch(dspLaunch);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                // 查询最新数据
                DspLaunch latest = dspLaunchMapper.selectDspLaunchById(dspLaunch.getId());
                etcdTemplate.syncUpdate("launch", latest.getId(), latest);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }

    /**
     * 批量删除投放配置
     *
     * @param ids 需要删除的投放配置主键集合
     * @return 结果
     */
    @Override
    public int deleteDspLaunchByIds(Long[] ids)
    {
        int rows = dspLaunchMapper.deleteDspLaunchByIds(ids);

        if (rows > 0 && etcdTemplate != null)
        {
            for (Long id : ids)
            {
                try
                {
                    etcdTemplate.syncDelete("launch", id);
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
     * 删除投放配置信息
     *
     * @param id 投放配置主键
     * @return 结果
     */
    @Override
    public int deleteDspLaunchById(Long id)
    {
        int rows = dspLaunchMapper.deleteDspLaunchById(id);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncDelete("launch", id);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }

    /**
     * 根据媒体广告位ID删除投放配置
     *
     * @param sspSlotId 媒体广告位ID
     * @return 结果
     */
    @Override
    public int deleteDspLaunchBySspSlotId(Long sspSlotId)
    {
        return dspLaunchMapper.deleteDspLaunchBySspSlotId(sspSlotId);
    }
}

package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Arrays;
import com.ruoyi.common.core.etcd.EtcdTemplate;
import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspSlotInfoMapper;
import com.ruoyi.system.mapper.DspProductMapper;
import com.ruoyi.system.domain.DspSlotInfo;
import com.ruoyi.system.domain.DspProduct;
import com.ruoyi.system.service.IDspSlotInfoService;

/**
 * 预算广告位Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@Service
public class DspSlotInfoServiceImpl implements IDspSlotInfoService
{
    private static final Logger logger = LoggerFactory.getLogger(DspSlotInfoServiceImpl.class);

    @Autowired
    private DspSlotInfoMapper dspSlotInfoMapper;

    @Autowired
    private DspProductMapper dspProductMapper;

    @Autowired(required = false)
    private EtcdTemplate etcdTemplate;

    /**
     * 查询预算广告位
     * 
     * @param id 预算广告位主键
     * @return 预算广告位
     */
    @Override
    public DspSlotInfo selectDspSlotInfoById(Long id)
    {
        return dspSlotInfoMapper.selectDspSlotInfoById(id);
    }

    /**
     * 查询预算广告位列表
     * 
     * @param dspSlotInfo 预算广告位
     * @return 预算广告位
     */
    @Override
    public List<DspSlotInfo> selectDspSlotInfoList(DspSlotInfo dspSlotInfo)
    {
        if (dspSlotInfo != null)
        {
            String[] dspSlotCodeList = dspSlotInfo.getDspSlotCodeList();
            if (dspSlotCodeList != null && dspSlotCodeList.length > 0)
            {
                dspSlotInfo.setDspSlotCode(null);
            }
            else if (StringUtils.isNotBlank(dspSlotInfo.getDspSlotCode()))
            {
                String[] parsedSlotCodeList = Arrays.stream(dspSlotInfo.getDspSlotCode().split("[\\s,，;；]+"))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .distinct()
                    .toArray(String[]::new);
                if (parsedSlotCodeList.length > 1)
                {
                    dspSlotInfo.setDspSlotCodeList(parsedSlotCodeList);
                    dspSlotInfo.setDspSlotCode(null);
                }
                else if (parsedSlotCodeList.length == 1)
                {
                    dspSlotInfo.setDspSlotCode(parsedSlotCodeList[0]);
                }
            }
        }
        return dspSlotInfoMapper.selectDspSlotInfoList(dspSlotInfo);
    }

    /**
     * 新增预算广告位
     *
     * @param dspSlotInfo 预算广告位
     * @return 结果
     */
    @Override
    public int insertDspSlotInfo(DspSlotInfo dspSlotInfo)
    {
        logger.info("========== 新增预算广告位开始 ==========");
        logger.info("广告位名称: {}", dspSlotInfo.getName());

        dspSlotInfo.setCreateTime(DateUtils.getNowDate());

        // 设置默认值：如果结算方式为空，默认为分成模式（1）
        if (dspSlotInfo.getDspPayType() == null)
        {
            dspSlotInfo.setDspPayType(1L);
        }

        int rows = dspSlotInfoMapper.insertDspSlotInfo(dspSlotInfo);

        // 数据库插入成功后，重新查询获取完整的记录（包含自增 ID）
        if (rows > 0)
        {
            // 通过唯一标识查询最新插入的记录
            DspSlotInfo query = new DspSlotInfo();
            query.setDspSlotCode(dspSlotInfo.getDspSlotCode());
            query.setCompanyId(dspSlotInfo.getCompanyId());
            query.setProductId(dspSlotInfo.getProductId());
            List<DspSlotInfo> list = dspSlotInfoMapper.selectDspSlotInfoList(query);

            if (list != null && !list.isEmpty())
            {
                // 取最新的记录（按 ID 倒序）
                DspSlotInfo latest = list.get(0);
                logger.info("✅ 数据库插入成功，获取到 ID: {}", latest.getId());

                // 查询产品名称用于 etcd 同步
                if (latest.getProductId() != null)
                {
                    DspProduct product = dspProductMapper.selectDspProductById(latest.getProductId());
                    latest.setProductName(product != null ? product.getName() : null);
                    logger.debug("设置产品信息 - productId: {}, productName: {}", latest.getProductId(), latest.getProductName());
                }

                // 同步到 etcd
                if (etcdTemplate != null && latest.getId() != null)
                {
                    try
                    {
                        etcdTemplate.syncAdd("dsp", latest.getId(), latest);
                        logger.info("✅ etcd 同步成功 - ID: {}", latest.getId());
                    }
                    catch (Exception e)
                    {
                        logger.error("❌ etcd 同步失败 - ID: {}, 错误: {}", latest.getId(), e.getMessage(), e);
                    }
                }
                else
                {
                    logger.warn("⚠️ etcdTemplate 为 null，跳过 etcd 同步");
                }
            }
        }

        logger.info("========== 新增预算广告位完成 ==========");
        return rows;
    }

    /**
     * 修改预算广告位
     *
     * @param dspSlotInfo 预算广告位
     * @return 结果
     */
    @Override
    public int updateDspSlotInfo(DspSlotInfo dspSlotInfo)
    {
        logger.info("========== 修改预算广告位开始 ==========");
        logger.info("广告位ID: {}, 名称: {}", dspSlotInfo.getId(), dspSlotInfo.getName());

        dspSlotInfo.setUpdateTime(DateUtils.getNowDate());
        int rows = dspSlotInfoMapper.updateDspSlotInfo(dspSlotInfo);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                // 查询最新数据
                DspSlotInfo latest = dspSlotInfoMapper.selectDspSlotInfoById(dspSlotInfo.getId());

                // 查询产品名称用于 etcd 同步
                if (latest.getProductId() != null)
                {
                    DspProduct product = dspProductMapper.selectDspProductById(latest.getProductId());
                    latest.setProductName(product != null ? product.getName() : null);
                    logger.debug("设置产品信息 - productId: {}, productName: {}", latest.getProductId(), latest.getProductName());
                }

                etcdTemplate.syncUpdate("dsp", latest.getId(), latest);
                logger.info("✅ etcd 更新成功 - ID: {}", latest.getId());
            }
            catch (Exception e)
            {
                logger.error("❌ etcd 更新失败 - ID: {}, 错误: {}", dspSlotInfo.getId(), e.getMessage(), e);
            }
        }
        else if (rows > 0 && etcdTemplate == null)
        {
            logger.warn("⚠️ etcdTemplate 为 null，跳过 etcd 同步");
        }

        logger.info("========== 修改预算广告位完成 ==========");
        return rows;
    }

    /**
     * 批量删除预算广告位
     * 
     * @param ids 需要删除的预算广告位主键
     * @return 结果
     */
    @Override
    public int deleteDspSlotInfoByIds(Long[] ids)
    {
        int rows = dspSlotInfoMapper.deleteDspSlotInfoByIds(ids);

        if (rows > 0 && etcdTemplate != null)
        {
            for (Long id : ids)
            {
                try
                {
                    etcdTemplate.syncDelete("dsp", id);
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
     * 删除预算广告位信息
     *
     * @param id 预算广告位主键
     * @return 结果
     */
    @Override
    public int deleteDspSlotInfoById(Long id)
    {
        int rows = dspSlotInfoMapper.deleteDspSlotInfoById(id);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncDelete("dsp", id);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }
}

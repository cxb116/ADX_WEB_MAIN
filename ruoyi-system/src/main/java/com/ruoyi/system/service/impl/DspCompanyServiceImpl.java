package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.core.etcd.EtcdTemplate;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DspCompanyMapper;
import com.ruoyi.system.domain.DspCompany;
import com.ruoyi.system.service.IDspCompanyService;

/**
 * 公司管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@Service
public class DspCompanyServiceImpl implements IDspCompanyService
{
    @Autowired
    private DspCompanyMapper dspCompanyMapper;

    @Autowired(required = false)
    private EtcdTemplate etcdTemplate;

    /**
     * 查询公司管理
     * 
     * @param id 公司管理主键
     * @return 公司管理
     */
    @Override
    public DspCompany selectDspCompanyById(Long id)
    {
        return dspCompanyMapper.selectDspCompanyById(id);
    }

    /**
     * 查询公司管理列表
     * 
     * @param dspCompany 公司管理
     * @return 公司管理
     */
    @Override
    public List<DspCompany> selectDspCompanyList(DspCompany dspCompany)
    {
        return dspCompanyMapper.selectDspCompanyList(dspCompany);
    }

    /**
     * 新增公司管理
     * 
     * @param dspCompany 公司管理
     * @return 结果
     */
    @Override
    public int insertDspCompany(DspCompany dspCompany)
    {
        dspCompany.setCreateTime(DateUtils.getNowDate());
        int rows = dspCompanyMapper.insertDspCompany(dspCompany);

        // 数据库插入成功后同步到 etcd
        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncAdd("company", dspCompany.getId(), dspCompany);
            }
            catch (Exception e)
            {
                // 仅记录日志，不影响返回结果
            }
        }

        return rows;
    }

    /**
     * 修改公司管理
     *
     * @param dspCompany 公司管理
     * @return 结果
     */
    @Override
    public int updateDspCompany(DspCompany dspCompany)
    {
        dspCompany.setUpdateTime(DateUtils.getNowDate());
        int rows = dspCompanyMapper.updateDspCompany(dspCompany);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                // 查询最新数据
                DspCompany latest = dspCompanyMapper.selectDspCompanyById(dspCompany.getId());
                etcdTemplate.syncUpdate("company", latest.getId(), latest);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }

    /**
     * 批量删除公司管理
     *
     * @param ids 需要删除的公司管理主键
     * @return 结果
     */
    @Override
    public int deleteDspCompanyByIds(Long[] ids)
    {
        int rows = dspCompanyMapper.deleteDspCompanyByIds(ids);

        if (rows > 0 && etcdTemplate != null)
        {
            for (Long id : ids)
            {
                try
                {
                    etcdTemplate.syncDelete("company", id);
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
     * 删除公司管理信息
     *
     * @param id 公司管理主键
     * @return 结果
     */
    @Override
    public int deleteDspCompanyById(Long id)
    {
        int rows = dspCompanyMapper.deleteDspCompanyById(id);

        if (rows > 0 && etcdTemplate != null)
        {
            try
            {
                etcdTemplate.syncDelete("company", id);
            }
            catch (Exception e)
            {
                // 仅记录日志
            }
        }

        return rows;
    }
}

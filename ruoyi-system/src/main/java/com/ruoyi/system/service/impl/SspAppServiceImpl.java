package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SspAppMapper;
import com.ruoyi.system.domain.SspApp;
import com.ruoyi.system.service.ISspAppService;

/**
 * 应用管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@Service
public class SspAppServiceImpl implements ISspAppService 
{
    @Autowired
    private SspAppMapper sspAppMapper;

    /**
     * 查询应用管理
     * 
     * @param id 应用管理主键
     * @return 应用管理
     */
    @Override
    public SspApp selectSspAppById(Long id)
    {
        return sspAppMapper.selectSspAppById(id);
    }

    /**
     * 查询应用管理列表
     * 
     * @param sspApp 应用管理
     * @return 应用管理
     */
    @Override
    public List<SspApp> selectSspAppList(SspApp sspApp)
    {
        return sspAppMapper.selectSspAppList(sspApp);
    }

    /**
     * 新增应用管理
     * 
     * @param sspApp 应用管理
     * @return 结果
     */
    @Override
    public int insertSspApp(SspApp sspApp)
    {
        sspApp.setCreateTime(DateUtils.getNowDate());
        return sspAppMapper.insertSspApp(sspApp);
    }

    /**
     * 修改应用管理
     * 
     * @param sspApp 应用管理
     * @return 结果
     */
    @Override
    public int updateSspApp(SspApp sspApp)
    {
        sspApp.setUpdateTime(DateUtils.getNowDate());
        return sspAppMapper.updateSspApp(sspApp);
    }

    /**
     * 批量删除应用管理
     * 
     * @param ids 需要删除的应用管理主键
     * @return 结果
     */
    @Override
    public int deleteSspAppByIds(Long[] ids)
    {
        return sspAppMapper.deleteSspAppByIds(ids);
    }

    /**
     * 删除应用管理信息
     * 
     * @param id 应用管理主键
     * @return 结果
     */
    @Override
    public int deleteSspAppById(Long id)
    {
        return sspAppMapper.deleteSspAppById(id);
    }
}

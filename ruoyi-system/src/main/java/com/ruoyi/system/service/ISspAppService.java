package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SspApp;

/**
 * 应用管理Service接口
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public interface ISspAppService 
{
    /**
     * 查询应用管理
     * 
     * @param id 应用管理主键
     * @return 应用管理
     */
    public SspApp selectSspAppById(Long id);

    /**
     * 查询应用管理列表
     * 
     * @param sspApp 应用管理
     * @return 应用管理集合
     */
    public List<SspApp> selectSspAppList(SspApp sspApp);

    /**
     * 新增应用管理
     * 
     * @param sspApp 应用管理
     * @return 结果
     */
    public int insertSspApp(SspApp sspApp);

    /**
     * 修改应用管理
     * 
     * @param sspApp 应用管理
     * @return 结果
     */
    public int updateSspApp(SspApp sspApp);

    /**
     * 批量删除应用管理
     * 
     * @param ids 需要删除的应用管理主键集合
     * @return 结果
     */
    public int deleteSspAppByIds(Long[] ids);

    /**
     * 删除应用管理信息
     * 
     * @param id 应用管理主键
     * @return 结果
     */
    public int deleteSspAppById(Long id);
}

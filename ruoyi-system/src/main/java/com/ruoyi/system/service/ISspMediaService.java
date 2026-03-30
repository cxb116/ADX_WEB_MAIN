package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SspMedia;

/**
 * 媒体管理Service接口
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public interface ISspMediaService 
{
    /**
     * 查询媒体管理
     * 
     * @param id 媒体管理主键
     * @return 媒体管理
     */
    public SspMedia selectSspMediaById(Long id);

    /**
     * 查询媒体管理列表
     * 
     * @param sspMedia 媒体管理
     * @return 媒体管理集合
     */
    public List<SspMedia> selectSspMediaList(SspMedia sspMedia);

    /**
     * 新增媒体管理
     * 
     * @param sspMedia 媒体管理
     * @return 结果
     */
    public int insertSspMedia(SspMedia sspMedia);

    /**
     * 修改媒体管理
     * 
     * @param sspMedia 媒体管理
     * @return 结果
     */
    public int updateSspMedia(SspMedia sspMedia);

    /**
     * 批量删除媒体管理
     * 
     * @param ids 需要删除的媒体管理主键集合
     * @return 结果
     */
    public int deleteSspMediaByIds(Long[] ids);

    /**
     * 删除媒体管理信息
     * 
     * @param id 媒体管理主键
     * @return 结果
     */
    public int deleteSspMediaById(Long id);
}

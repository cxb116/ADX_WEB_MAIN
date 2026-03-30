package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SspMediaMapper;
import com.ruoyi.system.domain.SspMedia;
import com.ruoyi.system.service.ISspMediaService;

/**
 * 媒体管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
@Service
public class SspMediaServiceImpl implements ISspMediaService 
{
    @Autowired
    private SspMediaMapper sspMediaMapper;

    /**
     * 查询媒体管理
     * 
     * @param id 媒体管理主键
     * @return 媒体管理
     */
    @Override
    public SspMedia selectSspMediaById(Long id)
    {
        return sspMediaMapper.selectSspMediaById(id);
    }

    /**
     * 查询媒体管理列表
     * 
     * @param sspMedia 媒体管理
     * @return 媒体管理
     */
    @Override
    public List<SspMedia> selectSspMediaList(SspMedia sspMedia)
    {
        return sspMediaMapper.selectSspMediaList(sspMedia);
    }

    /**
     * 新增媒体管理
     * 
     * @param sspMedia 媒体管理
     * @return 结果
     */
    @Override
    public int insertSspMedia(SspMedia sspMedia)
    {
        sspMedia.setCreateTime(DateUtils.getNowDate());
        return sspMediaMapper.insertSspMedia(sspMedia);
    }

    /**
     * 修改媒体管理
     * 
     * @param sspMedia 媒体管理
     * @return 结果
     */
    @Override
    public int updateSspMedia(SspMedia sspMedia)
    {
        sspMedia.setUpdateTime(DateUtils.getNowDate());
        return sspMediaMapper.updateSspMedia(sspMedia);
    }

    /**
     * 批量删除媒体管理
     * 
     * @param ids 需要删除的媒体管理主键
     * @return 结果
     */
    @Override
    public int deleteSspMediaByIds(Long[] ids)
    {
        return sspMediaMapper.deleteSspMediaByIds(ids);
    }

    /**
     * 删除媒体管理信息
     * 
     * @param id 媒体管理主键
     * @return 结果
     */
    @Override
    public int deleteSspMediaById(Long id)
    {
        return sspMediaMapper.deleteSspMediaById(id);
    }
}

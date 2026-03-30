package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应用管理对象 ssp_app
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public class SspApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 媒体Id */
    @Excel(name = "媒体Id")
    private Long mediaId;

    /** 媒体名称（用于查询，不属于ssp_app表字段） */
    @Excel(name = "媒体名称")
    private String mediaName;


    /** 应用名称 */
    @Excel(name = "应用名称")
    private String name;

    /** 操作系统类型，1=Android，2=iOS */
    @Excel(name = "操作系统类型，1=Android，2=iOS")
    private Long osType;

    /** 接入方式:1=API，2=SDK */
    @Excel(name = "接入方式:1=API，2=SDK")
    private Long accessType;

    /** 包名 */
    @Excel(name = "包名")
    private String pkg;

    /** 下载地址 */
    @Excel(name = "下载地址")
    private String downloadUrl;

    /** 状态：1正常 0禁用 2审核中 3拒绝 */
    @Excel(name = "状态：1正常 0禁用 2审核中 3拒绝")
    private Long enable;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setMediaId(Long mediaId) 
    {
        this.mediaId = mediaId;
    }

    public Long getMediaId() 
    {
        return mediaId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setOsType(Long osType) 
    {
        this.osType = osType;
    }

    public Long getOsType() 
    {
        return osType;
    }

    public void setAccessType(Long accessType) 
    {
        this.accessType = accessType;
    }

    public Long getAccessType() 
    {
        return accessType;
    }

    public void setPkg(String pkg) 
    {
        this.pkg = pkg;
    }

    public String getPkg() 
    {
        return pkg;
    }

    public void setDownloadUrl(String downloadUrl) 
    {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() 
    {
        return downloadUrl;
    }

    public void setEnable(Long enable) 
    {
        this.enable = enable;
    }

    public Long getEnable() 
    {
        return enable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mediaId", getMediaId())
            .append("name", getName())
            .append("osType", getOsType())
            .append("accessType", getAccessType())
            .append("pkg", getPkg())
            .append("downloadUrl", getDownloadUrl())
            .append("enable", getEnable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
}

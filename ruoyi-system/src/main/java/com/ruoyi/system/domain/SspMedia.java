package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 媒体管理对象 ssp_media
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public class SspMedia extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 媒体名称 */
    @Excel(name = "媒体名称")
    private String name;

    /** 账号名 */
    @Excel(name = "账号名")
    private String account;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String mediaCompanyName;

    /** 公司简称 */
    @Excel(name = "公司简称")
    private String mediaCompanyShort;

    /** 统一社会信用代码 */
    @Excel(name = "统一社会信用代码")
    private String mediaCompanyCode;

    /** 营业执照照片 */
    @Excel(name = "营业执照照片")
    private String mediaCompanyLicense;

    /** 公司地址 */
    @Excel(name = "公司地址")
    private String mediaCompanyAddress;

    /** 法人姓名 */
    @Excel(name = "法人姓名")
    private String mediaOwnerName;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 联系邮箱 */
    @Excel(name = "联系邮箱")
    private String contactEmail;

    /** 状态：1正常 0禁用 2审核中 3拒绝 */
    @Excel(name = "状态：1正常 0禁用 2审核中 3拒绝")
    private Long enable;

    @Excel(name="接入方式 接入方式:1=API，2=SDK")
    private int accessType;

    private int type;


    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setAccount(String account) 
    {
        this.account = account;
    }

    public String getAccount() 
    {
        return account;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setMediaCompanyName(String mediaCompanyName) 
    {
        this.mediaCompanyName = mediaCompanyName;
    }

    public String getMediaCompanyName() 
    {
        return mediaCompanyName;
    }

    public void setMediaCompanyShort(String mediaCompanyShort) 
    {
        this.mediaCompanyShort = mediaCompanyShort;
    }

    public String getMediaCompanyShort() 
    {
        return mediaCompanyShort;
    }

    public void setMediaCompanyCode(String mediaCompanyCode) 
    {
        this.mediaCompanyCode = mediaCompanyCode;
    }

    public String getMediaCompanyCode() 
    {
        return mediaCompanyCode;
    }

    public void setMediaCompanyLicense(String mediaCompanyLicense) 
    {
        this.mediaCompanyLicense = mediaCompanyLicense;
    }

    public String getMediaCompanyLicense() 
    {
        return mediaCompanyLicense;
    }

    public void setMediaCompanyAddress(String mediaCompanyAddress) 
    {
        this.mediaCompanyAddress = mediaCompanyAddress;
    }

    public String getMediaCompanyAddress() 
    {
        return mediaCompanyAddress;
    }

    public void setMediaOwnerName(String mediaOwnerName) 
    {
        this.mediaOwnerName = mediaOwnerName;
    }

    public String getMediaOwnerName() 
    {
        return mediaOwnerName;
    }

    public void setContactName(String contactName) 
    {
        this.contactName = contactName;
    }

    public String getContactName() 
    {
        return contactName;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setContactEmail(String contactEmail) 
    {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() 
    {
        return contactEmail;
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
            .append("name", getName())
            .append("account", getAccount())
            .append("password", getPassword())
            .append("mediaCompanyName", getMediaCompanyName())
            .append("mediaCompanyShort", getMediaCompanyShort())
            .append("mediaCompanyCode", getMediaCompanyCode())
            .append("mediaCompanyLicense", getMediaCompanyLicense())
            .append("mediaCompanyAddress", getMediaCompanyAddress())
            .append("mediaOwnerName", getMediaOwnerName())
            .append("contactName", getContactName())
            .append("contactPhone", getContactPhone())
            .append("contactEmail", getContactEmail())
            .append("enable", getEnable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

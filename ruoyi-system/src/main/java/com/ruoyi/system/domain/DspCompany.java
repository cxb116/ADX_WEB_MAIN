package com.ruoyi.system.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 公司管理对象 dsp_company
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public class DspCompany extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String name;

    /** 预算映射值（不可重复） */
    @Excel(name = "预算映射值", readConverterExp = "不=可重复")
    private Long dspCode;

    /** 请求地址 */
    @Excel(name = "请求地址")
    private String url;

    /** 请求方法 1 post 2 get */
    @Excel(name = "请求方法 1 post 2 get")
    private String  method;

    /** 超时时间 (例如650ms) */
    @Excel(name = "超时时间 (例如650ms)")
    private Long timeout;

    public void setId(Long id) 
    {
        this.id = id;
    }

    @JSONField(name = "id")
    public Long getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @JSONField(name = "name")
    public String getName()
    {
        return name;
    }

    public void setDspCode(Long dspCode)
    {
        this.dspCode = dspCode;
    }

    @JSONField(name = "dsp_code")
    public Long getDspCode()
    {
        return dspCode;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @JSONField(name = "url")
    public String getUrl()
    {
        return url;
    }



    public void setTimeout(Long timeout)
    {
        this.timeout = timeout;
    }

    @JSONField(name = "timeout")
    public Long getTimeout()
    {
        return timeout;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("dspCode", getDspCode())
            .append("url", getUrl())
            .append("method", getMethod())
            .append("timeout", getTimeout())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

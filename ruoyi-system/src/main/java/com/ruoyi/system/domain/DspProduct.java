package com.ruoyi.system.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Map;

/**
 * 产品管理对象 dsp_product
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public class DspProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String name;

    /** 公司id */
    @Excel(name = "公司id")
    private Long companyId;

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

    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    @JSONField(name = "company_id")
    public Long getCompanyId()
    {
        return companyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("companyId", getCompanyId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    /**
     * 重写 getParams，序列化到 etcd 时忽略 params 字段
     */
    @JSONField(serialize = false)
    @JsonIgnore
    @Override
    public Map<String, Object> getParams()
    {
        return super.getParams();
    }
}

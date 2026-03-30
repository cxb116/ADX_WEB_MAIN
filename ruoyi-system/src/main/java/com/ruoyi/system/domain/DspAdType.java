package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 广告类型对象 dsp_ad_type
 * 
 * @author ruoyi
 * @date 2026-02-28
 */
public class DspAdType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  ID */
    private Long id;
    private String name;
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }


}

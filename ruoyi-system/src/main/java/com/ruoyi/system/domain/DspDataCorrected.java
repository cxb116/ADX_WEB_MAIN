package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支出修正对象 dsp_data_corrected
 * 
 * @author ruoyi
 * @date 2026-03-22
 */
public class DspDataCorrected extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 媒体数据报表id */
    @Excel(name = "媒体数据报表id")
    private Long dataSspSlotDayId;

    /** 成交方式 */
    @Excel(name = "成交方式")
    private Long sspPayType;

    /** 成本(分) */
    @Excel(name = "成本(分)")
    private Long spend;

    /** 收入(分) */
    @Excel(name = "收入(分)")
    private Long income;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDataSspSlotDayId(Long dataSspSlotDayId) 
    {
        this.dataSspSlotDayId = dataSspSlotDayId;
    }

    public Long getDataSspSlotDayId() 
    {
        return dataSspSlotDayId;
    }

    public void setSspPayType(Long sspPayType) 
    {
        this.sspPayType = sspPayType;
    }

    public Long getSspPayType() 
    {
        return sspPayType;
    }

    public void setSpend(Long spend) 
    {
        this.spend = spend;
    }

    public Long getSpend() 
    {
        return spend;
    }

    public void setIncome(Long income) 
    {
        this.income = income;
    }

    public Long getIncome() 
    {
        return income;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataSspSlotDayId", getDataSspSlotDayId())
            .append("sspPayType", getSspPayType())
            .append("spend", getSpend())
            .append("income", getIncome())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

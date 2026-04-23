package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预算收益导入对象 dsp_income
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
public class DspIncome extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 导入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "导入时间(2026-10-10)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inputTime;

    /** 预算位Id */
    @Excel(name = "预算位Id")
    private Long dspSlotId;

    /** 预算位code */
    @Excel(name = "预算位编码")
    private String dspSlotCode;



    /** 媒体位id */
    @Excel(name = "媒体位id")
    private Long sspSlotId;

    /** 结算方式(1分成 2 RTB) */
    @Excel(name = "结算方式(1分成 2 RTB)")
    private Long dspPayType;

    /** 公司id */
//    @Excel(name = "公司id")
    private Long companyId;

    /** 导入数据条数 */
//    @Excel(name = "导入数据条数")
    private Long table;

    /** 预算放收益 */
    @Excel(name = "我方收益")
    private Long income;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDspSlotId(Long dspSlotId) 
    {
        this.dspSlotId = dspSlotId;
    }

    public Long getDspSlotId() 
    {
        return dspSlotId;
    }

    public void setDspSlotCode(String dspSlotCode) 
    {
        this.dspSlotCode = dspSlotCode;
    }

    public String getDspSlotCode() 
    {
        return dspSlotCode;
    }

    public void setIncome(Long income) 
    {
        this.income = income;
    }

    public Long getIncome() 
    {
        return income;
    }

    public void setSspSlotId(Long sspSlotId) 
    {
        this.sspSlotId = sspSlotId;
    }

    public Long getSspSlotId() 
    {
        return sspSlotId;
    }

    public void setDspPayType(Long dspPayType) 
    {
        this.dspPayType = dspPayType;
    }

    public Long getDspPayType() 
    {
        return dspPayType;
    }

    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
    }

    public void setTable(Long table) 
    {
        this.table = table;
    }

    public Long getTable() 
    {
        return table;
    }

    public void setInputTime(Date inputTime) 
    {
        this.inputTime = inputTime;
    }

    public Date getInputTime()
    {
        return inputTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dspSlotId", getDspSlotId())
            .append("dspSlotCode", getDspSlotCode())
            .append("income", getIncome())
            .append("sspSlotId", getSspSlotId())
            .append("dspPayType", getDspPayType())
            .append("companyId", getCompanyId())
            .append("table", getTable())
            .append("inputTime", getInputTime())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

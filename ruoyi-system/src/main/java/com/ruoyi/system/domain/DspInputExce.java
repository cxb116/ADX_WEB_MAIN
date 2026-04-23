package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收益导入exce对象 dsp_input_exce
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
public class DspInputExce extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 预算位id */
    @Excel(name = "预算位id")
    private Long dspSlotId;

    /** 导入条数 */
    @Excel(name = "导入条数")
    private Long table;

    /** 导入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "导入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inputTime;

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
            .append("table", getTable())
            .append("inputTime", getInputTime())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

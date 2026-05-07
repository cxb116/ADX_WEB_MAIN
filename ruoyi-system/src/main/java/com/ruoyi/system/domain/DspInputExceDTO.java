package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 这个是将导入的exce 数据规整
public class DspInputExceDTO {

    private String dspSlotCode;

    private long income;


    private Date inputTime;



    private List<SspInputExceData> sspInputExceDTOList;

    public String getDspSlotCode() {
        return dspSlotCode;
    }

    public void setDspSlotCode(String dspSlotCode) {
        this.dspSlotCode = dspSlotCode;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public long  getIncome() {
        return income;
    }

    public void setIncome(long  income) {
        this.income = income;
    }

    public List<SspInputExceData> getDspInputExceDTOList() {
        return sspInputExceDTOList;
    }

    public void addDspInputExceDTO(SspInputExceData dto) {
        if (this.sspInputExceDTOList == null) {
            this.sspInputExceDTOList = new ArrayList<SspInputExceData>();
        }
        this.sspInputExceDTOList.add(dto);
    }
}


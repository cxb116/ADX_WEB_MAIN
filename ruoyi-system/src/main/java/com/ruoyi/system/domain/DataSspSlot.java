package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 媒体数据报表对象 data_ssp_slot_hour_YYYYMM
 * 支持按月动态分表，表名格式：data_ssp_slot_hour_YYYYMM
 *
 * @author ruoyi
 * @date 2026-03-01
 */
public class DataSspSlot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 动态表名（格式：data_ssp_slot_hour_YYYYMM，如 data_ssp_slot_hour_202603） */
    private String tableName;

    /** 主键 */
    private Long id;

    /** 媒体用户Id */
    @Excel(name = "媒体用户Id")
    private Long mediaId;

    /** 应用ID */
    @Excel(name = "应用ID")
    private Long appId;

    /** SSP广告位ID */
    @Excel(name = "SSP广告位ID")
    private Long sspSlotId;

    /** DSP广告位ID */
    @Excel(name = "DSP广告位ID")
    private Long dspSlotId;

    /** 预算广告位编号 */
    @Excel(name = "预算广告位编号")
    private String dspSlotCode;

    /** 展示PV */
    @Excel(name = "展示PV")
    private Long showPv;

    /** 展示UV */
    @Excel(name = "展示UV")
    private Long showUv;

    /** 点击PV */
    @Excel(name = "点击PV")
    private Long clickPv;

    /** 点击UV */
    @Excel(name = "点击UV")
    private Long clickUv;

    /** 请求PV */
    @Excel(name = "请求PV")
    private Long reqPv;

    /** 请求UV */
    @Excel(name = "请求UV")
    private Long reqUv;

    /** 丢弃请求 */
    @Excel(name = "丢弃请求")
    private Long discard;

    /** 返回PV */
    @Excel(name = "返回PV")
    private Long retPv;

    /** 返回UV */
    @Excel(name = "返回UV")
    private Long retUv;

    /** 成本(分) */
    @Excel(name = "成本(分)")
    private Long spend;

    /** 收入(分) */
    @Excel(name = "收入(分)")
    private Long income;

    /** 折后点击 */
    @Excel(name = "折后点击")
    private Long discountClickPv;

    /** 折后展示 */
    @Excel(name = "折后展示")
    private Long discountShowPv;

    /** 调起成功 */
    @Excel(name = "调起成功")
    private Long dplsuccPv;

    /** 完成量 */
    @Excel(name = "完成量")
    private Long completePv;

    /** 安装量 */
    @Excel(name = "安装量")
    private Long installPv;

    /** 激活量 */
    @Excel(name = "激活量")
    private Long activatePv;

    /** 日期 yyyyMMdd */
    @Excel(name = "日期 yyyyMMdd")
    private Long date;

    /** 创建时间戳 */
    @Excel(name = "创建时间戳")
    private Long createdAt;

    /** 应用名称（关联查询，不属于表字段） */
    private String appName;

    /** 媒体名称（关联查询，不属于表字段） */
    private String mediaName;

    /** 媒体广告位名称（关联查询，不属于表字段） */
    private String sspSlotName;

    /** 预算广告位名称（关联查询，不属于表字段） */
    private String dspSlotName;

    /** 结算方式（关联查询，不属于表字段） */
    private Long sspPayType;

    /** 分成系数（关联查询，不属于表字段） */
    private Long sspDealRatio;

    /** 填充率（计算字段，不属于表字段） */
    private Double fillRate;

    /** 请求丢失率（计算字段，不属于表字段） */
    private Double requestLossRate;

    /** 展现率（计算字段，不属于表字段） */
    private Double showRate;

    /** 点击率（计算字段，不属于表字段） */
    private Double clickRate;

    /** SSP千次收益（关联查询，不属于表字段，单位分） */
    private Long sspEcpm;

    /** eCPM（计算字段，不属于表字段，单位元） */
    private Double ecpm;

    private int appOsType;

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

    public void setAppId(Long appId) 
    {
        this.appId = appId;
    }

    public Long getAppId() 
    {
        return appId;
    }

    public void setSspSlotId(Long sspSlotId) 
    {
        this.sspSlotId = sspSlotId;
    }

    public Long getSspSlotId() 
    {
        return sspSlotId;
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

    public void setShowPv(Long showPv) 
    {
        this.showPv = showPv;
    }

    public Long getShowPv() 
    {
        return showPv;
    }

    public void setShowUv(Long showUv) 
    {
        this.showUv = showUv;
    }

    public Long getShowUv() 
    {
        return showUv;
    }

    public void setClickPv(Long clickPv) 
    {
        this.clickPv = clickPv;
    }

    public Long getClickPv() 
    {
        return clickPv;
    }

    public void setClickUv(Long clickUv) 
    {
        this.clickUv = clickUv;
    }

    public Long getClickUv() 
    {
        return clickUv;
    }

    public void setReqPv(Long reqPv) 
    {
        this.reqPv = reqPv;
    }

    public Long getReqPv() 
    {
        return reqPv;
    }

    public void setReqUv(Long reqUv) 
    {
        this.reqUv = reqUv;
    }

    public Long getReqUv() 
    {
        return reqUv;
    }

    public void setDiscard(Long discard) 
    {
        this.discard = discard;
    }

    public Long getDiscard() 
    {
        return discard;
    }

    public void setRetPv(Long retPv) 
    {
        this.retPv = retPv;
    }

    public Long getRetPv() 
    {
        return retPv;
    }

    public void setRetUv(Long retUv) 
    {
        this.retUv = retUv;
    }

    public Long getRetUv() 
    {
        return retUv;
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

    public void setDiscountClickPv(Long discountClickPv) 
    {
        this.discountClickPv = discountClickPv;
    }

    public Long getDiscountClickPv() 
    {
        return discountClickPv;
    }

    public void setDiscountShowPv(Long discountShowPv) 
    {
        this.discountShowPv = discountShowPv;
    }

    public Long getDiscountShowPv() 
    {
        return discountShowPv;
    }

    public void setDplsuccPv(Long dplsuccPv) 
    {
        this.dplsuccPv = dplsuccPv;
    }

    public Long getDplsuccPv() 
    {
        return dplsuccPv;
    }

    public void setCompletePv(Long completePv) 
    {
        this.completePv = completePv;
    }

    public Long getCompletePv() 
    {
        return completePv;
    }

    public void setInstallPv(Long installPv) 
    {
        this.installPv = installPv;
    }

    public Long getInstallPv() 
    {
        return installPv;
    }

    public void setActivatePv(Long activatePv) 
    {
        this.activatePv = activatePv;
    }

    public Long getActivatePv() 
    {
        return activatePv;
    }

    public void setDate(Long date) 
    {
        this.date = date;
    }

    public Long getDate() 
    {
        return date;
    }

    public void setCreatedAt(Long createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Long getCreatedAt()
    {
        return createdAt;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setMediaName(String mediaName)
    {
        this.mediaName = mediaName;
    }

    public String getMediaName()
    {
        return mediaName;
    }

    public void setSspSlotName(String sspSlotName)
    {
        this.sspSlotName = sspSlotName;
    }

    public String getSspSlotName()
    {
        return sspSlotName;
    }

    public void setDspSlotName(String dspSlotName)
    {
        this.dspSlotName = dspSlotName;
    }

    public String getDspSlotName()
    {
        return dspSlotName;
    }

    public void setSspPayType(Long sspPayType)
    {
        this.sspPayType = sspPayType;
    }

    public Long getSspPayType()
    {
        return sspPayType;
    }

    public void setSspDealRatio(Long sspDealRatio)
    {
        this.sspDealRatio = sspDealRatio;
    }

    public Long getSspDealRatio()
    {
        return sspDealRatio;
    }

    public void setSspEcpm(Long sspEcpm)
    {
        this.sspEcpm = sspEcpm;
    }

    public Long getSspEcpm()
    {
        return sspEcpm;
    }

    public void setFillRate(Double fillRate)
    {
        this.fillRate = fillRate;
    }

    public Double getFillRate()
    {
        return fillRate;
    }

    public void setRequestLossRate(Double requestLossRate)
    {
        this.requestLossRate = requestLossRate;
    }

    public Double getRequestLossRate()
    {
        return requestLossRate;
    }

    public void setShowRate(Double showRate)
    {
        this.showRate = showRate;
    }

    public Double getShowRate()
    {
        return showRate;
    }

    public void setClickRate(Double clickRate)
    {
        this.clickRate = clickRate;
    }

    public Double getClickRate()
    {
        return clickRate;
    }

    public void setEcpm(Double ecpm)
    {
        this.ecpm = ecpm;
    }

    public Double getEcpm()
    {
        return ecpm;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mediaId", getMediaId())
            .append("appId", getAppId())
            .append("sspSlotId", getSspSlotId())
            .append("dspSlotId", getDspSlotId())
            .append("dspSlotCode", getDspSlotCode())
            .append("showPv", getShowPv())
            .append("showUv", getShowUv())
            .append("clickPv", getClickPv())
            .append("clickUv", getClickUv())
            .append("reqPv", getReqPv())
            .append("reqUv", getReqUv())
            .append("discard", getDiscard())
            .append("retPv", getRetPv())
            .append("retUv", getRetUv())
            .append("spend", getSpend())
            .append("income", getIncome())
            .append("discountClickPv", getDiscountClickPv())
            .append("discountShowPv", getDiscountShowPv())
            .append("dplsuccPv", getDplsuccPv())
            .append("completePv", getCompletePv())
            .append("installPv", getInstallPv())
            .append("activatePv", getActivatePv())
            .append("date", getDate())
            .append("sspPayType", getSspPayType())
            .append("sspDealRatio", getSspDealRatio())
            .append("sspEcpm", getSspEcpm())
            .append("ecpm", getEcpm())
            .append("createdAt", getCreatedAt())
            .toString();
    }

    public int getAppOsType() {
        return appOsType;
    }

    public void setAppOsType(int appOsType) {
        this.appOsType = appOsType;
    }
}

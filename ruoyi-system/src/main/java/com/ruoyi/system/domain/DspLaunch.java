package com.ruoyi.system.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 投放配置对象 dsp_launch
 *
 * @author ruoyi
 * @date 2026-02-25
 */
public class DspLaunch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 媒体广告位ID */
    @Excel(name = "媒体广告位ID")
    private Long sspSlotId;

    /** 预算方广告位ID */
    @Excel(name = "预算方广告位ID")
    private Long dspSlotId;

    /** 流量权重 */
    @Excel(name = "流量权重")
    private Integer trafficWeight;

    /** 投放策略：1对接第三方，2自主投放 */
    @Excel(name = "投放策略", readConverterExp = "1=对接第三方,2=自主投放")
    private Integer launchStrategy;

    /** 底价(分) */
    @Excel(name = "底价")
    private Long floorPrice;

    /** IP限流次数 */
    @Excel(name = "IP限流次数")
    private Integer ipLimit;

    /** 捕获日志时长(秒) */
    @Excel(name = "捕获日志时长")
    private Integer logCaptureAt;

    /** 上报黑名单 */
    @Excel(name = "上报黑名单")
    private String trackSchwarz;

    /** 请求次数限制 */
    @Excel(name = "请求次数限制")
    private Long req;

    /** 展现次数限制 */
    @Excel(name = "展现次数限制")
    private Long ims;

    /** 点击次数限制 */
    @Excel(name = "点击次数限制")
    private Long clk;

    /** 投放时段：1全时段，2自定义 */
    @Excel(name = "投放时段", readConverterExp = "1=全时段,2=自定义")
    private Integer launchTime;

    /** 地域定向：1不限制，2定向，3排除 */
    @Excel(name = "地域定向", readConverterExp = "1=不限制,2=定向,3=排除")
    private Integer regionDirection;

    /** 品牌定向：1不限制，2定向，3排除 */
    @Excel(name = "品牌定向", readConverterExp = "1=不限制,2=定向,3=排除")
    private Integer brandDirection;

    /** 价格透传：0不透传，1透传 */
    @Excel(name = "价格透传", readConverterExp = "0=不透传,1=透传")
    private Integer priceTransfer;

    /** 包透传：0不透传，1透传 */
    @Excel(name = "包透传", readConverterExp = "0=不透传,1=透传")
    private Integer pkgTransfer;

    /** 人群定向：1不限制，2定向，3排除 */
    @Excel(name = "人群定向", readConverterExp = "1=不限制,2=定向,3=排除")
    private Integer crowdDirection;

    private Integer indexs;

    private Integer dspPayType;

    public void setId(Long id)
    {
        this.id = id;
    }

    @JSONField(name = "id")
    public Long getId()
    {
        return id;
    }

    @JSONField(name = "ssp_slot_id")
    @JsonProperty("ssp_slot_id")
    public void setSspSlotId(Long sspSlotId)
    {
        this.sspSlotId = sspSlotId;
    }

    @JSONField(name = "ssp_slot_id")
    @JsonProperty("ssp_slot_id")
    public Long getSspSlotId()
    {
        return sspSlotId;
    }

    @JSONField(name = "dsp_slot_id")
    @JsonProperty("dsp_slot_id")
    public void setDspSlotId(Long dspSlotId)
    {
        this.dspSlotId = dspSlotId;
    }

    @JSONField(name = "dsp_slot_id")
    @JsonProperty("dsp_slot_id")
    public Long getDspSlotId()
    {
        return dspSlotId;
    }

    @JSONField(name = "traffic_weight")
    @JsonProperty("traffic_weight")
    public void setTrafficWeight(Integer trafficWeight)
    {
        this.trafficWeight = trafficWeight;
    }

    @JSONField(name = "traffic_weight")
    @JsonProperty("traffic_weight")
    public Integer getTrafficWeight()
    {
        return trafficWeight;
    }

    @JSONField(name = "launch_strategy")
    @JsonProperty("launch_strategy")
    public void setLaunchStrategy(Integer launchStrategy)
    {
        this.launchStrategy = launchStrategy;
    }

    @JSONField(name = "launch_strategy")
    @JsonProperty("launch_strategy")
    public Integer getLaunchStrategy()
    {
        return launchStrategy;
    }

    @JSONField(name = "floor_price")
    @JsonProperty("floor_price")
    public void setFloorPrice(Long floorPrice)
    {
        this.floorPrice = floorPrice;
    }

    @JSONField(name = "floor_price")
    @JsonProperty("floor_price")
    public Long getFloorPrice()
    {
        return floorPrice;
    }

    @JSONField(name = "ip_limit")
    @JsonProperty("ip_limit")
    public void setIpLimit(Integer ipLimit)
    {
        this.ipLimit = ipLimit;
    }

    @JSONField(name = "ip_limit")
    @JsonProperty("ip_limit")
    public Integer getIpLimit()
    {
        return ipLimit;
    }

    @JSONField(name = "log_capture_at")
    @JsonProperty("log_capture_at")
    public void setLogCaptureAt(Integer logCaptureAt)
    {
        this.logCaptureAt = logCaptureAt;
    }

    @JSONField(name = "log_capture_at")
    @JsonProperty("log_capture_at")
    public Integer getLogCaptureAt()
    {
        return logCaptureAt;
    }

    @JSONField(name = "track_schwarz")
    @JsonProperty("track_schwarz")
    public void setTrackSchwarz(String trackSchwarz)
    {
        this.trackSchwarz = trackSchwarz;
    }

    @JSONField(name = "track_schwarz")
    @JsonProperty("track_schwarz")
    public String getTrackSchwarz()
    {
        return trackSchwarz;
    }

    @JsonProperty("req")
    public void setReq(Long req)
    {
        this.req = req;
    }

    @JSONField(serialize = false)
    @JsonProperty("req")
    public Long getReq()
    {
        return req;
    }

    @JsonProperty("ims")
    public void setIms(Long ims)
    {
        this.ims = ims;
    }

    @JSONField(serialize = false)
    @JsonProperty("ims")
    public Long getIms()
    {
        return ims;
    }

    @JsonProperty("clk")
    public void setClk(Long clk)
    {
        this.clk = clk;
    }

    @JSONField(serialize = false)
    @JsonProperty("clk")
    public Long getClk()
    {
        return clk;
    }

    @JSONField(name = "launch_time")
    @JsonProperty("launch_time")
    public void setLaunchTime(Integer launchTime)
    {
        this.launchTime = launchTime;
    }

    @JSONField(name = "launch_time")
    @JsonProperty("launch_time")
    public Integer getLaunchTime()
    {
        return launchTime;
    }

    @JSONField(name = "region_direction")
    @JsonProperty("region_direction")
    public void setRegionDirection(Integer regionDirection)
    {
        this.regionDirection = regionDirection;
    }

    @JSONField(name = "region_direction")
    @JsonProperty("region_direction")
    public Integer getRegionDirection()
    {
        return regionDirection;
    }

    @JSONField(name = "brand_direction")
    @JsonProperty("brand_direction")
    public void setBrandDirection(Integer brandDirection)
    {
        this.brandDirection = brandDirection;
    }

    @JSONField(name = "brand_direction")
    @JsonProperty("brand_direction")
    public Integer getBrandDirection()
    {
        return brandDirection;
    }

    @JSONField(name = "price_transfer")
    @JsonProperty("price_transfer")
    public void setPriceTransfer(Integer priceTransfer)
    {
        this.priceTransfer = priceTransfer;
    }

    @JSONField(name = "price_transfer")
    @JsonProperty("price_transfer")
    public Integer getPriceTransfer()
    {
        return priceTransfer;
    }

    @JSONField(name = "pkg_transfer")
    @JsonProperty("pkg_transfer")
    public void setPkgTransfer(Integer pkgTransfer)
    {
        this.pkgTransfer = pkgTransfer;
    }

    @JSONField(name = "pkg_transfer")
    @JsonProperty("pkg_transfer")
    public Integer getPkgTransfer()
    {
        return pkgTransfer;
    }

    @JSONField(name = "crowd_direction")
    @JsonProperty("crowd_direction")
    public void setCrowdDirection(Integer crowdDirection)
    {
        this.crowdDirection = crowdDirection;
    }

    @JSONField(name = "crowd_direction")
    @JsonProperty("crowd_direction")
    public Integer getCrowdDirection()
    {
        return crowdDirection;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sspSlotId", getSspSlotId())
            .append("dspSlotId", getDspSlotId())
            .append("trafficWeight", getTrafficWeight())
            .append("launchStrategy", getLaunchStrategy())
            .append("floorPrice", getFloorPrice())
            .append("ipLimit", getIpLimit())
            .append("logCaptureAt", getLogCaptureAt())
            .append("trackSchwarz", getTrackSchwarz())
            .append("req", getReq())
            .append("ims", getIms())
            .append("clk", getClk())
            .append("launchTime", getLaunchTime())
            .append("regionDirection", getRegionDirection())
            .append("brandDirection", getBrandDirection())
            .append("priceTransfer", getPriceTransfer())
            .append("pkgTransfer", getPkgTransfer())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }


    public Integer getIndexs() {
        return indexs;
    }

    public void setIndexs(Integer indexs) {
        this.indexs = indexs;
    }

    public Integer getDspPayType() {
        return dspPayType;
    }

    public void setDspPayType(Integer dspPayType) {
        this.dspPayType = dspPayType;
    }
}

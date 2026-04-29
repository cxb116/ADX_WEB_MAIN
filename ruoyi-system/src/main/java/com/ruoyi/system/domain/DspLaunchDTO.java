package com.ruoyi.system.domain;

/**
 * 投放配置DTO对象
 * 包含 DspLaunch 和 DspSlotInfo 的组合信息
 *
 * @author ruoyi
 * @date 2026-02-25
 */
public class DspLaunchDTO
{
    /** 投放配置ID */
    private Long id;

    /** 媒体广告位ID */
    private Long sspSlotId;

    /** 预算方广告位ID */
    private Long dspSlotId;

    /** 流量权重 */
    private Integer trafficWeight;

    /** 投放策略：1对接第三方，2自主投放 */
    private Integer launchStrategy;

    /** 底价(分) */
    private Long floorPrice;

    /** IP限流次数 */
    private Integer ipLimit;

    /** 捕获日志时长(秒) */
    private Integer logCaptureAt;

    /** 上报黑名单 */
    private String trackSchwarz;

    /** 请求次数限制 */
    private Long req;

    /** 展现次数限制 */
    private Long ims;

    /** 点击次数限制 */
    private Long clk;

    /** 投放时段：1全时段，2自定义 */
    private Integer launchTime;

    /** 地域定向：1不限制，2定向，3排除 */
    private Integer regionDirection;

    /** 品牌定向：1不限制，2定向，3排除 */
    private Integer brandDirection;

    /** 价格透传：0不透传，1透传 */
    private Integer priceTransfer;

    /** 包透传：0不透传，1透传 */
    private Integer pkgTransfer;

    /** 流量组编号（1=流量1，2=流量2...） */
    private Integer crowdDirection;

    /** 预算方广告位信息 */
    private DspSlotInfo dspSlotInfo;

    private Integer indexs;

    private Integer sspPayType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSspSlotId() {
        return sspSlotId;
    }

    public void setSspSlotId(Long sspSlotId) {
        this.sspSlotId = sspSlotId;
    }

    public Long getDspSlotId() {
        return dspSlotId;
    }

    public void setDspSlotId(Long dspSlotId) {
        this.dspSlotId = dspSlotId;
    }

    public Integer getTrafficWeight() {
        return trafficWeight;
    }

    public void setTrafficWeight(Integer trafficWeight) {
        this.trafficWeight = trafficWeight;
    }

    public Integer getLaunchStrategy() {
        return launchStrategy;
    }

    public void setLaunchStrategy(Integer launchStrategy) {
        this.launchStrategy = launchStrategy;
    }

    public Long getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(Long floorPrice) {
        this.floorPrice = floorPrice;
    }

    public Integer getIpLimit() {
        return ipLimit;
    }

    public void setIpLimit(Integer ipLimit) {
        this.ipLimit = ipLimit;
    }

    public Integer getLogCaptureAt() {
        return logCaptureAt;
    }

    public void setLogCaptureAt(Integer logCaptureAt) {
        this.logCaptureAt = logCaptureAt;
    }

    public String getTrackSchwarz() {
        return trackSchwarz;
    }

    public void setTrackSchwarz(String trackSchwarz) {
        this.trackSchwarz = trackSchwarz;
    }

    public Long getReq() {
        return req;
    }

    public void setReq(Long req) {
        this.req = req;
    }

    public Long getIms() {
        return ims;
    }

    public void setIms(Long ims) {
        this.ims = ims;
    }

    public Long getClk() {
        return clk;
    }

    public void setClk(Long clk) {
        this.clk = clk;
    }

    public Integer getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Integer launchTime) {
        this.launchTime = launchTime;
    }

    public Integer getRegionDirection() {
        return regionDirection;
    }

    public void setRegionDirection(Integer regionDirection) {
        this.regionDirection = regionDirection;
    }

    public Integer getBrandDirection() {
        return brandDirection;
    }

    public void setBrandDirection(Integer brandDirection) {
        this.brandDirection = brandDirection;
    }

    public Integer getPriceTransfer() {
        return priceTransfer;
    }

    public void setPriceTransfer(Integer priceTransfer) {
        this.priceTransfer = priceTransfer;
    }

    public Integer getPkgTransfer() {
        return pkgTransfer;
    }

    public void setPkgTransfer(Integer pkgTransfer) {
        this.pkgTransfer = pkgTransfer;
    }

    public Integer getCrowdDirection() {
        return crowdDirection;
    }

    public void setCrowdDirection(Integer crowdDirection) {
        this.crowdDirection = crowdDirection;
    }

    public DspSlotInfo getDspSlotInfo() {
        return dspSlotInfo;
    }

    public void setDspSlotInfo(DspSlotInfo dspSlotInfo) {
        this.dspSlotInfo = dspSlotInfo;
    }

    public Integer getIndexs() {
        return indexs;
    }

    public void setIndexs(Integer indexs) {
        this.indexs = indexs;
    }

    public Integer getSspPayType() {
        return sspPayType;
    }

    public void setSspPayType(Integer sspPayType) {
        this.sspPayType = sspPayType;
    }
}

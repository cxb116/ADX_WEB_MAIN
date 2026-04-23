package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.IDspIncomeService;

/**
 * 预算收益导入Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
@Service
public class DspIncomeServiceImpl implements IDspIncomeService 
{
    private static final Logger log = LoggerFactory.getLogger(DspIncomeServiceImpl.class);
    @Autowired
    private DspIncomeMapper dspIncomeMapper;
    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private DataSspSlotMapper dataSspSlotMapper;

    @Autowired
    private DataDspSlotServiceImpl dataDspSlotServiceImpl;

    @Autowired
    private SspSlotInfoMapper sspSlotInfoMapper;
    @Autowired
    private DataDspSlotMapper dataDspSlotMapper;
    @Autowired
    private DspSlotInfoMapper dspSlotInfoMapper;

    /**
     * 查询预算收益导入
     * 
     * @param id 预算收益导入主键
     * @return 预算收益导入
     */
    @Override
    public DspIncome selectDspIncomeById(Long id)
    {
        return dspIncomeMapper.selectDspIncomeById(id);
    }

    /**
     * 查询预算收益导入列表
     * 
     * @param dspIncome 预算收益导入
     * @return 预算收益导入
     */
    @Override
    public List<DspIncome> selectDspIncomeList(DspIncome dspIncome)
    {
        return dspIncomeMapper.selectDspIncomeList(dspIncome);
    }

    /**
     * 新增预算收益导入
     * 
     * @param dspIncome 预算收益导入
     * @return 结果
     */
    @Override
    public int insertDspIncome(DspIncome dspIncome)
    {
        dspIncome.setCreateTime(DateUtils.getNowDate());
        return dspIncomeMapper.insertDspIncome(dspIncome);
    }

    /**
     * 修改预算收益导入
     * 
     * @param dspIncome 预算收益导入
     * @return 结果
     */
    @Override
    public int updateDspIncome(DspIncome dspIncome)
    {
        dspIncome.setUpdateTime(DateUtils.getNowDate());
        return dspIncomeMapper.updateDspIncome(dspIncome);
    }

    /**
     * 批量删除预算收益导入
     * 
     * @param ids 需要删除的预算收益导入主键
     * @return 结果
     */
    @Override
    public int deleteDspIncomeByIds(Long[] ids)
    {
        return dspIncomeMapper.deleteDspIncomeByIds(ids);
    }

    /**
     * 删除预算收益导入信息
     * 
     * @param id 预算收益导入主键
     * @return 结果
     */
    @Override
    public int deleteDspIncomeById(Long id)
    {
        return dspIncomeMapper.deleteDspIncomeById(id);
    }


    /**
     *  exce 导入收益
     */
    @Override
    public String importIncomeExcel(List<DspIncome> dspIncomeList, boolean updateSupport, String operName) {

        if (dspIncomeList == null || dspIncomeList.isEmpty()) {
            throw new ServiceException("导入我方收益不能为空");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (DspIncome dspIncomeExcel : dspIncomeList) {
            System.out.println("dspIncome: " + dspIncomeExcel);

            try {
                String code = dspIncomeExcel.getDspSlotCode();
                Date inputTime = dspIncomeExcel.getInputTime();
                long dspSlotId = dspIncomeExcel.getDspSlotId();
                if (dspIncomeExcel.getIncome() < 0) {
                    continue;
                }
                dspIncomeExcel.setIncome(dspIncomeExcel.getIncome() * 100);

                //  参数校验（任意一个为空就拦截）
                if (org.apache.commons.lang3.StringUtils.isBlank(code) || inputTime == null || dspSlotId == 0) {
                    throw new ServiceException("导入日期和预算位编码不能为空");
                }

                //  Date → LocalDate（标准写法）
                LocalDate date = inputTime.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                //  格式化
                String dspInputTime = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String dataTableTime = date.format(DateTimeFormatter.ofPattern("yyyyMM"));

                System.out.println("dspInputTime: " + dspInputTime);
                System.out.println("dataTableTime: " + dataTableTime);

                // ================== 业务逻辑 ==================

                DataSspSlot query = new DataSspSlot();
                query.setDate(Long.valueOf(dspInputTime));
                query.setDspSlotCode(code);
                query.setTableName("data_ssp_slot_day_"+dataTableTime);
                query.setDspSlotId(dspSlotId);

                List<DataSspSlot> dataSspSlotsList = dataSspSlotMapper.selectDataSspSlotListInputExce(query);   // 获取媒体的数据
                if (dataSspSlotsList.size() == 0) {
                    throw new ServiceException("导入日期和预算位编码不能为空");
                } else if (dataSspSlotsList.size() == 1) {  // 就是这一个预算下跑了1个预算
                    // 1 获取分成比例
                    Long id = dataSspSlotsList.get(0).getSspSlotId();
                    SspSlotInfo sspSlotInfo = sspSlotInfoMapper.selectSspSlotInfoById(id);
                    if (sspSlotInfo == null) {
                        throw new ServiceException("sspSlotId 查询不到数据");
                    }
                    Long sspDealRatio = sspSlotInfo.getSspDealRatio();  // 获取分成系数
                    Long sspPayType = sspSlotInfo.getSspPayType();      // 获取结算方式
                    if (sspPayType != 1 || sspDealRatio <= 0) {
                        throw new ServiceException("不是分成无法导入计算下游收益,查看分成系数是否大于0");
                    }
                    // 2 根据分成比例算出下游媒体利润
                    Long sspIncome =  dspIncomeExcel.getIncome() * sspDealRatio / 100;
                    DataSspSlot sspSlot = new DataSspSlot();
                    sspSlot.setId(dataSspSlotsList.get(0).getId());
                    sspSlot.setIncome(sspIncome);
                    sspSlot.setUpdateTime(new Date());
                    sspSlot.setTableName("data_ssp_slot_day_"+dataTableTime);
                    // 3 入库同步利润
                    int i = dataSspSlotMapper.updateDataSspSlot(sspSlot);
                } else if (dataSspSlotsList.size() > 1) {
                    // 如果是大于1个预算，就需要通过获取他们的展现次数，算出，每个流量的展现比例,
                    Long showPvNum = 0L;
                    for (DataSspSlot dataSspSlot : dataSspSlotsList) {
                        showPvNum +=  dataSspSlot.getShowPv();
                    }
                    // 获取到曝光总和，需要通过showPvNum,去计算每个流量的占比
                    for (DataSspSlot dataSspSlot : dataSspSlotsList) {
                        // 获取分成系数
                        SspSlotInfo sspSlotInfo = sspSlotInfoMapper.selectSspSlotInfoById(dataSspSlot.getSspSlotId());
                        // 获取展现次数的占比   10%
                        System.out.println("showPv: " + dataSspSlot.getShowPv()+" showPvNum: " + showPvNum);
                        long ratio = (long) ((double) dataSspSlot.getShowPv() / showPvNum * 100);
                        System.out.println("ratio: "+ratio);
                        if (ratio == 0) {
                         continue;
                        }
                        // 检测是否是分成
                        if (sspSlotInfo.getSspPayType() != 1 || sspSlotInfo.getSspDealRatio() <= 0) {
                            continue;
                        }

                        // 2 根据分成比例算出下游媒体利润
                        Long sspIncome = dspIncomeExcel.getIncome() * sspSlotInfo.getSspDealRatio() / 100 * ratio /100;
                        DataSspSlot sspSlot = new DataSspSlot();
                        sspSlot.setId(dataSspSlot.getId());
                        sspSlot.setIncome(sspIncome);
                        sspSlot.setTableName("data_ssp_slot_day_" + dataTableTime);
                        // 3 入库同步利润
                        int i = dataSspSlotMapper.updateDataSspSlot(sspSlot);
                    }
                }
                // 预算计算
                List<DataSspSlot> dataSspSlotIncome = dataSspSlotMapper.selectDataSspSlotListInputExce(query);
                if (dataSspSlotIncome.size() == 0) {
                    throw new ServiceException("媒体数据不能为空");
                }
                long dspSpend = 0L;  // 就算出全部媒体的收益，减去总收益就是媒体的收益
                for (DataSspSlot dataSspSlot : dataSspSlotIncome) {
                    dspSpend +=  dataSspSlot.getIncome();
                }
                System.out.println("给下游媒体的总收益 sspIncome: " + dspSpend);
                long dspIncomes = dspIncomeExcel.getIncome() - dspSpend;
                System.out.println("预算给我的收益 dspIncomes: " + dspIncomes);

                DataDspSlot dataDspSlot = new DataDspSlot();
                dataDspSlot.setDspSlotId(dspIncomeExcel.getDspSlotId());
                dataDspSlot.setDspSlotCode(dspIncomeExcel.getDspSlotCode());
                dataDspSlot.setDate(Long.valueOf(dspInputTime));
                dataDspSlot.setTableName("data_dsp_slot_day_" + dataTableTime);
                List<DataDspSlot> dataDspSlotList= dataDspSlotMapper.selectDataDspSlotList(dataDspSlot);
                if (dataDspSlotList.size() > 1 || dataDspSlotList.size() == 0) {
                    throw new ServiceException("预算数据异常，检查数据");
                }

                DataDspSlot InputdataDspSlot = new DataDspSlot();
                InputdataDspSlot.setId(dataDspSlotList.get(0).getId());
                InputdataDspSlot.setSpend(dspSpend);
                InputdataDspSlot.setIncome(dspIncomes);
                InputdataDspSlot.setTableName("data_dsp_slot_day_" + dataTableTime);
                int i = dataDspSlotMapper.updateDataDspSlot(InputdataDspSlot);



                // 根据日期 dspSlotCode 去查 公司id,然后再去跟新这个数据
//                DspSlotInfoMapper
                DspSlotInfo dspSlotInfo = dspSlotInfoMapper.selectDspSlotInfoById(dataDspSlotList.get(0).getDspSlotId());
                if (dspSlotInfo == null) {
                    throw new ServiceException("dspSlotInfo 预算数据异常，检查数据,公司数据未能查到");
                }
                DspIncome dspIncome = dspIncomeMapper.selectDspIncomeById(dspSlotInfo.getId());
                if (dspIncome == null) {
                    throw new ServiceException("dspIncome 预算数据异常，检查数据,公司数据未能查到");
                }


                DspIncome exce = new DspIncome();
                exce.setId(dspIncome.getId());
                if (dspIncomeExcel.getInputTime() == null) {
                    exce.setInputTime(new Date());
                }
                exce.setUpdateTime(new Date());
                exce.setCreateTime(new Date());
                dspIncomeMapper.updateDspIncome(exce);
                successNum++;

            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + dspIncomeExcel.getDspSlotCode() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        successMsg.append("<br/>共 ").append(successNum).append(" 条导入成功");
        if (failureNum > 0) {
            successMsg.append("<br/>共 ").append(failureNum).append(" 条导入失败")
                    .append(failureMsg);
        }

        return successMsg.toString();
    }

}

package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.IDspInputCompanyExceService;

import static io.vertx.core.http.impl.HttpClientConnection.log;

/**
 * 导入流水Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-18
 */
@Service
public class DspInputCompanyExceServiceImpl implements IDspInputCompanyExceService 
{
    @Autowired
    private DspInputCompanyExceMapper dspInputCompanyExceMapper;



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
     * 查询导入流水
     * 
     * @param id 导入流水主键
     * @return 导入流水
     */
    @Override
    public DspInputCompanyExce selectDspInputCompanyExceById(Long id)
    {
        return dspInputCompanyExceMapper.selectDspInputCompanyExceById(id);
    }

    /**
     * 查询导入流水列表
     * 
     * @param dspInputCompanyExce 导入流水
     * @return 导入流水
     */
    @Override
    public List<DspInputCompanyExce> selectDspInputCompanyExceList(DspInputCompanyExce dspInputCompanyExce)
    {
        return dspInputCompanyExceMapper.selectDspInputCompanyExceList(dspInputCompanyExce);
    }

    /**
     * 新增导入流水
     * 
     * @param dspInputCompanyExce 导入流水
     * @return 结果
     */
    @Override
    public int insertDspInputCompanyExce(DspInputCompanyExce dspInputCompanyExce)
    {
//        dspInputCompanyExce.setCreateTime(DateUtils.getNowDate());
        return dspInputCompanyExceMapper.insertDspInputCompanyExce(dspInputCompanyExce);
    }

    /**
     * 修改导入流水
     * 
     * @param dspInputCompanyExce 导入流水
     * @return 结果
     */
    @Override
    public int updateDspInputCompanyExce(DspInputCompanyExce dspInputCompanyExce)
    {
        dspInputCompanyExce.setUpdateTime(DateUtils.getNowDate());
        return dspInputCompanyExceMapper.updateDspInputCompanyExce(dspInputCompanyExce);
    }

    /**
     * 批量删除导入流水
     * 
     * @param ids 需要删除的导入流水主键
     * @return 结果
     */
    @Override
    public int deleteDspInputCompanyExceByIds(Long[] ids)
    {
        return dspInputCompanyExceMapper.deleteDspInputCompanyExceByIds(ids);
    }

    /**
     * 删除导入流水信息
     * 
     * @param id 导入流水主键
     * @return 结果
     */
    @Override
    public int deleteDspInputCompanyExceById(Long id)
    {
        return dspInputCompanyExceMapper.deleteDspInputCompanyExceById(id);
    }

    @Override
    public String importIncomeExcel(List<DspIncome> dspIncomeList, boolean updateSupport, String operName) {
        if (dspIncomeList == null || dspIncomeList.isEmpty()) {
            throw new ServiceException("导入我方收益不能为空");
        }

//        int successNum = 0;
//        int failureNum = 0;
//        StringBuilder successMsg = new StringBuilder();
//        StringBuilder failureMsg = new StringBuilder();

//        DspInputExceDTO dspInputExceDTO = new DspInputExceDTO();

        List<DspInputExceDTO> result = new ArrayList<>();

        DspInputExceDTO currentGroup = null; // 当前分组

        int total = 0;  // 统计条数

        for (DspIncome dspIncomeExcel : dspIncomeList) {
            total += 1;
            String dspSlotCode = dspIncomeExcel.getDspSlotCode();
            // 1. 如果 dspSlotCode 有值 → 新建一组
            if (dspSlotCode != null && !dspSlotCode.trim().isEmpty()) {

                currentGroup = new DspInputExceDTO();
                currentGroup.setDspSlotCode(dspSlotCode);
                currentGroup.setInputTime(dspIncomeExcel.getInputTime());
                currentGroup.setIncome(dspIncomeExcel.getIncome());
                result.add(currentGroup);
            }

            // 2. 处理子数据（每一行都有）
            if (currentGroup == null) {
                //  防御：第一行就是空
                continue;
            }

            SspInputExceData child = new SspInputExceData();
            child.setSspSlotId(dspIncomeExcel.getSspSlotId());
            child.setDspDealRatio(dspIncomeExcel.getProportion());

//            currentGroup.addDspInputExceDTO.add(child);
            currentGroup.addDspInputExceDTO(child);
        }


        for (DspInputExceDTO dspInputExceDTO : result) {
            String dspSlotCode = dspInputExceDTO.getDspSlotCode();
            // 首先 100 是将元转化成分， 10 这个是为了让0.1分钱变成 1
            Long dspIncom = dspInputExceDTO.getIncome() * 10 * 100;
            // 从这里面获取预算的曝光次数

            Date inputTime = dspInputExceDTO.getInputTime();
            LocalDate date = inputTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String dspInputTime = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String dataTableTime = date.format(DateTimeFormatter.ofPattern("yyyyMM"));

            System.out.println("dspInputTime: " + dspInputTime);
            System.out.println("dataTableTime: " + dataTableTime);

            long showCount = 0;
            // 曝光总次数
            for (SspInputExceData sspInputExceData : dspInputExceDTO.getDspInputExceDTOList()) {
//                Integer dspDealRatio = sspInputExceData.getDspDealRatio();

                DataSspSlot dataSspSlot = new DataSspSlot();
                dataSspSlot.setDspSlotCode(dspSlotCode);
                dataSspSlot.setSspSlotId(sspInputExceData.getSspSlotId());
                dataSspSlot.setTableName("data_ssp_slot_day_" + dataTableTime);
                dataSspSlot.setDate(Long.valueOf(dspInputTime));
                List<DataSspSlot> dataSspSlots = dataSspSlotMapper.selectDataSspSlotListInputExce(dataSspSlot);
                if (dataSspSlots.size() == 0) {
                    showCount += dataSspSlots.get(0).getShowPv();
                } else {
                    return "dataSspSlots size=" + dataSspSlots.size() + "数据异常,查找出了多条数据";
                }
            }
//            Long RemainingIncome = 0;
            Long CountIncome = 0L;
            for (SspInputExceData sspInputExceData : dspInputExceDTO.getDspInputExceDTOList()) {
//                Integer dspDealRatio = sspInputExceData.getDspDealRatio();

                DataSspSlot dataSspSlot = new DataSspSlot();
                dataSspSlot.setDspSlotCode(dspSlotCode);
                dataSspSlot.setSspSlotId(sspInputExceData.getSspSlotId());
                dataSspSlot.setTableName("data_sp_slot_day_" + dataTableTime);
                dataSspSlot.setDate(Long.valueOf(dspInputTime));
                List<DataSspSlot> dataSspSlots = dataSspSlotMapper.selectDataSspSlotListInputExce(dataSspSlot);
                if (dataSspSlots.size() == 0) {

//                 流量占比   展现 / 总展现 * 100
                    long ratio = dataSspSlots.get(0).getShowPv() / showCount * 100;
                    // 利润 * 分成系数 / 100 * 展现 / 100 （这个100 是把元转化成了分） 后面的这个10 是我用来将分后面的小数转化的（在获取的时候会去掉）
                    long incom = dspIncom * sspInputExceData.getDspDealRatio() * ratio;
                    dataSspSlot.setShowPv(incom);
                    int i = dataSspSlotMapper.updateDataSspSlot(dataSspSlot);
                    if (i > 0) {
                        CountIncome += incom;
                    }

                } else {
                    return "dataSspSlots size=" + dataSspSlots.size() + "数据异常,查找出了多条数据";
                }
            }
            if (CountIncome < dspIncom) {
                Long RemainingIncome = CountIncome - dspIncom;
                DataDspSlot dataDspSlot = new DataDspSlot();
                dataDspSlot.setDspSlotCode(dspSlotCode);
                dataDspSlot.setDate(Long.valueOf(dspInputTime));
                dataDspSlot.setTableName("data_dsp_slot_day_" + dataTableTime);
                DataDspSlot dataDspSlot1 = dataDspSlotMapper.selectDataDspSlotOne(dataDspSlot);
                if (dataDspSlot1 != null) {
                    DataDspSlot dataDspSlot2 = new DataDspSlot();
                    dataDspSlot2.setId(dataDspSlot1.getId());
                    dataDspSlot2.setSpend(CountIncome);
                    dataDspSlot2.setIncome(RemainingIncome);
                    dataDspSlot2.setTableName("data_dsp_slot_day_" + dataTableTime);
                    int i = dataDspSlotMapper.updateDataDspSlot(dataDspSlot2);
                }
            }
        }
        return "数据跟新成功";
    }
}

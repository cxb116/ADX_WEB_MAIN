package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.DspCompany;

/**
 * 公司管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-24
 */
public interface DspCompanyMapper 
{
    /**
     * 查询公司管理
     * 
     * @param id 公司管理主键
     * @return 公司管理
     */
    public DspCompany selectDspCompanyById(Long id);

    /**
     * 查询公司管理列表
     * 
     * @param dspCompany 公司管理
     * @return 公司管理集合
     */
    public List<DspCompany> selectDspCompanyList(DspCompany dspCompany);

    /**
     * 新增公司管理
     * 
     * @param dspCompany 公司管理
     * @return 结果
     */
    public int insertDspCompany(DspCompany dspCompany);

    /**
     * 修改公司管理
     * 
     * @param dspCompany 公司管理
     * @return 结果
     */
    public int updateDspCompany(DspCompany dspCompany);

    /**
     * 删除公司管理
     * 
     * @param id 公司管理主键
     * @return 结果
     */
    public int deleteDspCompanyById(Long id);

    /**
     * 批量删除公司管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDspCompanyByIds(Long[] ids);
}

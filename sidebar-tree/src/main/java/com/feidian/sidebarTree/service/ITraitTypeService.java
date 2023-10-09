package com.feidian.sidebarTree.service;

import java.util.List;
import com.feidian.sidebarTree.domain.TraitType;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Service接口
 *
 * @author feidian
 * @date 2023-07-03
 */
public interface ITraitTypeService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param traitTypeId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TraitType selectTraitTypeByTraitTypeId(Long traitTypeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param traitType 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TraitType> selectTraitTypeList(TraitType traitType);

    /**
     * 新增【请填写功能名称】
     *
     * @param traitType 【请填写功能名称】
     * @return 结果
     */
    public int insertTraitType(TraitType traitType);

    /**
     * 修改【请填写功能名称】
     *
     * @param traitType 【请填写功能名称】
     * @return 结果
     */
    public int updateTraitType(TraitType traitType);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traitTypeIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTraitTypeByTraitTypeIds(Long[] traitTypeIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traitTypeId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTraitTypeByTraitTypeId(Long traitTypeId);


    //下拉框 性状类型
    public List<String> selectTraitTypeName();

    //1.5 搜索框 通过 性状类型名称 模糊匹配 查询
    public List<TraitType> selectTraitType(String trait_type_name);

    //1.5
    public Integer checkTraitTypeName(String trait_type_name);

    //1.7 更新关联表 查询type对应的type_id
    public Long selectIdByTypeName(String type);

}
package com.feidian.sidebarTree.service.impl;

import java.util.List;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.mapper.TraitTypeMapper;
import com.feidian.sidebarTree.domain.TraitType;
import com.feidian.sidebarTree.service.ITraitTypeService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author feidian
 * @date 2023-07-03
 */
@Service
public class TraitTypeServiceImpl implements ITraitTypeService
{
    @Autowired
    private TraitTypeMapper traitTypeMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param traitTypeId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TraitType selectTraitTypeByTraitTypeId(Long traitTypeId)
    {
        return traitTypeMapper.selectTraitTypeByTraitTypeId(traitTypeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param traitType 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TraitType> selectTraitTypeList(TraitType traitType)
    {
        return traitTypeMapper.selectTraitTypeList(traitType);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param traitType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTraitType(TraitType traitType)
    {
        traitType.setCreateTime(DateUtils.getNowDate());
        traitType.setCreateBy(SecurityUtils.getUserId().toString());
        traitType.setUpdateTime(DateUtils.getNowDate());
        traitType.setUpdateBy(SecurityUtils.getUserId().toString());
        return traitTypeMapper.insertTraitType(traitType);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param traitType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTraitType(TraitType traitType)
    {
        traitType.setUpdateTime(DateUtils.getNowDate());
        traitType.setUpdateBy(SecurityUtils.getUserId().toString());
        return traitTypeMapper.updateTraitType(traitType);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traitTypeIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTraitTypeByTraitTypeIds(Long[] traitTypeIds)
    {
        return traitTypeMapper.deleteTraitTypeByTraitTypeIds(traitTypeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traitTypeId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTraitTypeByTraitTypeId(Long traitTypeId)
    {
        return traitTypeMapper.deleteTraitTypeByTraitTypeId(traitTypeId);
    }
    @Override
    //下拉框 性状类型
    public List<String> selectTraitTypeName(){
        return traitTypeMapper.selectTraitTypeName();
    }

    @Override
    //1.5 搜索框 通过 性状类型名称 模糊匹配 查询
    public List<TraitType> selectTraitType(String trait_type_name){
        return traitTypeMapper.selectTraitType(trait_type_name);
    }

    @Override
    //1.5
    public Integer checkTraitTypeName(String trait_type_name){
        return traitTypeMapper.checkTraitTypeName(trait_type_name);
    }

    @Override
    //1.7 更新关联表 查询type对应的type_id
    public Long selectIdByTypeName(String type){
        return traitTypeMapper.selectIdByTypeName(type);
    }

}
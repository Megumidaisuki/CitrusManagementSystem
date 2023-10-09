package com.feidian.sidebarTree.service.impl;

import java.util.List;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.SecurityUtils;
import com.feidian.sidebarTree.domain.Trait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.mapper.AsTraitTypeMapper;
import com.feidian.sidebarTree.domain.AsTraitType;
import com.feidian.sidebarTree.service.IAsTraitTypeService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author feidian
 * @date 2023-07-05
 */
@Service
public class AsTraitTypeServiceImpl implements IAsTraitTypeService
{
    @Autowired
    private AsTraitTypeMapper asTraitTypeMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param asTraitTypeId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public AsTraitType selectAsTraitTypeByAsTraitTypeId(Long asTraitTypeId)
    {
        return asTraitTypeMapper.selectAsTraitTypeByAsTraitTypeId(asTraitTypeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param asTraitType 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<AsTraitType> selectAsTraitTypeList(AsTraitType asTraitType)
    {
        return asTraitTypeMapper.selectAsTraitTypeList(asTraitType);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param asTraitType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAsTraitType(AsTraitType asTraitType)
    {
        asTraitType.setCreateTime(DateUtils.getNowDate());
        asTraitType.setCreateBy(SecurityUtils.getUserId().toString());
        asTraitType.setUpdateTime(DateUtils.getNowDate());
        asTraitType.setUpdateBy(SecurityUtils.getUserId().toString());
        return asTraitTypeMapper.insertAsTraitType(asTraitType);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param asTraitType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAsTraitType(AsTraitType asTraitType)
    {
        asTraitType.setUpdateTime(DateUtils.getNowDate());
        asTraitType.setUpdateBy(SecurityUtils.getUserId().toString());
        return asTraitTypeMapper.updateAsTraitType(asTraitType);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param asTraitTypeIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAsTraitTypeByAsTraitTypeIds(Long[] asTraitTypeIds)
    {
        return asTraitTypeMapper.deleteAsTraitTypeByAsTraitTypeIds(asTraitTypeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param asTraitTypeId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAsTraitTypeByAsTraitTypeId(Long asTraitTypeId)
    {
        return asTraitTypeMapper.deleteAsTraitTypeByAsTraitTypeId(asTraitTypeId);
    }


    //1.7 大查询 多表查询 返回需要高亮的数据的
    @Override
    public List<Trait> selectHighlight(String type, String name) {
        return asTraitTypeMapper.selectHighlight(type, name);
    }

    @Override
    public List<Trait> selectHighlightin(String type, String name) {
        return asTraitTypeMapper.selectHighlightin(type, name);
    }

    //1.7 更新关联表中的数据
    @Override
    public int CheckUpdate(Long trait_id, String trait_type_name) {
        return asTraitTypeMapper.CheckUpdate(trait_id, trait_type_name);
    }

    //1.7 模糊匹配性状名称
    @Override
    public List<String> selectTraitName(String  trait_name){
        return asTraitTypeMapper.selectTraitName(trait_name);
    }

}

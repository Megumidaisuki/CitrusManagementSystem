package com.feidian.sidebarTree.service;

import java.util.List;
import com.feidian.sidebarTree.domain.AsTraitType;
import com.feidian.sidebarTree.domain.Trait;

/**
 * 【请填写功能名称】Service接口
 *
 * @author feidian
 * @date 2023-07-05
 */
public interface IAsTraitTypeService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param asTraitTypeId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AsTraitType selectAsTraitTypeByAsTraitTypeId(Long asTraitTypeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param asTraitType 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AsTraitType> selectAsTraitTypeList(AsTraitType asTraitType);

    /**
     * 新增【请填写功能名称】
     *
     * @param asTraitType 【请填写功能名称】
     * @return 结果
     */
    public int insertAsTraitType(AsTraitType asTraitType);

    /**
     * 修改【请填写功能名称】
     *
     * @param asTraitType 【请填写功能名称】
     * @return 结果
     */
    public int updateAsTraitType(AsTraitType asTraitType);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param asTraitTypeIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteAsTraitTypeByAsTraitTypeIds(Long[] asTraitTypeIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param asTraitTypeId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAsTraitTypeByAsTraitTypeId(Long asTraitTypeId);


    //大查询 多表查询 返回需要高亮的数据
    public List<Trait> selectHighlight(String type, String name);

    public List<Trait> selectHighlightin(String type, String name);


    //更新关联表中的数据
    public int CheckUpdate(Long trait_id, String trait_type_name);


    //1.7 模糊匹配性状名称 x
    public List<String> selectTraitName(String  trait_name);


}

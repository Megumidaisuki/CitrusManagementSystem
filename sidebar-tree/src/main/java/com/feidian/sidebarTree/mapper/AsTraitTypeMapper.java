package com.feidian.sidebarTree.mapper;

import java.util.List;
import com.feidian.sidebarTree.domain.AsTraitType;
import com.feidian.sidebarTree.domain.Trait;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author feidian
 * @date 2023-07-05
 */
@Mapper
public interface AsTraitTypeMapper
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
     * 删除【请填写功能名称】
     *
     * @param asTraitTypeId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAsTraitTypeByAsTraitTypeId(Long asTraitTypeId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param asTraitTypeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAsTraitTypeByAsTraitTypeIds(Long[] asTraitTypeIds);

    //大查询 多表查询 返回需要高亮的数据的
    public List<Trait> selectHighlight(@Param("type") String type, @Param("name") String name);

    public List<Trait> selectHighlightin(@Param("type") String type, @Param("name") String name);

    //更新关联表中的数据
    public int CheckUpdate(@Param("trait_id") Long trait_id, @Param("type") String trait_type_name);


    //1.7 模糊匹配性状名称 x
    public List<String> selectTraitName(@Param("trait_name") String  trait_name);

}
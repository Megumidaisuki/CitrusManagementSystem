package com.feidian.sidebarTree.mapper;

import java.util.List;
import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.domain.vo.DataAnalysisVO;
import com.feidian.sidebarTree.domain.vo.TraitTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author feidian
 * @date 2023-07-03
 */
@Mapper
public interface TraitMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param traitId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Trait selectTraitByTraitId(Long traitId);

    public Trait selectTraitByTraitIdWithoutDeleted(Long traitId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param trait 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Trait> selectTraitList(Trait trait);

    public List<Trait> selectTraitListWithoutDeleted(Trait trait);

    /**
     * 新增【请填写功能名称】
     *
     * @param trait 【请填写功能名称】
     * @return 结果
     */
    public int insertTrait(Trait trait);

    /**
     * 修改【请填写功能名称】
     *
     * @param trait 【请填写功能名称】
     * @return 结果
     */
    public int updateTrait(Trait trait);

    /**
     * 删除【请填写功能名称】
     *
     * @param traitId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTraitByTraitId(Long traitId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traitIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTraitByTraitIds(Long[] traitIds);

    //1.6 大查询 模糊匹配 根据性状名称模糊匹配
    public List<Trait> selectTrait(@Param("trait_name") String trait_name);

    //1.6
    public Integer checkTraitName(@Param("trait_name") String trait_name);

    //查询性状Id、性状名称、性状类型id、性状类型名称
    List<TraitTypeVO> selectTraitAndTraitTypeList();
}

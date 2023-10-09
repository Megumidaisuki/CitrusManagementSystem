package com.feidian.sidebarTree.mapper;

import java.util.List;
import java.util.Map;

import com.feidian.sidebarTree.domain.Species;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * speciesMapper接口
 *
 * @author feidian
 * @date 2023-07-01
 */
@Mapper
public interface SpeciesMapper
{
    /**
     * 查询species
     *
     * @param speciesId species主键
     * @return species
     */
    public Species selectSpeciesBySpeciesId(Long speciesId);

    public Species selectSpeciesBySpeciesIdWithoutDeleted(Long speciesId);

    /**
     * 查询species列表
     *
     * @param species species
     * @return species集合
     */
    public List<Species> selectSpeciesList(Species species);

    public List<Species> selectSpeciesListWithoutDeleted(Species species);

    /**
     * 新增species
     *
     * @param species species
     * @return 结果
     */
    public int insertSpecies(Species species);

    /**
     * 修改species
     *
     * @param species species
     * @return 结果
     */
    public int updateSpecies(Species species);

    /**
     * 删除species
     *
     * @param speciesId species主键
     * @return 结果
     */
    public int deleteSpeciesBySpeciesId(Long speciesId);

    /**
     * 批量删除species
     *
     * @param speciesIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpeciesBySpeciesIds(Long[] speciesIds);

    List<Species> ListSpecies();

    //1.3 搜索框 大查询 根据 物种名称 模糊匹配
    public List<Species> selectSpecies(@Param("species_name") String species_name);

    public Integer checkSpeciesName(@Param("species_name") String species_name);

    //1.4
    public Species selectSpeciesBySpeciesName(@Param("species_name") String species_name);
}

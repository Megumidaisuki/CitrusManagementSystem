package com.feidian.sidebarTree.service;

import java.util.List;
import com.feidian.sidebarTree.domain.Species;

/**
 * speciesService接口
 * 
 * @author feidian
 * @date 2023-07-01
 */
public interface ISpeciesService 
{
    /**
     * 查询species
     * 
     * @param speciesId species主键
     * @return species
     */
    public Species selectSpeciesBySpeciesId(Long speciesId);

    /**
     * 查询species列表
     * 
     * @param species species
     * @return species集合
     */
    public List<Species> selectSpeciesList(Species species);

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
     * 批量删除species
     * 
     * @param speciesIds 需要删除的species主键集合
     * @return 结果
     */
    public int deleteSpeciesBySpeciesIds(Long[] speciesIds);

    /**
     * 删除species信息
     * 
     * @param speciesId species主键
     * @return 结果
     */
    public int deleteSpeciesBySpeciesId(Long speciesId);

    //1.3 搜索框 大查询 根据 物种名称 模糊匹配
    public List<Species> selectSpecies(String species_name);

    //1.3
    public Integer checkSpeciesName(String species_name);

    //1.4
    public Species selectSpeciesBySpeciesName(String species_name);
}

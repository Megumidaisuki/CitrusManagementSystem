package com.feidian.sidebarTree.service;

import java.util.List;
import com.feidian.sidebarTree.domain.Population;
import com.feidian.sidebarTree.domain.PopulationUse;
import org.apache.ibatis.annotations.Param;

/**
 * populationService接口
 * 
 * @author feidian
 * @date 2023-07-01
 */
public interface IPopulationService 
{
    /**
     * 查询population
     * 
     * @param populationId population主键
     * @return population
     */
    public Population selectPopulationByPopulationId(Long populationId);

    /**
     * 查询population列表
     * 
     * @param population population
     * @return population集合
     */
    public List<Population> selectPopulationList(Population population);

    /**
     * 新增population
     * 
     * @param population population
     * @return 结果
     */
    public int insertPopulation(Population population);

    /**
     * 修改population
     * 
     * @param population population
     * @return 结果
     */
    public int updatePopulation(Population population);

    /**
     * 批量删除population
     * 
     * @param populationIds 需要删除的population主键集合
     * @return 结果
     */
    public int deletePopulationByPopulationIds(Long[] populationIds);

    /**
     * 删除population信息
     * 
     * @param populationId population主键
     * @return 结果
     */
    public int deletePopulationByPopulationId(Long populationId);


    //查询 群种所属物种
    public List<String> selectSpeciesName();

    //大查询 通过 所属物种 和 群体名称 查询
    public List<PopulationUse> selectPopulation(String species_name, String population_name);

    //1.4
    public Integer checkPopulationName(String population_name);

    //1.4
    public Long GetSpeciesIdBySpeciesName(String species_name);

}

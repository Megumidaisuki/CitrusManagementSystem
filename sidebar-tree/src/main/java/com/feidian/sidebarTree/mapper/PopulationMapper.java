package com.feidian.sidebarTree.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * populationMapper接口
 *
 * @author feidian
 * @date 2023-07-01
 */
@Mapper
public interface PopulationMapper
{
    /**
     * 查询population
     *
     * @param populationId population主键
     * @return population
     */
    public Population selectPopulationByPopulationId(Long populationId);

    public Population selectPopulationByPopulationIdWithoutDeleted(Long populationId);

    /**
     * 查询population列表
     *
     * @param population population
     * @return population集合
     */
    public List<Population> selectPopulationList(Population population);

    public List<Population> selectPopulationListWithoutDeleted(Population population);

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
     * 删除population
     *
     * @param populationId population主键
     * @return 结果
     */
    public int deletePopulationByPopulationId(Long populationId);

    /**
     * 批量删除population
     *
     * @param populationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePopulationByPopulationIds(Long[] populationIds);

    List<Population> selectPopulationByListId(List<Long> populationIds);


    //查询 群种所属物种
    public List<String> selectSpeciesName();

    //大查询 通过 所属物种 和 群体名称 查询
    public List<PopulationUse> selectPopulation(@Param("species_name") String species_name, @Param("population_name") String population_name);

    //1.4
    public Integer checkPopulationName(@Param("population_name") String population_name);

    //1.4
    public Long GetSpeciesIdBySpeciesName(@Param("species_name") String species_name);

    //通过群体id获取对应的物种名称
    public String selectSpeciesNameByPopulationId(@Param("population_id") Long population_id);
}

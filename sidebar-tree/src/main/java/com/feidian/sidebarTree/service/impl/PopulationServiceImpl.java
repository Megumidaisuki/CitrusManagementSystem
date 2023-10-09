package com.feidian.sidebarTree.service.impl;

import java.util.List;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.SecurityUtils;
import com.feidian.sidebarTree.domain.PopulationUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.mapper.PopulationMapper;
import com.feidian.sidebarTree.domain.Population;
import com.feidian.sidebarTree.service.IPopulationService;

/**
 * populationService业务层处理
 *
 * @author feidian
 * @date 2023-07-01
 */
@Service
public class PopulationServiceImpl implements IPopulationService
{
    @Autowired
    private PopulationMapper populationMapper;

    /**
     * 查询population
     *
     * @param populationId population主键
     * @return population
     */
    @Override
    public Population selectPopulationByPopulationId(Long populationId)
    {
        return populationMapper.selectPopulationByPopulationId(populationId);
    }

    /**
     * 查询population列表
     *
     * @param population population
     * @return population
     */
    @Override
    public List<Population> selectPopulationList(Population population)
    {
        return populationMapper.selectPopulationList(population);
    }

    /**
     * 新增population
     *
     * @param population population
     * @return 结果
     */
    @Override
    public int insertPopulation(Population population)
    {
        population.setCreateTime(DateUtils.getNowDate());
        population.setCreateBy(SecurityUtils.getUserId().toString());
        population.setUpdateTime(DateUtils.getNowDate());
        population.setUpdateBy(SecurityUtils.getUserId().toString());
        return populationMapper.insertPopulation(population);
    }

    /**
     * 修改population
     *
     * @param population population
     * @return 结果
     */
    @Override
    public int updatePopulation(Population population)
    {
        population.setUpdateTime(DateUtils.getNowDate());
        population.setUpdateBy(SecurityUtils.getUserId().toString());
        return populationMapper.updatePopulation(population);
    }

    /**
     * 批量删除population
     *
     * @param populationIds 需要删除的population主键
     * @return 结果
     */
    @Override
    public int deletePopulationByPopulationIds(Long[] populationIds)
    {
        return populationMapper.deletePopulationByPopulationIds(populationIds);
    }

    /**
     * 删除population信息
     *
     * @param populationId population主键
     * @return 结果
     */
    @Override
    public int deletePopulationByPopulationId(Long populationId)
    {
        return populationMapper.deletePopulationByPopulationId(populationId);
    }

    //查询 群种所属物种
    @Override
    public List<String> selectSpeciesName() {
        return populationMapper.selectSpeciesName();
    }

    //大查询 通过 所属物种 和 群体名称 查询
    @Override
    public List<PopulationUse> selectPopulation(String species_name, String population_name) {
        return populationMapper.selectPopulation(species_name, population_name);
    }

    //1.4
    @Override
    public Integer checkPopulationName(String population_name) {
        return populationMapper.checkPopulationName(population_name);
    }

    //1.4
    public Long GetSpeciesIdBySpeciesName(String species_name) {
        return populationMapper.GetSpeciesIdBySpeciesName(species_name);
    }

}

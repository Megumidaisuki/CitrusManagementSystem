package com.feidian.sidebarTree.service.impl;

import java.util.List;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.SecurityUtils;
import com.feidian.sidebarTree.domain.Population;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.mapper.SpeciesMapper;
import com.feidian.sidebarTree.domain.Species;
import com.feidian.sidebarTree.service.ISpeciesService;

/**
 * speciesService业务层处理
 *
 * @author feidian
 * @date 2023-07-01
 */
@Service
public class SpeciesServiceImpl implements ISpeciesService
{
    @Autowired
    private SpeciesMapper speciesMapper;

    /**
     * 查询species
     *
     * @param speciesId species主键
     * @return species
     */
    @Override
    public Species selectSpeciesBySpeciesId(Long speciesId)
    {
        return speciesMapper.selectSpeciesBySpeciesId(speciesId);
    }

    /**
     * 查询species列表
     *
     * @param species species
     * @return species
     */
    @Override
    public List<Species> selectSpeciesList(Species species)
    {
        return speciesMapper.selectSpeciesList(species);
    }

    /**
     * 新增species
     *
     * @param species species
     * @return 结果
     */
    @Override
    public int insertSpecies(Species species)
    {
        species.setCreateTime(DateUtils.getNowDate());
        species.setCreateBy(SecurityUtils.getUserId().toString());
        species.setUpdateTime(DateUtils.getNowDate());
        species.setUpdateBy(SecurityUtils.getUserId().toString());
        return speciesMapper.insertSpecies(species);
    }

    /**
     * 修改species
     *
     * @param species species
     * @return 结果
     */
    @Override
    public int updateSpecies(Species species)
    {
        species.setUpdateTime(DateUtils.getNowDate());
        species.setUpdateBy(SecurityUtils.getUserId().toString());
        return speciesMapper.updateSpecies(species);
    }

    /**
     * 批量删除species
     *
     * @param speciesIds 需要删除的species主键
     * @return 结果
     */
    @Override
    public int deleteSpeciesBySpeciesIds(Long[] speciesIds)
    {
        return speciesMapper.deleteSpeciesBySpeciesIds(speciesIds);
    }

    /**
     * 删除species信息
     *
     * @param speciesId species主键
     * @return 结果
     */
    @Override
    public int deleteSpeciesBySpeciesId(Long speciesId)
    {
        return speciesMapper.deleteSpeciesBySpeciesId(speciesId);
    }

    //1.3 搜索框 大查询 根据 物种名称 模糊匹配
    @Override
    public List<Species> selectSpecies(String species_name){
        return speciesMapper.selectSpecies(species_name);
    }

    //1.4
    @Override
    public Integer checkSpeciesName(String species_name) {
        return speciesMapper.checkSpeciesName(species_name);
    }

    //1.4
    @Override
    public Species selectSpeciesBySpeciesName(String species_name) {
        return speciesMapper.selectSpeciesBySpeciesName(species_name);
    }
}

package com.feidian.sidebarTree.service.impl;

import java.util.*;

import com.feidian.sidebarTree.domain.*;
import com.feidian.sidebarTree.mapper.PhenotypeFileMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.service.IPhenotypeService;

/**
 * 型Service业务层处理
 *
 * @author feidian
 * @date 2023-07-01
 */
@Service
public class PhenotypeServiceImpl implements IPhenotypeService {
    @Autowired
    private PhenotypeFileMapper phenotypeFileMapper;

    @Override
    public List<String> listAllPhenotypeTableNames() {
        return null;
    }

    @Override
    public List<Phenotype> listAllPhenotype(List<String> TableNames) {
        return null;
    }


    @Override
    public List<String> selectTableName (Long speciesId, Long populationId, String year, String location) {
        List<String> tableName = phenotypeFileMapper.selectTableNameByFourElement(speciesId, populationId, year, location);
        return tableName;
    }


    @Override
    public List<Phenotype> selectTable(Long speciesId, Long populationId, String year, String location, String tableName)  {
        Integer isIn = phenotypeFileMapper.selectTableIsInPhenotypeFile(tableName);
        if(isIn == 0){
            throw new RuntimeException ("表不在表型表中，请添加对应表型表");
        }

        List<Phenotype> phenotypes = phenotypeFileMapper.selectPhenotypeByTableName(tableName);
        return phenotypes;
    }

   /* @Autowired
    private SpeciesMapper speciesMapper;

    @Autowired
    private PopulationMapper populationMapper;


    @Autowired
    private GermplasmParentsMapper germplasmParentsMapper;*/


    /**
     * 查询列出所有物种
     *
     * @return 所有species
     *//*
    @Override
    public Map<Long, String> listSpecies() {
        Map<Long, String> speciesMap = new HashMap<>();

        List<Species> species = speciesMapper.ListSpecies();
        for (Species sp : species) {
            speciesMap.put(sp.getSpeciesId(), sp.getSpeciesName());
        }
        return speciesMap;
    }*/

   /* *//**
     * 列出群落
     *
     * @return
     *//*
    @Override
    public Map<Long, String> listPopulation(Long speciesId) {
        Map<Long, String> populationMap = new HashMap<>();
        final List<Long> populationIds = phenotypeFileMapper.selectPopulationBySpecies(speciesId);
        if (populationIds.isEmpty()) {
            throw new RuntimeException("所选物种下无种群");
        }
        List<Population> populations = populationMapper.selectPopulationByListId(populationIds);
        for (Population population : populations) {
            populationMap.put(population.getPopulationId(), population.getPopulationName());
        }
        return populationMap;
    }
*/
    /**
     * 查询年份
     *
     * @param speciesId
     * @param populationId
     * @return
     *//*
    @Override
    public List<String> listYears(Long speciesId, Long populationId) {
        List<String> years = phenotypeFileMapper.selectPhenotypeListYears(speciesId, populationId);
        if(years.isEmpty()){
            throw new RuntimeException("数据库日期未填写");
        }
        for (int i = 0; i < years.size(); i++) {
            String year = years.get(i);
            year = year.substring(0, 4);
            years.set(i, year);
        }
        return years;
    }
*/
 /*   @Override
    public List<String> listLocations(Long speciesId, Long populationId, String year) {
        List<String> locations = phenotypeFileMapper.selectPhenotypeListLocations(speciesId, populationId, year);

        if(locations.isEmpty()){
            throw new RuntimeException("数据库位置未填写");
        }

        return locations;
    }
*/

    /*// 主函数，参数为物料ID，返回值为该物料的所有关系
    @Override
    public List<Relationship> selectMaterialRelationships(String material_id) {
        // 构建一个映射表，用于快速查找父母
        Map<String, GermplasmParents> parentLookup = new HashMap<>();
        // 对数据库中所有的GermplasmParents对象进行遍历，放入映射表中
        for (GermplasmParents gp : germplasmParentsMapper.getAllGermplasmParents()) {
            parentLookup.put(gp.getMaterialId(), gp);
        }

        // 创建一个列表，用于存储找到的所有关系
        List<Relationship> relations = new ArrayList<>();
        // 创建一个集合，用于存储已经访问过的物料ID，避免循环
        Set<String> visitedMaterials = new HashSet<>();

        // 从给定的物料ID开始，进行深度优先搜索
        dfs(material_id, parentLookup, relations, visitedMaterials);

        // 返回找到的所有关系
        return relations;
    }

    // 辅助函数，进行深度优先搜索
    private void dfs(String materialId, Map<String, GermplasmParents> parentLookup, List<Relationship> relations, Set<String> visitedMaterials) {
        // 如果已经访问过该物料ID，为了避免循环，直接返回
        if (visitedMaterials.contains(materialId)) {
            return;
        }

        // 标记该物料ID已经被访问过
        visitedMaterials.add(materialId);

        // 获取该物料ID的父母
        GermplasmParents gp = parentLookup.get(materialId);
        // 如果该物料ID的父母存在
        if (gp != null) {
            String mother = gp.getMother();
            String father = gp.getFather();

            // 将物料ID与其父母的关系添加到关系列表中
            relations.add(new Relationship(materialId, mother, 0));
            relations.add(new Relationship(materialId, father, 1));

            // 递归地访问父母
            dfs(mother, parentLookup, relations, visitedMaterials);
            dfs(father, parentLookup, relations, visitedMaterials);
        }
    }

*/
}


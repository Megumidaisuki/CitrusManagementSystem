package com.feidian.sidebarTree.service;

import com.feidian.sidebarTree.domain.Phenotype;

import java.util.List;
import java.util.Map;


/**
 * 型Service接口
 * 
 * @author feidian
 * @date 2023-07-01
 */
public interface IPhenotypeService 
{

    Map<Long,String> listSpecies();

    Map<Long,String> listPopulation(Long speciesId);

    List<String> listYears(Long speciesId, Long populationId);

    List<String> listLocations(Long speciesId, Long populationId, String year);


    List<Relationship> selectMaterialRelationships(String material_id);


    List<String> selectTableName(Long speciesId, Long populationId, String year, String location);
    List<Phenotype> selectTable(Long speciesId, Long populationId, String year, String location,String tableName) ;
    List<String> listAllPhenotypeTableNames();

    List<Phenotype> listAllPhenotype(List<String> TableNames);
}

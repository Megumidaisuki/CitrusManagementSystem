package com.feidian.sidebarTree.utils;

import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.mapper.PopulationMapper;
import com.feidian.sidebarTree.mapper.SpeciesMapper;
import com.feidian.sidebarTree.mapper.TraitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@Component
public class InfoUtil{
    private HashMap<String,Long> speciesMap = null;

    private HashMap<Long,String> speciesMapReverse = null;

    private HashMap<String,Long> populationsMap = null;

    private HashMap<Long,String> populationsMapReverse = null;

    private HashMap<String,Long> traitsMap = null;

    private HashMap<Long,String> traitsMapReverse = null;

    private HashMap<Long,Trait> traitsMapObjectReverse = null;

    @Autowired
    private SpeciesMapper speciesMapper;

    @Autowired
    private PopulationMapper populationMapper;

    @Autowired
    private TraitMapper traitMapper;


    public HashMap<String,Long> getSpeciesMap(){
//        if(speciesMap == null){
            List<Species> speciesList = speciesMapper.selectSpeciesListWithoutDeleted(new Species());
            HashMap<String, Long> speciesMap = new HashMap<>();
            for (Species species : speciesList) {
                speciesMap.put(species.getSpeciesName(), species.getSpeciesId());
            }
            this.speciesMap = speciesMap;
//        }
        return speciesMap;
    }

    public HashMap<String,Long> getPopulationsMap(){
//        if(populationsMap == null){
            List<Population> populationList = populationMapper.selectPopulationListWithoutDeleted(new Population());
            HashMap<String, Long> populationsMap = new HashMap<>();
            for (Population population : populationList) {
                populationsMap.put(population.getPopulationName(), population.getPopulationId());
            }
            this.populationsMap = populationsMap;
//        }
        return populationsMap;
    }

    public HashMap<String,Long> getTraitsMap(){
//        if(traitsMap == null){
            List<Trait> traitList = traitMapper.selectTraitListWithoutDeleted(new Trait());
            HashMap<String, Long> traitsMap = new HashMap<>();
            for (Trait trait : traitList) {
                traitsMap.put(trait.getTraitName(), trait.getTraitId());
            }
            this.traitsMap = traitsMap;
//        }
        return traitsMap;
    }

    public HashMap<Long,String> getSpeciesMapReverse(){
        List<Species> speciesList = speciesMapper.selectSpeciesListWithoutDeleted(new Species());
        HashMap<Long, String> speciesMap = new HashMap<>();
        for (Species species : speciesList) {
            speciesMap.put(species.getSpeciesId(), species.getSpeciesName());
        }
        this.speciesMapReverse = speciesMap;
        return speciesMapReverse;
    }

    public HashMap<Long,String> getPopulationsMapReverse(){
        List<Population> populationList = populationMapper.selectPopulationListWithoutDeleted(new Population());
        HashMap<Long, String> populationsMap = new HashMap<>();
        for (Population population : populationList) {
            populationsMap.put(population.getPopulationId(), population.getPopulationName());
        }
        this.populationsMapReverse = populationsMap;
        return populationsMapReverse;
    }

    public HashMap<Long,String> getTraitsMapReverse(){
        List<Trait> traitList = traitMapper.selectTraitListWithoutDeleted(new Trait());
        HashMap<Long, String> traitsMap = new HashMap<>();
        for (Trait trait : traitList) {
            traitsMap.put(trait.getTraitId(), trait.getTraitName());
        }
        this.traitsMapReverse = traitsMap;
        return traitsMapReverse;
    }

    public HashMap<Long,Trait> getTraitsObjectMapReverse(){
        List<Trait> traitList = traitMapper.selectTraitListWithoutDeleted(new Trait());
        HashMap<Long, Trait> traitsMap = new HashMap<>();
        for (Trait trait : traitList) {
            traitsMap.put(trait.getTraitId(), trait);
        }
        this.traitsMapObjectReverse = traitsMap;
        return traitsMapObjectReverse;
    }
}
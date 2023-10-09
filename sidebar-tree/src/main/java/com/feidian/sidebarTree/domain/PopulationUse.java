package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;

public class PopulationUse extends BaseEntity {

    private Long population_id;

    @Excel(name = "物种ID", sort = 2)
    private Long species_id;

    @Excel(name = "所属物种名称", sort = 3)
    private String species_name;

    @Excel(name = "群体名称", sort = 1)
    private String population_name;


    public PopulationUse() {
    }

    public PopulationUse(String species_name, String population_name, Long population_id, Long species_id) {
        this.species_name = species_name;
        this.population_name = population_name;
        this.population_id = population_id;
        this.species_id = species_id;
    }

    public String getSpecies_name() {
        return species_name;
    }

    public void setSpecies_name(String species_name) {
        this.species_name = species_name;
    }

    public String getPopulation_name() {
        return population_name;
    }

    public void setPopulation_name(String population_name) {
        this.population_name = population_name;
    }

    public Long getPopulation_id() {
        return population_id;
    }

    public void setPopulation_id(Long population_id) {
        this.population_id = population_id;
    }

    public Long getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(Long species_id) {
        this.species_id = species_id;
    }
}

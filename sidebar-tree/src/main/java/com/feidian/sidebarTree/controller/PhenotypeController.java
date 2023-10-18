package com.feidian.sidebarTree.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.feidian.sidebarTree.domain.Phenotype;
import com.feidian.sidebarTree.service.impl.PhenotypeServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.service.IPhenotypeService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

/**
 * 4 材料管理
 *
 * @author feidian
 * @date 2023-07-01
 */
@RestController
@RequestMapping("/materials/phenotype")
public class PhenotypeController extends BaseController {
    @Autowired
    private IPhenotypeService phenotypeService;

    //本项目好像都不会有这些数据
   /* @GetMapping("/species")
    public AjaxResult listSpecies() {
        return AjaxResult.success(phenotypeService.listSpecies());
    }

    @GetMapping("/population")
    public AjaxResult listPopulation(@RequestParam Long speciesId) {
        return AjaxResult.success(phenotypeService.listPopulation(speciesId));
    }

    @GetMapping("/years")
    public AjaxResult listYears(@RequestParam Long speciesId, @RequestParam Long populationId) {

        return AjaxResult.success(phenotypeService.listYears(speciesId, populationId));
    }

    @GetMapping("/locations")
    public AjaxResult listLocations(@RequestParam Long speciesId, @RequestParam Long populationId, @RequestParam String year) {

        return AjaxResult.success(phenotypeService.listLocations(speciesId, populationId, year));
    }*/
    @GetMapping("/tableName")
    public AjaxResult listTableName(@RequestParam(value = "speciesId",required=false)  Long speciesId, @RequestParam(value = "populationId",required=false)  Long populationId,
                                    @RequestParam(value = "year",required=false)  String year , @RequestParam(value = "location",required=false)  String location) {

        return AjaxResult.success(phenotypeService.selectTableName(speciesId, populationId, year,location));
    }

    @GetMapping("/table")
    public AjaxResult listTable(@RequestParam Long speciesId, @RequestParam Long populationId,
                                    @RequestParam String year, @RequestParam String location,@RequestParam String tableName) {

        return AjaxResult.success(phenotypeService.selectTable(speciesId, populationId, year,location,tableName));
    }


    //本项目没有父母本，所以不存在亲属关系
    /*@GetMapping("/relations")
    public AjaxResult ShowRelationships(@RequestParam String material_id){

        return AjaxResult.success(phenotypeService.selectMaterialRelationships(material_id));
    }*/

    /**
     * 查询所有表型
     *
     */
    @GetMapping("/list")
    public TableDataInfo listAllPhenotype(@RequestParam Long speciesId, @RequestParam Long populationId,
                                          @RequestParam String year, @RequestParam String location,@RequestParam String tableName) {

        startPage();
        List<Phenotype> phenotypes = phenotypeService.selectTable(speciesId, populationId, year, location, tableName);
        return getDataTable(phenotypes);
    }
}

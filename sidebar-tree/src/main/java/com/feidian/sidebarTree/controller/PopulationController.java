package com.feidian.sidebarTree.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.feidian.sidebarTree.domain.PopulationUse;
import com.feidian.sidebarTree.domain.Species;
import com.feidian.sidebarTree.service.ISpeciesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.domain.Population;
import com.feidian.sidebarTree.service.IPopulationService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

/**
 * populationController
 *
 * @author feidian
 * @date 2023-07-01
 */
@RestController
@RequestMapping("/population/population")
public class PopulationController extends BaseController
{
    @Autowired
    private IPopulationService populationService;

    @Autowired
    private ISpeciesService speciesService;

    /**
     * 查询population列表
     */
//    //@PreAuthorize("@ss.hasPermi('population:population:list')")
    @GetMapping("/list")
    public TableDataInfo list(Population population)
    {
        startPage();
        List<Population> list = populationService.selectPopulationList(population);
        return getDataTable(list);
    }

    /**
     * 导出population列表
     */
//    //@PreAuthorize("@ss.hasPermi('population:population:export')")
    @Log(title = "population", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Population population)
    {
        List<Population> list = populationService.selectPopulationList(population);
        ExcelUtil<Population> util = new ExcelUtil<Population>(Population.class);
        util.exportExcel(response, list, "population数据");
    }

    /**
     * 获取population详细信息
     */
//    //@PreAuthorize("@ss.hasPermi('population:population:query')")
    @GetMapping(value = "/{populationId}")
    public AjaxResult getInfo(@PathVariable("populationId") Long populationId)
    {
        return AjaxResult.success(populationService.selectPopulationByPopulationId(populationId));
    }

//    //@PreAuthorize("@ss.hasPermi('population:population:query')")
    @GetMapping("use/{populationId}")
    public AjaxResult getInfouse(@PathVariable("populationId") Long populationId)
    {
        Population population=populationService.selectPopulationByPopulationId(populationId);
        PopulationUse use=new PopulationUse();
        use.setPopulation_id(populationId);

        use.setPopulation_name(population.getPopulationName());
        Species species=speciesService.selectSpeciesBySpeciesId(population.getSpeciesId());
        use.setSpecies_name(species.getSpeciesName());
        use.setSpecies_id(species.getSpeciesId());

        use.setCreateBy(population.getCreateBy());
        use.setCreateTime(population.getCreateTime());
        use.setUpdateBy(population.getUpdateBy());
        use.setUpdateTime(population.getUpdateTime());
        use.setRemark(population.getRemark());

        return AjaxResult.success(use);
    }

    /**
     * 新增population
     */
//    //@PreAuthorize("@ss.hasPermi('population:population:add')")
    @Log(title = "population", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Population population)
    {
        return toAjax(populationService.insertPopulation(population));
    }

//    //@PreAuthorize("@ss.hasPermi('population:population:adduse')")
    @Log(title = "population", businessType = BusinessType.INSERT)
    @PostMapping("use")
    public AjaxResult adduse(@RequestBody PopulationUse populationuse)
    {
        Population population=new Population();

        Species species=speciesService.selectSpeciesBySpeciesName(populationuse.getSpecies_name());
        population.setSpeciesId(species.getSpeciesId());
        population.setPopulationName(populationuse.getPopulation_name());
        population.setRemark(populationuse.getRemark());
        return toAjax(populationService.insertPopulation(population));
    }

    /**
     * 修改population
     */
//    //@PreAuthorize("@ss.hasPermi('population:population:edit')")
    @Log(title = "population", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Population population)
    {
        return toAjax(populationService.updatePopulation(population));
    }

//    //@PreAuthorize("@ss.hasPermi('population:population:edit')")
    @Log(title = "population", businessType = BusinessType.UPDATE)
    @PutMapping("use")
    public AjaxResult edituse(@RequestBody PopulationUse populationuse)
    {
        Population population=new Population();
        population.setPopulationId(populationuse.getPopulation_id());

        Species species =speciesService.selectSpeciesBySpeciesName(populationuse.getSpecies_name());
        population.setPopulationName(populationuse.getPopulation_name());
        population.setSpeciesId(species.getSpeciesId());
        population.setRemark(populationuse.getRemark());
        return toAjax(populationService.updatePopulation(population));
    }

    /**
     * 删除population
     */
//    //@PreAuthorize("@ss.hasPermi('population:population:remove')")
    @Log(title = "population", businessType = BusinessType.DELETE)
	@DeleteMapping("/{populationIds}")
    public AjaxResult remove(@PathVariable Long[] populationIds)
    {
        return toAjax(populationService.deletePopulationByPopulationIds(populationIds));
    }

    //1.4 下拉框 查询 群种所属物种
    @ResponseBody
//    //@PreAuthorize("@ss.hasPermi('system:population:list')")
    @GetMapping("/selectSpeciesName")
    public AjaxResult selectSpeciesName() {
        List<String> list = populationService.selectSpeciesName();
        return AjaxResult.success(list);
    }

    //1.4 大查询 通过 所属物种 和 群体名称 查询
//    //@PreAuthorize("@ss.hasPermi('system:population:list')")
    @PostMapping("/selectPopulation")
    public TableDataInfo selectPopulation( String species_name, String population_name) {
        startPage();
        List<PopulationUse> list = populationService.selectPopulation(species_name, population_name);
        return getDataTable(list);
    }

    //1.4 查询是否存在输入的name
//    //@PreAuthorize("@ss.hasPermi('system:population:list')")
    @PostMapping("/checkPopulationName")
    public AjaxResult checkPopulationName(@RequestBody PopulationUse populationUse) {
        Integer pop = populationService.checkPopulationName(populationUse.getPopulation_name());
        return AjaxResult.success(pop);//1 存在，拦截
    }

    //1.4 下载
//    //@PreAuthorize("@ss.hasPermi('system:population:download')")
    @RequestMapping("/download")
    public void download(HttpServletResponse response,@RequestBody List<Long> list) {
        try {
            List<PopulationUse> alist = new ArrayList<>();
            for (Long id:list) {
                Population population = populationService.selectPopulationByPopulationId(id);
                Species species = speciesService.selectSpeciesBySpeciesId(population.getSpeciesId());
                PopulationUse use = new PopulationUse();

                use.setPopulation_id(population.getPopulationId());
                use.setPopulation_name(population.getPopulationName());;
                use.setSpecies_id(population.getSpeciesId());
                use.setSpecies_name(species.getSpeciesName());
                use.setRemark(population.getRemark());

                alist.add(use);
            }
            ExcelUtil<PopulationUse> util = new ExcelUtil<>(PopulationUse.class);
            util.exportExcel(response, alist, "查询数据 Population");
        } catch (Exception ex) {
            logger.error("添加信息异常", ex);
        }
    }

}

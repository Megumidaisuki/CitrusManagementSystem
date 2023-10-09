package com.feidian.sidebarTree.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.feidian.sidebarTree.domain.Population;
import com.feidian.sidebarTree.domain.PopulationUse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.domain.Species;
import com.feidian.sidebarTree.service.ISpeciesService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

/**
 * speciesController
 * 
 * @author feidian
 * @date 2023-07-01
 */
@RestController
@RequestMapping("/species/species")
public class SpeciesController extends BaseController
{
    @Autowired
    private ISpeciesService speciesService;

    /**
     * 查询species列表
     */
    //@PreAuthorize("@ss.hasPermi('species:species:list')")
    @GetMapping("/list")
    public TableDataInfo list(Species species)
    {
        startPage();
        List<Species> list = speciesService.selectSpeciesList(species);
        return getDataTable(list);
    }

    /**
     * 导出species列表
     */
    //@PreAuthorize("@ss.hasPermi('species:species:export')")
    @Log(title = "species", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Species species)
    {
        List<Species> list = speciesService.selectSpeciesList(species);
        ExcelUtil<Species> util = new ExcelUtil<Species>(Species.class);
        util.exportExcel(response, list, "species数据");
    }

    /**
     * 获取species详细信息
     */
    //@PreAuthorize("@ss.hasPermi('species:species:query')")
    @GetMapping(value = "/{speciesId}")
    public AjaxResult getInfo(@PathVariable("speciesId") Long speciesId)
    {
        return AjaxResult.success(speciesService.selectSpeciesBySpeciesId(speciesId));
    }

    /**
     * 新增species
     */
    //@PreAuthorize("@ss.hasPermi('species:species:add')")
    @Log(title = "species", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Species species)
    {
        return toAjax(speciesService.insertSpecies(species));
    }

    /**
     * 修改species
     */
    //@PreAuthorize("@ss.hasPermi('species:species:edit')")
    @Log(title = "species", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Species species)
    {
        return toAjax(speciesService.updateSpecies(species));
    }

    /**
     * 删除species
     */
    //@PreAuthorize("@ss.hasPermi('species:species:remove')")
    @Log(title = "species", businessType = BusinessType.DELETE)
	@DeleteMapping("/{speciesIds}")
    public AjaxResult remove(@PathVariable Long[] speciesIds)
    {
        return toAjax(speciesService.deleteSpeciesBySpeciesIds(speciesIds));
    }

    //1.3 搜索框 大查询 根据 物种名称 模糊匹配t
    //@PreAuthorize("@ss.hasPermi('system:species:list')")
    @RequestMapping("/selectSpecies")
    public TableDataInfo selectSpecies(Species species) {
        startPage();
        List<Species> list = speciesService.selectSpecies(species.getSpeciesName());
        return getDataTable(list);
    }

    //1.4 查询是否存在输入的name
    //@PreAuthorize("@ss.hasPermi('system:population:list')")
    @PostMapping("/checkSpeciesName")
    public AjaxResult checkSpeciesName(@RequestBody Species species) {
        Integer spe = speciesService.checkSpeciesName(species.getSpeciesName());
        return AjaxResult.success(spe);//1存在，拦截
    }

    //1.3 下载
    //@PreAuthorize("@ss.hasPermi('system:species:download')")
    @RequestMapping("/download")
    public void download(HttpServletResponse response,@RequestBody List<Long> list) {
        try {
            List<Species> alist = new ArrayList<>();
            for (Long id:list) {
                Species species = speciesService.selectSpeciesBySpeciesId(id);
                alist.add(species);
            }
            ExcelUtil<Species> util = new ExcelUtil<>(Species.class);
            util.exportExcel(response, alist, "查询数据 Species");
        } catch (Exception ex) {
            logger.error("添加信息异常", ex);
        }
    }

}

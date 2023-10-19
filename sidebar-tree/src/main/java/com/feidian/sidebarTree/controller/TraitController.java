package com.feidian.sidebarTree.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import com.feidian.sidebarTree.domain.vo.DataAnalysisVO;
import com.feidian.sidebarTree.domain.vo.TraitVO;
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
import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.service.ITraitService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author feidian
 * @date 2023-07-03
 */
@RestController
@RequestMapping("/trait")
public class TraitController extends BaseController
{
    @Autowired
    private ITraitService traitService;

    /**
     * 查询【请填写功能名称】列表
     */
    //@PreAuthorize("@ss.hasPermi('system:trait:list')")
    @GetMapping("/list")
    public TableDataInfo list(Trait trait)
    {
        startPage();
        List<Trait> list = traitService.selectTraitList(trait);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    //@PreAuthorize("@ss.hasPermi('system:trait:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Trait trait)
    {
        List<Trait> list = traitService.selectTraitList(trait);
        ExcelUtil<Trait> util = new ExcelUtil<Trait>(Trait.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:trait:query')")
    @GetMapping(value = "/{traitId}")
    public AjaxResult getInfo(@PathVariable("traitId") Long traitId)
    {
        return AjaxResult.success(traitService.selectTraitByTraitId(traitId));
    }

    /**
     * 新增【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:trait:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Trait trait)
    {
        return toAjax(traitService.insertTrait(trait));
    }

    /**
     * 修改【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:trait:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Trait trait)
    {
        return toAjax(traitService.updateTrait(trait));
    }

    /**
     * 删除【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:trait:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{traitIds}")
    public AjaxResult remove(@PathVariable Long[] traitIds)
    {
        return toAjax(traitService.deleteTraitByTraitIds(traitIds));
    }

    @GetMapping("/listByType")
    public AjaxResult listAllByType(){
          List<TraitVO> result =  traitService.listAllByType();
          return AjaxResult.success(result);
    }


    /*@GetMapping("/listLocationByTraitId")
    public AjaxResult listLocationByTraitId(String name){
        Trait trait =new Trait();
        trait.setTraitName(name);
        List<Trait> traits = traitService.selectTraitList(trait);
        if(traits.size()==0)
            return AjaxResult.error("性状名不存在");
        Set<String> result =  traitService.listLocationByTraitId(traits.get(0).getTraitId());
        return AjaxResult.success(result);
    }*/


    @GetMapping("/dataAnalysis")
    public AjaxResult dataAnalysis(Long id){

        List<DataAnalysisVO> result =  traitService.dataAnalysis(id);
        return AjaxResult.success(result);
    }

    @GetMapping("/getTraitByType")
    public AjaxResult getTraitByType(Long typeId){
        if(typeId==null||typeId==0)
            return AjaxResult.error("不存在");
        List<Trait> list =traitService.getTraitByType(typeId);
        return AjaxResult.success(list);
    }

    @GetMapping("/dataAnalysisByName")
    public AjaxResult dataAnalysisByName(Long id,String traitName){
        Trait query =new Trait();
        query.setTraitName(traitName);
        List<Trait> list = traitService.selectTraitList(query);
        if(list.size()==0) return AjaxResult.error("查询失败，无此性状");
        DataAnalysisVO result =  traitService.dataAnalysisByName(id,traitName);
        return AjaxResult.success(result);
    }

    @GetMapping("/dataAnalysisByMaterilId")
    public AjaxResult dataAnalysisBymaterialId(Long id,String materialId){
        Trait query =new Trait();
        List<DataAnalysisVO> result =  traitService.dataAnalysisBymaterialId(id,materialId);
        return AjaxResult.success(result);
    }


    //1.6 大查询 模糊匹配 根据性状名称模糊匹配
    //@PreAuthorize("@ss.hasPermi('system:trait:list')")
    @PostMapping("/selectTrait")
    public TableDataInfo selectTrait(Trait trait) {
        startPage();
        List<Trait> list = traitService.selectTrait(trait.getTraitName());
        return getDataTable(list);
    }

    //1.6 查询是否存在输入的name
    //@PreAuthorize("@ss.hasPermi('system:population:list')")
    @PostMapping("/checkTraitName")
    public AjaxResult checkTraitName(@RequestBody Trait trait) {
        Integer tra = traitService.checkTraitName(trait.getTraitName());
        return AjaxResult.success(tra);
    }

    //1.6 1.7 下载
    //@PreAuthorize("@ss.hasPermi('system:trait:download')")
    @RequestMapping("/download")
    public void download(HttpServletResponse response, @RequestBody List<Long> list) {
        try {
            List<Trait> alist = new ArrayList<>();
            for (Long id : list) {
                Trait trait = traitService.selectTraitByTraitId(id);
                alist.add(trait);
            }
            ExcelUtil<Trait> util = new ExcelUtil<>(Trait.class);
            util.exportExcel(response, alist, "查询数据 Trait");
        } catch (Exception ex) {
            logger.error("添加信息异常", ex);
        }
    }

}

package com.feidian.sidebarTree.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.feidian.sidebarTree.domain.Trait;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.domain.TraitType;
import com.feidian.sidebarTree.service.ITraitTypeService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author feidian
 * @date 2023-07-03
 */
@RestController
@RequestMapping("Type")
public class TraitTypeController extends BaseController
{
    @Autowired
    private ITraitTypeService traitTypeService;

    /**
     * 查询【请填写功能名称】列表
     */
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraitType traitType)
    {
        startPage();
        List<TraitType> list = traitTypeService.selectTraitTypeList(traitType);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    //@PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraitType traitType)
    {
        List<TraitType> list = traitTypeService.selectTraitTypeList(traitType);
        ExcelUtil<TraitType> util = new ExcelUtil<TraitType>(TraitType.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{traitTypeId}")
    public AjaxResult getInfo(@PathVariable("traitTypeId") Long traitTypeId)
    {
        return AjaxResult.success(traitTypeService.selectTraitTypeByTraitTypeId(traitTypeId));
    }

    /**
     * 新增【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraitType traitType)
    {
        return toAjax(traitTypeService.insertTraitType(traitType));
    }

    /**
     * 修改【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraitType traitType)
    {
        return toAjax(traitTypeService.updateTraitType(traitType));
    }

    /**
     * 删除【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{traitTypeIds}")
    public AjaxResult remove(@PathVariable Long[] traitTypeIds)
    {
        return toAjax(traitTypeService.deleteTraitTypeByTraitTypeIds(traitTypeIds));
    }


    //1.7 下拉框 性状类型
    @ResponseBody
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/selectTraitTypeName")
    public AjaxResult selectTraitTypeName() {
        return AjaxResult.success(traitTypeService.selectTraitTypeName());
    }

    //1.5 搜索框 通过 性状类型名称 模糊匹配 查询
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @PostMapping("/selectTraitType")
    public TableDataInfo selectTraitType(String trait_type_name) {
        startPage();
        List<TraitType> list = traitTypeService.selectTraitType(trait_type_name);
        return getDataTable(list);
    }

    //1.5 查询是否存在输入的name
    //@PreAuthorize("@ss.hasPermi('system:population:list')")
    @PostMapping("/checkTraitTypeName")
    public AjaxResult checkTraitTypeName(@RequestBody TraitType traitType) {
        Integer tt = traitTypeService.checkTraitTypeName(traitType.getTraitTypeName());
        return AjaxResult.success(tt);
    }

    //1.5 下载
    //@PreAuthorize("@ss.hasPermi('system:type:download')")
    @RequestMapping("/download")
    public void download(HttpServletResponse response, @RequestBody List<Long> list) {
        try {
            List<TraitType> alist = new ArrayList<>();
            for (Long id : list) {
                TraitType traitType = traitTypeService.selectTraitTypeByTraitTypeId(id);
                alist.add(traitType);
            }
            ExcelUtil<TraitType> util = new ExcelUtil<>(TraitType.class);
            util.exportExcel(response, alist, "查询数据 Species");
        } catch (Exception ex) {
            logger.error("添加信息异常", ex);
        }
    }

}
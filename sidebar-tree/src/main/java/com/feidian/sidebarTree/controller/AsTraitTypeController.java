package com.feidian.sidebarTree.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.utils.ServletUtils;
import com.feidian.sidebarTree.domain.AsTypeUse;
import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.service.ITraitTypeService;
import com.github.pagehelper.PageInfo;
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
import com.feidian.sidebarTree.domain.AsTraitType;
import com.feidian.sidebarTree.service.IAsTraitTypeService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

import static com.feidian.common.core.page.TableSupport.PAGE_NUM;
import static com.feidian.common.core.page.TableSupport.PAGE_SIZE;

/**
 * 【请填写功能名称】Controller
 *
 * @author feidian
 * @date 2023-07-05
 */
@RestController
@RequestMapping("/sidebarTree/type")
public class AsTraitTypeController extends BaseController
{
    @Autowired
    private IAsTraitTypeService asTraitTypeService;

    @Autowired
    private ITraitTypeService traitTypeService;

    /**
     * 查询【请填写功能名称】列表
     */
    //@PreAuthorize("@ss.hasPermi('sidebarTree:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(AsTraitType asTraitType)
    {
        startPage();
        List<AsTraitType> list = asTraitTypeService.selectAsTraitTypeList(asTraitType);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    //@PreAuthorize("@ss.hasPermi('sidebarTree:type:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AsTraitType asTraitType)
    {
        List<AsTraitType> list = asTraitTypeService.selectAsTraitTypeList(asTraitType);
        ExcelUtil<AsTraitType> util = new ExcelUtil<AsTraitType>(AsTraitType.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    //@PreAuthorize("@ss.hasPermi('sidebarTree:type:query')")
    @GetMapping(value = "/{asTraitTypeId}")
    public AjaxResult getInfo(@PathVariable("asTraitTypeId") Long asTraitTypeId)
    {
        return AjaxResult.success(asTraitTypeService.selectAsTraitTypeByAsTraitTypeId(asTraitTypeId));
    }

    /**
     * 新增【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('sidebarTree:type:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AsTraitType asTraitType)
    {
        return toAjax(asTraitTypeService.insertAsTraitType(asTraitType));
    }

    /**
     * 修改【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('sidebarTree:type:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AsTraitType asTraitType)
    {
        return toAjax(asTraitTypeService.updateAsTraitType(asTraitType));
    }

    /**
     * 删除【请填写功能名称】
     */
    //@PreAuthorize("@ss.hasPermi('sidebarTree:type:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{asTraitTypeIds}")
    public AjaxResult remove(@PathVariable Long[] asTraitTypeIds)
    {
        return toAjax(asTraitTypeService.deleteAsTraitTypeByAsTraitTypeIds(asTraitTypeIds));
    }


    //1.7 多表查询 返回需要高亮的数据 大查询
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @PostMapping("/selectHighlight")
    public TableDataInfo selectHighlight(String type, String name) {
        startPage();
        List<Trait> list = asTraitTypeService.selectHighlight(type, name);


        return getDataTable(list);
    }

    //1.7 查询高亮的条数
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/selectHighlightin")
    public AjaxResult selectHighlightin(String type, String name) {
        startPage();
        List<Trait> a = asTraitTypeService.selectHighlightin(type, name);
        String pageNum = ServletUtils.getParameter(PAGE_NUM);
        String pageSize = ServletUtils.getParameter(PAGE_SIZE);

        System.out.println(a.size());
        return AjaxResult.success(a.size());
    }

    //1.7 更新关联表中的数据 按照先把as_trait_type中存在的数据删除 再向其中添加所有传来的
    //@ResponseBody
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @PostMapping("/CheckUpdate")
    public AjaxResult CheckUpdate(@RequestBody AsTypeUse asTypeUse) {
        List<Long> list = asTypeUse.getList();
        String type = asTypeUse.getType();
        String name = asTypeUse.getName();
        List<Trait> tr_list = asTraitTypeService.selectHighlightin(type, name);

        List<Long> tr_id_list = new ArrayList<>();
        for(Trait trait : tr_list){
            tr_id_list.add(trait.getTraitId());
        }

        for (Long tr_id : tr_id_list) {
            asTraitTypeService.CheckUpdate(tr_id, type);//如果存在就删除
        }

        for(Long id : list){
            Long type_id = traitTypeService.selectIdByTypeName(type);
            AsTraitType as = new AsTraitType();
            as.setTraitId(id.toString());
            as.setTraitTypeId(type_id);
            as.setRemark(asTypeUse.getRemark());
            asTraitTypeService.insertAsTraitType(as);
        }
        return AjaxResult.success(200);
    }

    //1.7 模糊匹配性状名称
    //@PreAuthorize("@ss.hasPermi('system:trait:list')")
    @GetMapping("/selectTraitName")
    public AjaxResult selectTraitName(Trait trait) {
        startPage();
        List<String> a = asTraitTypeService.selectTraitName(trait.getTraitName());
        return AjaxResult.success(a);
    }

    //1.7 多表查询 返回需要高亮的数据 大查询
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @PostMapping("/selectHighlightAll")
    public TableDataInfo selectHighlightAll(String type, String name) {
        startPage();
        List<Trait> list = asTraitTypeService.selectHighlight(type, name);
        return getDataTable(list);
    }

    //1.7 查询高亮的条数
    //@PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/selectHighlightinAll")
    public AjaxResult selectHighlightinAll(String type, String name) {
        startPage();
        List<Trait> a = asTraitTypeService.selectHighlightin(type, name);
        System.out.println(a.size());
        return AjaxResult.success(a.size());
    }


}

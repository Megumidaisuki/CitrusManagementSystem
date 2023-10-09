package com.feidian.sidebarTree.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.core.domain.entity.SysRole;
import com.feidian.sidebarTree.domain.SidebarTree;
import com.feidian.sidebarTree.domain.vo.LogVo;
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
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.sidebarTree.service.ISidebarTreeService;

/**
 * 侧边栏树的Controller
 *
 * @author feidian
 * @date 2022-07-01
 */
@RestController
@RequestMapping("/system/tree")
public class SidebarTreeController extends BaseController
{
    @Autowired
    private ISidebarTreeService sidebarTreeService;

    /**
     * 查询侧边栏树的列表
     */
    @GetMapping("/list")
    public AjaxResult list(SidebarTree sidebarTree)
    {
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        SidebarTree sidebarTree1 = null;
        System.out.println(sidebarTree);
        if(collect.contains(1L) || collect.contains(5L)){
            sidebarTree1 = sidebarTreeService.selectAllTreeNode(true,sidebarTree);
        }
        else{
            sidebarTree1 = sidebarTreeService.selectAllTreeNode(false,sidebarTree);
        }
        return AjaxResult.success(sidebarTree1);
    }

    @GetMapping("/listForUserKind")//展示种苗图片给普通用户
    public AjaxResult listForUserKind()
    {
        SidebarTree sidebarTree =new SidebarTree();
        sidebarTree.setTreeId(1L);
        sidebarTree.setTreeType(2);
        SidebarTree sidebarTree1 = sidebarTreeService.selectAllTreeNode(false,sidebarTree);

        return AjaxResult.success(sidebarTree1);
    }

    /**
     * 导出侧边栏树的列表
     */
    //@PreAuthorize("@ss.hasPermi('system:tree:export')")
    @Log(title = "侧边栏树的", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SidebarTree sidebarTree)
    {
        List<SidebarTree> list = sidebarTreeService.selectSidebarTreeList(sidebarTree);
        ExcelUtil<SidebarTree> util = new ExcelUtil<SidebarTree>(SidebarTree.class);
        util.exportExcel(response, list, "侧边栏树的数据");
    }

    /**
     * 获取侧边栏树的详细信息
     */
//    //@PreAuthorize("@ss.hasPermi('system:tree:query')")
    @GetMapping(value = "/{treeId}")
    public AjaxResult getInfo(@PathVariable("treeId") Long treeId)
    {
        return AjaxResult.success(sidebarTreeService.selectSidebarTreeByTreeId(treeId));
    }

    /**
     * 新增侧边栏树的
     */
//    //@PreAuthorize("@ss.hasPermi('system:tree:add')")
    @Log(title = "侧边栏树的", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SidebarTree sidebarTree)
    {
        return toAjax(sidebarTreeService.insertSidebarTree(sidebarTree));
    }

    /**
     * 修改侧边栏树的
     */
//    //@PreAuthorize("@ss.hasPermi('system:tree:edit')")
    @Log(title = "侧边栏树的", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SidebarTree sidebarTree)
    {
        Long treeId = sidebarTree.getTreeId();

        System.out.println(sidebarTree);
        if(treeId==1||treeId==2 )return AjaxResult.error("根目录不可修改");
        int i = sidebarTreeService.updateSidebarTree(sidebarTree);
        if(i==-1) return AjaxResult.error("修改失败，无修改权限");
        if(i==-2) return AjaxResult.error("修改失败，存在未公开父节点");
        return toAjax(i);
    }

    /**
     * 删除侧边栏树的
     */
//    //@PreAuthorize("@ss.hasPermi('system:tree:remove')")
    @Log(title = "侧边栏树的", businessType = BusinessType.DELETE)
	@DeleteMapping("/{treeIds}")
    public AjaxResult remove(@PathVariable Long[] treeIds)
    {
        int i = sidebarTreeService.deleteSidebarTreeByTreeIds(treeIds);
        if(i==0)
            return error("无修改权限");
        else if(i==-1){
            return error("节点不存在或已删除");
        }
        return success("删除成功");
    }

    /**
     * 操作日志
     */
    @Log(title = "侧边栏树的", businessType = BusinessType.DELETE)
    @GetMapping("getLogNumbers")
    public AjaxResult getLogNumbers(Long treeId)
    {
        HashMap<String,Integer> map= sidebarTreeService.getLogNumbers(treeId);

        List<LogVo> result =new ArrayList<>();
        for(Map.Entry<String,Integer> entery:map.entrySet()){
            result.add(new LogVo(entery.getKey(),entery.getValue()));
        }
        return AjaxResult.success(result);
    }
}


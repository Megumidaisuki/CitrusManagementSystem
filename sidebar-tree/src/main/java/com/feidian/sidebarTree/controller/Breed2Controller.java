package com.feidian.sidebarTree.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.core.domain.entity.SysRole;
import com.feidian.common.core.domain.entity.SysUser;
import com.feidian.common.utils.ServletUtils;
import com.feidian.framework.web.service.TokenService;

import com.feidian.sidebarTree.pythonCode.PythonUse;
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
import com.feidian.sidebarTree.domain.Breed2;
import com.feidian.sidebarTree.service.IBreed2Service;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;

import static com.feidian.common.utils.SecurityUtils.getUsername;

/**
 * 【请填写功能名称】Controller
 *
 * @author feidian
 * @date 2023-09-10
 */
@RestController
@RequestMapping("/system/breed2")
public class Breed2Controller extends BaseController
{
    @Autowired
    private IBreed2Service breed2Service;

    @Autowired
    private PythonUse pythonUse;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed2:list')")
    @GetMapping("/list")
    public TableDataInfo list(Breed2 breed2)
    {
        //数据过滤
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        List<SysRole> roles = user.getRoles();
        for (SysRole role : roles) {
            //超级管理员是1，特权角色是5
            if (role.getRoleId() != 1 && role.getRoleId() != 5) {
                breed2.setCreateBy(getUserId().toString());
            }
        }

        startPage();
        List<Breed2> list = breed2Service.selectBreed2List(breed2);
        return getDataTable(list);
    }

    /**
     * 导出
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed2:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Breed2 breed2)
    {
        //数据过滤
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        List<SysRole> roles = user.getRoles();
        for (SysRole role : roles) {
            //超级管理员是1，特权角色是5
            if (role.getRoleId() != 1 && role.getRoleId() != 5) {
                breed2.setCreateBy(getUserId().toString());
            }
        }

        List<Breed2> list = breed2Service.selectBreed2List(breed2);
        ExcelUtil<Breed2> util = new ExcelUtil<Breed2>(Breed2.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 新增
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed2:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(String param)
    {
        pythonUse.getPDF2(param,getUserId().toString());
        return AjaxResult.success();

    }

    /**
     * 修改
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed2:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Breed2 breed2)
    {
        return toAjax(breed2Service.updateBreed2(breed2));
    }

    /**
     * 删除
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed2:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(breed2Service.deleteBreed2ByIds(ids));
    }



    @PostMapping("/getPdf")
    public AjaxResult getPdf(Long id){
        Breed2 breed2 = breed2Service.selectBreed2ById(id);
        return AjaxResult.success(breed2);
    }
}

package com.feidian.sidebarTree.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.domain.TreeFile;
import com.feidian.sidebarTree.domain.vo.TreeFileVo;
import com.feidian.sidebarTree.service.FillService;
import com.feidian.sidebarTree.service.ITreeFileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 树节点上的文件Controller
 *
 * @author feidian
 * @date 2022-07-04
 */
@RestController
@RequestMapping("/system/file")
public class TreeFileController extends BaseController
{
    @Autowired
    private ITreeFileService treeFileService;
    @Autowired
    private FillService fillService;

    /**
     * 查询树节点上的文件列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TreeFileVo treeFile)
    {
        startPage();

        List<TreeFile> list = treeFileService.selectTreeFileList(treeFile);
        return getDataTable(list);
    }

    @PostMapping("/upload")
    public AjaxResult upload(int treeId, @RequestParam("file") MultipartFile file, int fileStatus,String description,String fileName,String dateTime){
        boolean upload = fillService.uploadFile(treeId, file, fileStatus, description, fileName,dateTime);
        if(upload)return AjaxResult.success();
        else return AjaxResult.error();
    }

    /**
     * 获取树节点上的文件详细信息
     */
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return AjaxResult.success(treeFileService.selectTreeFileByFileId(fileId));
    }

    /**
     * 新增树节点上的文件
     */
    //@PreAuthorize("@ss.hasPermi('system:file:add')")
    @PostMapping
    public AjaxResult add(@RequestBody TreeFile treeFile)
    {

        return toAjax(treeFileService.insertTreeFile(treeFile));
    }

    /**
     * 修改树节点上的文件
     */
    @Log(title = "树节点上的文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TreeFile treeFile)
    {
        return toAjax(treeFileService.updateTreeFile(treeFile));
    }

    /**
     * 删除树节点上的文件
     */
    @Log(title = "树节点上的文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(treeFileService.deleteTreeFileByFileIds(fileIds));
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        fillService.downloadFile(request,response);
    }

    /**
     * 对csv文件进行处理
     */
    @GetMapping("/csvSynthesis")
    public AjaxResult csvSynthesis(int treeId) throws IOException, ParseException {
        String lists = fillService.dealCsv(treeId);
        if(StringUtils.isEmpty(lists))
            return AjaxResult.success("未添加文件");
        else
            return AjaxResult.success(lists);
    }
}

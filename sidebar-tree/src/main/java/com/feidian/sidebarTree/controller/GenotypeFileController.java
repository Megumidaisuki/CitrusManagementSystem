package com.feidian.sidebarTree.controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.core.domain.entity.SysRole;
import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.pythonCode.PythonUse;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.domain.GenotypeFile;
import com.feidian.sidebarTree.service.IGenotypeFileService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 基因型文件Controller
 *
 * @author feidian
 * @date 2023-07-06
 */
@RestController
@RequestMapping("/genotypeFile")
public class GenotypeFileController extends BaseController
{
    @Autowired
    private IGenotypeFileService genotypeFileService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询基因型文件列表
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(GenotypeFile genotypeFile)
    {
        //不是管理员或特权用户就只能看到自己上传的文件 1 admin 5 特权用户
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        if(!collect.contains(1L) && !collect.contains(5L)){
            genotypeFile.setCreateBy(getUserId().toString());
        }

        startPage();
        List<GenotypeFile> list = genotypeFileService.selectGenotypeFileList(genotypeFile);
        TableDataInfo dataTable = getDataTable(list);
        if(StringUtils.isEmpty(genotypeFile.getTableName()))
            dataTable.setTotal(genotypeFileService.selectGenotypeFileListCount(genotypeFile));
        else
            dataTable.setTotal(genotypeFileService.selectGenotypeFileListCountByTableName(genotypeFile.getTableName()));
        return dataTable;
    }

    /**
     * 导出基因型文件列表
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "基因型文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GenotypeFile genotypeFile)
    {
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        if(collect.contains(1L) || collect.contains(5L)){
            genotypeFile.setCreateBy(getUserId().toString());
        }

        List<GenotypeFile> list = genotypeFileService.selectGenotypeFileList(genotypeFile);
        ExcelUtil<GenotypeFile> util = new ExcelUtil<GenotypeFile>(GenotypeFile.class);
        util.exportExcel(response, list, "基因型文件数据");
    }

    /**
     * 获取基因型文件详细信息
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return AjaxResult.success(genotypeFileService.selectGenotypeFileByFileId(fileId));
    }

    /**
     * 上传
     *
     * @param treeId       树id
     * @param file         文件
     * @param status   是否公开
     * @param remark       备注
     * @param fileName     文件名称
     * @return {@link AjaxResult}
     */
    @Log(title = "基因型文件",businessType = BusinessType.INSERT)
    @PostMapping("/upload")//新建一个文件和表
    public AjaxResult upload(Long treeId, @RequestParam("file") MultipartFile file, int status, String remark, String fileName)throws Exception {
        String tableName = genotypeFileService.uploadFile(treeId, file, status, remark, fileName);
        if(!StringUtils.isEmpty(tableName)){
            genotypeFileService.waitUpdate(tableName);
            genotypeFileService.excutePCA(tableName);
            return AjaxResult.success("上传成功");
        }
        else return AjaxResult.error("上传失败");
    }

    @Log(title = "基因型文件",businessType = BusinessType.UPDATE)
    @PostMapping("/merge")//混合数据
    public AjaxResult merge(@RequestParam("file") MultipartFile file, String tableName, String remark, String fileName) throws Exception {
        boolean merge = genotypeFileService.mergeFile(file,tableName,remark,fileName);
        if(merge){
            genotypeFileService.waitUpdate(tableName);
            genotypeFileService.excutePCA(tableName);
            return AjaxResult.success("合并成功");
        }
        else return AjaxResult.error("合并失败");
    }


    /**
     * 修改基因型文件
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "基因型文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GenotypeFile genotypeFile)
    {
        return toAjax(genotypeFileService.updateGenotypeFile(genotypeFile));
    }

    /**
     * 删除基因型文件
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "基因型文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(genotypeFileService.deleteGenotypeFileByFileIds(fileIds));
    }

    /**
     * 修改基因型文件数据
     *
     * @param fileId     文件id
     * @param genotypeId 基因型id
     * @param request    请求
     * @return {@link AjaxResult}
     */
    @Log(title = "基因型文件",businessType = BusinessType.UPDATE)
    @PostMapping("/updateGenoTypeFile")
    public AjaxResult updateGenoTypeFile(Long fileId, Long genotypeId, HttpServletRequest request) throws IOException {
        HashMap<String,String> map = getDataMap(request);
        if(ObjectUtils.isEmpty(map)){
            return AjaxResult.success("更新成功");
        }
        genotypeFileService.updateGenoTypeFile(fileId, genotypeId, map);
        return AjaxResult.success("更新成功");
    }

    @GetMapping("/endUpdate")
    public AjaxResult endUpdate(String tableName) throws IOException {
        genotypeFileService.waitUpdate(tableName);
        return AjaxResult.success("已加入任务队列");
    }

    @GetMapping("/selectDetailByFileId/{fileId}")
    public TableDataInfo selectDetailByFileId(@PathVariable Long fileId){
        List<LinkedHashMap<String, String>> list = genotypeFileService.selectDetailByFileId(fileId,true);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(genotypeFileService.selectGenotypeDataCountByfileId(fileId));
        return dataTable;
    }

    @GetMapping("/selectHistoryDetailByFileId/{fileId}")
    public TableDataInfo selectHistoryDetailByFileId(@PathVariable Long fileId, int pageNum, int pageSize) throws IOException {
        TableDataInfo dataTable = genotypeFileService.selectHistoryDetailByFileId(fileId,pageNum,pageSize);
        return dataTable;
    }

    @GetMapping("/selectTableColumnByFileId/{fileId}")
    public AjaxResult selectTableColumnByFileId(@PathVariable Long fileId){
        List<String> list = genotypeFileService.selectTableColumnByFileId(fileId);
        return AjaxResult.success(list);
    }

    @GetMapping("/exportFile")
    public AjaxResult exportFile(String tableName) throws IOException {
        String url = genotypeFileService.exportFile(tableName);
        if (StringUtils.isEmpty(url)){
            Object o = redisTemplate.opsForValue().get("exportGenoTypeFileExecution:" + tableName);
            if(!ObjectUtils.isEmpty(o)){
                return AjaxResult.error("文件生成中，请稍后再试");
            }
            genotypeFileService.waitUpdate(tableName);
            return AjaxResult.error("文件生成中，请稍后再试");
        }
        return AjaxResult.success("文件生成成功",url);
    }

    //todo 查文件对应的R生成的图，返回地址
    @GetMapping("/getXXXX/{fileId}")
    public AjaxResult run(@PathVariable Long fileId){
        return AjaxResult.success("获取成功",genotypeFileService.getImgUrl(fileId));
    }

    private HashMap<String, String> getDataMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(parameterMap.isEmpty() || StringUtils.isEmpty(parameterMap)){
            return null;
        }
        HashMap<String,String> res =new HashMap<>();
        for(Map.Entry<String,String[]> map:parameterMap.entrySet()){
            String key = map.getKey();
            String[] value = map.getValue();
            if(!key.equals("fileId") && !key.equals("genotypeId"))
                res.put(key,value[0]);
        }

        return res;
    }

    @GetMapping("/getChromDensity")
    public AjaxResult getChromDensity(String tableName){
        return AjaxResult.success("获取成功",genotypeFileService.getChromDensity(tableName));
    }

    /**
     * @param tableName
     * @return {@link AjaxResult}
     * 按表名获取不包含基本属性的材料列名
     */
    @GetMapping("/getMaterialList")
    public AjaxResult getMaterialList(String tableName){
        return AjaxResult.success("获取成功",genotypeFileService.getMaterialListByTableName(tableName));
    }


    /**
     * @param tableName
     * @return {@link AjaxResult}
     * 按表名获取chrom的行数占的百分比 + 最小最大最大POS
     */
    @GetMapping("/getChromRatioAndMaxPos")
    public AjaxResult getChromRatioAndMaxPos(String tableName){
        return AjaxResult.success("获取成功",genotypeFileService.getChromRatioAndMaxPos(tableName));
    }

    //根据表名，材料名，染色体编号，染色体位置间隔，获取基因型信息
    @GetMapping("/getMaterialInfo")
    public AjaxResult getMaterialInfo(String tableName,String materialName,String chrom,long start,long end){
        return AjaxResult.success("获取成功",genotypeFileService.getMaterialInfo(tableName,materialName,chrom,start,end));
    }

    @GetMapping("/getHeatMap")
    public AjaxResult getHeatMap(String tableName,String[] materialName,String chrom,long start,long end){
        return AjaxResult.success("获取成功",genotypeFileService.getHeatMap(tableName,materialName,chrom,start,end));
    }



}
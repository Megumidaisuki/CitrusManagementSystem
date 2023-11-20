package com.feidian.sidebarTree.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.core.domain.entity.SysRole;
import com.feidian.common.core.domain.model.LoginUser;
import com.feidian.common.core.page.PageDomain;
import com.feidian.common.core.page.TableSupport;
import com.feidian.common.exception.ServiceException;
import com.feidian.common.utils.StringUtils;
import com.feidian.common.utils.sql.SqlUtil;
import com.feidian.sidebarTree.domain.Material;
import com.feidian.sidebarTree.domain.PCA;
import com.feidian.sidebarTree.pythonCode.PythonUse;
import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.domain.vo.AreaVO;
import com.feidian.sidebarTree.domain.vo.PhenotypeDetailVO;
import com.feidian.sidebarTree.domain.vo.PhenotypeFileVO;
import com.feidian.sidebarTree.service.IGenotypeFileService;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.domain.PhenotypeFile;
import com.feidian.sidebarTree.service.IPhenotypeFileService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 表型文件Controller
 *
 * @author feidian
 * @date 2023-07-02
 */
@RestController
@RequestMapping("/phenotypeFile")
public class PhenotypeFileController extends BaseController
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IPhenotypeFileService phenotypeFileService;

    @Autowired
    private IGenotypeFileService genotypeFileService;

    /**
     * 查询表型文件列表
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(PhenotypeFile phenotypeFile,HttpServletRequest request)
    {
        //不是管理员或特权用户就只能看到自己上传的文件 1 admin 5 特权用户
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        if(!collect.contains(1L) && !collect.contains(5L)){
            phenotypeFile.setCreateBy(getUserId().toString());
        }

        String pageSize1 = request.getParameter("pageSize");
        System.out.println(pageSize1);
//        startPage();?
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pS = pageDomain.getPageSize();
        System.out.println(pS);
        Integer pageSize = 100010;
        if(pageSize1!=null)
            pageSize =Integer.valueOf(pageSize1);
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);


        List<PhenotypeFileVO> list = phenotypeFileService.selectPhenotypeFileVOList(phenotypeFile);
        TableDataInfo dataTable = getDataTable(list);
        //有tablename是查一个文件的历史记录
        if (StringUtils.isEmpty(phenotypeFile.getTableName()))
            dataTable.setTotal(phenotypeFileService.selectPhenotypeFileListCount(phenotypeFile));
        else
            dataTable.setTotal(phenotypeFileService.selectPhenotypeFileListCountByTableName(phenotypeFile.getTableName()));
        return dataTable;
    }

    /**
     * 导出表型文件列表
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "表型文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PhenotypeFile phenotypeFile)
    {
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        if(!collect.contains(1L) && !collect.contains(5L)){
            phenotypeFile.setCreateBy(getUserId().toString());
        }
        List<PhenotypeFileVO> list = phenotypeFileService.selectPhenotypeFileVOList(phenotypeFile);
        ExcelUtil<PhenotypeFileVO> util = new ExcelUtil<>(PhenotypeFileVO.class);
        util.exportExcel(response, list, "表型文件数据");
    }

    /**
     * 获取表型文件详细信息
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return AjaxResult.success(phenotypeFileService.selectPhenotypeFileByFileId(fileId));
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
    @PostMapping("/upload")//新建一个文件和表
    //该接口多传一个参数pointStatus，若为1则代表普通上传，若为0则代表大文件上传
    public AjaxResult upload(Long treeId, @RequestParam("file") MultipartFile file, int status, String remark, String fileName,int pointStatus,@RequestParam(required = false) String filePath)throws Exception {
        String tableName;
        if(filePath != null && !filePath.isEmpty()){
            tableName = phenotypeFileService.uploadFile(treeId, file, status, remark, fileName,pointStatus,filePath);
        }else {
            tableName = phenotypeFileService.uploadFile(treeId, file, status, remark, fileName,pointStatus);
        }

        if(!StringUtils.isEmpty(tableName)){
            phenotypeFileService.waitUpdate(tableName);
            return AjaxResult.success("上传成功");
        }
        else return AjaxResult.error("上传失败");
    }

    @PostMapping("/merge")//混合数据
    public AjaxResult merge(@RequestParam("file") MultipartFile file, String tableName, String remark, String fileName) throws IOException {
        boolean merge = phenotypeFileService.mergeFile(file,tableName,remark,fileName);
        if(merge) {
            phenotypeFileService.waitUpdate(tableName);
            return AjaxResult.success("合并成功");
        }
        else return AjaxResult.error("合并失败");
    }

    /**
     * 修改表型文件详细信息（只能修改status,remark;其他数据全都为生成，不可修改）
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "表型文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PhenotypeFile phenotypeFile)
    {
        return toAjax(phenotypeFileService.updatePhenotypeFile(phenotypeFile));
    }

    /**
     * 删除表型文件
     */
//    //@PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "表型文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(phenotypeFileService.deletePhenotypeFileByFileIds(fileIds));
    }


    /*获取性状数据*/
    @Log(title = "表型文件", businessType = BusinessType.GRANT)
    @GetMapping("selectTraitByFileId/{fileId}")
    public AjaxResult selectTraitByFileId(@PathVariable Long fileId,int pageSize,int pageNum)
    {
        AjaxResult result = new AjaxResult();
        List<List<Map.Entry<String, Integer>>>  list = phenotypeFileService.selectTraitByFileId(fileId,pageSize,pageNum);
        int total = phenotypeFileService.selectTableCount(fileId);
        result.put("list",list);
        result.put("total",total);
        return  result;
    }

    /**
     * 在表型表里查材料基本信息
     */
    @PostMapping(value = "/selectMaterial")
    public TableDataInfo selectMaterial(@RequestBody Material m)
    {
        System.out.println(m);
        m.setTableName(phenotypeFileService.selectTableNameByFileId(m.getFileId()));
        List<Material> materials = new LinkedList<>();
        if(phenotypeFileService.ifHaveTable(m.getTableName()) != 0) {
            startPage();
            materials = phenotypeFileService.selectMaterialByTableName(m);
        }
        return getDataTable(materials);
    }

    /**
     * 获取PCA分析的数据
     */
    @GetMapping("/getPcaData")
    public AjaxResult getPcaData(String fileId) {
        //String fileName = genotypeFileService.selectFileNameByFileId(fileId);
        List<PCA> list = new LinkedList<>();
        try {
            FileReader fr = new FileReader("C:\\SeedlinManagement\\pythonCode\\"+fileId+".csv");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();//排除第一行
            while ( (line = br.readLine()) != null) {
                System.out.println(line);
                String[] inline = line.split(",");
                PCA pca = new PCA(inline[0],inline[1],inline[2],inline[3]);
                list.add(pca);
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.success(list);
    }

    /*获取性状数据*/
    @Log(title = "表型文件", businessType = BusinessType.GRANT)
    @GetMapping("selectDetailByFileId/{fileId}")
    public TableDataInfo selectDetailByFileId(@PathVariable Long fileId)
    {
        List<PhenotypeDetailVO>  list = phenotypeFileService.selectDetailByFileId(fileId,true);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(phenotypeFileService.selectTableLineCountByFileId(fileId));
        return dataTable;
    }




    /*获取性状详细数据*/
    @Log(title = "表型文件", businessType = BusinessType.GRANT)
    @GetMapping("selectTraitColByFileId/{fileId}")
    public AjaxResult selectTraitColByFileId(@PathVariable Long fileId)
    {
        List<Trait> list = phenotypeFileService.selectTraitColByFileId(fileId);
        return  AjaxResult.success(list);
    }

    /*获取所有地区数据*//*
    @Log(title = "表型文件", businessType = BusinessType.GRANT)
    @GetMapping("/getAreaData")
    public AjaxResult getAreaData()
    {
        List areas = phenotypeFileService.getAreaData();
        return  AjaxResult.success(areas);
    }*/


    /*根据性状名上获取地区*//*
    @Log(title = "表型文件", businessType = BusinessType.GRANT)
    @GetMapping("/selectTraitByLocation")
    public AjaxResult selectTraitByLocation(String location)
    {
        PhenotypeFile phenotypeFile =new PhenotypeFile();
        phenotypeFile.setLocation(location);
        List<Trait> result = phenotypeFileService.selectTraitByLocation(location);
        return  AjaxResult.success(result);
    }*/


    @GetMapping("/getMaterialIdByFileId")
    public AjaxResult getMaterialIdByFileId(Long fileId) {
        Set<String> list = phenotypeFileService.getMaterialIdByFileId(fileId);
        return AjaxResult.success(list);
    }

    @PostMapping("/updatePhenoTypeFile")
    public AjaxResult updatePhenoTypeFile(Long fileId, Long phenotypeId, HttpServletRequest request) {
        AjaxResult result = AjaxResult.success("更新成功");
        HashMap<String,String> map = getDataMap(request);
        phenotypeFileService.updatePhenoTypeFile(fileId,phenotypeId,map);
        return result;
    }

    @GetMapping("/endUpdate")
    public AjaxResult endUpdate(String tableName) throws IOException {
        phenotypeFileService.waitUpdate(tableName);
        return AjaxResult.success("已加入任务队列");
    }

    @GetMapping("/exportFile")
    public AjaxResult exportFile(String tableName) throws IOException {
        String url = phenotypeFileService.exportFile(tableName);
        if (StringUtils.isEmpty(url)){
            Object o = redisTemplate.opsForValue().get("exportPhenoTypeFileExecution:" + tableName);
            if(!ObjectUtils.isEmpty(o)){
                return AjaxResult.error("文件生成中，请稍后再试");
            }
            phenotypeFileService.waitUpdate(tableName);
            return AjaxResult.error("文件生成中，请稍后再试");
        }
        return AjaxResult.success("文件生成成功",url);
    }

    private HashMap<String, String> getDataMap(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        if(parameterMap.size()==0 || StringUtils.isEmpty(parameterMap)){
            return null;
        }

        HashMap<String,String> res =new HashMap<>();

        for(Map.Entry<String,String[]> map:parameterMap.entrySet()){
            String key = map.getKey();
            String[] value = map.getValue();
            if (/*key.contains("repeat") || key.contains("kind_id") ||
                    key.contains("kind_name") || key.contains("material_id") ||
                    key.contains("field_id") || key.contains("control_type") ||
                    key.contains("father") || key.contains("mother") ||*/
                    key.contains("remark") || key.contains("trait_id")) {
                if(key.contains("trait_id")) {
                    String[] s = key.split("_");
                    key = "trait_value_"+s[2];
                }
                res.put(key,value[0]);
            }
        }
        return res;
    }

    @GetMapping("/getAllTraitFromFile")
    public AjaxResult getAllTraitFromFile(){

        List<Trait> traits =  phenotypeFileService.getAllTraitFromFile();
        return AjaxResult.success(traits);
    }



//    /**
//     * 下载文件
//     */
//    @PostMapping("/dowload/{fileId}")//新建一个文件和表
//    public AjaxResult dowload(@PathVariable Long fileId)throws ServiceException {
//        boolean upload = phenotypeFileService.dowload(treeId, file, fileStatus, remark, fileName);
//        if(upload)return AjaxResult.success();
//        else return AjaxResult.error();
//    }
}

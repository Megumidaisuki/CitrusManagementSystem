package com.feidian.sidebarTree.controller;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feidian.common.annotation.RepeatSubmit;
import com.feidian.common.utils.StringUtils;
import com.feidian.common.utils.file.FileUtils;
import com.feidian.sidebarTree.domain.GenotypeFile;
import com.feidian.sidebarTree.domain.PhenotypeFile;
import com.feidian.sidebarTree.mapper.Breed2Mapper;
import com.feidian.sidebarTree.mapper.BreedMapper;
import com.feidian.sidebarTree.mapper.PhenotypeFileMapper;
import com.feidian.sidebarTree.pythonCode.BreedPythonUser;
import com.feidian.sidebarTree.pythonCode.UseParam;
import com.feidian.sidebarTree.service.IGenotypeFileService;
import com.feidian.sidebarTree.service.IPhenotypeFileService;
import com.feidian.sidebarTree.service.impl.PhenotypeFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feidian.common.annotation.Log;
import com.feidian.common.core.controller.BaseController;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.enums.BusinessType;
import com.feidian.sidebarTree.domain.Breed;
import com.feidian.sidebarTree.service.IBreedService;
import com.feidian.common.utils.poi.ExcelUtil;
import com.feidian.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * breedController
 *
 * @author feidian
 * @date 2023-07-03
 */
@RestController
@RequestMapping("/system/breed")
public class BreedController extends BaseController
{
    @Autowired
    private IBreedService breedService;

    @Autowired
    private IPhenotypeFileService phenotypeFileService;

    @Autowired
    private IGenotypeFileService genotypeFileService;

    private static final Logger log = LoggerFactory.getLogger(BreedController.class);
    /**
     * 查询breed列表
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed:list')")
    @GetMapping("/list")
    public TableDataInfo list(Breed breed)
    {
        startPage();
        List<Breed> list = breedService.selectBreedList(breed);
        //对list进行一个判断

        return getDataTable(list);
    }

    /**
     * 导出breed列表
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed:export')")
    @Log(title = "breed", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Breed breed)
    {
        List<Breed> list = breedService.selectBreedList(breed);
        ExcelUtil<Breed> util = new ExcelUtil<Breed>(Breed.class);
        util.exportExcel(response, list, "breed数据");
    }

    /**
     * 获取breed详细信息
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed:query')")
    @GetMapping(value = "/{breedId}")
    public AjaxResult getInfo(@PathVariable("breedId") Long breedId)
    {
        return AjaxResult.success(breedService.selectBreedByBreedId(breedId));
    }

    /**
     * 新增breed
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed:add')")
    @Log(title = "breed", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Breed breed)
    {
        return AjaxResult.success(breedService.insertBreed(breed));
    }

    /**
     * 修改breed
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed:edit')")
    @Log(title = "breed", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Breed breed)
    {
        return toAjax(breedService.updateBreed(breed));
    }

    /**
     * 删除breed
     */
//    //@PreAuthorize("@ss.hasPermi('system:breed:remove')")
    @Log(title = "breed", businessType = BusinessType.DELETE)
	@DeleteMapping("/{breedIds}")
    public AjaxResult remove(@PathVariable Long[] breedIds)
    {
        return toAjax(breedService.deleteBreedByBreedIds(breedIds));
    }


    /**
     * 上传文件
     * @param file
     * @param type
     * @return
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") Integer type) {
        return AjaxResult.success(breedService.uploadFile(file,type));
    }

    @PostMapping("/task")
    public AjaxResult taskManager(@RequestParam Long genofileId,@RequestParam Long phenofileId,@RequestParam Integer scene,@RequestParam Integer genofileFlag,@RequestParam Integer phenofileFlag){
        return AjaxResult.success(breedService.taskManager(genofileId,phenofileId,scene,genofileFlag,phenofileFlag));
    }


    @GetMapping("/downloadPdf")
    public AjaxResult downloadPdf(Long breedId){
        String url =  breedService.downloadPdf(breedId);
        if(url==null) return error("模型还在运行");
        return success(url);
    }

    /**
     * 本地资源通用下载
     */
    @RepeatSubmit(interval = 10000,message = "请勿频繁操作，稍后再试")
    @GetMapping("/download/resource")
    public void resourceDownload(@RequestParam Long fileId,@RequestParam Integer type,@RequestParam Integer flag, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
//            if (!FileUtils.checkAllowDownload(resource))
//            {
//                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
//            }
//            // 本地资源路径
//            String localPath = ZhongMiaoConfig.getProfile();
//            // 数据库资源地址
//            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
//            // 下载名称
//            String downloadName = StringUtils.substringAfterLast(downloadPath, File.separator);
//
//            System.out.println("===========");
//            System.out.println("1"+localPath+"2"+downloadName+"3"+downloadPath);



            String resource = breedService.resourceDownload(fileId,type,flag);

            String substring = resource.substring(resource.lastIndexOf("\\"));
            log.debug("下载文件:{}", resource);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, substring);
            FileUtils.writeBytes(resource, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    @Autowired
    private BreedPythonUser pythonUser;


    @PostMapping("/callPythonByNewFile")
    public AjaxResult callPythonByNewFile(String phenoDataType,
                                 String genoDataType,
                                 MultipartFile phenoFile,
                                 MultipartFile genoFile ,
                                 String[] models){
        AjaxResult result =new AjaxResult();
        //对上传文件判断，如果为空的话则用id,不为空则用martipartfile
        File pf =null;
        File gf =null;
        //将文件写入uploadData里
        if(!phenoFile.isEmpty()&&!genoFile.isEmpty()){
            //下载上传文件
            long currentTimeInNanos = System.nanoTime();
            long currentTimeInMicros = currentTimeInNanos / 1000;
            String parentPath =  UseParam.PHENOPATH+"/"+String.valueOf(currentTimeInMicros)+"/";
            new File(parentPath).mkdir();
            String pfPath =parentPath+phenoFile.getOriginalFilename();
            String genoPath = parentPath+genoFile.getOriginalFilename();
            pf = new File(pfPath);
            gf = new File(genoPath);
            System.out.println(pf);
            System.out.println(gf);
            //将临时文件写入
            try {
                phenoFile.transferTo(pf);
                genoFile.transferTo(gf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return error("未选择文件");
        }

        Breed breed =new Breed();
        Integer count = breedService.getCount();
        //运行深度学习文件
        Long id = null;
        if(StringUtils.isNull(count)){
             id = 1L;
        }
        else
        {
             id  =  Long.valueOf(count+1);
        }
        breed.setBreedId(id);
        breed.setCreateBy(getUsername());
        breed.setStatus(0);
        breed.setSubmitTime(new Date());
        breed.setPhenofile(pf.getAbsolutePath());
        breed.setGenofile(gf.getAbsolutePath());
        String mo = "";
        for(String model:models) {
            mo = mo+"*"+model;

        }
        breed.setModels(mo);
        breedService.insertBreed(breed);

        //调用模型方法开始训练
        for(String model:models) {
            try {
                pythonUse( id,model,gf,pf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success("模型运行中");
    }



    @PostMapping("/callPythonByFileId")
    public AjaxResult callPythonByFileId( String phenoDataType,
                                          String genoDataType,
                                          Long phenoFileId,
                                          Long genoFileId ,
                                          String[] models) throws Exception {

        AjaxResult result =new AjaxResult();
        //对上传文件判断，如果为空的话则用id,不为空则用martipartfile
        File pf =null;
        File gf =null;

        //根据文件id获取该文件信息，地址
        PhenotypeFile phenotypeFile = phenotypeFileService.selectPhenotypeFileByFileId(phenoFileId);
        GenotypeFile genotypeFile = genotypeFileService.selectGenotypeFileByFileId(genoFileId);
        System.out.println(phenotypeFile);
        System.out.println(genotypeFile);
        String url = genotypeFile.getUrl();
        String url2 = phenotypeFile.getUrl();

        String csv = convertVCFtoCSV(url);
        String csv2 = convertVCFtoCSV2(url2);
        gf = new File(csv);//convert vcf to csv
        pf = new File(csv2);
        if(!gf.exists())  BreedPythonUser.transferVCF(url,csv);
        if(!pf.exists()) BreedPythonUser.transferCSV(url2,csv2);

        Thread.sleep(1000);//wait for converting

        Breed breed =new Breed();

        System.out.println("=============callpython============");
        //运行深度学习文件
        Long id  = null;
        Integer count = breedService.getCount();

        if(StringUtils.isNull(count)){
            id = 1L;
        }
        else
        {
            id  =  Long.valueOf(count+1);
        }
        breed.setBreedId(id);
        breed.setCreateBy(getUsername());
        breed.setStatus(0);
        breed.setSubmitTime(new Date());
        System.out.println("filename:");
        System.out.println(pf.getAbsolutePath());
        System.out.println(gf.getAbsolutePath());

        breed.setPhenofile(pf.getAbsolutePath());
        breed.setGenofile(gf.getAbsolutePath());
        String mo = "";
        for(String model:models) {
            mo = mo+"*"+model;
        }
        breed.setModels(mo);
        breedService.insertBreed(breed);

        for(String model:models) {
            try {
                pythonUse(id,model,gf,pf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success("模型运行中");//调用PYTHON结束，先返回给前端任务列表。
    }

    private String convertVCFtoCSV2(String url) {
        // 检查字符串是否包含 ".csv"
        if (url.contains(".csv")) {
            // 在 ".csv" 前面添加 "new"
            String modifiedString = url.replace(".csv", "new.csv");
            System.out.println("修改后的字符串: " + modifiedString);
            return modifiedString;
        } else {
            System.out.println("输入字符串不包含 \".csv\"。");
            return "";
        }
    }


    private void pythonUse(Long id,String model,File gf,File pf) throws Exception {

        switch (model) {
            case "BL":
                pythonUser.BL(id,model,gf,pf);
                break;
            case "BRR":
                pythonUser.BL(id,model,gf,pf);
                break;
            case "BayesA":
                pythonUser.BL(id,model,gf,pf);
                break;
            case "BayesB":
                pythonUser.BL(id,model,gf,pf);
                break;
            case "G3":
                pythonUser.G3(id);
                break;
            case "train_4":
                pythonUser.trains_4(id,gf,pf);
            case "test":
                pythonUser.python1(id);
            case "rrBLUP":
                pythonUser.rrBLUP(id,gf,pf);
                break;
            case "DeepGS":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "ResGS":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "LCNN":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "MLP":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "gMLP":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "SVR":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "RF":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            case "LightGBM":
                pythonUser.sdxx(id,model,gf,pf);
                break;
            default:
                pythonUser.python1(id);
//                throw new Exception("该模型不存在");

        }
    }

    // 这是一个示例方法，用于将 VCF 内容转换为 CSV 内容
    private static String convertVCFtoCSV(String str) {
        // 找到最后一个 ".vcf" 的位置
        int lastIndex = str.lastIndexOf(".vcf");
        String outputFileName;
        // 如果找到了 ".vcf"，则进行替换
        if (lastIndex != -1) {
            // 使用 substring 分割字符串，替换最后一个 ".vcf" 为 ".csv"
            outputFileName = str.substring(0, lastIndex) + "new.csv";
        } else {
            System.out.println("输入文件不是 .vcf 文件。");
            return "";
        }
        return outputFileName;
    }

    @GetMapping("/getResultData")
    public AjaxResult getResultData(Long id){
        ArrayList<String> resultData = breedService.getResultData(id);

        return  AjaxResult.success(resultData);

    }

//    @Autowired
//    private Breed2Mapper breedMapper;
//    @GetMapping("demo")
//    public void demo(
//
//    ){
//        Integer integer = breedMapper.selectMaxId();
//
//        System.out.println();
//    }

}

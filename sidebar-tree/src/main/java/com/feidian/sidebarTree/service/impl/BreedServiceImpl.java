package com.feidian.sidebarTree.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.domain.GenotypeFile;
import com.feidian.sidebarTree.domain.Phenotype;
import com.feidian.sidebarTree.domain.PhenotypeFile;
import com.feidian.sidebarTree.mapper.GenotypeFileMapper;
import com.feidian.sidebarTree.mapper.PhenotypeFileMapper;
import com.feidian.sidebarTree.pythonCode.BreedPythonUser;
import com.feidian.sidebarTree.pythonCode.PythonUse;
import com.feidian.sidebarTree.pythonCode.UseParam;
import com.feidian.sidebarTree.service.IGenotypeFileService;
import com.feidian.sidebarTree.service.IPhenotypeFileService;
import com.feidian.sidebarTree.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.mapper.BreedMapper;
import com.feidian.sidebarTree.domain.Breed;
import com.feidian.sidebarTree.service.IBreedService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * breedService业务层处理
 *
 * @author feidian
 * @date 2023-07-03
 */
@Service
public class BreedServiceImpl implements IBreedService {
    @Autowired
    private BreedMapper breedMapper;


    @Autowired
    private PhenotypeFileMapper phenotypeFileMapper;

    @Autowired
    private GenotypeFileMapper genotypeFileMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IPhenotypeFileService phenotypeFileService;
    @Autowired
    private IGenotypeFileService genotypeFileService;

    @Value("${feidian.taskResPath}")
    private String taskResPath;

    @Value("${feidian.taskUploadPath}")
    private String taskUploadPath;


    @Autowired
    private BreedPythonUser pythonUser;
    /**
     * 查询breed
     *
     * @param breedId breed主键
     * @return breed
     */
    @Override
    public Breed selectBreedByBreedId(Long breedId) {
        return breedMapper.selectBreedByBreedId(breedId);
    }

    /**
     * 查询breed列表
     *
     * @param breed breed
     * @return breed
     */
    @Override
    public List<Breed> selectBreedList(Breed breed) {
        return breedMapper.selectBreedList(breed);
    }

    /**
     * 新增breed
     *
     * @param breed breed
     * @return 结果
     */
    @Override
    public int insertBreed(Breed breed) {
        breed.setCreateTime(DateUtils.getNowDate());
        return breedMapper.insertBreed(breed);
    }

    /**
     * 修改breed
     *
     * @param breed breed
     * @return 结果
     */
    @Override
    public int updateBreed(Breed breed) {
        breed.setUpdateTime(DateUtils.getNowDate());
        return breedMapper.updateBreed(breed);
    }

    /**
     * 批量删除breed
     *
     * @param breedIds 需要删除的breed主键
     * @return 结果
     */
    @Override
    public int deleteBreedByBreedIds(Long[] breedIds) {
        return breedMapper.deleteBreedByBreedIds(breedIds);
    }

    /**
     * 删除breed信息
     *
     * @param breedId breed主键
     * @return 结果
     */
    @Override
    public int deleteBreedByBreedId(Long breedId) {
        return breedMapper.deleteBreedByBreedId(breedId);
    }


    @Override
    public String resourceDownload(Long fileId, Integer type, Integer flag) throws Exception {

        //type = 0 为表型 type = 1为基因型
        String resPath = null;


        if (type == 0) {
            resPath = breedMapper.selectBreedPhenotypeFile(fileId);
        } else {
            resPath = breedMapper.selectBreedGenotypeFile(fileId);
        }

//        if (flag == 0) {
//            if (type == 0) {
//                resPath = breedMapper.selectBreedPhenotypeFile(fileId);
//            } else {
//                resPath = breedMapper.selectBreedGenotypeFile(fileId);
//            }
//        } else {
//            // TODO  最新数据的导出
//
//            //文件会在后台存在生成
//            if (type == 0) {
//                //
//                //调用表型文件的导出
//                String tableName = breedMapper.selectBreedPhenotypeNewFile(fileId);
//                resPath = phenotypeFileService.exportFile(tableName);
//                if (StringUtils.isEmpty(resPath)) {
//                    Object o = redisTemplate.opsForValue().get("exportPhenoTypeFileExecution:" + tableName);
//                    if (!ObjectUtils.isEmpty(o)) {
//                        throw new Exception("文件生成中，请稍后再试");
//                    }
//                    phenotypeFileService.waitUpdate(tableName);
//                    throw new Exception("文件生成中，请稍后再试");
//                }
//
//            } else {
//                //获得基因型文件得url
//                String tableName = breedMapper.selectBreedGenotypeNewFile(fileId);
//                resPath = genotypeFileService.exportFile(tableName);
//                if (StringUtils.isEmpty(resPath)) {
//                    Object o = redisTemplate.opsForValue().get("exportGenoTypeFileExecution:" + tableName);
//                    if (!ObjectUtils.isEmpty(o)) {
//                        throw new Exception("文件生成中，请稍后再试");
//                    }
//                    phenotypeFileService.waitUpdate(tableName);
//                    throw new Exception("文件生成中，请稍后再试");
//                }
//
//            }
//        }
        return resPath;
    }

    @Override
    public int getCount() {
        return breedMapper.getCount();

    }

    @Override
    public String downloadPdf(Long breedId) {
        Breed breed = breedMapper.selectBreedByBreedId(breedId);
        String breedUrl = breed.getBreedUrl();
        if(StringUtils.isEmpty(breedUrl)){
            String result = breed.getResult();
            String[] strings1 = BreedPythonUser.convertStringArry(result);//获取所有的参数结果
            String models = breed.getModels();
            String[] strings2 = BreedPythonUser.convertStringArry(models);

            if(strings1.length != strings2.length) return null;

            String savepath = UseParam.pdfparentpath;
            File savefile  = new File(savepath+"/"+breedId+".pdf");
            String savefilepath  = pythonUser.getPDF(strings1,models,savefile,breed.getGenofile());//此时说明模型已经完成，只需要把对应的值传过去即可，在此处生成


//            String s = PythonUse.generatePDF(breed);

            breed.setBreedUrl(savefilepath);
            breedMapper.updateBreed(breed);
            System.out.println(savefilepath);
            return savefilepath;
        }else{
            return breedUrl;
        }
    }

    @Override
    public ArrayList<String> getResultData(Long id) {
        Breed breed = breedMapper.selectBreedByBreedId(id);
        String result = breed.getResult();
        String[] strings = BreedPythonUser.convertStringArry(result);
        ArrayList<String> sj =new ArrayList<>();
        for (int i=0;i< strings.length;i++) {
            String s[] = BreedPythonUser.convertResultArray(strings[i]);
            sj.add("本次基因组预测所用模型为"+s[0]+"模型,模型的预测结果为"+s[1]);
        }
        return sj;
    }

    @Override
    public Long uploadFile(MultipartFile file, Integer type) {

        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

        String newFilename = FileUtil.getFilenameByTimestamp() + "." + fileType;//重命名之后的
        String uploadFilePath = taskUploadPath + newFilename;


        boolean ifSave = FileUtil.save(file, uploadFilePath);        //如果不成功就发生错误,error处理'


        //插入改变表型文件表和基因文件表
        Long resFileId;

        //根据type来判断保存数据库的位置
        if (type == 0) {//插入表型
//            breedMapper.inserPhenotypeFile(uploadFilePath);
            PhenotypeFile phenotypeFile = new PhenotypeFile();
            phenotypeFile.setUrl(uploadFilePath);
            phenotypeFileMapper.insertPhenotypeFile(phenotypeFile);
            resFileId = phenotypeFile.getFileId();
        } else {//插入基因
//            breedMapper.inserGenotypeFile(uploadFilePath);
            GenotypeFile genotypeFile = new GenotypeFile();
            genotypeFile.setUrl(uploadFilePath);

            genotypeFileMapper.insertGenotypeFile(genotypeFile);
            resFileId = genotypeFile.getFileId();
        }

        return resFileId;
    }

    @Override
    public Breed taskManager(Long genofileId, Long phenofileId, Integer scene, Integer genofileFlag, Integer phenofileFlag) {

        //TODO 后续完善结果文件名 和 结束时间等
        String Path;
        Path = taskResPath + FileUtil.getFilenameByTimestamp() + ".csv";

        // 创建文件对象
        File file = new File(Path);
        try {
            // 创建新文件
            boolean created = file.createNewFile();
            if (created) {
                System.out.println("文件已创建: " + Path);
            } else {
                System.out.println("文件创建失败: " + Path);
            }
        } catch (IOException e) {
            System.out.println("文件创建失败: " + e.getMessage());
        }
//        if (phenofileFlag == 1) {
//            String phenofileUrl = null;
//            //拿当前最新的生成一个新的基因型表存储
//            String tableName = breedMapper.selectBreedPhenotypeNewFile(phenofileId);
//            phenofileUrl = phenotypeFileService.exportFile(tableName);
//            if (phenofileUrl == null) {
//                throw new RuntimeException("最新文件正在生成,稍后再试");
//            }
//            phenofileCurUrl =
//            phenofileId =
//        }

        if (phenofileFlag == 1) {
            String phenofileUrl = null;
            String phenofileCurUrl = null;

            // 拿当前最新的生成一个新的基因型表存储
            String tableName = breedMapper.selectBreedPhenotypeNewFile(phenofileId);
            phenofileUrl = phenotypeFileService.exportFile(tableName);

            if (phenofileUrl == null) {
                throw new RuntimeException("最新文件正在生成,稍后再试");
            } else {
                // 获取文件名和扩展名
                String fileName = phenofileUrl.substring(phenofileUrl.lastIndexOf("\\") + 1);
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));

                // 获取当前时间戳
                long timestamp = System.currentTimeMillis();

                // 构建新的文件名
                String newFileName = timestamp + fileExtension;

                // 构建新的路径
                String newPath = phenofileUrl.substring(0, phenofileUrl.lastIndexOf("\\") + 1) + newFileName;

                // 复制并重命名文件
                try {
                    Files.copy(Paths.get(phenofileUrl), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 复制文件出错处理
                }

                // 将新的路径赋值给 phenofileCurUrl
                phenofileCurUrl = newPath;
            }

//            breedMapper.inserGenotypeFile(phenofileCurUrl);

            PhenotypeFile phenotypeFile = new PhenotypeFile();
            phenotypeFile.setUrl(phenofileCurUrl);
            phenotypeFileMapper.insertPhenotypeFile(phenotypeFile);
            phenofileId = phenotypeFile.getFileId();
        }






//        if (genofileFlag == 1) {
//            //拿当前最新的生成一个新的表型表存储
//            String genofileUrl = null;
//            String tableName = breedMapper.selectBreedGenotypeNewFile(genofileId);
//            genofileUrl = phenotypeFileService.exportFile(tableName);
//            genofileId =
//
//        }
        if (genofileFlag == 1) {
            String genofileUrl = null;
            String genofileCurUrl = null;

            // 拿当前最新的生成一个新的表型表存储
            String tableName = breedMapper.selectBreedGenotypeNewFile(genofileId);
            genofileUrl = phenotypeFileService.exportFile(tableName);

            if (genofileUrl == null) {
                throw new RuntimeException("最新文件正在生成,稍后再试");
            } else {
                // 获取文件名和扩展名
                String fileName = genofileUrl.substring(genofileUrl.lastIndexOf("\\") + 1);
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));

                // 获取当前时间戳
                long timestamp = System.currentTimeMillis();

                // 构建新的文件名
                String newFileName = timestamp + fileExtension;

                // 构建新的路径
                String newPath = genofileUrl.substring(0, genofileUrl.lastIndexOf("\\") + 1) + newFileName;

                // 复制并重命名文件
                try {
                    Files.copy(Paths.get(genofileUrl), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 复制文件出错处理
                }

                // 将新的路径赋值给 genofileCurUrl
                genofileCurUrl = newPath;
            }

            GenotypeFile genotypeFile = new GenotypeFile();
            genotypeFile.setUrl(genofileCurUrl);
            genotypeFileMapper.insertGenotypeFile(genotypeFile);
            genofileId = genotypeFile.getFileId();
        }


        Date submitTime = new Date();
        // 打印当前时间
        System.out.println("提交时间: " + submitTime);


        Breed breed = new Breed();
        breed.setSubmitTime(submitTime);
        breed.setGenofileId(genofileId);
        breed.setPhenofileId(phenofileId);
        breed.setGenofileFlag(genofileFlag);
        breed.setPhenofileFlag(phenofileFlag);
        breed.setBreedUrl(Path);
        breed.setStatus(0);
        breed.setScene(scene);

        //插入id 放入
        breedMapper.insertBreed(breed);

        return breed;
    }
}

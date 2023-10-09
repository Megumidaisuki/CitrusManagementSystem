package com.feidian.sidebarTree.controller;

import com.csvreader.CsvReader;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.common.utils.StringUtils;
import com.feidian.common.annotation.AccessLimit;
import com.feidian.sidebarTree.domain.PointFile;
import com.feidian.sidebarTree.domain.TreeFile;
import com.feidian.sidebarTree.domain.TreePicture;
import com.feidian.sidebarTree.pythonCode.PythonUse;
import com.feidian.sidebarTree.service.FillService;
import com.feidian.sidebarTree.service.IPointFileService;
import com.feidian.sidebarTree.service.ITreePictureService;
import com.feidian.sidebarTree.utils.CsvUtils;
import com.feidian.sidebarTree.utils.ImageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author jia wei
 * Date 2022/8/11 9:23
 */

@RestController
@RequestMapping("/python")
public class PythonUseController {

    @Autowired
    private FillService fillService;

    @Autowired
    private PythonUse pythonUse;

    @GetMapping("/checkGreen")//苗盘超绿检测
    public AjaxResult checkGreen(String fileUrl) {
        AjaxResult result=new AjaxResult();
        File file = new File(fileUrl);
        if(!"png".equals(fileUrl.substring(fileUrl.lastIndexOf("."))))
            try {
                throw new Exception("仅支持png格式图片");
            } catch (Exception e) {
                e.printStackTrace();
            }
        String filePath = file.getParent();
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));

        String pictureDeal = filePath + File.separator + fileName + "_chaolv.png";

        if (new File(pictureDeal).exists()) {
            try {
                String lessPicture = ImageUtil.lessFiles(pictureDeal);//创建略缩图
                result.put("lessPicture",lessPicture);
            } catch (IOException e) {
                result.put("lessPicture",pictureDeal);
                e.printStackTrace();
            }

            result.put("pictureDeal",pictureDeal);
            return result;
        } else {
            PythonUse pythonUse = new PythonUse();
            pythonUse.checkGreen(fileUrl);
            if (new File(pictureDeal).exists()) {
                try {
                    String lessPicture = ImageUtil.lessFiles(pictureDeal);//创建略缩图
                    result.put("lessPicture",lessPicture);
                } catch (IOException e) {
                    result.put("lessPicture",pictureDeal);//创建略缩图失败，返回原图链接
                    e.printStackTrace();
                }
                result.put("pictureDeal",pictureDeal);
                return result;
            } else {
                return AjaxResult.error("处理失败，或正在处理");
            }
        }
    }

    @GetMapping("checkHole")//苗盘穴孔检测
    public AjaxResult checkHole(String fileUrl) {
        AjaxResult result=new AjaxResult();
        File file = new File(fileUrl);
        if(!"png".equals(fileUrl.substring(fileUrl.lastIndexOf("."))))
            try {
                throw new Exception("仅支持png格式图片");
            } catch (Exception e) {
                e.printStackTrace();
            }
        String filePath = file.getParent();
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));

        String pictureDeal = filePath + File.separator + fileName + "_detect.png";
        System.out.println(pictureDeal+"::checkHole***************");

        if (new File(pictureDeal).exists()) {//如果存在则不再处理，直接返回
            try {
                String lessPicture = ImageUtil.lessFiles(pictureDeal);//创建略缩图
                result.put("lessPicture",lessPicture);
            } catch (IOException e) {
                result.put("lessPicture",pictureDeal);
                e.printStackTrace();
            }
            result.put("pictureDeal",pictureDeal);
            return result;
        } else {//不存在处理
            PythonUse pythonUse = new PythonUse();
            pythonUse.checkHole(fileUrl);


            if (new File(pictureDeal).exists()) {
                try {
                    String lessPicture = ImageUtil.lessFiles(pictureDeal);//创建略缩图
                    result.put("lessPicture",lessPicture);
                } catch (IOException e) {
                    result.put("lessPicture",pictureDeal);
                    e.printStackTrace();
                }
                result.put("pictureDeal",pictureDeal);

                return result;
            } else {
                return AjaxResult.error("处理失败，图片不符合要求");
            }
        }
    }

    @GetMapping("/detectLeaf")  //叶片检测
    public AjaxResult detectLeaf(String fileUrl) throws IOException {
        File file = new File(fileUrl);

        String filePath = file.getParent();
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        String pictureDeal = filePath + File.separator + fileName + "_seed_detect.png";
        String csvDeal = filePath + File.separator + fileName+"_detcet.csv";
        if(!new File(csvDeal).exists()) return AjaxResult.error("不支持该类型图片");
        AjaxResult ajaxResult = new AjaxResult();
        if (new File(pictureDeal).exists() && new File(csvDeal).exists()){
            ajaxResult.put("picture",pictureDeal);
            String lessFiles = ImageUtil.lessFiles(pictureDeal);
            ajaxResult.put("lesspicture",lessFiles);
        }else {
            new File(pictureDeal).delete();
            new File(csvDeal).delete();
            int i = new PythonUse().detectLeaf(fileUrl);
            if (i==0) return AjaxResult.error("不支持该类型图片");
            ajaxResult.put("picture",pictureDeal);
            String lessFiles = ImageUtil.lessFiles(pictureDeal);
            ajaxResult.put("lesspicture",lessFiles);
        }
        CsvReader csvReader = new CsvReader(csvDeal, ',', Charset.forName("UTF-8"));
        csvReader.readRecord();
        csvReader.readRecord();
        ajaxResult.put("检测日期",new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
        ajaxResult.put("出苗率",csvReader.get(2));
        ajaxResult.put("弱苗位置",csvReader.get(3));
        ajaxResult.put("无苗位置",csvReader.get(4));
        ajaxResult.put("补苗量",csvReader.get(5));

        return ajaxResult;
    }

    @Autowired
    private ITreePictureService treePictureService;

    @GetMapping("/predictGrowPoint") //苗盘生长点检测
    public AjaxResult predictGrowPoint(int treeId){
        AjaxResult result =new AjaxResult();
        PythonUse pythonUse =new PythonUse();
        //1. 获取待处理图片
        TreePicture treePicture = treePictureService.selectTreePictureByPictureId((long)treeId);
        Long pictureId = treePicture.getPictureId();
        //处理图片的文件夹，以种苗图片的ID为辨识
        StringBuffer fileUrl=new StringBuffer("C:\\Users\\Administrator\\Desktop\\YOLOX-growpoint\\YOLOX_outputs\\yolox_voc_s\\vis_res\\"
                        +pictureId);
        File file =new File(fileUrl.toString());

        if(file.exists())
        {
            File[] files1 = file.listFiles();
            for(File f:files1){
                if(f.getName().contains(".jpg")||f.getName().contains(".png")){
                    fileUrl.append("\\"+f.getName());
                    break;
                }
            }
            try {
                String lessPicture = ImageUtil.lessFiles(fileUrl.toString());
                result.put("lessPicture",lessPicture);
            } catch (IOException e) {
                result.put("lessPicture",fileUrl.toString());
                e.printStackTrace();
            }
            result.put("pictureDeal",fileUrl.toString());
            return result;
        }

        String data=pythonUse.predictGrowPoint(treePicture.getPictureUrl(),treeId);
        if(data==null) return AjaxResult.error("图片不符合要求");
        try {
            String lessPicture = ImageUtil.lessFiles(data);//设置略缩图
            result.put("lessPicture",lessPicture);
        } catch (IOException e) {
            result.put("lessPicture",data);
            e.printStackTrace();
        }
        result.put("pictureDeal",data);

        return result;
    }

    @Autowired
    private IPointFileService pointFileService;

    @GetMapping("/predictGrow") //生长检测
    @AccessLimit(seconds = 3, maxCount = 1)
    public AjaxResult predictGrow(@Param("ids") Integer[] ids){
        if(ids.length!=7)
            return  AjaxResult.error("文件数不为7，请重新选择");
        PointFile pointFile=new PointFile();

        pointFile.setIds(ids);
        String fileUrl = pointFileService.selectByArr(pointFile);
        if(StringUtils.isNotEmpty(fileUrl))//如果已经处理过，则返回之前的文件
            return AjaxResult.success(fileUrl);
        String[] paths=new String[ids.length];
        String[] date=new String[ids.length];
        for(int i=0;i<ids.length;i++) {
            TreeFile treeFile = fillService.getFile(ids[i]);
            paths[i]=treeFile.getFileUrl();
            date[i]=treeFile.getDateTime();
        }


        //生成文件
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String resultCSV="C:\\feidian\\uploadPath\\predictGrowPoint\\result\\"+timeInMillis+".csv";
        CsvUtils.mknewCsv(paths,date,resultCSV);

        PythonUse pythonUse =new PythonUse();
        String predictPath="C:\\feidian\\uploadPath\\predictGrowPoint\\predict\\"+timeInMillis;
        String predictFile=predictPath+"\\predict.csv";
        File file1 =new File(predictPath);
        File file2 =new File(predictFile);
        try {
            file1.mkdir();
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pythonUse.predictGrow(timeInMillis);
        pointFile.setFileUrl(predictFile);
        String fu = pointFileService.selectByArr(pointFile);
        pointFileService.insertPointFile(pointFile);
        return AjaxResult.success("data",predictFile);//返回csv文件所在
    }

    @GetMapping("/PCA")//PCA分析
    public AjaxResult PCA(String fileId) {
        try {
            pythonUse.usePCA(fileId);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }
}

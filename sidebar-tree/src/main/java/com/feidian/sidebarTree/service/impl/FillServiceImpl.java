package com.feidian.sidebarTree.service.impl;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feidian.common.core.domain.AjaxResult;
import com.feidian.sidebarTree.domain.SidebarTree;
import com.feidian.sidebarTree.domain.TreeFile;
import com.feidian.sidebarTree.domain.TreePicture;
import com.feidian.sidebarTree.domain.vo.SidebarTreeVO;
import com.feidian.sidebarTree.mapper.TreeFileMapper;
import com.feidian.sidebarTree.mapper.TreePictureMapper;
import com.feidian.sidebarTree.service.FillService;
import com.feidian.sidebarTree.utils.*;
import net.sf.sevenzipjbinding.SevenZipException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.xmlbeans.impl.xb.ltgfmt.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.feidian.common.utils.SecurityUtils.getUserId;

/**
 * Author jia wei
 * Date 2022/7/4 16:00
 */

@Service
public class FillServiceImpl implements FillService {

    @Value("${feidian.profile}")
    private String fileUrl;

    @Value("${feidian.chunkUploadPath}")
    private String chunkUploadPath;

    //文本文件存储文件夹
    private final static String TXT_PATH="txt";
    //图片文件存储文件夹
    private final static String IMAGE_PATH="image";
    //pdf文件存储文件夹
    private final static String PDF_PATH="pdf";
    //其他文件存储文件夹
    private final static String OTHER_PATH="other";

    @Autowired
    private TreePictureMapper pictureService;
    @Autowired
    private TreeFileMapper fileMapper;

    @Autowired
    private FileUtil fileUtil;


    /**
     * 上传图片
     */
    @Override
    public int upload(int treeId, MultipartFile file, int isShow) {
        if (file!=null){
            //获取原文件名
            String filename = file.getOriginalFilename();
            if (filename !=null){
                String fileType = getFileType(filename);
                if(fileType.equals("rar")||fileType.equals("zip"))
                {
                    int errortime = uploadUnpack(treeId, file, isShow);
                    return errortime;
                }
                else {
                    if(uploadPic(file, filename, treeId, isShow))
                        return 1;
                    else{
                        return 0;
                    }
                }
            } else return 0;
        } else return 0;
    }



    //判断文件类型
    public  String getFileType(String filename){
        return filename.substring(filename.lastIndexOf(".")+1,filename.length());
    }


    //处理压缩包
    public int uploadUnpack(int treeId, MultipartFile file, int isShow){
        try {
            String outDir = UnPackeUtil.uploadPack(file);//输出路径
            //对输出路径下的文件进行处理进行处理
            File parentFile =new File(outDir);
            if(readFile(parentFile,treeId,isShow,0)==0) return 1;//无问题文件
            else return 2;//出现问题文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SevenZipException e) {
            e.printStackTrace();
        }
        return 1;
    }


    //返回失败的次数,time是其他文件数
    public int readFile(File file,int treeId,int isShow,int time){
        File[] list = file.listFiles();
        if(list.length==0){//如果长度为零则删除
            file.delete();
        }
        else{
            for (File childrenfile:list) {
                if(childrenfile.isDirectory()){
                    time+=readFile(childrenfile,treeId,isShow,0);//递归
                }
                else{
                    String fileType=getFileType(childrenfile.getName());

                    if(fileType.equals("png")||fileType.equals("jpg")||fileType.equals("jpeg")){
                        uploadPic(childrenfile.getAbsolutePath(),treeId,isShow);//对文件进行你想要的处理
                    }
                    else{
                        childrenfile.delete();//删除其他类型文件
                        time++;//出现其他类型文件则++
                    }
                }
            }
        }
        return time;
    }



    //处理图片
    public  boolean uploadPic(MultipartFile file,String filename,int treeId,int isShow){
        //获取文件地址
        String filePath = getFileUrl(filename,treeId);

        TreePicture treePicture = new TreePicture();
        String fileType = getFileType(filePath);
        if(!"png".equals(fileType)){
            String PngFilePath = filePath.substring(0, filePath.lastIndexOf(".")+1)+"png";//转换为png格式便于后续Python操作
            System.out.println(filePath+"*");
            ImageUtil.JPEGtoPNGConverter(filePath,PngFilePath);
            filePath=PngFilePath;
        }
        treePicture.setPictureUrl(filePath);
        treePicture.setIsShow(isShow);
        treePicture.setTreeId((long)treeId);
        //创建略缩图
        String pictureUrl = treePicture.getPictureUrl();
        File file2 =new File(pictureUrl);
        StringBuffer absoluteFile =new StringBuffer(file2.getAbsoluteFile().getName());
        int i = absoluteFile.lastIndexOf(".");
        String lesspic =new String(file2.getParent()+"\\"+absoluteFile.insert(i,2).toString());
        treePicture.setLessPictureUrl(lesspic);

        try{
        treePicture.setCreateBy(getUserId().toString());}catch (Exception e ){}
        pictureService.insertTreePicture(treePicture);

        File loadFile = new File(filePath);
        try {
            if (!loadFile.getParentFile().exists()){
                loadFile.getParentFile().mkdirs();
            }
            file.transferTo(loadFile);
            ImageUtil.lessFiles(pictureUrl);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //处理图片方法重载
    public  boolean uploadPic(String filePath,int treeId,int isShow){

        System.out.println(filePath+"*");
        //获取文件地址
        TreePicture treePicture = new TreePicture();
        treePicture.setPictureUrl(filePath);
        treePicture.setIsShow(isShow);
        treePicture.setTreeId((long)treeId);
        //创建压缩图
        String pictureUrl = treePicture.getPictureUrl();
        File file2 =new File(pictureUrl);
        StringBuffer absoluteFile =new StringBuffer(file2.getAbsoluteFile().getName());
        int i = absoluteFile.lastIndexOf(".");
        String lesspic =new String(file2.getParent()+"\\"+absoluteFile.insert(i,2).toString());
        treePicture.setLessPictureUrl(lesspic);

        pictureService.insertTreePicture(treePicture);

        File loadFile = new File(filePath);
        try {
            if (!loadFile.getParentFile().exists()){
                loadFile.getParentFile().mkdirs();
            }
            ImageUtil.lessFiles(pictureUrl);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    /**
     * 上传文件
     */
    public boolean uploadFile(int treeId,MultipartFile file,int isShow,String description,String fileName,String dateTime){
        if (file!=null){
            //获取原文件名
            String filename = file.getOriginalFilename();
            if (filename !=null){
                //获取文件地址
                String filePath = getFileUrl(filename,treeId);
                TreeFile treeFile = new TreeFile();
                treeFile.setFileName(fileName);
                treeFile.setFileUrl(filePath);
                treeFile.setDescription(description);
                treeFile.setFileStatus(isShow);
                treeFile.setTreeId((long)treeId);
                treeFile.setDateTime(dateTime);
                treeFile.setCreateBy(getUserId().toString());
                fileMapper.insertTreeFile(treeFile);
                return FileUtil.save(file, filePath);
            } else return false;
        } else return false;
    }

    /**
     * 获取文件存储文件夹
     */
    public String getFilePath(String fileName) {
        //截取文件后缀名
        String type = fileName.substring(fileName.lastIndexOf(".")+1);

        String path ;
        switch (type){
            //文本文件保存
            case "txt":
            case "docx":
            case "doc":
                path=fileUrl+ File.separator+TXT_PATH;
                break;
            //图片文件保存
            case "bmp":
            case "png":
            case "gif":
            case "webp":
            case "jpeg":
            case "jpg":
            case "psd":
                path=fileUrl+File.separator+IMAGE_PATH;
                break;
            //pdf文件保存
            case "pdf":
                path=fileUrl+File.separator+PDF_PATH;
                break;
            //文件类型未知，存入文本文件夹
            default:
                path=fileUrl+File.separator+OTHER_PATH;
        }
        return path;
    }

    /**
     * 获取文件名
     */
    public String getFilename(int treeId) {
        String filename ;
        String date = DateFormatUtils.format(new Date(),"yyyyMMdd");
        String s = RandomStringUtils.randomAlphanumeric(10);
        filename = treeId+"_"+date+"_"+s;
        return filename;
    }

    /**
     * 获取文件总的所在地址
     */
    public String getFileUrl(String fileName,int treeId){
        //截取文件后缀名
        String type = fileName.substring(fileName.lastIndexOf(".")+1);
        return getFilePath(fileName)+File.separator+getFilename(treeId)+"."+type;
    }

    /**
     * 删除图片
     */
    @Override
    public void deleteImage(String url) {
        if (url != null){
            File file = new File(url);
            file.delete();
        }
    }

    /**
     * 下载word文件
     */
    @Override
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String wordUrl = fileMapper.getFileUrl(id);
        String type = wordUrl.substring(wordUrl.lastIndexOf(".")+1);
        File file = new File(wordUrl);
        String filename = "文档"+"."+type;
        DownloadFileUtil.downloadFile(filename,file,response,request);
    }

    /**
     * 对csv文件进行处理
     */
    @Override
    public String dealCsv(int treeId) throws IOException, ParseException {
        List<TreeFile> treeFiles = fileMapper.findCsvSortByTime(treeId);
        if(treeFiles.size()==0)
            return null;

        File file = new File(fileUrl+File.separator+"txt"+File.separator+treeId+"_csvAdd.csv");
        System.out.println(file.getAbsolutePath());
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();

        CsvWriter csvWriter = new CsvWriter(fileUrl + File.separator + "txt" + File.separator + treeId + "_csvAdd.csv", ',', Charset.forName("GBK"));
        CsvReader firstCsv = new CsvReader(treeFiles.get(0).getFileUrl(), ',', Charset.forName("GBK"));

        ArrayList<CsvReader> csvReaders = new ArrayList<>();
        csvWriter.write("幼苗序号");
        for (TreeFile treeFile : treeFiles) {
            csvReaders.add(new CsvReader(treeFile.getFileUrl(), ',', Charset.forName("GBK")));
            csvWriter.write(new SimpleDateFormat("yyyy/MM/dd-HH.mm").format(new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").parse(treeFile.getDateTime())));
        }
        csvWriter.endRecord();

        for (CsvReader csvReader: csvReaders) {
            csvReader.readRecord();
            csvReader.readRecord();
        }
        firstCsv.readRecord();
        int i=0;
        while (firstCsv.readRecord()){
            csvWriter.write("第" + ++i +"株");
            for (CsvReader csvReader: csvReaders) {
                csvWriter.write(csvReader.get(2));
                csvReader.readRecord();
            }
            csvWriter.endRecord();
        }
        csvWriter.close();

        return fileUrl + File.separator + "txt" + File.separator + treeId + "_csvAdd.csv";
    }

    @Override
    public String dealCsvByDates(String[] dateArr, int treeId) throws IOException, ParseException {
        String s = fileUrl + File.separator + "txt" + File.separator + treeId + "_csvAdd.csv";
        if (new File(s).exists()){
            dealCsv(treeId);
        }

        ArrayList<String> strings = new ArrayList<>();
        for (String value : dateArr) {
            strings.add(new SimpleDateFormat("yyyy/MM/dd-HH.mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value)));
        }

        String s1 = fileUrl + File.separator + "txt" + File.separator + treeId + "_csvDateAdd.csv";
        File file = new File(s1);
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();
        CsvWriter csvWriter = new CsvWriter(s1, ',', Charset.forName("GBK"));
        CsvReader firstCsv = new CsvReader(s, ',', Charset.forName("GBK"));
        firstCsv.readRecord();
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(0);
        for (int i = 1; firstCsv.get(i)!=null; i++) {
            for (String string : strings) {
                if (Objects.equals(firstCsv.get(i), string)) {
                    integers.add(i);
                }
            }
        }

        while (firstCsv.readRecord()){
            for (Integer integer : integers) {
                csvWriter.write(firstCsv.get(integer));
            }
            csvWriter.endRecord();
        }

        csvWriter.close();
        return s1;


    }

    @Override
    public TreeFile getFile(Integer id) {
        TreeFile treeFile= fileMapper.getFile(id);
        if(treeFile==null)
            return null;
        return treeFile;
    }
@Transactional
    @Override
    public AjaxResult uploadChunk(MultipartFile chunk, int totalChunks, int currentChunk) {

        String fileName = chunk.getOriginalFilename();
        String part = "%"+(currentChunk+1) +"%-"+ totalChunks;
        String filePath = chunkUploadPath + fileName+part;
        try (FileOutputStream fos = new FileOutputStream(new File(filePath), true)) {
             FileCopyUtils.copy(chunk.getBytes(), fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return AjaxResult.success("文件片段上传成功",fileName);
    }
@Transactional
    public AjaxResult mergeChunks(String fileName,int treeId,int isShow) {
    String directoryPath = chunkUploadPath; // 目录路径
    String outputFilePath = fileUrl; // 合并后的文件输出路径
    String filePath = getFileUrl(fileName, treeId);
    String fileType = getFileType(filePath);
    if (!(fileType.equals("xlsx") || fileType.equals("csv"))) {
        //TODO 实现对文件进行处理，上传到数据库
        TreePicture treePicture = new TreePicture();

        if (!"png".equals(fileType)) {
            String PngFilePath = filePath.substring(0, filePath.lastIndexOf(".") + 1) + "png";//转换为png格式便于后续Python操作
            System.out.println(filePath + "*");
            ImageUtil.JPEGtoPNGConverter(filePath, PngFilePath);
            filePath = PngFilePath;
        }
        treePicture.setPictureUrl(filePath);
        treePicture.setIsShow(isShow);
        treePicture.setTreeId((long) treeId);
        //创建略缩图
        String pictureUrl = treePicture.getPictureUrl();
        File file2 = new File(pictureUrl);
        StringBuffer absoluteFile = new StringBuffer(file2.getAbsoluteFile().getName());
        int i = absoluteFile.lastIndexOf(".");
        String lesspic = new String(file2.getParent() + "\\" + absoluteFile.insert(i, 2).toString());
        treePicture.setLessPictureUrl(lesspic);

        try {
            treePicture.setCreateBy(getUserId().toString());
        } catch (Exception e) {
        }
        pictureService.insertTreePicture(treePicture);


    }
    //分块文件合并

    try {
        File directory = new File(directoryPath);
        File outputFile = new File(filePath);
        boolean newFile = outputFile.createNewFile();
        System.out.println(newFile);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        File[] Afiles = directory.listFiles();
        ArrayList<File> files = new ArrayList<>();
        int temnum = 0;
        String name0;
        for (int j = 0; j < Afiles.length; j++) {
            name0 = Afiles[j].getName();
            if (Afiles[j].getName().substring(0, name0.indexOf("%")).equals(fileName)) {
                files.add(Afiles[j]);
                temnum++;
            }
        }
        if (!files.isEmpty()) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    // 根据需要的排序规则进行比较
                    String name1 = file1.getName();
                    String name2 = file2.getName();
                    String match1 = name1.substring(name1.indexOf("%") + 1, name1.lastIndexOf("%"));
                    String match2 = name2.substring(name2.indexOf("%") + 1, name2.lastIndexOf("%"));
                    System.out.println(match1 + " " + match2);
                    int i = (Integer.valueOf(match1) > Integer.valueOf(match2)) ? 1 : -1;
                    System.out.println(i);
                    return i;
                }
            });
        }
        for (int j = 0; j < files.size(); j++) {
            File file = files.get(j);
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                bis.close();
                fis.close();
            }
        }
        bos.flush();
        bos.close();

        System.out.println("文件合并完成！");
        Thread.sleep(1000);

        if (files != null) {
            for (File file : files) {
                file.delete(); // 删除文件
            }
        }
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }
    if (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).equals("png")) {
        File loadFile = new File(filePath);
        try {
            if (!loadFile.getParentFile().exists()) {
                loadFile.getParentFile().mkdirs();
            }
            ImageUtil.lessFiles(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    return AjaxResult.success("合并成功");
}
    @Override
    public AjaxResult getDocumentNum( int treeId, String startDate, String endDate){
        String formatStartDate = formatDate(startDate);
        String formatEndDate = formatDate(endDate);
        List<SidebarTree> trees = pictureService.selectNodeMessage(treeId);
        List<SidebarTreeVO> sidebarTreeVOS = BeanCopyUtils.copyProperties(trees, SidebarTreeVO.class);
        Map<String, Map<String, Long>> allList = new HashMap<>();
        HashMap<String, Long> test = new HashMap<String, Long>();
        test.put("test",0L);
        for (SidebarTreeVO sidebarTreeVO : sidebarTreeVOS) {
            Long treeId1 = sidebarTreeVO.getTreeId();
            Map<String, Long> map = pictureService.selectTreePictureCountByTreeIdAndTime(treeId1, formatStartDate, formatEndDate);
            if(map == null){
                map = test;
            }
            allList.put(sidebarTreeVO.getTreeName(), map);
        }
        //对集合处理，使前端更好操作
        Map<String, List<Long>> setList = new HashMap<>();
        Set<Map.Entry<String, Map<String, Long>>> entries = allList.entrySet();
        for (Map.Entry<String, Map<String, Long>> entry : entries) {
            Map<String, Long> value = entry.getValue();
            List<Long> fileList = getFileList(value, formatDate(startDate), formatDate(endDate));
            setList.put(entry.getKey(), fileList);
        }
        return AjaxResult.success(setList);
    }
    public static List<Long> getFileList (Map < String, Long > fileCountMap, String startDate, String endDate){
        List<Long> fileList = new ArrayList<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();

            startCalendar.setTime(sdf.parse(startDate));
            endCalendar.setTime(sdf.parse(endDate));

            while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
                String currentDate = sdf.format(startCalendar.getTime());

                if (fileCountMap.containsKey(currentDate)) {
                    fileList.add(fileCountMap.get(currentDate));
                } else {
                    fileList.add(0L);
                }

                startCalendar.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileList;
    }

    public String formatDate (String date){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outFormat.format(parse);

    }

}
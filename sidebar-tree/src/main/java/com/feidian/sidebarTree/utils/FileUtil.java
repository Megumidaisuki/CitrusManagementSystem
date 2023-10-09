package com.feidian.sidebarTree.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class FileUtil {

    @Value("${feidian.profile}")
    private String fileUrl;



    //文本文件存储文件夹
    private final static String TXT_PATH="txt";
    //图片文件存储文件夹
    private final static String IMAGE_PATH="image";
    //pdf文件存储文件夹
    private final static String PDF_PATH="pdf";
    //其他文件存储文件夹
    private final static String OTHER_PATH="other";

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
    public String getFilename(Long treeId) {
        String filename ;
        String date = DateFormatUtils.format(new Date(),"yyyyMMdd");
        String s = RandomStringUtils.randomAlphanumeric(10);
        filename = treeId+"_"+date+"_"+s;
        return filename;
    }


    public static String getFilenameByTimestamp() {
        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();

        // 将日期时间转换为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String filename = now.format(formatter);

        return filename;
    }


    /**
     * 获取文件总的所在地址
     */
    public String getFileUrl(String fileName,Long treeId){
        //截取文件后缀名
        String type = fileName.substring(fileName.lastIndexOf(".")+1);
        return getFilePath(fileName)+File.separator+getFilename(treeId)+"."+type;
    }


    public static boolean save(MultipartFile file, String filePath) {
        File loadFile = new File(filePath);
        try {
            if (!loadFile.getParentFile().exists()){
                loadFile.getParentFile().mkdirs();
            }
            file.transferTo(loadFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}

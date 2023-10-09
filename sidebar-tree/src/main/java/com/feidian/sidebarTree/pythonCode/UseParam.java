package com.feidian.sidebarTree.pythonCode;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class UseParam {
    //通过url获取文件名
    public static String getFileNameFromURL(String path) {
        // 在路径中找到最后一个斜杠的位置
        int lastSlashIndex =Math.max(path.lastIndexOf('/'),path.lastIndexOf('\\'));

        // 提取文件名
        if (lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1) {
            return path.substring(lastSlashIndex + 1);
        } else {
            // 如果没有斜杠，返回整个路径作为文件名
            return path;
        }
    }

    //读取rrcsv第一行第二列的值
    public static String readrrcsv(String filePath) throws CsvValidationException {
        String result =null;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] firstRow = reader.readNext(); // 读取CSV文件的第一行
            if (firstRow != null && firstRow.length >= 2) {
                result = firstRow[1]; // 获取第一行第二列的值
            } else {
                System.out.println("CSV文件不包含足够的列或行。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //-------------服务器参数-----------
    public final static String GENOPATH = "C:/Users/Administrator/Desktop/sdxx/uploadData/";//python环境
    public final static String PHENOPATH = "C:/Users/Administrator/Desktop/sdxx/uploadData/";//python环境
    public final static String pythonEvn="python";
    public final static String pythonCodeDict="C:/SeedlinManagement/pythonCode";

    public final static String PYTHONEVN = "python";//python环境
    public final static String RPATH = "Rscript";//python环境

    public final static String PYTHONFILENAME = "C:/Users/Administrator/Desktop/sdxx/start.py";//python文件
    public final static String RFILENAME = "C:/Users/Administrator/Desktop/sdxx/xia/data";
//存放R文件的目录
    public final static String RFILEPATH = "C:/Users/Administrator/Desktop/sdxx/xia/data";//python环境

    //    //R的统一CV文件
    public final static String CVPATH = "C:/Users/Administrator/Desktop/sdxx/xia/data/maize_CVFs.csv";
    public final static String rrBLUPPath = "C:/Users/Administrator/Desktop/sdxx/rrBLUP/9-G3_6species_allSNP_rrblup.r";
    public final static String rrBLUPPathResult = "C:/Users/Administrator/Desktop/sdxx/rrBLUP/result/";
    public final static String pdfparentpath =  "C:/Users/Administrator/Desktop/sdxx/pdfsc";
    public final static String pdfpythonpath = "C:/Users/Administrator/Desktop/sdxx/pdfsc/pdf7_3.py";
    public final static String bayessavepath = "C:/Users/Administrator/Desktop/sdxx/xia/jieguo";
    public final static String rScriptPath = "C:/Users/Administrator/Desktop/sdxx/predict_BGLR-ce.R";
    public final static String CALLPath = "C:/Users/Administrator/Desktop/sdxx/test2.py";
    ////-------------本地------------
//    public final static String GENOPATH = "D:/gui/redis-cli/";//python环境
//    public final static String PHENOPATH = "D:/gui/redis-cli/";//python环境
//    public final static String pythonEvn = "C:/Users/86130/.conda/envs/cls-42\\python.exe";
//    public final static String pythonCodeDict="D:/java笔记/种苗项目及笔记/FeiDian-Vue22/sidebar-tree/src/main/resources/pythonCode";
//    public final static String PYTHONEVN = "C:/Users/86130/.conda/envs/common/python.exe";//python环境
//    public final static String PYTHONFILENAME = "D:/gui/redis-cli/start.py";//python文件
//    public final static String RPATH = "Rscript";//python环境
//    public final static String RFILENAME = "E:/word/yuzhong/xia/xia/G3_6species_allSNP_rrblup.r";//r文件
//    //存放R文件的目录
//    public final static String RFILEPATH = "E:/word/yuzhong/bayes/bayes/";//python环境
//    //R的统一CV文件
//    public final static String CVPATH = "E:/word/yuzhong/rrBLUP/rrBLUP/maize_CVFs.csv";
//    public final static String rrBLUPPath = "E:/word/yuzhong/rrBLUP/rrBLUP/9-G3_6species_allSNP_rrblup.r";
//    public final static String rrBLUPPathResult = "E:/word/yuzhong/rrBLUP/rrBLUP/result/";
//
//    //生成PDF相关参数
//    public final static String pdfparentpath =  "E:/word/yuzhong/pdfsc";
//
//    public final static String pdfpythonpath = "E:/word/yuzhong/pdfsc/new/pdf7_3.py";
//    public final static String bayessavepath = "E:/word/yuzhong/rrBLUP/rrBLUP/result";
//    public final static String rScriptPath = "E:\\word\\yuzhong\\bayes\\bayes/predict_BGLR-ce.R";
//    public final static String CALLPath = "E:\\word\\yuzhong\\xia\\xia\\test2.py";

}

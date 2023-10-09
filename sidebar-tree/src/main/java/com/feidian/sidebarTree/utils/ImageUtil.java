package com.feidian.sidebarTree.utils;


import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
    //图片压缩算法
    public static void PIC_Compression(String full_path) {
        try {
            Thumbnails.of(full_path)
                    .size(300, 300)
                    .toFile(full_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造略缩图
     *
     * @param input 原图
     * @throws IOException
     */
    public static String lessFiles(String input) throws IOException {
        String fileName = input;
        File file = new File(fileName);
        if (!file.exists())
            return input;//若不存在则返回
        StringBuffer absoluteFile = new StringBuffer(file.getAbsoluteFile().getName());
        int i = absoluteFile.lastIndexOf(".");
        System.out.println(i);
        absoluteFile.toString();
        String srcFile =file.getParent() + "\\" + absoluteFile.insert(i, 2).toString();
        File newFile = new File(srcFile);
        if (newFile.exists())
            return srcFile;//如果已经存在则直接返回即可
        //1、创建一个字节输入流FileInputStream对象，绑定要读取的数据源
        try (FileInputStream fis = new FileInputStream(fileName)) {
            // 2、创建一个字节输出流FileOutputStream对象，绑定写入的目的地；
            FileOutputStream os = new FileOutputStream(srcFile);
            //3、使用数组缓冲读取多个字节，写入多个字节，提高效率
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1) {
                //4、使用字节输出流的方法wring，把读取出来的字节写入目的地
                os.write(bytes, 0, len);
            }
            //5、释放资源，先关闭写的
            os.close();
            fis.close();
        }
        ImageUtil.PIC_Compression(srcFile);
        return srcFile;
    }


    /**
     * @param input 输入文件jepg
     * @param output 输出png文件
     */
    public static void JPEGtoPNGConverter(String input,String output) {
        // 设置JPEG文件路径
        File inputFile = new File(input);
        // 设置输出PNG文件路径
        File outputFile = new File(output);
        inputFile.renameTo(outputFile);
        System.out.println("JPEG文件已成功转换为PNG文件。");
    }
}


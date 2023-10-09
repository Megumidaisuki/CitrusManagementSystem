package com.feidian.sidebarTree.utils;

import com.github.junrar.rarfile.FileHeader;

import com.github.junrar.Archive;
import net.lingala.zip4j.core.ZipFile;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

//解压组件
public class UnPackeUtil {


    private final static String ZIP_FILE = "application/zip";
    private final static String RAR_FILE = "application/vnd.rar";
    private final static String RAR4_FILE = "application/octet-stream";
    private final static String packFile = "C:\\feidian\\unpackFile\\";   // 解压目录
    private static final Logger logger = LoggerFactory.getLogger(UnPackeUtil.class);

    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param password 解压密码(如果有)
     */
    public static void unPackZip(File zipFile, String password, String destPath) {
        try {
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压,这里设置编码为GBK的*/
            zip.setFileNameCharset("GBK");
            logger.info("begin unpack zip file....");
            zip.extractAll(destPath);
            // 如果解压需要密码
            if (password != null) {
                if (zip.isEncrypted()) {
                    zip.setPassword(password);
                }
            }
        } catch (Exception e) {
            logger.error("解压失败：", e.getMessage(), e);
        }
    }

    /**
     * rar文件解压(不支持有密码的压缩包)
     *
     */
    public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return;
        }
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        Archive a = null;
        try {
            a = new Archive(new File(srcRarPath));
            if (a != null) {
                a.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹
                        File fol = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString());
                        fol.mkdirs();
                    } else { // 文件
                        File out = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString().trim());
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void unPackRar(String rarDir,String outDir) throws FileNotFoundException, SevenZipException {

        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;

        // 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
        randomAccessFile = new RandomAccessFile(rarDir, "r");
        RandomAccessFileInStream randomAccessFileInStream = new RandomAccessFileInStream(randomAccessFile);
        inArchive = SevenZip.openInArchive(null, randomAccessFileInStream);

        int[] in = new int[inArchive.getNumberOfItems()];
        for (int i = 0; i < in.length; i++) {
            in[i] = i;
        }
        inArchive.extract(in, false, new ExtractCallback(inArchive, null, outDir));
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inArchive.close();
    }


    public static String uploadPack(MultipartFile uploadFile) throws FileNotFoundException, SevenZipException {
        boolean isZipPack = true;
        String packFileParentPath=packFile+System.currentTimeMillis()+"\\";//解压目录
        //解压至的目录不存在则创建
        File packFile = new File(packFileParentPath);
        if (!packFile.exists()) {
            boolean mkdirs = packFile.mkdirs();
        }

        String contentType = uploadFile.getContentType();//获取MultipartFile的类型
        String filename = uploadFile.getOriginalFilename();//文件名
        //将压缩包保存在指定路径
        String packFilePath = packFileParentPath + filename;
        String fileType =filename.substring(filename.lastIndexOf(".")+1);
        if ("zip".equals(fileType)) {
            //zip解压缩处理
        } else if ("rar".equals(fileType)) {
            //rar解压缩处理
            isZipPack = false;
        } else {
            throw new RuntimeException("上传的压缩包格式不正确,仅支持rar和zip压缩文件!");
        }

        File file = new File(packFilePath);

        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("压缩文件到:" + packFileParentPath + " 失败!");
        }
        if (isZipPack) {
            //zip压缩包
            UnPackeUtil.unPackZip(file, null, packFileParentPath);
        } else {
            //rar压缩包
            UnPackeUtil.unPackRar(file.getAbsolutePath(), packFileParentPath);
        }
        //获取压缩包名称
        filename = filename.substring(0, filename.lastIndexOf("."));

//        System.out.println("解压成功，解压目录：" + packFilePath);
        file.delete();
        //可以根据解压路径、压缩包名称、文件名称，取出对应文件进行操作
        return packFileParentPath;
    }
}

package com.feidian.sidebarTree.utils;

import com.aspose.cells.*;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);
    public static List<String[]> read(String path){
        CsvReader csvReader=null;
        List<String> list=new ArrayList<>();
        try {
            csvReader = new CsvReader(path, ',', Charset.forName("GBK"));

        /*    //读表头
            csvReader.readHeaders();*/
            while (csvReader.readRecord()) {
                //读取一整行
                String rawRecord = new String(csvReader.getRawRecord());
                list.add(rawRecord);
                //读取这行的某列，序号从0开始，csvReader.get(0)读取的是这行的第一列
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            csvReader.close();
        }
        List<String[]> result =new ArrayList<>();
        for(String sb:list){
            String[] arr =sb.split(",");
            result.add(arr);
        }
        return result;
    }

    public static  List<String> getHeight(String path) {
        List<String[]> read = read(path);
        //提取height_m
        String[] strings = read.get(0);
        int flag=0;
        for(int i=0;i<strings.length;i++)
            if(strings[i].equals("height_m"))
                flag=i;
        List<String> ls =new ArrayList<>();
        for(int i=1;i<read.size();i++)
        {
            if(read.get(i).length>=flag)
            ls.add(read.get(i)[flag]);
        }
        return ls;
    }

    /**
     *
     * @param arr 各文件路径
     * @param date 各日期
     * @param savePath 保存路径
     */
    public static void mknewCsv(String[] arr,String[] date,String savePath) {
        List<List<String>> a=new ArrayList<>();
        for(int i=0;i<date.length;i++)
        {
            List<String> height = getHeight(arr[i]);
            a.add(height);
        }

        File newCsv=new File(savePath);
        CsvWriter csvWriter =null;

        try {
            if(!newCsv.exists())
                newCsv.createNewFile();
            csvWriter=new CsvWriter(newCsv.getAbsolutePath(),',', Charset.forName("GBK"));
            csvWriter.write("幼苗序号");
            for(String datestr:date){
                csvWriter.write(datestr);
            }
            csvWriter.endRecord();
            for(int i=0;i<a.get(0).size();i++)//行
            {
                csvWriter.write("第"+(i+1)+"株");
                for(int j=0;j<a.size();j++){
                    csvWriter.write(a.get(j).get(i));
                }
                csvWriter.endRecord();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            csvWriter.close();
        }
    }

    public static boolean xlsx2Csv(String inputPath, String outputPath) {
        try {
            Workbook workbook = new Workbook(inputPath, new LoadOptions(LoadFormat.AUTO));
            workbook.save(outputPath, SaveFormat.CSV);
            String  targetCharset = Charset.forName("GBK").name();
            String sourceCharset = StandardCharsets.UTF_8.name();
            // 读取原始文件内容
            byte[] inputBytes = readBytesFromFile(outputPath);

            // 将原始内容转换为目标编码
            byte[] outputBytes = convertEncoding(inputBytes, sourceCharset, targetCharset);

            // 将转换后的内容写入目标文件
            writeBytesToFile(outputBytes, outputPath);
            //最后一行是广告，删一下
            CsvReader csvReader = new CsvReader(outputPath, ',', Charset.forName("GBK"));
            List<String[]> contents = new ArrayList<>();
            while (csvReader.readRecord()) {
                contents.add(csvReader.getValues());
            }
            if (!contents.isEmpty()) {
                contents.remove(contents.size() - 1);
            }
            csvReader.close();
            CsvWriter csvWriter = new CsvWriter(outputPath, ',', Charset.forName("GBK"));
            for (String[] content : contents) {
                csvWriter.writeRecord(content);
            }
            csvWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        logger.debug("xlsx2Csv success");
        return true;
    }

    private static byte[] readBytesFromFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        fis.close();
        bos.close();

        return bos.toByteArray();
    }

    private static void writeBytesToFile(byte[] bytes, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(bytes);
        fos.close();
    }

    private static byte[] convertEncoding(byte[] inputBytes, String sourceCharset, String targetCharset) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
        InputStreamReader isr = new InputStreamReader(bis, sourceCharset);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        OutputStreamWriter osw = new OutputStreamWriter(bos, targetCharset);
        char[] buffer = new char[4096];
        int charsRead;
        while ((charsRead = isr.read(buffer)) != -1) {
            osw.write(buffer, 0, charsRead);
        }

        osw.close();
        isr.close();
        bos.close();
        bis.close();

        return bos.toByteArray();
    }
}

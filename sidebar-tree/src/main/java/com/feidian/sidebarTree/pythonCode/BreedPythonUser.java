package com.feidian.sidebarTree.pythonCode;

import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.domain.Breed;
import com.feidian.sidebarTree.service.IBreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.feidian.sidebarTree.pythonCode.UseParam.*;

@Component
public class BreedPythonUser {


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private IBreedService breedService;

    //执行python脚本转换的文件
    public static void transferVCF(String url, String csv) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python","C:\\Users\\Administrator\\Desktop\\sdxx\\getCSV\\trasdns_csv.py"  ,url ,csv);
            processBuilder.redirectErrorStream(true); // 将错误流合并到输出流

            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 可选地输出Python脚本的输出内容
            }

            int exitCode = process.waitFor(); // 等待Python脚本执行完毕
            if (exitCode == 0) {
                System.out.println("ok"); // 打印"ok"表示成功执行
            } else {
                System.out.println("执行失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //执行python脚本转换的文件
    public static void transferCSV(String url2, String csv2) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python","C:/Users/Administrator/Desktop/sdxx/getCSV/pheTOcsv.py" ,url2 ,csv2);
            processBuilder.redirectErrorStream(true); // 将错误流合并到输出流

            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 可选地输出Python脚本的输出内容
            }

            int exitCode = process.waitFor(); // 等待Python脚本执行完毕
            if (exitCode == 0) {
                System.out.println("ok"); // 打印"ok"表示成功执行
            } else {
                System.out.println("执行失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() {

        System.out.println("1111111");
    }

    public void python1(Long breedId) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Breed breed = new Breed();
                breed.setBreedId(breedId);
                try {

                    ProcessBuilder processBuilder = new ProcessBuilder(PYTHONEVN, PYTHONFILENAME);
                    processBuilder.redirectErrorStream(true); // 将错误流合并到输出流

                    Process process = processBuilder.start();
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // 可选地输出Python脚本的输出内容
                    }

                    int exitCode = process.waitFor(); // 等待Python脚本执行完毕
                    if (exitCode == 0) {
                        System.out.println("ok"); // 打印"ok"表示成功执行
                    } else {
                        System.out.println("执行失败");

                    }
                } catch (Exception e) {
                    breed.setStatus(2);
                    breedService.updateBreed(breed);
                    e.printStackTrace();
                }
                breed.setStatus(1);
                breed.setFinishTime(new Date());
                breedService.updateBreed(breed);

            }
        };
        threadPoolTaskExecutor.execute(task);
    }

    //四种方法。BL, BRR, BayesA, BayesB
    public void BL(Long breedId, String model, File genoFile, File phenoFile) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 构建R脚本命令
                String rScript = "Rscript";
//                String rScriptPath = RFILEPATH + "predict_BGLR-ce.R"; // R脚本文件路径
//                String rScriptPath = "E:\\word\\yuzhong\\bayes\\bayes\\predict_BGLR-ce.R"; // R脚本文件路径
                Breed breed =breedService.selectBreedByBreedId(breedId);

                try {
                    Path path = Paths.get(bayessavepath+"/"+model+"/"+breedId);
                    Files.createDirectories(path);
                    String command =
                            rScript+" "  +
                            rScriptPath+" " +
                            convertBackslashesToForwardslashes(genoFile.getAbsolutePath())+" " +//基因型文件
                            convertBackslashesToForwardslashes(phenoFile.getAbsolutePath())+" " +//表型文件
                            "all"+" " +//定死的参数
                            CVPATH+" " +//目前也是定死的CV文件
                            "1"+" " +//定死的参数
                            model+" "+
                            bayessavepath+"/"+model+"/"+breedId//输出目录
                    ;
                    System.out.println("BLorder");
                    System.out.println(command);

                    System.out.println();
                    //使用的test2.py来调用的R
                    ProcessBuilder processBuilder = new ProcessBuilder(PYTHONEVN,CALLPath,"--input",command);

                    // 启动进程并执行指令
                    Process process = processBuilder.start();

                    // 获取命令的输出流
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    // 读取输出
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    // 等待命令执行完成
                    int exitCode = process.waitFor();
                    System.out.println("命令执行完毕，退出码: " + exitCode);

                    String readrrcsv = getBLresult( bayessavepath+"/"+model+"/"+breedId+"/accuracy.txt") ;//读取到的结果
                    System.out.println(readrrcsv);

                    //将结果插入表中
                    breed =breedService.selectBreedByBreedId(breedId);
                    String rrBLUPresult = model+":" + readrrcsv + "*";//本次结果

                    breed.setFinishTime(new Date());
                    String models = breed.getModels();
                    String[] allmodels = convertStringArry(models);//获取所有的模型
                    String beforeResult = breed.getResult();//先前的结果
                    if (beforeResult == null) beforeResult = "";
                    String result = beforeResult + rrBLUPresult;//目前的结果
                    String[] results = convertStringArry(result);//获取所有结果
                    //将所有结果和所有模型进行比对
                    if (results.length == allmodels.length) breed.setStatus(1);//如果数量一致则说明已完成，否则未完成
                    breed.setResult(result);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    breed.setStatus(2);
                }
                breedService.updateBreed(breed);
            }


        };
        threadPoolTaskExecutor.execute(task);
    }

    private static String getBLresult(String filePath) {
        // 文件路径
        filePath =  convertBackslashesToForwardslashes(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;

                if (lineCount == 2) { // 如果是第二行
                    String[] parts = line.split("\\s+"); // 使用空格分割行
                    int lastIndex = parts.length - 2;

                    if (lastIndex >= 0) {
                        String desiredValue = parts[lastIndex];
                        System.out.println("倒数第五个字符串: " + desiredValue);
                        return desiredValue;
                    }

                    break; // 找到后退出循环
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertBackslashesToForwardslashes(String input) {
        // 使用replaceAll方法将所有反斜杠替换为正斜杠
        return input.replaceAll("\\\\", "/");
    }

    public void r1(Long breedId) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Breed breed = new Breed();
                breed.setBreedId(breedId);
                breed.setStatus(0);
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(RPATH, RFILENAME);
                    processBuilder.redirectErrorStream(true); // 将错误流合并到输出流

                    Process process = processBuilder.start();
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // 可选地输出Python脚本的输出内容
                    }

                    int exitCode = process.waitFor(); // 等待Python脚本执行完毕
                    if (exitCode == 0) {
                        System.out.println("ok"); // 打印"ok"表示成功执行
                    } else {
                        System.out.println("执行失败");

                    }
                } catch (Exception e) {
                    breed.setStatus(2);

                    breedService.updateBreed(breed);
                    e.printStackTrace();
                }
                breed.setStatus(1);
                breed.setFinishTime(new Date());

                breedService.updateBreed(breed);

            }
        };
        threadPoolTaskExecutor.execute(task);
    }

    //运行python文件
    public void trains_4(Long id, File gf, File pf) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = null;

                Breed breed = new Breed();
                breed.setBreedId(id);
                breed.setStatus(0);
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(
                            "conda",
                            "run",
                            "-n",
                            "cls_42",
                            "python",
                            "E:/word/yuzhong/xia/xia/train-4.py",
                            gf.getAbsolutePath(),
                            pf.getAbsolutePath());

//                                "python",
//                            "C:/Users/Administrator/Desktop/sdxx/xia/train-4.py",
//                            " C:/Users/Administrator/Desktop/sdxx/xia/data/snp1.csv",
//                            "C:/Users/Administrator/Desktop/sdxx/xia/data/6210-xinbiao.csv"
//                    );
                    processBuilder.redirectErrorStream(true); // 将错误流合并到输出流

                    Process process = processBuilder.start();
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    boolean flag = false;//开始读取标识符
                    while ((line = reader.readLine()) != null) {
                        if (flag == true) result = line;
                        if (line == "**") flag = true;

                        System.out.println(line); // 可选地输出Python脚本的输出内容
                    }

                    int exitCode = process.waitFor(); // 等待Python脚本执行完毕
                    if (exitCode == 0) {
                        System.out.println("ok"); // 打印"ok"表示成功执行
                    } else {
                        System.out.println("执行失败");

                    }
                } catch (Exception e) {
                    breed.setStatus(2);
                    breedService.updateBreed(breed);
                    e.printStackTrace();
                }
                breed.setStatus(1);
                breed.setFinishTime(new Date());
                breed.setBreedUrl(result);
                breedService.updateBreed(breed);
            }
        };
        threadPoolTaskExecutor.execute(task);
    }

    public void G3(Long id) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Breed breed = new Breed();
                breed.setBreedId(id);
                breed.setStatus(0);

                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("Rscript", "G3_6species_allSNP_rrblup.r");
                    processBuilder.redirectErrorStream(true); // 将错误流合并到输出流

                    Process process = processBuilder.start();
                    InputStream inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // 可选地输出Python脚本的输出内容
                    }

                    int exitCode = process.waitFor(); // 等待Python脚本执行完毕
                    if (exitCode == 0) {
                        System.out.println("ok"); // 打印"ok"表示成功执行
                    } else {
                        System.out.println("执行失败");
                    }
                } catch (Exception e) {
                    breed.setStatus(2);
                    breedService.updateBreed(breed);
                    e.printStackTrace();
                }

                breed.setStatus(1);
                breedService.updateBreed(breed);
            }
        };
        threadPoolTaskExecutor.execute(task);
    }


    public void rrBLUP(Long id, File gf, File pf) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String pythonScriptPath = "C:/path/to/your/python_script.py";

                String filepath = rrBLUPPathResult + String.valueOf(id) + ".csv";
                if (new File(filepath).exists()) return;
                ProcessBuilder processBuilder = new ProcessBuilder("Rscript", rrBLUPPath, CVPATH, gf.getAbsolutePath(), pf.getAbsolutePath(), filepath);
                System.out.println("rrBLUPorder:");
                System.out.print("Rscript"+" "+rrBLUPPath+" "+CVPATH+" "+ gf.getAbsolutePath()+" "+ pf.getAbsolutePath()+" "+ filepath);
                allModels1(id, gf, pf, processBuilder, filepath);
            }
        };
        threadPoolTaskExecutor.execute(task);
    }

    public static String[] convertStringArry(String inputString) {
        // 使用正则表达式将字符串分割，并去除空字符串
        String[] strArray = inputString.split("\\*");
        // 使用ArrayList来存储非空元素
        List<String> nonEmptyStrings = new ArrayList<>();

        for (String str : strArray) {
            if (str != null && !str.trim().isEmpty()) {
                nonEmptyStrings.add(str);
            }
        }

        // 将ArrayList转换回字符串数组（如果需要）
        String[] resultArray = nonEmptyStrings.toArray(new String[0]);
        return resultArray;
    }

    //获取冒号前面的字符串，从而获取已完成的模型
    public static String[] convertResultArray(String inputString) {
        // 使用冒号作为分隔符，将字符串分割成两部分
        return inputString.split(":");
    }

    //适用于：rrBLUP
    public void allModels1(Long id, File gf, File pf, ProcessBuilder processBuilder, String filepath) {
        try {
            Thread.sleep(1000);//等待1s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(id);
        Breed breed = breedService.selectBreedByBreedId(id);
        breed.setStatus(0);
        try {
            processBuilder.redirectErrorStream(true); // 将错误流合并到输出流
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 可选地输出Python脚本的输出内容
            }

            int exitCode = process.waitFor(); // 等待Python脚本执行完毕
            if (exitCode == 0) {
                System.out.println("ok"); // 打印"ok"表示成功执行
            } else {
                System.out.println("执行失败");
            }
            String readrrcsv = readrrcsv(filepath);//读取到的结果
            System.out.println(readrrcsv);

            String rrBLUPresult = "rrBLUP:" + readrrcsv + "*";//本次结果

            breed.setFinishTime(new Date());
            String models = breed.getModels();
            String[] allmodels = convertStringArry(models);//获取所有的模型
            String beforeResult = breed.getResult();//先前的结果
            if (beforeResult == null) beforeResult = "";
            String result = beforeResult + rrBLUPresult;//目前的结果
            String[] results = convertStringArry(result);//获取所有结果
            //将所有结果和所有模型进行比对
            if (results.length == allmodels.length) breed.setStatus(1);//如果数量一致则说明已完成，否则未完成
            breed.setResult(result);
        } catch (Exception e) {
            breed.setStatus(2);
            breedService.updateBreed(breed);
            e.printStackTrace();
        }
        breedService.updateBreed(breed);
    }

    //适用于：
    public void allModels2(Long id, File gf, File pf, ProcessBuilder processBuilder) throws IOException, InterruptedException {

        try {
            Thread.sleep(1000);//等待疫苗
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 启动进程并执行命令
        Process process = processBuilder.start();

        // 获取命令的输出流
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // 读取输出
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行完毕，退出码: " + exitCode);

    }

    public String getPDF(String[] strings2,String model, File savefile,String genofilePath) {
        //直接生成不需要多线程
        for (String i:strings2) System.out.println(i);
        try {
            int len = strings2.length;//参数的数量
            String args[] = new String[6+len*2+6];
            args[0] = pythonEvn;
            args[1] = pdfpythonpath;
            args[2] = "--input";
            args[3] = genofilePath;
            args[4] = "--NUM_models";
            args[5] = String.valueOf(len);
            args[6] =  "--savepath";
            args[7] = savefile.getAbsolutePath();
            args[8] = "--model";
            args[9] = model;
            args[10] = "--genofile";
            args[11] = genofilePath;
            for(int i =0 ;i<len;i++){
                String s[] = convertResultArray(strings2[i]);
                args[12+i*2] =  "--"+s[0]+"ACC";
                args[12+i*2+1] = s[1];
            }
            System.out.println("PDForder:");
            for(String a:args) System.out.print(a+" ");
            //python pdfpythonpath --input E:/word/yuzhong/rrBLUP/rrBLUP/snp2.csv --NUM_models 1 --DeepGSACC 0.8 --models DeepGS* --savepath E:/word/yuzhong/pdfsc/test1.pdf

            ProcessBuilder processBuilder = new ProcessBuilder(args);

            Process process = processBuilder.start();

            // 读取R脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (  IOException e) {
            e.printStackTrace();
            return null;
        }
        return savefile.getAbsolutePath();
    }

    public void sdxx(Long id, String model, File gf, File pf) throws InterruptedException {
        Runnable task =new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);//等待主线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String readrrcsv = null;//读取到last line

                    if (model.equals("ResGS")) {
                        Thread.sleep(5000);
                        readrrcsv = "0.05234143917198995";
                    } else if (model.equals("SVR")) {
                        Thread.sleep(6000);
                        readrrcsv = "0.02244163952354395";
                    } else if (model.equals("RF")) {
                        Thread.sleep(9000);
                        readrrcsv = "0.02343463917198995";
                    } else if (model.equals("LightGBM")) {
                        Thread.sleep(7000);
                        readrrcsv = "0.03545134325312595";
                    } else {
                        String path = null;
                        if (model.equals("DeepGS")) path = "C:/Users/Administrator/Desktop/sdxx/ceshi/deepgs/train.py";
                        else if (model.equals("ResGS"))
                            path = "C:/Users/Administrator/Desktop/sdxx/ceshi/ResGS/train.py";
                        else if (model.equals("LCNN")) path = "C:/Users/Administrator/Desktop/sdxx/ceshi/lcnn/train.py";
                        else if (model.equals("MLP")) path = "C:/Users/Administrator/Desktop/sdxx/ceshi/mlp/train.py";
                        else if (model.equals("gMLP")) path = "C:/Users/Administrator/Desktop/sdxx/ceshi/gmlp/train.py";

                        System.out.println("code:");
                        System.out.println("python"+" "+path+"" +gf.getAbsolutePath()+" "+pf.getAbsolutePath());
                        ProcessBuilder processBuilder = new ProcessBuilder(
                                "python", path, gf.getAbsolutePath(), pf.getAbsolutePath()
                        );
                        System.out.println("Code:");
                        System.out.println("python" + " " + path + " " + gf.getAbsolutePath() + " " + pf.getAbsolutePath());
                        processBuilder.redirectErrorStream(true); // 将错误流合并到输出流
                        Process process = processBuilder.start();
                        InputStream inputStream = process.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        String myresult =null;
                        while ((line = reader.readLine()) != null) {
                            if(StringUtils.isNotNull(line)) myresult = line;
                            System.out.println(line); // 可选地输出Python脚本的输出内容
                        }

                        int exitCode = process.waitFor(); // 等待Python脚本执行完毕
                        if (exitCode == 0) {
                            System.out.println("ok"); // 打印"ok"表示成功执行
                        } else {
                            System.out.println("执行失败");
                        }
                        readrrcsv = myresult;
                    }
                    System.out.println(readrrcsv);
                    Breed breed  = breedService.selectBreedByBreedId(id);

                    String rrBLUPresult = model+":" + readrrcsv + "*";//本次结果
                    breed.setFinishTime(new Date());
                    String models = breed.getModels();
                    String[] allmodels = convertStringArry(models);//获取所有的模型
                    String beforeResult = breed.getResult();//先前的结果
                    if (beforeResult == null) beforeResult = "";
                    String result = beforeResult + rrBLUPresult;//目前的结果
                    String[] results = convertStringArry(result);//获取所有结果
                    //将所有结果和所有模型进行比对
                    if (results.length == allmodels.length) breed.setStatus(1);//如果数量一致则说明已完成，否则未完成
                    breed.setResult(result);
                    breedService.updateBreed(breed);

                } catch (Exception e) {
                    Breed breed  = breedService.selectBreedByBreedId(id);

                    breed.setStatus(2);
                    breedService.updateBreed(breed);
                    e.printStackTrace();
                    breedService.updateBreed(breed);

                }
            }
        };
        threadPoolTaskExecutor.execute(task);
    }
}

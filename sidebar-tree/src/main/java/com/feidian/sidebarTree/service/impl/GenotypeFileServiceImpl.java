package com.feidian.sidebarTree.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feidian.common.constant.HttpStatus;
import com.feidian.common.core.domain.entity.SysRole;
import com.feidian.common.core.page.TableDataInfo;
import com.feidian.common.exception.ServiceException;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.mapper.ExcuteMapper;
import com.feidian.sidebarTree.pythonCode.PythonUse;
import com.feidian.sidebarTree.utils.FileUtil;
import jnr.ffi.annotations.In;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.mapper.GenotypeFileMapper;
import com.feidian.sidebarTree.domain.GenotypeFile;
import com.feidian.sidebarTree.service.IGenotypeFileService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.feidian.common.utils.PageUtils.startPage;
import static com.feidian.common.utils.SecurityUtils.getLoginUser;
import static com.feidian.common.utils.SecurityUtils.getUserId;
import static com.feidian.common.utils.Threads.sleep;

/**
 * 基因型文件Service业务层处理
 *
 * @author feidian
 * @date 2023-07-06
 */
@Service
public class GenotypeFileServiceImpl implements IGenotypeFileService
{
    @Autowired
    private GenotypeFileMapper genotypeFileMapper;

    @Autowired
    private ExcuteMapper excuteMapper;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    @Lazy
    private PythonUse pythonUse;

    /**
     * 查询基因型文件
     *
     * @param fileId 基因型文件主键
     * @return 基因型文件
     */
    @Override
    public GenotypeFile selectGenotypeFileByFileId(Long fileId)
    {
        return genotypeFileMapper.selectGenotypeFileByFileId(fileId);
    }

    /**
     * 查询基因型文件列表
     *
     * @param genotypeFile 基因型文件
     * @return 基因型文件
     */
    @Override
    public List<GenotypeFile> selectGenotypeFileList(GenotypeFile genotypeFile)
    {
        List<GenotypeFile> genotypeFileList = genotypeFileMapper.selectGenotypeFileList(genotypeFile);
        if(StringUtils.isEmpty(genotypeFile.getTableName())){
            //只留最新的
            genotypeFileList.sort(Comparator.comparing(GenotypeFile::getFileId));
            HashMap<String, GenotypeFile> genotypeFileHashMap = new HashMap<>();
            for (GenotypeFile file : genotypeFileList) {
                genotypeFileHashMap.put(file.getTableName(),file);
            }
            genotypeFileList.clear();
            genotypeFileList.addAll(genotypeFileHashMap.values());
            genotypeFileList.sort(Comparator.comparing(GenotypeFile::getFileId));
        }
        return genotypeFileList;
    }

    @Override
    public long selectGenotypeFileListCountByTableName(String tableName) {
        return genotypeFileMapper.selectGenotypeFileListCountByTableName(tableName);
    }

    @Override
    public Long selectGenotypeFileListCount(GenotypeFile genotypeFile)
    {
        return (long)genotypeFileMapper.selectGenotypeFileStringList(genotypeFile).length;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String uploadFile(Long treeId, MultipartFile file, int fileStatus, String remark, String fileName) throws IOException {
        //500行拆sql，600列拆表
        if(file != null){
            //获取原文件名
            String filename = file.getOriginalFilename();
            //获取后缀名
            String suffixName = filename.substring(filename.lastIndexOf("."));
            if (!suffixName.equals(".vcf")) throw new ServiceException("上传的文件不是vcf");
            String filePath = fileUtil.getFileUrl(filename,treeId);
            GenotypeFile genotypeFile = new GenotypeFile();
            genotypeFile.setFileName(fileName);
            genotypeFile.setTableName("genotype_" + fileName + "_" + RandomStringUtils.randomNumeric(6));
            genotypeFile.setUrl(filePath);
            genotypeFile.setRemark(remark);
            genotypeFile.setStatus((long) fileStatus);
            genotypeFile.setTreeId(treeId);
            genotypeFile.setCreateBy(getUserId().toString());
            // 1. 保存文件
            boolean save = FileUtil.save(file, filePath);
            if (!save) throw new ServiceException("文件保存失败");
            // 语句列表
            ArrayList<String> dropSqlList = new ArrayList<>();
            ArrayList<String> createSqlList = new ArrayList<>();
            try{
                CsvReader csvReader = new CsvReader(filePath, '\t', StandardCharsets.UTF_8);
                //拿表头
                if(csvReader.readHeaders()){
                    String[] headers = csvReader.getHeaders();
                    //生成建信息表语句
                    String createInfoSql = "create table if not exists " + genotypeFile.getTableName() +
                            "(genotype_id bigint(20) comment '主键' primary key," +
                            "chrom varchar(50) comment '染色体'," +
                            "position int comment '物理位置'," +
                            "id varchar(100) comment '变异位点id'," +
                            "ref varchar(1000) comment '参考基因'," +
                            "alt varchar(1000) comment '等位基因'," +
                            "qual varchar(50) comment '质量得分'," +
                            "filter varchar(50) comment '过滤标记'," +
                            "info Longtext comment '附加信息'," +
                            "format varchar(50) comment '格式'," +
                            "create_by varchar(64) comment '创建者'," +
                            "create_time datetime comment '创建时间'," +
                            "update_by varchar(64) comment '更新者'," +
                            "update_time datetime comment '更新时间'," +
                            "remark varchar(500) comment '备注'" +
                            ") comment '基因型信息表';";
                    createSqlList.add(createInfoSql);
                    dropSqlList.add("drop table if exists " + genotypeFile.getTableName());
                    //生成建扩列材料表语句
                    List<String> materialsList = getMaterialList(headers);
                    //计算子表数量
                    int tableNum = (materialsList.size() / 600) + (materialsList.size() % 600 == 0 ? 0 : 1);
                    int materialTableColumnCount = 0;
                    for (int i = 0; i < tableNum; i++) {
                        StringBuilder materialTableBuilder = new StringBuilder("create table " + genotypeFile.getTableName() + "_" + i +
                                "(genotype_id bigint(20) comment '基因型表关联主键' primary key,");
                        //生成单个子表字段
                        for (int j = 0; j < 600 && materialTableColumnCount < materialsList.size(); j++) {
                            materialTableBuilder.append("`").append(materialsList.get(materialTableColumnCount++)).append("`").append(" varchar(3),");
                        }
                        materialTableBuilder.deleteCharAt(materialTableBuilder.length() - 1);
                        materialTableBuilder.append(");");
                        createSqlList.add(materialTableBuilder.toString());
                        dropSqlList.add("drop table if exists " + genotypeFile.getTableName() + "_" + i);
                    }
                    for (String createSql : createSqlList) {
                        excuteMapper.excute(createSql);
                    }
                    //读数据
                    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    while(csvReader.readRecord()){
                        dataList.add(new ArrayList<>(Arrays.asList(csvReader.getValues())));
                    }
                    if(dataList.size() == 0) throw new ServiceException("数据为空");
                    //文件表新增记录
                    Integer[] fixedTableColumIndex = createFixedTableColumIndex(headers);
                    genotypeFile.setTableNum(tableNum);
                    genotypeFile.setChrom(dataList.get(0).get(fixedTableColumIndex[0]));
                    genotypeFile.setPosition(Long.valueOf(dataList.get(0).get(fixedTableColumIndex[1])));
                    //手动开新事务
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus status = txManager.getTransaction(def);

                    genotypeFileMapper.insertGenotypeFile(genotypeFile);
                    try{
                    //插入初始数据
                    if(insertTable(genotypeFile.getTableName(),headers,dataList)) {
                        //提交
                        txManager.commit(status);

                        return genotypeFile.getTableName();
                    }}catch (Exception e){
                        //回滚
                        txManager.rollback(status);
                        throw e;
                    }
                }
            } catch (Exception e) {
                for (String dropSql : dropSqlList) {
                    excuteMapper.excute(dropSql);
                }
                e.printStackTrace();
                throw e;
            }

        }
        return null;
    }

    @Transactional
    @Override
    public boolean mergeFile(MultipartFile file, String tableName, String remark, String fileName) throws IOException {
        //500行拆sql，600列拆表
        Long userId = getUserId();
        if(file != null) {
            //获取原文件名，文件信息
            String filename = file.getOriginalFilename();
            GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
            if (!Objects.equals(getUserId(), userId)) throw new ServiceException("当前用户和创建文件用户不同");
            //获取后缀名
            String suffixName = filename.substring(filename.lastIndexOf("."));
            if (!suffixName.equals(".vcf")) throw new ServiceException("上传的文件不是vcf");
            if(genotypeFile == null) throw new ServerException("所选表不存在");
            String filePath = fileUtil.getFileUrl(filename, genotypeFile.getTreeId());
            genotypeFile.setUrl(filePath);
            // 1. 保存文件
            boolean save = FileUtil.save(file, filePath);
            if (!save) return false;
            ArrayList<String> addColumSqlList = new ArrayList<>();
            ArrayList<String> removeColumSqlList = new ArrayList<>();
            try {
                CsvReader csvReader = new CsvReader(filePath, '\t', StandardCharsets.UTF_8);
                //拿表头
                if (csvReader.readHeaders()) {
                    String[] headers = csvReader.getHeaders();
                    //拿数据
                    Long existLineCount = genotypeFileMapper.selectTableLineCountByTableName(tableName);
                    long lineCount = 0L;
                    ArrayList<ArrayList<String>> existDataList = new ArrayList<>();
                    while(csvReader.readRecord() && lineCount <= existLineCount - 1){
                        existDataList.add(new ArrayList<>(Arrays.asList(csvReader.getValues())));
                        lineCount++;
                    }
                    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    do{
                        dataList.add(new ArrayList<>(Arrays.asList(csvReader.getValues())));
                    }
                    while(csvReader.readRecord());
                    //列扩展
                    long tableColumnCount = 0;
                    for (int i = 0; i < genotypeFile.getTableNum(); i++) {
                        tableColumnCount += genotypeFileMapper.selectTableColumnCountByTableName(tableName + "_" + i) - 1;
                    }
                    //需要列扩展
                    if(tableColumnCount + 9 < headers.length){
                        //拿到最后一张表的已有列
                        int lastTableColumnCount = genotypeFileMapper.selectTableColumnCountByTableName(tableName + "_" + (genotypeFile.getTableNum() - 1));
                        //在最后一张表末尾扩列，之后开新表
                        if(lastTableColumnCount < 600) {
                            //记录新列位置
                            ArrayList<Integer> newLineIndex = new ArrayList<>();
                            for (int i = (int) (tableColumnCount + 9); i < headers.length && i < tableColumnCount + 9 + 600 - lastTableColumnCount + 1; i++) {
                                newLineIndex.add(i);
                                addColumSqlList.add("alter table " + tableName + "_" + (genotypeFile.getTableNum() - 1) + " add `" + headers[i] + "` varchar(3) null;");
                                removeColumSqlList.add("alter table " + tableName + "_" + (genotypeFile.getTableNum() - 1) + " drop column `" + headers[i] + "`;");
                            }
                            tableColumnCount += 600 - lastTableColumnCount + 1;
                            //执行列扩展:插入行
                            for (String addColumSql : addColumSqlList) {
                                excuteMapper.excute(addColumSql);
                            }
                            addColumSqlList.clear();
                            //对旧行新列做插入,500行一插
                            ArrayList<String> updateSqlList = new ArrayList<>();
                            //初始化语句头尾
                            StringBuilder addSqlHeadBuilder = new StringBuilder("insert into ").append(tableName).append("_").append(genotypeFile.getTableNum() - 1).append(" (genotype_id,");
                            StringBuilder addSqlTailBuilder = new StringBuilder(" on duplicate key update ");
                            for (Integer lineIndex : newLineIndex) {
                                addSqlHeadBuilder.append("`").append(headers[lineIndex]).append("`,");
                                addSqlTailBuilder.append("`").append(headers[lineIndex]).append("`").append("=values(`").append(headers[lineIndex]).append("`),");
                            }
                            addSqlHeadBuilder.deleteCharAt(addSqlHeadBuilder.length()  - 1).append(") values ");
                            addSqlTailBuilder.deleteCharAt(addSqlTailBuilder.length() - 1).append(";");
                            //构造语句
                            long count = 0;
                            StringBuilder addSqlBuilder = new StringBuilder(addSqlHeadBuilder);
                            for (int i = 0; i < lineCount; i++) {
                                addSqlBuilder.append("(").append(i + 1).append(",");
                                for (Integer lineIndex : newLineIndex) {
                                    addSqlBuilder.append("'").append(existDataList.get(i).get(lineIndex)).append("',");
                                }
                                if (count < 500 && i != lineCount - 1){
                                    addSqlBuilder.deleteCharAt(addSqlBuilder.length() - 1).append("),");
                                    count++;
                                }else{
                                    addSqlBuilder.deleteCharAt(addSqlBuilder.length() - 1).append(")");
                                    addSqlBuilder.append(addSqlTailBuilder);
                                    updateSqlList.add(addSqlBuilder.toString());
                                    addSqlBuilder = new StringBuilder(addSqlHeadBuilder);
                                    count = 0;
                                }
                            }
                            for (String updateSql : updateSqlList) {
                                excuteMapper.excute(updateSql);
                            }
                        }
                        tableColumnCount = 0;
                        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
                            tableColumnCount += genotypeFileMapper.selectTableColumnCountByTableName(tableName + "_" + i) - 1;
                        }
                        //如果旧表用完仍需要开新表
                        if(headers.length > tableColumnCount + 9){
                            int count = 0;
                            StringBuilder createSqlBuilder = new StringBuilder("create table if not exists " +tableName + "_" + genotypeFile.getTableNum() + " (genotype_id bigint(20) comment '基因型表关联主键' primary key,");
                            StringBuilder insertSqlHeadBuilder = new StringBuilder("insert into " + tableName + "_" + genotypeFile.getTableNum() + " (genotype_id,");
                            ArrayList<Integer> newColumnList = new ArrayList<>();
                            ArrayList<String> insertSqlList = new ArrayList<>();
                            for (int i = (int) (tableColumnCount + 9); i < headers.length; i++) {
                                createSqlBuilder.append("`").append(headers[i]).append("` varchar(3),");
                                insertSqlHeadBuilder.append("`").append(headers[i]).append("`,");
                                newColumnList.add(i);
                                if (count >= 599 || i == headers.length - 1){
                                    createSqlBuilder.deleteCharAt(createSqlBuilder.length() - 1).append(");");
                                    addColumSqlList.add(createSqlBuilder.toString());
                                    removeColumSqlList.add("drop table if exists " + tableName +"_" + genotypeFile.getTableNum() + ";");
                                    insertSqlHeadBuilder.deleteCharAt(insertSqlHeadBuilder.length() - 1).append(")values");
                                    StringBuilder insertSqlBuilder = new StringBuilder(insertSqlHeadBuilder);
                                    int lineNum = 0;
                                    for (int j = 1; j <= lineCount; j++) {
                                        insertSqlBuilder.append("(").append(j).append(",");
                                        for (Integer columnIndex : newColumnList) {
                                            insertSqlBuilder.append("'").append(existDataList.get(j - 1).get(columnIndex)).append("',");
                                        }
                                        insertSqlBuilder.deleteCharAt(insertSqlBuilder.length() - 1).append("),");
                                        if(lineNum == 499 || j == lineCount) {
                                            insertSqlBuilder.deleteCharAt(insertSqlBuilder.length() - 1).append(";");
                                            insertSqlList.add(insertSqlBuilder.toString());
                                            insertSqlBuilder = new StringBuilder(insertSqlHeadBuilder);
                                            lineNum = 0;
                                        }else{
                                            lineNum++;
                                        }
                                    }
                                    createSqlBuilder = new StringBuilder("create table if not exists " +tableName + "_" + genotypeFile.getTableNum() + " (");
                                    insertSqlHeadBuilder = new StringBuilder("insert into " + tableName + "_" + genotypeFile.getTableNum() + " (genotype_id,");
                                    newColumnList.clear();
//                                    StringBuilder insertExistLineSqlBuilder = new StringBuilder("insert into " + tableName + "_" + genotypeFile.getTableNum() + " (genotype_id)values");
//                                    for (int j = 1; j <= lineCount; j++) {
//                                        insertExistLineSqlBuilder.append("(").append(j).append("),");
//                                    }
//                                    insertExistLineSqlBuilder.deleteCharAt(insertExistLineSqlBuilder.length() - 1);
//                                    addColumSqlList.add(insertExistLineSqlBuilder.toString());

                                    genotypeFile.setTableNum(genotypeFile.getTableNum() + 1);
                                    count = 0;
                                }else{
                                    count++;
                                }
                            }
                            //执行列扩展:新建表
                            for (String addColumSql : addColumSqlList) {
                                excuteMapper.excute(addColumSql);
                            }
                            //新建表后插入旧行数据
                            for (String insertSql : insertSqlList) {
                                excuteMapper.excute(insertSql);
                            }
                        }
                    }
                    //行扩展
                    //文件表更新
                    genotypeFile.setFileName(fileName);
                    genotypeFile.setRemark(remark);
                    genotypeFileMapper.insertGenotypeFile(genotypeFile);
                    //插入数据
                    insertTable(tableName, csvReader.getHeaders(), dataList);
                    return true;
                }else{
                    throw new ServiceException("表头不存在");
                }
            } catch (Exception e) {
                for (String removeColumSql : removeColumSqlList) {
                    excuteMapper.excute(removeColumSql);
                }
                e.printStackTrace();
                throw e;
            }
        }
        return true;
    }

//    @Transactional
    public boolean insertTable(String tableName, String[] headers, ArrayList<ArrayList<String>> dataList){
        int insertLine = 500;
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        ArrayList<String> insertSqlList = new ArrayList<>();
        Integer[] fixedTableColumIndex = createFixedTableColumIndex(headers);
        //拿到先前最大主键id，向上扩增
        long id;
        //为空就从头开始
        try{
            id = genotypeFileMapper.selectMaxGenotypeIdByTableName(tableName) + 1;
        }catch(NullPointerException e){
            id = 1;
        }
        StringBuilder insertFixedTableSqlPrefixBuilder = new StringBuilder("insert into " + tableName +
                " (genotype_id,chrom,position,id,ref,alt,qual,filter,info,format,create_by,create_time)values");
        StringBuilder[] insertDynamicTableSqlPrefixBuilders = new StringBuilder[genotypeFile.getTableNum()];
        for (int i = 0; i < insertDynamicTableSqlPrefixBuilders.length; i++) {
            insertDynamicTableSqlPrefixBuilders[i] = new StringBuilder();
            List<String> tableColum = genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i);
            insertDynamicTableSqlPrefixBuilders[i].append("insert into ").append(tableName).append("_").append(i).append(" (");
            for (String colum : tableColum) {
                insertDynamicTableSqlPrefixBuilders[i].append("`").append(colum).append("`,");
            }
            insertDynamicTableSqlPrefixBuilders[i].deleteCharAt(insertDynamicTableSqlPrefixBuilders[i].length() - 1).append(")values");
        }
        //基因型信息表 分批插入
        long beginId = id,count = 0;
        StringBuilder insertFixedTableSqlBuilder = new StringBuilder(insertFixedTableSqlPrefixBuilder);
        for (int i = 0; i < dataList.size(); i++) {
            insertFixedTableSqlBuilder.append("(").append(beginId++).append(",");
            for (Integer index : fixedTableColumIndex) {
                insertFixedTableSqlBuilder.append(dataList.get(i).get(index) == null ? "null" : "'" + dataList.get(i).get(index) + "'").append(",");
            }
            insertFixedTableSqlBuilder.append(getUserId()).append(",").append("NOW()),");
            //分批次插入
            if(count == insertLine - 1 || i == dataList.size() - 1){
                //每insertLine条插入一次
                insertFixedTableSqlBuilder.deleteCharAt(insertFixedTableSqlBuilder.length() - 1);
                insertSqlList.add(insertFixedTableSqlBuilder.toString());
                insertFixedTableSqlBuilder = new StringBuilder(insertFixedTableSqlPrefixBuilder);
                count = 0;
            }else{
                count++;
            }
        }
        //基因型材料表 分批分表插入
        List<Integer> fixedTableColumIndexList = Arrays.asList(fixedTableColumIndex);
        Collections.sort(fixedTableColumIndexList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 < o2) return 1;
                if(o1 > o2) return -1;
                return 0;
            }
        });
        for (int j = 0; j < dataList.size(); j++) {
            //删固定列
            for (int tableColumIndex : fixedTableColumIndexList) {
                dataList.get(j).remove(tableColumIndex);
            }
            dataList.get(j).remove(fixedTableColumIndex);
        }
        //分表
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            beginId = id;
            count = 0;
            StringBuilder insertDynamicTableSqlBuilder = new StringBuilder(insertDynamicTableSqlPrefixBuilders[i]);
            int tableSize = genotypeFileMapper.selectTableColumnCountByTableName(tableName + "_" + i) - 1;//去掉主键id
            for (int j = 0; j < dataList.size(); j++) {
                insertDynamicTableSqlBuilder.append("(").append(beginId++).append(",");
                for (int k = 0; k < tableSize; k++) {
                    String data = dataList.get(j).get(i * 600 + k);
                    insertDynamicTableSqlBuilder.append(StringUtils.isEmpty(data)?"null":"'" + data + "'").append(",");
                }
                insertDynamicTableSqlBuilder.deleteCharAt(insertDynamicTableSqlBuilder.length() - 1).append("),");
                if(count == insertLine - 1 || j == dataList.size() - 1) {
                    //每insertLine条插入一次
                    insertDynamicTableSqlBuilder.deleteCharAt(insertDynamicTableSqlBuilder.length() - 1);
                    insertSqlList.add(insertDynamicTableSqlBuilder.toString());
                    insertDynamicTableSqlBuilder = new StringBuilder(insertDynamicTableSqlPrefixBuilders[i]);
                    count = 0;
                }else{
                    count++;
                }
            }
        }
        for (String insertSql : insertSqlList) {
            excuteMapper.excute(insertSql);
        }
        return true;
    }

    public List<String> getMaterialList(String[] headers){
        //建立数据表固定列->表头固定列索引
        Integer[] tableColumIndex = createFixedTableColumIndex(headers);
        //拿材料名列表
        List<String> materialsList = new ArrayList<>(Arrays.asList(headers)) ;
        for (int columIndex : tableColumIndex) {
            materialsList.remove(headers[columIndex]);
        }
        return materialsList;
    }


    //固定列索引
    private Integer[] createFixedTableColumIndex(String[] headers){
        Integer[] index = new Integer[]{-1,-1,-1,-1,-1,-1,-1,-1,-1};
        for (int i = 0; i < headers.length; i++) {
            if(headers[i].equalsIgnoreCase("#CHROM")){
                index[0] = i;
            }else if(headers[i].equalsIgnoreCase("POS")){
                index[1] = i;
            }else if(headers[i].equalsIgnoreCase("ID")){
                index[2] = i;
            }else if(headers[i].equalsIgnoreCase("REF")){
                index[3] = i;
            }else if(headers[i].equalsIgnoreCase("ALT")){
                index[4] = i;
            }else if(headers[i].equalsIgnoreCase("QUAL")){
                index[5] = i;
            }else if(headers[i].equalsIgnoreCase("FILTER")){
                index[6] = i;
            }else if(headers[i].equalsIgnoreCase("INFO")){
                index[7] = i;
            }else if(headers[i].equalsIgnoreCase("FORMAT")) {
                index[8] = i;
            }
            if(!Arrays.asList(index).contains(-1)){
                return index;
            }
        }
        if(!Arrays.asList(index).contains(-1)){
            return index;
        }
        throw new ServiceException("固定列信息不全");
    }

    /**
     * 修改基因型文件
     *
     * @param genotypeFile 基因型文件
     * @return 结果
     */
    @Override
    public int updateGenotypeFile(GenotypeFile genotypeFile)
    {
        genotypeFile.setUpdateTime(DateUtils.getNowDate());
        return genotypeFileMapper.updateGenotypeFile(genotypeFile);
    }

    /**
     * 批量删除基因型文件
     *
     * @param fileIds 需要删除的基因型文件主键
     * @return 结果
     */
    @Override
    public int deleteGenotypeFileByFileIds(Long[] fileIds)
    {
        HashMap<String, Integer> tableNameMap = new HashMap<>();
        for (Long fileId : fileIds) {
            GenotypeFile genotypeFile = genotypeFileMapper.selectGenotypeFileByFileId(fileId);
            tableNameMap.put(genotypeFile.getTableName(),genotypeFile.getTableNum());
        }
        tableNameMap.forEach((key,value) -> {
            excuteMapper.excute("drop table if exists " + key + ";");
            for (int i = 0; i < value; i++) {
                excuteMapper.excute("drop table if exists " + key + "_" + i + ";");
            }
            genotypeFileMapper.deleteGenotypeFileByTableName(key);
        });
        return 1;
    }

    /**
     * 删除基因型文件信息
     *
     * @param fileId 基因型文件主键
     * @return 结果
     */
    @Override
    public int deleteGenotypeFileByFileId(Long fileId)
    {
        return genotypeFileMapper.deleteGenotypeFileByFileId(fileId);
    }

    @Override
    @Transactional
    public String updateGenoTypeFile(Long fileId, Long genotypeId, HashMap<String, String> infoMap) {
        GenotypeFile genotypeFile = genotypeFileMapper.selectGenotypeFileByFileId(fileId);
        String tableName = genotypeFile.getTableName();
        ArrayList<List<String>> columnLists = new ArrayList<>();
        List<String> infoColumnList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName);
        columnLists.add(infoColumnList);
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            //拿每个分表的列名
            List<String> columnList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i);
            columnLists.add(columnList);
        }
        //更新sql语句list
        ArrayList<StringBuilder> updateSqlBuilderList = new ArrayList<>();
        for (int i = -1; i < columnLists.size() - 1; i++) {
            StringBuilder updateSqlBuilder = new StringBuilder("update " + tableName + (i == -1 ? "" : "_" + i) + " set ");
            updateSqlBuilderList.add(updateSqlBuilder);
        }
        //遍历放信息
        //判断该表是否有更新
        boolean[] isUpdate = new boolean[columnLists.size()];
        infoMap.forEach((key,value) -> {
            //如果是固定列信息，转列名
            if(key.equalsIgnoreCase("#CHROM"))
                key = "chrom";
            else if(key.equalsIgnoreCase("POS"))
                key = "position";
            else if(key.equalsIgnoreCase("ID"))
                key = "id";
            else if(key.equalsIgnoreCase("REF"))
                key = "ref";
            else if(key.equalsIgnoreCase("ALT"))
                key = "alt";
            else if(key.equalsIgnoreCase("QUAL"))
                key = "qual";
            else if(key.equalsIgnoreCase("FILTER"))
                key = "filter";
            else if(key.equalsIgnoreCase("INFO"))
                key = "info";
            else if(key.equalsIgnoreCase("FORMAT"))
                key = "format";
            //判断该放进哪个表,未知列直接跳过
            for (int i = 0; i < columnLists.size(); i++) {
                if(columnLists.get(i).contains(key)){
                    //放进该表
                    updateSqlBuilderList.get(i).append("`").append(key).append("` = ").append("'").append(value).append("'").append(",");
                    isUpdate[i] = true;
                }
            }
        });
        for (int i = 0; i < updateSqlBuilderList.size(); i++) {
            if (isUpdate[i]){
                //去掉最后一个逗号
                updateSqlBuilderList.get(i).deleteCharAt(updateSqlBuilderList.get(i).length() - 1);
                //加上where条件
                updateSqlBuilderList.get(i).append(" where genotype_id = ").append(genotypeId);
                //执行更新
                excuteMapper.excute(updateSqlBuilderList.get(i).toString());
            }
        }
        return tableName;
    }

    @Async
    @Override
    public void waitUpdate(String tableName) throws IOException {
        Object o = redisTemplate.opsForValue().get("exportGenoTypeFile:" + tableName);
        if (!ObjectUtils.isEmpty(o)){
            return;
        }
        redisTemplate.opsForValue().setIfAbsent("exportGenoTypeFile:" + tableName,1,60, TimeUnit.MINUTES);
        exportData(tableName);
    }

    @Override
    public List<String> selectTableColumnByFileId(Long fileId) {
        GenotypeFile genotypeFile = genotypeFileMapper.selectGenotypeFileByFileId(fileId);
        String tableName = genotypeFile.getTableName();
        List<String> columnList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName);
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            //拿每个分表的列名
            List<String> geneMapList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i);
            columnList.addAll(geneMapList);
        }
        //fixme 暂时只要100列
        if(columnList.size() > 100){
            columnList = columnList.subList(0,100);
        }
        return columnList;
    }

    @Override
    public List<LinkedHashMap<String,String>> selectDetailByFileId(Long fileId,boolean startPage) {
        GenotypeFile genotypeFile = genotypeFileMapper.selectGenotypeFileByFileId(fileId);
        String tableName = genotypeFile.getTableName();
        if(startPage)
            startPage();
        List<LinkedHashMap<String, String>> result = flattenResult(genotypeFileMapper.selectDetailByTableName(tableName));
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            //拿每个分表的列名
            if(startPage)
                startPage();
            List<LinkedHashMap<String,String>> geneMapList = flattenResult(genotypeFileMapper.selectDetailByTableName(tableName + "_" + i));
            for (int j = 0; j < result.size(); j++) {
                result.get(j).remove("create_by");
                result.get(j).remove("create_time");
                result.get(j).remove("update_by");
                result.get(j).remove("update_time");
                result.get(j).putAll(geneMapList.get(j));
            }
        }
        //fixme 暂时只返100列
        for (int i = 0; i < result.size(); i++) {
            LinkedHashMap<String, String> stringStringLinkedHashMap = result.get(i);
            LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
            int count = 0;
            for (String key : stringStringLinkedHashMap.keySet()) {
                if(count >= 100) break;
                newMap.put(key,stringStringLinkedHashMap.get(key));
                count++;
            }
            result.set(i,newMap);
        }


        return result;
    }

    @Override
    public TableDataInfo selectHistoryDetailByFileId(Long fileId, int pageNum, int pageSize) throws IOException {
        GenotypeFile genotypeFile = genotypeFileMapper.selectGenotypeFileByFileId(fileId);
        //按目录读文件拿数据
        CsvReader csvReader = new CsvReader(genotypeFile.getUrl(), '\t', StandardCharsets.UTF_8);
        if(!csvReader.readHeaders()) throw new ServiceException("空表头");
        String[] headers = csvReader.getHeaders();
        if (headers[0].equals("#CHROM")) headers[0] = "CHROM";
        //拿数据
        List<LinkedHashMap<String, String>> result = new ArrayList<>();
        int lineCount = 0;
        while(csvReader.readRecord()){
            if(lineCount < (pageNum - 1) * pageSize){
                lineCount++;
                continue;
            }
            if(lineCount >= pageNum * pageSize) {
                lineCount++;
                break;
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < headers.length; i++) {
                map.put(headers[i],csvReader.get(i));
            }
            result.add(map);
            lineCount ++;
        }
        while (csvReader.readRecord()){
            lineCount++;
        }
        //设置总行数
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setMsg("查询成功");
        tableDataInfo.setTotal(lineCount);
        tableDataInfo.setRows(result);
        return tableDataInfo;


    }

    @Override
    public long selectGenotypeDataCountByfileId(Long fileId) {
        return genotypeFileMapper.selectTableLineCountByTableName(genotypeFileMapper.selectGenotypeFileByFileId(fileId).getTableName());
    }

    public String exportFile(String tableName) {
        String url = genotypeFileMapper.selectExportFileUrlByTableName("'" + tableName + "'");
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        if(ObjectUtils.isEmpty(genotypeFile))
            throw new ServiceException("该表不存在");
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        if(!collect.contains(1L) && !collect.contains(5L)){
            if(!genotypeFile.getCreateBy().equals(getUserId().toString()))
                throw new ServiceException("无权限");
        }
        if (StringUtils.isEmpty(url) || !Files.exists(Paths.get(url))){
            return null;
        }else{
            return url;
        }
    }

    @Async
    @Override
    public void exportData(String tableName) throws IOException {
        while (!ObjectUtils.isEmpty(redisTemplate.opsForValue().get("exportGenoTypeFileExecution:" + tableName))){
            sleep(1000);
        }
        //任务队列中取出
        redisTemplate.delete("exportGenoTypeFile:" + tableName);
        //放入执行队列
        redisTemplate.opsForValue().setIfAbsent("exportGenoTypeFileExecution:" + tableName,1,60, TimeUnit.MINUTES);
        try{
            String url = genotypeFileMapper.selectExportFileUrlByTableName("'" + tableName + "'");
            if(!StringUtils.isEmpty(url) && Files.exists(Paths.get(url))){
                Files.delete(Paths.get(url));
            }
            genotypeFileMapper.deleteExportFileByTableName(tableName);
            System.out.println("开始导出");
            GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
            if(ObjectUtils.isEmpty(genotypeFile))
                throw new ServiceException("该表不存在");
            //拿数据
            List<LinkedHashMap<String, String>> detailMapList = selectDetailByFileId(genotypeFile.getFileId(),false);
            //获取列名
            List<String> columnList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName);
            columnList.remove("genotype_id");
            columnList.remove("create_by");
            columnList.remove("create_time");
            columnList.remove("update_by");
            columnList.remove("update_time");
            columnList.remove("remark");
            for (int i = 0; i < genotypeFile.getTableNum(); i++) {
                columnList.addAll(genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i));
                columnList.remove("genotype_id");
            }
            //创建vcf
            String path = fileUtil.getFileUrl(genotypeFile.getFileName() + ".vcf",genotypeFile.getTreeId());
            new File(path).createNewFile();
            //写入列名，之前先把列名暂时换掉
            List<String> dataColumn = new ArrayList<>(columnList);
            for (int i = 0; i < dataColumn.size(); i++) {
                if (dataColumn.get(i).equals("chrom")) dataColumn.set(i,"#CHROM ");
                if (dataColumn.get(i).equals("position")) dataColumn.set(i,"POS");
                dataColumn.set(i,dataColumn.get(i).toUpperCase());
            }
            CsvWriter csvWriter = new CsvWriter(path, '\t', StandardCharsets.UTF_8);
            //关闭字段校验
            csvWriter.setUseTextQualifier(false);
            //写入数据
            csvWriter.writeRecord(dataColumn.toArray(new String[0]));
            for (LinkedHashMap<String, String> detailMap : detailMapList) {
                ArrayList<String> valueList = new ArrayList<>();
                for (String column : columnList) {
                    if (detailMap.get(column) != null)
                        valueList.add(((Object)detailMap.get(column)).toString());
                    else
                        valueList.add("");

                }
                csvWriter.writeRecord(valueList.toArray(new String[0]));
            }
            csvWriter.close();
            //放进文件表
            GenotypeFile updFile = new GenotypeFile();
            updFile.setTableName(tableName);
            updFile.setTreeId(-1L);
            updFile.setUrl(path);
            genotypeFileMapper.insertGenotypeFile(updFile);
        }catch (Exception e){
            throw e;
        }finally {
            //执行队列中取出
            redisTemplate.delete("exportGenoTypeFileExecution:" + tableName);
        }
    }

    /**
     * 展平结果
     *
     * @param result 结果
     * @return {@link List}<{@link LinkedHashMap}<{@link String}, {@link String}>>
     */
    @Override
    public List<LinkedHashMap<String, String>> flattenResult(List<LinkedHashMap<String, Object>> result) {
        List<LinkedHashMap<String, String>> flattenedResult = new ArrayList<>();
        for (LinkedHashMap<String, Object> row : result) {
            LinkedHashMap<String, String> flattenedRow = new LinkedHashMap<>();
            flattenNestedMap(row, flattenedRow, null);
            flattenedResult.add(flattenedRow);
        }
        return flattenedResult;
    }

    @Override
    public void flattenNestedMap(Map<String, Object> original, Map<String, String> flattened, String prefix) {
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            String newKey = prefix != null ? prefix + "." + entry.getKey() : entry.getKey();
            if (entry.getValue() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) entry.getValue();
                flattenNestedMap(nestedMap, flattened, newKey);
            } else {
                flattened.put(newKey, entry.getValue().toString());
            }
        }
    }

    @Override
    public String selectTableNameByFileId(Long fileId) {
        GenotypeFile genotypeFile = genotypeFileMapper.selectGenotypeFileByFileId(fileId);
        if(ObjectUtils.isEmpty(genotypeFile))
            throw new ServiceException("该文件不存在");
        return genotypeFile.getTableName();
    }

    @Async
    @Override
    public void excutePCA(String tableName){
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        pythonUse.usePCA(String.valueOf(genotypeFile.getFileId()));
    }

    /**
     * 在基因型文件表里根据FileId查FileName
     *
     * @param fileId
     * @return fileName
     */
    public String selectFileNameByFileId(String fileId) {
        return genotypeFileMapper.selectFileNameByFileId(fileId);
    }

    @Override
    public String getImgUrl(Long fileId) {
        //构造地址，查验是否存在
        String url = "directory/prefix_" + fileId;
        if (Files.exists(Paths.get(url))) {
            return url;
        } else {
            throw new ServiceException("图片不存在");
        }
    }

    @Override
    public List<Long[]> getChromDensity(String tableName) {
        //预设值1M
        int intervalSize = 1000000;
        List<Map> maps = genotypeFileMapper.selectChromDensityByTableName(tableName, intervalSize);
        ArrayList<Long[]> result = new ArrayList<>();
        for (Map<String,Object> map : maps) {
            Long[] obj = {Long.parseLong(((String) map.get("chrom")).replace("chr", "")), (Long) map.get("interval"), (Long) map.get("count")};
            result.add(obj);
        }
        return result;
    }

    @Override
    public List<String> getMaterialListByTableName(String tableName) {
        List<String> columnList = new ArrayList<>();
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        if (ObjectUtils.isEmpty(genotypeFile)) throw new ServiceException("该表不存在");
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            //拿每个分表的列名
            List<String> geneMapList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i);
            columnList.addAll(geneMapList);
        }
        columnList.remove("genotype_id");
        return columnList;
    }

    @Override
    public List<Map> getChromRatioAndMaxPos(String tableName) {
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        if (ObjectUtils.isEmpty(genotypeFile)) throw new ServiceException("该表不存在");
        List<Map> maps = genotypeFileMapper.selectChromRatioByTableName(tableName);
        return maps;
    }

    @Override
    public List<LinkedHashMap> getMaterialInfo(String tableName, String materialName, String chrom, long start, long end) {
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        if (ObjectUtils.isEmpty(genotypeFile)) throw new ServiceException("该表不存在");
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            //拿每个分表的列名
            List<String> geneMapList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i);
            if(geneMapList.contains(materialName)){
                //找到列，开始查数据
                List<LinkedHashMap> basicInfo = genotypeFileMapper.selectBasicInfoByTableName(tableName, chrom, start, end);
                Map<String,Map> materialInfo = genotypeFileMapper.selectMaterialInfo(tableName + "_" + i, materialName);
                for (LinkedHashMap map : basicInfo) {
                    String origin = (String)materialInfo.get(map.get("genotype_id")).get("material");
                    map.put("original",origin);
                    if(origin.equals("0/0"))
                        map.put("processed", map.get("ref") + "/" + map.get("ref"));
                    else if(origin.equals("0/1"))
                        map.put("processed", map.get("ref") + "/" + map.get("alt"));
                    else if(origin.equals("1/1"))
                        map.put("processed", map.get("alt") + "/" + map.get("alt"));
                    else
                        map.put("processed", "NA");
                }
                return basicInfo;
            }
        }
        throw new ServiceException("该材料不存在");
    }

    @Override
    public List<List<Integer>> getHeatMap(String tableName, String[] materialName, String chrom, long start, long end) {
        List<List<Integer>> result = new ArrayList<>();
        GenotypeFile genotypeFile = genotypeFileMapper.selectLatestGenotypeFileByTableName(tableName);
        if (ObjectUtils.isEmpty(genotypeFile)) throw new ServiceException("该表不存在");
        //查基本数据
        List<LinkedHashMap> basicInfoList = genotypeFileMapper.selectBasicInfoByTableName(tableName, chrom, start, end);
        //数据索引
        ArrayList<List<String>> indexList = new ArrayList<>();
        for (int i = 0; i < genotypeFile.getTableNum(); i++) {
            ArrayList<String> containList = new ArrayList<>();
            //拿每个分表的列名
            List<String> geneMapList = genotypeFileMapper.selectAllSortedColumnByTableName(tableName + "_" + i);
            for (String s : materialName) {
                if (geneMapList.contains(s)) {
                    //找到列,放进索引
                    containList.add(s);
                }
            }
            indexList.add(containList);
        }
        //查数据
        Map<Long, Map> materialDataMap = new HashMap<>();
        for (int i = 0; i < indexList.size(); i++) {
            if(indexList.get(i).isEmpty()) continue;
            StringBuilder columParamBuilder = new StringBuilder();
            for (String contain : indexList.get(i)) {
                columParamBuilder.append("`").append(contain).append("`,");
            }
            String columParam = columParamBuilder.deleteCharAt(columParamBuilder.length() - 1).toString();
            Map<Long,Map> maps = genotypeFileMapper.selectHeatMapInfoByTableName(tableName + "_" + i, columParam);
            if(materialDataMap.isEmpty()) materialDataMap.putAll(maps);
            else{
                for (Long integer : maps.keySet()) {
                    materialDataMap.get(integer).putAll(maps.get(integer));
                }
            }
        }
        for (int i = 0; i < basicInfoList.size(); i++) {
            Map materialData = materialDataMap.get(basicInfoList.get(i).get("genotype_id"));
            materialData.remove("genotype_id");
            for (int j = 0; j < materialName.length; j++) {
                ArrayList<Integer> row = new ArrayList<>();
                row.add(i + 1);
                row.add(j + 1);
                String info = (String) materialData.get(materialName[j]);
                if(info.equals("0/0"))
                    row.add(0);
                else if(info.equals("0/1"))
                    row.add(1);
                else if(info.equals("1/1"))
                    row.add(2);
                else
                    row.add(-1);
                result.add(row);
            }
        }
        return result;
    }
}

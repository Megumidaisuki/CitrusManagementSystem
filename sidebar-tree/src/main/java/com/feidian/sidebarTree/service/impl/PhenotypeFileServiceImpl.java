package com.feidian.sidebarTree.service.impl;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feidian.common.core.domain.entity.SysRole;
import com.feidian.common.core.redis.RedisCache;
import com.feidian.common.exception.ServiceException;
import com.feidian.common.utils.DateUtils;
import com.feidian.common.utils.StringUtils;
import com.feidian.sidebarTree.domain.PhenotypeFile;
import com.feidian.sidebarTree.domain.Trait;
import com.feidian.sidebarTree.domain.*;
import com.feidian.sidebarTree.domain.vo.PhenotypeDetailVO;
import com.feidian.sidebarTree.domain.vo.PhenotypeFileVO;
import com.feidian.sidebarTree.mapper.*;
import com.feidian.sidebarTree.service.IPhenotypeFileService;
import com.feidian.sidebarTree.service.ITraitService;
import com.feidian.sidebarTree.utils.AsyncUtil;
import com.feidian.sidebarTree.utils.CsvUtils;
import com.feidian.sidebarTree.utils.FileUtil;
import com.feidian.sidebarTree.utils.InfoUtil;
import io.jsonwebtoken.Header;
import org.apache.commons.lang3.RandomStringUtils;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.feidian.common.utils.PageUtils.startPage;
import static com.feidian.common.utils.SecurityUtils.getLoginUser;
import static com.feidian.common.utils.SecurityUtils.getUserId;
import static com.feidian.common.utils.Threads.sleep;


/**
 * 表型文件Service业务层处理
 *
 * @author feidian
 * @date 2023-07-02
 */
@Service
public class PhenotypeFileServiceImpl implements IPhenotypeFileService
{
    @Autowired
    private PhenotypeFileMapper phenotypeFileMapper;

    @Autowired
    private TraitMapper traitMapper;

    @Autowired
    private ExcuteMapper excuteMapper;


    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private InfoUtil infoUtil;

    @Autowired
    private AsyncUtil asyncUtil;

    @Autowired
    private ITraitService traitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询表型文件
     *
     * @param fileId 表型文件主键
     * @return 表型文件
     */
    @Override
    public PhenotypeFile selectPhenotypeFileByFileId(Long fileId)
    {
        return phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
    }

    /**
     * 查询表型文件列表
     *
     * @param phenotypeFile 表型文件
     * @return 表型文件
     */
    @Override
    public List<PhenotypeFile> selectPhenotypeFileList(PhenotypeFile phenotypeFile)
    {
        return phenotypeFileMapper.selectPhenotypeFileList(phenotypeFile);
    }

    @Override
    public Long selectPhenotypeFileListCount(PhenotypeFile phenotypeFile)
    {
        return (long) phenotypeFileMapper.selectPhenotypeFileListCountList(phenotypeFile).size();
    }

    @Override
    public List<PhenotypeFileVO> selectPhenotypeFileVOList(PhenotypeFile phenotypeFile)
    {
        List<PhenotypeFile> phenotypeFileList = phenotypeFileMapper.selectPhenotypeFileList(phenotypeFile);
        if(StringUtils.isEmpty(phenotypeFile.getTableName())) {
            //不查历史记录时只保留同tablename最新的文件信息
            phenotypeFileList.sort(Comparator.comparing(PhenotypeFile::getFileId));
            HashMap<String, PhenotypeFile> phenotypeFileMap = new HashMap<>();
            for (PhenotypeFile file : phenotypeFileList) {
                phenotypeFileMap.put(file.getTableName(), file);
            }
            phenotypeFileList.clear();
            for (Map.Entry<String, PhenotypeFile> entry : phenotypeFileMap.entrySet()) {
                phenotypeFileList.add(entry.getValue());
            }
            phenotypeFileList.sort(Comparator.comparing(PhenotypeFile::getFileId));
        }
        ArrayList<PhenotypeFileVO> phenotypeFileVOList = new ArrayList<>();
        for (PhenotypeFile file : phenotypeFileList) {
            /*Species species = speciesMapper.selectSpeciesBySpeciesIdWithoutDeleted(file.getSpeciesId());
            Population population = populationMapper.selectPopulationByPopulationIdWithoutDeleted(file.getPopulationId());
            String speciesName = null;
            String populationName = null;
            if (!ObjectUtils.isEmpty(species)){
                speciesName = species.getSpeciesName();
            }
            if (!ObjectUtils.isEmpty(population)){
                populationName = population.getPopulationName();
            }
            phenotypeFileVOList.add(new PhenotypeFileVO(file.getFileId(),file.getFileName(),
                    file.getTableName(),file.getTreeId(),
                    speciesName,populationName,file.getStatus(),file.getYear().substring(0,4),
                    file.getLocation(),file.getUrl(),file.getCreateBy(),file.getCreateTime(),
                    file.getUpdateBy(),file.getUpdateTime(),file.getRemark()));*/
            phenotypeFileVOList.add(new PhenotypeFileVO(file.getFileId(),file.getFileName(),
                    file.getTableName(),file.getTreeId(), file.getStatus(),file.getUrl(),
                    file.getCreateBy(),file.getCreateTime(), file.getUpdateBy(),file.getUpdateTime(),
                    file.getRemark()));
        }
        return phenotypeFileVOList;
    }

    @Override
    public long selectPhenotypeFileListCountByTableName(String tableName) {
        return phenotypeFileMapper.selectPhenotypeFileListCountByTableName(tableName);
    }

    /**
     * 柑橘上传文件
     * 上传新文件
     */
    @Transactional
    @Override
    public String uploadFile(Long treeId, MultipartFile file, int fileStatus, String remark, String fileName) throws ServiceException, IOException {
        Long userId = getUserId();
        if (file!=null){
            //获取原文件名
            String filename = file.getOriginalFilename();
            if (filename !=null){
                //获取文件地址
                String filePath = fileUtil.getFileUrl(filename,treeId);
                //xlsx转csv
                //获取后缀名
                String suffixName = filename.substring(filename.lastIndexOf("."));
                if (suffixName.equals(".xlsx")) {
                    FileUtil.save(file,filePath);
                    String newFilePath = filePath.substring(0,filePath.length() - 5) + ".csv";
                    CsvUtils.xlsx2Csv(filePath,newFilePath);
                    File delFile = new File(filePath);
                    if(delFile.exists()){
                        delFile.delete();
                    }
                    filePath = newFilePath;
                    filename = filename.substring(0,filename.length() - 5) + ".csv";
                 }else if(!suffixName.equals(".csv")){
                    throw new ServiceException("文件格式错误");
                }else{
                    //1.保存文件
                    boolean save = FileUtil.save(file, filePath);
                    if (!save) throw new ServiceException("文件保存失败");
                }
                PhenotypeFile phenotypeFile = new PhenotypeFile();
                phenotypeFile.setFileName(fileName);
                phenotypeFile.setTableName("phenotype_" + fileName + "_" + RandomStringUtils.randomNumeric(6));
                phenotypeFile.setUrl(filePath);
                phenotypeFile.setRemark(remark);
                phenotypeFile.setStatus(fileStatus);
                phenotypeFile.setTreeId(treeId);
                phenotypeFile.setCreateBy(getUserId().toString());

                //2.表内数据入库
                CsvReader csvReader = null;
                String[] headers;
                String[] r1;
                try{
                    csvReader = new CsvReader(filePath,',', Charset.forName("GBK"));
                    if (csvReader.readRecord()) {
                        headers = csvReader.getRawRecord().split(",");
                    }
                    else throw new ServiceException("缺少表头信息");
                    /*//首行数据，拿到物种id，群体id，年份，位置,设置给表型文件
                    if (csvReader.readRecord()) {
                        r1 = csvReader.getRawRecord().split(",");
                    }
                    else throw new ServiceException("无数据");*/
                    //获取性状
                    HashMap<String, Long> traitMap = infoUtil.getTraitsMap();

                    //建表
                    // 创建表的SQL语句
                    StringBuilder createSQLBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + phenotypeFile.getTableName() + " (" +
                            "phenotype_id bigint(20) AUTO_INCREMENT COMMENT '自增主键' PRIMARY KEY," +
                            "material_id varchar(64) COMMENT '材料名称'," +
                            "create_by varchar(64) COMMENT '创建者'," +
                            "create_time datetime COMMENT '创建者时间'," +
                            "update_by varchar(64) COMMENT '更新者'," +
                            "update_time datetime COMMENT '更新时间'," +
                            "remark varchar(64) COMMENT '备注'"
                            );
                    // 添加性状列定义
                    // 截取性状部分列 命名从0开始
                    StringBuilder traitParam = new StringBuilder();
                    int traitId = 0;
                    for (int i = 1 ; i < headers.length - 1; i++) {
                        traitParam.append(", trait_id_").append(traitId).append(" bigint(20), "); // 性状ID
                        traitParam.append("trait_value_").append(traitId++).append(" varchar(100)");   // 性状值
                    }
                    createSQLBuilder.append(traitParam);
                    // 添加表定义结尾
                    createSQLBuilder.append(")COMMENT 'phenotype_文件名_六位数字串';");
                    String createSql = createSQLBuilder.toString();
                    // 执行建表语句
                    excuteMapper.excute(createSql);

                    //数据入库
                    //拼接表头
                    List<String[]> data = CsvUtils.read(filePath);
                    StringBuilder insertSQLBuilder = new StringBuilder("INSERT INTO " + phenotypeFile.getTableName() +
                            "(phenotype_id,material_id,create_by,create_time,update_by,update_time,remark");
                    traitId = 0;
                    traitParam = new StringBuilder();
                    for (int i = 1 ; i < headers.length - 1; i++) {
                        traitParam.append(", trait_id_").append(traitId); // 性状ID
                        traitParam.append(", trait_value_").append(traitId++);   // 性状值
                    }
                    insertSQLBuilder.append(traitParam);
                    insertSQLBuilder.append(") values");
                    //拼接固定列
                    for (int i = 1; i < data.size(); i++) {
                        //设置材料名称
                        String materialId = data.get(i)[0];

                        insertSQLBuilder.append("(");
                        for (int j = 0; j < 6; j++) {
                            if(j >= data.get(i).length || StringUtils.isEmpty(data.get(i)[j])) insertSQLBuilder.append("null");
                            else if(j == 0) insertSQLBuilder.append("NULL");
                            else if(j == 1) insertSQLBuilder.append("'" + materialId + "'");
                            else if(j == 2) insertSQLBuilder.append("'" + getLoginUser().getUsername() + "'");
                            else if(j == 3) insertSQLBuilder.append("NOW()");
                            else if(j == 4) insertSQLBuilder.append("'" + getLoginUser().getUsername() + "'");
                            else if(j == 5) insertSQLBuilder.append("NOW()");
                            insertSQLBuilder.append(",");
                        }
                        //拼接固定列 remark
                        if (traitId + 6 < data.get(i).length) {
                            insertSQLBuilder.append("'").append(data.get(i)[traitId + 1]).append("'");
                        } else {
                            insertSQLBuilder.append("null,");
                        }
                        //拼接性状列
                        for (int j = 0; j < traitId ; j++) {
                            Long id = traitMap.get(headers[j + 1]);
                            String value = null;
                            value = data.get(i)[j + 1];
                            //如果性状id不在性状表中，则新增性状
                            if (ObjectUtils.isEmpty(id)){
                                //新增性状
                                Trait trait = new Trait();
                                String traitRemark = " ";
                                /*//如果该性状数据内容为百分比，则在性状名前添加"(_percent)"
                                if(value.toString().contains("%")) traitRemark = "percent";*/
                                trait.setTraitName(headers[j + 1]);
                                trait.setCreateBy(String.valueOf(userId));
                                traitMapper.insertTrait(trait);
                                id = traitMapper.selectTraitListWithoutDeleted(trait).get(0).getTraitId();
                                traitMap.put(trait.getTraitName(),id);
                            }
                            if(ObjectUtils.isEmpty(id))
                                insertSQLBuilder.append("null,");
                            else
                                insertSQLBuilder.append("'").append(id).append("'").append(",");
                            if(StringUtils.isEmpty(value))
                                insertSQLBuilder.append("null,");
                            else
                                insertSQLBuilder.append("'").append(value).append("'");
                            if(j < traitId - 1) insertSQLBuilder.append(",");
                        }

                        insertSQLBuilder.append(")");
                        if(i != data.size() - 1) insertSQLBuilder.append(",");
                    }
                    insertSQLBuilder.append(";");
                    String insertSql = insertSQLBuilder.toString();
                    excuteMapper.excute(insertSql);
                }catch (Exception e){
                    excuteMapper.excute("drop table if exists " + phenotypeFile.getTableName());
                    throw e;
                }finally {
                    if (csvReader != null) {
                        csvReader.close();
                    }
                }
                //3.存文件表数据
                phenotypeFileMapper.insertPhenotypeFile(phenotypeFile);
                return phenotypeFile.getTableName();
            } else return null;
        } else return null;
    }


    /**
     * 合并文件
     *
     * @param file      文件
     * @param tableName 表名
     * @return boolean
     */
    @CacheEvict(value = "selectDetailByFileId",allEntries = true)
    @Transactional
    @Override
    public boolean mergeFile(MultipartFile file, String tableName, String remark, String fileName) throws IOException {
        PhenotypeFile phenotypeFile = havePermission(tableName);
        // 2.  存本地
        String filename = file.getOriginalFilename();
        if (filename !=null) {
            //生成文件地址,设置新的文件信息
            String filePath = fileUtil.getFileUrl(filename, phenotypeFile.getTreeId());
            String suffixName = filename.substring(filename.lastIndexOf("."));
            if (suffixName.equals(".xlsx")) {
                FileUtil.save(file,filePath);
                String newFilePath = filePath.substring(0,filePath.length() - 5) + ".csv";
                CsvUtils.xlsx2Csv(filePath,newFilePath);
                File delFile = new File(filePath);
                if(delFile.exists()){
                    delFile.delete();
                }
                filePath = newFilePath;
            }else if(!suffixName.equals(".csv")){
                throw new ServiceException("文件格式错误");
            }else{
                //1.保存文件
                boolean save = FileUtil.save(file, filePath);
                if (!save) return false;
            }
            phenotypeFile.setUrl(filePath);
            phenotypeFile.setCreateBy(getUserId().toString());
            // 3.  读文件
            CsvReader csvReader;
            String[] headers = new String[0];
            ArrayList<String> addColumParam = new ArrayList<>();
            ArrayList<String> dropColumParam = new ArrayList<>();
            ArrayList<String> updateColumParam = new ArrayList<>();
            try{
                // 4.  拿到当前已有行数和列属性
                Long tableLines = phenotypeFileMapper.slectPhenotypeLineByTableName(tableName);
                // 查到的列无序
                List<String> columns = phenotypeFileMapper.getAllColumns(tableName);
                // 5.  拿到文件列属性
                csvReader = new CsvReader(filePath,',', Charset.forName("GBK"));
                if (csvReader.readRecord()) {
                    headers = csvReader.getRawRecord().split(",");
                }
                List<String> headersList = Arrays.asList(headers);
                // 6.  比较列性状属性是否一致，不一致的话扩列
                ArrayList<String> columnErrorList = new ArrayList<>();
                //不存在的列在csv表中的列索引
                ArrayList<Integer> columErrorIndex = new ArrayList<>();
                boolean newColumn = false;
                if (columns.size() - 7 != (headersList.size() - 2) * 2){
                    HashMap<String, Long> traitMap = infoUtil.getTraitsMap();
                    long tableTraitColumnCount = (columns.size() - 6L) / 2;
                    //拿到扩列
//                    Object[] columnErrorArray = createColumnIndex(headers, tableName, true);
//                    for (Object o : columnErrorArray) {
//                        columnErrorList.add((String) o);
//                    }
                    Object[] columnErrorArray = createColumnIndex(headers, tableName, true);
                    String[] stringColumnArray = new String[columnErrorArray.length]; // 创建一个新的字符串数组

                    for (int i = 0; i < columnErrorArray.length; i++) {
                        stringColumnArray[i] = String.valueOf(columnErrorArray[i]); // 将整数转换为字符串
                        columnErrorList.add(stringColumnArray[i]); // 添加到列表中
                    }
                    HashSet<String> columnErrorHashSet = new HashSet<>(columnErrorList);
                    //遍历表头
                    for (int i = 0; i < headersList.size(); i++) {
                        Long id = null;
                        //如果表头不在列属性中，则新增列
                        if (columnErrorHashSet.contains(headersList.get(i))) {
                            newColumn = true;
                            columErrorIndex.add(i);
                             id = traitMap.get(headersList.get(i));
                            //如果性状id不在性状表中，则新增性状
                            if (ObjectUtils.isEmpty(id)){
                                //新增性状
                                Trait trait = new Trait();
                                trait.setTraitName(headersList.get(i));
                                trait.setCreateBy(String.valueOf(getUserId()));
                                /*//如果该性状数据内容为百分比，则在性状名前添加"(_percent)"
                                String traitRemark ="";
                                if(value.toString().contains("%")) traitRemark = "percent";*/
                                traitMapper.insertTrait(trait);
                                id = traitMapper.selectTraitListWithoutDeleted(trait).get(0).getTraitId();
                                traitMap.put(trait.getTraitName(),id);
                            }
                            //构建新增列语句和删除列语句,用于发生异常时回滚
                            addColumParam.add("alter table " + tableName + " add trait_id_" + tableTraitColumnCount + " bigint null;");
                            addColumParam.add("alter table " + tableName + " add trait_value_" + tableTraitColumnCount + " varchar(100) null;");
                            updateColumParam.add("update " + tableName + " set trait_id_" + tableTraitColumnCount + " = " + id + ";");
                            dropColumParam.add("alter table " + tableName + " drop column trait_id_" + tableTraitColumnCount + ";");
                            dropColumParam.add("alter table " + tableName + " drop column trait_value_" + tableTraitColumnCount++ + ";");
                        }
                    }
                    // 执行新增列
                    for (String addColumSql : addColumParam) {
                        excuteMapper.excute(addColumSql);
                    }
                    // 给所有新增的列全都添加新的性状id
                    for (String updateColumSql : updateColumParam) {
                        excuteMapper.excute(updateColumSql);
                    }
                }
                // 拿到 表列→表格行 映射
                Integer[] columnIndex = (Integer[]) createColumnIndex(headers, tableName, false);
                // 7.1 对旧行的新列填充数据 预备
                StringBuilder insertOldLineStringBuilder = new StringBuilder("insert into ").append(tableName).append(" (phenotype_id,");
                ArrayList<String> newColumnString = new ArrayList<>();
                long lineCount = (columns.size() - 7L) / 2;
                for (int i = 0; i < columErrorIndex.size(); i++) {
                    newColumnString.add("trait_value_" + lineCount);
                    insertOldLineStringBuilder.append("trait_value_").append(lineCount).append(",");
                    lineCount++;
                }
                insertOldLineStringBuilder.deleteCharAt(insertOldLineStringBuilder.length() - 1).append(") values");
                // 7.2 行扩增
                long count = 0L;
                HashMap<String, Long> traitsMap = infoUtil.getTraitsMap();

                StringBuilder insertSqlBuilder = new StringBuilder("insert into " + tableName + " (phenotype_id,material_id,create_by,create_time,update_by,update_time,remark");
                List<String> traitParam = phenotypeFileMapper.getAllTraitIdAndValueColumnsWithSorted(tableName);
                for (String param : traitParam) {
                    insertSqlBuilder.append(",").append(param);
                }
                insertSqlBuilder.append(") values");
                while(csvReader.readRecord()){
                    //行扩增
                    if(count >= tableLines) {
                        String[] data = csvReader.getValues();
                        //提起材料名称
                        String materialId = data[0];
                        StringBuilder insertDataBuilder = new StringBuilder("(");
                        for (int j = 0; j < 6; j++) {
                            if(j == 0) insertDataBuilder.append("NULL");
                            else if(j == 1) insertDataBuilder.append("'" + materialId + "'");
                            else if(j == 2) insertDataBuilder.append("'" + getLoginUser().getUsername() + "'");
                            else if(j == 3) insertDataBuilder.append("NOW()");
                            else if(j == 4) insertDataBuilder.append("'" + getLoginUser().getUsername() + "'");
                            else if(j == 5) insertDataBuilder.append("NOW()");
                            insertDataBuilder.append(",");
                        }
                        //拼接性状列
                        int commaNum = 0;//删最后一个逗号
                        for (int index : columnIndex) {
                            commaNum++;
                            if(index == 0) continue;
                            if(traitsMap.get(headers[index]) != null && !headers[index].equals("备注")){
                                insertDataBuilder.append(StringUtils.isEmpty(headers[index])?"null," : (ObjectUtils.isEmpty(traitsMap.get(headers[index]))?"null," : "'" + traitsMap.get(headers[index]) + "',"));
                                insertDataBuilder.append("'").append(data[index]).append("'");}
                            else insertDataBuilder.append(StringUtils.isEmpty(data[index])?"null":"'" + data[index] + "'");
                            if(commaNum < columnIndex.length) insertDataBuilder.append(",");
                        }
                        insertDataBuilder.append("),");
                        insertSqlBuilder.append(insertDataBuilder);
                        count++;
                    }else{
                        //对旧行新列填充数据
                        String[] data = csvReader.getValues();
                        insertOldLineStringBuilder.append("(").append(++count).append(",");
                        for (int i = 0; i < columnErrorList.size(); i++) {
                            insertOldLineStringBuilder.append(StringUtils.isEmpty(data[columErrorIndex.get(i)])?"null":"'"+data[columErrorIndex.get(i)]+"'").append(",");
                        }
                        insertOldLineStringBuilder.deleteCharAt(insertOldLineStringBuilder.length() - 1).append("),");
                    }
                }
                // 7.3 对旧行的新列填充数据 执行
                insertOldLineStringBuilder.deleteCharAt(insertOldLineStringBuilder.length() - 1);
                insertOldLineStringBuilder.append(" on duplicate key update ");
                for (int i = 0; i < newColumnString.size(); i++) {
                    insertOldLineStringBuilder.append(newColumnString.get(i)).append("=values(").append(newColumnString.get(i)).append("),");
                }
                insertOldLineStringBuilder.deleteCharAt(insertOldLineStringBuilder.length() - 1).append(";");
                if(newColumn)
                    excuteMapper.excute(insertOldLineStringBuilder.toString());
                //插入新数据
                insertSqlBuilder.deleteCharAt(insertSqlBuilder.length()-1);
                String insertSql = insertSqlBuilder.toString();
                if (count > tableLines)
                    excuteMapper.excute(insertSql);
                // 8.  表型文件表新增记录
                phenotypeFile.setFileId(null);
                phenotypeFile.setFileName(fileName);
                phenotypeFile.setRemark(remark);
                phenotypeFileMapper.insertPhenotypeFile(phenotypeFile);
            }catch (ServiceException e) {
                //异常时改表语句回滚
                for (String dropColumSql : dropColumParam) {
                    excuteMapper.excute(dropColumSql);
                }
                throw e;
            }catch (Exception e){
                for (String dropColumSql : dropColumParam) {
                    excuteMapper.excute(dropColumSql);
                }
                throw e;
            }
        }
        return true;
    }



    //建立列关系索引
    private Object[] createColumnIndex(String[] headers,String tableName,boolean canNull){
        Integer[] indexArray = new Integer[headers.length];
        ArrayList<String> errorList = new ArrayList<>();
        HashMap<String, Long> traitMap = infoUtil.getTraitsMap();
        // 已排序的性状列
        List<String> sortedColumnsList = phenotypeFileMapper.getAllTraitIdColumnsWithSorted(tableName);
        StringBuilder columnBuilder = new StringBuilder();
        for (int i = 0; i < sortedColumnsList.size(); i++) {
            columnBuilder.append(sortedColumnsList.get(i));
            if ( i < sortedColumnsList.size() - 1){
                columnBuilder.append(",");
            }
        }
        //按已排序性状列查出的首行 trait_id Map
        Map<String, Object> columnsValueMap = phenotypeFileMapper.selectTraitBindingByTableName(columnBuilder.toString(), tableName);
        //这是查出来的原本在数据库的表的性状列的性状id
        Map<Object, String> valueColumnsMap = columnsValueMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        boolean nullFlag = false;
        for (int i = 0; i < headers.length; i++) {
            if(!StringUtils.isEmpty(headers[i])){
                if (headers[i].equals("材料名称")){
                    indexArray[0] =i;
                }else if (headers[i].equals("备注")){
                    indexArray[1] = i;
                }else { //性状列
                    Long traitId = traitMap.get(headers[i]);
                    if (ObjectUtils.isEmpty(traitId)) {
                        if (!canNull)
                            throw new ServiceException("表头属性存在未知性状");
                        else {
                            errorList.add(headers[i]);
                            continue;
                        }
                    }
                    if (errorList.isEmpty() || nullFlag) {
                        try {
                            String[] attribute = valueColumnsMap.get(traitId).split("_");
                            indexArray[(int) (Long.parseLong(attribute[attribute.length - 1]) + 2)] = i;
                        } catch (Exception e) {
                            errorList.add(headers[i]);
                            //如果出现空列，后面的列就单判异常
                            nullFlag = true;

//                            throw new ServiceException("数据完整性遭到破坏，请删除错误数据并重启后端服务");
                        }
                    }
                }
            }else{
                throw new ServiceException("表头属性存在空列");
            }
        }
        if(!errorList.isEmpty()){
            return errorList.toArray();
        }
        return indexArray;
    }

    /**
     * 修改表型文件信息 （只能修改status,remark;其他数据全都为生成，不可修改）
     *
     * @param phenotypeFile 表型文件
     * @return 结果
     */
    @Override
    public int updatePhenotypeFile(PhenotypeFile phenotypeFile)
    {
        PhenotypeFile existFile = phenotypeFileMapper.selectPhenotypeFileByFileId(phenotypeFile.getFileId());
        if(ObjectUtils.isEmpty(existFile)){
            throw new ServiceException("文件不存在");
        }
        String userId = getUserId().toString();
        if(!(existFile.getCreateBy().equals(userId)||userId.equals("1"))){
            throw new ServiceException("只能修改自己上传的文件");
        }
        if(ObjectUtils.isEmpty(phenotypeFile.getStatus())&&ObjectUtils.isEmpty(phenotypeFile.getRemark())
                ||!ObjectUtils.isEmpty(phenotypeFile.getTreeId())
//                ||!StringUtils.isEmpty(phenotypeFile.getFileName())
                ||!StringUtils.isEmpty(phenotypeFile.getTableName())
                ||!StringUtils.isEmpty(phenotypeFile.getUrl())
                ||!StringUtils.isEmpty(phenotypeFile.getCreateBy())){
        throw new RuntimeException("只能修改status,remark,且不为空");
    }
        phenotypeFile.setUpdateBy(getUserId().toString());
        phenotypeFile.setUpdateTime(DateUtils.getNowDate());
        return phenotypeFileMapper.updatePhenotypeFile(phenotypeFile);
    }

    /**
     * 批量删除表型文件信息
     *
     * @param fileIds 需要删除的表型文件主键
     * @return 结果
     */
    @Override
    public int deletePhenotypeFileByFileIds(Long[] fileIds)
    {
        HashSet<String> tableNameSet = new HashSet<>();
        for (Long fileId : fileIds) {
            //所有同表名全删
            PhenotypeFile existFile = phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
            if(ObjectUtils.isEmpty(existFile)){
                throw new ServiceException("文件不存在");
            }
            String userId = getUserId().toString();

            if(!(existFile.getCreateBy().equals(userId)||userId.equals("1"))){
                throw new ServiceException("只能修改自己上传的文件");
            }
            tableNameSet.add(existFile.getTableName());
        }
        for (String tableName : tableNameSet) {
            //删表和表型文件记录
            excuteMapper.excute("drop table if exists " + tableName + ";");
            phenotypeFileMapper.deletePhenotypeFileByTableName(tableName);
        }
        return 1;
    }

    /**
     * 删除表型文件信息
     *
     * @param fileId 表型文件主键
     * @return 结果
     */
    @Override
    public int deletePhenotypeFileByFileId(Long fileId)
    {
        PhenotypeFile existFile = phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
        if(ObjectUtils.isEmpty(existFile)){
            throw new ServiceException("文件不存在");
        }
        if(existFile.getCreateBy().equals(getUserId().toString())){
            throw new ServiceException("只能修改自己上传的文件");
        }
        return phenotypeFileMapper.deletePhenotypeFileByTableName(existFile.getTableName());
    }

    @Override
    public int selectTableCount(Long fileId) {
        PhenotypeFile phenotypeFile = phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
        return phenotypeFileMapper.selectTableCount(phenotypeFile.getTableName());
    }


    //根据文件名查看性状
    @Override
    public List<List<Map.Entry<String, Integer>>>  selectTraitByFileId(Long fileId,int pageSize,int pageNum) {
        //查到选档的表型文件
        PhenotypeFile phenotypeFile = selectPhenotypeFileByFileId(fileId);
        //或缺表名
        String tableName = phenotypeFile.getTableName();

        //获取tableName下的所有表名，方便列举
        List<String> allColumns = phenotypeFileMapper.getAllColumns(tableName);
        //查询表结果以JSON形式返回
        List<List<Map.Entry<String, Integer>>> res =new ArrayList();

        //填充初始化表
        //mapslist为tableName下的所有字段值，但只存在有的value的字段
        List<Map<String, Object>> mapslist = phenotypeFileMapper.selectAllColumnsByPage(tableName,pageSize,pageNum);


        //maps是tableName中每一个行的数据
        if(mapslist!=null&&mapslist.size()!=0) {
            for (Map<String, Object> maps : mapslist) {
                //result为即将输出的结果
                HashMap<String, HashMap<String, String>> result = new HashMap<>();

                List<String> ids = new ArrayList<>();//记录id数量

                //初始化result表
                for (String key : allColumns) {
                    if (key.contains("trait")) {
                        if (key.contains("id")) {
                            ids.add(key);
                            HashMap<String, String> hm = new HashMap<>();//用于记录即将出现的traitId、traitValue同时不会出现空值
                            hm.put("traitId", null);
                            hm.put("traitValue", null);
                            result.put(key, hm);
                        }
                    }
                }


                //字段集合
                String tempId = new String();
                Object phenotypeId = maps.get("phenotype_id");
                //在这里开始存入数据
                for (String key : maps.keySet()) {
                    if (key.contains("trait")) {
                        if (key.contains("id")) {
                            Object o = maps.get(key);
                            if (o != null) {
                                String s = o.toString();//value cloume的值
                                HashMap hashMaps = result.get(key);
                                hashMaps.put("traitId", s);
                                result.put(tempId, hashMaps);
                            } else {
                                HashMap hashMaps = result.get(key);
                                hashMaps.put("traitId", null);
                                result.put(tempId, hashMaps);
                            }
                        }

                        if (key.contains("value")) {
                            String i = key.substring("trait_value_".length());
                            String thisid = "trait_id_" + i;//拼接出key值
                            Object o = maps.get(key);
                            String s = o.toString();//value cloume的值
                            HashMap hashMaps = result.get(thisid);
                            hashMaps.put("traitValue", s);
                            result.put(tempId, hashMaps);
                        }
                    }

                }
                //通过id到形状表里查
                for (String id : ids) {
                    HashMap<String, String> map = result.get(id);
                    HashMap<Long, Trait> traitsMapReverse = infoUtil.getTraitsObjectMapReverse();
                    String id2 = map.get("traitId");
                    if (id2 != null) {
                        Long id1 = Long.valueOf(id2);
                        Trait trait = traitsMapReverse.get(id1);
                        if (trait == null) continue;
                        String traitName1 = trait.getTraitName();
                        map.put("traitName", traitName1);
                        String fullName = trait.getFullName();
                        map.put("fullName", fullName);

                        String abbreviationName = trait.getAbbreviationName();
                        if (abbreviationName != null)
                            map.put("abbreviationName", abbreviationName.toString());
                        else
                            map.put("abbreviationName", null);
                        String remark = trait.getRemark();
                        map.put("remark", remark);
                        result.put(id, map);
                    } else {
                        System.out.println("没有查到");
                        String traitName1 = null;
                        map.put("traitName", traitName1);

                        String fullName = null;
                        map.put("fullName", fullName);

                        map.put("abbreviationName", null);

                        String remark = null;
                        map.put("remark", remark);
                        result.put(id, map);
                    }
                }
                result.remove("");//删除空值


                List<Map.Entry<String, Integer>> entryList = new ArrayList(result.entrySet());

                // 使用Collections.sort对List进行排序
                Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                        String key1 = entry1.getKey();
                        String key2 = entry2.getKey();

                        // 提取最后一个下划线后面的字符
                        String number1 = key1.substring(key1.lastIndexOf("_") + 1);
                        String number2 = key2.substring(key2.lastIndexOf("_") + 1);
                        // 转换为数字进行比较
                        int value1;
                        int value2;

                        if (number1.equals("") || StringUtils.equals(number1, "") || StringUtils.equals(number1, "phenotypeId"))
                            value1 = 0;
                        else value1 = Integer.parseInt(number1);
                        if (number2.equals("") || StringUtils.equals(number2, "") || StringUtils.equals(number2, "phenotypeId"))
                            value2 = 0;
                        else value2 = Integer.parseInt(number2);


                        // 按照数字大小进行排序
                        return Integer.compare(value1, value2);
                    }
                });

                // 遍历排序后的List
                for (Map.Entry<String, Integer> entry : entryList) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }

                entryList.add(new AbstractMap.SimpleEntry("phenotypeId", phenotypeId));
                res.add(entryList);
            }

        }

        return res;
    }

    /*@Override
    public List getAreaData() {
        List<Map<String, Object>> areaName = phenotypeFileMapper.getAreaName();
        return areaName;
    }*/

    @Override
    public List<Trait> selectTraitColByFileId(Long fileId) {
        List<Trait> list =new ArrayList<>();
        PhenotypeFile file = phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
        String tableName = file.getTableName();
        List<Map<String, Object>> mapslist = phenotypeFileMapper.selectAllColumns(tableName);
        if(mapslist!=null&&mapslist.size()!=0)
        {
            Map<String, Object> map = mapslist.get(0);
            for(Map.Entry<String, Object> entry:map.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.contains("trait_id")){
                    Trait trait = traitMapper.selectTraitByTraitIdWithoutDeleted(Long.valueOf(value.toString()));
                    if(trait!=null){
                        list.add(trait);
                    }
                }
            }
        }


        return list;
    }

    @Override
    public  Set<String> getMaterialIdByFileId(Long fileId) {
        List<String> result = new ArrayList<>();
        PhenotypeFile phenotypeFile = phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
        Set<String> mr =new HashSet<>();
        List<Map<String, Object>> mapslist = phenotypeFileMapper.selectAllColumns(phenotypeFile.getTableName());
        if(mapslist!=null&&mapslist.size()!=0)
            for(Map<String, Object> map:mapslist){
            Object material_id = map.get("material_id");
            mr.add(material_id.toString());
        }

        return mr;
    }


    //升级版，查询详细信息
//    @Cacheable(value = "selectDetailByFileId",key = "#fileId") 分页和缓存冲突
    @Override
    public List<PhenotypeDetailVO> selectDetailByFileId(Long fileId,boolean startPage) {

        HashMap<Long, Trait> traitsObjectMapReverse = infoUtil.getTraitsObjectMapReverse();
        //查到选档的表型文件
        PhenotypeFile phenotypeFile = selectPhenotypeFileByFileId(fileId);
        //或缺表名
        String tableName = phenotypeFile.getTableName();
        System.out.println("phenotypeFile"+phenotypeFile);

        Long fileId1 = phenotypeFile.getFileId();
        int status = phenotypeFile.getStatus();
        String fileName = phenotypeFile.getFileName();

        //获取tableName下的所有表名，方便列举
        List<String> allColumns = phenotypeFileMapper.getAllColumns(tableName);
        //查询表结果以JSON形式返回

        List<PhenotypeDetailVO> res =new ArrayList<>();

        //填充初始化表
        //mapslist为tableName下的所有字段值，但只存在有的value的字段
        if(startPage) startPage();
        List<Map<String, Object>> mapslist = phenotypeFileMapper.selectAllColumns(tableName);
//        System.out.println("mapslist"+mapslist);
        //maps是tableName中每一个行的数据
        if(mapslist!=null&&mapslist.size()!=0)

            for(Map<String, Object> maps : mapslist) {
            //result为即将输出的结果
            HashMap<String,HashMap<String,Object>> result =new HashMap<>();

            List<String> ids =new ArrayList<>();//记录id数量

            //初始化result表
            for (String key : allColumns) {
                if(key.contains("trait")){
                    if(key.contains("id")){
                        ids.add(key);
                        HashMap<String, Object> hm = new HashMap<>();//用于记录即将出现的traitId、traitValue同时不会出现空值
                        hm.put("traitId",null);
                        hm.put("traitValue", null);
                        result.put(key,hm);
                    }
                }
            }


            //字段集合
            String tempId = new String();

            //在这里开始存入数据
            for (String key : maps.keySet()) {
                if (key.contains("trait")) {
                    if (key.contains("id")) {
                        Object o = maps.get(key);
                        if (o != null) {
                            String s = o.toString();//value cloume的值
                            HashMap hashMaps = result.get(key);
                            hashMaps.put("traitId", s);
                            result.put(tempId, hashMaps);
                        } else {
                            HashMap hashMaps = result.get(key);
                            hashMaps.put("traitId", null);
                            result.put(tempId, hashMaps);
                        }
                    }

                    if (key.contains("value")) {
                        String i = key.substring("trait_value_".length());
                        String thisid = "trait_id_" + i;//拼接出key值
                        Object o = maps.get(key);
                        String s = o.toString();//value cloume的值
                        HashMap hashMaps = result.get(thisid);
                        hashMaps.put("traitValue",s);
                        result.put(tempId, hashMaps);
                    }
                }

            }
            //通过id到形状表里查
            HashMap<Long, Trait> traitsMap = new HashMap<>();
            for (String id : ids) {
                HashMap<String, Object> map = result.get(id);
                String id2 = (String) map.get("traitId");
                if (id2 != null) {
                    Long id1 = Long.valueOf(id2);
                    Trait trait = traitsMap.get(id1);
                    if(ObjectUtils.isEmpty(trait)) {
                        trait = traitsObjectMapReverse.get(id1);
                        traitsMap.put(id1, trait);
                    }
                    if(trait==null) continue;
                    String traitName1 = trait.getTraitName();
                    map.put("traitName", traitName1);
                    String fullName = trait.getFullName();
                    map.put("fullName", fullName);

                    String abbreviationName = trait.getAbbreviationName();
                    if(abbreviationName!=null)
                        map.put("abbreviationName", abbreviationName.toString());
                    else
                        map.put("abbreviationName", null);
                    String remark = trait.getRemark();
                    map.put("remark", remark);
                    result.put(id, map);
                } else {
                    System.out.println("没有查到");
                    String traitName1 = null;
                    map.put("traitName", traitName1);

                    String fullName = null;
                    map.put("fullName", fullName);

                    map.put("abbreviationName", null);

                    String remark = null;
                    map.put("remark", remark);
                    result.put(id, map);
                }
            }
            result.remove("");//删除空值


            List<Map.Entry<String, Integer>> entryList = new ArrayList(result.entrySet());

            // 使用Collections.sort对List进行排序
            Collections.sort(entryList, (entry1, entry2) -> {
                String key1 = entry1.getKey();
                String key2 = entry2.getKey();

                // 提取最后一个下划线后面的字符
                String number1 = key1.substring(key1.lastIndexOf("_") + 1);
                String number2 = key2.substring(key2.lastIndexOf("_") + 1);
                // 转换为数字进行比较
                int value1;
                int value2;

                if(number1.equals("")||StringUtils.equals(number1,"")||StringUtils.equals(number1,"phenotypeId")) value1=0;
                else value1= Integer.parseInt(number1);
                if(number2.equals("")||StringUtils.equals(number2,"")||StringUtils.equals(number2,"phenotypeId")) value2=0;
                else value2 = Integer.parseInt(number2);


                // 按照数字大小进行排序
                return Integer.compare(value1, value2);
            });

            // 遍历排序后的List
//            for (Map.Entry<String, Integer> entry : entryList) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
            Object phenotypeId = maps.get("phenotype_id");
            Object material_id = maps.get("material_id");
            Object remark = maps.get("remark");
//            PhenotypeFile phenotypeFile1 =new PhenotypeFile();
//            PhenotypeDetailVO convert = OrikaUtils.convert(phenotypeFile1, PhenotypeDetailVO.class);
//            System.out.println("convert:"+convert);
            PhenotypeDetailVO phenotypeDetailVO  =new PhenotypeDetailVO();
            phenotypeDetailVO.setFileId(phenotypeFile.getFileId());
            phenotypeDetailVO.setFileName(phenotypeFile.getFileName());
            phenotypeDetailVO.setUrl(phenotypeFile.getUrl());
            phenotypeDetailVO.setStatus( phenotypeFile.getStatus());
            phenotypeDetailVO.setTreeId(phenotypeFile.getTreeId());
            if (phenotypeId != null) {
                phenotypeDetailVO.setPhenotypeId((long) phenotypeId);
            }

            phenotypeDetailVO.setMaterialId(material_id != null ? material_id.toString() : null);

            phenotypeDetailVO.setTableName(tableName);
            phenotypeDetailVO.setRemark(remark != null ? remark.toString() : null);
            ArrayList<LinkedHashMap<String, HashMap<String,String>>> traitMap = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : entryList) {
                LinkedHashMap<String, HashMap<String,String>> map = new LinkedHashMap<>();
                map.put(entry.getKey(), (HashMap<String,String>)(Object)entry.getValue());
                traitMap.add(map);
            }

            phenotypeDetailVO.setTraits(traitMap);

            res.add(phenotypeDetailVO);
        }



        return res;
    }

    /*@Override
    public List<Trait> selectTraitByLocation(String location){
        List<PhenotypeFile> phenotypeFiles = phenotypeFileMapper.selectLatestFileByLocation(location);
        Set<Long> traitIdset =new HashSet<>();

        for (int i = 0; i < phenotypeFiles.size(); i++) {
            PhenotypeFile phenotypeFile = phenotypeFiles.get(i);
            String tableName = phenotypeFile.getTableName();
            List<Map<String, Object>> maps = phenotypeFileMapper.selectAllColumns(tableName);
            if(maps!=null&&maps.size()!=0) {
                Map<String, Object> map = maps.get(0);
                if(map==null) continue;
                for (Map.Entry<String, Object> col : map.entrySet()) {
                    String key = col.getKey();
                    if (key.contains("trait_id"))
                        traitIdset.add(Long.valueOf(col.getValue().toString()));
                }
            }

        }
        List<Trait> list = new ArrayList<>();
        for(Long id:traitIdset){
            Trait trait = traitMapper.selectTraitByTraitIdWithoutDeleted(id);
            if(trait!=null)
                list.add(trait);
        }
        return list;
    }*/


    /**
     * 在表型文件表里根据FileId查TableName
     *
     * @param fileId
     * @return tableName
     */
    public String selectTableNameByFileId(String fileId) {
        return phenotypeFileMapper.selectTableNameByFileId(fileId);
    }

    /**
     * 是否存在该表
     */
    public Integer ifHaveTable(String tableName) {
        return phenotypeFileMapper.ifHaveTable(tableName);
    }

    /**
     * 在表型表里查材料基本信息
     *
     * @param m Material对象
     * @return Material的list
     */
    public List<Material> selectMaterialByTableName(Material m) {
        return phenotypeFileMapper.selectMaterialByTableName(m);
    }

    /**
     * 在表型文件表里根据FileId查FileName
     *
     * @param fileId
     * @return fileName
     */
    public String selectFileNameByFileId(String fileId) {
        return phenotypeFileMapper.selectFileNameByFileId(fileId);
    }

    /**
     * 更新表型表
     */

    @CacheEvict(value = "selectDetailByFileId",key = "#fileId")
    @Override
    public void updatePhenoTypeFile(Long fileId, Long phenotypeId,HashMap<String, String> map) {
        PhenotypeFile phenotypeFile = phenotypeFileMapper.selectPhenotypeFileByFileId(fileId);
        if (!getUserId().toString().equals(phenotypeFile.getCreateBy())) throw new ServiceException("只能修改自己上传的文件");
        String tableName = phenotypeFile.getTableName();
        System.out.println("tableName::"+tableName);
        System.out.println("phenotypeId::"+phenotypeId);
        for(Map.Entry<String,String> m:map.entrySet()) {
            String key = m.getKey();
            key = "`" + key + "`";
            phenotypeFileMapper.updatePhenotypeFileColum(tableName,phenotypeId, m.getValue(),key);
        }
        String time= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        phenotypeFileMapper.updatePhenotypeFileColum(tableName,phenotypeId,time,"update_time");
        phenotypeFileMapper.updatePhenotypeFileColum(tableName,phenotypeId,getUserId().toString(),"update_by");
    }

    @Async
    @Override
    public void waitUpdate(String tableName) throws IOException {
        Object o = redisTemplate.opsForValue().get("exportPhenoTypeFile:" + tableName);
        while(!ObjectUtils.isEmpty(o)){
            return;
        }
        redisTemplate.opsForValue().setIfAbsent("exportPhenoTypeFile:" + tableName,1,60, TimeUnit.MINUTES);
        exportData(tableName);
    }

    @Override
    public String exportFile(String tableName) {
        String url = phenotypeFileMapper.selectExportFileUrlByTableName("'" + tableName + "'");
        PhenotypeFile phenotypeFile = phenotypeFileMapper.selectLatestPhenotypeFileByTableName(tableName);
        if(ObjectUtils.isEmpty(phenotypeFile))
            throw new ServiceException("该表不存在");
        List<Long> collect = getLoginUser().getUser().getRoles().stream().mapToLong(SysRole::getRoleId).boxed().collect(Collectors.toList());
        if(!collect.contains(1L) && !collect.contains(5L)){
            if(!phenotypeFile.getCreateBy().equals(getUserId().toString()))
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
        while (!ObjectUtils.isEmpty(redisTemplate.opsForValue().get("exportPhenoTypeFileExecution:" + tableName))){
            sleep(10000);
        }
        //任务队列中取出
        redisTemplate.delete("exportPhenoTypeFile:" + tableName);
        //放入执行队列
        redisTemplate.opsForValue().setIfAbsent("exportPhenoTypeFileExecution:" + tableName,1,60, TimeUnit.MINUTES);

        try{
            String url = phenotypeFileMapper.selectExportFileUrlByTableName("'" + tableName + "'");
            if(!StringUtils.isEmpty(url) && Files.exists(Paths.get(url))){
                Files.delete(Paths.get(url));
            }
            phenotypeFileMapper.deleteExportFileByTableName(tableName);
            System.out.println("开始导出");
            PhenotypeFile phenotypeFile = phenotypeFileMapper.selectLatestPhenotypeFileByTableName(tableName);
            if(ObjectUtils.isEmpty(phenotypeFile))
                throw new ServiceException("该表不存在");
            //拿数据, 转map
            List<PhenotypeDetailVO> data = selectDetailByFileId(phenotypeFile.getFileId(),false);
            if(ObjectUtils.isEmpty(data))
                throw new ServiceException("该表无数据");
            //生成列名
            List<String> columns = phenotypeFileMapper.getAllColumns(tableName);
            columns.removeIf(s -> s.contains("trait_"));
            columns.remove("phenotype_id");
            columns.remove("create_by");
            columns.remove("create_time");
            columns.remove("update_by");
            columns.remove("update_time");
            for (LinkedHashMap<String, HashMap<String,String>> trait : data.get(0).getTraits()) {
                Set<Map.Entry<String, HashMap<String, String>>> set = trait.entrySet();
                for (Map.Entry<String, HashMap<String, String>> entry : set) {
                    String traitName = entry.getValue().get("traitName");
                    columns.add(traitName);
                }
            }
            //改表头名字
            columns.set(columns.indexOf("material_id"),"材料名称");
            columns.set(columns.indexOf("remark"),"备注");
            //生成文件
            String path = fileUtil.getFileUrl(phenotypeFile.getFileName() + ".csv",phenotypeFile.getTreeId());
            new File(path).createNewFile();
            CsvWriter csvWriter = new CsvWriter(path, ',', Charset.forName("GBK"));
            //关闭字段校验
            csvWriter.setUseTextQualifier(false);
            csvWriter.writeRecord(columns.toArray(new String[0]));
            //写入数据
            for (PhenotypeDetailVO phenotypeDetailVO : data) {
                ArrayList<String> info = new ArrayList<>();
                info.add(phenotypeDetailVO.getMaterialId() == null ? null : phenotypeDetailVO.getMaterialId());
                info.add(phenotypeDetailVO.getRemark() == null ? null : phenotypeDetailVO.getRemark());
                for (LinkedHashMap<String, HashMap<String,String>> trait : phenotypeDetailVO.getTraits()) {
                    Set<Map.Entry<String, HashMap<String, String>>> set = trait.entrySet();
                    for (Map.Entry<String, HashMap<String, String>> entry : set) {
                        String value = entry.getValue().get("traitValue");
                        info.add(value);
                    }
                }
                csvWriter.writeRecord(info.toArray(new String[0]));
            }
            csvWriter.close();
            PhenotypeFile upd = new PhenotypeFile();
            upd.setTableName(tableName);
            upd.setTreeId(-1L);
            upd.setUrl(path);
            phenotypeFileMapper.insertPhenotypeFile(upd);
        }catch (Exception e) {
            throw e;
        }finally {
            //执行队列中取出
            redisTemplate.delete("exportPhenoTypeFileExecution:" + tableName);
        }
    }



    public PhenotypeFile havePermission(String tableName) {
        Long userId = getUserId();
        PhenotypeFile phenotypeFile = phenotypeFileMapper.selectLatestPhenotypeFileByTableName(tableName);
        if (!userId.toString().equals(phenotypeFile.getCreateBy()) && userId != 1) throw new ServiceException("无权限,只有文件的创建用户和管理员可以操作");
        else return phenotypeFile;
    }

    public String getTableNameByFileId(String fileId) {
        return phenotypeFileMapper.getTableNameByFileId(fileId);
    }



    //获取所有的性状名
    @Override
    public List<Trait> getAllTraitFromFile() {
//        //从缓存中快查
//        List<Trait> alltraits = new ArrayList<>();
//        alltraits = redisCache.getCacheList("alltraits");
//        if (alltraits != null && alltraits.size() != 0)
//            return alltraits;
//
//        System.out.println("111");
//
//        //1. 查询出所有含有trait_id的字段，再从数据库中查询
//        HashSet<Long> traitIdSet = new HashSet<>();
//
//
//        List<PhenotypeFile> phenotypeFiles = phenotypeFileMapper.selectPhenotypeFileList(null);//获取所有的File
//        for(PhenotypeFile phenotypeFile:phenotypeFiles){
//            String tableName = phenotypeFile.getTableName();
//            if(StringUtils.isNull(tableName)||StringUtils.isEmpty(tableName)){
//                continue;
//            }
//
//            List<Map<String, Object>> mapslist = phenotypeFileMapper.selectAllColumns(tableName);
//
//            Map<String, Object> maps = mapslist.get(0);//有一个第一个就够了
//            //2. 找到所有的id值，把值反到set里
//
//            for(Map.Entry<String,Object> map:maps.entrySet()){
//                if(!map.getKey().contains("trait_id"))continue;
//                traitIdSet.add((Long) map.getValue());
//            }
//        }
//
//
//        //3. 从mysql中查找trait塞到list
//        for(Long id:traitIdSet){
//            Trait trait = traitMapper.selectTraitByTraitId(id);
//            alltraits.add(trait);
//        }
//        //4. 把list塞到redis，并返回
//        redisCache.setCacheList("alltraits",alltraits);
        List<Trait> traits = traitMapper.selectTraitListWithoutDeleted(null);
        return traits;

    }

    @Override
    public long selectTableLineCountByFileId(Long fileId) {
        return phenotypeFileMapper.selectTableCount(phenotypeFileMapper.getTableNameByFileId(String.valueOf(fileId)));
    }
}

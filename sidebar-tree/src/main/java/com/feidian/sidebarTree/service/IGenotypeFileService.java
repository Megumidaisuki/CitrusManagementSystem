package com.feidian.sidebarTree.service;

import java.io.IOException;
import java.util.*;

import com.feidian.common.core.page.TableDataInfo;
import com.feidian.sidebarTree.domain.GenotypeFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 基因型文件Service接口
 *
 * @author feidian
 * @date 2023-07-06
 */
public interface IGenotypeFileService
{
    /**
     * 查询基因型文件
     *
     * @param fileId 基因型文件主键
     * @return 基因型文件
     */
    public GenotypeFile selectGenotypeFileByFileId(Long fileId);

    /**
     * 查询基因型文件列表
     *
     * @param genotypeFile 基因型文件
     * @return 基因型文件集合
     */
    public List<GenotypeFile> selectGenotypeFileList(GenotypeFile genotypeFile);

    Long selectGenotypeFileListCount(GenotypeFile genotypeFile);


    /**
     * 修改基因型文件
     *
     * @param genotypeFile 基因型文件
     * @return 结果
     */
    public int updateGenotypeFile(GenotypeFile genotypeFile);

    /**
     * 批量删除基因型文件
     *
     * @param fileIds 需要删除的基因型文件主键集合
     * @return 结果
     */
    public int deleteGenotypeFileByFileIds(Long[] fileIds);

    /**
     * 删除基因型文件信息
     *
     * @param fileId 基因型文件主键
     * @return 结果
     */
    public int deleteGenotypeFileByFileId(Long fileId);

    String uploadFile(Long treeId, MultipartFile file, int fileStatus, String remark, String fileName) throws IOException;

    boolean mergeFile(MultipartFile file, String tableName, String remark, String fileName) throws IOException;

    String updateGenoTypeFile(Long fileId, Long genotypeId, HashMap<String, String> map);

    void waitUpdate(String tableName) throws IOException;

    List<LinkedHashMap<String,String>> selectDetailByFileId(Long fileId,boolean startPage);

    String exportFile(String tableName);

    void exportData(String tableName) throws IOException;

    List<LinkedHashMap<String, String>> flattenResult(List<LinkedHashMap<String, Object>> result);

    void flattenNestedMap(Map<String, Object> original, Map<String, String> flattened, String prefix);

    String selectTableNameByFileId(Long fileId);

    void excutePCA(String tableName);

    long selectGenotypeDataCountByfileId(Long fileId);
    /**
     * 在基因型文件表里根据FileId查FileName
     *
     * @param fileId
     * @return fileName
     */
    public String selectFileNameByFileId(String fileId);

    long selectGenotypeFileListCountByTableName(String tableName);

    List<String> selectTableColumnByFileId(Long fileId);

    TableDataInfo selectHistoryDetailByFileId(Long fileId, int pageNum, int pageSize) throws IOException;

    String getImgUrl(Long fileId);

    List<Long[]> getChromDensity(String tableName);

    List<String > getMaterialListByTableName(String tableName);

    List<Map> getChromRatioAndMaxPos(String tableName);

    List<LinkedHashMap> getMaterialInfo(String tableName, String materialName, String chrom, long start, long end);

    List<List<Integer>> getHeatMap(String tableName, String[] materialName, String chrom, long start, long end);
}
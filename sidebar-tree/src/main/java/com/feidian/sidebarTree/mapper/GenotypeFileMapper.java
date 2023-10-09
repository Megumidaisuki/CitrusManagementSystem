package com.feidian.sidebarTree.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.feidian.sidebarTree.domain.GenotypeFile;
import jnr.ffi.annotations.In;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 基因型文件Mapper接口
 *
 * @author feidian
 * @date 2023-07-06
 */
@Mapper
public interface GenotypeFileMapper
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

    /**
     * 新增基因型文件
     *
     * @param genotypeFile 基因型文件
     * @return 结果
     */
    public int insertGenotypeFile(GenotypeFile genotypeFile);

    /**
     * 修改基因型文件
     *
     * @param genotypeFile 基因型文件
     * @return 结果
     */
    public int updateGenotypeFile(GenotypeFile genotypeFile);

    /**
     * 删除基因型文件
     *
     * @param fileId 基因型文件主键
     * @return 结果
     */
    public int deleteGenotypeFileByFileId(Long fileId);

    /**
     * 批量删除基因型文件
     *
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGenotypeFileByFileIds(Long[] fileIds);

    public int deleteGenotypeFileByTableName(@Param("tableName") String tableName);

    GenotypeFile selectLatestGenotypeFileByTableName(@Param("tableName") String tableName);

    Long selectMaxGenotypeIdByTableName(@Param("tableName") String tableName);

    List<String> selectAllSortedColumnByTableName(@Param("tableName") String tableName);

    int selectTableColumnCountByTableName(@Param("tableName") String tableName);

    Long selectTableLineCountByTableName(@Param("tableName") String tableName);

    @MapKey("genotype_id")

    List<LinkedHashMap<String,Object>> selectDetailByTableName(@Param("tableName") String tableName);

    String selectExportFileUrlByTableName(@Param("tableName") String tableName);

    void deleteExportFileByTableName(@Param("tableName") String tableName);

    String[] selectGenotypeFileStringList(GenotypeFile genotypeFile);

    /**
     * 在基因型文件表里根据FileId查FileName
     *
     * @param fileId
     * @return fileName
     */
    public String selectFileNameByFileId(String fileId);

    long selectGenotypeFileListCountByTableName(@Param("tableName") String tableName);

    List<Map> selectChromDensityByTableName(@Param("tableName")String tableName, @Param("intervalSize")int intervalSize);

    List<Map> selectChromRatioByTableName(@Param("tableName")String tableName);

    List<LinkedHashMap> selectBasicInfoByTableName(@Param("tableName")String tableName, @Param("chrom") String chrom, @Param("start") long start, @Param("end") long end);

    @MapKey("genotype_id")
    Map<String,Map> selectMaterialInfo(@Param("tableName")String tableName,@Param("materialName") String materialName);

    @MapKey("genotype_id")
    Map<Long,Map> selectHeatMapInfoByTableName(@Param("tableName")String tableName, @Param("columParam")String columParam);
}

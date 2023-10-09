package com.feidian.sidebarTree.mapper;

import java.util.List;
import java.util.Map;

import com.feidian.sidebarTree.domain.Material;
import com.feidian.sidebarTree.domain.Phenotype;
import com.feidian.sidebarTree.domain.PhenotypeFile;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 表型文件Mapper接口
 *
 * @author feidian
 * @date 2023-07-02
 */
@Mapper
public interface PhenotypeFileMapper
{
    /**
     * 查询表型文件
     *
     * @param fileId 表型文件主键
     * @return 表型文件
     */
    public PhenotypeFile selectPhenotypeFileByFileId(Long fileId);

    /**
     * 查询表型文件列表
     *
     * @param phenotypeFile 表型文件
     * @return 表型文件集合
     */
    public List<PhenotypeFile> selectPhenotypeFileList(PhenotypeFile phenotypeFile);

    public List<PhenotypeFile> selectAll();

    /**
     * 新增表型文件
     *
     * @param phenotypeFile 表型文件
     * @return 结果
     */
    public int insertPhenotypeFile(PhenotypeFile phenotypeFile);

    /**
     * 修改表型文件
     *
     * @param phenotypeFile 表型文件
     * @return 结果
     */
    public int updatePhenotypeFile(PhenotypeFile phenotypeFile);

    /**
     * 删除表型文件
     *
     * @param fileId 表型文件主键
     * @return 结果
     */
    public int deletePhenotypeFileByFileId(Long fileId);

    /**
     * 批量删除表型文件
     *
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePhenotypeFileByFileIds(Long[] fileIds);

    public int deletePhenotypeFileByTableName(@Param("tableName") String tableName);



    List<Long> selectPopulationBySpecies(Long speciesId);

    List<String> selectPhenotypeListYears(@Param("speciesId") Long speciesId,@Param("populationId") Long populationId);

    List<String> selectPhenotypeListLocations(@Param("speciesId")Long speciesId,@Param("populationId") Long populationId,@Param("year") String year);

    List<String> selectTableNameByFourElement(@Param("speciesId") Long speciesId, @Param("populationId") Long populationId, @Param("year") String year, @Param("location") String location);

    //获取所有字段
    List<String> getAllColumns(String tableName);

    //获取所有值
    List<Map<String, Object>> selectAllColumns(@Param("tableName") String tableName);


    /**
     * 在表型文件表里根据FileId查TableName
     *
     * @param fileId
     * @return tableName
     */
    public String selectTableNameByFileId(String fileId);

    /**
     * 是否存在该表
     */
    public Integer ifHaveTable(String tableName);

    /**
     * 在表型表里查材料基本信息
     *
     * @param m Material对象
     * @return Material的list
     */
    public List<Material> selectMaterialByTableName(Material m);

    /**
     * 在表型文件表里根据FileId查FileName
     *
     * @param fileId
     * @return fileName
     */
    public String selectFileNameByFileId(String fileId);

    @Select("SELECT location, COUNT(DISTINCT table_name) as count FROM phenotype_file where location is not null GROUP BY location\n")
    List<Map<String, Object>> getAreaName();

    PhenotypeFile selectLatestPhenotypeFileByTableName(@Param("tableName") String tableName);

    List<Phenotype> selectPhenotypeByTableName(@Param("tableName") String tableName);

    List<String> listAllPhenotypeTableNames();

    Long slectPhenotypeLineByTableName(@Param("tableName") String tableName);

    List<String> getAllTraitIdColumnsWithSorted(@Param("tableName") String tableName);


    Map<String, Object> selectTraitBindingByTableName(@Param("columns") String columns, @Param("tableName") String tableName);

    Integer selectTableIsInPhenotypeFile(String tableName);

    List<String> getAllTraitIdAndValueColumnsWithSorted(@Param("tableName") String tableName);

    List<String> getAllSortedColumns(@Param("tableName") String tableName);

    List<PhenotypeFile> selectLatestFileByLocation(String location);

    void updatePhenotypeFileColum(@Param("tableName") String tableName,
                                  @Param("phenotypeId") Long phenotypeId,
                                  @Param("value") String value,
                                  @Param("key")String key);

    String selectExportFileUrlByTableName(@Param("tableName") String tableName);

    void deleteExportFileByTableName(@Param("tableName") String tableName);

    String getTableNameByFileId(@Param("fileId") String fileId);

    List<String> selectPhenotypeFileListCountList(PhenotypeFile phenotypeFile);

    @MapKey("id")
    List<Map<String, Object>> selectAllColumnsByPage(@Param("tableName")String tableName,@Param("pageSize") Integer pageSize,@Param("pageNum") Integer pageNum);

    int selectTableCount(String tableName);

    long selectPhenotypeFileListCountByTableName(@Param("tableName") String tableName);
}

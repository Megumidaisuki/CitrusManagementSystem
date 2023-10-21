package com.feidian.sidebarTree.service;

import com.feidian.common.exception.ServiceException;
import com.feidian.sidebarTree.domain.Material;
import com.feidian.sidebarTree.domain.PhenotypeFile;
import com.feidian.sidebarTree.domain.Trait;
import org.springframework.stereotype.Service;
import com.feidian.sidebarTree.domain.vo.PhenotypeDetailVO;
import com.feidian.sidebarTree.domain.vo.PhenotypeFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 表型文件Service接口
 *
 * @author feidian
 * @date 2023-07-02
 */
public interface IPhenotypeFileService {
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

    Long selectPhenotypeFileListCount(PhenotypeFile phenotypeFile);

    List<PhenotypeFileVO> selectPhenotypeFileVOList(PhenotypeFile phenotypeFile);

    /**
     * 修改表型文件
     *
     * @param phenotypeFile 表型文件
     * @return 结果
     */
    public int updatePhenotypeFile(PhenotypeFile phenotypeFile);

    /**
     * 批量删除表型文件
     *
     * @param fileIds 需要删除的表型文件主键集合
     * @return 结果
     */
    public int deletePhenotypeFileByFileIds(Long[] fileIds);

    /**
     * 删除表型文件信息
     *
     * @param fileId 表型文件主键
     * @return 结果
     */
    public int deletePhenotypeFileByFileId(Long fileId);

    String uploadFile(Long treeId, MultipartFile file, int fileStatus, String remark, String fileName,int pointStatus) throws ServiceException, IOException;

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

    public List<List<Map.Entry<String, Integer>>> selectTraitByFileId(Long fileId,int pageSize,int pageNum);

    /*List getAreaData();*/

    boolean mergeFile(MultipartFile file, String tableName, String remark, String fileName) throws IOException;

    List<Trait> selectTraitColByFileId(Long fileId);

    Set<String> getMaterialIdByFileId(Long fileId);

    List<PhenotypeDetailVO> selectDetailByFileId(Long fileId, boolean startPage);

    /*//根据地区获取所有性状
    List<Trait> selectTraitByLocation(String location);*/

    void updatePhenoTypeFile(Long fileId,Long phenotypeId, HashMap<String, String> map);

    void waitUpdate(String tableName) throws IOException;

    String exportFile(String tableName);

    void exportData(String tableName) throws IOException;

     int selectTableCount(Long tableName);

    List<Trait> getAllTraitFromFile();

    long selectTableLineCountByFileId(Long fileId);

    long selectPhenotypeFileListCountByTableName(String tableName);
}

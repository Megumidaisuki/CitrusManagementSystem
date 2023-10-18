package com.feidian.sidebarTree.domain.vo;

import com.feidian.common.annotation.Excel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*完整的表型信息*/
public class PhenotypeDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    private String fileName;

    /**
     * 数据表名
     */
    @Excel(name = "数据表名")
    private String tableName;

    /**
     * 树节点id
     */
    @Excel(name = "树节点id")
    private Long treeId;

    /**
     * 表型数据ID
     */
    @Excel(name = "表型数据ID")
    private Long phenotypeId;


    /**
     * 是否公开
     */
    @Excel(name = "是否公开")
    private int status;


    /**
     * 数据路径url
     */
    @Excel(name = "数据路径url")
    private String url;

    private String remark;

    /**
     * 材料编号
     */
    @Excel(name = "材料编号")
    private String materialId;


    /*有顺序的性状排列表*/
    List<LinkedHashMap<String, HashMap<String, String>>> traits;

    public PhenotypeDetailVO() {
    }

    public PhenotypeDetailVO(Long fileId, String fileName, String tableName, Long treeId, Long phenotypeId, int status, String url, String remark, String materialId, List<LinkedHashMap<String, HashMap<String, String>>> traits) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.tableName = tableName;
        this.treeId = treeId;
        this.phenotypeId = phenotypeId;
        this.status = status;
        this.url = url;
        this.remark = remark;
        this.materialId = materialId;
        this.traits = traits;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * 获取
     *
     * @return fileId
     */
    public Long getFileId() {
        return fileId;
    }

    /**
     * 设置
     *
     * @param fileId
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * 获取
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取
     *
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取
     *
     * @return treeId
     */
    public Long getTreeId() {
        return treeId;
    }

    /**
     * 设置
     *
     * @param treeId
     */
    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    /**
     * 获取
     *
     * @return phenotypeId
     */
    public Long getPhenotypeId() {
        return phenotypeId;
    }

    /**
     * 设置
     *
     * @param phenotypeId
     */
    public void setPhenotypeId(Long phenotypeId) {
        this.phenotypeId = phenotypeId;
    }

    /**
     * 获取
     *
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取
     *
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取
     *
     * @return materialId
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * 设置
     *
     * @param materialId
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * 获取
     *
     * @return traits
     */
    public List<LinkedHashMap<String, HashMap<String, String>>> getTraits() {
        return traits;
    }

    /**
     * 设置
     *
     * @param traits
     */
    public void setTraits(List<LinkedHashMap<String, HashMap<String, String>>> traits) {
        this.traits = traits;
    }

    @Override
    public String toString() {
        return "PhenotypeDetailVO{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", treeId=" + treeId +
                ", status=" + status +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", materialId='" + materialId + '\'' +
                ", traits=" + traits +
                '}';
    }
}

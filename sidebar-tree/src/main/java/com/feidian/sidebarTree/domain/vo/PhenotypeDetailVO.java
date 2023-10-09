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

    /** 文件ID */
    private Long fileId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 数据表名 */
    @Excel(name = "数据表名")
    private String tableName;

    /** 树节点id */
    @Excel(name = "树节点id")
    private Long treeId;

    /** 表型数据ID */
    @Excel(name = "表型数据ID")
    private Long phenotypeId;

    /** 物种ID */
    @Excel(name = "物种ID")
    private Long speciesId;

    /** 物种名称 */
    @Excel(name = "物种名称")
    private String speciesName;

    /** 群体ID */
    @Excel(name = "群体ID")
    private Long populationId;

    /** 群体名称 */
    @Excel(name = "群体名称")
    private String populationName;

    /** 是否公开 */
    @Excel(name = "是否公开")
    private int status;

    /** 数据采集年份 */
    @Excel(name = "数据采集年份")
    private String year;

    /** 数据采集地区 */
    @Excel(name = "数据采集地区")
    private String location;

    /** 数据路径url */
    @Excel(name = "数据路径url")
    private String url;

    private String remark;

    /** 重复实验 */
    @Excel(name = "重复实验")
    private Long repeat;

    /** 品种ID */
    @Excel(name = "品种ID")
    private String kindId;

    /** 品种名称 */
    @Excel(name = "品种名称")
    private String kindName;

    /** 材料编号 */
    @Excel(name = "材料编号")
    private String materialId;

    /** 田间编号 */
    @Excel(name = "田间编号")
    private Long fieldId;

    /** 对照类型 */
    @Excel(name = "对照类型")
    private String controlType;

    /** 父本 */
    @Excel(name = "父本")
    private String father;

    /** 母本 */
    @Excel(name = "母本")
    private String mother;

    /*有顺序的性状排列表*/
    List<LinkedHashMap<String, HashMap<String,String>>> traits;

    @Override
    public String toString() {
        return "PhenotypeDetailVO{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", treeId=" + treeId +
                ", speciesId=" + speciesId +
                ", speciesName='" + speciesName + '\'' +
                ", populationId=" + populationId +
                ", populationName='" + populationName + '\'' +
                ", status=" + status +
                ", year='" + year + '\'' +
                ", location='" + location + '\'' +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", repeat=" + repeat +
                ", kindId='" + kindId + '\'' +
                ", kindName='" + kindName + '\'' +
                ", materialId='" + materialId + '\'' +
                ", fieldId=" + fieldId +
                ", controlType='" + controlType + '\'' +
                ", father='" + father + '\'' +
                ", mother='" + mother + '\'' +
                ", traits=" + traits +
                '}';
    }

    public PhenotypeDetailVO() {
    }

    public Long getPhenotypeId() {
        return phenotypeId;
    }

    public void setPhenotypeId(Long phenotypeId) {
        this.phenotypeId = phenotypeId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRepeat() {
        return repeat;
    }

    public void setRepeat(Long repeat) {
        this.repeat = repeat;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public List<LinkedHashMap<String, HashMap<String,String>>> getTraits() {
        return traits;
    }

    public void setTraits(List<LinkedHashMap<String, HashMap<String,String>>> traits) {
        this.traits = traits;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getPopulationName() {
        return populationName;
    }

    public void setPopulationName(String populationName) {
        this.populationName = populationName;
    }
}

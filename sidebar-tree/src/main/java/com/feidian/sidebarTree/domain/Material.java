package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;

/**
 * 材料对象 material
 *
 * @author feidian
 * @date 2023-07-02
 */
public class Material
{
    private static final long serialVersionUID = 1L;

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

    private String tableName;

    private String fileId;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "Material{" +
                "kindId='" + kindId + '\'' +
                ", kindName='" + kindName + '\'' +
                ", materialId='" + materialId + '\'' +
                ", fieldId=" + fieldId +
                ", controlType='" + controlType + '\'' +
                ", father='" + father + '\'' +
                ", mother='" + mother + '\'' +
                ", tableName='" + tableName + '\'' +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}

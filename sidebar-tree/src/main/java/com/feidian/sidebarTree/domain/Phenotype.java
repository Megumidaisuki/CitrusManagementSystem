package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 型对象 phenotypeVO
 * 
 * @author feidian
 * @date 2023-07-01
 */
public class Phenotype extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    private Long phenotypeId;

    /** 材料编号 */
    @Excel(name = "材料编号")
    private String materialId;

    /** 性状ID */
    @Excel(name = "性状ID")
    private Long traitId;

    /** 性状值 */
    @Excel(name = "性状值")
    private Long traitValue;

    public Phenotype() {
    }

    public Phenotype(Long phenotypeId, String materialId, Long traitId, Long traitValue) {
        this.phenotypeId = phenotypeId;
        this.materialId = materialId;
        this.traitId = traitId;
        this.traitValue = traitValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("phenotypeId", getPhenotypeId())
            .append("traitId", getTraitId())
            .append("traitValue", getTraitValue())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    /**
     * 获取
     * @return phenotypeId
     */
    public Long getPhenotypeId() {
        return phenotypeId;
    }

    /**
     * 设置
     * @param phenotypeId
     */
    public void setPhenotypeId(Long phenotypeId) {
        this.phenotypeId = phenotypeId;
    }

    /**
     * 获取
     * @return materialId
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * 设置
     * @param materialId
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * 获取
     * @return traitId
     */
    public Long getTraitId() {
        return traitId;
    }

    /**
     * 设置
     * @param traitId
     */
    public void setTraitId(Long traitId) {
        this.traitId = traitId;
    }

    /**
     * 获取
     * @return traitValue
     */
    public Long getTraitValue() {
        return traitValue;
    }

    /**
     * 设置
     * @param traitValue
     */
    public void setTraitValue(Long traitValue) {
        this.traitValue = traitValue;
    }
}

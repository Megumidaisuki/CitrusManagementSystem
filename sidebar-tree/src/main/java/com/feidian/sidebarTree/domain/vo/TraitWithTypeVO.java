package com.feidian.sidebarTree.domain.vo;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.swing.*;

public class TraitWithTypeVO extends BaseEntity {

    private Long traitTypeId;

    private String traitTypeName;

    /**
     * 自增主键
     */
    private Long traitId;

    /**
     * 性状名称
     */
    @Excel(name = "性状名称", sort = 1)
    private String traitName;

    /**
     * 全称
     */
    @Excel(name = "全称", sort = 2)
    private String fullName;

    /**
     * 缩写
     */
    @Excel(name = "缩写", sort = 3)
    private String abbreviationName;

    /**
     * 是否被删除
     */
    private Integer isdeleted;

    public TraitWithTypeVO() {
    }

    public TraitWithTypeVO(Long traitTypeId, String traitTypeName, Long traitId, String traitName, String fullName, String abbreviationName, Integer isdeleted) {
        this.traitTypeId = traitTypeId;
        this.traitTypeName = traitTypeName;
        this.traitId = traitId;
        this.traitName = traitName;
        this.fullName = fullName;
        this.abbreviationName = abbreviationName;
        this.isdeleted = isdeleted;
    }

    public void setTraitId(Long traitId) {
        this.traitId = traitId;
    }

    public Long getTraitId() {
        return traitId;
    }

    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    public String getTraitName() {
        return traitName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setAbbreviationName(String abbreviationName) {
        this.abbreviationName = abbreviationName;
    }

    public String getAbbreviationName() {
        return abbreviationName;
    }

    public Integer getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("traitId", getTraitId())
                .append("traitName", getTraitName())
                .append("fullName", getFullName())
                .append("abbreviationName", getAbbreviationName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("isdeleted",getIsdeleted())
                .toString();
    }

    /**
     * 获取
     * @return traitTypeId
     */
    public Long getTraitTypeId() {
        return traitTypeId;
    }

    /**
     * 设置
     * @param traitTypeId
     */
    public void setTraitTypeId(Long traitTypeId) {
        this.traitTypeId = traitTypeId;
    }

    /**
     * 获取
     * @return traitTypeName
     */
    public String getTraitTypeName() {
        return traitTypeName;
    }

    /**
     * 设置
     * @param traitTypeName
     */
    public void setTraitTypeName(String traitTypeName) {
        this.traitTypeName = traitTypeName;
    }
}

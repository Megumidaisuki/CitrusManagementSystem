package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * trait
 *
 * @author feidian
 * @date 2023-07-03
 */
public class Trait extends BaseEntity {
    private static final long serialVersionUID = 1L;

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
}

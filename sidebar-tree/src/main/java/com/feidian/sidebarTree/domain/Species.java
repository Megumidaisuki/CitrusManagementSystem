package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * species对象 species
 *
 * @author feidian
 * @date 2023-07-01
 */
public class Species extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 物种ID
     */
    private Long speciesId;

    /**
     * 物种名称
     */
    @Excel(name = "物种名称", sort = 1)
    private String speciesName;

    /**
     * 是否被删除
     */
    private Integer isdeleted;

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() {
        return speciesName;
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
                .append("speciesId", getSpeciesId())
                .append("speciesName", getSpeciesName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("isdeleted",getIsdeleted())
                .toString();
    }
}

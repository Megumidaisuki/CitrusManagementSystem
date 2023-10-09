package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * population对象 population
 *
 * @author feidian
 * @date 2023-07-01
 */
public class Population extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 群体ID
     */
    private Long populationId;

    /**
     * 群体名称
     */
    @Excel(name = "群体名称", sort = 1)
    private String populationName;

    /**
     * 物种ID
     */
    @Excel(name = "物种ID", sort = 2)
    private Long speciesId;

    /**
     * 是否被删除
     */
    private Integer isdeleted;

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationName(String populationName) {
        this.populationName = populationName;
    }

    public String getPopulationName() {
        return populationName;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getSpeciesId() {
        return speciesId;
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
                .append("populationId", getPopulationId())
                .append("populationName", getPopulationName())
                .append("speciesId", getSpeciesId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("isdeleted",getIsdeleted())
                .toString();
    }
}

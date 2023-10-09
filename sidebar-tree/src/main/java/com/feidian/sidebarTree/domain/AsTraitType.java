package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 as_trait_type
 *
 * @author feidian
 * @date 2023-07-05
 */
public class AsTraitType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 关联ID
     */
    private Long asTraitTypeId;

    /**
     * 性状类型ID
     */
    @Excel(name = "性状类型ID", sort = 1)
    private Long traitTypeId;

    /**
     * 性状ID
     */
    @Excel(name = "性状ID", sort = 2)
    private String traitId;

    public void setAsTraitTypeId(Long asTraitTypeId) {
        this.asTraitTypeId = asTraitTypeId;
    }

    public Long getAsTraitTypeId() {
        return asTraitTypeId;
    }

    public void setTraitTypeId(Long traitTypeId) {
        this.traitTypeId = traitTypeId;
    }

    public Long getTraitTypeId() {
        return traitTypeId;
    }

    public void setTraitId(String traitId) {
        this.traitId = traitId;
    }

    public String getTraitId() {
        return traitId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("asTraitTypeId", getAsTraitTypeId())
                .append("traitTypeId", getTraitTypeId())
                .append("traitId", getTraitId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}

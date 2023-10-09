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

    /** 物种ID */
    @Excel(name = "物种ID")
    private String speciesId;

    /** 群体ID */
    @Excel(name = "群体ID")
    private Long populationId;

    /** 数据采集年份 */
    @Excel(name = "数据采集年份")
    private String year;

    /** 数据采集地区 */
    @Excel(name = "数据采集地区")
    private String location;

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

    /** 性状ID */
    @Excel(name = "性状ID")
    private Long traitId;

    /** 性状值 */
    @Excel(name = "性状值")
    private Long traitValue;

    public void setPhenotypeId(Long phenotypeId) 
    {
        this.phenotypeId = phenotypeId;
    }

    public Long getPhenotypeId() 
    {
        return phenotypeId;
    }
    public void setSpeciesId(String speciesId) 
    {
        this.speciesId = speciesId;
    }

    public String getSpeciesId() 
    {
        return speciesId;
    }
    public void setPopulationId(Long populationId) 
    {
        this.populationId = populationId;
    }

    public Long getPopulationId() 
    {
        return populationId;
    }
    public void setYear(String year) 
    {
        this.year = year;
    }

    public String getYear() 
    {
        return year;
    }
    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }
    public void setRepeat(Long repeat) 
    {
        this.repeat = repeat;
    }

    public Long getRepeat() 
    {
        return repeat;
    }
    public void setKindId(String kindId) 
    {
        this.kindId = kindId;
    }

    public String getKindId() 
    {
        return kindId;
    }
    public void setKindName(String kindName) 
    {
        this.kindName = kindName;
    }

    public String getKindName() 
    {
        return kindName;
    }
    public void setMaterialId(String materialId) 
    {
        this.materialId = materialId;
    }

    public String getMaterialId() 
    {
        return materialId;
    }
    public void setFieldId(Long fieldId) 
    {
        this.fieldId = fieldId;
    }

    public Long getFieldId() 
    {
        return fieldId;
    }
    public void setControlType(String controlType) 
    {
        this.controlType = controlType;
    }

    public String getControlType() 
    {
        return controlType;
    }
    public void setFather(String father) 
    {
        this.father = father;
    }

    public String getFather() 
    {
        return father;
    }
    public void setMother(String mother) 
    {
        this.mother = mother;
    }

    public String getMother() 
    {
        return mother;
    }
    public void setTraitId(Long traitId) 
    {
        this.traitId = traitId;
    }

    public Long getTraitId() 
    {
        return traitId;
    }
    public void setTraitValue(Long traitValue) 
    {
        this.traitValue = traitValue;
    }

    public Long getTraitValue() 
    {
        return traitValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("phenotypeId", getPhenotypeId())
            .append("speciesId", getSpeciesId())
            .append("populationId", getPopulationId())
            .append("year", getYear())
            .append("location", getLocation())
            .append("repeat", getRepeat())
            .append("kindId", getKindId())
            .append("kindName", getKindName())
            .append("materialId", getMaterialId())
            .append("fieldId", getFieldId())
            .append("controlType", getControlType())
            .append("father", getFather())
            .append("mother", getMother())
            .append("traitId", getTraitId())
            .append("traitValue", getTraitValue())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

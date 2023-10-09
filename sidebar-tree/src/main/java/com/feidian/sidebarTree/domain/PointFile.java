package com.feidian.sidebarTree.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;

import java.util.Arrays;

/**
 * 【请填写功能名称】对象 point_file
 * 
 * @author feidian
 * @date 2022-11-07
 */
public class PointFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num1;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num2;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num3;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num4;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num5;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num6;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long num7;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String fileUrl;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNum1(Long num1) 
    {
        this.num1 = num1;
    }

    public Long getNum1() 
    {
        return num1;
    }
    public void setNum2(Long num2) 
    {
        this.num2 = num2;
    }

    public Long getNum2() 
    {
        return num2;
    }
    public void setNum3(Long num3) 
    {
        this.num3 = num3;
    }

    public Long getNum3() 
    {
        return num3;
    }
    public void setNum4(Long num4) 
    {
        this.num4 = num4;
    }

    public Long getNum4() 
    {
        return num4;
    }
    public void setNum5(Long num5) 
    {
        this.num5 = num5;
    }

    public Long getNum5() 
    {
        return num5;
    }
    public void setNum6(Long num6) 
    {
        this.num6 = num6;
    }

    public Long getNum6() 
    {
        return num6;
    }
    public void setNum7(Long num7) 
    {
        this.num7 = num7;
    }

    public Long getNum7() 
    {
        return num7;
    }
    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("num1", getNum1())
            .append("num2", getNum2())
            .append("num3", getNum3())
            .append("num4", getNum4())
            .append("num5", getNum5())
            .append("num6", getNum6())
            .append("num7", getNum7())
            .append("fileUrl", getFileUrl())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }

    public void setIds(Integer[] ids) {
        Arrays.sort(ids);
        this.num1=ids[0].longValue();
        this.num2=ids[1].longValue();
        this.num3=ids[2].longValue();
        this.num4=ids[3].longValue();
        this.num5=ids[4].longValue();
        this.num6=ids[5].longValue();
        this.num7=ids[6].longValue();
    }

}

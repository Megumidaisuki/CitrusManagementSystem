package com.feidian.sidebarTree.domain.vo;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 表型文件对象 phenotype_file
 *
 * @author feidian
 * @date 2023-07-02
 */
public class PhenotypeFileVO extends BaseEntity
{
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

    /** 物种ID */
    @Excel(name = "物种名")
    private String speciesName;

    /** 群体ID */
    @Excel(name = "群体名")
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

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }
    public void setTreeId(Long treeId)
    {
        this.treeId = treeId;
    }

    public Long getTreeId()
    {
        return treeId;
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

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
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
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public PhenotypeFileVO(Long fileId, String fileName, String tableName, Long treeId, String speciesName, String populationName, int status, String year, String location, String url, String createBy, Date createTime, String updateBy, Date updateTime, String remark) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.tableName = tableName;
        this.treeId = treeId;
        this.speciesName = speciesName;
        this.populationName = populationName;
        this.status = status;
        this.year = year;
        this.location = location;
        this.url = url;
        super.setCreateBy(createBy);
        super.setCreateTime(createTime);
        super.setUpdateBy(updateBy);
        super.setUpdateTime(updateTime);
        super.setRemark(remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("fileName", getFileName())
                .append("tableName", getTableName())
                .append("treeId", getTreeId())
                .append("speciesName", getSpeciesName())
                .append("populationName", getPopulationName())
                .append("status", getStatus())
                .append("year", getYear())
                .append("location", getLocation())
                .append("url", getUrl())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
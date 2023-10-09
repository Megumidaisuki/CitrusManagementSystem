package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基因型文件对象 genotype_file
 *
 * @author feidian
 * @date 2023-07-06
 */
public class GenotypeFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long fileId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 数据表名，新建节点下的文件时就建一张新表，表名存在这 */
    @Excel(name = "数据表名，新建节点下的文件时就建一张新表，表名存在这")
    private String tableName;

    /** 树节点id，该文件属于哪个树节点 */
    @Excel(name = "树节点id，该文件属于哪个树节点")
    private Long treeId;

    /** 染色体 */
    @Excel(name = "染色体")
    private String chrom;

    /** 物理位置 */
    @Excel(name = "物理位置")
    private Long position;

    /** 是否公开，该数据集是否公开展示，0表示公开，1表示隐藏 */
    @Excel(name = "是否公开，该数据集是否公开展示，0表示公开，1表示隐藏")
    private Long status;

    /** 数据路径url，数据路径完整url（包含文件名） */
    @Excel(name = "数据路径url，数据路径完整url", readConverterExp = "包=含文件名")
    private String url;

    /** 分表数量 */
    @Excel(name = "分表数量")
    private int tableNum;

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
    public void setChrom(String chrom)
    {
        this.chrom = chrom;
    }

    public String getChrom()
    {
        return chrom;
    }
    public void setPosition(Long position)
    {
        this.position = position;
    }

    public Long getPosition()
    {
        return position;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("fileName", getFileName())
                .append("tableName", getTableName())
                .append("treeId", getTreeId())
                .append("chrom", getChrom())
                .append("position", getPosition())
                .append("status", getStatus())
                .append("url", getUrl())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
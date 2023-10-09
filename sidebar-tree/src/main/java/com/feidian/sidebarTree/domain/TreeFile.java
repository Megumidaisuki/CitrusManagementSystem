package com.feidian.sidebarTree.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;

/**
 * 树节点上的文件对象 tree_file
 *
 * @author feidian
 * @date 2022-07-04
 */
public class TreeFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 树文件id */
    private Long fileId;

    /** 树文件的url */
    @Excel(name = "树文件的url")
    private String fileUrl;

    /** 文件在树的哪一个节点 */
    @Excel(name = "文件在树的哪一个节点")
    private Long treeId;

    /** 该文件的描述 */
    @Excel(name = "该文件的描述")
    private String description;

    /** 该文件是否公开 */
    @Excel(name = "该文件是否公开")
    private Integer fileStatus;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件日期 */
    @Excel(name = "文件日期")
    private String dateTime;

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }
    public void setTreeId(Long treeId)
    {
        this.treeId = treeId;
    }

    public Long getTreeId()
    {
        return treeId;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setFileStatus(Integer fileStatus)
    {
        this.fileStatus = fileStatus;
    }

    public Integer getFileStatus()
    {
        return fileStatus;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("fileUrl", getFileUrl())
            .append("treeId", getTreeId())
            .append("description", getDescription())
            .append("fileStatus", getFileStatus())
            .append("fileName", getFileName())
            .append("dateTime",getDateTime())
            .toString();
    }
}

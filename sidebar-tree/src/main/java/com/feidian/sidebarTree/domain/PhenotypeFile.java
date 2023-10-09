package com.feidian.sidebarTree.domain;

import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 表型文件对象 phenotype_file
 *
 * @author feidian
 * @date 2023-07-02
 */
public class PhenotypeFile extends BaseEntity
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
        @Excel(name = "物种ID")
        private Long speciesId;

        /** 群体ID */
        @Excel(name = "群体ID")
        private Long populationId;

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
        public void setSpeciesId(Long speciesId)
        {
                this.speciesId = speciesId;
        }

        public Long getSpeciesId()
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

        public static long getSerialVersionUID() {
                return serialVersionUID;
        }

        @Override
        public String toString() {
                return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                        .append("fileId", getFileId())
                        .append("fileName", getFileName())
                        .append("tableName", getTableName())
                        .append("treeId", getTreeId())
                        .append("speciesId", getSpeciesId())
                        .append("populationId", getPopulationId())
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
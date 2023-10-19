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

        /** 是否公开 */
        @Excel(name = "是否公开")
        private int status;

        /** 数据路径url */
        @Excel(name = "数据路径url")
        private String url;

        public PhenotypeFile() {
        }

        public PhenotypeFile(Long fileId, String fileName, String tableName, Long treeId, int status, String url) {
                this.fileId = fileId;
                this.fileName = fileName;
                this.tableName = tableName;
                this.treeId = treeId;
                this.status = status;
                this.url = url;
        }


        @Override
        public String toString() {
                return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                        .append("fileId", getFileId())
                        .append("fileName", getFileName())
                        .append("tableName", getTableName())
                        .append("treeId", getTreeId())
                        .append("status", getStatus())
                        .append("url", getUrl())
                        .append("createBy", getCreateBy())
                        .append("createTime", getCreateTime())
                        .append("updateBy", getUpdateBy())
                        .append("updateTime", getUpdateTime())
                        .append("remark", getRemark())
                        .toString();
        }

        /**
         * 获取
         * @return fileId
         */
        public Long getFileId() {
                return fileId;
        }

        /**
         * 设置
         * @param fileId
         */
        public void setFileId(Long fileId) {
                this.fileId = fileId;
        }

        /**
         * 获取
         * @return fileName
         */
        public String getFileName() {
                return fileName;
        }

        /**
         * 设置
         * @param fileName
         */
        public void setFileName(String fileName) {
                this.fileName = fileName;
        }

        /**
         * 获取
         * @return tableName
         */
        public String getTableName() {
                return tableName;
        }

        /**
         * 设置
         * @param tableName
         */
        public void setTableName(String tableName) {
                this.tableName = tableName;
        }

        /**
         * 获取
         * @return treeId
         */
        public Long getTreeId() {
                return treeId;
        }

        /**
         * 设置
         * @param treeId
         */
        public void setTreeId(Long treeId) {
                this.treeId = treeId;
        }

        /**
         * 获取
         * @return status
         */
        public int getStatus() {
                return status;
        }

        /**
         * 设置
         * @param status
         */
        public void setStatus(int status) {
                this.status = status;
        }

        /**
         * 获取
         * @return url
         */
        public String getUrl() {
                return url;
        }

        /**
         * 设置
         * @param url
         */
        public void setUrl(String url) {
                this.url = url;
        }
}
package com.feidian.sidebarTree.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 侧边栏树的对象 sidebar_tree
 *
 * @author feidian
 * @date 2022-07-01
 */
public class SidebarTree extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 侧边栏树的id
     */
    private Long treeId;

    /**
     * 侧边栏树的名称
     */
    @Excel(name = "侧边栏树的名称")
    private String treeName;

    /**
     * 侧边栏树的父节点id
     */
    @Excel(name = "侧边栏树的父节点id")
    private Long parentId;

    /**
     * 是否显示，1为显示，0为不显示
     */
    @Excel(name = "是否显示，1为显示，0为不显示")
    private Integer isShow;

    @Excel(name = "父目录")
    private String parentFile;

    @Excel(name = "ip地址")
    private String ip;

    @Excel(name = "拍摄设备")
    private String shootEqmt;
    @Excel(name = "拍摄时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shootTime;
    @Excel(name = "处理方法")
    private String procMeth;


    @Excel(name = "关键词")
    private String keyword;

    /**
     * 子菜单
     */
    private List<SidebarTree> children = new ArrayList<SidebarTree>();

    /** 是哪一个树，1为种苗品种，2为种苗图片，盘穴检测和叶片检测，3为种苗表型信息和生长预测 */
    @Excel(name = "是哪一个树，1为种苗品种，2为种苗图片，盘穴检测和叶片检测，3为种苗表型信息和生长预测")
    private Integer treeType;

    public String getParentFile() {
        return parentFile;
    }

    public void setParentFile(String parentFile) {
        this.parentFile = parentFile;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setTreeType(Integer treeType) { this.treeType = treeType; }

    public Integer getTreeType() { return treeType; }

    public List<SidebarTree> getChildren() {
        return children;
    }

    public void setChildren(List<SidebarTree> children) {
        this.children = children;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getShootEqmt() {
        return shootEqmt;
    }

    public void setShootEqmt(String shootEqmt) {
        this.shootEqmt = shootEqmt;
    }

    public Date getShootTime() {
        return shootTime;
    }

    public void setShootTime(Date shootTime) {
        this.shootTime = shootTime;
    }

    public String getProcMeth() {
        return procMeth;
    }

    public void setProcMeth(String procMeth) {
        this.procMeth = procMeth;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("treeId", getTreeId())
                .append("treeName", getTreeName())
                .append("parentId", getParentId())
                .append("isShow", getIsShow())
                .append("treeType", getTreeType())
                .append("parentFile",getParentFile())
                .append("ip",getIp())
                .append("shootEqmt",getShootEqmt())
                .append("shootTime",getShootTime())
                .append("procMeth",getProcMeth())
                .append("keyword",getKeyword())
                .toString();
    }
}

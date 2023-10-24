package com.feidian.sidebarTree.domain.vo;

import com.feidian.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SidebarTreeVO {
    /**
     * 侧边栏树的id
     */
    private Long treeId;

    /**
     * 侧边栏树的名称
     */

    private String treeName;

    private List<Long> dailyNodeNum;


    /**
     * 侧边栏树对应的数量
     */
    private Long nodeNum;

    public void setDailyNodeNum(List<Long> dailyNodeNum){
        this.dailyNodeNum = dailyNodeNum;
    }
    public List<Long> getDailyNodeNum(){
        return dailyNodeNum;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }
    public void setNodeNum(Long nodeNum) {
        this.nodeNum = nodeNum;
    }

    public Long getNodeNum(){
        return nodeNum;
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
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("treeId", getTreeId())
                .append("treeName", getTreeName())
                .append("nodeNum",getNodeNum()).toString();
    }
}

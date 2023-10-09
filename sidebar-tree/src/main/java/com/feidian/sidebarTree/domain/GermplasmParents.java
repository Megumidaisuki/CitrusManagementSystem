package com.feidian.sidebarTree.domain;
import java.util.Date;
import java.util.Objects;

public class GermplasmParents {
    private Long id;
    private String materialId;
    private String father;
    private String mother;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
//
//    public GermplasmParents(GermplasmParents g) {
//        this.id = g.getId();
//        this.materialId = materialId;
//        this.father = father;
//        this.mother = mother;
//        this.createBy = createBy;
//        this.createTime = createTime;
//        this.updateBy = updateBy;
//        this.updateTime = updateTime;
//        this.remark = remark;
//    }


    @Override
    public boolean equals(Object obj) {
        return this.materialId.equals(((GermplasmParents) obj).getMaterialId()) && this.father.equals(((GermplasmParents) obj).getFather()) && this.mother.equals(((GermplasmParents) obj).getMother());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.materialId,this.father,this.father);
    }
}

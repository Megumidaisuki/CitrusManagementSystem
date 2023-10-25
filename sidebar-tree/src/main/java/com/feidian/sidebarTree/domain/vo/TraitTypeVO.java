package com.feidian.sidebarTree.domain.vo;

public class TraitTypeVO {
    private Long traitId;
    private String traitName;
    private Long traitTypeId;
    private String traitTypeName;

    public TraitTypeVO() {
    }

    public TraitTypeVO(Long traitId, String traitName, Long traitTypeId, String traitTypeName) {
        this.traitId = traitId;
        this.traitName = traitName;
        this.traitTypeId = traitTypeId;
        this.traitTypeName = traitTypeName;
    }

    /**
     * 获取
     * @return traitId
     */
    public Long getTraitId() {
        return traitId;
    }

    /**
     * 设置
     * @param traitId
     */
    public void setTraitId(Long traitId) {
        this.traitId = traitId;
    }

    /**
     * 获取
     * @return traitName
     */
    public String getTraitName() {
        return traitName;
    }

    /**
     * 设置
     * @param traitName
     */
    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    /**
     * 获取
     * @return traitTypeId
     */
    public Long getTraitTypeId() {
        return traitTypeId;
    }

    /**
     * 设置
     * @param traitTypeId
     */
    public void setTraitTypeId(Long traitTypeId) {
        this.traitTypeId = traitTypeId;
    }

    /**
     * 获取
     * @return traitTypeName
     */
    public String getTraitTypeName() {
        return traitTypeName;
    }

    /**
     * 设置
     * @param traitTypeName
     */
    public void setTraitTypeName(String traitTypeName) {
        this.traitTypeName = traitTypeName;
    }

    public String toString() {
        return "TraitTypeVO{traitId = " + traitId + ", traitName = " + traitName + ", traitTypeId = " + traitTypeId + ", traitTypeName = " + traitTypeName + "}";
    }
}

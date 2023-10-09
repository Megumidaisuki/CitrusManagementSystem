package com.feidian.sidebarTree.domain.vo;

public class DataAnalysisVO {
    private Long triatId;
    private String traitName;
    private Double average;
    private Double maxNum;
    private Double minNum;

    @Override
    public String toString() {
        return "DataAnalysisVO{" +
                "triatId=" + triatId +
                ", traitName='" + traitName + '\'' +
                ", average=" + average +
                ", maxNum=" + maxNum +
                ", minNum=" + minNum +
                '}';
    }

    public DataAnalysisVO() {
    }

    public DataAnalysisVO(Long triatId, String traitName,Double maxNum, Double minNum, Double average) {
        this.triatId = triatId;
        this.traitName = traitName;
        this.average = average;
        this.maxNum = maxNum;
        this.minNum = minNum;
    }

    public Long getTriatId() {
        return triatId;
    }

    public void setTriatId(Long triatId) {
        this.triatId = triatId;
    }

    public String getTraitName() {
        return traitName;
    }

    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Double maxNum) {
        this.maxNum = maxNum;
    }

    public Double getMinNum() {
        return minNum;
    }

    public void setMinNum(Double minNum) {
        this.minNum = minNum;
    }
}

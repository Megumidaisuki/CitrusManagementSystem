package com.feidian.sidebarTree.domain.vo;

public class DataAnalysisVO {
    private Long triatId;
    private String traitName;
    private Long traitTypeId;
    private String traitTypeName;
    private Object value;
    private Object average;
    private Object maxNum;
    private Object minNum;

    public DataAnalysisVO(Long triatId, String traitName, Object value, Object average, Object maxNum, Object minNum) {
        this.triatId = triatId;
        this.traitName = traitName;
        this.value = value;
        this.average = average;
        this.maxNum = maxNum;
        this.minNum = minNum;
    }

    public DataAnalysisVO(Long triatId, String traitName, Long traitTypeId, String traitTypeName, Object value, Object average, Object maxNum, Object minNum) {
        this.triatId = triatId;
        this.traitName = traitName;
        this.traitTypeId = traitTypeId;
        this.traitTypeName = traitTypeName;
        this.value = value;
        this.average = average;
        this.maxNum = maxNum;
        this.minNum = minNum;
    }



    @Override
    public String toString() {
        return "DataAnalysisVO{" +
                "triatId=" + triatId +
                ", traitName='" + traitName + '\'' +
                ", traitType=" + traitTypeId +
                ", traitTypeName=" + traitTypeName +
                ", value=" + value +
                ", average=" + average +
                ", maxNum=" + maxNum +
                ", minNum=" + minNum +
                '}';
    }

    public DataAnalysisVO() {
    }

    public DataAnalysisVO(Long triatId, String traitName,Object maxNum, Object minNum, Object average) {
        this.triatId = triatId;
        this.traitName = traitName;
        this.average = average;
        this.maxNum = maxNum;
        this.minNum = minNum;
    }


    /**
     * 获取
     * @return triatId
     */
    public Long getTriatId() {
        return triatId;
    }

    /**
     * 设置
     * @param triatId
     */
    public void setTriatId(Long triatId) {
        this.triatId = triatId;
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

    /**
     * 获取
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * 设置
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }


    /**
     * 获取
     * @return average
     */
    public Object getAverage() {
        return average;
    }

    /**
     * 设置
     * @param average
     */
    public void setAverage(Object average) {
        this.average = average;
    }

    /**
     * 获取
     * @return maxNum
     */
    public Object getMaxNum() {
        return maxNum;
    }

    /**
     * 设置
     * @param maxNum
     */
    public void setMaxNum(Object maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * 获取
     * @return minNum
     */
    public Object getMinNum() {
        return minNum;
    }

    /**
     * 设置
     * @param minNum
     */
    public void setMinNum(Object minNum) {
        this.minNum = minNum;
    }
}

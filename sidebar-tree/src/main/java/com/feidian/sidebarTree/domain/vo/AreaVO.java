package com.feidian.sidebarTree.domain.vo;

public class AreaVO {
    private String area;
    private Integer num;

    @Override
    public String toString() {
        return "AreaVO{" +
                "area='" + area + '\'' +
                ", num=" + num +
                '}';
    }

    public AreaVO(String area) {
        this.area = area;
    }

    public AreaVO(String area, Integer num) {
        this.area = area;
        this.num = num;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}

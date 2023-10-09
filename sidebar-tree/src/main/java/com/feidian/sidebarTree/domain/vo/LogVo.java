package com.feidian.sidebarTree.domain.vo;


public class LogVo {
    private String time;
    private Integer number;

    public LogVo(String time, Integer number) {
        this.time = time;
        this.number = number;
    }

    public LogVo() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}

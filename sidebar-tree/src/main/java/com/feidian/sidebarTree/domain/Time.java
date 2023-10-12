package com.feidian.sidebarTree.domain;

import com.feidian.common.core.domain.BaseEntity;

public class Time extends BaseEntity {
    private String starttime;
    private String endtime;

    public Time() {
    }

    public Time(String starttime, String endtime) {
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}

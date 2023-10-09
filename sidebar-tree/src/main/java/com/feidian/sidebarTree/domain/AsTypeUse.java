package com.feidian.sidebarTree.domain;

import com.feidian.common.core.domain.BaseEntity;

import java.util.List;

public class AsTypeUse extends BaseEntity {
    private List<Long> list;

    public String type;

    public String name;

    public AsTypeUse() {
    }

    public AsTypeUse(List<Long> list, String type, String name) {
        this.list = list;
        this.type = type;
        this.name = name;
    }

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

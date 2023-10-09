package com.feidian.sidebarTree.domain;

import java.util.Date;

/**
 * @program: zhongmiao
 * @description:
 * @author: Tinaliasd
 **/
public class Relationship {
    //子的编号
    private String material_id;
    //父母的编号
    private String parent;
    //是否父母 0代表母亲,1代表父亲
    private Integer flag;

    public Relationship(String material_id, String parent, Integer flag) {
        this.material_id = material_id;
        this.parent = parent;
        this.flag = flag;
        Date a = new Date();
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}

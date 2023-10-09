package com.feidian.sidebarTree.domain.vo;

public class TraitVO {
    private Object chidren;
    private String name;
    private Long id;

    @Override
    public String toString() {
        return "TraitVO{" +
                "chidren=" + chidren +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public TraitVO() {
    }

    public TraitVO(Object chidren, String name, Long id) {
        this.chidren = chidren;
        this.name = name;
        this.id = id;
    }

    public Object getChidren() {
        return chidren;
    }

    public void setChidren(Object chidren) {
        this.chidren = chidren;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package com.feidian.sidebarTree.domain.vo;

public class DocumentNumVO {
    //文件创建时间
    private String createTime;
    //文件数
    private Long count;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "DocumentNumVO{" +
                "createTime='" + createTime + '\'' +
                ", count=" + count +
                '}';
    }

    public DocumentNumVO(String createTime, Long count) {
        this.createTime = createTime;
        this.count = count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

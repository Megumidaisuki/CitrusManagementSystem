package com.feidian.sidebarTree.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 对象 tree_picture
 *
 * @author feidian
 * @date 2022-07-03
 */
public class TreePicture extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图片的id编号 */
    private Long pictureId;

    /** 图片在服务器上的存储地址 */
    @Excel(name = "图片在服务器上的存储地址")
    private String pictureUrl;

    /** 图片在树的哪一个节点 */
    @Excel(name = "图片在树的哪一个节点")
    private Long treeId;

    /** 该图片是否公开 */
    @Excel(name = "该图片是否公开")
    private Integer isShow;

    /** 该图片是否公开 */
    @Excel(name = "该图片是否公开")
    private String lessPictureUrl;

    @Excel(name = "拍摄时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shotTime;

    public void setPictureId(Long pictureId)
    {
        this.pictureId = pictureId;
    }

    public Long getPictureId()
    {
        return pictureId;
    }
    public void setPictureUrl(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl()
    {
        return pictureUrl;
    }
    public void setTreeId(Long treeId)
    {
        this.treeId = treeId;
    }

    public Long getTreeId()
    {
        return treeId;
    }
    public void setIsShow(Integer isShow)
    {
        this.isShow = isShow;
    }

    public Integer getIsShow()
    {
        return isShow;
    }

    public String getLessPictureUrl() {
        return lessPictureUrl;
    }

    public void setLessPictureUrl(String lessPictureUrl) {
        this.lessPictureUrl = lessPictureUrl;
    }

    public Date getShotTime() {
        return shotTime;
    }

    public void setShotTime(Date shootTime) {
        this.shotTime = shootTime;
    }

    @Override
    public String toString() {
        return "TreePicture{" +
                "pictureId=" + pictureId +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", treeId=" + treeId +
                ", isShow=" + isShow +
                ", lessPictureUrl='" + lessPictureUrl + '\'' +
                ", shotTime='"+shotTime+'\'';
    }
}

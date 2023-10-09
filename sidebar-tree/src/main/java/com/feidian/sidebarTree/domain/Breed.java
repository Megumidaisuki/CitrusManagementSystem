package com.feidian.sidebarTree.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.feidian.common.annotation.Excel;
import com.feidian.common.core.domain.BaseEntity;
import com.feidian.sidebarTree.pythonCode.UseParam;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * breed对象 breed
 *
 * @author feidian
 * @date 2023-07-03
 */
public class Breed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 育种任务ID */
    private Long breedId;

    /** 基因型数据集ID */
    @Excel(name = "基因型数据集ID")
    private Long genofileId;

    /** 表型数据集ID */
    @Excel(name = "表型数据集ID")
    private Long phenofileId;

    /** 育种结果存放目录 */
    @Excel(name = "育种结果存放目录")
    private String breedUrl;

    /** 所属场景 */
    @Excel(name = "所属场景")
    private Integer scene;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private Integer status;

    private String result;

    private String models;

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    /** 表型文件标志 */
    private Integer phenofileFlag;

    /** 基因文件标志 */
    private Integer genofileFlag;

    /** 任务提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submitTime;

    /** 任务结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    private String phenofile;

    private String genofile;

    private String genofileName;

    private String phenofileName;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPhenofile() {
        return phenofile;
    }

    public void setPhenofile(String phenofile) {
        this.phenofile = phenofile;
        if(phenofile!=null){
            setPhenofileName(UseParam.getFileNameFromURL(phenofile));
        }
    }

    public String getGenofile() {
        return genofile;
    }

    public void setGenofile(String genofile) {
        this.genofile = genofile;
        if(genofile!=null){
            setGenofileName(UseParam.getFileNameFromURL(genofile));
        }
    }

    public Integer getPhenofileFlag() {
        return phenofileFlag;
    }

    public void setPhenofileFlag(Integer phenofileFlag) {
        this.phenofileFlag = phenofileFlag;
    }

    public Integer getGenofileFlag() {
        return genofileFlag;
    }

    public void setGenofileFlag(Integer genofileFlag) {
        this.genofileFlag = genofileFlag;
    }

    public void setBreedId(Long breedId)
    {
        this.breedId = breedId;
    }

    public Long getBreedId()
    {
        return breedId;
    }
    public void setGenofileId(Long genofileId)
    {
        this.genofileId = genofileId;
    }

    public Long getGenofileId()
    {
        return genofileId;
    }
    public void setPhenofileId(Long phenofileId)
    {
        this.phenofileId = phenofileId;
    }

    public Long getPhenofileId()
    {
        return phenofileId;
    }
    public void setBreedUrl(String breedUrl)
    {
        this.breedUrl = breedUrl;
    }

    public String getBreedUrl()
    {
        return breedUrl;
    }
    public void setScene(Integer scene)
    {
        this.scene = scene;
    }

    public Integer getScene()
    {
        return scene;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }
    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public String getGenofileName() {
        return genofileName;
    }

    public void setGenofileName(String genofileName) {
        this.genofileName = genofileName;
    }

    public String getPhenofileName() {
        return phenofileName;
    }

    public void setPhenofileName(String phenofileName) {
        this.phenofileName = phenofileName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("breedId", getBreedId())
                .append("genofileId", getGenofileId())
                .append("phenofileId", getPhenofileId())
                .append("breedUrl", getBreedUrl())
                .append("scene", getScene())
                .append("status", getStatus())
                .append("submitTime", getSubmitTime())
                .append("finishTime", getFinishTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("phenofileFlag", getPhenofileFlag())
                .append("genofileFlag", getGenofileFlag())
                .append("phenofile", getPhenofile())
                .append("genofile", getGenofile())
                .append("phenofileName",getPhenofileName())
                .append("genofileName",getGenofileName())
                .append("result",getResult())
                .append("models",getModels())
                .toString();
    }
}

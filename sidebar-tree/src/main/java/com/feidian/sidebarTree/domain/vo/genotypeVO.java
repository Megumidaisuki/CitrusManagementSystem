package com.feidian.sidebarTree.domain.vo;

import java.util.HashMap;

public class genotypeVO {
    //染色体
    private String chrom;
    //物理位置
    private int position;
    //变异位点ID
    private String id;
    //参考基因
    private String ref;
    //等位基因
    private String ALT;
    //质量得分
    private double QUAL;
    //过滤标记
    private String FILTER;
    //附加信息
    private String INFO;
    //格式
    private String FORMAT;

    private HashMap<String,String> map;

    private String remarks;

    public genotypeVO() {
    }


    public genotypeVO(String chrom, int position, String id, String ref, String ALT, double QUAL, String FILTER, String INFO, String FORMAT, HashMap<String, String> map, String remarks) {
        this.chrom = chrom;
        this.position = position;
        this.id = id;
        this.ref = ref;
        this.ALT = ALT;
        this.QUAL = QUAL;
        this.FILTER = FILTER;
        this.INFO = INFO;
        this.FORMAT = FORMAT;
        this.map = map;
        this.remarks = remarks;
    }

    public String getChrom() {
        return chrom;
    }

    public void setChrom(String chrom) {
        this.chrom = chrom;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getALT() {
        return ALT;
    }

    public void setALT(String ALT) {
        this.ALT = ALT;
    }

    public double getQUAL() {
        return QUAL;
    }

    public void setQUAL(double QUAL) {
        this.QUAL = QUAL;
    }

    public String getFILTER() {
        return FILTER;
    }

    public void setFILTER(String FILTER) {
        this.FILTER = FILTER;
    }

    public String getINFO() {
        return INFO;
    }

    public void setINFO(String INFO) {
        this.INFO = INFO;
    }

    public String getFORMAT() {
        return FORMAT;
    }

    public void setFORMAT(String FORMAT) {
        this.FORMAT = FORMAT;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

package com.feidian.sidebarTree.domain;

/**
 * 材料对象 pca
 *
 * @author feidian
 * @date 2023-07-05
 */
public class PCA {//
    private String material;
    private String pca1;
    private String pca2;
    private String pca3;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPca1() {
        return pca1;
    }

    public void setPca1(String pca1) {
        this.pca1 = pca1;
    }

    public String getPca2() {
        return pca2;
    }

    public void setPca2(String pca2) {
        this.pca2 = pca2;
    }

    public String getPca3() {
        return pca3;
    }

    public void setPca3(String pca3) {
        this.pca3 = pca3;
    }

    @Override
    public String toString() {
        return "PCA{" +
                "material='" + material + '\'' +
                ", pca1='" + pca1 + '\'' +
                ", pca2='" + pca2 + '\'' +
                ", pca3='" + pca3 + '\'' +
                '}';
    }

    public PCA(String material, String pca1, String pca2, String pca3) {
        this.material = material;
        this.pca1 = pca1;
        this.pca2 = pca2;
        this.pca3 = pca3;
    }
}

package com.hyperchain.controller.vo;

/**
 * Created by liangyue on 2017/4/13.
 */
public class RepoCertVo {
    private String repoBusinessNo;
    private String productName;
    private int productQuantitiy;
    private int repoBusiStatus;
    private String repoEnterpriseName;

    public RepoCertVo(String repoBusinessNo, String productName, int productQuantitiy, int repoBusiStatus, String repoEnterpriseName) {
        this.repoBusinessNo = repoBusinessNo;
        this.productName = productName;
        this.productQuantitiy = productQuantitiy;
        this.repoBusiStatus = repoBusiStatus;
        this.repoEnterpriseName = repoEnterpriseName;
    }

    public String getRepoEnterpriseName() {
        return repoEnterpriseName;
    }

    public void setRepoEnterpriseName(String repoEnterpriseName) {
        this.repoEnterpriseName = repoEnterpriseName;
    }

    public String getRepoBusinessNo() {
        return repoBusinessNo;
    }

    public void setRepoBusinessNo(String repoBusinessNo) {
        this.repoBusinessNo = repoBusinessNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantitiy() {
        return productQuantitiy;
    }

    public void setProductQuantitiy(int productQuantitiy) {
        this.productQuantitiy = productQuantitiy;
    }

    public int getRepoBusiStatus() {
        return repoBusiStatus;
    }

    public void setRepoBusiStatus(int repoBusiStatus) {
        this.repoBusiStatus = repoBusiStatus;
    }

}

package com.hyperchain.controller.vo;

import java.util.List;

/**
 * Created by chenxiaoyang on 2017/4/26.
 */
public class RepoCertList {
    private String repoCertNo;//仓单编号
    private String productName;//产品名称
    private String repoEnterpriseName;//仓储公司名称
    private long productQuantity;//产品数量
    private  int repoCertStatus;//仓单状态

    public String getRepoCertNo() {
        return repoCertNo;
    }

    public void setRepoCertNo(String repoCertNo) {
        this.repoCertNo = repoCertNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRepoEnterpriseName() {
        return repoEnterpriseName;
    }

    public void setRepoEnterpriseName(String repoEnterpriseName) {
        this.repoEnterpriseName = repoEnterpriseName;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getRepoCertStatus() {
        return repoCertStatus;
    }

    public void setRepoCertStatus(int repoCertStatus) {
        this.repoCertStatus = repoCertStatus;
    }
}

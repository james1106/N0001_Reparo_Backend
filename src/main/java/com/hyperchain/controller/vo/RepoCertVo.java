package com.hyperchain.controller.vo;

import java.util.List;

/**
 * Created by chenxiaoyang on 2017/4/13.
 * 仓单详情即仓单流水数据
 */
public class RepoCertVo {

    private String repoCertNo;//仓单编号
    private String repoBusinessNo;//仓储业务编号
    private String productName;//产品名称
    private String measureUnit;//计量单位
    private String productLocation;//产品所在位置
    private String repoEnterpriseAddress;//仓储公司address
    private String storerAddress;//存货人address
    private String holderAddress;//持有人address
    private List<OperationRecordVo> repoCertHisList;
    private String productQuantity;//产品数量
    private String productTotalPrice;//产品总价
    private String repoCreateDate;//仓单生成时间

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getRepoCreateDate() {
        return repoCreateDate;
    }

    public void setRepoCreateDate(String repoCreateDate) {
        this.repoCreateDate = repoCreateDate;
    }


    public String getRepoCertNo() {
        return repoCertNo;
    }

    public void setRepoCertNo(String repoCertNo) {
        this.repoCertNo = repoCertNo;
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

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getRepoEnterpriseAddress() {
        return repoEnterpriseAddress;
    }

    public void setRepoEnterpriseAddress(String repoEnterpriseAddress) {
        this.repoEnterpriseAddress = repoEnterpriseAddress;
    }

    public String getStorerAddress() {
        return storerAddress;
    }

    public void setStorerAddress(String storerAddress) {
        this.storerAddress = storerAddress;
    }

    public String getHolderAddress() {
        return holderAddress;
    }

    public void setHolderAddress(String holderAddress) {
        this.holderAddress = holderAddress;
    }

    public List<OperationRecordVo> getRepoCertHisList() {
        return repoCertHisList;
    }

    public void setRepoCertHisList(List<OperationRecordVo> repoCertHisList) {
        this.repoCertHisList = repoCertHisList;
    }

}

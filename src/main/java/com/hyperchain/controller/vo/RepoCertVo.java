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
    private String repoEnterpriseName;//仓储公司名称
    private String storerAddress;//存货人address
    private String holderAddress;//持有人address
    private String storerName;//存货人
    private String holderName;//持有人
    private List<OperationRecordVo> repoCertHisList;//仓单流水记录
    private long productQuantity;//产品数量
    private long productTotalPrice;//产品总价
    private long repoCreateDate;//仓单生成时间
    private  int repoCertStatus;//仓单状态
    private List<OperationRecordVo> recordVos;

    public List<OperationRecordVo> getRecordVos() {
        return recordVos;
    }

    public void setRecordVos(List<OperationRecordVo> recordVos) {
        this.recordVos = recordVos;
    }


    public String getStorerName() {
        return storerName;
    }

    public void setStorerName(String storerName) {
        this.storerName = storerName;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public int getRepoCertStatus() {
        return repoCertStatus;
    }

    public void setRepoCertStatus(int repoCertStatus) {
        this.repoCertStatus = repoCertStatus;
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

    public long getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(long productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public long getRepoCreateDate() {
        return repoCreateDate;
    }

    public void setRepoCreateDate(long repoCreateDate) {
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

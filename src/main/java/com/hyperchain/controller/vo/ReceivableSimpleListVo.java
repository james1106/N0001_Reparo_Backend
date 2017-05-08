package com.hyperchain.controller.vo;

/**
 * Created by YanYufei on 2017/4/12.
 */
public class ReceivableSimpleListVo {
    private String receivableNo;
    private String productName;
    private String enterpriseName;
    private String firstOwnerName;
    private String accptrName;

    private long discountApplyTime;
    private long productQuantity;
    private String isseAmt;
    private long dueDt;
    private int status;

    public String getFirstOwnerName() {
        return firstOwnerName;
    }

    public void setFirstOwnerName(String firstOwnerName) {
        this.firstOwnerName = firstOwnerName;
    }

    public String getAccptrName() {
        return accptrName;
    }

    public void setAccptrName(String accptrName) {
        this.accptrName = accptrName;
    }

    public long getDiscountApplyTime() {
        return discountApplyTime;
    }

    public void setDiscountApplyTime(long discountApplyTime) {
        this.discountApplyTime = discountApplyTime;
    }

    public String getIsseAmt() {
        return isseAmt;
    }

    public void setIsseAmt(String isseAmt) {
        this.isseAmt = isseAmt;
    }

    public String getReceivableNo() {
        return receivableNo;
    }

    public void setReceivableNo(String receivableNo) {
        this.receivableNo = receivableNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }


    public long getDueDt() {
        return dueDt;
    }

    public void setDueDt(long dueDt) {
        this.dueDt = dueDt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

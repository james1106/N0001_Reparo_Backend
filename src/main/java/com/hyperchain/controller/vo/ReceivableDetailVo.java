package com.hyperchain.controller.vo;

import org.springframework.stereotype.Component;

/**
 * Created by YanYufei on 2017/4/10.
 */
@Component
public class ReceivableDetailVo {
    private String receivableNo;
    private String orderNo;
    private String signer;
    private String accptr;
    private String pyer;
    private String pyee;
    private String firstOwner;
    private String secondOwner;
    private String status;
    private String lastStatus;
    private String rate;
    private String contractNo;
    private String invoiceNo;

    private String pyerEnterpriseName;
    private String pyerAcctSvcrName;
    private String pyeeEnterpriseName;
    private String pyeeAcctSvcrName;

    private long isseAmt;
    private long cashedAmount;
    private long isseDt;
    private long signInDt;
    private long dueDt;

    public String getReceivableNo() {
        return receivableNo;
    }

    public void setReceivableNo(String receivableNo) {
        this.receivableNo = receivableNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getAccptr() {
        return accptr;
    }

    public void setAccptr(String accptr) {
        this.accptr = accptr;
    }

    public String getPyer() {
        return pyer;
    }

    public void setPyer(String pyer) {
        this.pyer = pyer;
    }

    public String getPyee() {
        return pyee;
    }

    public void setPyee(String pyee) {
        this.pyee = pyee;
    }

    public String getFirstOwner() {
        return firstOwner;
    }

    public void setFirstOwner(String firstOwner) {
        this.firstOwner = firstOwner;
    }

    public String getSecondOwner() {
        return secondOwner;
    }

    public void setSecondOwner(String secondOwner) {
        this.secondOwner = secondOwner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getPyerEnterpriseName() {
        return pyerEnterpriseName;
    }

    public void setPyerEnterpriseName(String pyerEnterpriseName) {
        this.pyerEnterpriseName = pyerEnterpriseName;
    }

    public String getPyerAcctSvcrName() {
        return pyerAcctSvcrName;
    }

    public void setPyerAcctSvcrName(String pyerAcctSvcrName) {
        this.pyerAcctSvcrName = pyerAcctSvcrName;
    }

    public String getPyeeEnterpriseName() {
        return pyeeEnterpriseName;
    }

    public void setPyeeEnterpriseName(String pyeeEnterpriseName) {
        this.pyeeEnterpriseName = pyeeEnterpriseName;
    }

    public String getPyeeAcctSvcrName() {
        return pyeeAcctSvcrName;
    }

    public void setPyeeAcctSvcrName(String pyeeAcctSvcrName) {
        this.pyeeAcctSvcrName = pyeeAcctSvcrName;
    }

    public long getIsseAmt() {
        return isseAmt;
    }

    public void setIsseAmt(long isseAmt) {
        this.isseAmt = isseAmt;
    }

    public long getCashedAmount() {
        return cashedAmount;
    }

    public void setCashedAmount(long cashedAmount) {
        this.cashedAmount = cashedAmount;
    }

    public long getIsseDt() {
        return isseDt;
    }

    public void setIsseDt(long isseDt) {
        this.isseDt = isseDt;
    }

    public long getSignInDt() {
        return signInDt;
    }

    public void setSignInDt(long signInDt) {
        this.signInDt = signInDt;
    }

    public long getDueDt() {
        return dueDt;
    }

    public void setDueDt(long dueDt) {
        this.dueDt = dueDt;
    }
}

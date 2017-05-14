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
    private int status;
    private int lastStatus;
    private String rate;
    private String contractNo;
    private String invoiceNo;

    private String pyerEnterpriseName;
    private String pyerAcctSvcrName;
    private String pyeeEnterpriseName;
    private String pyeeAcctSvcrName;

    private String isseAmt;

    private String  payerRepoCertNo ;
    private String  payeeRepoCertNo ;
    private String  payerRepoEnterpriseName ;
    private String  payeeRepoEnterpriseName ;
    private String  waybillNo ;
    private String  logisticsEnterpriseName ;
    private String discountedRate;

    public String getDiscountedRate() {
        return discountedRate;
    }

    public void setDiscountedRate(String discountedRate) {
        this.discountedRate = discountedRate;
    }

    public String getPayerRepoCertNo() {
        return payerRepoCertNo;
    }

    public void setPayerRepoCertNo(String payerRepoCertNo) {
        this.payerRepoCertNo = payerRepoCertNo;
    }

    public String getPayeeRepoCertNo() {
        return payeeRepoCertNo;
    }

    public void setPayeeRepoCertNo(String payeeRepoCertNo) {
        this.payeeRepoCertNo = payeeRepoCertNo;
    }

    public String getPayerRepoEnterpriseName() {
        return payerRepoEnterpriseName;
    }

    public void setPayerRepoEnterpriseName(String payerRepoEnterpriseName) {
        this.payerRepoEnterpriseName = payerRepoEnterpriseName;
    }

    public String getPayeeRepoEnterpriseName() {
        return payeeRepoEnterpriseName;
    }

    public void setPayeeRepoEnterpriseName(String payeeRepoEnterpriseName) {
        this.payeeRepoEnterpriseName = payeeRepoEnterpriseName;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getLogisticsEnterpriseName() {
        return logisticsEnterpriseName;
    }

    public void setLogisticsEnterpriseName(String logisticsEnterpriseName) {
        this.logisticsEnterpriseName = logisticsEnterpriseName;
    }

    public String getIsseAmt() {
        return isseAmt;
    }

    public void setIsseAmt(String isseAmt) {
        this.isseAmt = isseAmt;
    }

    public String getCashedAmount() {
        return cashedAmount;
    }

    public void setCashedAmount(String cashedAmount) {
        this.cashedAmount = cashedAmount;
    }

    private String cashedAmount;
    private long isseDt;
    private long signInDt;
    private long dueDt;
    private String discountInHandAmount;

    private int discounted;
    private String note;

    private String pyerLinkMan;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(int lastStatus) {
        this.lastStatus = lastStatus;
    }

    private String pyeeLinkMan;

    private String pyerLinkPhone;

    public String getPyerLinkPhone() {
        return pyerLinkPhone;
    }

    public void setPyerLinkPhone(String pyerLinkPhone) {
        this.pyerLinkPhone = pyerLinkPhone;
    }

    public String getPyeeLinkPhone() {
        return pyeeLinkPhone;
    }

    public void setPyeeLinkPhone(String pyeeLinkPhone) {
        this.pyeeLinkPhone = pyeeLinkPhone;
    }

    private String pyeeLinkPhone;


    public String getPyerLinkMan() {
        return pyerLinkMan;
    }

    public void setPyerLinkMan(String pyerLinkMan) {
        this.pyerLinkMan = pyerLinkMan;
    }

    public String getPyeeLinkMan() {
        return pyeeLinkMan;
    }

    public void setPyeeLinkMan(String pyeeLinkMan) {
        this.pyeeLinkMan = pyeeLinkMan;
    }

    public String getDiscountInHandAmount() {
        return discountInHandAmount;
    }

    public void setDiscountInHandAmount(String discountInHandAmount) {
        this.discountInHandAmount = discountInHandAmount;
    }

    public String getReceivableNo() {
        return receivableNo;
    }

    public int getDiscounted() {
        return discounted;
    }

    public void setDiscounted(int discounted) {
        this.discounted = discounted;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

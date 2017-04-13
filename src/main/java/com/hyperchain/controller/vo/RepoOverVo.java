package com.hyperchain.controller.vo;

/**
 * Created by liangyue on 2017/4/10.
 */
public class RepoOverVo {

    private String payerRepoBusinessNo;//买家仓储流水号
    private String payeeRepoBusinessNo;//卖家仓储流水号
    private String payerRepoCertNo;    //买家仓单编号
    private String payeeRepoCertNo;    //卖家仓单编号
    private String payerRepoCompany;
    private String payeeRepoCompany;
    private int payerRepoBusiState;
    private int payeeRepoBusiState;
    private long inApplyTime;
    private long outApplyTime;

    public String getPayerRepoBusinessNo() {
        return payerRepoBusinessNo;
    }

    public void setPayerRepoBusinessNo(String payerRepoBusinessNo) {
        this.payerRepoBusinessNo = payerRepoBusinessNo;
    }

    public String getPayeeRepoBusinessNo() {
        return payeeRepoBusinessNo;
    }

    public void setPayeeRepoBusinessNo(String payeeRepoBusinessNo) {
        this.payeeRepoBusinessNo = payeeRepoBusinessNo;
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

    public String getPayerRepoCompany() {
        return payerRepoCompany;
    }

    public void setPayerRepoCompany(String payerRepoCompany) {
        this.payerRepoCompany = payerRepoCompany;
    }

    public String getPayeeRepoCompany() {
        return payeeRepoCompany;
    }

    public void setPayeeRepoCompany(String payeeRepoCompany) {
        this.payeeRepoCompany = payeeRepoCompany;
    }

    public int getPayerRepoBusiState() {
        return payerRepoBusiState;
    }

    public void setPayerRepoBusiState(int payerRepoBusiState) {
        this.payerRepoBusiState = payerRepoBusiState;
    }

    public int getPayeeRepoBusiState() {
        return payeeRepoBusiState;
    }

    public void setPayeeRepoBusiState(int payeeRepoBusiState) {
        this.payeeRepoBusiState = payeeRepoBusiState;
    }

    public long getInApplyTime() {
        return inApplyTime;
    }

    public void setInApplyTime(long inApplyTime) {
        this.inApplyTime = inApplyTime;
    }

    public long getOutApplyTime() {
        return outApplyTime;
    }

    public void setOutApplyTime(long outApplyTime) {
        this.outApplyTime = outApplyTime;
    }
}

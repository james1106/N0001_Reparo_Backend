package com.hyperchain.controller.vo;

import org.springframework.stereotype.Component;

/**
 * Created by liangyue on 2017/4/9.
 */
@Component
public class OrderOverVo {
    private String orderNo;
    private String productName;
    private long productQuantity;
    private String productUnitPrice;
    private String productTotalPrice;
    private String payerRepoName;
    private String payeeRepoName;
    private String payerBank;
    private String payerBankAccount;
    private int payingMethod;
    private long orderGenerateTime;
    private long orderConfirmTime;
    private String payerCompanyName;
    private String payeeCompanyName;
    private int transactionStatus;
    private int repoStatus;
    private int receStatus;
    private int wayBillStatus;

    public String getPayerRepoName() {
        return payerRepoName;
    }

    public void setPayerRepoName(String payerRepoName) {
        this.payerRepoName = payerRepoName;
    }

    public String getPayeeRepoName() {
        return payeeRepoName;
    }

    public void setPayeeRepoName(String payeeRepoName) {
        this.payeeRepoName = payeeRepoName;
    }

    public int getPayingMethod() {
        return payingMethod;
    }

    public void setPayingMethod(int payingMethod) {
        this.payingMethod = payingMethod;
    }

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public int getRepoStatus() {
        return repoStatus;
    }

    public void setRepoStatus(int repoStatus) {
        this.repoStatus = repoStatus;
    }

    public int getReceStatus() {
        return receStatus;
    }

    public void setReceStatus(int receStatus) {
        this.receStatus = receStatus;
    }

    public int getWayBillStatus() {
        return wayBillStatus;
    }

    public void setWayBillStatus(int wayBillStatus) {
        this.wayBillStatus = wayBillStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getOrderGenerateTime() {
        return orderGenerateTime;
    }

    public void setOrderGenerateTime(long orderGenerateTime) {
        this.orderGenerateTime = orderGenerateTime;
    }

    public long getOrderConfirmTime() {
        return orderConfirmTime;
    }

    public void setOrderConfirmTime(long orderConfirmTime) {
        this.orderConfirmTime = orderConfirmTime;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(String productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }

    public String getPayerBankAccount() {
        return payerBankAccount;
    }

    public void setPayerBankAccount(String payerBankAccount) {
        this.payerBankAccount = payerBankAccount;
    }

    public String getPayerCompanyName() {
        return payerCompanyName;
    }

    public void setPayerCompanyName(String payerCompanyName) {
        this.payerCompanyName = payerCompanyName;
    }

    public String getPayeeCompanyName() {
        return payeeCompanyName;
    }

    public void setPayeeCompanyName(String payeeCompanyName) {
        this.payeeCompanyName = payeeCompanyName;
    }
}

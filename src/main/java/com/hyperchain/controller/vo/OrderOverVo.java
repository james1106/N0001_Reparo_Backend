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
    private long productUnitPrice;
    private long productTotalPrice;
    private String payerRepo;
    private String payerBank;
    private String payerBankAccount;
    private String payingMethod;
    private long orderGenerateTime;
    private long orderConfirmTime;
    private String payerAddress;
    private String payeeAddress;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getOrderGenerateTime() {
        return orderGenerateTime;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(String payeeAddress) {
        this.payeeAddress = payeeAddress;
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

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress;
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

    public long getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(long productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public long getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(long productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getPayerRepo() {
        return payerRepo;
    }

    public void setPayerRepo(String payerRepo) {
        this.payerRepo = payerRepo;
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

    public String getPayingMethod() {
        return payingMethod;
    }

    public void setPayingMethod(String payingMethod) {
        this.payingMethod = payingMethod;
    }
}

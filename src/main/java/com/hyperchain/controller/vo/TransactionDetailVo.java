package com.hyperchain.controller.vo;

import org.springframework.stereotype.Component;

/**
 * Created by liangyue on 2017/4/9.
 */
@Component
public class TransactionDetailVo {

    private String orderId;
    private String productName;
    private String payerAddress;
    private String payeeAddress;
    private String payerBank;

    private String payerBankClss;
    private String payerAccount;



    private long productUnitPrice;
    private long productQuantity;
    private long productTotalPrice;

    private String payerRepo;
    private String payeeRepo;
    private String repoCertNo;
    private String repoBusinessNo;
    private String payingMethod;

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    private String orderState;

    public long getOrderGenerateTime() {
        return orderGenerateTime;
    }

    public void setOrderGenerateTime(long orderGenerateTime) {
        this.orderGenerateTime = orderGenerateTime;
    }

    private long orderGenerateTime;





//        List<String> partParams1 = (List<String>) contractResult.getValueMap().get(contractParams[2]);
//        List<String> partParams2 = (List<String>) contractResult.getValueMap().get(contractParams[3]);
//        String payingMethod = (String)contractResult.getValueMap().get(contractParams[4]);
//        String orderState = (String)contractResult.getValueMap().get(contractParams[5]);
//
//        String timeStamp = partParams2.get(3);
//

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }

    public String getPayerBankClss() {
        return payerBankClss;
    }

    public void setPayerBankClss(String payerBankClss) {
        this.payerBankClss = payerBankClss;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }


    public String getPayerRepo() {
        return payerRepo;
    }

    public void setPayerRepo(String payerRepo) {
        this.payerRepo = payerRepo;
    }

    public String getPayeeRepo() {
        return payeeRepo;
    }

    public void setPayeeRepo(String payeeRepo) {
        this.payeeRepo = payeeRepo;
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

    public String getPayingMethod() {
        return payingMethod;
    }

    public void setPayingMethod(String payingMethod) {
        this.payingMethod = payingMethod;
    }

    public long getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(long productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
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

}

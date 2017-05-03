package com.hyperchain.controller.vo;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangyue on 2017/4/9.
 */
@Component
public class TransactionDetailVo {

    private String orderId;
    private String productName;
    private String payerCompanyName;
    private String payeeCompanyName;
    private String payerBank;
    private String payerBankClss;
    private String payerAccount;
    private String productUnitPrice;
    private long productQuantity;
    private String productTotalPrice;
    private String payerRepo;
    private String payeeRepo;
    private int payingMethod;
    private String payerRepoBusinessNo;//买家仓储流水号
    private String payeeRepoBusinessNo;//卖家仓储流水号
    private String payerRepoCertNo;    //买家仓单编号
    private String payeeRepoCertNo;    //卖家仓单编号
    private List<OperationRecordVo> operationRecordVoList;

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

    public List<OperationRecordVo> getOperationRecordVoList() {
        return operationRecordVoList;
    }

    public void setOperationRecordVoList(List<OperationRecordVo> operationRecordVoList) {
        this.operationRecordVoList = operationRecordVoList;
    }

    public int getPayingMethod() {
        return payingMethod;
    }

    public void setPayingMethod(int payingMethod) {
        this.payingMethod = payingMethod;
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

    public String getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(String productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

}

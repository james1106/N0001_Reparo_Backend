package com.hyperchain.controller.vo;

import com.hyperchain.dal.entity.OperationRecord;
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
    private long productUnitPrice;
    private long productQuantity;
    private long productTotalPrice;
    private String payerRepo;
    private String payeeRepo;
    private String repoCertNo;
    private String repoBusinessNo;
    private int payingMethod;
    private List<OperationRecord> operationRecordList;

    public List<OperationRecord> getOperationRecordList() {
        return operationRecordList;
    }

    public void setOperationRecordList(List<OperationRecord> operationRecordList) {
        this.operationRecordList = operationRecordList;
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
